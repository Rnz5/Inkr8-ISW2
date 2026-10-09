# CORE-001 — contratos comprobados y asistencia de integración

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

## CORE-CLOSE-001 — compatibilidad, consumidores y control del cierre

Problema → decisión → cambio → comprobación → límite registrados en
[CORE_CLOSURE_AUDIT](CORE_CLOSURE_AUDIT.md). DEC-MODE-001 es elección explícita de
Marco en este chat; código/revisión/pruebas Codex. Compatibilidad es corrección
funcional, no cambio de escritor/esquema. Ghost lee estado/rating dentro del commit,
Results refresca GHOST del mismo ID y el trigger espera matching con catch intacto.
Fórmulas/tiempos/errores/claves conservados; diferimiento de pruning declarado.
Rojas anteriores y aserciones intactas, nuevas rojas antes/después y restricciones
de datos inválidos documentadas. Trece métodos Android únicos PASS, 15 casos de
modo SDK PASS, ghost2/lifecycle1 y once invariantes Ranked PASS. Build Android/lab/
Functions auténticos PASS; eventos Android automáticos locales y R8 HTTP doble.
Dos sondas Writing separadas: pérdida de nueva revisión FAIL, dos UUID PASS como
caracterización, no aceptación. Consulta única de edición/segundo envío pendiente;
sin manualidad estudiantil exigida. Auditoría estática y negativos van separados
de aceptación real. No aceptación integral/SOLID integral ni proveedor externo.

## A07-CORE-001 y A06-RES-001 — control atómico y resultado vivo

06/10/2026. Código, revisión y pruebas: Codex bajo DEC-AI-AUTH-001. No nueva
edición/revisión humana atribuida. Marco decide sólo el refresco de Results del
mismo envío mediante respuesta explícita DEC-RES-001; precedencia de modo abierta.

**Problema → justificación → cambio.** ALT-13: evaluación/R8 y selección/commit
de pareja cambian por razones independientes. tryMatchRankedSubmission se mueve
verbatim a rankedMatching.ts como primer paso estructural comprobado. No nuevos
patrones ni cinco principios forzados. Correcciones funcionales separadas: el
motor lee el estado almacenado antes del proveedor y dentro de la transacción;
sólo contabiliza PENDING (o ausencia legacy ya admitida). Fallo tardío/repetido no
reemplaza terminal EVALUATED/FAILED. Matching lee ambos envíos dentro del commit,
exige EVALUATED/PENDING, aplica rating/resultados/rachas una vez y conserva
reciprocidad. Ajustes legacy de liga son locales al intento de transacción; se
retira el update externo que podía resucitar una pareja MATCHED como PENDING.
Fórmulas, recompensas, cap/reserva, placement, ±20/48h, esquema y efectos existentes
de sesión/ligas/reputación permanecen; su retirada requiere bloque separado.

**Antes/después y alcance real.** Las ocho invariantes CORE-001 (1 PASS/7 FAIL)
se conservan y ahora dan 8 PASS con transacciones Firestore SDK reales. Tres casos
nuevos: antes 1 PASS/2 FAIL, después 3 PASS (FAILED terminal, error tardío frente
a éxito/sesión posterior, dos envíos distintos del mismo autor). Total 11 casos
pertinentes; CloudEvents, respuesta R8 y barreras de planificación son dobles
explícitos, sin evento/proveedor cloud. Functions build auténtico PASS (22 s).

AppRoot→Writing→repositorio→Firestore→entrega automática Functions→R8 HTTP doble
→Results ejecutado en WHPX: Practice PASS y Ranked con candidato previo PASS,
score/feedback/Merit y rating/reciprocidad observados. El soporte inicial falla
por PlayMode.name inexistente y selector 80.0 ambiguo; logs conservados, corrección
del arnés sin cambiar producto/aserciones de aceptación. Un tercer método revela
un defecto previo: tras mostrar EVALUATED, un match posterior deja VM PENDING/0
aunque servidor MATCHED/+1. Guardia loadingResolved anterior a A06 explica el
defecto; no regresión atribuida a la extracción.

**Decisión humana → corrección → prueba.** Marco: «Actualizar Results del mismo
envío». A06-RES-001 añade una rama: ID/generación vigentes primero; Results visible,
ya resuelto, sin timeout, EVALUATED/RANKED/MATCHED y mismo latestSubmission.id.
Único efecto: actualizar latestSubmission; no navegar ni reiniciar espera. Android
auténtico PASS (41 s), lab PASS (48 s). La misma sonda automática roja sin cambios
pasa, +1 visible; cuatro pruebas de guardias también PASS (otro ID/generación,
salir de Results, timeout y ViewModel cerrado). Esas cuatro usan reflexión/estado
de timeout explícitos; no se presentan como entrega Firebase o timer de 93 s.
Evidencia previa del timer real se reutiliza, sin repetirla ni sumarla como nueva.
JSON observado antes del wait UI; JUnit final comprueba el refresco.

