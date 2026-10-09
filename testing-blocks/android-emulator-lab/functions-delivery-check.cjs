// Actual emulator delivery and genuine engine/R8 SDK, with a local HTTP evaluator double.
const assert=require('node:assert/strict'),fs=require('node:fs'),path=require('node:path');
const [root,output]=process.argv.slice(2);
assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');
assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');
assert(!process.env.GOOGLE_APPLICATION_CREDENTIALS && !process.env.OPENAI_API_KEY);
const {db}=require(path.join(root,'lib/firebase/admin.js'));
const nativeFetch=global.fetch;
global.fetch=(url,...args)=>{
  assert(['http://127.0.0.1:9099','http://127.0.0.1:5001','http://127.0.0.1:5010'].includes(new URL(String(url)).origin));
  return nativeFetch(url,...args);
};
const delay=ms=>new Promise(r=>setTimeout(r,ms));
(async()=>{
  const signed=await fetch('http://127.0.0.1:9099/identitytoolkit.googleapis.com/v1/accounts:signUp?key=local-fixture',
    {method:'POST',headers:{'content-type':'application/json'},body:JSON.stringify({returnSecureToken:true})});
  assert(signed.ok);const auth=await signed.json();
  const token=JSON.parse(Buffer.from(auth.idToken.split('.')[1],'base64url'));
  assert.equal(token.aud,'demo-inkr8-local'); // Never print or save the token.
  const uid=auth.localId;
  const user=db.collection('users').doc(uid);
  await user.set({name:'Synthetic delivery user',merit:10000,meritCap:50000,meritHold:0,rating:60,
    isPlaced:true,currentlyInRanked:false,savedSubmissionsCount:0,submissionsCount:0,currentStreak:0,
    totalMeritEarned:0,lastSubmissionDay:0,recentScores:[]});
  const content='Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us.';
  const doc=db.collection('submissions').doc();
  const before=(await (await fetch('http://127.0.0.1:5010/fixture-metrics')).json()).requests;
  await doc.set({id:doc.id,authorId:uid,playmode:'PRACTICE',gamemode:'STANDARD',status:'PENDING',
    content,timestamp:Date.now(),wordCount:51,wordsUsed:[],isSaved:false});
  const deadline=Date.now()+90000;let result;
  do {result=(await doc.get()).data();if(['EVALUATED','FAILED'].includes(result.status))break;await delay(100);}
  while(Date.now()<deadline);
  assert.equal(result.status,'EVALUATED');assert.equal(result.evaluation.finalScore,80);
  assert.equal(result.evaluation.feedback,'Explicit local transport fixture. No provider called.');
  const evaluated=(await user.get()).data();assert.equal(evaluated.submissionsCount,1);
  const requests=(await (await fetch('http://127.0.0.1:5010/fixture-metrics')).json()).requests-before;
  assert(requests>=1);
  const called=await fetch('http://127.0.0.1:5001/demo-inkr8-local/us-central1/applyMeritAction',{
    method:'POST',headers:{'content-type':'application/json',authorization:'Bearer '+auth.idToken},
    body:JSON.stringify({data:{action:'SAVE_SUBMISSION',submissionId:doc.id}})});
  assert(called.ok);const reply=await called.json();assert(!reply.error);
  const saved=(await user.get()).data();assert.equal(saved.merit,evaluated.merit-2000);
  assert.equal(saved.savedSubmissionsCount,1);assert.equal((await doc.get()).data().isSaved,true);
  fs.writeFileSync(output,JSON.stringify({project:'demo-inkr8-local',delivery:'actual Firestore-created event via Functions emulator',
    evaluated:{score:result.evaluation.finalScore,sourceField:result.evaluation.source,submissionsCount:evaluated.submissionsCount},
    callable:{actualEndpoint:true,costObserved:2000,savedCount:saved.savedSubmissionsCount},
    r8TransportDoubleRequests:requests,realProvider:false,auth:'actual emulator anonymous identity; no Google OAuth',
    noSyntheticEngineRun:true},null,2)+'\n');
  await db.terminate();console.log('PASS: automatic local event delivery, evaluation via HTTP double, authenticated callable');
})().catch(e=>{console.error(e);process.exitCode=1;db.terminate().catch(()=>{});});
