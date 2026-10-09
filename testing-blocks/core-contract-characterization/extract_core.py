"""Extract existing pure/key/decision fragments; never wire Android or Firebase."""
import hashlib
import json
from pathlib import Path
import re
import sys

repo=Path(__file__).resolve().parents[2]
target=Path(sys.argv[1]).resolve()
assert target==Path(__file__).resolve().parent/'build/generated/core'
records={}

def capture(name,path,pattern):
    raw=(repo/path).read_bytes();source=raw.decode('utf-8').replace('\r\n','\n')
    matches=list(re.finditer(pattern,source,re.S));assert len(matches)==1,(name,len(matches))
    m=matches[0];body=m.group('body')
    records[name]={'path':path,'source_sha256':hashlib.sha256(raw).hexdigest(),
        'start_line':source[:m.start('body')].count('\n')+1,'end_line':source[:m.end('body')].count('\n')+1,
        'fragment':body,'fragment_sha256_lf':hashlib.sha256(body.encode()).hexdigest()}
    return body

draft='app/src/main/java/com/inkr8/utils/DraftManager.kt'
vm='app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt'
key=capture('draftKey',draft,r'    (?P<body>fun getDraftKey\(gamemode: String, playmode: String, tournamentId: String\?\): String \{.*?\n    \})\n\}')
guard=capture('acceptUpdateGuard',vm,r'private fun handleSubmissionUpdate\(submission: Submissions\) \{\n        if \((?P<body>[^\n]+)\) \{')
placement=capture('placementReveal',vm,r'val justGotPlaced = (?P<body>[^\n]+)\n')
policy='app/src/main/java/com/inkr8/viewmodel/ResultWaitPolicy.kt'
policy_raw=(repo/policy).read_bytes()
records['production_wait_policy']={'path':policy,'source_sha256':hashlib.sha256(policy_raw).hexdigest()}
for call in ['resultWaitElapsedSeconds(pollCount)', 'hasResultWaitTimedOut(loadingElapsedSeconds)',
             'isEvaluatedResult(submission.status)', 'isFailedResult(submission.status)']:
    assert (repo/vm).read_text(encoding='utf-8').count(call)==1, call

generated='''// GENERATED TEST ONLY; not Android/Firebase DTOs or implementation.
package com.inkr8.characterization
import com.inkr8.data.SubmissionStatus

data class PlacementFixture(val isPlaced: Boolean, val hasSeenPlacementReveal: Boolean)
data class StatusFixture(val status: SubmissionStatus)

object DraftKeySourceProbe {
    '''+key+'''
}
object WaitSourceProbe {
    fun acceptsUpdate(loadingResolved: Boolean, loadingTimeout: Boolean): Boolean = '''+guard+'''
    fun revealsPlacement(previousUserIsPlaced: Boolean, currentUser: PlacementFixture): Boolean = '''+placement+'''
}
'''
output=target/'com/inkr8/characterization/CoreSourceProbe.kt';output.parent.mkdir(parents=True,exist_ok=True)
output.write_text(generated,encoding='utf-8',newline='\n')
manifest={'fragments':records,'generated_sha256':hashlib.sha256(generated.encode()).hexdigest(),
    'scope':'Scalar fragments and authentic SubmissionStatus only; no ViewModel, callbacks, elapsed wall clock, DraftManager storage, Android or Firebase.'}
(target/'SOURCE_MANIFEST.json').write_text(json.dumps(manifest,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
