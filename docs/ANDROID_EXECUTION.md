# AND-002 — ejecución Android aislada y contraste del núcleo

**Vigente CORE-FINAL-001 — 06/10/2026:** cierre técnico del núcleo A01–A07 en los
contratos explícitos y laboratorio autorizado. Revisión exacta del texto,
bloqueo de persistencia, borradores por UID/ejercicio/palabras y reintento sólo de
consulta del ID confirmado implementados por Codex. 46 métodos Android únicos y
19 unit tests PASS; 11 Ranked +2 ghost +1 lifecycle SDK PASS; fórmulas intactas.
Reentrada/metadata y espera anterior corregidas; fallos de soporte conservados.
R8/eventos/barreras dobles declarados; no producto completo ni aceptación externa.
[Tabla final, criterios, diffs, APK y comandos](CORE_FINAL_AUDIT.md).
Los pendientes anteriores de estos contratos son antecedentes superados; los
frentes de temporadas, retiradas completas, validación externa y academia siguen.

**CORE-CLOSE-001 / DEC-MODE-001 — revisión 06/10/2026:** compatibilidad
gamemode→gamemodeName en siete lectores Android, motor/Merit y estadísticas,
escrituras/historia intactas. Correcciones funcionales ghost atómico, Results del
mismo ID GHOST y promesa de matching esperada; fórmulas originales conservadas.
Android/Functions/lab compilan. 13 métodos Android únicos PASS; matriz Functions
15 PASS, ghost2 PASS, lifecycle1 PASS, invariantes Ranked11 PASS (sin sumar
repeticiones). Android→Firestore→evaluador→Results STANDARD/ON_TOPIC comprobado
localmente, R8 HTTP doble. Auditoría separada del agente; no cierre integral.
Nueva revisión durante guardado pierde borrador: roja preservada, consulta única
pendiente; contexto/reintento y ejecución externa siguen abiertos. Ranked/rating/
Merit permanecen; retiradas/temporadas separadas. [Tabla A01–A07, alcance,
resultados y reproducción](CORE_CLOSURE_AUDIT.md), [diff consolidado](../../../docs/evidence/CORE-CLOSE-001/CORE-current.patch).

Los registros anteriores que indican modo sin resolver o aporte manual requerido
son antecedentes de sus versiones, superados por DEC-MODE-001/DEC-AI-AUTH-001.

**Núcleo A05–A07 / DEC-RES-001 — resultado 06/10/2026:**
A05 retiene texto/borrador hasta persistencia confirmada (5 sondas PASS);
A06 sigue el UUID confirmado con listener/sondeo y generación (8 PASS previos).
A07 separa matching existente del evaluador y corrige control atómico: Functions
PASS, **11 casos transaccionales PASS**, fórmulas rating/Merit intactas. Recorrido
automático local Practice y Ranked con rival previo PASS; detectado Results
congelado tras match posterior. Marco eligió actualizar el mismo envío
(DEC-RES-001): corrección Codex, Android original PASS (41 s), lab PASS (48 s),
sonda roja original ahora PASS y cuatro guardias PASS (**5 métodos**). Dobles
de R8 y entrega/estado de guardias declarados; no proveedor cloud/OAuth integral.
Auditor final 187 estáticos PASS y controles negativos, no aceptación funcional.
Gamemode/gamemodeName, contexto de borradores/H1, retiradas y temporadas siguen
independientes. No cierre integral ni autoría estudiantil del código generado.
[Diff consolidado](evidence/A06-RES-001/CORE-current.patch),
[resultados](evidence/A06-RES-001/EXECUTED_RESULTS.json).

**CORE-001 / FIX-001 — 06/10/2026, prioridad DEC-ACC-001:** Android auténtico
compila; corregido el mapeo SDK de isPlaced/isPhilosopher mediante anotaciones de
campos existentes. Prueba roja original y consumidor VM/placement PASS. Ocho
métodos Android pertinentes únicos: 5 PASS / 3 FAIL conservados (modo, borrador,
identidad); captura Android→motor reproduce ON_TOPIC→STANDARD. Ocho invariantes
Firestore reales: 1 PASS / 7 FAIL, incluyendo reserva Merit duplicada. R8/CloudEvent
dobles explícitos. Sin cambiar Writing/VM/mappers/motor ni fórmulas/transacciones;
conexiones y algoritmo atómico requieren aporte estudiantil según P1 §2.7.
Consulta agrupada enviada, aún sin decisión nueva registrada. Auditor 175
contrastes estáticos PASS, controles negativos rechazan alteraciones. No cierre
integral. [Cambio, resultados, recorridos y aporte exacto](CORE_IMPLEMENTATION.md).

