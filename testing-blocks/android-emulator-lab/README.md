# Laboratorio AND-002

**Resultado posterior CORE-001 / FIX-001 (06/10/2026):** las anotaciones de
`Users.isPlaced/isPhilosopher` corrigen el mapeo de los nombres del servidor. La
prueba roja original de LabWaitTest pasa sin cambiarla. Ocho métodos Android
pertinentes únicos: cinco PASS, tres FAIL conservados (modo del servidor, borrador
rechazado e identidad A/B). Este total corresponde a las selecciones de CORE-001,
no a una repetición completa de las 14 pruebas históricas. El listener de usuario
y la decisión de navegar a placementReveal fueron ejecutados con actualizaciones
de evaluación explícitas; no acreditan placement automático del backend.

La preparación existente incorpora `LabDataContractTest` y
`LabCoreAcceptanceTest`. En un laboratorio ya preparado, sincronizar esas dos
fuentes y el Users.kt actual, compilar y seleccionar los métodos afectados; no
recrear SDK/AVD/configuración ni ejecutar el callable si Functions no está activo.

```powershell
adb -s emulator-5560 shell am instrument -w -r -e class com.inkr8.lab.LabDataContractTest,com.inkr8.lab.LabCoreAcceptanceTest,com.inkr8.lab.LabWaitTest#serverProfileFlagsAreReadByTheActualUserModel com.inkr8.lab.test/androidx.test.runner.AndroidJUnitRunner
```

Los tres FAIL son sondas de aceptación deliberadas, con aserciones intactas.
Las caracterizaciones antiguas siguen separadas. Capturar
`/data/user/0/com.inkr8.lab/files/core-mode-payload.json` y usar
`gamemode-contract-check.cjs <functionsRoot> <captura.json> <resultado-nuevo.json>`
con `GCLOUD_PROJECT=demo-inkr8-local` y
`FIRESTORE_EMULATOR_HOST=127.0.0.1:8080`, sin credenciales externas. La sonda consume
el payload real Android, adapta sólo contenido/autor en otro documento sintético
y reproduce el motor recibiendo STANDARD en lugar de ON_TOPIC. R8 y CloudEvent
son dobles explícitos. No corrige la compatibilidad de campos ni el contexto
temático por suposición.

Para Ranked, `testing-blocks/rating-characterization/firestore-invariants-check.cjs`
usa las mismas variables de entorno, admite `<functionsRoot> <prefijo-nuevo>
<resultado-nuevo.json>` y un cuarto argumento opcional `hold`. Siete sondas
principales: una PASS y seis FAIL; `hold` ejecuta sólo la sonda adicional del
reparto líquido/reserva y falla. Usa una base nombrada **real** dentro del mismo
emulador demo por prefijo: binding del destino sólo en tests, no un doble de
transacciones. R8, entrega CloudEvent y la barrera de programación son dobles;
no vacía ni modifica datos anteriores. El cliente se cierra tras terminar las
transacciones iniciadas por el motor. Los casos no prueban entrega de producción.

Los apartados siguientes conservan los antecedentes y comandos de AND-002;
su fallo de flags es histórico, no el resultado vigente.

Soporte de verificación de Codex. No es configuración original ni integración
estudiantil. Copia las fuentes actuales en otra carpeta y conserva las versiones
auténticas. Nunca instalar aquí el APK original configurado para la nube.

## Preparar y compilar

Usar Python y directorios **nuevos fuera del checkout**:

```powershell
python testing-blocks/android-emulator-lab/prepare.py --repo $repoRoot --out $androidLab --sdk $sdkPath
python testing-blocks/android-emulator-lab/prepare_firebase.py --repo $repoRoot --out $firebaseLab
```

`$repoRoot`, `$androidLab`, `$firebaseLab` y `$sdkPath` son rutas absolutas.
El primer script excluye el Google Services original, genera identificadores
ficticios para `demo-inkr8-local/com.inkr8.lab`, desactiva Analytics y aloja los
composables auténticos. No arranca MainActivity/Ads/Google OAuth.
El segundo copia Functions ya compiladas, reutiliza las dependencias instaladas
por junction y selecciona sólo el callable auténtico applyMeritAction.
Se comprobó la preparación del segundo script; su copia de verificación no se lanzó.