**Revisión y límites.** Inversión exacta de cambios declarados reconstruye motor,
función de matching y VM completos antes de los oráculos anteriores; consumidores,
importaciones y fronteras revisados por Codex en una fase separada. Auditor final
187 estáticos PASS, controles negativos rechazan guardia de candidato, aserción
histórica y guardia del resultado alteradas. No sustituye pruebas, auditor externo
ni cumplimiento individual. R8 puede ser llamado dos veces si dos workers llegan
antes del commit; sólo uno contabiliza. Stats legacy fuera de tx no son durable
exactly-once. No Home/cobro Ranked, OAuth/Ads, proveedor real o placement automático
completo. Original APK cloud no se instala; config auténtica conserva hashes.

Pendientes concretos: precedencia gamemode/gamemodeName si ambos difieren (consulta
ya enviada, escrituras/lecturas sin normalizar); cuenta/ejercicio/edición simultánea
y segundo envío; detalle H1/reintento; retiradas de torneos/ligas/reputación y reglas
de temporadas. Ranked/rating/Merit permanecen. No datos reales ni despliegue/push.

[Diff consolidado](evidence/A06-RES-001/CORE-current.patch),
[revisión A07](evidence/A07-CORE-001/REVIEW.json),
[11 casos](evidence/A07-CORE-001/EXECUTED_RESULTS.json),
[corrección y 5 métodos](evidence/A06-RES-001/EXECUTED_RESULTS.json).

### Reproducción acotada

En checkout, con JAVA_HOME JDK17 y ANDROID_HOME apuntando al SDK existente:
`gradlew.bat --offline --no-daemon :app:assembleDebug --console=plain`.
En functions, PATH del Node20 auténtico: `npm.cmd run build`. No reinstalar ni
sustituir versiones/configuración. APK original:
`app/build/outputs/apk/debug/app-debug.apk` (config cloud, no usado en sondas).

Para repetir una sonda, usar sólo com.inkr8.lab/APKs A06-RES-001-private,
emulator-5560 y demo-inkr8-local con puertos Auth9099/Firestore8080/Functions5001;
R8 HTTP doble5010 y fetch externo bloqueado. Instalación `adb -s emulator-5560
install -r <apk-lab>` y su androidTest; adb reverse para los tres puertos.
Filtro instrumental: `com.inkr8.lab.LabIntegratedFlowTest#laterAutomaticMatchUpdatesAlreadyDisplayedSameResult`
en runner `com.inkr8.lab.test/androidx.test.runner.AndroidJUnitRunner`. El otro
filtro `com.inkr8.lab.LabResultRefreshTest` prueba guardias mediante dobles.
Preparación/reanudación local documentada en RUNTIME_PRESERVATION.json; conservar
el export más reciente, no borrar fixtures ni arrancar con datos cloud.

## A06-CON-001 — ID confirmado y callbacks de la espera vigente

Problema: consultas al último documento permiten que B resuelva A; callbacks
ya encolados sobrevivían al cambio de espera o cierre del ViewModel. Decisión
existente: seguir el envío iniciado y conservar políticas; DEC-AI-AUTH-001 permite
la conexión/corrección del agente, sin nueva edición manual exigida. Autoría Codex.

submitWriting transmite finalSubmission.id después de onPersisted. Repositorio
añade getSubmission/listenToSubmission por document(id), preservando traducción
DTO, auth guard y errores. VM captura ID y generación; ambos caminos comprueban
generación y document.id. onCleared invalida respuestas ya encoladas. El sondeo
antiguo no modifica el contador de una espera nueva. getLastSubmission y el
fallback de Results permanecen. Validación, A05, sesión/torneos, scalar policy,
delay 3000, timeout >90 (=93 nominal), errores sin resolver, FAILED→home y
resultados tardíos ignorados siguen iguales. Corrección funcional, no nuevo retry.

Antes: tres sondas nuevas FAIL. Después: ocho métodos pertinentes PASS: esas
tres, roja original B≠A intacta, FAILED, error, timeout/tardío y regresión A05
por AppRoot. Sondeo se comprueba quitando listener y creando B más reciente;
callback antiguo/cierre usan reflexión explícita como doble de entrega, no se
presentan como eventos Firebase reales. SDK/Firestore/Auth locales reales;
evaluación se escribe como fixture, no R8. Android original y ambos builds lab PASS.
Revisión inversa completa conserva todas las operaciones ajenas y auditor 181
estáticos PASS. Originales, A05, pruebas rojas y evidencias anteriores intactos.

