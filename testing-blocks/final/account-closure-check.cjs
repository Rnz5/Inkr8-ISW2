// Real emulator Auth, Firestore REST and callable transport; no credentials saved.
const assert=require('node:assert/strict'),fs=require('node:fs'),path=require('node:path');
const [root,out,phase='green']=process.argv.slice(2);assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');
const db=require(path.join(root,'lib/firebase/admin.js')).db,{getAuth}=require('firebase-admin/auth'),rows=[];
async function test(name,fn){try{await fn();rows.push({name,pass:true})}catch(e){rows.push({name,pass:false,error:e.message})}}
async function auth(){const r=await fetch('http://127.0.0.1:9099/identitytoolkit.googleapis.com/v1/accounts:signUp?key=demo-key',{method:'POST',headers:{'Content-Type':'application/json'},body:'{"returnSecureToken":true}'});assert.equal(r.status,200);return r.json()}
async function callable(name,a,data={}){const r=await fetch('http://127.0.0.1:5001/demo-inkr8-local/us-central1/'+name,{method:'POST',headers:{'Content-Type':'application/json',...(a?{Authorization:'Bearer '+a.idToken}:{})},body:JSON.stringify({data})});return {status:r.status,body:await r.json()}}
async function client(path,a,method='GET'){return(await fetch('http://127.0.0.1:8080/v1/projects/demo-inkr8-local/databases/(default)/documents/'+path,{method,headers:{Authorization:'Bearer '+a.idToken}})).status}
async function fixture(a){const ref=db.collection('users').doc(a.localId);await ref.set({id:a.localId,name:'Closed'+a.localId,merit:321,rating:500,meritHold:77,currentlyInRanked:true,rankedSessionStartedAt:Date.now(),isPlaced:true,hasChosenUsername:true});await db.collection('usernames').doc(('Closed'+a.localId).toLowerCase()).set({userId:a.localId});await ref.collection('meritTransactions').doc('historical').set({amount:7,reason:'historical fixture'});const id='account-history-'+a.localId;await db.collection('submissions').doc(id).set({authorId:a.localId,status:'EVALUATED',playmode:'PRACTICE',content:'Synthetic historical fixture'});return {ref,id,before:(await ref.get()).data()}}
(async()=>{
 const a=await auth(),b=await auth(),f=await fixture(a);
 if(phase!=='red')await test('Owner cannot forge the Functions-only closure marker',async()=>{
  const r=await fetch('http://127.0.0.1:8080/v1/projects/demo-inkr8-local/databases/(default)/documents/users/'+a.localId+'?updateMask.fieldPaths=accountClosed',{method:'PATCH',headers:{'Content-Type':'application/json',Authorization:'Bearer '+a.idToken},body:JSON.stringify({fields:{accountClosed:{booleanValue:true}}})});assert.equal(r.status,403);
 });
 const closed=await callable('closeAccount',a,{userId:a.localId});await test('Trusted owner closure available',async()=>{assert.equal(closed.status,200);assert.equal(closed.body.result.closed,true)});
 if(phase!=='red'&&closed.status===200){
  await test('Auth disabled genuinely, not deleted',async()=>assert.equal((await getAuth().getUser(a.localId)).disabled,true));
  await test('Profile balances/rating/reservation preserved exactly',async()=>{const now=(await f.ref.get()).data();delete now.accountClosed;delete now.accountClosedAt;assert.deepEqual(now,f.before)});
  await test('Username, submission and economic ledger retained',async()=>{assert((await db.collection('usernames').doc(('Closed'+a.localId).toLowerCase()).get()).exists);assert((await db.collection('submissions').doc(f.id).get()).exists);assert((await f.ref.collection('meritTransactions').doc('historical').get()).exists)});
  await test('Old ID token cannot read closed profile',async()=>assert.equal(await client('users/'+a.localId,a),403));
  await test('Old ID token cannot read prior submission',async()=>assert.equal(await client('submissions/'+f.id,a),403));
  await test('Old ID token cannot use catalog',async()=>assert.equal(await client('themes/fin-theme',a),403));
  await test('Direct profile deletion still denied',async()=>assert.equal(await client('users/'+a.localId,a,'DELETE'),403));
  await test('Closed caller cannot reopen the marker or reset retained balance',async()=>{
   const r=await fetch('http://127.0.0.1:8080/v1/projects/demo-inkr8-local/databases/(default)/documents/users/'+a.localId+'?updateMask.fieldPaths=accountClosed&updateMask.fieldPaths=merit',{method:'PATCH',headers:{'Content-Type':'application/json',Authorization:'Bearer '+a.idToken},body:JSON.stringify({fields:{accountClosed:{booleanValue:false},merit:{integerValue:'1000'}}})});assert.equal(r.status,403);assert.equal((await f.ref.get()).get('merit'),321);
  });
  await test('Closed token cannot use season ranking/history',async()=>{for(const n of ['getSeasonRanking','getSeasonHistory'])assert.equal((await callable(n,a)).status,403)});
  await test('Closed token cannot spend or enter Ranked',async()=>{for(const action of ['PURCHASE_EXAMPLE_SENTENCE','ENTER_RANKED','EXPAND_MERIT_CAP'])assert.equal((await callable('applyMeritAction',a,{action})).status,403);assert.equal((await f.ref.get()).get('merit'),321)});
  await test('Closed token cannot delete historical submission',async()=>assert.equal((await callable('deleteSubmission',a,{submissionId:f.id})).status,403));
  await test('Repeated/concurrent closure preserves first marker and history',async()=>{const before=(await f.ref.get()).data();const results=await Promise.all(Array.from({length:8},()=>callable('closeAccount',a,{userId:a.localId})));assert(results.every(x=>x.status===200));assert.deepEqual((await f.ref.get()).data(),before)});
  await test('Refresh of existing Auth session is denied',async()=>{const r=await fetch('http://127.0.0.1:9099/securetoken.googleapis.com/v1/token?key=demo-key',{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded'},body:new URLSearchParams({grant_type:'refresh_token',refresh_token:a.refreshToken})});assert.equal(r.status,400);assert.equal((await r.json()).error.message,'USER_DISABLED')});
  await test('Another user cannot close this account or a foreign target',async()=>{assert.equal((await callable('closeAccount',b,{userId:a.localId})).status,403);assert.equal((await getAuth().getUser(b.localId)).disabled,false)});
  await test('Anonymous callable closure denied',async()=>assert.equal((await callable('closeAccount',null)).status,401));
  await test('Philosopher old token gate precedes existing purchase placeholder',async()=>{const h=require(path.join(root,'lib/users/philosopherController.js')).activatePhilosopherStatus;await assert.rejects(h.run({auth:{uid:a.localId},data:{purchaseToken:'synthetic-not-secret',productId:'fixture'}}),e=>e.code==='permission-denied');assert.equal((await f.ref.get()).get('isPhilosopher'),undefined)});
  await test('Partial Auth failure blocks access, safe retry completes closure',async()=>{
   const c=await auth(),fc=await fixture(c),h=require(path.join(root,'lib/users/accountAccess.js')).closeAccount,authSdk=getAuth(),original=authSdk.updateUser;
   authSdk.updateUser=async()=>{throw Error('Explicit Auth delivery failure double')};
   try{await assert.rejects(h.run({auth:{uid:c.localId},data:{userId:c.localId}}),e=>e.code==='internal')}finally{authSdk.updateUser=original}
   assert.equal((await fc.ref.get()).get('accountClosed'),true);assert.equal(await client('users/'+c.localId,c),403);
   await h.run({auth:{uid:c.localId},data:{userId:c.localId}});assert.equal((await getAuth().getUser(c.localId)).disabled,true);assert.equal((await fc.ref.get()).get('merit'),321);
  });
 }
 fs.writeFileSync(out,JSON.stringify({passed:rows.filter(x=>x.pass).length,failed:rows.filter(x=>!x.pass).length,rows,phase,transport:'Real Auth/REST/callable/Firestore/Admin SDK; synthetic users only. No token or text retained in evidence.',doubles:['One explicit Auth update delivery failure injected only in final partial-failure case; retry uses genuine SDK','Historical data fixtures; no cloud/OAuth/real billing']},null,2)+'\n');console.log(JSON.stringify({passed:rows.filter(x=>x.pass).length,failed:rows.filter(x=>!x.pass).length}));await db.terminate();if(rows.some(x=>!x.pass))process.exitCode=1;
})().catch(e=>{console.error(e.message);process.exitCode=1});
