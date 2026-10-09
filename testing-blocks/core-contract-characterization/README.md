# Caracterización previa de A05–A07

**Vigente tras IMPL-008/009 (05/10/2026):** la suite JVM conserva sus 15 casos;
contador, timeout y predicados de estado llaman directamente a ResultWaitPolicy.kt
de producción. Clave, guardia y placement siguen siendo fragmentos extraídos.
La suite Node conserva sus 21 casos y ahora importa dynamicRatingChange.ts
directamente, usando el runtime Node comprobado; sólo el mínimo cero sigue siendo
una expresión extraída. No se requiere ni se reconstruye package/tsconfig del producto.
Resultados reales antes/después: 15/15 PASS JVM y 21/21 PASS Node. Los límites de
SDK, reloj, efectos, integración y transacciones que siguen continúan vigentes.
La descripción IMPL-005 de los extractores es histórica.

**Ampliación VER-001 (05/10/2026):** `check_rating_consumers.mjs` comprueba tipos
del módulo de producción y compila una copia byte a byte junto con las dos
llamadas escalares extraídas del motor. Compara el ejecutable generado con la
función de Git anterior (referencia explícita), comprueba la resolución del import
y exige que un argumento de outcome inválido falle con TS2345. Necesita una ruta
real a `typescript/lib/tsc.js` y un directorio de resultados nuevo:

```powershell
node testing-blocks/core-contract-characterization/check_rating_consumers.mjs --tsc C:/ruta/typescript/lib/tsc.js --report-dir C:/ruta/nueva/rating-check
```

Ejecutado desde la raíz del checkout con TypeScript **7.0.2 disponible en caché**
y Node 24.20.0: tipos/compilación pasan, 1.156 pares (2.312 deltas) sin diferencias,
control de tipos rechazado. `strict`, target ES2020 y CommonJS son ajustes explícitos
de esta comprobación aislada; no representan configuración original de Functions.
Los valores negativos, fraccionarios y no finitos caracterizan la función JS,
sin ratificarlos como datos válidos de Ranked. No se ejecutan efectos, SDKs,
transacciones, R8 ni el grafo de módulos del producto. No se repitieron las suites
JVM ni los 21 casos previos sin un cambio que lo requiriese.

Apoyo de Codex sobre lógica existente; no cambia producción ni elige ALT-01/07/13.
Versiones JVM reutilizadas de testing-baseline: Kotlin 2.4.20/JUnit 4.13.2/Gradle 8.14.5, JDK 17.

Desde la raíz del checkout con JAVA_HOME del JDK auténtico:

```powershell
.\testing-baseline\gradlew.bat -p testing-blocks/core-contract-characterization clean test --no-daemon --offline
```

Después, desde este directorio, con Node existente comprobado (v24.20.0 en la ejecución):

```powershell
node extract_rating.mjs
node --test --test-reporter=junit src/test/node/rating.test.mjs
```

extract_core.py captura literalmente getDraftKey, los predicados de estado/guardia/placement y expresiones de contador/timeout del ViewModel. Compila wrappers de prueba y el enum SubmissionStatus auténtico. PlacementFixture/StatusFixture no son Users/Submissions ni SDKs. No se ejecutan ViewModel, callbacks, consulta por ID/último, navegación, reloj/corrutinas o SharedPreferences; los tests sólo prueban los fragmentos señalados en SOURCE_MANIFEST.

extract_rating.mjs captura calculateDynamicRatingChange existente y la expresión de mínimo cero; Node elimina anotaciones TypeScript en memoria, sin cambiar el cuerpo. Se ejecutan sólo esos fragmentos, sin importar SDK, R8 o módulos del trigger. No hay TypeScript type checking, configuración Functions recuperada, transacción, idempotencia, concurrencia, escritura de rating ni placement probado. El runtime auxiliar no identifica la versión original de despliegue.

Expected caracteriza el código actual; no ratifica umbrales/fórmula/timeout/Q-R/Q-D ni corrige colisiones de clave o delta persistido. Los tests y su ejecución son Codex; el estudiante debe validar/ejecutar, justificar decisiones e intervenir en lógica crítica/integración conforme a P1. Las retiradas y temporadas quedan separadas.
