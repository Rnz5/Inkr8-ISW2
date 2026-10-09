// Functional withdrawal acceptance; SDK is genuine, CloudEvents/R8/cron invocation are doubles.
const assert=require('node:assert/strict'),path=require('node:path'),fs=require('node:fs');
const [root,prefix,output]=process.argv.slice(2);
assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');
assert(!process.env.GOOGLE_APPLICATION_CREDENTIALS&&!process.env.OPENAI_API_KEY);
global.fetch=async()=>{throw new Error('External access forbidden');};
const adapter=require(path.join(root,'lib/firebase/admin.js'));
adapter.FieldValue=require('firebase-admin/firestore').FieldValue;
adapter.db=require('firebase-admin/firestore').getFirestore('retirement-'+prefix);
const {db}=adapter;const api=require(path.join(root,'lib/index.js'));
require(path.join(root,'lib/r8/evaluateWithR8.js')).evaluateWithR8=async()=>({finalScore:80,feedback:'R8 double',source:'mock'});
const rows=[];
function check(name,actual,expected){let error=null;try{assert.deepEqual(actual,expected);}catch(e){error=e.message;}rows.push({name,actual,expected,error,passed:!error});}
async function user(name,extra={}) {const u=db.collection('users').doc(name);await u.set({name,rating:119,merit:10000,meritHold:0,meritCap:50000,isPlaced:true,currentStreak:0,lastSubmissionDay:0,recentScores:[],submissionsCount:0,totalMeritEarned:0,reputation:900,rankedWinStreak:1,rankedLossStreak:0,currentlyInRanked:true,rankedSessionStartedAt:Date.now()-61*60000,...extra});return u;}
(async()=>{
 const retired=['tournamentEvaluationEngine','tournamentPhaseController','tournamentRefundEngine','tournamentSubmissionWatcher','tournamentFinalizerEngine','enrollInTournament','seedSystemTournaments','createUserTournament','pruneOldTournaments','onTipCreated','dailyDebtPenaltyProcessor'];
 check('No tournament/tip/reputation job exports',Object.keys(api).filter(n=>/tournament|onTipCreated|dailyDebtPenalty/i.test(n)).sort(),[]);
 const rep=await user('purchase');let rejected=false;try{await api.applyMeritAction.run({auth:{uid:rep.id},data:{action:'PURCHASE_REPUTATION_VIEW'}});}catch(e){rejected=e.code==='failed-precondition';}
 const pu=(await rep.get()).data();check('Retired reputation purchase rejected with no debit',{rejected,merit:pu.merit,transactions:(await rep.collection('meritTransactions').get()).size},{rejected:true,merit:10000,transactions:0});
 const abandon=await user('abandon');await api.applyMeritAction.run({auth:{uid:abandon.id},data:{action:'ABANDON_RANKED'}});const au=(await abandon.get()).data();
 check('Abandon clears session without reputation/streak penalty',{rep:au.reputation,win:au.rankedWinStreak,loss:au.rankedLossStreak,merit:au.merit,active:au.currentlyInRanked,started:au.rankedSessionStartedAt},{rep:900,win:1,loss:0,merit:10000,active:false,started:null});
 const clean=await user('expired');await api.rankedSessionCleaner.run({});const cu=(await clean.get()).data();
 check('Expired session cleanup leaves retained economy and retired data intact',{rep:cu.reputation,merit:cu.merit,win:cu.rankedWinStreak,loss:cu.rankedLossStreak,active:cu.currentlyInRanked},{rep:900,merit:10000,win:1,loss:0,active:false});
 const entry=await user('entry',{currentlyInRanked:false});await api.applyMeritAction.run({auth:{uid:entry.id},data:{action:'ENTER_RANKED'}});const en=(await entry.get()).data();
 check('Existing Ranked pricing/auth/session/log retained',{merit:en.merit,rep:en.reputation,active:en.currentlyInRanked,transactions:(await entry.collection('meritTransactions').get()).size},{merit:9916,rep:900,active:true,transactions:1});
 const profile=await user('placement',{rating:0,isPlaced:false,placementMatchesPlayed:5,totalPlacementScore:400});const metadata=db.collection('metadata').doc('rankings');const old={leagueCounts:{SCRIBE:7,STYLIST:9},historical:'preserve'};await metadata.set(old);
 const hist=db.collection('tournaments').doc('historical');await hist.set({status:'COMPLETED',prizePool:1234,participants:['old']});const history=(await hist.get()).data();
 const sub=db.collection('submissions').doc('placement');await sub.set({authorId:profile.id,playmode:'RANKED',gamemodeName:'STANDARD',status:'PENDING',timestamp:Date.now(),content:'Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us.',wordCount:51,wordsUsed:[]});
 await api.submissionEvaluationEngine.run({data:await sub.get()});await new Promise(r=>setTimeout(r,200));const u=(await profile.get()).data(),s=(await sub.get()).data();
 check('Placement formula and Merit reward preserved',{placed:u.isPlaced,rating:u.rating,played:u.placementMatchesPlayed,earned:s.evaluation.meritEarned,merit:u.merit,session:u.currentlyInRanked},{placed:true,rating:96,played:6,earned:457,merit:10457,session:false});
 check('Ranked completion no reputation update',u.reputation,900);check('No league population mutation', (await metadata.get()).data(),old);
 check('Historical tournament document untouched',(await hist.get()).data(),history);
 const week=await user('tax');await api.weeklyTaxProcessor.run({});const wu=(await week.get()).data();check('Weekly Merit tax unchanged',{merit:wu.merit,reputation:wu.reputation},{merit:9900,reputation:900});
 fs.writeFileSync(output,JSON.stringify({rows,passed:rows.filter(x=>x.passed).length,failed:rows.filter(x=>!x.passed).length,doubles:['R8 function returns 80','CloudEvents and cron direct invocation'],project:'demo-inkr8-local',realSDK:true},null,2)+'\n');console.log(JSON.stringify({passed:rows.filter(x=>x.passed).length,failed:rows.filter(x=>!x.passed).length}));await db.terminate();if(rows.some(x=>!x.passed))process.exitCode=1;
})().catch(e=>{console.error(e);process.exitCode=1;});
