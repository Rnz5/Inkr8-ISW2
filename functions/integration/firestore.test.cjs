const {test, before, beforeEach, after} = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
if (!process.env.FIRESTORE_EMULATOR_HOST) throw Error('Use a local Firestore emulator; never run these tests against production.');
const {initializeApp, deleteApp} = require('firebase-admin/app');
const {getFirestore} = require('firebase-admin/firestore');
const {initializeTestEnvironment, assertFails, assertSucceeds} = require('@firebase/rules-unit-testing');
const {doc, getDoc, setDoc, collection, query, where, orderBy, getDocs} = require('firebase/firestore');
const {FirebaseUserRepository} = require('../lib/data/repository/firebaseUserRepository');
const {FirebaseGameRepository} = require('../lib/data/repository/firebaseGameRepository');
const {FirebaseSeasonRepository} = require('../lib/data/repository/firebaseSeasonRepository');
const {FirebaseMatchRepository} = require('../lib/data/repository/firebaseMatchRepository');
const {FirebaseContentRepository} = require('../lib/data/repository/firebaseContentRepository');
const {seasonAt, SEASON_ANCHOR_MS, SEASON_DURATION_MS} = require('../lib/domain/policy/seasonPolicy');
const app = initializeApp({projectId:'demo-inkr8'}, 'integration');
const db = getFirestore(app);
let now = SEASON_ANCHOR_MS, env;
const clock = {now: () => now};
const users = new FirebaseUserRepository(db, clock), games = new FirebaseGameRepository(db, clock);
const seasons = new FirebaseSeasonRepository(db), matches = new FirebaseMatchRepository(db, clock);
const challenge = {words: ['uno','dos','tres','cuatro'].map(text => ({text, definition:'', sentence:''})), topic:null, theme:null};
const content = Array(50).fill('palabra').join(' ');
const idA = 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', idB = 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb';
before(async () => {
  const [host, port] = process.env.FIRESTORE_EMULATOR_HOST.split(':');
  env = await initializeTestEnvironment({projectId:'demo-inkr8', firestore:{host, port:Number(port), rules:fs.readFileSync(path.resolve(__dirname, '../../firestore.rules'), 'utf8')}});
});
beforeEach(async () => { await env.clearFirestore(); now = SEASON_ANCHOR_MS; });
after(async () => { await env.cleanup(); await db.terminate(); await deleteApp(app); });
async function start(id = idA, uid = 'alice', mode = 'RANKED') {
  await users.initialize(uid, uid, null, seasonAt(now));
  return games.start(id, uid, mode, 'STANDARD', challenge, seasonAt(now), now);
}
async function evaluated(id, uid, score) {
  const game = await start(id, uid);
  await games.submit(id, uid, content);
  const pending = await games.get(id);
  await games.complete(pending, {score, feedback:'Feedback'}, seasonAt(now));
  return game;
}
test('concurrent duplicate entry debits Ranked exactly once and enforces ownership', async () => {
  await users.initialize('alice','Alice',null,seasonAt(now));
  await Promise.all(Array.from({length:5}, () => games.start(idA,'alice','RANKED','STANDARD',challenge,seasonAt(now),now)));
  assert.equal((await db.doc('users/alice').get()).get('merit'), 900);
  assert.equal((await db.collection('submissions').get()).size, 1);
  await users.initialize('bob','Bob',null,seasonAt(now));
  await assert.rejects(() => games.start(idA,'bob','RANKED','STANDARD',challenge,seasonAt(now),now), /otra partida/);
});
test('insufficient balance creates no game and invalid submission remains a draft', async () => {
  await users.initialize('alice','Alice',null,seasonAt(now));
  await db.doc('users/alice').update({merit:10});
  await assert.rejects(() => games.start(idA,'alice','RANKED','STANDARD',challenge,seasonAt(now),now), /insuficiente/);
  assert.equal((await db.collection('submissions').get()).size,0);
  await games.start(idA,'alice','PRACTICE','STANDARD',challenge,seasonAt(now),now);
  await assert.rejects(() => games.submit(idA,'alice','corto'), /Escribe/);
  assert.equal((await games.get(idA)).status,'DRAFT');
});
test('concurrent evaluation settlement awards one reward and immutable submissions reject edits', async () => {
  await start(); await games.submit(idA,'alice',content);
  await games.submit(idA,'alice',content);
  await assert.rejects(() => games.submit(idA,'alice',content+' edit'), /ya fue enviada/);
  const pending = await games.get(idA);
  await Promise.all(Array.from({length:5}, () => games.complete(pending,{score:60,feedback:'OK'},seasonAt(now))));
  assert.equal((await db.doc('users/alice').get()).get('merit'),1095);
  assert.equal((await games.get(idA)).evaluation.meritEarned,195);
});
test('failed evaluation retry uses the same paid game', async () => {
  await start(); await games.submit(idA,'alice',content); await games.fail(idA,'Retry');
  await games.retry(idA,'alice'); await games.retry(idA,'alice');
  assert.equal((await games.get(idA)).status,'PENDING');
  assert.equal((await db.doc('users/alice').get()).get('merit'),900);
  await assert.rejects(() => games.retry(idA,'bob'), /no disponible/);
});
test('late evaluation grants Merit but no rating in the new season', async () => {
  await start(); await games.submit(idA,'alice',content);
  await db.doc('users/alice').update({rating:90,league:4});
  const oldGame = await games.get(idA);
  now += SEASON_DURATION_MS;
  await games.complete(oldGame,{score:90,feedback:'OK'},seasonAt(SEASON_ANCHOR_MS));
  const user = await db.doc('users/alice').get();
  assert.equal(user.get('rating'),0); assert.equal(user.get('seasonIndex'),1); assert.equal(user.get('merit'),1507);
  assert.equal((await db.doc('seasons/s0/members/alice').get()).get('rating'),90);
  assert.notEqual((await db.doc(`submissions/${idA}`).get()).get('matchStatus'),'PENDING');
});
test('simultaneous matchmaking settles both players once and never crosses a season', async () => {
  await evaluated(idA,'alice',80); await evaluated(idB,'bob',60);
  await Promise.all([matches.match(idA,seasonAt(now)), matches.match(idB,seasonAt(now)), matches.match(idA,seasonAt(now))]);
  assert.equal((await db.doc('users/alice').get()).get('rating'),4);
  assert.equal((await db.doc('users/bob').get()).get('rating'),0);
  assert.equal((await db.doc(`submissions/${idA}`).get()).get('matchStatus'),'MATCHED');
  await matches.match(idA,seasonAt(now));
  assert.equal((await db.doc('users/alice').get()).get('rating'),4);
  now += SEASON_DURATION_MS;
  await seasons.resetAll(seasonAt(now));
  await matches.match(idA,seasonAt(SEASON_ANCHOR_MS));
  assert.equal((await db.doc('users/alice').get()).get('rating'),0);
});
test('R8 fallback waits exactly 48 hours and settles concurrent retries only once', async () => {
  await evaluated(idA,'alice',80);
  now += 48 * 60 * 60 * 1000 - 1;
  await matches.recover(seasonAt(now));
  assert.equal((await db.doc(`submissions/${idA}`).get()).get('matchStatus'),'PENDING');
  assert.equal((await db.doc('users/alice').get()).get('rating'),0);
  now += 1;
  await Promise.all([matches.match(idA,seasonAt(now)), matches.recover(seasonAt(now)), matches.match(idA,seasonAt(now))]);
  const settled = await db.doc(`submissions/${idA}`).get();
  assert.equal(settled.get('matchStatus'),'MATCHED');
  assert.equal(settled.get('match').opponentScore,65);
  assert.equal(settled.get('match').outcome,'WIN');
  assert.equal((await db.doc('users/alice').get()).get('rating'),4);
  await matches.recover(seasonAt(now));
  assert.equal((await db.doc('users/alice').get()).get('rating'),4);
  await matches.match('missing-game',seasonAt(now));
});

