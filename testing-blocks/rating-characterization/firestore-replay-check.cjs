// Characterization, not acceptance: real Admin SDK/Firestore transactions.
// R8 and CloudEvent delivery are doubles. Race scheduling is a test-only barrier.
const assert = require('node:assert/strict');
const path = require('node:path');
const fs = require('node:fs');
const [root, prefix, output] = process.argv.slice(2);
assert(root && prefix && output && /^[a-z]+$/.test(prefix));
assert.equal(process.env.FIRESTORE_EMULATOR_HOST, '127.0.0.1:8080');
assert.equal(process.env.GCLOUD_PROJECT, 'demo-inkr8-local');
assert(!process.env.GOOGLE_APPLICATION_CREDENTIALS && !process.env.OPENAI_API_KEY);
let externalFetches = 0, doubleCalls = 0;
global.fetch = async () => { externalFetches++; throw new Error('External fetch forbidden'); };
const r8 = require(path.join(root, 'lib/r8/evaluateWithR8.js'));
r8.evaluateWithR8 = async () => {
  doubleCalls++;
  return {finalScore:80, feedback:'Explicit local R8 double', source:'mock'};
};
const {db} = require(path.join(root, 'lib/firebase/admin.js'));
const {submissionEvaluationEngine:engine} = require(path.join(root, 'lib/submissions/submissionEvaluationEngine.js'));
const content = 'Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us.';
const delay = ms => new Promise(resolve => setTimeout(resolve, ms));
const user = () => ({name:'Synthetic replay user', rating:60, merit:1000, meritCap:50000,
  meritHold:0, reputation:0, isPlaced:true, currentlyInRanked:true, rankedWinStreak:0,
  rankedLossStreak:0, submissionsCount:0, totalMeritEarned:0, currentStreak:0,
  recentScores:[], bestScore:0, lastSubmissionDay:0, hasChosenUsername:true});
async function seed(name, mode='PRACTICE') {
  const uid=`${prefix}-${name}-author`, ref=db.collection('submissions').doc(`${prefix}-${name}`);
  const u=db.collection('users').doc(uid);
  assert(!(await ref.get()).exists && !(await u.get()).exists, 'Use a fresh prefix; do not overwrite data');
  await u.set(user());
  await ref.set({authorId:uid, playmode:mode, gamemode:'STANDARD', status:'PENDING',
    timestamp:Date.now(), content, wordCount:content.split(/\s+/).length, wordsUsed:[]});
  return {ref,u,uid};
}
async function until(read, predicate) {
  const deadline=Date.now()+30000;
  do { const value=await read(); if(predicate(value))return value; await delay(50); }
  while(Date.now()<deadline);
  throw new Error('Local observation deadline exceeded');
}
async function userState(s) {
  const u=(await s.u.get()).data();
  return {count:u.submissionsCount, merit:u.merit, earned:u.totalMeritEarned,
    rewardDocuments:(await s.u.collection('meritTransactions').get()).size,
    rating:u.rating, winStreak:u.rankedWinStreak};
}
(async () => {
  const results=[];
  for(const concurrent of [false,true]) {
    const s=await seed(concurrent?'practiceparallel':'practicereplay');
    const event={data:await s.ref.get()}; // Same created snapshot, delivered twice.
    if(concurrent) await Promise.all([engine.run(event),engine.run(event)]);
    else { await engine.run(event); await engine.run(event); }
    const state=await userState(s);
    assert.equal(state.count,2); assert.equal(state.rewardDocuments,2);
    assert.equal((await s.ref.get()).data().status,'EVALUATED');
    results.push({case:concurrent?'concurrent Practice replay':'sequential Practice replay',
      observed:state, defect:'One submission receives two rewards/count increments'});
  }

  const candidate=await seed('aa-candidate','RANKED');
  await candidate.ref.update({status:'EVALUATED',matchStatus:'PENDING',timestamp:Date.now()-5000,
    evaluation:{finalScore:70,ratingChange:0}});
  const a=await seed('racea','RANKED'), c=await seed('raceb','RANKED');
  const createdA={data:await a.ref.get()}, createdC={data:await c.ref.get()};
  const originalTransaction=db.runTransaction.bind(db);
  let arrivals=0, complete=0, release;
  const barrier=new Promise(resolve=>{release=resolve;});
  const timer=setTimeout(()=>release(),10000);
  db.runTransaction=async function(callback,...options) {
    // Both production queries must select their candidate before either match writes.
    if(callback.toString().includes('candidateAuthorRef')) {
      arrivals++; if(arrivals===2)release(); await barrier;
      const value=await originalTransaction(callback,...options); complete++; return value;
    }
    return originalTransaction(callback,...options);
  };
  try {
    await Promise.all([engine.run(createdA),engine.run(createdC)]);
    await until(async()=>complete,n=>n===2);
  } finally { clearTimeout(timer); db.runTransaction=originalTransaction; }
  assert.equal(arrivals,2);
  const aa=(await a.ref.get()).data(), cc=(await c.ref.get()).data(), target=(await candidate.ref.get()).data();
  assert.equal(aa.matchResult.opponentId,candidate.uid);
  assert.equal(cc.matchResult.opponentId,candidate.uid);
  assert([a.uid,c.uid].includes(target.matchResult.opponentId));
  results.push({case:'two matchers select one candidate',
    observed:{twoClaims:true,reciprocalResults:1,ratings:(await Promise.all([userState(a),userState(c)])).map(x=>x.rating).sort(),
      candidateRating:(await userState(candidate)).rating,candidateLossStreak:(await candidate.u.get()).data().rankedLossStreak},
    defect:'Candidate is used twice; its reciprocal result retains only one opponent'});

  const before=await userState(a), old=(await a.ref.get()).data();
  // The guard skips an EVALUATED snapshot, but duplicate created events retain PENDING.
  await engine.run({data:await a.ref.get()});
  assert.deepEqual(await userState(a),before);
  results.push({case:'evaluated snapshot is skipped',observed:{unchanged:true},defect:null});
  await engine.run(createdA);
  await until(async()=>(await a.ref.get()).data(),v=>v.matchStatus==='PENDING');
  const after=await userState(a), late=(await a.ref.get()).data();
  assert.equal(after.count,before.count+1); assert.equal(after.rewardDocuments,before.rewardDocuments+1);
  assert.deepEqual(late.matchResult,old.matchResult);
  results.push({case:'Ranked replay after match',observed:{additionalRewards:after.rewardDocuments-before.rewardDocuments,
    additionalCount:after.count-before.count,matchStatus:late.matchStatus,staleMatchResultRetained:true,rating:after.rating},
    defect:'Replay rewards again and resets matchStatus while retaining the previous result'});
  assert.equal(externalFetches,0); assert.equal(doubleCalls,7);
  fs.writeFileSync(output,JSON.stringify({project:'demo-inkr8-local',results,doubleCalls,externalFetches,
    scope:'Real Firestore and transactions; synthetic CloudEvents, R8 double, test-only race barrier; defects are preserved characterization'},null,2)+'\n');
  await db.terminate(); console.log('PASS characterization: 5 scenarios; reproduced defects are not acceptance');
})().catch(error=>{console.error(error);process.exitCode=1;db.terminate().catch(()=>{});});
