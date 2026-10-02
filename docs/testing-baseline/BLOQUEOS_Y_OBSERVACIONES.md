# Bloqueos y Observaciones

## Bloqueos del repositorio actual

1. **Ausencia de configuración Gradle del proyecto Android.** No existen `settings.gradle`, `settings.gradle.kts`, `build.gradle` ni `build.gradle.kts` en la raíz o en `app/`. No es posible construir la aplicación Android desde este clon.
2. **Ausencia de Gradle Wrapper funcional en raíz.** Sólo existe `gradle/wrapper/gradle-wrapper.properties`; faltan `gradlew`, `gradlew.bat` y `gradle-wrapper.jar`. No se completó ni corrigió el wrapper raíz.
3. **Ausencia de configuración para Functions.** No existen `package.json`, lockfile ni `tsconfig.json` en la raíz o en `functions/`. No es posible instalar, compilar o ejecutar de forma reproducible las Firebase Functions.
4. **Ausencia de tests existentes.** No existían carpetas o archivos de tests Android/Kotlin ni TypeScript en el commit inspeccionado.
5. **Referencia TypeScript faltante.** `functions/src/submissions/submissionEvaluationEngine.ts` y `functions/src/tournaments/evaluationEngine.ts` importan `../r8/evaluateWithR8`, pero `functions/src/r8/evaluateWithR8.ts` no existe en el repositorio.

Ninguno de estos problemas fue corregido durante esta tarea.

## Observaciones de ejecución

- El equipo tenía por defecto Oracle JRE 8 `1.8.0_491`, sin compilador Java. Para la ejecución registrada se utilizó un JDK Temurin 17 temporal y verificado por SHA-256. El arnés requiere un JDK completo, no sólo un JRE.
- El `gradle-wrapper.properties` incompleto de la raíz menciona Gradle `9.1.0`. Ese wrapper no se utilizó ni se alteró. El arnés aislado utiliza Gradle `8.14.5` porque es estable, compatible con Kotlin `2.4.20` y puede arrancar en entornos anteriores a Java 17.
- El arnés limita deliberadamente el `sourceSet` a siete archivos reales de producción. Esto evita compilar la aplicación Android completa y confirma que no se copiaron las implementaciones bajo prueba.
- El comportamiento de `ReputationManager.adjustReputation` que transforma un resultado exacto de cero en `+1` o `-1` queda caracterizado. No se evalúa aquí si la regla es correcta desde el punto de vista del producto.
- `TournamentEconomyCalculator` normaliza un premio no positivo a `1` y un máximo de jugadores menor que `2` a `2`. Este comportamiento existente queda protegido sin emitir juicio funcional.

## Áreas no cubiertas

- UI, navegación y componentes Jetpack Compose.
- Pruebas instrumentadas Android.
- `MainActivity`, `Inkr8App`, autenticación y anuncios.
- `AppViewModel` y repositorios.
- Firebase Auth, Firestore y Cloud Functions reales o emulados.
- Todas las Firebase Functions TypeScript, incluidas sus utilidades puras, por ausencia de configuración reproducible del subproyecto.
- Red, APIs, evaluación externa y pruebas end-to-end.
- `TimeUtils`, por depender del reloj del sistema y no ofrecer inyección temporal.
- `SubmissionFactory`, mappers y modelos ligados a tipos Firebase u otros archivos no incluidos en el arnés mínimo.
- `League.fromRating`, `FormatUtils`, `PantheonManager` y demás lógica no priorizada.

## Posibles pasos posteriores

Estas observaciones no autorizan cambios de producción. Tras revisión humana, convendría recuperar o reconstruir por separado la configuración real de Android y Functions, restaurar el módulo `r8` faltante y ampliar el baseline sólo donde el riesgo de refactorización lo justifique.
