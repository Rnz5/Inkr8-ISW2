// Acceptance probes over the existing student engine. Do not change this into
// assertions accepting the known duplicate-reward/candidate-race defects.
// Real Admin SDK/Firestore; R8 and CloudEvent delivery are explicit doubles.
const assert = require('node:assert/strict');
const path = require('node:path');
const fs = require('node:fs');
const [root, prefix, output] = process.argv.slice(2);
const only=process.argv[5];
assert(root && /^[a-z]+$/.test(prefix) && output);
assert(only===undefined||only==='hold');
assert.equal(process.env.FIRESTORE_EMULATOR_HOST, '127.0.0.1:8080');
assert.equal(process.env.GCLOUD_PROJECT, 'demo-inkr8-local');
assert(!process.env.GOOGLE_APPLICATION_CREDENTIALS && !process.env.OPENAI_API_KEY);
let externalFetches=0;
global.fetch=async()=>{externalFetches++;throw new Error('External HTTP forbidden');};
require(path.join(root,'lib/r8/evaluateWithR8.js')).evaluateWithR8=async()=>({
  finalScore:80,feedback:'Explicit R8 double; no provider invoked',source:'mock'});
const adapter=require(path.join(root,'lib/firebase/admin.js'));
// Explicit test-only destination binding to another REAL database in the same
// demo emulator: prior fixtures cannot enter candidate selection; no data deleted.
const database='core-invariants-'+prefix;
adapter.db=require(require.resolve('firebase-admin/firestore',{paths:[root]})).getFirestore(database);
const {db}=adapter;
// The original engine starts match transactions without awaiting them. Track
// genuine transaction promises so test teardown cannot terminate their client.
const nativeTransaction=db.runTransaction.bind(db),pendingTransactions=new Set();
db.runTransaction=(...args)=>{
  const p=nativeTransaction(...args);pendingTransactions.add(p);
  p.then(()=>pendingTransactions.delete(p),()=>pendingTransactions.delete(p));return p;
};
const {submissionEvaluationEngine:engine}=require(path.join(root,'lib/submissions/submissionEvaluationEngine.js'));
const content='Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us.';
const results=[];
const delay=ms=>new Promise(resolve=>setTimeout(resolve,ms));
function check(name, actual, expected) {
  let error=null;
  try {assert.deepEqual(actual,expected);} catch(e) {error=e.message;}
  results.push({name,passed:error===null,actual,expected,error});
}
async function seed(name,mode='PRACTICE',placed=true) {
  const uid=`${prefix}-${name}-author`,ref=db.collection('submissions').doc(`${prefix}-${name}`);
  const u=db.collection('users').doc(uid);
  assert(!(await ref.get()).exists && !(await u.get()).exists,'Fresh test prefix required');
  await u.set({name:'Local invariant fixture',rating:60,merit:1000,meritHold:0,meritCap:50000,
    submissionsCount:0,totalMeritEarned:0,isPlaced:placed,placementMatchesPlayed:0,totalPlacementScore:0,
    rankedWinStreak:0,rankedLossStreak:0,currentStreak:0,lastSubmissionDay:0,recentScores:[],
    currentlyInRanked:true,reputation:0,bestScore:0,hasChosenUsername:true});
  await ref.set({authorId:uid,playmode:mode,gamemode:'STANDARD',status:'PENDING',timestamp:Date.now(),
    content,wordCount:51,wordsUsed:[]});
  return {uid,ref,u,event:{data:await ref.get()}};
}
async function state(s) {
  const u=(await s.u.get()).data(), d=(await s.ref.get()).data();
  return {count:u.submissionsCount,merit:u.merit,hold:u.meritHold,earned:u.totalMeritEarned,
    rewardDocuments:(await s.u.collection('meritTransactions').get()).size,rating:u.rating,
    win:u.rankedWinStreak,loss:u.rankedLossStreak,placement:u.placementMatchesPlayed,
    placementScore:u.totalPlacementScore,status:d.status,matchStatus:d.matchStatus,
    matchResult:d.matchResult??null,evaluation:d.evaluation??null};
}
async function until(predicate) {
  const deadline=Date.now()+30000;
  while(!(await predicate())) {assert(Date.now()<deadline,'Test observation deadline');await delay(50);}
}
(async()=>{
  if(only==='hold') {
    const s=await seed('cap-hold');
    await s.u.update({merit:49900});
    await engine.run(s.event);await engine.run(s.event);
    const actual=await state(s);
    check('cap/hold: one reward split, even when replay adds no liquid ledger entry',
      {count:actual.count,merit:actual.merit,hold:actual.hold,earned:actual.earned,rewardDocuments:actual.rewardDocuments},
      {count:1,merit:50000,hold:205,earned:305,rewardDocuments:1});
  } else {
  for(const concurrent of [false,true]) {
    const s=await seed(concurrent?'parallel':'sequential');
    if(concurrent)await Promise.all([engine.run(s.event),engine.run(s.event)]);
    else {await engine.run(s.event);await engine.run(s.event);}
    const actual=await state(s);
    check(`${concurrent?'parallel':'sequential'}: one reward/count per submission`,
      {count:actual.count,rewardDocuments:actual.rewardDocuments,merit:actual.merit,earned:actual.earned,hold:actual.hold},
      {count:1,rewardDocuments:1,merit:1305,earned:305,hold:0});
  }
  const candidate=await seed('aa-candidate','RANKED');
  await candidate.ref.update({status:'EVALUATED',matchStatus:'PENDING',timestamp:Date.now()-5000,
    evaluation:{finalScore:70,ratingChange:0}});
  const a=await seed('racea','RANKED'), c=await seed('raceb','RANKED');
  const original=db.runTransaction.bind(db);let arrivals=0,completed=0,release;
  const barrier=new Promise(resolve=>{release=resolve;});
  const timer=setTimeout(()=>release(),10000);
  db.runTransaction=async(callback,...options)=>{
    // Test scheduling only: the two production queries select before either tx starts.
    if(callback.toString().includes('candidateAuthorRef')) {
      arrivals++;if(arrivals===2)release();await barrier;
      const value=await original(callback,...options);completed++;return value;
    }
    return original(callback,...options);
  };
  try {await Promise.all([engine.run(a.event),engine.run(c.event)]);await until(async()=>completed>=2);}
  finally {clearTimeout(timer);db.runTransaction=original;}
  const aa=await state(a),cc=await state(c),target=await state(candidate);
  const claims=[aa,cc].filter(x=>x.matchStatus==='MATCHED'&&x.matchResult?.opponentId===candidate.uid).length;
  const reciprocal=[{s:a,v:aa},{s:c,v:cc}].filter(x=>
    x.v.matchResult?.opponentId===candidate.uid&&target.matchResult?.opponentId===x.s.uid).length;
  check('race: one candidate, one reciprocal pair',{claims,reciprocal},{claims:1,reciprocal:1});
  check('race: candidate rating/streak change once',{rating:target.rating,loss:target.loss},{rating:54,loss:1});
  const old=await state(a);
  await engine.run({data:await a.ref.get()});
  check('fresh EVALUATED snapshot is skipped',await state(a),old);
  await engine.run(a.event);await delay(500);
  check('created-event replay preserves settled effects and pair',await state(a),old);
  // Placement increments are asserted independently of any match result.
  const p=await seed('placement','RANKED',false);
  await engine.run(p.event);await engine.run(p.event);
  const placement=await state(p);
  check('placement: one evaluation contributes once',
    {count:placement.count,played:placement.placement,totalScore:placement.placementScore,rewards:placement.rewardDocuments},
    {count:1,played:1,totalScore:80,rewards:1});
  }
  assert.equal(externalFetches,0);
  await delay(500);
  await until(async()=>pendingTransactions.size===0);
  const report={project:'demo-inkr8-local',database,results,passed:results.filter(x=>x.passed).length,
    failed:results.filter(x=>!x.passed).length,externalFetches,
    scope:'Actual Firestore transactions; synthetic created CloudEvents, fixed-score R8 double, test-only race barrier. No delivery/idempotency acceptance from static checks.'};
  fs.writeFileSync(output,JSON.stringify(report,null,2)+'\n');
  await db.terminate();console.log(`Acceptance probes: ${report.passed} PASS, ${report.failed} FAIL`);
  if(report.failed)process.exitCode=1;
})().catch(e=>{console.error(e);process.exitCode=1;db.terminate().catch(()=>{});});
