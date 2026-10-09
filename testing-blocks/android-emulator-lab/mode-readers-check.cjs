// Real named Firestore databases and compiled engine/stats; R8 invocation is a double.
const assert=require('node:assert/strict'),fs=require('node:fs'),path=require('node:path');
const [root,prefix,output]=process.argv.slice(2);
assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');
assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');
assert(!process.env.GOOGLE_APPLICATION_CREDENTIALS&&!process.env.OPENAI_API_KEY);
global.fetch=async()=>{throw new Error('External HTTP forbidden');};
let captured;
require(path.join(root,'lib/r8/evaluateWithR8.js')).evaluateWithR8=async p=>{
  captured=p.gamemode;return {finalScore:80,feedback:'Explicit mode R8 double',source:'mock'};
};
const adapter=require(path.join(root,'lib/firebase/admin.js'));
const {getFirestore}=require('firebase-admin/firestore');
const engine=require(path.join(root,'lib/submissions/submissionEvaluationEngine.js')).submissionEvaluationEngine;
const stats=require(path.join(root,'lib/stats/dailyStatsSnapshot.js')).dailyStatsSnapshot;
const cases=[];
for(const mode of ['STANDARD','ON_TOPIC']) {
  cases.push({name:mode+' primary',fields:{gamemode:mode},expected:mode});
  cases.push({name:mode+' alias',fields:{gamemodeName:mode},expected:mode});
  cases.push({name:mode+' equal',fields:{gamemode:mode,gamemodeName:mode},expected:mode});
  cases.push({name:mode+' conflict',fields:{gamemode:mode,gamemodeName:mode==='STANDARD'?'ON_TOPIC':'STANDARD'},expected:mode});
}
cases.push({name:'both absent',fields:{},expected:'STANDARD'});
cases.push({name:'null primary',fields:{gamemode:null,gamemodeName:'ON_TOPIC'},expected:'STANDARD',statsExpected:'ON_TOPIC'});
cases.push({name:'numeric primary',fields:{gamemode:7,gamemodeName:'ON_TOPIC'},expected:7,statsExpected:'ON_TOPIC'});
cases.push({name:'unknown primary',fields:{gamemode:'ALIEN',gamemodeName:'ON_TOPIC'},expected:'ALIEN'});
cases.push({name:'empty primary',fields:{gamemode:'',gamemodeName:'ON_TOPIC'},expected:''});
cases.push({name:'null alias',fields:{gamemodeName:null},expected:'STANDARD'});
cases.push({name:'numeric alias',fields:{gamemodeName:7},expected:7});
const rows=[],dbs=[];
(async()=>{
  for(const [index,c] of cases.entries()) {
    const db=getFirestore(undefined,'mode-'+prefix+'-'+index);dbs.push(db);adapter.db=db;
    const ref=db.collection('submissions').doc('mode-doc');
    const user=db.collection('users').doc('mode-author');
    await user.set({merit:1000,meritCap:50000,meritHold:0,submissionsCount:0,currentStreak:0,lastSubmissionDay:0});
    const now=new Date();now.setUTCHours(0,0,0,0);const yesterday=now.getTime()-86400000;
    await ref.set({id:ref.id,authorId:user.id,content:'Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends.',wordCount:51,status:'PENDING',playmode:'PRACTICE',timestamp:yesterday+1000,themeId:'mode-theme',...c.fields});
    captured=undefined;await engine.run({data:await ref.get()});
    const evaluatorActual=captured;
    await stats.run({}); // Explicit scheduler invocation, not cron delivery/automation.
    const record=(await db.collection('stats').doc('daily').collection('records').doc(new Date(yesterday).toISOString().slice(0,10)).get()).data();
    const statsExpected=(c.statsExpected??c.expected)==='ON_TOPIC'?'mode-theme':'N/A';
    const ok=evaluatorActual===c.expected&&record.hardestTheme===statsExpected&&record.practiceSubmissions===1&&record.rankedSubmissions===0;
    rows.push({name:c.name,fields:c.fields,evaluatorActual,evaluatorExpected:c.expected,hardestTheme:record.hardestTheme,statsExpected,playmodePreserved:record.practiceSubmissions===1&&record.rankedSubmissions===0,passed:ok});
  }
  const result={rows,passed:rows.filter(r=>r.passed).length,failed:rows.filter(r=>!r.passed).length,scope:'Compiled genuine readers and actual Firestore SDK; synthetic CloudEvent/scheduler and R8 function double. No real provider or scheduled delivery.'};
  fs.writeFileSync(output,JSON.stringify(result,null,2)+'\n');
  console.log(JSON.stringify({passed:result.passed,failed:result.failed}));
  await Promise.all(dbs.map(db=>db.terminate()));
  assert.equal(result.failed,0,'Mode contracts must pass without normalizing invalid fields');
})().catch(e=>{console.error(e);process.exitCode=1;Promise.all(dbs.map(db=>db.terminate())).catch(()=>{});});
