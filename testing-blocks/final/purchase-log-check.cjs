// Existing purchase stub only: prove log hygiene, never genuine purchase verification.
const assert=require('node:assert/strict'),fs=require('node:fs'),path=require('node:path');
const [root,out]=process.argv.slice(2);
assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');
assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');
global.fetch=async()=>{throw Error('External calls forbidden')};
const adapter=require(path.join(root,'lib/firebase/admin.js'));
adapter.db=require('firebase-admin/firestore').getFirestore('purchase-log-'+Date.now());
const db=adapter.db,rows=[],token='synthetic-sensitive-test-value';
(async()=>{
 await db.collection('users').doc('member').set({isPhilosopher:false,merit:2345,rating:500});
 const logs=[],original=console.log;console.log=(...args)=>logs.push(args.map(String).join(' '));
 let result;
 try{result=await require(path.join(root,'lib/users/philosopherController.js')).activatePhilosopherStatus.run({auth:{uid:'member'},data:{purchaseToken:token,productId:'synthetic-product'}})}finally{console.log=original}
 const data=(await db.collection('users').doc('member').get()).data();
 rows.push({name:'Existing stub effect and Merit/rating unchanged',pass:result.success===true&&data.isPhilosopher===true&&data.merit===2345&&data.rating===500});
 rows.push({name:'Sensitive purchase token absent from console log',pass:logs.every(line=>!line.includes(token))});
 fs.writeFileSync(out,JSON.stringify({rows,passed:rows.filter(r=>r.pass).length,failed:rows.filter(r=>!r.pass).length,realSDK:true,doubles:['Constructed auth/callable request; synthetic purchase token; existing production Google verification stub, NOT genuine billing'],limits:['Stub accepts any nonempty token/product. This is a prior security defect and external integration dependency; not fixed by log hygiene.','No purchase, Google API or external account used']},null,2)+'\n');
 console.log(JSON.stringify({passed:rows.filter(r=>r.pass).length,failed:rows.filter(r=>!r.pass).length,purchase_verification_still_placeholder:true}));await db.terminate();if(rows.some(r=>!r.pass))process.exitCode=1;
})().catch(e=>{console.error(e.message);process.exitCode=1});
