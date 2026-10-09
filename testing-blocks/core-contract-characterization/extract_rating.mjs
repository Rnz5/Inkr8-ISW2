import { readFileSync, writeFileSync, mkdirSync } from 'node:fs';
import { resolve, dirname } from 'node:path';
import { fileURLToPath } from 'node:url';
import { createHash } from 'node:crypto';
import { stripTypeScriptTypes } from 'node:module';
import assert from 'node:assert/strict';

const directory = dirname(fileURLToPath(import.meta.url));
const path = resolve(directory, '../../functions/src/submissions/submissionEvaluationEngine.ts');
const raw = readFileSync(path);
const source = raw.toString('utf8').replaceAll('\r\n', '\n');
const productionPath = resolve(directory, '../../functions/src/utils/dynamicRatingChange.ts');
const productionRaw = readFileSync(productionPath);
const fragment = productionRaw.toString('utf8').replaceAll('\r\n', '\n');
assert(fragment.startsWith('export function calculateDynamicRatingChange('));
assert.equal((fragment.match(/function /g) || []).length, 1);
const floorPattern = /const newRating = (Math\.max\(0, currentMyRating \+ myRatingChange\));/g;
const matches = [...source.matchAll(floorPattern)];
assert.equal(matches.length, 1);
const floor = matches[0][1];
const wrapped = 'function ratingFloorProbe(currentMyRating: number, myRatingChange: number): number { return ' + floor + '; }\n';
const generated = stripTypeScriptTypes(wrapped, { mode: 'strip' }) + '\nexport { ratingFloorProbe };\n';
const output = resolve(directory, 'build/generated/rating');
mkdirSync(output, { recursive: true });
writeFileSync(resolve(output, 'RatingSourceProbe.mjs'), generated);
const hash = value => createHash('sha256').update(value).digest('hex');
writeFileSync(resolve(output, 'SOURCE_MANIFEST.json'), JSON.stringify({
    path, source_sha256: hash(raw), fragment, fragment_sha256_lf: hash(fragment),
    productionPath, production_sha256: hash(productionRaw),
    floor_expression: floor,
    floor_line: source.slice(0, matches[0].index).split('\n').length,
    generated_sha256: hash(generated), node_version: process.version,
    scope: 'Native Node strips TS types; direct production TS module tested; generated floor expression only. No TS type checking, Functions configuration, R8 or Firestore transaction.'
}, null, 2) + '\n');
