$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path $PSScriptRoot -Parent
Push-Location $projectRoot
try {
    $violations = @(
        rg -n '^import (android\.|androidx\.|com\.google\.firebase|com\.inkr8\.data|com\.inkr8\.presentation)' domain/src/main
        rg -n '^import (com\.google\.firebase|com\.inkr8\.data|com\.inkr8\.di)' app/src/main/java/com/inkr8/presentation
        rg -n '\bDraftRepository\b' app/src/main/java/com/inkr8/presentation
        rg -n 'GamePolicy\.(wordCount|validateWriting)\s*\(' app/src/main/java/com/inkr8/presentation/ui
        rg -n 'from ["''](firebase|openai|.*(/data/|/presentation/))' functions/src/domain
        rg -n -i 'tournament|torneo|reputation|admob|play-services-ads|meritHold|philosopher|paywall' app/src/main functions/src domain/src/main gradle app/build.gradle.kts
    )
    if ($violations.Count -gt 0) { throw ($violations -join "`n") }
    Write-Output 'Architecture boundaries and excluded systems: PASS'
} finally { Pop-Location }
