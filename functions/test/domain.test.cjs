const {test} = require('node:test');
const assert = require('node:assert/strict');
const {seasonAt, currentUser, acceptsRating, SEASON_ANCHOR_MS, SEASON_DURATION_MS} = require('../lib/domain/policy/seasonPolicy');
const {league, meritReward, validateWriting, ratingChange} = require('../lib/domain/policy/gamePolicy');
const {EvaluateGame} = require('../lib/domain/usecase/gameUseCases');
test('season boundaries use exactly fourteen UTC days, including dates before the anchor', () => {
  assert.equal(seasonAt(SEASON_ANCHOR_MS - 1).index, -1);
  assert.equal(seasonAt(SEASON_ANCHOR_MS).index, 0);
  assert.equal(seasonAt(SEASON_ANCHOR_MS + SEASON_DURATION_MS - 1).index, 0);
  assert.equal(seasonAt(SEASON_ANCHOR_MS + SEASON_DURATION_MS).index, 1);
  assert.equal(seasonAt(Date.parse('2028-02-29T00:00:00Z')).end - seasonAt(Date.parse('2028-02-29T00:00:00Z')).start, SEASON_DURATION_MS);
});
test('reset preserves Merit, is repeatable and cannot regress a newer season', () => {
  const user = {id: 'a', name: 'A', email: null, merit: 1025, rating: 93, seasonIndex: 0};
  const next = seasonAt(SEASON_ANCHOR_MS + SEASON_DURATION_MS);
  const reset = currentUser(user, next);
  assert.equal(reset.rating, 0); assert.equal(reset.merit, user.merit);
  assert.deepEqual(currentUser({...reset, rating: 4}, next), {...reset, rating: 4});
  assert.deepEqual(currentUser(reset, seasonAt(SEASON_ANCHOR_MS)), reset);
  assert.equal(acceptsRating(0, 1), false);
});
test('only six numeric leagues exist and transitions are stable', () => {
  assert.deepEqual([0,29,30,59,60,89,90,119,120,149,150,500].map(league), [1,1,2,2,3,3,4,4,5,5,6,6]);
});
test('basic rewards reject invalid scores and have no additional economy operations', () => {
  assert.equal(meritReward(60, 50, false), 130);
  assert.equal(meritReward(60, 50, true), 195);
  assert.equal(meritReward(0, 50, false), 30);
  for (const value of [NaN, Infinity, -1, 101]) assert.throws(() => meritReward(value, 50, false));
  assert.equal(ratingChange(0, 0, 'WIN'), 4); assert.equal(ratingChange(0, 0, 'LOSS'), -6);
});
test('writing validation accepts limits and rejects empty or oversized text', () => {
  const words = (n) => Array(n).fill('palabra').join(' ');
  for (const n of [50,150]) validateWriting(words(n), 'STANDARD');
  assert.throws(() => validateWriting(words(49), 'STANDARD'));
  assert.throws(() => validateWriting(words(151), 'STANDARD'));
  validateWriting(words(200), 'ON_TOPIC');
  assert.throws(() => validateWriting('', 'ON_TOPIC'));
});
test('evaluated games never run the evaluator or settle rewards again', async () => {
  let calls = 0;
  const games = {get: async () => ({status:'EVALUATED'})};
  await new EvaluateGame(games, {evaluate: async () => {calls++;}}, {}, {now: () => SEASON_ANCHOR_MS}).execute('a');
  assert.equal(calls, 0);
});
test('invalid evaluator responses fail without awarding Merit', async () => {
  let completed = 0, failed = 0;
  const games = {get: async () => ({id:'a', status:'PENDING', mode:'PRACTICE'}), complete: async () => {completed++;}, fail: async () => {failed++;}};
  const useCase = new EvaluateGame(games, {evaluate: async () => ({score:NaN, feedback:'x'})}, {}, {now: () => SEASON_ANCHOR_MS});
  await assert.rejects(() => useCase.execute('a')); assert.equal(completed, 0); assert.equal(failed, 1);
});
test('matching failures cannot mark an already settled evaluation failed', async () => {
  let failed = 0, completed = 0;
  const games = {get: async () => ({id:'a',status:'PENDING',mode:'RANKED'}), complete: async () => {completed++;}, fail: async () => {failed++;}};
  const useCase = new EvaluateGame(games, {evaluate: async () => ({score:65,feedback:'Good'})}, {match: async () => {throw Error('offline');}}, {now: () => SEASON_ANCHOR_MS});
  await assert.rejects(() => useCase.execute('a')); assert.equal(completed, 1); assert.equal(failed, 0);
});