Alcance: asociación y ciclo de callbacks; no política de persistencias múltiples
simultáneas, cuenta/ejercicio, reintentos H1, cambio de duración/errores ni proveedor
real. Quedan independientes gamemode/gamemodeName, retirada de ligas/reputación
y temporadas. A07 sigue con control atómico sin cambiar fórmulas.
[Diff](evidence/A06-CON-001/production-change.patch),
[revisión](evidence/A06-CON-001/REVIEW.json),
[pruebas](evidence/A06-CON-001/EXECUTED_RESULTS.json).

## A05-CON-001 — conexión y retención hasta confirmación

06/10/2026. Corrección funcional del defecto previo, separada de IMPL-007.
Decisión humana existente: conservar texto/borrador hasta persistencia confirmada.
Codex escribe la conexión por instrucción humana explícita; posteriormente
DEC-AI-AUTH-001 elimina el bloqueo de nuevas ediciones manuales para A05–A07.
Procedencia: permiso del profesor comunicado por Marco, sin inferir fecha del
acto docente, documento adicional o revisión humana. Todo código y pruebas es
del agente; no se acredita autoría o evaluación individual por esta ejecución.

Writing:45 cambia el evento a envío + continuación; la limpieza existente pasa
dentro de esa continuación. AppRoot:84 la transmite como onPersisted; preview
adaptado a dos argumentos. ViewModel/repo permanecen intactos desde A05-SUP-001:
éxito Task.set → onPersisted → startLoadingResult. El error conserva Toast y
limpieza de sesión. Pendiente/rechazo no limpian. Revisión inversa exacta devuelve
ambos archivos a su preimagen; fábrica/UUID, validación/mensajes, tokenización,
conteo y claves intactos. API de pruebas adaptada sin cambiar aserciones.

Android original assembleDebug PASS 49 s; builds aislados antes/después PASS.
AppRoot real con VM/repositorio/Firebase SDK: antes pendiente/rechazo FAIL y
éxito PASS; después tres PASS. Éxito navega a loading: se comprueba borrador
vacío y editor restaurado al volver; sonda aislada existente comprueba editor
en vivo vacío tras éxito real. Probe rojo CORE-001 de rechazo ahora PASS intacto.
Total cinco métodos pertinentes únicos PASS. Rechazo por reglas locales y UUID
original; pendiente por red Firestore deshabilitada y metadata.hasPendingWrites.
Auth anónimo de emulador real, host/launcher fixtures; no OAuth/Ads/R8/evaluación.

Auditor conserva oráculos anteriores invirtiendo sólo callback/limpieza declarados;
179 contrastes estáticos PASS no equivalen a pruebas funcionales. Primera
adaptación leyó JSON con codificación Windows y alteró caracteres Unicode:
fallo conservado; se restauraron los campos originales con UTF-8. Controles
negativos rechazan pérdida del callback y debilitamiento del rechazo.
Versiones originales/HEAD/índice y toda evidencia histórica intactos; APK anterior
preservado. Laboratorio continúa abierto para A06, sin datos cloud/despliegue.

Alcance comprobado: Standard/Practice, una cuenta/ejercicio/revisión por caso;
no edición concurrente, segundo envío, claves por cuenta/ejercicio ni aceptación
integral del producto. No cierre total A05–A07/temporadas/retiradas.
[Diff](evidence/A05-CON-001/production-change.patch),
[resultados](evidence/A05-CON-001/EXECUTED_RESULTS.json),
[revisión](evidence/A05-CON-001/REVIEW.json), [APK](evidence/A05-CON-001/APKS.json).

**DEC-AI-AUTH-001 — permiso particular comunicado por Marco, 06/10/2026:** Marco aclara que el código original fue hecho manualmente y que el profesor autorizó al agente a realizar la refactorización, sin nuevas ediciones manuales del estudiante. Esta dirección posterior elimina el bloqueo operativo de autoría manual que se aplicaba a A05–A07 y sus conexiones/correcciones acordadas sobre el código existente. Codex puede implementarlas y comprobarlas autónomamente dentro del alcance vigente; registrar cada cambio como refactorización conservadora o corrección funcional, con autoría real del agente. La aprobación de reglas de negocio aún abiertas y los requisitos de evidencia/entrega no se inventan. Los apartados anteriores que exigían aporte manual para continuar la refactorización quedan como antecedentes superados; las copias originales de P1 no se reescriben.


## A05 — siguiente intervención conjunta: AppRoot y Writing

