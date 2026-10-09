# Entrega Inkr8 FIN-002 para revisión

Fuentes auténticas recuperadas + núcleo A01–A07 corregido y refactorizado,
retiradas de torneos/ligas/reputación, Ranked/rating/Merit conservados y temporadas.
Código, pruebas, revisión e informe derivados: Codex bajo DEC-AI-AUTH-001 comunicado
por Marco. El PR es borrador para revisión independiente y validaciones indispensables.
No hacer merge, deploy o conexión a datos reales por estos comandos.

## Contenido y resultados

- `Informe-Inkr8-P4.docx` y `.pdf`: 13 capítulos oficiales, 23 páginas revisadas;
  visión/ODS del informe humano citados, ejemplos/sprints/contribuciones no inventados.
- `uml/`: 6 fuentes PlantUML, SVG/PNG; `source-map.json` mensajes→métodos→líneas/SHA.
- `inkr8-lab-debug.apk`: paquete **com.inkr8.lab**, **demo-inkr8-local** solamente.
  Abre host local sin instrumentación; entrada Auth→AppRoot comprobada. Se omiten
  MainActivity/Google OAuth/Ads, R8 HTTP doble. Requiere servicios locales+ADB reverse.
- `inkr8-lab-androidTest.apk`: necesario sólo para sondas automáticas. 7 métodos
  finales PASS, incluidas las dos sondas pendientes; no fue un fallo de producción.
- `evidence/` y `evidence-index.json`: copias fieles de resultados rojos/verdes
  pertinentes y sus hashes, sin secretos/exports originales privados.
- FIN-00139 seguridad PASS, FIN-0026 nombre-atómico PASS, 14 operación PASS,
  token-rojo1/1→2verde; 287 contrastes **estáticos**, separados de pruebas funcionales.
- Historial de suites CORE/SEA reutilizado; no se suman ejecuciones repetidas.

Reglas `security/firestore.proposed.rules`: propuesta **NUEVA**, no original ni
desplegada. Acceso owner, cross-user denegado y temporadas/dinero Admin-only
comprobados localmente. No valida por sí sola antiabuso de metadata/reservas de
sesión. Cuenta-client-delete denegada requiere un cierre confiable si se restituye.
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
# Para automatizar los siete métodos de aceptación final
adb -s $serial shell am instrument -w -r -e class com.inkr8.lab.LabSecureAcceptanceTest com.inkr8.lab.test/androidx.test.runner.AndroidJUnitRunner
```

**Leer `OK (7 tests)` y los estados del runner**, no sólo exit0 de adb. No limpiar
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
El APK final corresponde al guardado protegido, reglas nuevas y resultado7 PASS.
La UI Settings de cierre de cuenta recibe denegación; su ciclo seguro/política no
están resueltos ni se presentan como retirada humana aprobada.
