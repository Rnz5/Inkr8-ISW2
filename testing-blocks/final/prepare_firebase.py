"""Prepare separate demo runtime with authentic compiled Functions and NEW proposed rules.
Never copy product secrets, deploy, reuse an existing output or contact a cloud project.
"""
import argparse,json,shutil,subprocess,datetime,hashlib,os
from pathlib import Path
p=argparse.ArgumentParser(description=__doc__)
p.add_argument('--repo',type=Path,required=True);p.add_argument('--out',type=Path,required=True)
args=p.parse_args();repo=args.repo.resolve();out=args.out.resolve()
assert not out.exists() and repo not in out.parents and out not in repo.parents
assert (repo/'functions/lib/submissions/submissionEvaluationEngine.js').is_file(), 'Run the authentic npm ci / npm run build first'
assert (repo/'functions/node_modules/firebase-admin/package.json').is_file()
runtime=out/'functions';runtime.mkdir(parents=True)
for name in ['lib','src']:shutil.copytree(repo/'functions'/name,runtime/name)
for name in ['package.json','package-lock.json','tsconfig.json']:shutil.copyfile(repo/'functions'/name,runtime/name)
pkg=json.loads((runtime/'package.json').read_text(encoding='utf-8'));pkg['main']='lib/labEntry.js'
(runtime/'package.json').write_text(json.dumps(pkg,indent=2)+'\n',encoding='utf-8')
(runtime/'lib/labEntry.js').write_text('''// Test runtime only. A genuine SDK, explicit local HTTP double, no cloud.
const assert=require('node:assert/strict');
assert.equal(process.env.GCLOUD_PROJECT,'demo-inkr8-local');
assert.equal(process.env.FIRESTORE_EMULATOR_HOST,'127.0.0.1:8080');
const nativeFetch=global.fetch;
global.fetch=(url,...args)=>{
 if(new URL(String(url)).origin!=='http://127.0.0.1:5010') throw Error('External HTTP forbidden');
 return nativeFetch(url,...args);
};
const adapter=require('./firebase/admin.js');
if(adapter.FieldValue===undefined) adapter.FieldValue=require('firebase-admin/firestore').FieldValue;
module.exports=require('./users/applyMeritAction.js');
Object.assign(module.exports,require('./submissions/submissionEvaluationEngine.js'));
Object.assign(module.exports,require('./submissions/deleteSubmission.js'));
Object.assign(module.exports,require('./users/accountAccess.js'));
const seasons=require('./seasons/seasonFunctions.js');
for(const name of ['seasonSubmissionCreated','seasonSubmissionUpdated','getSeasonRanking','getSeasonHistory']) module.exports[name]=seasons[name];
''',encoding='utf-8')
shutil.copyfile(repo/'security/firestore.proposed.rules',out/'firestore.rules')
(runtime/'.secret.local').write_text('OPENAI_API_KEY=local-transport-fixture-not-a-secret\n',encoding='utf-8')
activation=int(datetime.datetime.now(datetime.timezone.utc).timestamp()*1000)
(runtime/'.env.local').write_text('OPENAI_BASE_URL=http://127.0.0.1:5010/v1\nSEASON_ACTIVATED_AT_MS='+str(activation)+'\n',encoding='utf-8')
if os.name=='nt':
 source=str(repo/'functions/node_modules').replace("'","''");dest=str(runtime/'node_modules').replace("'","''")
 subprocess.run(['powershell','-NoProfile','-Command',f"New-Item -ItemType Junction -Path '{dest}' -Target '{source}' | Out-Null"],check=True)
else:(runtime/'node_modules').symlink_to(repo/'functions/node_modules',target_is_directory=True)
config={'functions':[{'source':'functions','codebase':'default'}],'firestore':{'rules':'firestore.rules'},'emulators':{x:{'host':'127.0.0.1','port':port} for x,port in [('auth',9099),('firestore',8080),('functions',5001)]}}
config['emulators'].update({'ui':{'enabled':False},'singleProjectMode':True})
(out/'firebase.json').write_text(json.dumps(config,indent=2)+'\n',encoding='utf-8')
(out/'lab-preparation.json').write_text(json.dumps({'project':'demo-inkr8-local','rule_source':'NEW FIN-001 proposed rules, not original','activation':activation,'R8':'HTTP double fixed80; external forbidden','secrets_copied':False,'source_sha256':{q.relative_to(repo).as_posix():hashlib.sha256(q.read_bytes()).hexdigest() for q in (repo/'functions/src').rglob('*.ts')}},indent=2)+'\n',encoding='utf-8')
print('Separate demo runtime prepared. Start only with --project demo-inkr8-local. No deployment.')
