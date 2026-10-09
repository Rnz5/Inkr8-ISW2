"""Local fixture using copied, genuinely compiled functions. No cloud credentials."""
import argparse, hashlib, json, shutil, subprocess
from pathlib import Path
parser=argparse.ArgumentParser()
parser.add_argument('--repo',type=Path,required=True)
parser.add_argument('--out',type=Path,required=True)
parser.add_argument('--enable-evaluator',action='store_true')
args=parser.parse_args();repo=args.repo.resolve();out=args.out.resolve()
assert not out.exists() and repo not in out.parents and out not in repo.parents
assert (repo/'functions/node_modules/firebase-admin/package.json').is_file()
out.mkdir(parents=True);runtime=out/'functions';runtime.mkdir()
hashes={}
for folder in ['src','lib']:
    shutil.copytree(repo/'functions'/folder,runtime/folder)
    for p in (runtime/folder).rglob('*'):
        if p.is_file():hashes[p.relative_to(runtime).as_posix()]=hashlib.sha256(p.read_bytes()).hexdigest()
for name in ['package.json','package-lock.json','tsconfig.json']:
    shutil.copyfile(repo/'functions'/name,runtime/name)
pkg=json.loads((runtime/'package.json').read_text(encoding='utf-8'));pkg['main']='lib/labEntry.js'
(runtime/'package.json').write_text(json.dumps(pkg,indent=2)+'\n',encoding='utf-8')
support=Path(__file__).resolve().parent
shutil.copyfile(support/'emulator-entry.cjs',runtime/'lib/labEntry.js')
shutil.copyfile(support/'firestore.rules',out/'firestore.rules')
# Explicit fake secret and transport fixture; never read original .env or credentials.
(runtime/'.secret.local').write_text('OPENAI_API_KEY=local-transport-fixture-not-a-secret\n',encoding='utf-8')
(runtime/'.env.local').write_text('OPENAI_BASE_URL=http://127.0.0.1:5010/v1\nLAB_ENABLE_EVALUATION='+
    ('true' if args.enable_evaluator else 'false')+'\n',encoding='utf-8')
dest=str(runtime/'node_modules').replace("'","''");source=str(repo/'functions/node_modules').replace("'","''")
subprocess.run(['powershell','-NoProfile','-Command',
    f"New-Item -ItemType Junction -Path '{dest}' -Target '{source}' | Out-Null"],check=True)
config={'functions':[{'source':'functions','codebase':'default'}],'firestore':{'rules':'firestore.rules'},
    'emulators':{'auth':{'host':'127.0.0.1','port':9099},'firestore':{'host':'127.0.0.1','port':8080},
    'functions':{'host':'127.0.0.1','port':5001},'ui':{'enabled':False},'singleProjectMode':True}}
(out/'firebase.json').write_text(json.dumps(config,indent=2)+'\n',encoding='utf-8')
(out/'lab-preparation.json').write_text(json.dumps({'project':'demo-inkr8-local',
    'original_functions_sha256':hashes,'evaluator_enabled':args.enable_evaluator,
    'provider':'explicit local HTTP double, never real R8',
    'security_rules':'test fixture, not original policy','student_authorship':False},indent=2)+'\n',encoding='utf-8')
print('Local Firebase fixture prepared; original configuration and sources retained.')
