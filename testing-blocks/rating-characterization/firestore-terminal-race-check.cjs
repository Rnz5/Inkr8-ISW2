// Genuine Admin SDK/transactions. R8 and created-event delivery are explicit doubles.
const assert = require('node:assert/strict');
const path = require('node:path');
const fs = require('node:fs');
const [root, prefix, output] = process.argv.slice(2);
assert(root && /^[a-z]+$/.test(prefix) && output);
assert.equal(process.env.FIRESTORE_EMULATOR_HOST, '127.0.0.1:8080');
assert.equal(process.env.GCLOUD_PROJECT, 'demo-inkr8-local');
assert(!process.env.GOOGLE_APPLICATION_CREDENTIALS && !process.env.OPENAI_API_KEY);
let externalFetches = 0;
global.fetch = async () => { externalFetches++; throw new Error('External HTTP forbidden'); };
const evaluator = require(path.join(root, 'lib/r8/evaluateWithR8.js'));
const fixed = {finalScore: 80, feedback: 'Explicit provider double', source: 'mock'};
evaluator.evaluateWithR8 = async () => fixed;
const adapter = require(path.join(root, 'lib/firebase/admin.js'));
adapter.db = require(require.resolve('firebase-admin/firestore', {paths: [root]})).getFirestore('a07-terminal-' + prefix);
const {db} = adapter;
const pending = new Set(), nativeTransaction = db.runTransaction.bind(db);
db.runTransaction = (...args) => {
  const p = nativeTransaction(...args); pending.add(p);
  p.then(() => pending.delete(p), () => pending.delete(p)); return p;
};
const {submissionEvaluationEngine: engine} = require(path.join(root, 'lib/submissions/submissionEvaluationEngine.js'));
const content = 'Bright rivers carry ancient stories while curious writers explore the quiet valley. Every morning a traveler observes changing shadows beside patient trees and remembers distant friends. Gentle rain reveals new colors across the landscape, inspiring thoughtful descriptions of journeys, discoveries, and ordinary moments that deserve careful attention from everyone around us.';
const results = [];
function check(name, actual, expected) {
  let error = null;
  try { assert.deepEqual(actual, expected); } catch (e) { error = e.message; }
  results.push({name, actual, expected, passed: error === null, error});
}
async function seed(name, mode = 'PRACTICE') {
  const uid = prefix + '-' + name, user = db.collection('users').doc(uid);
  const ref = db.collection('submissions').doc(uid + '-submission');
  assert(!(await user.get()).exists && !(await ref.get()).exists);
  await user.set({name: 'Local terminal fixture', rating: 60, merit: 1000, meritHold: 0, meritCap: 50000,
    submissionsCount: 0, totalMeritEarned: 0, isPlaced: true, placementMatchesPlayed: 0,
    totalPlacementScore: 0, rankedWinStreak: 0, rankedLossStreak: 0, currentStreak: 0,
    lastSubmissionDay: 0, recentScores: [], currentlyInRanked: true, reputation: 0,
    bestScore: 0, hasChosenUsername: true});
  await ref.set({authorId: uid, playmode: mode, gamemode: 'STANDARD', status: 'PENDING', timestamp: Date.now(), content, wordCount: 51, wordsUsed: []});
  return {user, ref, event: {data: await ref.get()}};
}
async function summary(s) {
  const u = (await s.user.get()).data(), d = (await s.ref.get()).data();
  return {status: d.status, score: d.evaluation?.finalScore ?? null, error: d.evaluationError ?? null,
    count: u.submissionsCount, merit: u.merit, rewards: (await s.user.collection('meritTransactions').get()).size,
    inRanked: u.currentlyInRanked, session: u.rankedSessionStartedAt ?? null};
}
async function settle() {
  await new Promise(resolve => setTimeout(resolve, 500));
  while (pending.size) await Promise.allSettled([...pending]);
}
(async () => {
  const terminal = await seed('failed');
  await terminal.ref.update({status: 'FAILED', evaluationError: 'Prior terminal failure'});
  await engine.run(terminal.event); await settle();
  check('Stale created event cannot reopen terminal FAILED', await summary(terminal),
    {status: 'FAILED', score: null, error: 'Prior terminal failure', count: 0, merit: 1000, rewards: 0, inRanked: true, session: null});

  const race = await seed('late-error', 'RANKED');
  let entered, rejectFirst, calls = 0;
  const firstEntered = new Promise(resolve => { entered = resolve; });
  evaluator.evaluateWithR8 = async () => {
    calls++;
    if (calls === 1) return new Promise((resolve, reject) => { rejectFirst = reject; entered(); });
    return fixed;
  };
  const delayed = engine.run(race.event);
  await firstEntered;
  await engine.run(race.event); await settle();
  // A newer session is an explicit fixture; a stale error must not clear it.
  await race.user.update({currentlyInRanked: true, rankedSessionStartedAt: 123456});
  const confirmed = await summary(race);
  rejectFirst(new Error('Explicit delayed provider failure'));
  await delayed; await settle();
  check('Late provider error preserves committed success and newer session', await summary(race), confirmed);
  assert.equal(confirmed.status, 'EVALUATED'); assert.equal(confirmed.count, 1);

  evaluator.evaluateWithR8 = async () => fixed;
  const distinct = await seed('distinct');
  const secondRef = db.collection('submissions').doc(prefix + '-second-distinct');
  await secondRef.set({...distinct.event.data.data(), timestamp: Date.now() + 1});
  await Promise.all([engine.run(distinct.event), engine.run({data: await secondRef.get()})]);
  await settle();
  const final = await summary(distinct);
  check('Distinct submissions on one author still each contribute once',
    {count: final.count, merit: final.merit, rewards: final.rewards}, {count: 2, merit: 1610, rewards: 2});
  assert.equal(externalFetches, 0);
  const report = {project: 'demo-inkr8-local', database: 'a07-terminal-' + prefix, results,
    passed: results.filter(x => x.passed).length, failed: results.filter(x => !x.passed).length,
    externalFetches, scope: 'Actual Firestore SDK/transactions; R8 responses/failure timing, newer session and created-event delivery are explicit fixtures'};
  fs.writeFileSync(output, JSON.stringify(report, null, 2) + '\n');
  await db.terminate(); console.log(`Terminal/race probes: ${report.passed} PASS, ${report.failed} FAIL`);
  if (report.failed) process.exitCode = 1;
})().catch(e => {console.error(e); process.exitCode = 1; db.terminate().catch(() => {});});
