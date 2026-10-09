"""Read-only audit of the existing local refactor, not Android/Firebase acceptance."""
import argparse
import hashlib
import json
import re
from pathlib import Path
import subprocess
import sys
import textwrap

def normalized(data):
    return data.decode('utf-8').replace('\r\n', '\n')

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--repo', type=Path, required=True, help='Reference Git checkout')
    parser.add_argument('--candidate-root', type=Path, help='Optional isolated copy to inspect')
    parser.add_argument('--report-dir', type=Path, required=True, help='New output directory')
    args = parser.parse_args()
    repo = args.repo.resolve()
    candidate = (args.candidate_root or repo).resolve()
    output = args.report_dir.resolve()
    if output.exists():
        parser.error('Use a new report directory; existing evidence is preserved.')
    if output == repo or output in repo.parents or output == candidate or output in candidate.parents:
        parser.error('Report directory cannot replace a source root.')
    spec_path = Path(__file__).with_name('audit-spec.json')
    spec = json.loads(spec_path.read_text(encoding='utf-8'))
    checks = []
    def check(name, predicate, detail=None):
        checks.append({'name': name, 'passed': bool(predicate), 'detail': detail})
    def git(*arguments):
        proc = subprocess.run(['git', '-C', str(repo), *arguments], capture_output=True)
        if proc.returncode:
            raise RuntimeError(proc.stderr.decode(errors='replace'))
        return proc.stdout
    def head(name):
        return normalized(git('show', spec['baseline'] + ':' + name))
    def operational_bytes(name):
        declared = spec.get('declared_functional_scope_changes', {}).get(name)
        path = candidate / name
        if not declared:
            return path.read_bytes()
        data = path.read_bytes() if path.exists() else None
        digest = hashlib.sha256(data).hexdigest() if data is not None else None
        if digest != declared['after_sha256']:
            raise ValueError('Functional delta differs from reviewed source: ' + name)
        old = declared['before_text']
        if old is None:
            raise ValueError('New functional source cannot masquerade as old core: ' + name)
        restored = old.encode('utf-8')
        if hashlib.sha256(restored).hexdigest() != declared['before_sha256']:
            raise ValueError('Functional inversion corrupted original source: ' + name)
        return restored
    def current(name):
        return normalized(operational_bytes(name))
    def unique_replace(source, old, new):
        if source.count(old) != 1:
            raise ValueError('Expected one occurrence of the declared source fragment')
        return source.replace(old, new, 1)
    def final_conserved_current(name):
        source = current(name)
        declared = spec.get('declared_core_final_changes', {}).get(name)
        if declared:
            source = unique_replace(source, declared['new'], declared['old'])
        return source
    def mode_conserved_current(name):
        source = final_conserved_current(name)
        declared = spec.get('declared_mode_closure_changes', {}).get(name)
        if declared:
            for change in declared['exact_replacements']:
                count = change.get('occurrences', 1)
                if source.count(change['new']) != count:
                    raise ValueError('Declared closure source fragment count mismatch: ' + name)
                source = source.replace(change['new'], change['old'])
        return source
    def conserved_current(name):
        source = mode_conserved_current(name)
        declared = spec.get('declared_callback_changes', {}).get(name) or spec.get('declared_atomic_changes', {}).get(name)
        if declared:
            for change in declared['exact_replacements']:
                source = unique_replace(source, change['new'], change['old'])
        return source
    try:
        for name, declared in spec.get('declared_functional_scope_changes', {}).items():
            path = candidate / name
            digest = hashlib.sha256(path.read_bytes()).hexdigest() if path.exists() else None
            check('reviewed_functional_delta:' + name, digest == declared['after_sha256'],
                  {'record': declared['record'], 'author': 'Codex', 'functional_not_conservative': True})
        for name, declared in spec.get('declared_core_final_changes', {}).items():
            check('declared_core_final_changes_only:' + name,
                  hashlib.sha256(final_conserved_current(name).encode('utf-8')).hexdigest() == declared['before_sha256_lf'],
                  {'record': declared['record'], 'agent_authorship': True, 'functional_correction': True})
        for name, declared in spec.get('declared_callback_changes', {}).items():
            restored = conserved_current(name)
            check('declared_callback_changes_only:' + name,
                  hashlib.sha256(restored.encode('utf-8')).hexdigest() == declared['before_sha256_lf'],
                  {'record': declared['record'], 'author': declared['author'], 'student_authorship': False})
        for name, declared in spec.get('declared_atomic_changes', {}).items():
            check('declared_atomic_changes_only:' + name,
                  hashlib.sha256(conserved_current(name).encode('utf-8')).hexdigest() == declared['before_sha256_lf'],
                  {'record': declared['record'], 'author': declared['author'], 'functional_correction': True})
        for name, declared in spec.get('declared_mode_closure_changes', {}).items():
            check('declared_closure_changes_only:' + name,
                  hashlib.sha256(mode_conserved_current(name).encode('utf-8')).hexdigest() == declared['before_sha256_lf'],
                  {'record': declared['record'], 'agent_authorship': True})
        # Publication adds commits; the frozen source oracle remains the same ancestor.
        baseline_exists = git('cat-file', '-t', spec['baseline']).decode().strip() == 'commit'
        ancestry = subprocess.run(['git', '-C', str(repo), 'merge-base', '--is-ancestor', spec['baseline'], 'HEAD'], capture_output=True)
        check('baseline_commit_preserved_and_ancestor', baseline_exists and ancestry.returncode == 0,
              {'baseline': spec['baseline'], 'publication_record': 'FIN-002'})
        check('reference_origin', git('remote', 'get-url', 'origin').decode().strip() == spec['origin'])
        files = git('ls-tree', '-r', '--name-only', spec['baseline']).decode().splitlines()
        production = [n for n in files if (n.startswith('app/src/main/') and n.endswith('.kt'))
                      or (n.startswith('functions/src/') and n.endswith('.ts'))]
        changed = set(spec['changed_original_sources'])
        support_fixes = spec.get('functional_support_fixes', {})
        for name in production:
            if name not in changed and name not in support_fixes:
                check('unchanged_source:' + name, conserved_current(name) == head(name))
        for name, fix in support_fixes.items():
            restored = current(name)
            for addition in fix['exact_additions']:
                restored = unique_replace(restored, addition, '')
            check('declared_functional_support_only:' + name, restored == head(name),
                  {'record': fix['record'], 'not_conservative_refactor': True})
        for name, expected in spec['complete_component_sha256_lf'].items():
            actual = hashlib.sha256(current(name).encode('utf-8')).hexdigest()
            check('complete_component_source:' + name, actual == expected)

        validation = current(spec['validation_path'])
        validation = unique_replace(validation, spec['validation_declarations'], '')
        validation = unique_replace(validation, 'split(whitespacePattern)', 'split("\\\\s+".toRegex())')
        validation = unique_replace(validation, 'replace(nonAsciiLetterPattern, "")',
                                    'replace("[^a-zA-Z]".toRegex(), "")')
        check('ValidationUtils_algorithm_messages_preserved', validation == head(spec['validation_path']))

        original_writing = head(spec['writing_path'])
        start = original_writing.index('@Composable\nfun DirectiveCard(')
        end = original_writing.index('@Preview(showBackground', start)
        fragment = original_writing[start:end]
        components = current(spec['writing_components_path'])
        check('Writing_five_components_verbatim', components.endswith(fragment))
        check('Writing_components_package', components.startswith('package com.inkr8.screens\n'))
        writing = conserved_current(spec['writing_path'])
        core = spec['mechanical_core_extractions']
        # Derive old decisions from Git HEAD, never from new candidate hashes.
        match = re.search(r'    val canSubmit by remember \{\n        derivedStateOf \{\n(?P<body>.*?)\n        \}\n    \}', original_writing, re.S)
        if not match:
            raise ValueError('Original canSubmit missing')
        old_admission = match['body']
        admission_body = textwrap.dedent(old_admission).replace('wordCount >=', 'wordCount() >=').replace('wordCount <=', 'wordCount() <=')
        expected_admission = ('package com.inkr8.evaluation\n\nimport com.inkr8.data.Gamemode\n\n'
            '// Count is read only in the same nullable-limit branches as the original derived state.\n'
            'internal fun isWritingAdmitted(userText: String, gamemode: Gamemode, wordCount: () -> Int): Boolean {\n'
            '    return ' + admission_body.replace('\n', '\n    ') + '\n}\n')
        check('A05_complete_production_admission_from_original_expression', current(core['admission_path']) == expected_admission)
        writing = unique_replace(writing, 'import com.inkr8.evaluation.isWritingAdmitted\n', '')
        writing = unique_replace(writing, '            isWritingAdmitted(userText, gamemode) { wordCount }', old_admission)

        original_vm = head(core['viewmodel_path'])
        expressions = [
            re.search(r'loadingElapsedSeconds = (pollCount \* [^\n]+)', original_vm)[1],
            re.search(r'if \((loadingElapsedSeconds > [^)]+)\) \{\n                    loadingTimeout = true', original_vm)[1],
            'status == SubmissionStatus.EVALUATED', 'status == SubmissionStatus.FAILED']
        expected_policy = ('package com.inkr8.viewmodel\n\nimport com.inkr8.data.SubmissionStatus\n\n'
            '// Existing scalar wait decisions; scheduling, snapshot reads and effects stay in AppViewModel.\n'
            'internal fun resultWaitElapsedSeconds(pollCount: Int): Int = ' + expressions[0] + '\n\n'
            'internal fun hasResultWaitTimedOut(loadingElapsedSeconds: Int): Boolean = ' + expressions[1] + '\n\n'
            'internal fun isEvaluatedResult(status: SubmissionStatus): Boolean = ' + expressions[2] + '\n\n'
            'internal fun isFailedResult(status: SubmissionStatus): Boolean = ' + expressions[3] + '\n')
        check('A06_complete_scalar_policy_from_original_decisions', current(core['wait_policy_path']) == expected_policy)
        vm = conserved_current(core['viewmodel_path'])
        for old, call in core['wait_replacements']:
            vm = unique_replace(vm, call, old)
        check('A06_complete_ViewModel_states_reads_callbacks_order_preserved', vm == original_vm)

        original_engine = head(core['engine_path'])
        rating_start = original_engine.index('function calculateDynamicRatingChange(')
        rating_end = original_engine.index('// quality check', rating_start)
        rating_fragment = original_engine[rating_start:rating_end]
        check('A07_complete_rating_module_verbatim_export_only', current(core['rating_path']) == 'export ' + rating_fragment.rstrip() + '\n')
        engine = unique_replace(conserved_current(core['engine_path']), core['rating_import'], '')
        engine = unique_replace(engine, '// quality check', rating_fragment + '// quality check')
        check('A07_complete_engine_effects_calls_transactions_preserved', engine == original_engine)
        if spec.get('matching_module'):
            module = spec['matching_module']
            original_matching = original_engine[original_engine.index('async function tryMatchRankedSubmission('):]
            check('A07_matching_function_derived_verbatim_from_original', conserved_current(module['path']) == module['imports'] + 'export ' + original_matching)

        writing = unique_replace(writing, spec['writing_declarations'], '')
        writing = unique_replace(writing, 'split(writingWhitespacePattern)', 'split("\\\\s+".toRegex())')
        writing = unique_replace(writing, 'split(writingWordBoundaryPattern)', 'split(Regex("\\\\W+"))')
        preview = '@Preview(showBackground'
        writing = unique_replace(writing, preview, fragment + preview)
        check('Writing_editor_admission_tokenization_draft_event_preview_preserved', writing == original_writing)

        results = head(spec['results_path'])
        component_source = current(spec['results_components_path'])
        for extraction in spec['results_extractions']:
            results = unique_replace(results, extraction['chunk'], extraction['call'])
            marker = extraction['signature'] + '\n'
            body = component_source.split(marker, 1)[1].split('\n}', 1)[0]
            check('Results_component_body:' + extraction['name'],
                  textwrap.dedent(body).strip('\n') == textwrap.dedent(extraction['chunk']).strip('\n'))
        for deletion in spec['retired_presentation'][spec['results_path']]:
            results = unique_replace(results, deletion['text'], '')
        check('Results_only_declared_extraction_and_RET001', results == current(spec['results_path']))
        header = head(spec['header_path'])
        for deletion in spec['retired_presentation'][spec['header_path']]:
            header = unique_replace(header, deletion['text'], '')
        check('UserHeader_only_declared_RET001', header == current(spec['header_path']))
        # RET-002/003: declared withdrawals; every original fragment must exist in Git HEAD.
        # Compare the entire remaining file, including contracts, listeners and Ranked controls.
        for name, changes in spec.get('approved_presentation_changes', {}).items():
            expected = head(name)
            for change in changes:
                if expected.count(change['old']) != change['occurrences']:
                    raise ValueError('Original withdrawal fragment/count mismatch: ' + change['label'])
                expected = expected.replace(change['old'], change['new'])
            check('only_declared_presentation_withdrawal:' + name, current(name) == expected)
        for resource in spec['resources']:
            data = (candidate / resource['path']).read_bytes()
            blob = hashlib.sha1(b'blob ' + str(len(data)).encode('ascii') + b'\0' + data).hexdigest()
            check('authentic_resource:' + resource['path'], blob == resource['blob'] and len(data) == resource['size'])
        for name, expected_sha256 in spec.get('authentic_recovered_files', {}).items():
            check('authentic_recovered_file:' + name,
                  hashlib.sha256(operational_bytes(name)).hexdigest() == expected_sha256)
        for name, expected_sha256 in spec.get('product_verification_files', {}).items():
            check('product_verification_file:' + name,
                  hashlib.sha256(operational_bytes(name)).hexdigest() == expected_sha256)
        recovered_sources = {n for n in spec.get('authentic_recovered_files', {})
                             if n.endswith(('.kt', '.ts')) and n.startswith(('app/src/main/', 'functions/src/'))}
        known = set(production) | recovered_sources | {spec['writing_components_path'], spec['results_components_path'],
            core['admission_path'], core['wait_policy_path'], core['rating_path']} | set(spec.get('declared_source_modules', []))
        for name, declared in spec.get('declared_functional_scope_changes', {}).items():
            if name.endswith(('.kt', '.ts')) and name.startswith(('app/src/main/', 'functions/src/')):
                if declared['after_sha256'] is None:
                    known.discard(name)
                elif declared['before_text'] is None:
                    known.add(name)
        actual = {f.relative_to(candidate).as_posix() for root in [candidate / 'app/src/main', candidate / 'functions/src']
                  for f in root.rglob('*') if f.suffix in ['.kt', '.ts'] and f.is_file()}
        check('no_unreviewed_production_source_additions', actual == known,
              {'unexpected': sorted(actual - known), 'missing': sorted(known - actual)})
    except (OSError, RuntimeError, ValueError, IndexError, KeyError) as error:
        check('audit_precondition_or_source_structure', False, str(error))
    report = {'baseline': spec['baseline'], 'candidate_root': str(candidate),
              'spec_sha256': hashlib.sha256(spec_path.read_bytes()).hexdigest(), 'checks': checks,
              'passed': all(c['passed'] for c in checks),
              'scope': 'Source conservation and authentic resource bytes; CRLF/LF normalized for Kotlin/TypeScript only.',
              'limits': ['No Kotlin/Compose type checking or rendering', 'No Firebase/R8/transactions executed',
                         'No student authorship or SOLID acceptance inferred', 'Code/review/runs are Codex work under DEC-AI-AUTH-001; unresolved functional decisions and individual academic evidence remain']}
    output.mkdir(parents=True)
    (output / 'audit.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    print(json.dumps({'passed': report['passed'], 'checks': len(checks),
                      'failed': [c['name'] for c in checks if not c['passed']], 'report': str(output / 'audit.json')}))
    return 0 if report['passed'] else 1

if __name__ == '__main__':
    sys.exit(main())
