# Resultados Pre-Refactor

## Ejecución registrada

Comando exacto utilizado en PowerShell:

```powershell
$env:JAVA_HOME='C:\Users\marco\AppData\Local\Temp\inkr8-baseline-jdk17\expanded\jdk-17.0.20.1+1'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
.\testing-baseline\gradlew.bat -p testing-baseline clean test --no-daemon
```

Entorno relevante:

- Windows 10 `10.0 amd64`.
- Eclipse Temurin JDK `17.0.20.1+1`.
- Gradle Wrapper `8.14.5`.
- Kotlin/JVM `2.4.20`, target JVM `1.8`.
- Kotlin Test JUnit `2.4.20`.
- JUnit `4.13.2`.
- Rama `master`.
- Commit `3d107ae6bb93f7c9d4bf9e103eccac1ae2d931de`.

Resultado final:

- Tests ejecutados: 22.
- PASS: 22.
- FAIL: 0.
- SKIPPED: 0.
- Errores: 0.
- Duración informada por Gradle: 26 s para `clean test`.
- Tiempo acumulado de los casos en XML: 0.099 s.

| Test | Resultado |
|---|---|
| `RankedCostCalculatorTest.neutral input preserves base cost` | PASS |
| `RankedCostCalculatorTest.streak modifiers stop at their current caps` | PASS |
| `RankedCostCalculatorTest.reputation modifiers change at current thresholds` | PASS |
| `RankedCostCalculatorTest.combined streak and reputation modifiers are truncated to long` | PASS |
| `RankedCostCalculatorTest.calculated cost never falls below one` | PASS |
| `TournamentEconomyCalculatorTest.nominal projection preserves current fee and profit calculations` | PASS |
| `TournamentEconomyCalculatorTest.invalid values are coerced to current safe minimums` | PASS |
| `TournamentEconomyCalculatorTest.entrance fee rounds upward when target revenue is not divisible` | PASS |
| `TournamentRewardCalculatorTest.non-positive player counts produce no rewards` | PASS |
| `TournamentRewardCalculatorTest.a single player receives the complete reward` | PASS |
| `TournamentRewardCalculatorTest.ten-player rewards preserve winner count order and total` | PASS |
| `EconomyConfigTest.save-submission cost increases at each group of three` | PASS |
| `EconomyConfigTest.streak multiplier follows every current boundary` | PASS |
| `ReputationManagerTest.reputation is clamped at both limits` | PASS |
| `ReputationManagerTest.positive delta that reaches zero crosses to positive one` | PASS |
| `ReputationManagerTest.negative delta that reaches zero crosses to negative one` | PASS |
| `ReputationManagerTest.event helpers preserve their current deltas` | PASS |
| `ReputationManagerTest.daily drift moves one point toward zero` | PASS |
| `ValidationUtilsTest.content shorter than fifty trimmed characters is rejected` | PASS |
| `ValidationUtilsTest.a word longer than thirty-five characters is rejected` | PASS |
| `ValidationUtilsTest.ten highly repetitive words are rejected` | PASS |
| `ValidationUtilsTest.diverse readable content is accepted` | PASS |

## Intentos preliminares visibles

No se ocultaron los fallos encontrados durante la preparación:

1. La ejecución inicial con el JRE 8 instalado por defecto no llegó a compilar porque no incluía `javac` (`JAVA_COMPILER`).
2. La primera ejecución con JDK 17 detectó targets incompatibles: Java `17` frente a Kotlin `1.8`. Se alinearon ambos targets a JVM 8 únicamente en `testing-baseline/build.gradle.kts`.
3. La primera ejecución completa obtuvo 21 PASS y 1 FAIL porque el resultado esperado escrito inicialmente para la proyección nominal era incorrecto. La salida observada del código real fue entrada `560`, ingreso `11200` y beneficio `860`; se corrigió exclusivamente la expectativa del test de caracterización. Producción no se modificó.

## Conclusión

El baseline queda **APTO** para comenzar una refactorización de las clases protegidas. La suite final es reproducible con un JDK completo y todos los casos pasan. Esta conclusión no implica que la aplicación Android completa ni las Firebase Functions puedan construirse desde el clon actual; esos bloqueos permanecen documentados y fuera del alcance autorizado.