Revisión 06/10/2026 posterior a A05-SUP-001. La preparación es código de Codex;
se comprobó el SHA actual, la inversión exacta de sus dos cambios y la conservación
de AppRoot/Writing/repositorio. Se reutiliza su compilación PASS; no se ejecutó otra.
La revisión anterior que no encontraba el callback queda como antecedente histórico.

Contrato propuesto para la conexión que escribirá el estudiante: el evento de
Writing entrega el mismo Submissions y una continuación sin argumentos. AppRoot
recibe ambos y transmite esa continuación como onPersisted a submitWriting;
conserva el Toast de onError. Writing define la continuación con la limpieza actual
de DraftManager y userText; la limpieza deja de ejecutarse al pulsar Submit.
La confirmación existente es addOnSuccessListener del Task de set, no el retorno
Unit, la creación del UUID ni EVALUATED. Error y pendiente no invocan esa continuación.
El orden en VM sigue confirmación → inicio de espera. No editar VM/repositorio,
validación, fábrica/UUID, mensajes, conteo, tokenización ni claves del borrador.

Fragmentos vigentes: Writing:45 y 341–345; AppRoot:84–88;
FirestoreSubmissionRepository:32–34; AppViewModel:140–143 y 169–177.
No se suministra una implementación completa para copiar ni se atribuye la guía
como integración estudiantil. P1 §2.7 reserva la conexión Backend/Frontend;
el aporte pendiente es la conexión real escrita y explicada por el estudiante.

Tras guardar ambos archivos: revisar el diff real, adaptar sólo el soporte de
pruebas al contrato efectivamente escrito, compilar el cambio y comprobar pendiente,
éxito y rechazo real de Firestore en demo-inkr8-local. Las aserciones existentes
de conservar editor/borrador en rechazo permanecen. Una prueba que conecte
Writing directamente al repositorio no acredita el paso por AppRoot.
Las caracterizaciones históricas de pérdida se conservan como antecedentes.
No hay aún ejecución de esa conexión ni aceptación funcional de A05.
Alcance una cuenta/ejercicio/revisión; edición simultánea, segundo envío y
claves cuenta/ejercicio siguen abiertos. A06/A07 y compatibilidad de modo independientes.

Evidencia: [checkpoint](evidence/CORE-002/A05_CONNECTION_CHECKPOINT_001.json).

## Actualización A05-SUP-001 — 06/10/2026

La preparación pequeña existe ahora como **código de Codex**: onPersisted opcional antes de onError, valor vacío predeterminado e invocación sólo desde el éxito de addSubmission antes de startLoadingResult. Los cuatro consumidores actuales siguen usando el valor vacío. AppRoot, Writing y FirestoreSubmissionRepository no cambian; el borrado anticipado todavía no está corregido.

Las revisiones CORE-002 de ausencia que siguen son históricas. No pedir esta preparación otra vez ni atribuirla al alumno. La intervención estudiantil pendiente es conectar la confirmación por AppRoot a Writing y limpiar únicamente desde el éxito, con pruebas de pendiente/rechazo/éxito. P1 §2.7, requisitos de autoría y algoritmos críticos siguen vigentes; no cierre funcional ni cumplimiento académico acreditados. La evidencia nueva está en la carpeta de coordinación docs/evidence/A05-SUP-001.

06/10/2026, America/Lima. Prioridad DEC-ACC-001. HEAD
`7d879c22030527e1d5f5003ad84b76b750d3b29e`, rama `codex/refactorizacion-por-bloques`,
índice vacío. Codex implementó soporte, generó/ejecutó pruebas y realizó la revisión
posterior. Marco aportó el encargo y decisiones previas; no se le atribuye código,
ejecución, revisión o elección nueva. Consulta agrupada por compatibilidad de modo,
estrategia transaccional y aporte estudiantil enviada, **sin respuesta registrada**
al cerrar. No equivale a aprobación del equipo.

## Cambio aplicado: FIX-001, corrección funcional de soporte

`app/src/main/java/com/inkr8/data/Users.kt:3,29,34`: import PropertyName y
`@get:PropertyName` para isPhilosopher/isPlaced. Fuente auténtica:
`functions/src/users/userInitializer.ts:25–26`, philosopherController:56 y
submissionEvaluationEngine:288 escriben esos nombres; `UserRepository.kt:31,76`
lee con toObject y crea perfiles con Users. Sin anotación, el SDK Android escribe
philosopher/placed y no carga correctamente los flags canónicos. Se reutiliza el
patrón **ya productivo** de FirestoreSubmission.isSaved:21, sin clase/campo de BD,
relación, fórmula, callback o transacción nuevos. Tipos, defaults y constructor
siguen iguales. Es corrección deliberada del defecto previo, no conservación del
comportamiento defectuoso ni resolución de A05–A07 por SOLID.

