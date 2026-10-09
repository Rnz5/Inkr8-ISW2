// Genuine scheduled handler/SDK transactions. Scheduling and competing commit
// are explicit fixtures; no cron delivery, human matcher or external service claim.
const assert=require('node:assert/strict'),fs=require('node:fs'),path=require('node:path');
const [root,prefix,output]=process.argv.slice(2);
assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');
global.fetch=async()=>{throw new Error('External HTTP forbidden');};
const adapter=require(path.join(root,'lib/firebase/admin.js'));
const ghost=require(path.join(root,'lib/submissions/ghostMatchProcessor.js')).ghostMatchProcessor;
const {getFirestore}=require('firebase-admin/firestore');
const rows=[],dbs=[];
(async()=>{
 for(const scenario of ['claimed','fresh-rating']) {
  const db=getFirestore(undefined,'ghost-'+prefix+'-'+scenario);dbs.push(db);adapter.db=db;
  const user=db.collection('users').doc('author'),ref=db.collection('submissions').doc('aged');
  await user.set({rating:60,isPlaced:true,recentScores:[]});
  await ref.set({authorId:user.id,playmode:'RANKED',status:'EVALUATED',matchStatus:'PENDING',timestamp:Date.now()-49*3600000,evaluation:{finalScore:80,ratingChange:0}});
  const native=db.runTransaction.bind(db);let entered,release;
  const started=new Promise(r=>entered=r),gate=new Promise(r=>release=r);
  // Delay the first genuine transaction; no database or transaction result double.
  let once=true;db.runTransaction=async(...args)=>{if(once){once=false;entered();await gate;}return native(...args);};
  const pending=ghost.run({});await started;
  if(scenario==='claimed') {
   const rival=db.collection('submissions').doc('partner');
   await rival.set({authorId:'opponent',status:'EVALUATED',matchStatus:'PENDING'});
   await native(async tx=>{
    tx.update(ref,{matchStatus:'MATCHED',matchResult:{opponentId:'opponent',ratingChange:4},'evaluation.ratingChange':4});
    tx.update(rival,{matchStatus:'MATCHED',matchResult:{opponentId:user.id,ratingChange:-6}});
    tx.update(user,{rating:64});
   });
  } else await user.update({rating:70});
  release();await pending;db.runTransaction=native;
  const doc=(await ref.get()).data(),u=(await user.get()).data();
  const expected=scenario==='claimed'?{matchStatus:'MATCHED',rating:64,delta:4,opponentId:'opponent'}:{matchStatus:'GHOST',rating:72,delta:2,opponentId:'GHOST'};
  const actual={matchStatus:doc.matchStatus,rating:u.rating,delta:doc.evaluation.ratingChange,opponentId:doc.matchResult.opponentId};
  rows.push({scenario,expected,actual,passed:JSON.stringify(expected)===JSON.stringify(actual)});
 }
 const result={rows,passed:rows.filter(r=>r.passed).length,failed:rows.filter(r=>!r.passed).length,scope:'Firestore SDK and compiled ghost handler; delayed transaction/competing commit fixtures. Ghost benchmark, ±2 and +2/-4/+1 unchanged.'};
 fs.writeFileSync(output,JSON.stringify(result,null,2)+'\n');console.log(JSON.stringify({passed:result.passed,failed:result.failed}));
 await Promise.all(dbs.map(d=>d.terminate()));assert.equal(result.failed,0,'Ghost must not replace a claimed match or stale-write rating');
})().catch(e=>{console.error(e);process.exitCode=1;Promise.all(dbs.map(d=>d.terminate())).catch(()=>{});});