**Antecedentes conservados a continuación:** los resultados anteriores describen
su versión y no sustituyen las comprobaciones posteriores.

Registro 2026-10-06T16:58:04.336410-05:00 · America/Lima · autor del código, arneses, revisión y pruebas: **Codex**.
HEAD `7d879c22030527e1d5f5003ad84b76b750d3b29e`, rama codex/refactorizacion-por-bloques; índice vacío. Sin publicación,
despliegue, reset, actualización de fuentes ni operaciones sobre datos reales.
Originales, baseline, IMPL-001–009, RET-001–005, REC-002/SCP-001, AND-001 y auditorías previas conservados.

## Resultado por capa

| Capa | Resultado realmente observado | Límite |
|---|---|---|
| Windows/AVD | HypervisorPlatform Disabled→Enabled con proceso elevado, autorizado por «hazlo tu yo te concedo todos los permisos». Marco reinició manualmente; WHPX exit 0 y HypervisorPresent=true. AVD API36 completó boot, ADB online. | No teléfono USB. Primeras tareas interrumpidas por reinicio conservadas; no se repitió el intento software. |
| Android | Copia aislada del checkout, wrapper/AGP/catálogo originales, JDK17/API36. APK app y AndroidTest compilados e instalados en el AVD. | appId com.inkr8.lab; no MainActivity/Ads/Google OAuth ni configuración de nube originales ejecutados. No nuevo build del original innecesario. |
| Compose/Android SDK | **14 métodos únicos: 13 PASS, 1 FAIL real** en ejecuciones selectivas. Tres casos antes de IMPL-007/008 PASS y mismos comportamientos que después. | No se suman repeticiones. FAIL = mapeo de isPlaced. No aceptación integral, perfil/placement automático ni cobertura de todos los modos/dispositivos. |
| Auth/Firestore | SDK Android real, usuario anónimo local, persistencia y rechazo de reglas, listeners/sondeo/ViewModel y SharedPreferences reales. | Google OAuth, seguridad original, App Check/Installations y cambio de cuenta/ejercicio no validados. Reglas permisivas/de rechazo son fixtures. |
| Functions | Node20/SDK auténticos, callable applyMeritAction desde Android; guardar cuesta 2000 y actualiza isSaved/contador/Merit. Otro caso: **entrega automática Firestore→Functions**, motor y SDK R8 auténticos, transacción/recompensa y callable HTTP autenticado PASS. | Sólo exports seleccionados del laboratorio. R8 usa HTTP doble 127.0.0.1:5010; `source: real` de su respuesta no acredita proveedor real. No se llamó `.run` en el caso de entrega automática. |
| Ranked/transacciones | **5 escenarios únicos antes/después equivalentes** con Firestore real: replay Practice secuencial/concurrente, dos matchers/un candidato, guardia EVALUATED y replay del snapshot creado Ranked. | R8 doble y eventos `.run` sintéticos; carrera con barrera de planificación explícita, transacciones reales. Conserva defectos, no acepta idempotencia/atomicidad de emparejamiento. |
| Auditor | 171 contrastes estáticos PASS; 158 anteriores intactos. Control negativo cambia isPlaced assertTrue→assertFalse en copia y es rechazado por hash. | No ejecuta JUnit ni convierte el probe rojo en PASS. No son pruebas funcionales. |

APK original conservado: `C:\Users\marco\OneDrive\Documents\ChatGPT\Refactorizar inkr8\checkout\Inkr8-ISW2\app\build\outputs\apk\debug\app-debug.apk` (SHA `a938ea713609bfc834373233e94c69f60c050b20be41c5cf6a8587592e8023b6`).
APK ejecutado: `C:\Users\marco\Documents\Codex\2026-10-04\inkr8-refactorizacion-por-bloques\work\AND-002-private\android\app\build\outputs\apk\debug\app-debug.apk` (SHA `3b41f7cc84dd6951ca62972afbc0fcdbad0622c5379cb435e45dc980b8bda017`).
El APK original conserva su Firebase de nube y no fue instalado/lanzado. Los tokens
de Auth no se imprimieron ni guardaron en evidencia pública.