Antes: cuatro pruebas SDK reales, una PASS y tres FAIL. Después: ambas sondas de
flags PASS; la prueba roja original AND-002 también PASS, sin cambiar el archivo
LabWaitTest ni sus aserciones. Compatibilidad comprobada con los aliases antiguos
**en falso** coexistiendo con flags del servidor en verdadero. El listener real
de UserRepository actualiza AppViewModel y el resultado A navega a placementReveal
cuando el flag cambia false→true: PASS. Evaluación escrita como fixture explícita;
no prueba placement automático ni su orden de entrega desde Functions. No se
inspeccionaron datos reales; no se declara compatibilidad con perfiles históricos
arbitrarios que sólo tengan placed/philosopher=true, ni se migran/eliminan aliases.

La separación existente del modelo, repositorio y consumidores se mantiene. Aquí
no hace falta Strategy, interfaz ni extraer getters: el defecto era un contrato
del SDK. Todos los archivos de IMPL-007–009 se conservaron byte a byte, incluidos
lecturas diferidas, firmas, operaciones y límites transaccionales.

## CT-01 / modo: reproducción y corrección mínima a aportar

Cadena contrastada:

| Fragmento actual | Campo real / resultado |
|---|---|
| SubmissionFactory → SubmissionMapper:44 → FirestoreSubmission:14 → addSubmission:35 | Dominio ON_TOPIC se almacena como gamemodeName=ON_TOPIC, sin gamemode. SDK/repositorio reales. |
| SubmissionMapper:15 | Lee sólo gamemodeName; una fuente servidor con gamemode=ON_TOPIC llega como cadena vacía. Sonda roja conservada. |
| submissionEvaluationEngine:101,194,248 | Registra/lee gamemode; con payload Android, R8 recibe STANDARD. Motor auténtico compilado, evento y R8 dobles. |
| dailyStatsSnapshot:55 | Sigue usando gamemodeName para la estadística temática. Renombrar sólo el escritor perjudicaría este consumidor. |

Ejemplos reproducibles: `ANDROID_MODE_PAYLOAD.json`, `GAMEMODE_CONTRACT.json` y
LabDataContractTest. Se conserva el documento capturado; otra fixture adapta sólo
autor/contenido para superar la calidad y llegar a R8. Resultado: ON_TOPIC→STANDARD,
themeId/topicId presentes pero themeName/topicName null. El contexto por nombres
es CT-02 independiente; no se rellena por suposición. calculateMerit:50–81 recibe
gamemode pero actualmente **no lo utiliza**: no se afirma diferencia de Merit
por esta discrepancia, ni se añade un bonus.

Alternativa mínima propuesta: conservar las escrituras, aceptar ambos nombres en
los lectores afectados y reutilizar los códigos existentes STANDARD/ON_TOPIC.
Evita modificar históricos o diseñar esquema nuevo. Falta decidir únicamente el
caso de dos valores diferentes: prioridad de gamemode o error explícito. El
fallback cuando no hay ninguno tampoco debe modificar silenciosamente la
semántica existente de cada consumidor. El estudiante debe implementar/justificar
esa traducción en `SubmissionMapper.toDomain` y en la preparación de argumentos
del motor; revisar dailyStats para documentos de ambos productores. No se eligió
ni implementó esta conexión mediante IA. Pruebas listas: lector del DTO rojo,
captura de escritor y comprobación Android→motor roja. Completar STANDARD/ON_TOPIC,
ambos nombres coincidentes/conflictivos/ausentes y preservar Playmode por separado
cuando se resuelva el contrato.

## A05 / confirmación y borrador: intervención acotada

Decisión humana vigente: conservar texto/borrador hasta guardado confirmado.
No se vuelve a consultar. Recorrido actual y frontera precisa:

1. Writing:323 crea el envío y su UUID; **342 borra borrador**, 344 invoca callback,
   345 vacía editor. No hay retorno de confirmación: firma en Writing:45.
2. AppRoot:84 conecta ese callback a AppViewModel.submitWriting:140, que sólo
   expone error al editor. Los mensajes y validación actuales deben conservarse.
3. FirestoreSubmissionRepository.addSubmission:19–37 ya distingue onSuccess y
   onError mediante el Task real de set. Confirmación es el éxito de ese Task,
   no crear el objeto, encolar el write local, ver PENDING ni recibir EVALUATED.
4. Sólo en éxito se puede limpiar la revisión de texto/borrador remitida; el
   inicio de espera sigue ocurriendo desde el éxito de persistencia. En fallo,
   no limpiar ni navegar a espera; conservar la limpieza de sesión/mensaje
   existentes del ViewModel. No generar otro UUID al confirmar.

