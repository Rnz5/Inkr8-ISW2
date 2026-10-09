import test from 'node:test';
import assert from 'node:assert/strict';
import { calculateDynamicRatingChange as delta } from '../../../../../functions/src/utils/dynamicRatingChange.ts';
import { ratingFloorProbe as floor } from '../../../build/generated/rating/RatingSourceProbe.mjs';

const examples = [
    ['equal low ratings win', 100, 100, 'WIN', 4],
    ['equal low ratings loss', 100, 100, 'LOSS', -6],
    ['equal ratings draw', 100, 100, 'DRAW', 1],
    ['positive gap at clamp win', 100, 200, 'WIN', 9],
    ['positive gap beyond clamp win', 100, 201, 'WIN', 9],
    ['negative gap at clamp win floor', 100, 0, 'WIN', 1],
    ['positive gap loss floor', 100, 200, 'LOSS', -1],
    ['negative gap loss', 100, 0, 'LOSS', -11],
    ['below first rating ceiling', 149, 249, 'WIN', 9],
    ['first rating ceiling starts at 150', 150, 250, 'WIN', 3],
    ['first ceiling still at 179', 179, 279, 'WIN', 3],
    ['second rating ceiling starts at 180', 180, 280, 'WIN', 2],
    ['high rating weak opponent preserves minimum win', 180, 0, 'WIN', 1],
    ['positive half rounds win upward', 100, 110, 'WIN', 5],
    ['negative half rounds loss toward positive infinity', 100, 110, 'LOSS', -5],
    ['negative gap half loss', 100, 90, 'LOSS', -6],
    ['draw does not use gap or win ceiling', 180, 0, 'DRAW', 1],
];
for (const [name, mine, opponent, outcome, expected] of examples) {
    test(name, () => assert.equal(delta(mine, opponent, outcome), expected));
}
test('win and loss deltas are not zero sum at equal ratings', () => {
    assert.equal(delta(100, 100, 'WIN') + delta(100, 100, 'LOSS'), -2);
});
test('draw gives one to each player in the two-call calculation', () => {
    assert.equal(delta(100, 100, 'DRAW') + delta(100, 100, 'DRAW'), 2);
});
test('stored delta can differ from actual variation when floor applies', () => {
    const change = delta(0, 0, 'LOSS');
    assert.equal(change, -6);
    assert.equal(floor(0, change), 0);
    assert.notEqual(floor(0, change) - 0, change);
});
test('floor expression preserves ordinary positive resulting rating', () => {
    assert.equal(floor(100, delta(100, 100, 'LOSS')), 94);
});
