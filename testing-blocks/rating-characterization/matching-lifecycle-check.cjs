// Production handler and matching with real SDK; explicit scheduling barrier.
// Measures function-promise ownership, not cloud termination or provider delivery.
const assert=require('node:assert/strict'),path=require('node:path'),fs=require('node:fs');
const [root,prefix,output]=process.argv.slice(2);
assert(root&&/^[a-z]+$/.test(prefix)&&output);
assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');
assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');
assert(!process.env.GOOGLE_APPLICATION_CREDENTIALS&&!process.env.OPENAI_API_KEY);
global.fetch=async()=>{throw new Error('External HTTP forbidden');};
require(path.join(root,'lib/r8/evaluateWithR8.js')).evaluateWithR8=async()=>({finalScore:80,feedback:'Explicit R8 double',source:'mock'});
const adapter=require(path.join(root,'lib/firebase/admin.js'));
adapter.db=require(require.resolve('firebase-admin/firestore',{paths:[root]})).getFirestore('matching-lifecycle-'+prefix);
const {db}=adapter,matching=require(path.join(root,'lib/submissions/rankedMatching.js'));
const originalMatch=matching.tryMatchRankedSubmission;
let entered,release;
const enteredPromise=new Promise(r=>entered=r),gate=new Promise(r=>release=r);
matching.tryMatchRankedSubmission=async(...args)=>{entered();await gate;return originalMatch(...args);};
const {submissionEvaluationEngine:engine}=require(path.join(root,'lib/submissions/submissionEvaluationEngine.js'));
const content='Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us.';
async function main(){
  const uid=prefix+'-author',other=prefix+'-candidate',ref=db.collection('submissions').doc(prefix+'-a');
  assert(!(await ref.get()).exists);
  const user={name:'Local lifecycle fixture',rating:60,merit:1000,meritHold:0,meritCap:50000,submissionsCount:0,totalMeritEarned:0,isPlaced:true,currentStreak:0,lastSubmissionDay:0,recentScores:[],rankedWinStreak:0,rankedLossStreak:0,currentlyInRanked:true,reputation:0};
  await db.collection('users').doc(uid).set(user);await db.collection('users').doc(other).set(user);
  await db.collection('submissions').doc(prefix+'-b').set({authorId:other,playmode:'RANKED',gamemode:'STANDARD',status:'EVALUATED',matchStatus:'PENDING',timestamp:Date.now(),evaluation:{finalScore:80}});
  await ref.set({authorId:uid,playmode:'RANKED',gamemodeName:'STANDARD',status:'PENDING',timestamp:Date.now(),content,wordCount:51,wordsUsed:[]});
  let resolved=false;
  const run=engine.run({data:await ref.get()}).then(()=>resolved=true);
  await enteredPromise;await new Promise(r=>setTimeout(r,100));
  const resolvedWhileMatchingBlocked=resolved;
  release();await run;
  // Before version does not await matching. Retain the final observation after
  // release without making that drain substitute the promise-ownership assertion.
  for(let i=0;i<100&&(await ref.get()).get('matchStatus')!=='MATCHED';i++)await new Promise(r=>setTimeout(r,50));
  const snap=await ref.get();
  const row={name:'trigger owns the matching promise',resolvedWhileMatchingBlocked,matchStatus:snap.get('matchStatus'),passed:!resolvedWhileMatchingBlocked&&snap.get('matchStatus')==='MATCHED'};
  fs.writeFileSync(output,JSON.stringify({rows:[row],passed:Number(row.passed),failed:Number(!row.passed),scope:'Compiled genuine trigger/matching and real Firestore SDK; R8/CloudEvent and matching scheduling barrier are explicit doubles. No cloud-lifecycle claim.'},null,2)+'\n');
  await db.terminate();if(!row.passed)process.exitCode=1;
}
main().catch(e=>{console.error(e);process.exitCode=1;});