Aporte estudiantil exacto: diseñar y escribir el retorno de esa confirmación
`addSubmission → submitWriting → AppRoot → Writing`, retirando la limpieza
anticipada. Propuesta mínima, no elección humana: callback de finalización del
envío, reutilizando los callbacks existentes; alternativa: estado observado de
confirmación en VM, con más sincronización. No se exige copiar una edición
mecánica. El estudiante debe demostrar qué evento confirma y qué ocurre en
fallo; Codex puede ajustar el soporte de pruebas a la API realmente escrita.

Sonda nueva real: rechazo PERMISSION_DENIED por regla **de laboratorio**, editor
vacío y borrador 0 bytes frente a 376 esperados: FAIL. Caracterización AND-002
inalterada y en archivo separado. El éxito real previo de addSubmission continúa
como evidencia del callback de repositorio, no de confirmación UI corregida.
Después del aporte: comprobar pendiente antes del ack, éxito, rechazo y reintento
manual sin debilitar la preservación. Cuenta/ejercicio y edición/segundo envío
mientras hay persistencia pendiente no tienen todavía contrato completo; acotar
la primera verificación a una cuenta, ejercicio y revisión de texto. No introducir
claves nuevas, bloqueos del editor ni políticas de concurrencia de borradores
sin resolver esos casos. Admisión, tokenización, conteo y límites intactos.

## A06 / identidad: intervención acotada sin políticas nuevas

CT-04 ya aporta identidad: SubmissionFactory:20 genera UUID, repo escribe document(id)
y motor usa snapshot.id. La brecha está en `startLoadingResult:278`: pierde ID y
consulta/listen al más reciente (getLastSubmission:137 / getLastSubmissionRealtime:151).
Nueva sonda de aceptación: A persistido/PENDING, B más reciente/EVALUATED → el VM
resuelve B antes de A: FAIL, con ambos IDs en IDENTITY_OBSERVATION.json.

Aporte estudiantil exacto: pasar el **ID del envío confirmado**, con su autor,
desde `submitWriting` a `startLoadingResult`; añadir lectura/listener del documento
exacto en el repositorio, conservando las APIs de último envío para sus otros
consumidores. Listener y sondeo deben usar el mismo ID; handleSubmissionUpdate
debe rechazar otro ID y un callback de una espera anterior ya sustituida. No basta
filtrar la query latest: B podría ocultar permanentemente A. Conservar eliminación
del listener/cancelación del job y limpieza en onCleared. No añadir IDs de BD ni
un estado Strategy para justificarlo.

Pruebas: nueva A/B roja y AND-002 (FAILED→Home, error registrado/no resolución,
poll3s, timeout lógico>90→93, resultado tardío ignorado). Estas últimas no se
repitieron: no cambió VM/repositorio/política. Después de la conexión escrita,
ejecutar A/B fuera de orden, A ya evaluado al suscribirse, callback de espera
anterior y regresión de esas políticas. Duración/reintento/resultados tardíos
permanecen abiertos para una corrección funcional posterior, no se reinterpretan.

## A07 / idempotencia y candidato exclusivo: alternativa estudiantil

Motor: transacción de evaluación en submissionEvaluationEngine:212 sólo relee
usuario; guardia status:127 examina **snapshot de creación**, que continúa PENDING
en una entrega repetida. Matcher: consulta candidatos:406 fuera de transacción;
tx:438 relee usuarios:439–440 pero no los dos envíos seleccionados. Los SDK y
transacciones son reales en las reproducciones; no se ha implementado su solución.

Invariantes de verificación derivadas de los efectos conservados, para validación
estudiantil, sin ganancias/fórmulas nuevas:

- Un envío produce una sola evaluación contabilizada: count, Merit líquido/reserva,
  total ganado, recentScores/placement y registro monetario no vuelven a crecer
  por repetir el mismo evento; cap/reparto/fórmulas vigentes se conservan.
- Ambos documentos de la pareja quedan MATCHED con rival recíproco, scores/outcomes
  consistentes; un candidato no pertenece a dos parejas. Rating y rachas se aplican
  una vez y respetan los dos cálculos actuales y el clamp cero dentro de la tx.
- Repetir creación tras emparejar no cambia resultado, rating, evaluation.ratingChange
  ni vuelve a PENDING; una relectura EVALUATED sigue siendo omitida.
- El reparto al llegar al cap cuenta igual: la repetición puede duplicar reserva
  aunque el número de documentos de recompensa líquida siga siendo uno.

