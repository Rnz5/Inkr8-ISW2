const assert=require('node:assert/strict'),fs=require('node:fs'),path=require('node:path'),{performance}=require('node:perf_hooks');
const [root,out]=process.argv.slice(2);assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');global.fetch=async()=>{throw Error('External calls forbidden')};
const a=require(path.join(root,'lib/firebase/admin.js'));const {getFirestore,FieldValue,Timestamp}=require('firebase-admin/firestore');a.db=getFirestore('fin-operations-'+Date.now());a.FieldValue=FieldValue;const db=a.db,L=require(path.join(root,'lib/seasons/seasonLedger.js')),API=require(path.join(root,'lib/seasons/seasonFunctions.js'));process.env.SEASON_ACTIVATED_AT_MS='1';
const rows=[],timings={};function check(name,x,y){assert.deepEqual(x,y,name);rows.push({name,pass:true})}async function timed(name,fn){const t=performance.now();const result=await fn();timings[name]=Math.round(performance.now()-t);return result}
async function create(id,uid){let ref=db.collection('submissions').doc(id);await ref.set({authorId:uid,playmode:'RANKED',status:'PENDING',timestamp:Date.now()});return ref}
function metadataFixture(snap,iso){return{exists:snap.exists,id:snap.id,ref:snap.ref,data:()=>snap.data(),get:(key)=>snap.get(key),createTime:Timestamp.fromMillis(Date.parse(iso))}}
(async()=>{
 check('UTC exact bounds include leap year/year rollover',[
 '2028-02-29T23:59:59.999Z','2028-03-01T00:00:00.000Z','2026-12-31T23:59:59.999Z','2027-01-01T00:00:00.000Z','2026-10-01T00:00:00+14:00','2026-09-30T23:00:00-05:00'].map(x=>L.utcSeason(Date.parse(x)).id),['2028-02','2028-03','2026-12','2027-01','2026-09','2026-10']);
 await db.collection('users').doc('member').set({isPlaced:true,rating:500,merit:2345,name:'Member'});const before=(await db.collection('users').doc('member').get()).data();
 const prev=await create('boundary-before','member'),next=await create('boundary-after','member');
 await L.recordSeasonSubmission(metadataFixture(await prev.get(),'2026-08-31T23:59:59.999Z'));await L.recordSeasonSubmission(metadataFixture(await next.get(),'2026-09-01T00:00:00.000Z'));
 check('Persistence boundary allocates adjacent months, not arrival clock',[(await db.collection('seasonAssignments').doc(prev.id).get()).get('seasonId'),(await db.collection('seasonAssignments').doc(next.id).get()).get('seasonId')],['2026-08','2026-09']);
 check('Prior month does not close unresolved',await L.closeSeason('2026-08',Date.parse('2026-09-01T00:00:00Z')),false);
 await prev.update({status:'EVALUATED',matchStatus:'MATCHED',seasonRatingChange:-7,evaluation:{meritEarned:457}});const late=await prev.get();await Promise.all(Array.from({length:8},()=>L.syncSeasonSubmission(late)));
 check('Late result counts once in original month',(await db.collection('seasons').doc('2026-08').collection('members').doc('member').get()).data().rating,-7);
 check('Late result does not affect next month',(await db.collection('seasons').doc('2026-09').collection('members').doc('member').get()).get('rating'),0);
 await Promise.all(Array.from({length:8},()=>L.closeSeason('2026-08',Date.parse('2026-09-01T00:00:00Z'))));const closed=(await db.collection('seasons').doc('2026-08').get()).data();
 await L.syncSeasonSubmission(late);await Promise.all(Array.from({length:8},()=>L.closeSeason('2026-08',Date.parse('2026-09-02T00:00:00Z'))));
 check('Concurrent/repeated closure and late callbacks preserve final snapshot',(await db.collection('seasons').doc('2026-08').get()).data(),closed);
 const pending=await create('live-pending','member');await L.recordSeasonSubmission(await pending.get());const id=L.utcSeason(Date.now()).id,season=db.collection('seasons').doc(id),end=(await season.get()).get('end');
 check('Exact current end blocked by pending delivery',await L.closeSeason(id,end),false);
 const volume=1200;await timed('seed_1200_members_and_ranked_ms',async()=>{
  for(let i=0;i<volume;i+=150){const batch=db.batch();for(let j=i;j<Math.min(i+150,volume);j++){
   const uid='volume-'+String(j).padStart(4,'0');batch.set(season.collection('members').doc(uid),{userId:uid,name:'Local volume',rating:Math.floor(j/3),meritEarned:j});
   batch.set(db.collection('submissions').doc(uid),{playmode:'RANKED',authorId:uid,status:'FAILED',timestamp:Date.now()});
   batch.set(db.collection('seasonAssignments').doc(uid),{eligible:false,authorId:uid,confirmedAt:Date.now()});
  }await batch.commit();}
 });
 const rank=await timed('rank_1201_members_ms',()=>API.getSeasonRanking.run({auth:{uid:'member'},data:{}}));check('Volume retains all members and shared ranks',[rank.members.length,rank.members[0].rating,rank.members.slice(0,3).map(x=>x.position)],[1201,399,[1,1,1]]);
 const unacked=await create('creation-delayed','unknown');await pending.update({status:'FAILED'});await L.syncSeasonSubmission(await pending.get());check('Delayed creation at volume blocks closure',await L.closeSeason(id,end),false);
 await L.recordSeasonSubmission(await unacked.get());await timed('concurrent_close_1203_ranked_ms',()=>Promise.all(Array.from({length:4},()=>L.closeSeason(id,end))));check('Volume closes once after all acknowledgements',(await season.get()).get('status'),'CLOSED');
 check('General rating/Merit never changed by season bookkeeping',(await db.collection('users').doc('member').get()).data(),before);
 const history=await timed('history_3_periods_1201_members_ms',()=>API.getSeasonHistory.run({auth:{uid:'member'},data:{}}));check('Own history returns old closed month and current closed period',history.history.map(x=>x.id),[id,'2026-08']);
 const profiler=(await API.getSeasonHistory.run({auth:{uid:'volume-0000'},data:{}})).history;check('History belongs to authenticated account',profiler.every(x=>x.id===id),true);
 const initial=require(path.join(root,'lib/users/userInitializer.js'));await Promise.all(Array.from({length:8},()=>initial.createUserProfile.run({uid:'race-new'})));check('Concurrent initializer creates one original balance',(await db.collection('users').doc('race-new').get()).get('merit'),1000);
 fs.writeFileSync(out,JSON.stringify({passed:rows.length,rows,timings,volume:{members:1201,ranked_scanned:1203,concurrent_closures:4},realSDK:true,doubles:['Two explicit server createTime snapshot fixtures for month boundary; all other creation metadata is genuine','Closure now=end is a controlled clock parameter, not elapsed real month','Terminal evaluation states and volume rows are Admin fixtures; no external R8','CloudEvent/auth request bodies for .run are constructed; not cloud Auth/Scheduler transport'],limits:['One local host and one batch size; no cloud load SLA','No real 48h wallclock or Cloud Scheduler execution']},null,2)+'\n');console.log(JSON.stringify({passed:rows.length,timings}));await db.terminate();
})().catch(e=>{fs.writeFileSync(out,JSON.stringify({rows,timings,failed:e.message},null,2));console.error(e.message);process.exitCode=1});
