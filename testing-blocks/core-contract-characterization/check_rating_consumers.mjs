// VER-001: compile the actual producer and copied transaction call expressions.
// This is an isolated scalar check; no SDK/provider/transaction is implemented or run.
import { readFileSync, writeFileSync, mkdirSync, existsSync } from 'node:fs';
import { resolve, dirname } from 'node:path';
import { fileURLToPath } from 'node:url';
import { spawnSync, execFileSync } from 'node:child_process';
import { createHash } from 'node:crypto';
import { stripTypeScriptTypes } from 'node:module';
import assert from 'node:assert/strict';

const args = process.argv.slice(2);
const option = (name, fallback) => {
  const i = args.indexOf(name);
  return i < 0 ? fallback : args[i + 1];
};
const repo = resolve(dirname(fileURLToPath(import.meta.url)), '../..');
const outputArg = option('--report-dir');
const tscArg = option('--tsc');
assert(outputArg && tscArg, 'Provide a new --report-dir and existing --tsc path; no product configuration is invented.');
const output = resolve(outputArg);
const tsc = resolve(tscArg);
assert(!existsSync(output), 'Preserve existing results: use a new report directory.');
assert(existsSync(tsc), 'TypeScript tool must already be available.');
const reference = option('--reference', '7d879c22030527e1d5f5003ad84b76b750d3b29e');
const enginePath = 'functions/src/submissions/submissionEvaluationEngine.ts';
const producerPath = 'functions/src/utils/dynamicRatingChange.ts';
const hash = data => createHash('sha256').update(data).digest('hex');
const engineRaw = readFileSync(resolve(repo, enginePath));
const producerRaw = readFileSync(resolve(repo, producerPath));
const engine = engineRaw.toString('utf8').replaceAll('\r\n', '\n');
const originalRaw = execFileSync('git', ['-C', repo, 'show', `${reference}:${enginePath}`]);
const original = originalRaw.toString('utf8').replaceAll('\r\n', '\n');
const originalStart = original.indexOf('function calculateDynamicRatingChange(');
const originalEnd = original.indexOf('// quality check', originalStart);
assert(originalStart >= 0 && originalEnd > originalStart);
const historicalFunction = original.slice(originalStart, originalEnd).trimEnd();
assert.equal(producerRaw.toString('utf8').replaceAll('\r\n', '\n'), `export ${historicalFunction}\n`);
const getCalls = source => {
  const start = source.indexOf('const myRatingChange = calculateDynamicRatingChange(');
  const end = source.indexOf('\n\n      tx.update(mySubmissionRef,', start);
  assert(start >= 0 && end > start);
  return source.slice(start, end);
};
const calls = getCalls(engine);
assert.equal(calls, getCalls(original), 'Both real call expressions must be identical to the reference.');
const importLine = 'import {calculateDynamicRatingChange} from "../utils/dynamicRatingChange";';
assert.equal(engine.split(importLine).length, 2);
const txStart = engine.indexOf('    await db.runTransaction(async (tx) => {', engine.indexOf('const leagueAdjustments:'));
assert(txStart >= 0 && txStart < engine.indexOf(calls));
assert(engine.indexOf('const currentMyRating = Number(', txStart) < engine.indexOf(calls));

