"""Create a separate local execution harness; never overwrite the product checkout."""
import argparse, hashlib, json, shutil
from pathlib import Path

parser = argparse.ArgumentParser()
parser.add_argument('--repo', type=Path, required=True)
parser.add_argument('--out', type=Path, required=True)
parser.add_argument('--sdk', type=Path, required=True)
args = parser.parse_args()
repo, out, sdk = args.repo.resolve(), args.out.resolve(), args.sdk.resolve()
assert repo != out and not out.exists(), 'Use a new directory outside the product checkout'
assert repo not in out.parents and out not in repo.parents
assert (sdk/'platforms/android-36/android.jar').is_file()
out.mkdir(parents=True)
copied = {}
names = ['build.gradle.kts', 'settings.gradle.kts', 'gradle.properties', 'gradlew', 'gradlew.bat',
         'app/build.gradle.kts', 'app/proguard-rules.pro']
names += [p.relative_to(repo).as_posix() for folder in ['gradle', 'app/src']
          for p in (repo/folder).rglob('*') if p.is_file()]
for name in names:
    source, dest = repo/name, out/name
    dest.parent.mkdir(parents=True, exist_ok=True)
    shutil.copyfile(source, dest)
    copied[name] = hashlib.sha256(source.read_bytes()).hexdigest()
build = out/'app/build.gradle.kts'
text = build.read_text(encoding='utf-8')
assert text.count('applicationId = "com.inkr8"') == 1
build.write_text(text.replace('applicationId = "com.inkr8"', 'applicationId = "com.inkr8.lab"'), encoding='utf-8')
(out/'local.properties').write_text('sdk.dir='+sdk.as_posix()+'\n', encoding='utf-8')
fixture = {'project_info': {'project_number':'123456789000','project_id':'demo-inkr8-local',
    'storage_bucket':'demo-inkr8-local.appspot.com'}, 'client':[{'client_info':{
    'mobilesdk_app_id':'1:123456789000:android:0000000000000000000000',
    'android_client_info':{'package_name':'com.inkr8.lab'}}, 'oauth_client':[
    {'client_id':'123456789000-local-fixture.apps.googleusercontent.com','client_type':3}],
    'api_key':[{'current_key':'AIza'+'0'*35}], 'services':{
    'appinvite_service':{'other_platform_oauth_client':[]}}}], 'configuration_version':'1'}
(out/'app/google-services.json').write_text(json.dumps(fixture, indent=2)+'\n', encoding='utf-8')
support = Path(__file__).resolve().parent
for name in ['LabApplication.kt', 'LabHostActivity.kt']:
    dest = out/'app/src/debug/java/com/inkr8/lab'/name
    dest.parent.mkdir(parents=True, exist_ok=True); shutil.copyfile(support/name, dest)
for name in sorted(p.name for p in support.glob('Lab*.kt') if p.name not in ['LabHostActivity.kt','LabApplication.kt']):
    dest = out/'app/src/androidTest/java/com/inkr8/lab'/name
    dest.parent.mkdir(parents=True, exist_ok=True); shutil.copyfile(support/name, dest)
(out/'app/src/debug/AndroidManifest.xml').write_text('''<manifest xmlns:android="http://schemas.android.com/apk/res/android"
 xmlns:tools="http://schemas.android.com/tools">
 <uses-permission android:name="android.permission.INTERNET"/>
 <application android:name="com.inkr8.lab.LabApplication" android:usesCleartextTraffic="true"
  tools:replace="android:name">
  <activity android:name="com.inkr8.MainActivity" tools:node="remove"/>
  <activity android:name="com.inkr8.lab.LabHostActivity" android:exported="true">
   <intent-filter><action android:name="android.intent.action.MAIN"/>
    <category android:name="android.intent.category.LAUNCHER"/></intent-filter>
  </activity>
  <provider android:name="com.google.android.gms.ads.MobileAdsInitProvider" tools:node="remove"/>
  <meta-data android:name="com.google.android.gms.ads.APPLICATION_ID" tools:node="remove"/>
  <meta-data android:name="firebase_analytics_collection_deactivated" android:value="true"/>
  <meta-data android:name="firebase_data_collection_default_enabled" android:value="false"/>
 </application>
</manifest>\n''', encoding='utf-8')
(out/'lab-preparation.json').write_text(json.dumps({'project':'demo-inkr8-local',
    'application_id':'com.inkr8.lab', 'original_sources_sha256':copied,
    'destinations':{'auth':'127.0.0.1:9099','firestore':'127.0.0.1:8080','functions':'127.0.0.1:5001'},
    'transport':'ADB reverse required on the selected test device',
    'auth':'Local anonymous test identity; Google OAuth not exercised',
    'no_original_google_services_copied':True,
    'no_original_build_versions_changed':True,
    'screen_tests_executed':False}, indent=2)+'\n', encoding='utf-8')
print('Isolated demo Android harness prepared; product checkout unchanged.')
