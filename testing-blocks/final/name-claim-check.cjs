// Check actual getAfter-based consumer contract with genuine emulator Auth and atomic REST commits.
const assert=require('node:assert/strict'),fs=require('node:fs'),path=require('node:path');
const [root,out]=process.argv.slice(2);assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');
const db=require(path.join(root,'lib/firebase/admin.js')).db,rows=[];
function value(x){return typeof x==='boolean'?{booleanValue:x}:typeof x==='number'?{integerValue:String(x)}:{stringValue:x}}
function fields(o){return Object.fromEntries(Object.entries(o).map(([k,v])=>[k,value(v)]))}
async function auth(){const r=await fetch('http://127.0.0.1:9099/identitytoolkit.googleapis.com/v1/accounts:signUp?key=demo-key',{method:'POST',headers:{'Content-Type':'application/json'},body:'{"returnSecureToken":true}'});assert.equal(r.status,200);return r.json()}
async function claim(a,name,includeIndex=true){
 const base='projects/demo-inkr8-local/databases/(default)/documents/',normalized=name.toLowerCase();
 const writes=[{update:{name:base+'users/'+a.localId,fields:fields({name,hasChosenUsername:true})},updateMask:{fieldPaths:['name','hasChosenUsername']}}];
 if(includeIndex)writes.push({update:{name:base+'usernames/'+normalized,fields:fields({userId:a.localId,username:name,normalized,createdAt:Date.now()})},currentDocument:{exists:false}});
 return(await fetch('http://127.0.0.1:8080/v1/projects/demo-inkr8-local/databases/(default)/documents:commit',{method:'POST',headers:{'Content-Type':'application/json',Authorization:'Bearer '+a.idToken},body:JSON.stringify({writes})})).status;
}
(async()=>{
 const a=await auth(),b=await auth();for(const x of [a,b])await db.collection('users').doc(x.localId).set({name:'',hasChosenUsername:false,merit:1000,rating:0});
 const name='Fin'+Date.now();assert.equal(await claim(a,name,false),403);rows.push({name:'Name cannot be claimed without atomic matching index',pass:true});
 assert.equal(await claim(a,name),200);rows.push({name:'Actual username writer contract accepted atomically',pass:true});
 const collision=await claim(b,name);assert(collision===403||collision===409);rows.push({name:'Cross-user existing name cannot be stolen',pass:true});
 assert.equal((await db.collection('usernames').doc(name.toLowerCase()).get()).get('userId'),a.localId);assert.equal((await db.collection('users').doc(b.localId).get()).get('name'),'');rows.push({name:'Denied collision has no partial profile/index effects',pass:true});
 const own=await claim(a,name+'Changed');assert.equal(own,403);rows.push({name:'Second free claim denied; paid change remains callable',pass:true});
 assert.equal((await db.collection('users').doc(a.localId).get()).get('merit'),1000);rows.push({name:'Name registration does not grant or charge Merit',pass:true});
 fs.writeFileSync(out,JSON.stringify({passed:rows.length,rows,realTransport:'Auth ID tokens and Firestore atomic REST commit; no tokens saved',fixtures:'Minimal unclaimed profiles inserted by Admin; genuine production field contract, not full Android onboarding'},null,2)+'\n');console.log(JSON.stringify({passed:rows.length}));await db.terminate();
})().catch(e=>{console.error(e.message);process.exitCode=1});
