"""Compile attributable Writing expressions, not a replacement Android screen.

Bodies and the membership predicate are extracted verbatim with fail-closed markers.
The generated wrappers and SelectedWordFixture exist only in this isolated harness.
No Compose, Firebase, SubmissionFactory, serialization, or callbacks are simulated.
"""
import hashlib
import json
from pathlib import Path
import re
import sys

repo = Path(__file__).resolve().parents[2]
source_path = repo / "app/src/main/java/com/inkr8/screens/Writing.kt"
source_bytes = source_path.read_bytes()
source = source_bytes.decode("utf-8").replace("\r\n", "\n")
output = Path(sys.argv[1]).resolve()
assert output == Path(__file__).resolve().parent / "build/generated/writing"
output.mkdir(parents=True, exist_ok=True)
fragments = {}


def capture(name, pattern):
    matches = list(re.finditer(pattern, source, re.S))
    assert len(matches) == 1, f"Expected exactly one authentic {name} source fragment"
    match = matches[0]
    body = match.group("body")
    fragments[name] = {
        "start_line": source[:match.start("body")].count("\n") + 1,
        "end_line": source[:match.end("body")].count("\n") + 1,
        "source": body,
        "sha256_lf": hashlib.sha256(body.encode("utf-8")).hexdigest(),
    }
    return body


bodies = {}
for name in ["wordCount", "normalizedUserWords"]:
    bodies[name] = capture(name, r"    val " + name + r" by remember \{\n        derivedStateOf \{\n(?P<body>.*?)\n        \}\n    \}")
admission_call = capture("canSubmit_call", r"    val canSubmit by remember \{\n        derivedStateOf \{\n(?P<body>.*?)\n        \}\n    \}")
assert admission_call == "            isWritingAdmitted(userText, gamemode) { wordCount }"
admission_path = repo / "app/src/main/java/com/inkr8/evaluation/WritingAdmission.kt"
predicate = capture("wordsUsed", r"wordsUsed = selectedWords\.filter \{\n(?P<body>.*?)\n                                \},")
declarations = []
for name in ["writingWhitespacePattern", "writingWordBoundaryPattern"]:
    matches = list(re.finditer(r"^private val " + name + r" = (?P<body>[^\n]+)$", source, re.M))
    assert len(matches) <= 1
    if matches:
        match = matches[0]
        declarations.append(match.group(0))
        fragments[name] = {"start_line": source[:match.start()].count("\n") + 1,
                           "end_line": source[:match.end()].count("\n") + 1,
                           "source": match.group(0),
                           "sha256_lf": hashlib.sha256(match.group(0).encode()).hexdigest()}

# Approved functional lifecycle differs from the historic pre-confirmation cleanup.
# Inspect the actual submission event; do not claim its old order was conserved.
event_start = source.index("                        if (canSubmit && pendingId == null && !isPersisting) {")
event = source[event_start:source.index("                    enabled = canSubmit", event_start)]
order_markers = ["if (canSubmit && pendingId == null && !isPersisting) {",
                 "val qualityCheck = ValidationUtils.isContentLowQuality(userText)",
                 "if (qualityCheck.first) {", "return@Button", "val submission = SubmissionFactory.create(",
                 "wordsUsed = selectedWords.filter {", "val sentRevision = revision",
                 "pendingId = submission.id", "saveCurrentRevision()", "onAddSubmission(submission, {",
                 "if (revision == sentRevision && storedRevision == sentRevision && userText == sentText)",
                 "DraftManager.confirmRevision(context, it, sentRevision)", 'userText = ""']
positions = [event_start + event.index(marker) for marker in order_markers]
assert positions == sorted(positions), "Approved submission lifecycle markers changed; inspect production callbacks."

generated = '''// GENERATED TEST HARNESS from Writing.kt; never used by the Android app.
package com.inkr8.blocks

import com.inkr8.data.Gamemode

''' + "\n".join(declarations) + '''

// Scalar fixture, not the Firebase-backed Words model or a DTO.
data class SelectedWordFixture(val id: String, val word: String)

object WritingSourceProbe {
    fun wordCount(userText: String): Int {
        return
''' + bodies["wordCount"] + '''
    }

    fun normalizedUserWords(userText: String): Set<String> {
        return
''' + bodies["normalizedUserWords"] + '''
    }

    fun wordsUsed(normalizedUserWords: Set<String>, selectedWords: List<SelectedWordFixture>): List<SelectedWordFixture> {
        return selectedWords.filter {
''' + predicate + '''
        }
    }
}
'''
# A Kotlin return cannot have its expression on the following statement line.
# Only wrapper syntax is inserted; authentic captured expression bodies stay intact.
generated = generated.replace("        return\n", "        return ")
target = output / "com/inkr8/blocks/WritingSourceProbe.kt"
target.parent.mkdir(parents=True, exist_ok=True)
target.write_text(generated, encoding="utf-8", newline="\n")
manifest = {"source": str(source_path), "writing_sha256_raw": hashlib.sha256(source_bytes).hexdigest(),
            "production_admission_path": str(admission_path), "production_admission_sha256": hashlib.sha256(admission_path.read_bytes()).hexdigest(),
            "fragment_source_normalization": "CRLF to LF only", "fragments": fragments,
            "event_order_static_only": [{"marker": marker, "line": source[:position].count("\n") + 1}
                                       for marker, position in zip(order_markers, positions)],
            "generated_sha256": hashlib.sha256(target.read_bytes()).hexdigest(),
            "boundary": "Scalar expression execution and static event order; no Compose or Firebase integration."}
(output / "SOURCE_MANIFEST.json").write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
print("Extracted count, tokens and membership; tests compile production admission. Android screen not compiled.")
