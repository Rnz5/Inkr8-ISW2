// Genuine Auth/Firestore REST; transaction cases use genuine Admin SDK in an isolated named demo database.
const assert=require('node:assert/strict'),fs=require('node:fs'),path=require('node:path');
const [root,out,phase='green']=process.argv.slice(2);
assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');
const adapter=require(path.join(root,'lib/firebase/admin.js')),original=adapter.db,rows=[];
async function test(name,fn){try{await fn();rows.push({name,pass:true})}catch(e){rows.push({name,pass:false,error:e.message})}}
async function auth(){const r=await fetch('http://127.0.0.1:9099/identitytoolkit.googleapis.com/v1/accounts:signUp?key=demo-key',{method:'POST',headers:{'Content-Type':'application/json'},body:'{"returnSecureToken":true}'});assert.equal(r.status,200);return r.json()}
async function direct(id,a){return(await fetch('http://127.0.0.1:8080/v1/projects/demo-inkr8-local/databases/(default)/documents/submissions/'+id,{method:'DELETE',headers:{Authorization:'Bearer '+a.idToken}})).status}
(async()=>{
 const a=await auth(),id='fin-delete-'+Date.now();
 // Retired playmode in the red fixture avoids automatic delivery; set Ranked afterwards.
 await original.collection('submissions').doc(id).set({authorId:a.localId,playmode:'TOURNAMENT',status:'EVALUATED',matchStatus:'MATCHED'});
 await original.collection('submissions').doc(id).update({playmode:'RANKED'});
 await test('Client cannot erase Ranked before seasonal acknowledgement',async()=>assert.equal(await direct(id,a),403));
 if(phase!=='red'){
  const {getFirestore}=require('firebase-admin/firestore');
  const db=getFirestore(require('firebase-admin/app').getApp(),'fin-delete-'+Date.now());adapter.db=db;
  const handler=require(path.join(root,'lib/submissions/deleteSubmission.js')).deleteSubmission;
  async function add(key,fields={}){await db.collection('submissions').doc(key).set({authorId:a.localId,playmode:'RANKED',status:'EVALUATED',matchStatus:'MATCHED',...fields})}
  function call(key,uid=a.localId){return handler.run({auth:uid?{uid}:undefined,data:{submissionId:key}})}
  async function rejects(key,code,uid=a.localId){await assert.rejects(call(key,uid),e=>e.code===code);assert((await db.collection('submissions').doc(key).get()).exists)}
  await add('pending',{status:'PENDING'});await test('Pending owner deletion rejected without effects',()=>rejects('pending','failed-precondition'));
  await add('unmatched',{matchStatus:'UNMATCHED'});await test('Unmatched evaluated Ranked retained',()=>rejects('unmatched','failed-precondition'));
  await add('no-ack');await test('Missing assignment retained before delayed creation delivery',()=>rejects('no-ack','failed-precondition'));
  await add('unsettled');await db.collection('seasonAssignments').doc('unsettled').set({eligible:true,terminal:false});await test('Eligible unsettled terminal Ranked retained',()=>rejects('unsettled','failed-precondition'));
  await add('failed',{status:'FAILED'});await test('Failed Ranked also needs seasonal acknowledgement',()=>rejects('failed','failed-precondition'));
  await add('foreign');await test('Cross-user callable deletion denied',()=>rejects('foreign','permission-denied','another'));
  await test('Unauthenticated callable deletion denied',()=>rejects('foreign','unauthenticated',null));
  await add('practice',{playmode:'PRACTICE'});await test('Terminal Practice deletion contract retained',async()=>{await call('practice');assert(!(await db.collection('submissions').doc('practice').get()).exists)});
  await add('negative');await db.collection('seasonAssignments').doc('negative').set({eligible:false});await test('Negative eligibility acknowledgement permits ordinary deletion',async()=>{await call('negative');assert(!(await db.collection('submissions').doc('negative').get()).exists)});
  await add('settled');await db.collection('seasonAssignments').doc('settled').set({eligible:true,terminal:true,rating:14,meritEarned:7});
  await test('Settled deletion preserves historical assignment and effects',async()=>{const before=(await db.collection('seasonAssignments').doc('settled').get()).data();await call('settled');assert.deepEqual((await db.collection('seasonAssignments').doc('settled').get()).data(),before)});
  await test('Repeated missing-document delete remains idempotent',async()=>assert.deepEqual(await call('settled'),{deleted:true}));
  await add('concurrent');await db.collection('seasonAssignments').doc('concurrent').set({eligible:true,terminal:true});await test('Concurrent deletes do not repeat rewards or rewrite assignment',async()=>{const before=(await db.collection('seasonAssignments').doc('concurrent').get()).data();await Promise.all(Array.from({length:8},()=>call('concurrent')));assert.deepEqual((await db.collection('seasonAssignments').doc('concurrent').get()).data(),before)});
  await add('historical');const cutoff=process.env.SEASON_ACTIVATED_AT_MS;process.env.SEASON_ACTIVATED_AT_MS=String(Date.now()+60000);await test('Pre-activation history retains ordinary delete contract without backfill',async()=>{await call('historical');assert(!(await db.collection('seasonAssignments').doc('historical').get()).exists)});process.env.SEASON_ACTIVATED_AT_MS=cutoff;
  await add('invalid-cutoff');process.env.SEASON_ACTIVATED_AT_MS='invalid';await test('Configured invalid activation fails safely',()=>rejects('invalid-cutoff','failed-precondition'));process.env.SEASON_ACTIVATED_AT_MS=cutoff;
  await test('Real callable HTTP/Auth permits acknowledged deletion',async()=>{
   // Direct write fixture, authentic callable transport, no constructed auth on this case.
   await original.collection('seasonAssignments').doc(id).set({eligible:false});
   const r=await fetch('http://127.0.0.1:5001/demo-inkr8-local/us-central1/deleteSubmission',{method:'POST',headers:{'Content-Type':'application/json',Authorization:'Bearer '+a.idToken},body:JSON.stringify({data:{submissionId:id}})});
   assert.equal(r.status,200);assert.equal((await r.json()).result.deleted,true);assert(!(await original.collection('submissions').doc(id).get()).exists);
  });await db.terminate();
 }
 fs.writeFileSync(out,JSON.stringify({passed:rows.filter(x=>x.pass).length,failed:rows.filter(x=>!x.pass).length,rows,transport:'REST real Auth tokens; green also uses real Admin transactions in named demo database and one real callable HTTP/Auth. No tokens recorded.',phase,limits:'Named DB cases call genuine handler .run with explicit auth/clock fixtures. Red checks only REST deletion. Not cloud or scheduler.'},null,2)+'\n');
 console.log(JSON.stringify({passed:rows.filter(x=>x.pass).length,failed:rows.filter(x=>!x.pass).length}));await original.terminate();if(rows.some(x=>!x.pass))process.exitCode=1;
})().catch(e=>{console.error(e.message);process.exitCode=1});