Con JDK 17 y el wrapper original, desde `$androidLab`:

```powershell
.\gradlew.bat --no-daemon :app:assembleDebug :app:assembleDebugAndroidTest --console=plain
```

SDK: API 36/Build Tools 36.0.0. CLI Firebase local 15.32.1, Node 20.20.2 y Java 21
son herramientas del laboratorio; no reemplazan versiones Android/Functions.
Desde `$firebaseLab`, con esas herramientas en PATH y sin credenciales de nube:

```powershell
$env:FUNCTIONS_DISCOVERY_TIMEOUT = '60'
firebase emulators:start --only auth,firestore,functions --project demo-inkr8-local --config firebase.json
```

Para conservar sesiones previas, usar `--import` con el export sintético elegido;
exportar a una carpeta nueva antes de detener los emuladores. No vaciar colecciones
ni reutilizar prefijos de las pruebas transaccionales.

## Dispositivo aislado

AVD AOSP API36 de prueba, WHPX comprobado con `emulator -accel-check` exit 0.
Usar sólo su serial autorizado. AND-002 usó `emulator-5560`; no había teléfono USB.
Los servicios están en localhost del PC. Después de **cada reinicio de adbd**,
incluido `adb root`, restaurar:

```powershell
adb -s emulator-5560 reverse tcp:9099 tcp:9099
adb -s emulator-5560 reverse tcp:8080 tcp:8080
adb -s emulator-5560 reverse tcp:5001 tcp:5001
adb -s emulator-5560 reverse --list
```

AND-002 restringió la salida IPv4/IPv6 del UID de `com.inkr8.lab` al loopback
mediante iptables en ese AVD con root. No aplicar estas reglas a un teléfono ni a
otra app. El registro ANDROID_LAB_NETWORK_ISOLATION identifica UID/comandos.
Se verificaron proyecto, manifiesto y destinos antes de los flujos; Auth/Firestore
son emuladores reales. No se validaron Google OAuth, App Check o Installations.

Instalar los dos APK de **esta copia** y ejecutar los tests del laboratorio:

```powershell
adb -s emulator-5560 install -r "$androidLab/app/build/outputs/apk/debug/app-debug.apk"
adb -s emulator-5560 install -r "$androidLab/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk"
adb -s emulator-5560 shell am instrument -w -r -e class com.inkr8.lab.LabScreenTest,com.inkr8.lab.LabWaitTest,com.inkr8.lab.LabCallableTest com.inkr8.lab.test/androidx.test.runner.AndroidJUnitRunner
```

Interpretar `INSTRUMENTATION_STATUS_CODE` y el resumen JUnit: ADB exit 0 también
ocurre si fallan tests. El timeout usa 93 segundos **reales**. El probe de flags
de perfil queda deliberadamente rojo mientras `isPlaced` no se lea correctamente.
Las caracterizaciones de pérdida de borrador y resolución por B describen defectos;
un PASS de esas pruebas no satisface los requisitos humanos correspondientes.

## Functions y transacciones

Para entrega automática crear otro laboratorio con `--enable-evaluator` y
arrancar primero `node testing-blocks/android-emulator-lab/r8-transport-double.cjs`.
Esto habilita el motor auténtico y su SDK R8/OpenAI contra el doble HTTP
127.0.0.1:5010. La clave de `.secret.local` es ficticia; no copiar originales.
El loader bloquea HTTP externo del evaluador. `source: real` en una respuesta
del motor sigue siendo **transporte simulado**, no prueba del proveedor.

La CLI envuelve `admin.firestore` con bind y pierde `FieldValue` estático. El
loader local obtiene la clase real del módulo `firebase-admin/firestore` cuando
falta; no reemplaza transacciones ni modifica el adaptador del producto.

`functions-delivery-check.cjs` crea datos sintéticos, observa una entrega automática
y llama al endpoint auténtico con Auth local. `firestore-replay-check.cjs` usa el
motor emitido antes/después, R8 doble y `.run` con snapshots creados sintéticos;
la carrera tiene una barrera explícita que permite elegir el mismo candidato antes
de escribir. Son dos alcances distintos. Replays/carrera conservan defectos previos.

