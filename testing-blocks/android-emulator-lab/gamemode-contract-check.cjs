// Receives the document ID captured by the REAL Android repository. Uses its
// wire fields in a new local fixture; only content/user identity are adapted to
// reach the genuine engine's quality guard. Original captured document retained.
const assert=require('node:assert/strict'),fs=require('node:fs'),path=require('node:path');
const [root,input,output]=process.argv.slice(2);
assert(root&&input&&output);
assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');
assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');
assert(!process.env.GOOGLE_APPLICATION_CREDENTIALS&&!process.env.OPENAI_API_KEY);
let captured=null;
global.fetch=async()=>{throw new Error('External HTTP forbidden');};
require(path.join(root,'lib/r8/evaluateWithR8.js')).evaluateWithR8=async(params)=>{
  captured={gamemode:params.gamemode,themeName:params.themeName,topicName:params.topicName};
  return {finalScore:80,feedback:'Explicit R8 double',source:'mock'};
};
const {db}=require(path.join(root,'lib/firebase/admin.js'));
const {submissionEvaluationEngine:engine}=require(path.join(root,'lib/submissions/submissionEvaluationEngine.js'));
(async()=>{
  const wire=JSON.parse(fs.readFileSync(input,'utf8'));
  assert.equal(wire.project,'demo-inkr8-local');assert.equal(wire.expectedMode,'ON_TOPIC');
  const original=await db.collection('submissions').doc(wire.submissionId).get();
  assert(original.exists);
  const fields=original.data(),id='lab-core-mode-engine-'+Date.now(),uid=id+'-author';
  const ref=db.collection('submissions').doc(id);
  assert(!(await ref.get()).exists);
  await db.collection('users').doc(uid).set({id:uid,name:'Local mode fixture',merit:1000,
    meritCap:50000,meritHold:0,rating:0,isPlaced:false,currentStreak:0,lastSubmissionDay:0,
    recentScores:[],submissionsCount:0,totalMeritEarned:0});
  const content='Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us.';
  await ref.set({...fields,id,authorId:uid,content,wordCount:51,timestamp:Date.now()});
  await engine.run({data:await ref.get()});
  assert(captured,'The genuine engine must reach the R8 invocation');
  const stored=(await ref.get()).data();
  const report={project:wire.project,originalAndroidDocumentId:wire.submissionId,
    fields:{gamemodeName:fields.gamemodeName,gamemode:fields.gamemode??null,
      topicId:fields.topicId,themeId:fields.themeId},r8Arguments:captured,
    expectedGamemode:wire.expectedMode,modePreserved:captured.gamemode===wire.expectedMode,
    status:stored.status,meritEarned:stored.evaluation?.meritEarned,
    scope:'Actual Android wire payload and genuine compiled engine; synthetic CloudEvent and R8 function double, no automatic delivery/provider validation'};
  fs.writeFileSync(output,JSON.stringify(report,null,2)+'\n');
  await db.terminate();
  assert.equal(captured.gamemode,wire.expectedMode,'Android ON_TOPIC must reach the evaluator unchanged');
})().catch(e=>{console.error(e);process.exitCode=1;db.terminate().catch(()=>{});});
