// Explicit Admin SDK test fixture server. Loopback only, demo project only.
const assert=require('node:assert/strict'),http=require('node:http'),path=require('node:path');
assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');assert.equal(process.env.FIREBASE_AUTH_EMULATOR_HOST,'127.0.0.1:9099');
const a=require(path.join(process.argv[2],'lib/firebase/admin.js')),db=a.db,{getAuth}=require('firebase-admin/auth');
http.createServer(async(req,res)=>{try{
 if(req.method!=='POST'||!req.url.startsWith('/fixture/')){res.writeHead(404);res.end();return}
 let raw='';for await(const chunk of req){raw+=chunk;assert(raw.length<16000)}const d=JSON.parse(raw);await getAuth().getUser(d.uid);
 if(req.url==='/fixture/bootstrap'){
  await db.collection('users').doc(d.uid).set({id:d.uid,name:'Secure local fixture',email:null,isPlaced:true,hasChosenUsername:true,hasSeenPlacementReveal:true,merit:2345,rating:500,meritCap:50000,meritHold:0,reputation:900,currentlyInRanked:false});
  await db.collection('themes').doc('fin-theme').set({id:'fin-theme',name:'Nature',randomIndex:.5});await db.collection('topics').doc('fin-topic').set({id:'fin-topic',themeId:'fin-theme',name:'Rivers',randomIndex:.5});
  for(const word of ['river','ancient','patient'])await db.collection('words').doc('fin-'+word).set({id:'fin-'+word,word,randomIndex:.5});
 }else if(req.url==='/fixture/history'){
  const id=new Date().toISOString().slice(0,7);await db.collection('seasons').doc(id).set({status:'ACTIVE'},{merge:true});await db.collection('seasons').doc(id).collection('members').doc(d.uid).set({userId:d.uid,name:'Secure local fixture',rating:987654,meritEarned:457});
  const old='1997-01';await db.collection('seasons').doc(old).set({status:'CLOSED',start:852076800000,end:854755200000,pendingCount:0});await db.collection('seasons').doc(old).collection('members').doc(d.uid).set({userId:d.uid,rating:14,meritEarned:457});
 }else if(req.url==='/fixture/failure'){
  await db.collection('users').doc(d.uid).update({currentlyInRanked:false,rankedSessionStartedAt:null});
 }else if(req.url==='/fixture/closure-check'){
  const profile=await db.collection('users').doc(d.uid).get(),authUser=await getAuth().getUser(d.uid);
  const history=await db.collection('seasons').doc('1997-01').collection('members').doc(d.uid).get();
  res.writeHead(200,{'Content-Type':'application/json'});
  res.end(JSON.stringify({ok:true,closed:profile.get('accountClosed')===true,disabled:authUser.disabled,
    historyRetained:history.exists,merit:profile.get('merit'),rating:profile.get('rating')}));return;
 }else throw Error('Unknown fixture');res.writeHead(200,{'Content-Type':'application/json'});res.end('{"ok":true}');
}catch(e){res.writeHead(400);res.end('{"ok":false}')}}).listen(5011,'127.0.0.1',()=>console.log('Demo Admin fixture endpoint only on loopback 5011; synthetic data, no product service.'));