## Flujos y hallazgos

- Writing real: edición/recomposición, conteo con saltos de línea, 49/50/150/151,
  límites inclusivos, habilitación; reconocimiento con mayúsculas/puntuación y
  filtro de palabras omitidas conservado. Clave/modos nullable siguen en evidencia
  anterior; no se crean modos ficticios de producción.
- Borrador: restauración/autoguardado con SharedPreferences, guardado exitoso y
  rechazo real Firestore. El texto y borrador se borran antes de confirmación;
  **brecha comprobada**, no cumplimiento de conservarlos hasta guardar.
- Espera real: B más reciente resuelve A; FAILED vuelve Home; errores de consulta
  permanecen en Loading sin resolución; timeout a 93 segundos/31 sondeos y
  resultados tardíos ignorados. LoadingScreen renderizó JUDGMENT DELAYED;
  [imagen real](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/AND-002/wait-timeout.png>). No nueva política de identidad,
  reintento, duración o error elegida.
- Results: score 82.47%, feedback, Merit Gain, rating y callback Back PASS sobre
  composable real con datos fixture. Placement: Calibration Complete y Continue
  PASS; transición automática desde perfil **no aceptada**.
- Probe de perfil: campo almacenado isPlaced=true, `Users.isPlaced` no recibe true.
  **FAIL se conserva con assertTrue**. Modelo/consumidor intactos desde antes de
  la etapa; no se atribuye a las extracciones. isPhilosopher no llega a comprobarse
  porque la primera aserción falla. Se requiere corregir/verificar el contrato
  de mapeo con aporte estudiantil; no cambiar esquema histórico por suposición.
- Otro contrato observado: Android escribe gamemodeName; motor auténtico consulta
  gamemode y eventos Android registraron gamemode:null. Fuente y consumidores
  intactos. Falta verificar/corregir esa conexión con el estudiante; no se normalizó
  silenciosamente el payload. El caso Node de entrega usó forma del motor, no
  acredita que el envío Android conserve íntegramente el contexto evaluado.
- Ranked: snapshot creado repetido recompensa/incrementa dos veces; dos matchers
  reclaman el mismo candidato y sólo queda un resultado recíproco; replay Ranked
  añade premio y vuelve PENDING con matchResult previo. EVALUATED leído de nuevo
  sí se salta: distinguirlo del replay del evento creado PENDING. Todos aparecen
  igual antes/después. No se alteraron fórmulas, efectos ni transacciones.

## Correcciones del laboratorio y revisión separada

Se corrigieron exclusivamente fixtures/soporte: OAuth ID ficticio necesario para
compilar AuthManager; formato de API key ficticia; comparación EditableText en vez
de placeholder; almacenamiento explícito isActive; reverse perdido tras reiniciar
adbd; descubrimiento Functions local 10→60 s. La CLI 15.32.1 enlaza admin.firestore
con bind y pierde FieldValue; el loader usa la **clase real** de firebase-admin/firestore
cuando falta. No hay doble transaccional ni parche al producto/CLI/SDK.

Un primer replay usó snapshot EVALUATED y fue correctamente ignorado; se conservó
el fallo y se corrigió la entrega fixture para reutilizar PENDING del evento creado,
añadiendo además comprobación explícita de la guardia. Durante una comparación
timeout se activó prematuramente el motor y éste rechazó el texto corto como FAILED;
esa ejecución quedó 2/3 PASS y no era comparable. Se desactivó el export para el
sondeo controlado: 1/1 PASS, sin cambiar deadline ni aserciones. Todas las trazas
iniciales se preservan. ADB exit 0 se interpretó con el resumen JUnit, no como éxito.