mkdirSync(resolve(output, 'isolated/functions/src/submissions'), { recursive: true });
mkdirSync(resolve(output, 'isolated/functions/src/utils'), { recursive: true });
const copiedProducer = resolve(output, 'isolated', producerPath);
writeFileSync(copiedProducer, producerRaw);
const wrapper = `export function ratingCallSites(currentMyRating: number, currentTheirRating: number, isDraw: boolean, myScoreWins: boolean): [number, number] {\n${calls}\nreturn [myRatingChange, theirRatingChange];\n}\n`;
const consumer = resolve(output, 'isolated/functions/src/submissions/ratingCallSites.ts');
writeFileSync(consumer, `${importLine}\n\n${wrapper}`);
const negative = resolve(output, 'isolated/functions/src/submissions/invalidOutcome.ts');
writeFileSync(negative, `${importLine}\ncalculateDynamicRatingChange(100, 100, "UNKNOWN");\n`);
const commands = [];
const run = (label, command, parameters, expectedSuccess) => {
  const result = spawnSync(command, parameters, { cwd: output, encoding: 'utf8' });
  writeFileSync(resolve(output, `${label}.stdout.log`), result.stdout ?? '');
  writeFileSync(resolve(output, `${label}.stderr.log`), result.stderr ?? '');
  commands.push({ label, command, parameters, exit_code: result.status, expectedSuccess });
  assert.equal(result.status === 0, expectedSuccess, `${label}: ${result.stdout}\n${result.stderr}`);
  return result;
};
const toolVersion = run('compiler-version', process.execPath, [tsc, '--version'], true).stdout.trim();
// Explicit test settings; these do not establish the original target/module/strictness.
const flags = ['--strict', '--target', 'ES2020', '--module', 'commonjs'];
run('production-typecheck', process.execPath, [tsc, ...flags, '--noEmit', resolve(repo, producerPath)], true);
run('producer-consumer-compile', process.execPath,
  [tsc, ...flags, '--rootDir', resolve(output, 'isolated/functions/src'), '--outDir', resolve(output, 'compiled'), copiedProducer, consumer], true);
const rejected = run('invalid-outcome-typecheck', process.execPath, [tsc, ...flags, '--noEmit', negative], false);
assert(rejected.stdout.includes('TS2345'), 'The control must fail for argument type, not missing infrastructure.');
run('engine-native-syntax', process.execPath, ['--check', resolve(repo, enginePath)], true);

const compiled = await import(resolve(output, 'compiled/submissions/ratingCallSites.js')
  .replaceAll('\\', '/').replace(/^([A-Z]):/, 'file:///$1:'));
const legacy = await import('data:text/javascript;base64,' + Buffer.from(
  stripTypeScriptTypes(`${historicalFunction}\n${wrapper}`, { mode: 'strip' })
).toString('base64'));
const values = [-100, -1, 0, 1, 99, 100, 149, 149.5, 150, 179, 179.5, 180, 181, 1000, NaN, -Infinity, Infinity];
let pairs = 0;
for (const mine of values) for (const theirs of values) {
  for (const draw of [false, true]) for (const wins of [false, true]) {
    assert.deepEqual(compiled.ratingCallSites(mine, theirs, draw, wins), legacy.ratingCallSites(mine, theirs, draw, wins));
    pairs++;
  }
}
const result = {
  reference, reference_engine_sha256: hash(originalRaw), engine_path: enginePath, engine_sha256: hash(engineRaw),
  producer_path: producerPath, producer_sha256: hash(producerRaw), copied_producer_sha256: hash(readFileSync(copiedProducer)),
  original_function_sha256_lf: hash(historicalFunction), real_call_sites_sha256_lf: hash(calls),
  TypeScript_tool: toolVersion, Node_tool: process.version, test_flags: flags, commands,
  actual_producer_typechecked: true, exact_copied_producer_and_real_calls_compiled: true,
  invalid_outcome_rejected_TS2345: true, engine_syntax_checked_without_import_resolution: true,
  runtime_pair_comparisons: pairs, runtime_delta_comparisons: pairs * 2, failures: 0,
  values: values.map(v => Number.isFinite(v) ? v : String(v)),
  scope: 'Production scalar TypeScript typecheck; byte-identical producer and real two-call expressions compiled in isolation and executed against the original function. Edge inputs characterize JS behavior, not approved domain rules.',
  limits: ['No original Functions compiler/options inferred', 'No Firebase/R8/module graph/transaction execution',
    'No idempotency/concurrency/placement persistence or full product acceptance', 'No student test execution or authorship inferred'],
};
writeFileSync(resolve(output, 'result.json'), JSON.stringify(result, null, 2) + '\n');
process.stdout.write(JSON.stringify({ TypeScript: toolVersion, runtime_pairs: pairs, deltas: pairs * 2, failures: 0, report: resolve(output, 'result.json') }) + '\n');