Dos alternativas propuestas, no aprobadas: (a) releer/controlar el estado actual
del envío al contabilizar y de **ambos envíos** al emparejar en las transacciones
existentes, con sus efectos en el mismo commit; mínimo sin esquema nuevo;
(b) registro separado de eventos procesados, con decisión estudiantil de BD,
identidad CloudEvent y retención adicional. Sólo la guardia del snapshot de
creación o un check anterior a tx no protege concurrencia. Dejar cálculos y efectos
en la frontera actual; no añadir ganancias, ghost, nuevas reglas Ranked o temporadas.
No se afirma que una alternativa cierre fallos de R8 después de commit o el orden
entre distintas entregas; esos caminos deben revisarse con el aporte concreto.

Aporte exacto: estudiante escribe/justifica los controles atómicos y el resultado
de perder la carrera dentro de evaluación:212 y tryMatchRankedSubmission:438;
demuestra qué lee antes de escribir, cuándo el retry de Firestore vuelve a
evaluar disponibilidad y por qué no duplica efectos. Mantener selección ±20/48h,
fórmula importada IMPL-009 y valores/sesión fuera de la corrección.

Siete sondas principales: **1 PASS / 6 FAIL**, más cap/hold **0 PASS / 1 FAIL**.
Practice repetido: count2/Merit1610 frente a count1/1305; carrera: claims2,
reciprocidad1, candidato rating48/racha2 frente a54/1; placement repetido:2/160
frente a1/80. Con cap: reserva510 frente a205 y un solo registro monetario.
La prueba antigua que afirma estos defectos como caracterización no se cambia.
Las nuevas son sondas rojas separadas. CloudEvents son invocaciones sintéticas
con snapshot, no entregas completas para probar un ledger por event.id; si se
elige (b), hay que completar esa fixture con identidad auténtica. R8 fijo80 y
barrera de planificación son dobles, transacción/DB reales. Destino nombrado en
el mismo emulador demo evita cruzar fixtures históricas, sin borrarlas.

## Resultados, revisión separada y reproducción

- Android auténtico `:app:assembleDebug` PASS; APK original actualizado y anterior
  preservado en privado. No instalar el original con configuración cloud.
- Copia lab con configuración falsa demo: builds app/instrumentación PASS. Ocho
  métodos Android únicos pertinentes: **5 PASS, 3 FAIL** (gamemode, borrador,
  identidad). No sumar repeticiones ni llamarlos aceptación integral.
- Motor compilado auténtico previo reutilizado: Functions/src y configuración
  intactos, no repetir build sin cambio. La sonda Android→motor FAIL esperado
  por modo; ocho invariantes transaccionales **1 PASS / 7 FAIL**, aisladas.
- Auditor: **175 contrastes estáticos PASS**, no pruebas funcionales. Spec anterior
  y 171 resultados preservados. Sólo Users pasa de conservación byte a byte a
  reversión exacta de import/anotaciones contra Git HEAD; demás oráculos intactos.
  Dos controles negativos rechazan default alterado y sonda roja debilitada en
  copias privadas. Nuevos hashes de soporte tienen trazabilidad, no esconden FAIL.
- Revisión Codex separada: consumidores/firma/import/defaults de Users, call-sites
  de flags, contratos de escritura/lectura y diffs; sin otro cambio productivo.
  No se declara revisión humana/independiente ni SOLID integral. Hallazgo del arnés:
  cierre de DB antes de matcher background; corregido esperando tx pendientes,
  sin cambiar aserciones ni motor. Primer resultado/fallo conservado.

P1 §2.7, fuente exacta `evidence/IMPL-007-009/P1_pages_6_7.txt`: permite boilerplate
y pruebas sobre lógica existente; reserva «Los algoritmos que resuelven el
problema principal del proyecto deben ser escritos por el estudiante» y «La
conexión entre el Backend y el Frontend debe ser manual». Se aplica a conexión
de confirmación/identidad y controles atómicos, no a copiar anotaciones o trasladar
literalmente código. P1 §2.5 requiere validación/ejecución humana; las ejecuciones
de Codex no acreditan esa competencia. Falta aporte real; autorización no equivale
a implementación manual. No hay arquitectura o algoritmo crítico nuevo de IA.

Comandos/entorno y archivos públicos de apoyo: README del laboratorio. Reutilizar
AVD/SDK/lab actuales; evaluator delivery **deshabilitado** en pruebas de espera.
Auth9099/Firestore8080 por loopback, ADB reverse y firewall del UID lab. Functions
no se lanzó aquí; sus pruebas callable/delivery de AND-002 son antecedentes.
Rutas, SHA y tamaños de los tres APK en `evidence/CORE-001/APKS.json`; resultados
de JUnit en ANDROID_RESULTS y command.json/console.log por ejecución.

