// Verification only: actual emitted engine/Admin SDK and local Firestore transactions.
// R8 score is an explicit test double. No Functions event delivery or real evaluator claim.
const assert = require('node:assert/strict');
const path = require('node:path');
const fs = require('node:fs');
const [root, prefix, output] = process.argv.slice(2);
assert(root && prefix && output && /^[a-z]+$/.test(prefix));
assert.equal(process.env.FIRESTORE_EMULATOR_HOST, '127.0.0.1:8080');
assert.equal(process.env.GCLOUD_PROJECT, 'demo-inkr8-local');
assert(!process.env.GOOGLE_APPLICATION_CREDENTIALS && !process.env.OPENAI_API_KEY);

let externalFetches = 0;
global.fetch = async () => { externalFetches++; throw new Error('External fetch forbidden by local verification'); };
const r8 = require(path.join(root, 'lib/r8/evaluateWithR8.js'));
let doubleCalls = 0;
let score = 80;
r8.evaluateWithR8 = async () => { doubleCalls++; return {finalScore: score, feedback: 'Local test double; no R8 provider invoked', source: 'mock'}; };
const {db} = require(path.join(root, 'lib/firebase/admin.js'));
const {submissionEvaluationEngine} = require(path.join(root, 'lib/submissions/submissionEvaluationEngine.js'));
assert.equal(typeof submissionEvaluationEngine.run, 'function');
const content = 'Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us.';
const cases = [
  {name:'win', myRating:60, theirRating:65, myScore:80, theirScore:70, outcome:'WIN'},
  {name:'loss', myRating:65, theirRating:60, myScore:70, theirScore:80, outcome:'LOSS'},
  {name:'draw', myRating:60, theirRating:60, myScore:75, theirScore:75, outcome:'DRAW'},
  {name:'floor', myRating:0, theirRating:0, myScore:70, theirScore:80, outcome:'LOSS'},
];
const delay = ms => new Promise(resolve => setTimeout(resolve, ms));

(async () => {
  const results = [];
  for (const c of cases) {
    const author = `${prefix}-${c.name}-author`, other = `${prefix}-${c.name}-other`;
    const mine = db.collection('submissions').doc(`${prefix}-${c.name}-mine`);
    const theirs = db.collection('submissions').doc(`${prefix}-${c.name}-candidate`);
    const me = db.collection('users').doc(author), them = db.collection('users').doc(other);
    const user = rating => ({name:'Local test user', rating, merit:1000, meritCap:50000,
      meritHold:0, reputation:0, isPlaced:true, currentlyInRanked:true, rankedWinStreak:0,
      rankedLossStreak:0, submissionsCount:0, totalMeritEarned:0, currentStreak:0,
      recentScores:[], bestScore:0, lastSubmissionDay:0, hasChosenUsername:true});
    await Promise.all([me.set(user(c.myRating)), them.set(user(c.theirRating))]);
    await theirs.set({authorId:other, playmode:'RANKED', status:'EVALUATED', matchStatus:'PENDING',
      timestamp:Date.now(), content, evaluation:{finalScore:c.theirScore, ratingChange:0}});
    await mine.set({authorId:author, playmode:'RANKED', gamemode:'STANDARD', status:'PENDING',
      timestamp:Date.now(), content, wordCount:content.split(/\s+/).length, wordsUsed:[]});
    score = c.myScore;
    await submissionEvaluationEngine.run({data:await mine.get()});
    const deadline = Date.now() + 30000;
    let m;
    do { m = (await mine.get()).data(); if (m.matchStatus === 'MATCHED' || m.status === 'FAILED') break; await delay(100); }
    while (Date.now() < deadline);
    assert.equal(m.status, 'EVALUATED', JSON.stringify(m));
    assert.equal(m.matchStatus, 'MATCHED', JSON.stringify(m));
    const [otherDoc, myUser, theirUser] = await Promise.all([theirs.get(), me.get(), them.get()]);
    const t = otherDoc.data(), u = myUser.data(), v = theirUser.data();
    assert.equal(t.matchStatus, 'MATCHED');
    assert.equal(m.matchResult.opponentId, other);
    assert.equal(t.matchResult.opponentId, author);
    assert.equal(m.matchResult.outcome, c.outcome);
    assert.equal(u.rating, Math.max(0, c.myRating + m.matchResult.ratingChange));
    assert.equal(v.rating, Math.max(0, c.theirRating + t.matchResult.ratingChange));
    assert.equal(m.evaluation.ratingChange, m.matchResult.ratingChange);
    assert.equal(t.evaluation.ratingChange, t.matchResult.ratingChange);
    results.push({case:c.name, myRating:u.rating, theirRating:v.rating,
      myDelta:m.matchResult.ratingChange, theirDelta:t.matchResult.ratingChange,
      myOutcome:m.matchResult.outcome, theirOutcome:t.matchResult.outcome,
      myWinStreak:u.rankedWinStreak, myLossStreak:u.rankedLossStreak,
      theirWinStreak:v.rankedWinStreak, theirLossStreak:v.rankedLossStreak,
      merit:u.merit, hold:u.meritHold, meritEarned:m.evaluation.meritEarned,
      score:m.evaluation.finalScore, isMock:m.evaluation.isMock,
      currentlyInRanked:u.currentlyInRanked, status:m.status});
  }
  assert.equal(doubleCalls, cases.length);
  assert.equal(externalFetches, 0);
  fs.writeFileSync(output, JSON.stringify({project:'demo-inkr8-local', results, doubleCalls,
    externalFetches, scope:'Actual Firestore transactions and compiled engine; R8 double and synthetic CloudEvent'}, null, 2)+'\n');
  await db.terminate();
  console.log(`PASS: ${cases.length} local Firestore match scenarios; ${doubleCalls} explicit R8 double calls; no external fetch`);
})().catch(error => {console.error(error); process.exitCode = 1; db.terminate().catch(()=>{});});