Variables obligatorias: `GCLOUD_PROJECT=demo-inkr8-local` y
`FIRESTORE_EMULATOR_HOST=127.0.0.1:8080`; sin GOOGLE_APPLICATION_CREDENTIALS
ni OPENAI_API_KEY del proveedor. Las pruebas rechazan otros destinos.
Evidencia y resultados: [ANDROID_EXECUTION](../../docs/ANDROID_EXECUTION.md).

## Conexión A05 y seguimiento A06

A05-CON-001 conecta AppRoot y Writing al éxito real del Task: tres probes de
AppRoot y dos existentes PASS. La caracterización histórica de pérdida describe
el código anterior; no ejecutarla como aceptación de la corrección. A06-CON-001
transporta el UUID confirmado al mismo documento en listener y sondeo.
LabIdentityAcceptanceTest verifica sondeo sin listener y respuestas obsoletas;
estas últimas usan una entrega por reflexión explícita, no una entrega Firebase.
Las políticas de FAILED/error/timeout/tardío se verifican con los métodos
existentes de LabWaitTest. Usar filtros de métodos pertinentes, no asumir que
todas las caracterizaciones de defectos deben seguir pasando tras corregirlos.
No OAuth/Ads/R8/proyecto cloud por estas comprobaciones.

## A07-CORE-001 — recorrido automático local

LabIntegratedFlowTest usa AppRoot y Android SDK auténticos, entrega automática
Firestore→Functions y evaluateWithR8 auténtico con HTTP local doble (80 puntos).
No OAuth, proveedor real, anuncios, entrada/cobro Ranked o aceptación integral.
Practice y Ranked con candidato previo PASS. Emparejamiento posterior a Results
queda rojo: servidor MATCHED/+1, VM PENDING/0. No eliminar esa aserción ni contar
la caracterización antigua de duplicaciones como aceptación del control atómico.
El selector exacto 80.00% corrige ambigüedad del soporte; no cambia el resultado
esperado. Fixtures preservadas y datos exportados aparte. Consulte el registro
A07 y su decisión pendiente antes de cambiar actualización de Results.

## A06-RES-001 — DEC-RES-001

Marco eligió actualizar Results del mismo envío. VM añade un solo efecto de
refresco para EVALUATED/RANKED/MATCHED del ID/generación vigentes, con Results
visible, ya resuelto y sin timeout. No navega ni cambia políticas de espera.
La sonda automática posterior A07 se conserva y se vuelve a ejecutar con sus
aserciones intactas. LabResultRefreshTest entrega callbacks por reflexión y
configura timeout como doble explícito para comprobar guardias; no repite ni
sustituye la evidencia anterior del timer real de 93 segundos. Las observaciones
JSON se toman antes de esperar la actualización UI; los resultados JUnit finalizan
la aceptación. Android original sigue separado del APK demo sin datos cloud.

## CORE-CLOSE-001 — lectores compatibles y límites finales

DEC-MODE-001 prioriza gamemode y usa gamemodeName si falta, sin cambiar escritor,
Playmode o fallbacks inválidos por capa. LabModeCompatibilityTest ejerce siete
API de repositorio/matriz de payloads y dos recorridos AppRoot→Results automáticos
con HTTP R8 doble. LabDataContractTest usa el lector real y conserva la aserción
roja original. Results admite GHOST del mismo ID; ese test es callback reflejado.
Ghost/lifecycle scripts usan SDK real y barreras explícitas. Matching ahora es
esperado por el trigger; stats/pruning legacy no tienen garantía durable.
LabDraftConcurrencyTest deja roja la pérdida de nueva revisión mientras se guarda
A; el segundo clic caracteriza dos intenciones, no persistencia duplicada. No
ejecutar esa caracterización como aceptación de una política nueva.
Estado/evidencia/rutas y tabla A01–A07: CORE_CLOSURE_AUDIT. Antecedentes rojos
anteriores conservados; los 198 contrastes del auditor son estáticos.