Después del aporte auténtico, sincronizar sólo los fuentes afectados a la copia
lab, compilar app/instrumentación y seleccionar sondas pertinentes. Un FAIL
actual es dependencia demostrada; no convertirlo en PASS debilitando aserciones.
Revisar el diff incremental de CORE-001 antes de una auditoría independiente.
Ranked/rating/Merit permanecen; retiradas torneos/ligas/reputación y temporadas
no se tocaron. Datos locales exportados antes de detener servicios; no nube,
secretos publicados, push/merge, despliegue o borrado.

## A05 — primera intervención estudiantil, CORE-002 en curso

Encargo humano posterior en este chat: guiar addSubmission → submitWriting →
AppRoot → Writing; revisar el código realmente aportado y adaptar sus pruebas.
La decisión de conservar texto/borrador hasta confirmación permanece aprobada.
No hay aún edición estudiantil nueva, conexión completada o elección de
precedencia de modo registrada. El mensaje «dale puedes continuar» sólo autorizó
continuación. P1 §2.7 reserva la conexión Backend/Frontend al estudiante;
boilerplate y pruebas siguen como apoyo permitido, sin exigir copiar código.

Primera tarea propuesta, aún sin implementar: modificar únicamente
`app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt`, submitWriting:140 y
el onSuccess de addSubmission:167. Añadir una notificación opcional sin argumento,
por ejemplo onPersisted, que indique éxito del guardado. Mantener onError como
último parámetro: sus cuatro consumidores actuales usan lambda final de error.
El valor por defecto del callback nuevo debe conservar compilación/uso actual.
Invocar la notificación exclusivamente desde el éxito del repositorio y antes
de startLoadingResult, para que Writing pueda limpiar antes de salir del editor.
No tocar copia de autor/PENDING/evaluation, errores/mensajes, limpieza de sesión,
branch de torneos ni políticas de espera. No hay implementación completa de IA
para copiar: el estudiante escribirá y explicará este pequeño tramo real.

Evento existente de confirmación: Task de `submissionRef.set` y su
addOnSuccessListener (FirestoreSubmissionRepository:32–34), no click del botón,
creación del UUID, callback Unit de Writing o llegada de evaluación. Contrato
mínimo propuesto: notificación de persistencia sólo en éxito; onError existente
en fallo; editor/borrador intactos mientras no hay confirmación y ante fallo.
La limpieza definitiva y conexión en AppRoot/Writing se guiarán tras revisar el
primer aporte; el callback vacío opcional por sí solo no resuelve A05.

Orden de revisión tras recibir el aporte: diff contra el snapshot protegido,
firma/consumidores/onSuccess/onError, compilación afectada; después guiar
AppRoot:84–88 y Writing:45/342–345. Adaptar las sondas a la API escrita y ejecutar
pendiente, éxito y rechazo sin debilitar las aserciones de conservación. No
inventar bloqueo de edición, segundo envío, clave cuenta/ejercicio o regla nueva.
El paso A06 seguirá después con ID existente, documento exacto, listener/sondeo
y protección de callbacks antiguos, conservando timeout/error/tardíos actuales.

Recomendación de precedencia pendiente de decisión: gamemode por ser el nombre
que ya consume el evaluador auténtico; gamemodeName como lectura alternativa
cuando falte. Si ambos difieren, gana gamemode y puede cambiar el modo que
mostraban los lectores antiguos. Conservar escrituras e históricos hasta que
el estudiante resuelva el contrato. No aprobado/implementado por la recomendación.
A07 conserva pruebas/preparación; algoritmo atómico no escrito automáticamente.

Estado de esta continuación: checkout/consumidores contrastados, respaldo CORE-002
preparado; **cero cambios productivos y cero suites/builds nuevos**. El laboratorio
no se arrancó. La comprobación AppRoot+entrega automática identificada al inicio
queda pendiente de la intervención guiada, no se registra como ejecutada. Último
resultado comprobado: CORE-001/FIX-001. Originales, baseline y evidencias intactos.
Codex: lectura, propuesta y registro; estudiante: aporte todavía pendiente.


**Revisión efectiva posterior A05 (06/10/2026):** AppViewModel comparado con el
respaldo CORE-002 sigue idéntico byte a byte; onPersisted no existe. El diff de Git
corresponde a IMPL-008 previo, no a la edición guiada. Sin compilar/repetir pruebas
ni escribir la conexión por el alumno. Próximo paso: aporte acotado en firma y
onSuccess de submitWriting descrito arriba; después revisar, compilar y guiar
AppRoot/Writing conjuntamente. A06 expresamente fuera de esta etapa. Pruebas
pendiente/éxito/rechazo de la conexión nueva no ejecutadas porque no está escrita.