Revisión separada: consumidores/firmas/imports compilados; admisión conserva
String/Gamemode/lambda diferida dentro de remember/derivedStateOf, evaluación de
límites, texto vacío, tokenización y orden. Política escalar conserva estados y
lectura de cada snapshot; consulta por último sigue sin identidad específica.
Fórmula rating mantiene dos llamadas dentro de la misma transacción, mientras
la selección de candidato queda fuera como antes. No hallazgo nuevo atribuible
a IMPL-007–009 en el alcance ejecutado. No se añadieron traslados ni patrones
sin necesidad; SRP de decisiones/cálculo aislados no resuelve coordinación crítica
ni acredita SOLID integral. Revisión Codex, no auditoría independiente humana.

## Estado A05–A07 y dependencias

| Bloque | Implementado/validado ahora | Pendiente concreto |
|---|---|---|
| A05 | IMPL-007 y presentación/Regex existentes compilados; admisión, palabras, borrador y callback comprobados en dispositivo | Conectar confirmación de addSubmission a Writing sin borrado anticipado (decisión humana ya registrada). Contexto de borrador cuenta/ejercicio aún no elegido. |
| A06 | IMPL-008; listener/sondeo, error, FAILED, 93 s y llegada tardía ejecutados; antes/después conservan defectos | Identidad A frente a B, reintento/errores/resultados tardíos según reglas abiertas. Corregir mapeo isPlaced y payload gamemode con contrato/aporte estudiantil. |
| A07 | IMPL-009 y fórmula conservados con transacciones reales, concurrencia/replay y entrega automática local | Solución auténtica estudiantil de idempotencia y selección/revalidación del candidato; compatibilidad de efectos conservados con retiradas. No inventar nueva matemática. |
| Retiradas/temporadas | Ranked, rating y Merit íntegros; retiradas anteriores de presentación conservadas | Torneos/ligas/reputación de backend/selección siguen separados; temporadas y Q-R/Q-T/Q-D/Q-C no resueltos donde falta decisión. |

P1 §2.7 dice: «Los algoritmos que resuelven el problema principal del proyecto
deben ser escritos por el estudiante» y «La conexión entre el Backend y el
Frontend debe ser manual». Se aplica a la solución de emparejamiento/idempotencia
y las conexiones funcionales de confirmación/mapeo, **no** a cualquier condición,
extracción mecánica o test. [Fuente y reglas](PROFESSOR_REQUIREMENTS.md).
No se exige copiar código. Codex escribió/ejecutó todas las pruebas y el soporte;
Marco autorizó instalaciones/laboratorio y reinició Windows. No acreditamos
revisión, implementación ni ejecución académica estudiantil por esos permisos.

## Reproducir y retomar

[Arnés y comandos](../testing-blocks/android-emulator-lab/README.md), preparación
Android `prepare.py` y Firebase `prepare_firebase.py` en carpetas nuevas fuera del
checkout. Mantener el evaluador desactivado para fixtures de espera; habilitarlo
con doble HTTP explícito para entrega automática. Leer resumen JUnit: el probe
isPlaced sigue rojo. No repetir suites históricas como cobertura nueva.

Datos Auth/Firestore exportados a carpetas nuevas privadas antes de detener
procesos propios; no wipe, uninstall ni eliminación. APK actual de laboratorio
restituido después del contraste. WHPX queda habilitado como autorizado.
Reglas originales de seguridad, Google OAuth y proveedor R8 real siguen sin
aceptación integral; el arnés no los sustituye. No falta SDK/dispositivo para
las siguientes verificaciones locales. El siguiente aporte es del estudiante
sobre contratos/conexiones y algoritmos citados, con evidencia humana auténtica.


---

# AND-001 — Android compilado y verificación ampliada

Registro 2026-10-05T20:39:49.753471-05:00 · America/Lima · autor de herramientas y pruebas: Codex.
HEAD `7d879c22030527e1d5f5003ad84b76b750d3b29e`, rama `codex/refactorizacion-por-bloques`; índice vacío, sin commit/push/merge/despliegue.
El ZIP auténtico, baseline, IMPL-001–009, RET-001–005, REC-002/SCP-001 y auditorías anteriores se conservan como antecedentes.

Marco aceptó el contrato SDK mediante «claro». Después autorizó elegir entorno de prueba
con «mmm no tengo idea la verdad pero puedes usar el que quieras para lograr tu objetivo».
Codex eligió **demo-inkr8-local**, Auth/Firestore exclusivamente en 127.0.0.1.
Son permisos de instalación/verificación; no se atribuye código, arquitectura, nuevas
reglas ni intervención académica manual al estudiante.

