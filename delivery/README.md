# Entrega Inkr8 FIN-002 para revisión

**INT-001 — integración autorizada en master,09/10/2026:**
[PR #2 fusionado](https://github.com/Rnz5/Inkr8-ISW2/pull/2) mediante merge commit
[`6165415`](https://github.com/Rnz5/Inkr8-ISW2/commit/6165415ca9538c3a87c854caab2269c5cff6776f).
La rama `codex/refactorizacion-por-bloques` permanece en `180837d` como referencia.
El árbol fusionado coincide exactamente con la entrega comprobada:0 cambios de
contenido,124 hashes de artefactos y383 archivos del ZIP previo verificados.
Código/pruebas/documentación e integración: Codex, con autorización de Marco;
la revisión del equipo será posterior, no se acredita revisión humana realizada.
Cierre local comprobado; R8 HTTP doble, proveedor/cloud/IAM/compras y obligaciones
individuales auténticas pendientes. Sin despliegue ni cambios de datos reales.
Los registros inferiores de PR en borrador/sin fusión son antecedentes preservados.

Registro verificable: [INTEGRATION.json](INTEGRATION.json). Informe, diagramas y
APK conservan los bytes probados anteriores a la fusión. El ZIP FIN-002 anterior
corresponde a180837d; la descarga del repositorio integrado incorpora este registro
documental. Esta distinción no implica un nuevo build o validación funcional.

Fuentes auténticas recuperadas + núcleo A01–A07 corregido y refactorizado,
retiradas de torneos/ligas/reputación, Ranked/rating/Merit conservados y temporadas.
Código, pruebas, revisión e informe derivados: Codex bajo DEC-AI-AUTH-001 comunicado
por Marco. PR #2 integrado en master por autorización posterior explícita; revisión
del equipo y validaciones indispensables pendientes. No hacer deploy ni conectar
a datos reales mediante estos comandos.

## Contenido y resultados

- `Informe-Inkr8-P4.docx` y `.pdf`: 13 capítulos oficiales, 23 páginas revisadas;
  visión/ODS del informe humano citados, ejemplos/sprints/contribuciones no inventados.
- `uml/`: 6 fuentes PlantUML, SVG/PNG; `source-map.json` mensajes→métodos→líneas/SHA.
- `inkr8-lab-debug.apk`: paquete **com.inkr8.lab**, **demo-inkr8-local** solamente.
  Abre host local sin instrumentación; entrada Auth→AppRoot comprobada. Se omiten
  MainActivity/Google OAuth/Ads, R8 HTTP doble. Requiere servicios locales+ADB reverse.
- `inkr8-lab-androidTest.apk`: necesario sólo para sondas automáticas. 8 métodos
  finales PASS, incluidas las dos sondas pendientes; no fue un fallo de producción.
- `evidence/` y `evidence-index.json`: copias fieles de resultados rojos/verdes
  pertinentes y sus hashes, sin secretos/exports originales privados.
- FIN-00139 seguridad PASS, FIN-0026 nombre-atómico PASS, 14 operación PASS,
  token-rojo1/1→2verde; 289 contrastes **estáticos**, separados de pruebas funcionales.
- Historial de suites CORE/SEA reutilizado; no se suman ejecuciones repetidas.

Reglas `security/firestore.proposed.rules`: propuesta **NUEVA**, no original ni
desplegada. Acceso owner, cross-user denegado y temporadas/dinero Admin-only
comprobados localmente. No valida por sí sola antiabuso de metadata/reservas de
sesión. Delete directo denegado; el cierre de acceso aprobado usa callable/disable/
revoke y conserva perfil/historial/economía. Comprobado localmente, IAM cloud pendiente.
Philosopher conserva verificación de compras placeholder previo; se retiró token
del log, pero falta Google real. R8 externo, OAuth/Ads/billing y scheduler cloud
no acreditados. Por estos límites la entrega no es cierre cloud/académico completo.

## Dependencias comprobadas y preparación separada

JDK17 para Gradle; **JDK21 para emulador Firestore/CLI**; Node20.20.2, Firebase CLI
15.32.1; Android API36, Build Tools36.0.0, SDK/platform-tools y AVD acelerado.
`environment.json` y lockfiles fijan versiones; no se sustituyeron versiones originales.
Obtener herramientas oficiales y aceptar sus licencias en el equipo receptor.
El Google Services original es privado y NO se entrega; el laboratorio genera
identificadores ficticios. No reemplazar la configuración del producto por el demo.

PowerShell, rutas absolutas nuevas **fuera del checkout**:

```powershell
$repoRoot = (Get-Location).Path
$androidLab = 'C:/inkr8-lab/android'
$firebaseLab = 'C:/inkr8-lab/firebase'
$sdkPath = $env:ANDROID_HOME
# Node20 en PATH; conserva scripts/configuración del producto
Push-Location functions
npm ci
npm run build
Pop-Location
python testing-blocks/android-emulator-lab/prepare.py --repo $repoRoot --out $androidLab --sdk $sdkPath
python testing-blocks/final/prepare_firebase.py --repo $repoRoot --out $firebaseLab
# JDK17 para estos builds
Push-Location $androidLab
.\gradlew.bat --no-daemon :app:assembleDebug :app:assembleDebugAndroidTest :app:testDebugUnitTest --console=plain
Pop-Location
```

La nueva preparación Firebase copia fuentes compiladas auténticas, rules propuestas,
secret ficticio, cutoff de activación local y loader que bloquea HTTP externo. Reutiliza
node_modules mediante junction, sin copiarlos al paquete. FieldValue del loader usa
la misma clase real del SDK; la producción no necesita ese arreglo de CLI bind.
Nunca usar los scripts de fixtures como servicios públicos o API del producto.

## Arranque local sin despliegue

Tres terminales con Node20; Java21 en el terminal Firebase. Instalar CLI15.32.1
en una carpeta de herramientas local, o usar `npx --yes firebase-tools@15.32.1`.
Autenticarse en cloud no es necesario para proyecto **demo**; no configurar keys reales.

```powershell
# Terminal 1, desde el checkout
node testing-blocks/final/r8-transport-double.cjs
# Terminal 2, desde $firebaseLab, JDK21
npx --yes firebase-tools@15.32.1 emulators:start --only auth,firestore,functions --project demo-inkr8-local --config firebase.json
# Terminal 3, fixtures sintéticos de aceptación exclusivamente loopback
$env:GCLOUD_PROJECT='demo-inkr8-local'
$env:FIRESTORE_EMULATOR_HOST='127.0.0.1:8080'
$env:FIREBASE_AUTH_EMULATOR_HOST='127.0.0.1:9099'
$functionsRoot=Join-Path $repoRoot 'functions'
node testing-blocks/final/fixture-server.cjs $functionsRoot
```

Activación: `SEASON_ACTIVATED_AT_MS` es nueva configuración de la feature; el
script fija instante de preparación demo, no una fecha histórica del producto.
Si se importa un export, renovar cutoff **después** del import antes de nuevos
envíos, porque emulador cambia createTime; conservar assignments/ledger exportados.
El cierre scheduler se prueba por handlers/SDK/reloj controlado, no cron cloud.

AVD de prueba en ejecución, SDK adb en PATH. Elegir el serial real de ese AVD:

```powershell
$serial='emulator-5560'
adb -s $serial reverse tcp:9099 tcp:9099
adb -s $serial reverse tcp:8080 tcp:8080
adb -s $serial reverse tcp:5001 tcp:5001
adb -s $serial reverse tcp:5011 tcp:5011
adb -s $serial install -r delivery/inkr8-lab-debug.apk
adb -s $serial install -r delivery/inkr8-lab-androidTest.apk
adb -s $serial shell am start -n com.inkr8.lab/com.inkr8.lab.LabHostActivity
# Para automatizar los ocho métodos de aceptación final
adb -s $serial shell am instrument -w -r -e class com.inkr8.lab.LabSecureAcceptanceTest com.inkr8.lab.test/androidx.test.runner.AndroidJUnitRunner
```

**Leer `OK (8 tests)` y los estados del runner**, no sólo exit0 de adb. No limpiar
datos, desinstalar o hacer wipe para repetir. Fixtures crean usuarios demo nuevos.
El demo manual necesita catálogo/user de prueba; las sondas lo preparan de forma
explícita. Esta entrada manual no sustituye cobertura de onboarding/OAuth real.

En el AVD utilizado se agregó una guardia de red para UID de com.inkr8.lab con
iptables/ip6tables: sólo127.0.0.0/8 y ::1. Mantenerla para impedir SDK auxiliares
externos; usar `adb root` sólo en AVD autorizado, no en dispositivo personal.
Obtener UID con `adb shell cmd package list packages -U com.inkr8.lab`; comprobar
regla existente antes de agregar: OUTPUT -m owner --uid-owner UID ! -d127.0.0.0/8
-j REJECT (IPv6 ::1/128). Esta guardia de test no se despliega al producto.

## Sondas SDK y revisión

Variables demo anteriores, sin OPENAI_API_KEY ni GOOGLE_APPLICATION_CREDENTIALS.
Argumentos de functionsRoot y archivos de salida **absolutos**, nombres nuevos:

```powershell
$runOut='C:/inkr8-lab/resultados'
New-Item -ItemType Directory -Path $runOut
$env:NODE_PATH=Join-Path $functionsRoot 'node_modules'
node testing-blocks/final/security-check.cjs $functionsRoot "$runOut/security.json"
node testing-blocks/final/name-claim-check.cjs $functionsRoot "$runOut/name-claim.json"
node testing-blocks/final/operation-check.cjs $functionsRoot "$runOut/operations.json"
node testing-blocks/final/purchase-log-check.cjs $functionsRoot "$runOut/purchase-log.json"
node testing-blocks/final/deletion-settlement-check.cjs $functionsRoot "$runOut/deletion.json"
node testing-blocks/final/account-closure-check.cjs $functionsRoot "$runOut/account-closure.json"
python testing-blocks/local-refactor-audit/verify_local_refactor.py --repo $repoRoot --report-dir "$runOut/static-audit"
```

Node probes usan Firestore SDK/Admin genuinos en bases demo nombradas; CloudEvent,
auth request `.run`, clocks/fixtures/R8 se declaran en cada JSON. No equivalen a
transporte cloud. Unitarias/fórmulas/callbacks del código están en app/src/test y
testing-blocks; las suites históricas con evaluator-no-op necesitan su fase de
laboratorio específica documentada en CORE/SEA, no reglas permisivas en este demo.

Informe23 páginas P4 con QA de raster completo; editable DOCX y PDF finales.
UML editable PlantUML1.2026.8/JDK17, 6 PNG/SVG verificados; JAR no se entrega.
Referencias históricas privadas se conservan como localizadores, no se publican.
Las obligaciones académicas individuales y pares/sprints auténticos se listan
en informe y docs/FINAL_DELIVERY.md. No atribuir pruebas del agente al estudiante.

Borrado:16 casos SDK/REST/HTTP PASS; consumidor Android callable confirmado.
El APK final corresponde al guardado protegido, reglas nuevas y cierre Auth:8 PASS.
Settings usa ahora cierre de acceso Auth aprobado por Marco; conserva datos.
19 comprobaciones y consumidor Android pasan; IAM/Auth cloud no acreditados.

## Publicación y paquete

[PR2 en borrador](https://github.com/Rnz5/Inkr8-ISW2/pull/2) y
[rama publicada](https://github.com/Rnz5/Inkr8-ISW2/tree/codex/refactorizacion-por-bloques).
Para el auditor que usa Git, clonar la rama completa (incluye baseline):

```powershell
git clone --branch codex/refactorizacion-por-bloques https://github.com/Rnz5/Inkr8-ISW2.git Inkr8-review
cd Inkr8-review
python testing-blocks/local-refactor-audit/verify_local_refactor.py --repo . --report-dir C:/inkr8-audit-nuevo
```

El ZIP exportado contiene fuentes actuales/entregables, sin `.git`; para ese auditor
usar el clon anterior. Su `PACKAGE-EXPORT.json` identifica commit y hash de cada
archivo. No incluye credenciales originales, ZIP/documents privados, exports de
emuladores, node_modules/lib/build ni JAR de PlantUML. APK abre host local sin
instrumentación; las ocho sondas requieren AndroidTest y emuladores demo.

## ACCOUNT-ACCESS-001 — decisión posterior aplicada

Marco respondió «aplica lo recomendable» a la consulta agrupada de FIN-002.
Se adopta el cierre de acceso Auth con conservación de perfil, nombre, historial
y registros económicos. Código/pruebas/revisión: Codex; no aprobación grupal o
fuentes externas inferidas. La denegación temporal anterior es antecedente.

`UserRepository.deleteAccount` conserva firma/callbacks y llama `closeAccount`.
Settings informa cierre de acceso y retención de datos. La función marca primero
`accountClosed/accountClosedAt` (campos nuevos sólo Functions), sin tocar saldos,
rating, sesión, reservas, username o historial. Después deshabilita Auth y revoca
refresh tokens. Rules/callables bloquean tokens previos; fallo parcial mantiene
acceso cerrado y permite completar el cierre mediante reintento autenticado.
Sin reactivación automática ni nueva política de borrado. Procesamiento Admin de
envíos pendientes y fórmulas económicas anteriores permanecen.

**8 métodos Android PASS**, incluido Settings→VM→repositorio→callable→Auth/signout;
**19 comprobaciones de cierre PASS** REST/Auth/SDK/HTTP: permiso, preservación,
reapertura denegada,8 concurrentes, tokens antiguos y refresh, acceso ajeno,
fallo parcial explícito de entrega Auth y reintento SDK real. Sonda previa sin
callable disponible (preparación, no aceptación funcional);
16/1 posterior detectó conversión indebida de403 a400 en manejador económico:
se conserva HttpsError, sin cambios de costes/fórmulas. Aserciones intactas.
Regresiones afectadas:39 permisos,6 nombre,10 retirada/economía,14 operación,
16 borrado y2 higiene de log PASS. Auditor289 contrastes estáticos PASS; mutación
de nueva fuente detectada y oráculos anteriores intactos. No se suman repeticiones.

No se aportaron reglas originales, credenciales/ID de prueba R8/Google ni pruebas
individuales/actas/P5. Esos límites siguen; Auth/IAM cloud y proveedor externo no
se validan con emuladores. La nueva política local no implica despliegue o datos
reales ni autoría humana del código.
