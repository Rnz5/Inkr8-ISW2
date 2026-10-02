# Baseline de Testing Pre-Refactor

- Proyecto: Inkr8
- Fecha: 2026-10-02
- Rama: `master`
- Commit exacto: `3d107ae6bb93f7c9d4bf9e103eccac1ae2d931de`
- Objetivo: capturar un conjunto mínimo de comportamientos deterministas existentes antes de una refactorización, sin modificar código de producción.
- Estado general del sistema: el repositorio contiene una aplicación Android/Kotlin y Firebase Functions/TypeScript, pero no contiene la configuración necesaria para construir ninguno de los dos proyectos desde la raíz. El baseline se ejecuta mediante un arnés Kotlin/JVM aislado.
- Framework de testing utilizado: Kotlin Test `2.4.20` sobre JUnit `4.13.2`.
- Build runner: Gradle Wrapper `8.14.5`.
- JVM objetivo del bytecode: Java 8.
- JDK utilizado en la ejecución registrada: Eclipse Temurin `17.0.20.1+1`.
- Comando para ejecutar las pruebas: `./testing-baseline/gradlew -p testing-baseline clean test --no-daemon` en macOS/Linux o `.\testing-baseline\gradlew.bat -p testing-baseline clean test --no-daemon` en Windows, con `JAVA_HOME` apuntando a un JDK 8 o superior compatible con Gradle 8.14.5.
- Número total de pruebas: 22.
- Resultado general: **PASS — 22 aprobadas, 0 fallidas, 0 omitidas**.

## Compatibilidad de versiones

- Kotlin `2.4.20` declara compatibilidad completa con Gradle `7.6.3` a `9.7.0`.
- Gradle `8.14.5` pertenece a la última línea 8.x y puede ejecutarse con Java 8 a Java 24; se eligió en lugar del Gradle 9.1 indicado por el archivo raíz incompleto porque Gradle 9 requiere Java 17 para el daemon.
- `kotlin-test-junit` `2.4.20` utiliza JUnit 4; JUnit `4.13.2` se fijó explícitamente para que la resolución sea reproducible.

Referencias de compatibilidad:

- [Kotlin: configurar un proyecto Gradle](https://kotlinlang.org/docs/gradle-configure-project.html)
- [Gradle: matriz de compatibilidad](https://docs.gradle.org/current/userguide/compatibility.html)
- [Kotlin: pruebas JVM con JUnit](https://kotlinlang.org/docs/jvm-test-using-junit.html)

## Archivos/clases protegidos

| Clase/Módulo | Comportamiento protegido | Nº tests |
|---|---|---:|
| `RankedCostCalculator` | Coste base, topes de rachas, umbrales de reputación, combinación y mínimo | 5 |
| `TournamentEconomyCalculator` + `TournamentEconomyProjection` | Proyección nominal, entradas inválidas y redondeo ascendente | 3 |
| `TournamentRewardCalculator` | Participantes no positivos, participante único y reparto para diez jugadores | 3 |
| `EconomyConfig` | Incrementos del coste de guardado y límites del multiplicador de racha | 2 |
| `ReputationManager` | Límites, cruce por cero, eventos y deriva diaria | 5 |
| `ValidationUtils` | Longitud mínima, palabra excesiva, repetición y contenido aceptado | 4 |
| **Total** |  | **22** |

## Alcance

El baseline compila directamente los siete archivos Kotlin reales seleccionados bajo `app/src/main/java/`. El arnés no contiene copias ni reimplementaciones de su lógica. El `sourceSet` principal incluye únicamente esos archivos para evitar arrastrar Android, Compose, Firebase o servicios externos.

El baseline cubre reglas deterministas de economía, recompensas, reputación y validación. No pretende medir cobertura total ni probar la aplicación completa.

Quedan fuera: UI Android/Compose, instrumentación, `ViewModel`, repositorios, autenticación, Firebase, publicidad, red, persistencia, Cloud Functions TypeScript, integración, end-to-end, lógica dependiente del reloj y clases que requieren modelos o SDK externos. Los motivos y bloqueos se detallan en `BLOQUEOS_Y_OBSERVACIONES.md`.