test('recovery expires old pending games and retries current games without crossing seasons', async () => {
  await evaluated(idA,'alice',80);
  const oldMerit = (await db.doc('users/alice').get()).get('merit');
  now += SEASON_DURATION_MS;
  await evaluated(idB,'bob',70);
  await matches.recover(seasonAt(now));
  assert.equal((await db.doc(`submissions/${idA}`).get()).get('matchStatus'),'EXPIRED');
  assert.equal((await db.doc(`submissions/${idB}`).get()).get('matchStatus'),'PENDING');
  assert.equal((await db.doc('users/alice').get()).get('merit'),oldMerit);
  assert.equal((await db.doc('users/bob').get()).get('rating'),0);
  now += 48 * 60 * 60 * 1000;
  await matches.recover(seasonAt(now));
  assert.equal((await db.doc(`submissions/${idA}`).get()).get('matchStatus'),'EXPIRED');
  assert.equal((await db.doc(`submissions/${idB}`).get()).get('matchStatus'),'MATCHED');
  assert.equal((await db.doc('users/bob').get()).get('rating'),4);
});

test('global reset resumes past 500 users, preserves new scores and leaves Merit intact', {timeout:180000}, async () => {
  const writer = db.bulkWriter();
  for (let i=0;i<510;i++) writer.set(db.doc(`users/u${String(i).padStart(4,'0')}`),{name:'Offline',rating:93,merit:1234,seasonIndex:0});
  writer.set(db.doc('users/active'),{name:'Active',rating:7,merit:567,seasonIndex:1});
  writer.set(db.doc('users/legacy'),{name:'Legacy',rating:60,merit:800});
  await writer.close(); now += SEASON_DURATION_MS;
  await seasons.resetAll(seasonAt(now));
  assert.equal((await db.doc('seasonResetJobs/s1').get()).get('complete'),false);
  await seasons.resetAll(seasonAt(now));
  assert.equal((await db.doc('seasonResetJobs/s1').get()).get('complete'),true);
  const all = await db.collection('users').get();
  for (const user of all.docs) {
    assert.equal(user.get('rating'),user.id === 'active' ? 7 : 0);
    if (user.id.startsWith('u')) assert.equal(user.get('merit'),1234);
  }
  await db.doc('users/u0000').update({rating:4});
  await seasons.resetAll(seasonAt(now));
  assert.equal((await db.doc('users/u0000').get()).get('rating'),4);
});
test('daily word is stable and topic writing supports the existing topic schema', async () => {
  for (let i=0;i<4;i++) await db.doc(`words/${i}`).set({word:`word${i}`,definition:'Definition',sentence:'Example',isActive:true});
  await db.doc('topics/a').set({name:'Nature',themeId:'a'}); await db.doc('themes/a').set({name:'Life'});
  const repository = new FirebaseContentRepository(db);
  const first = await repository.dailyWord(100);
  await db.doc('words/0').delete();
  assert.deepEqual(await repository.dailyWord(100),first);
  const onTopic = await repository.challenge('ON_TOPIC');
  assert.equal(onTopic.words.length,2); assert.equal(onTopic.topic,'Nature'); assert.equal(onTopic.theme,'Life');
});
test('rules allow only own reads, deny client economy writes and block closed accounts', async () => {
  await start();
  const alice = env.authenticatedContext('alice').firestore(), bob = env.authenticatedContext('bob').firestore();
  const anonymous = env.unauthenticatedContext().firestore();
  await assertSucceeds(getDoc(doc(alice,'users/alice')));
  await assertSucceeds(getDoc(doc(alice,`submissions/${idA}`)));
  await assertSucceeds(getDocs(query(collection(alice,'submissions'),where('authorId','==','alice'),where('schemaVersion','==',2),orderBy('timestamp','desc'))));
  await assertFails(getDoc(doc(bob,'users/alice')));
  await assertFails(getDoc(doc(anonymous,'users/alice')));
  await assertFails(setDoc(doc(alice,'users/alice'),{merit:99999},{merge:true}));
  await assertFails(setDoc(doc(alice,'submissions/fake'),{authorId:'alice',status:'EVALUATED'}));
  await db.doc('users/alice').update({accountClosed:true});
  await assertFails(getDoc(doc(alice,'users/alice')));
  await assert.rejects(() => users.initialize('alice','Alice',null,seasonAt(now)), /cerrada/);
});
