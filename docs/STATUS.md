# Estado de trabajo de Inkr8

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

**Antecedente de publicación FIN-002 previo a ACCOUNT-ACCESS-001:** [PR #2 en borrador](https://github.com/Rnz5/Inkr8-ISW2/pull/2)
hacia master; [rama](https://github.com/Rnz5/Inkr8-ISW2/tree/codex/refactorizacion-por-bloques).
Código/pruebas/UML/informe: Codex; publicación con cuenta autorizada MACOABC.
7 Android,39 permisos,6 nombre,14 operación,16 borrado y2 log PASS;287 contrastes
estáticos separados. Copia limpia con autocrlf=true conserva recursos auténticos y
89 artefactos. Se corrigió versionado de bytes, sin modificar oráculos anteriores.
Sin merge/deploy/datos reales; dependencias externas/académicas en FINAL_DELIVERY.

**Vigente FIN-002 — 09/10/2026:** cierre y entrega revisable documentados en
[FINAL_DELIVERY.md](FINAL_DELIVERY.md), con 8 Android PASS, reglas nuevas
propuestas y límites externos/académicos explícitos. Los estados inferiores son
antecedentes preservados; no revocan DEC-AI-AUTH-001, las decisiones respondidas
ni el permiso actual de publicar rama/PR, sin fusión o despliegue.

**Vigente RET-006 / SEA-001 — 07/10/2026:** retirada funcional local de torneos,
ligas y reputación Android/Functions; Ranked/rating/Merit conservados. Marco
aprueba elección Standard/On-Topic y factor histórico de precio; aprueba temporadas
mensuales UTC con cierre tras pendientes, sin nuevas recompensas ni backfill.
Codex implementa bajo DEC-AI-AUTH-001. Android/Functions/lab compilan; 37 métodos
Android únicos y 19 unitarios PASS; retiradas 10 SDK PASS, temporadas 21 SDK +1
evento automático (6 documentos), Ranked11/ghost2/lifecycle1 PASS. 281 contrastes
estáticos separados, criterios anteriores intactos, 2 negativos rechazados.
Regresiones y fallos de soporte conservados/corregidos; no cuenta repetida.
Cierre local con R8 HTTP doble; reglas Firestore originales/seguridad, proveedor
externo, scheduler/fin de mes/escala e informe académico pendientes.
[Resultados, decisiones, diff, APK y reproducción](FUNCTIONAL_CLOSURE.md).
Los pendientes históricos de selección/temporadas quedan resueltos por las dos
respuestas humanas; no se infiere aprobación grupal ni código estudiantil.

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

**A06-CON-001 — identidad y ciclo de espera comprobados, 06/10/2026:**
Codex transporta finalSubmission.id tras persistir; listener y sondeo leen ese
documento. Generación de espera rechaza callbacks anteriores y posteriores a
onCleared. Android auténtico PASS (52 s); tres sondas nuevas antes 3 FAIL,
después **8 métodos pertinentes PASS**, incluida roja B≠A y políticas existentes
FAILED/error/timeout nominal 93/tardío, sin editar sus aserciones. Dos entregas
obsoletas son dobles explícitos por reflexión; otros recorridos usan SDK/emulador.
A05 regresión de éxito PASS. Auditor 181 estáticos PASS, no aceptación funcional.
No nuevas políticas; reintento/H1/contexto y compatibilidad siguen abiertos.
Código/revisión/pruebas del agente bajo DEC-AI-AUTH-001. Continúa A07.
[Resultados](evidence/A06-CON-001/EXECUTED_RESULTS.json).

**A05-CON-001 — corrección funcional comprobada, 06/10/2026:** Codex
conectó confirmación de persistencia por AppRoot hasta Writing; limpieza sólo en
éxito. Android auténtico PASS (49 s). Tres sondas AppRoot antes: 1 PASS/2 FAIL;
después, esas tres y dos sondas existentes: **5 PASS/0 FAIL**, incluida la roja
CORE-001 de rechazo, sin cambiar aserciones. Firestore/Auth locales reales,
reglas de rechazo fixture; evaluador deshabilitado. Auditor estático 179 PASS,
dos controles negativos rechazados; fallo intermedio de codificación conservado
y corregido restaurando los campos originales. DEC-AI-AUTH-001 permite continuar
sin aporte manual nuevo, según autorización docente comunicada por Marco;
código/pruebas/revisión del agente. Alcance una cuenta/ejercicio/revisión;
edición simultánea, segundo envío y claves siguen abiertos. A06 sigue pendiente
de asociación exacta; se continúa su implementación. [Evidencia](evidence/A05-CON-001/EXECUTED_RESULTS.json).

**DEC-AI-AUTH-001 — permiso particular comunicado por Marco, 06/10/2026:** Marco aclara que el código original fue hecho manualmente y que el profesor autorizó al agente a realizar la refactorización, sin nuevas ediciones manuales del estudiante. Esta dirección posterior elimina el bloqueo operativo de autoría manual que se aplicaba a A05–A07 y sus conexiones/correcciones acordadas sobre el código existente. Codex puede implementarlas y comprobarlas autónomamente dentro del alcance vigente; registrar cada cambio como refactorización conservadora o corrección funcional, con autoría real del agente. La aprobación de reglas de negocio aún abiertas y los requisitos de evidencia/entrega no se inventan. Los apartados anteriores que exigían aporte manual para continuar la refactorización quedan como antecedentes superados; las copias originales de P1 no se reescriben.


**A05 — conexión guiada, revisión 06/10/2026:** soporte A05-SUP-001
verificado contra sus hashes y reconstrucción exacta; se reutiliza assembleDebug
PASS de 46 s, sin nueva compilación. AppRoot/Writing siguen sin conectar la
confirmación y conservan la limpieza anticipada. Próximo aporte estudiantil:
transmitir una continuación de éxito en esos dos archivos y mover allí la limpieza.
Ningún código estudiantil recibido todavía; 0 cambios productivos y 0 pruebas
nuevos en esta revisión. Pendiente/éxito/rechazo se ejecutarán tras revisar el aporte
real, preservando las aserciones de retención. Una cuenta/ejercicio/revisión; A06
fuera de la etapa. [Evidencia](evidence/CORE-002/A05_CONNECTION_CHECKPOINT_001.json).

**A05-SUP-001 — soporte Codex, 06/10/2026:** añadido onPersisted opcional en submitWriting antes de onError y notificado sólo en éxito de addSubmission antes de esperar. AppRoot/Writing/repositorio intactos; consumidores actuales usan el valor vacío. La conexión de confirmación y limpieza sigue pendiente; no aporte estudiantil ni cierre A05. Compilación Android afectada PASS (assembleDebug, exit 0, 46 s), sin nuevas pruebas de pantallas. [Detalle y límites](CORE_IMPLEMENTATION.md).

**Revisión A05 / CORE-002 — 06/10/2026:** primera edición comprobada
contra el respaldo: AppViewModel permanece idéntico; no existe onPersisted.
onError, preparación, errores, sesión y torneos intactos. 0 builds/pruebas nuevos,
sin cambio productivo ni preparación del entorno. Próximo aporte: notificación
opcional en submitWriting, sólo desde éxito de persistencia y antes de la espera.
Después se guiarán AppRoot/Writing y comprobarán pendiente/éxito/rechazo.
A06 fuera de esta etapa; CORE-001 sigue siendo último resultado ejecutado.
[Contrato y tarea](CORE_IMPLEMENTATION.md).

**CORE-002 en curso — A05 guiado:** primera edición estudiantil en
submitWriting:140/167: notificación opcional de persistencia confirmada, conservando
onError como último parámetro y el éxito/error existente. Ningún aporte de código
recibido todavía; no implementación de conexión ni pruebas nuevas atribuibles.
Último resultado comprobado sigue siendo CORE-001/FIX-001. Laboratorio detenido,
originales/evidencias preservados. [Tarea pequeña y verificable](CORE_IMPLEMENTATION.md).

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

**Vigente AND-002 — 06/10/2026:** WHPX/AVD operativos y pantallas ejecutadas en
demo-inkr8-local. 14 métodos Android únicos: 13 PASS y 1 FAIL real (isPlaced no
llega al modelo); tres contrastes centrales antes/después PASS; cinco escenarios
transaccionales iguales y entrega automática local/callable PASS, R8 HTTP doble.
No código productivo nuevo; soporte/fixtures corregidos. Borrador anticipado,
resolución por B, idempotencia/carrera y payload gamemode siguen como defectos o
contratos pendientes. Ranked/rating/Merit permanecen. [Resultados, APK, límites y
aportes concretos](ANDROID_EXECUTION.md). No cierre integral ni autoría estudiantil.

**AND-001 — ejecución Android y comprobación ampliada, 05/10/2026:** SDK oficial API 36/Build Tools 36.0.0 instalado tras aceptación humana «claro». Android compila con Gradle/AGP/versiones recibidas intactas, APK debug firmado generado. 16 métodos pertinentes del módulo real + 1 ejemplo PASS; incluyen dos casos de snapshots Compose, no render/recomposición de pantallas. Cuatro escenarios del motor Ranked en Firestore local reales antes/después dan resultados idénticos; R8 doble y CloudEvent sintético. Auth local REST operativo, sin Google OAuth. Emulador Android sin controlador de aceleración, intento por software permanece offline: cero pantallas ejecutadas. Auditor 158 contrastes estáticos, expectativas anteriores conservadas. Sin cambio de producción, reglas, integración, retiradas o temporadas. [Resultados, reproducción y bloqueo](ANDROID_EXECUTION.md).


**Alcance posterior SCP-001 — registro 05/10/2026:** Marco comunica una nueva captura/mensaje de Renzo: «Ranked y Merit permanecen»; «Torneos, ligas y reputación se retiran». La imagen original y su fecha no están adjuntas al contexto disponible; no se infiere aprobación grupal ni fecha de subida. Esta instrucción humana posterior sustituye la retirada total de Merit de U3/DEC-03 para el trabajo vigente. Se conservan Ranked/rating/Merit y las reglas económicas existentes; no se inventan usos, precios o recompensas nuevos. Temporadas permanece incluida con reglas pendientes. RET-001–005 y sus evidencias describen el alcance anterior y no se reescriben. [Recuperación y restitución](RECOVERED_ENVIRONMENT.md).

**REC-002/SCP-001 — resultado local:** fuentes/configuración recuperadas sin sobrescribir los 193 archivos previos; Functions compila con TS 5.9.3/Node 20 antes y después de IMPL-009, 21 escenarios rating PASS sobre JS del build completo. Seis casos del evaluador auténtico con SDK real y transporte simulado PASS como caracterización; uno reproduce NaN previo, no aceptación. Android primer build real falla por SDK ausente antes de compilar Kotlin/Compose. Restituidos tres indicadores RET-001 de Merit, wallet propio Profile, controles de capacidad Settings y textos de racha Home; sin tocar ligas/reputación/reglas/backend. PSI 0 errores antes/después; auditor actualizado con fuente humana y hashes auténticos: 155 contrastes estáticos, no pruebas funcionales. Resultados/fallos iniciales y evidencias anteriores conservados. [Detalle, comandos y dependencias](RECOVERED_ENVIRONMENT.md).


**REC-001 — 05/10/2026:** confirmación humana de subida comprobada: no nueva rama/commit/PR visible en Inkr8-ISW2 ni paquete en las carpetas Drive registradas 04/05. 193 archivos locales + historial Git respaldados/verificados; producción/evidencias anteriores intactas. Cero originales importados, sin nuevas compilaciones. Consulta única pendiente por enlace o rama exactos de Renzo; [registro](IMPLEMENTATION_LOG.md). No búsqueda histórica repetida o configuración reconstruida.


**RET-005 — 05/10/2026:** retirados nombre y veredicto de liga de PlacementReveal; conserva «Calibration Complete», secuencia/espera/botón y firma/conexión anteriores. Sintaxis Kotlin PSI 0 errores antes/después; auditor 135 contrastes estáticos finales y dos controles negativos nuevos rechazados. Continúa RET-002–004 sin modificar sus entregas/evidencias. No nueva regla o sustituto de placement ni prueba Compose/Firebase. [Registro y límites](IMPLEMENTATION_LOG.md).


**RET-002–004 / revisión trazable — 05/10/2026:** retiradas de presentación aprobadas aplicadas en Profile, Competitions, Settings y Home; separadas de IMPL-001–009 y temporadas. Perfil básico/rating, controles/selección Ranked, auth y persistencia conservados. Kotlin PSI 0 errores antes/después; auditor 135 contrastes estáticos finales y cuatro controles negativos rechazados, sin Compose/Firebase funcional. [Cambios, límites y evidencia](IMPLEMENTATION_LOG.md). Cobros, listeners/rutas, placement ligado a ligas, integración de borrador confirmado y reglas/entorno originales siguen pendientes. Renzo confirmará fuentes; no búsqueda repetida ni automatización.


**VER-001 — 05/10/2026:** revisión separada de IMPL-007–009 sin defecto nuevo de extracción; [ejecución y límites](EXECUTION_READINESS.md). Productor rating typechecked y call-sites compilados con TS 7.0.2 disponible: 1.156 pares/2.312 deltas sin diferencias; control UNKNOWN rechazado TS2345. Gradle/JDK del arnés operativos; Android/Functions/Firebase/R8 siguen sin fuentes/configuración auténticas. [Lista única para el equipo](ORIGINAL_ENVIRONMENT_REQUEST.md). Decisión humana: conservar texto/borrador hasta persistencia confirmada; conexión de confirmación UI pendiente según P1 §2.7. Espera: orientación general recibida, sin regla nueva asumida; asociación cuenta/ejercicio y Ranked pendientes. Sin nuevas suites JVM/auditor repetidos ni cierre integral.



**Vigente — IMPL-007–009 / AUD-002, 05/10/2026:** extracciones mecánicas locales de admisión, decisiones escalares de espera y fórmula de rating; [registro y límites](MECHANICAL_CORE_REFACTOR.md). Pruebas pertinentes antes/después: Writing 20→28 PASS, espera 15→15, rating 21→21; auditor 135 PASS y cuatro controles negativos rechazados. Corrección explícita: P1 no exige copiar código para cualquier extracción. Todo código/pruebas es Codex; aporte humano = encargo/límites/autorización, sin atribuir elección arquitectónica o implementación estudiantil. A05–A07 sólo parcialmente abordados; entorno/integración/decisiones funcionales pendientes. Los estados anteriores que siguen son históricos.



**Revisión local AUD-001 — 05/10/2026:** [resultado y ejecución](LOCAL_AUDIT_REVIEW.md). Herramienta reproducible añadida: 132 contrastes estáticos finales pasan, dos controles negativos rechazados. Respuesta «sigue la mejor opcion posible» registrada; justificación/aporte estudiantil A05–A07 aún no acreditados. Producción y evidencia anteriores intactas; diff listo para auditoría independiente. Sin validación integral o nuevas suites JVM/Node.

**Preparación SOLID A05–A07 — 05/10/2026:** [tabla, consumidores y verificación](SOLID_BLOCK_PREPARATION.md). Consulta estudiantil única pendiente; ninguna alternativa elegida ni código crítico/integración automática. 105 fuentes revisadas; búsqueda adicional de originales sin nuevos archivos. IMPL-001–006, RET-001 y evidencia preservados. Cero nuevas ejecuciones JVM/Node/funcionales; A05–A07 siguen abiertos.

**Cierre de continuación — 04/10/2026:** IMPL-005 (15 PASS JVM/21 PASS Node), IMPL-006 (20 recursos auténticos recuperados), RET-001 (presentación retirada parcialmente). [Registro](IMPLEMENTATION_LOG.md) y [plan](CONSERVATIVE_BLOCK_PLAN.md) actualizados. A05–A07 requieren respuesta/aporte estudiantil; builds/Firebase/R8 siguen faltando. No refactorización/retirada integral ni temporadas completadas.

**Retirada de presentación RET-001 — 04/10/2026:** widgets Merit y League/Pantheon de cabecera/resultados retirados localmente; rating/perfil/resultados conservados. Cambio funcional parcial separado; cobros/backend/otras pantallas e integración manual pendientes. [Registro](IMPLEMENTATION_LOG.md).

**Recuperación local IMPL-006 — 04/10/2026:** 20 recursos Android originales restaurados con blobs verificados y 72/72 archivos compartidos idénticos. [Registro](IMPLEMENTATION_LOG.md). Builds/configuración Firebase/R8 y aportes estudiantiles A05–A07 pendientes; no producto integral o aceptación acreditados.

**Continuación local IMPL-005 — 04/10/2026:** caracterización previa acotada de A05–A07. [Registro](IMPLEMENTATION_LOG.md), [dependencias](CONSERVATIVE_BLOCK_PLAN.md). Los cambios centrales A05–A07 mantienen la consulta estudiantil pendiente; no se declara aceptación integral ni autoría humana de los aportes Codex.

**Cierre de continuación local — 04/10/2026:** IMPL-003/004 realizados; IMPL-001/002 y evidencias intactos. [Bloques/dependencias](CONSERVATIVE_BLOCK_PLAN.md) y [registro consolidado](IMPLEMENTATION_LOG.md). La siguiente intervención del núcleo requiere elección/aporte estudiantil o entorno auténtico; refactorización y aceptación integrales abiertas. Sin publicación/despliegue/datos.

**Bloque local A04 — 04/10/2026:** [IMPL-004](IMPLEMENTATION_LOG.md), separación de puntuación y feedback de Results; sintaxis y equivalencia de bloques fuente comprobadas; sin ejecución Compose. [Plan pendiente](CONSERVATIVE_BLOCK_PLAN.md). IMPL-001/002 preservados; revisión/intervención estudiantil y entorno integral pendientes, sin publicación ni cierre de refactorización.

**Bloque local A03 — 04/10/2026:** [IMPL-003](IMPLEMENTATION_LOG.md), separación de componentes informativos de Writing; 20 PASS antes/después en fragmentos y conservación de fuentes. [Plan pendiente](CONSERVATIVE_BLOCK_PLAN.md). IMPL-001/002 preservados; revisión/intervención estudiantil y entorno integral pendientes, sin publicación ni cierre de refactorización.

**Estado local A02 — 04/10/2026:** [IMPL-002](IMPLEMENTATION_LOG.md) aplicado y caracterizado: 20 PASS antes/después, sólo expresiones Kotlin extraídas de Writing y cuatro fuentes auténticas. canSubmit/wordsUsed intactos; no se compila ni se prueba la pantalla Compose. IMPL-001 conserva sus 38 PASS anteriores. No hay aceptación integral, autoría/revisión estudiantil, retirada/temporadas ejecutadas o publicación acreditadas. Los resúmenes de etapas anteriores que siguen son antecedentes.

**Etapa local vigente — 04/10/2026, chat 13, U19/DEC-26:** [IMPL-001](IMPLEMENTATION_LOG.md) aplicado sobre 7d879c2 en rama local codex/refactorizacion-por-bloques. Dos Regex reutilizadas en ValidationUtils; 38 PASS antes y 38 PASS después en JVM aislado (22 históricos + 16 nuevos). Sin publicación, retiradas/temporadas ejecutadas, producto integral, revisión estudiantil o autoría/pruebas individuales acreditadas. Los encargos y restricciones documentales descritos más abajo son antecedentes; esta autorización sólo amplía el trabajo local indicado. Q-R/Q-T/Q-D/Q-C siguen abiertos.

**Actualizado:** 03/10/2026 · America/Lima · versión documental 0.1.

## Etapa actual y autorización

Se crearon AGENTS/docs iniciales por U4. U5 autoriza publicar esta base documental en una rama con PR hacia master. La propuesta se limita a documentación; la refactorización y los cambios funcionales no han comenzado.

Base de código examinada y usada para la propuesta: `64983846d6dbf4cdfafcdd508464cd140a2a862e`, master. Esta edición añade documentos al repositorio oficial; la redacción y auditoría anteriores se hicieron en copias separadas. No se ha reconstruido el build ni cambiado producción. [Fuentes](SOURCE_INDEX.md).

## Resultados y límites

| Resultado | Evidencia | Estado |
|---|---|---|
| Auditoría y mapa técnico | G1/G2, ediciones publicables en reference | Completados como análisis; no diseño aprobado |
| Baseline JVM | 22 PASS, 0 FAIL/ERROR/SKIPPED el 02/10/2026 | Histórico; no prueba producto completo ni requisitos individuales |
| Alcance humano | D1/U2/U3, DEC-01–05/10 | Confirmado según fuentes |
| AGENTS y nueve docs principales | Índice docs/README | Creados; revisión humana de esta versión pendiente |
| Publicación documental | U5, DEC-11; rama codex/documentacion-inicial-inkr8 | Propuesta de integración en master mediante PR |
| Código/configuración/baseline histórico | Fuera del diff documental | Sin cambios |
| Implementación, fusión a master o despliegue | Ninguno | No autorizados en esta tarea |

El [registro de publicación](evidence/DOC-002_PUBLICATION.md) identifica selección de archivos y comprobaciones. La revisión de IA no equivale a aceptación grupal.

## Alcance asentado

Torneos, ligas, reputación y Merit completo fuera; núcleo de escritura/evaluación, Practice, Ranked, rating y perfil básico conservados; temporadas por incorporar. Mantener exclusiones específicas de D1 de detalle de partida, penalización por abandono, ranking/posición global, leaderboard de liga y consulta de perfil desde leaderboard. [Alcance](REFACTOR_SCOPE.md), [decisiones](DECISIONS.md).

## Pendientes

| Pendiente | Qué condiciona | Trabajo que puede continuar |
|---|---|---|
| Criterios completos del backlog / Anexo A | Aceptación y trazabilidad | Recuperar fuentes auténticas disponibles |
| Ejercicio Ranked sin ligas | Cambio funcional del ejercicio | Caracterizar selección actual y consultar regla humana |
| Entrada/cierre no económicos | Ranked sin Merit/reputación/penalización de abandono | Separar estado, límites y efectos retirados |
| Temporadas: períodos, ranking, cierre e historial | HU 3.27/3.28 | Matriz de aceptación sin completar reglas por inferencia |
| Compatibilidad de datos/borradores | Retirada y evolución del modelo | Inventario sin borrado de datos |
| Configuración original y R8 | Build/integración | Análisis documental y estático |
| Responsables y pruebas por integrante | Evidencia académica | Asignaciones reales y pruebas según rúbrica |
| Correspondencia releases/sprints | Informe | Registros auténticos y aclaración del profesor |

## Bloqueos técnicos del commit auditado

Faltan configuración Gradle principal/app y parte del wrapper; package.json, lockfile y tsconfig de Functions; evaluateWithR8.ts; configuración/reglas Firebase completas. Se observaron referencias a recursos Android ausentes. README de código no documenta instalación integral.

No se compiló ni probó el producto Android/Functions completo ni se verificó un despliegue. Esta publicación documental no corrige esos bloqueos.

## Siguiente tarea

**Chat:** Inkr8 — Alcance operativo y criterios de aceptación.

Leer AGENTS y los documentos del índice, D1/U2/U3/I1 y backlog real si está disponible. Consultar [mapa técnico G2](reference/TECHNICAL_MAP_2026-10-03.md), secciones 4/6/7. Producir matriz por función/HU y concentrarse en AC-01–AC-07.

Cierre: criterios originales recuperados y diferencias identificadas; reglas nuevas pendientes conservan condición de propuesta hasta decisión humana. Continuar en lo independiente si falta una respuesta. No interpretar silencio o «continúa» como una elección funcional.

## Límite operativo

U5 habilita commit/publicación **documental** en rama/PR de GitHub. No habilita fusión a master, cambios de código/configuración/baseline/originales, retirada ejecutada, temporadas, patrones/arquitectura, datos/Firebase o despliegues. Una instrucción posterior puede ampliar el trabajo dentro de su alcance real. Los estados históricos de G1/G2 no revocan U4/U5.