| Capa | Comprobación real de esta etapa | Resultado y límite |
|---|---|---|
| SDK/build Android | API 36, Build Tools 36.0.0 y Platform Tools 37.0.1; JDK 17.0.20.1, wrapper 9.1.0, AGP 9.0.0 y catálogo/plugin Compose originales. `:app:assembleDebug :app:testDebugUnitTest` | Dos builds exit 0. Primero 42 tareas/283,69 s y ejemplo original; segundo 3 tareas ejecutadas/39 up-to-date, 51,85 s tras añadir tests. No cambio de versiones. |
| Kotlin/Compose/consumidores | Compilación de todas las fuentes app, incluidas extracciones de Writing/Results, canSubmit/espera, restauraciones de Merit y consumidores | Tipos/importaciones/plugin Compose comprobados. No pantallas renderizadas ni recomposición de pantalla/integración acreditadas. |
| Pruebas Android locales | 7 escenarios existentes de clave de borrador y 5 de política de espera ejecutan clases reales. 4 métodos nuevos comparan admisión con predicado de HEAD sobre dos modos reales; 2 usan snapshots/derivedStateOf Compose real | **16 pertinentes PASS + 1 ejemplo**, 0 fail/error/skipped. Reutilizados no cuentan como cobertura nueva. 42 comparaciones del predicado/conteo y secuencia de estado; nullable de modos ficticios sigue en evidencia aislada anterior. No SharedPreferences IO, ciclo ViewModel ni pantalla real. |
| Functions/motor y Firestore | JS emitido por builds auténticos antes/después de REC-002 reutilizado; cuatro escenarios nuevos WIN/LOSS/DRAW/floor con Admin SDK y transacciones Firestore local | 4 PASS antes y 4 PASS después, **4 escenarios únicos**, resultados equivalentes de ambas escrituras, deltas, suelo, rachas, Merit y sesión. CloudEvent invocado con `.run`, no entrega automática Functions. R8 devuelve score desde doble explícito; no proveedor real. Sólo secuencial, sin aceptación de concurrencia/idempotencia. |
| Auth | REST del emulador 127.0.0.1:9099 crea usuario anónimo de prueba y devuelve token con aud demo-inkr8-local | PASS; tokens no impresos/guardados en evidencia pública. No app, sesión Google OAuth ni perfil real. |
| Emulador Android | Oficial 37.2.12/API36 AOSP x86_64 rev2, AVD local; `-accel-check` exit 6; intento software `-accel off`, sin ventana | No controlador de hipervisor. ADB sigue offline; no boot completado/APK instalado/pantalla ejecutada. No modificación de funciones Windows/controladores/reinicio. |
| Auditor | 155 expectativas previas intactas + hashes de 3 fuentes de verificación | **158 estáticos PASS**. Control negativo en copia cambiando expectativa de admisión rechazado. No son pruebas funcionales. |
| R8 | Fuente auténtica sigue íntegra y compila por evidencia REC-002; no nueva ejecución real del proveedor | Los seis casos de transporte simulado y NaN previo siguen como evidencia anterior. No se cambia el evaluador ni se oculta su defecto. |

## APK y configuración

APK: `C:\Users\marco\OneDrive\Documents\ChatGPT\Refactorizar inkr8\checkout\Inkr8-ISW2\app\build\outputs\apk\debug\app-debug.apk`.
Tamaño y SHA-256 en `docs/evidence/AND-001/APK_FINAL.json`; firma verificada por
apksigner exit 0, manifiesto/paquete por aapt exit 0. El APK no cambió al añadir tests.
**Este APK conserva la configuración Firebase auténtica recibida; no se instaló ni
se lanzó contra ella.** La autorización del laboratorio no identifica ese proyecto
como de prueba. No hay un override debug que cambie silenciosamente su destino.

`local.properties` configura únicamente el SDK instalado, es local e ignorado.
Gradle/Android/Functions/Firebase/R8 originales no se sobrescribieron. La CLI Firebase
15.32.1 y Java 21.0.12.1 se instalaron en el laboratorio para emuladores: la CLI rechazó
Java 17 con error explícito; Android conserva el Java 17 que compiló correctamente.
El runtime Java 21 vino del repositorio oficial Adoptium, ZIP/SHA-256 verificados.

El laboratorio está en `C:\Users\marco\Documents\Codex\2026-10-04\inkr8-refactorizacion-por-bloques\work\AND-001-private\firebase-lab`. Su firebase.json y reglas
permisivas son **fixtures nuevas**, no originales recuperados ni política de seguridad
del producto. No hubo login CLI, proyecto de nube, claves reales ni evaluación externa.
Todos los datos creados son sintéticos y permanecen en export-preserved (privado);
se detuvieron únicamente los procesos de emulación iniciados para esta etapa.

## Revisión separada y hallazgos

- A05: canSubmit mantiene remember/derivedStateOf y lambda diferida, blanco, conteo y
  límites. El predicado del test coincide con HEAD tras renombrar parámetros/indentación.
  Runtime Compose verifica lecturas/estado en los dos modos reales; las trazas nullable
  anteriores se conservan. Escritura, tokenización, callbacks y borrador no se alteraron.
- A06: cuatro llamadas existentes del ViewModel se resolvieron/compilaron; cinco casos
  de decisiones escalares pasan con enum y política reales. Guarda, delay, contador `*3`,
  timeout `>90` y selección actual del último envío quedan intactos. No se prueba asociación
  A/B, error/late result/cancelación mediante estas pruebas de política.
- A07: fuente before coincide con Git HEAD; se usan outputs de compilación auténtica
  antes/después. Los cuatro matches ejecutaron ambas llamadas a la fórmula dentro de las
  transacciones conservadas; comprobadas ambas submissions/users y efectos observables.
  No se movieron lecturas/escrituras, fronteras transaccionales, selección o fórmulas.
- No se identificó regresión de nuestros cambios en el alcance ejecutado. No fue necesario
  corregir producción ni introducir otra extracción/patrón. La separación previa favorece
  responsabilidad comprobable; no se declara SOLID integral ni cierre de A05–A07.
- Errores de herramienta/configuración conservados: sdkmanager actual dividió las rutas
  con punto y coma y devolvió exit 0 incompleto; corregido usando Android CLI con `/` y
  comprobación de archivos. AVD legacy requiere `;` y toolsdir, pointer corregido tras
  intentos fallidos. Java 17 incompatible con CLI Firebase actual; Java 21 sólo en lab.
  API Adoptium devolvió HTTP 403; descarga oficial GitHub y checksum verificados.
- Advertencias reales: XML SDK reader 3/metadata 4, D8 stack-map en play-services-auth
  21.5.0, APIs obsoletas/casts en código conservado. Builds exit 0; no cambio de versión
  inferido ni advertencias reclasificadas como regresiones. Logs completos preservados.

## Estado A05–A07 y dependencias

| Bloque | Implementado y ahora comprobado | Continúa pendiente |
|---|---|---|
| A05 | Extracción mecánica, tipos/Compose compilados, admisión real y snapshots probados; reconocimiento/presentación compilan | Pantalla y autosave real. **Decisión vigente: conservar texto/borrador hasta persistencia confirmada**; Writing todavía clearDraft antes del callback (342–345). Corrección funcional/conexión de confirmación separada. Cuenta/ejercicio del borrador sigue abierta. |
| A06 | Política escalar extraída, consumidores compilados, 5 casos reales; integración conservada sin alteraciones | Último envío en vez de ID propio, cuenta/errores/resultado tardío, timeout/reintento y placement reales. Orientación humana previa no define reglas nuevas. |
| A07 | Fórmula extraída y compilada; cuatro matches reales locales antes/después equivalentes | Entrega automática de trigger, concurrencia/idempotencia, R8 real y aceptación Ranked. Retirada de ligas/reputación en backend/selector sigue separada y dependiente; no se altera Merit/rating. |

Siguiente bloqueo de pantallas: **activar un hipervisor/controlador Android admitido en
Windows (administrador y reinicio si corresponde), o conectar un dispositivo Android de
prueba autorizado con depuración USB**. El intento software no proporciona un dispositivo
ejecutable. Después habrá que conectar explícitamente una variante de prueba al laboratorio
antes de arrancar pantallas con repositorios Firebase; actualmente no existe ese adaptador
Android de prueba y el APK original apunta a la configuración recibida.

P1 §2.7 reserva algoritmos principales, BD/relaciones e integración frontend/backend
a estudiantes; también permite configuración, componentes aislados y pruebas de lógica
original. Lo realizado son instalación/verificación/fixtures/tests Codex, sin nueva lógica
crítica, relaciones o conexión de producto. No se pide copiar código mecánico. La reserva
concreta de conexión de confirmación envío/borrador se mantiene para intervención auténtica;
fuente: P1 pp. físicas 6–7, `docs/evidence/IMPL-007-009/P1_pages_6_7.txt`.
No se atribuye revisión individual, aprobación arquitectónica ni cumplimiento académico.

Siguen faltando **reglas originales Firestore/fixtures autorizadas de seguridad**, un
ensayo de R8 real permitido con su configuración de prueba, decisión de contexto borrador,
reglas abiertas de espera/Ranked sin ligas y temporadas. La configuración nueva del
laboratorio no sustituye estas fuentes/decisiones. No hay retirada nueva ni datos históricos
eliminados, publicación o despliegue.

## Reproducción

En PowerShell, desde `C:\Users\marco\OneDrive\Documents\ChatGPT\Refactorizar inkr8\checkout\Inkr8-ISW2`:

```powershell
$env:JAVA_HOME = 'C:\Users\marco\AppData\Local\Temp\inkr8-baseline-jdk17\expanded\jdk-17.0.20.1+1'
$env:PATH = "$env:JAVA_HOME/bin;$env:PATH"
.\gradlew.bat --no-daemon :app:assembleDebug :app:testDebugUnitTest --console=plain
```

El SDK lo resuelve local.properties; en otra máquina usar su ruta real, no copiar la de
Renzo por suposición. Reportes JUnit: app/build/test-results/testDebugUnitTest;
HTML: app/build/reports/tests/testDebugUnitTest. Compilar no exige conectar el APK a Firebase.

Para repetir únicamente el laboratorio: Java 21 y Node 20 de las rutas registradas;
desde firebase-lab, invocar el firebase.js instalado con
`emulators:start --only auth,firestore --project demo-inkr8-local --config firebase.json
--import export-preserved`. Elegir **prefijos nuevos** en el test para no sobrescribir
datos sintéticos anteriores. Exportar a carpeta nueva antes de detenerlo.
`firestore-transaction-check.cjs` recibe root Functions compilado, prefijo alfabético y
ruta de resultado JSON; requiere FIRESTORE_EMULATOR_HOST=127.0.0.1:8080,
GCLOUD_PROJECT=demo-inkr8-local, sin credenciales ni OPENAI_API_KEY. No ejecutarlo
contra producción. La comparación exacta y comandos antes/después están en AND-001.

Revisar `testing-blocks/local-refactor-audit/verify_local_refactor.py --repo <checkout>
--report-dir <directorio nuevo>` con Python; no equivale a ejecutar los escenarios.
Diff de esta etapa y archivos públicos en outputs/Inkr8_AND-001_* del workspace Codex.
Las fuentes de pruebas son reproducibles; las expectativas anteriores del auditor están
en AND-001/before-audit y su control negativo sólo modifica una copia privada.


Fuentes técnicas para el laboratorio: [proyectos demo y Auth](https://firebase.google.com/docs/emulator-suite/connect_auth), [Firestore local](https://firebase.google.com/docs/emulator-suite/connect_firestore), [instalación Emulator Suite](https://firebase.google.com/docs/emulator-suite/install_and_configure) y [aceleración Windows/WHPX](https://developer.android.com/studio/run/emulator-acceleration). La guía Android pide activar Windows Hypervisor Platform y reiniciar; no se ejecutó ese cambio de sistema automáticamente. Las fuentes no sustituyen los resultados locales ni configuran reglas del producto.


Entrega: el diff textual normaliza finales LF; el ZIP conserva los bytes actuales. Se comprueba aplicación y contenido en copia privada normalizada, sin editar el checkout. El primer intento detectó contexto CRLF incompatible y se conserva en AND-001-private/artifact-attempt-1; la corrección corresponde al empaquetado Codex, no a producción.
