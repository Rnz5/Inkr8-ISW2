# Registro de decisiones y pendientes

## FIN-002 — autorización de publicación y procedencia

Marco, prompt de continuación desde FIN-001 en este chat, registro09/10/2026:
autoriza commit/push de codex/refactorizacion-por-bloques y PR hacia master;
borrador si falta validación indispensable; no merge/deploy ni datos reales.
DEC-AI-AUTH-001 vigente, toda implementación/test/revisión nueva de Codex.
No nueva elección funcional, fecha de acto docente o aprobación grupal inferida.
FIN-002 corrige preparación/sincronización de dos sondas y exposición de token
en log; no redefine reglas económicas. Propuesta rules nueva explícita, no original.

## DEC-RANKED-SELECT-001 / DEC-SEASON-001 — respuestas humanas vigentes

Marco responde a las dos opciones recomendadas de una consulta agrupada:
«me parece muy buena tu propuesta aplicala» (Ranked) y «me gusta tu propuesta»
(temporadas). Procedencia: respuestas de este chat, registradas en evidencia
RET-006/SEA-001 y cierre 07/10/2026; no fecha inferida del acto grupal/docente.

Ranked: el usuario elige Standard/On-Topic; si tema/tópico falta o falla la consulta,
error sin cobro ni fallback, conserva elección. Reputación histórica congelada
sólo como factor del precio existente; no mostrar/comprar/actualizar. No cambia
rating, costes, recompensas ni límite de entrada.

Temporadas: meses calendario UTC; usuarios con placement mediante Ranked; rating
como variación del rating general desde entrada, sin reset de rating/placement;
orden descendente, misma posición en empate; pertenencia por guardado confirmado;
cierre al resolver pendientes, incluido ghost 48 h existente; historial de posición,
rating final y Merit ya ganado, sin premios nuevos ni asignación histórica.
Configuración de activación de la nueva funcionalidad identificada como nueva,
no original recuperada; no despliegue. Código/revisión/pruebas: Codex bajo
DEC-AI-AUTH-001, no aporte manual atribuido. [Resultado](FUNCTIONAL_CLOSURE.md).

## CORE-FINAL-001 — decisiones explícitas de Marco

Procedencia: prompt humano de continuación desde CORE-CLOSE-001 en este chat,
registrado 06/10/2026. No se atribuye aprobación grupal ni implementación humana.
Código, revisión y pruebas: Codex, bajo DEC-AI-AUTH-001.

**DEC-DRAFT-REV-001**: Writing remains editable while A persists; Success clears only the exact unchanged revision, including A->B->A protection; Block second submission during persistence; release on success/failure.

**DEC-DRAFT-SCOPE-001**: Authenticated UID and existing modality/exercise context, including assigned words; Stable recompositions/re-entry; preserve unverifiable legacy entries without attribution.

**DEC-WAIT-RETRY-001**: Notify query error/delay; manual read of same confirmed ID; Fresh wait generation, no new write/evaluation/cost/effects; Keep 3s interval and >90s threshold; log without text/secrets; recover available results.

Identidad del ejercicio usa datos existentes; entradas antiguas sin propietario
comprobable permanecen intactas y no se restauran automáticamente. El reintento
sólo consulta, no vuelve a enviar. Fórmulas, estados terminales y frentes separados
conservados. Ver CORE_FINAL_AUDIT.md y evidencia CORE-FINAL-001.

## DEC-MODE-001 — compatibilidad de lectura de modo

06/10/2026. Marco elige explícitamente en este chat conservar escrituras y datos,
priorizar gamemode, leer gamemodeName cuando falte y resolver conflictos a favor
de gamemode. No afirma aprobación grupal adicional ni edición estudiantil.
Contrastar fallbacks actuales ante inválidos/ausencia de ambos; no inventar una
validación común. Código, revisión y pruebas Codex bajo DEC-AI-AUTH-001.
Playmode sigue independiente. Retiradas/temporadas permanecen separadas.

## DEC-RES-001 — actualizar el resultado del mismo envío

06/10/2026. Marco responde «Actualizar Results del mismo envío» a la consulta
sobre emparejamiento posterior a EVALUATED. Autoriza actualizar rating/rival en
Results del mismo documento, conservando navegación inmediata y políticas de
timeout/error actuales. No autoriza esperar MATCHED antes de mostrar evaluación,
resolver con B, modificar fórmulas ni adoptar reglas de temporadas. Código,
revisión y pruebas serán del agente bajo DEC-AI-AUTH-001; no edición estudiantil
atribuida. Gamemode/gamemodeName sigue pendiente por consulta independiente.

**DEC-AI-AUTH-001 — permiso particular comunicado por Marco, 06/10/2026:** Marco aclara que el código original fue hecho manualmente y que el profesor autorizó al agente a realizar la refactorización, sin nuevas ediciones manuales del estudiante. Esta dirección posterior elimina el bloqueo operativo de autoría manual que se aplicaba a A05–A07 y sus conexiones/correcciones acordadas sobre el código existente. Codex puede implementarlas y comprobarlas autónomamente dentro del alcance vigente; registrar cada cambio como refactorización conservadora o corrección funcional, con autoría real del agente. La aprobación de reglas de negocio aún abiertas y los requisitos de evidencia/entrega no se inventan. Los apartados anteriores que exigían aporte manual para continuar la refactorización quedan como antecedentes superados; las copias originales de P1 no se reescriben.


**DEC-A05-SUP-001 — 06/10/2026:** Marco pide resolver el estancamiento. Codex prepara la notificación opcional de persistencia en la API existente sin conectar consumidores ni atribuir autoría manual. Clasificación operativa de soporte de Codex, no aprobación del profesor: P1 §2.7 y la conexión completa AppRoot/Writing permanecen pendientes. No cambia alcance, modo, reglas A06/A07 ni habilita publicación/despliegue. [Detalle](CORE_IMPLEMENTATION.md).

**AND-001 — ejecución Android y comprobación ampliada, 05/10/2026:** SDK oficial API 36/Build Tools 36.0.0 instalado tras aceptación humana «claro». Android compila con Gradle/AGP/versiones recibidas intactas, APK debug firmado generado. 16 métodos pertinentes del módulo real + 1 ejemplo PASS; incluyen dos casos de snapshots Compose, no render/recomposición de pantallas. Cuatro escenarios del motor Ranked en Firestore local reales antes/después dan resultados idénticos; R8 doble y CloudEvent sintético. Auth local REST operativo, sin Google OAuth. Emulador Android sin controlador de aceleración, intento por software permanece offline: cero pantallas ejecutadas. Auditor 158 contrastes estáticos, expectativas anteriores conservadas. Sin cambio de producción, reglas, integración, retiradas o temporadas. [Resultados, reproducción y bloqueo](ANDROID_EXECUTION.md).


**Alcance posterior SCP-001 — registro 05/10/2026:** Marco comunica una nueva captura/mensaje de Renzo: «Ranked y Merit permanecen»; «Torneos, ligas y reputación se retiran». La imagen original y su fecha no están adjuntas al contexto disponible; no se infiere aprobación grupal ni fecha de subida. Esta instrucción humana posterior sustituye la retirada total de Merit de U3/DEC-03 para el trabajo vigente. Se conservan Ranked/rating/Merit y las reglas económicas existentes; no se inventan usos, precios o recompensas nuevos. Temporadas permanece incluida con reglas pendientes. RET-001–005 y sus evidencias describen el alcance anterior y no se reescriben. [Recuperación y restitución](RECOVERED_ENVIRONMENT.md).

**DEC-SCP-001:** decisión de alcance comunicada por Marco con procedencia declarada en Renzo. No hay imagen binaria/fecha original/aprobación grupal nueva comprobadas. Codex recupera/compara/importa fuentes y restaura presentación existente. No se atribuye intervención manual, algoritmo, BD o integración al estudiante. Borrador hasta persistencia confirmada sigue aprobado y pendiente de conexión P1; espera, Ranked sin ligas, temporadas y compatibilidad siguen independientes.


**Recuperación local IMPL-006 — 04/10/2026:** 20 recursos Android originales restaurados con blobs verificados y 72/72 archivos compartidos idénticos. [Registro](IMPLEMENTATION_LOG.md). Builds/configuración Firebase/R8 y aportes estudiantiles A05–A07 pendientes; no producto integral o aceptación acreditados.

**Continuación local IMPL-005 — 04/10/2026:** caracterización previa acotada de A05–A07. [Registro](IMPLEMENTATION_LOG.md), [dependencias](CONSERVATIVE_BLOCK_PLAN.md). Los cambios centrales A05–A07 mantienen la consulta estudiantil pendiente; no se declara aceptación integral ni autoría humana de los aportes Codex.

**Bloque local A04 — 04/10/2026:** [IMPL-004](IMPLEMENTATION_LOG.md), separación de puntuación y feedback de Results; sintaxis y equivalencia de bloques fuente comprobadas; sin ejecución Compose. [Plan pendiente](CONSERVATIVE_BLOCK_PLAN.md). IMPL-001/002 preservados; revisión/intervención estudiantil y entorno integral pendientes, sin publicación ni cierre de refactorización.

**Bloque local A03 — 04/10/2026:** [IMPL-003](IMPLEMENTATION_LOG.md), separación de componentes informativos de Writing; 20 PASS antes/después en fragmentos y conservación de fuentes. [Plan pendiente](CONSERVATIVE_BLOCK_PLAN.md). IMPL-001/002 preservados; revisión/intervención estudiantil y entorno integral pendientes, sin publicación ni cierre de refactorización.

**Continuación humana local — 04/10/2026:** Marco indicó «continua con el siguiente bloque» en chat 13. Habilita la continuación A02 propuesta bajo el alcance local de U19/DEC-26. [IMPL-002](IMPLEMENTATION_LOG.md) registra propuesta/cambio/pruebas IA: 20 PASS antes/después sobre fragmentos, reutilización de dos Regex. No constituye elección de ALT-01, reglas Q, arquitectura o revisión/autoria estudiantil. Evidencia original del mensaje en la carpeta nueva IMPL-002.

**Actualización local — 04/10/2026:** U19/DEC-26 del registro de coordinación autoriza el primer bloque conservador y pruebas pertinentes. [IMPL-001](IMPLEMENTATION_LOG.md) registra la ejecución IA local, sin decisión estudiantil de adopción o permiso de publicar este cambio. Las DEC-01–12 que siguen conservan su procedencia histórica.

**Versión inicial:** 0.1 · 03/10/2026. Los IDs son referencias documentales creadas para ordenar evidencia; no equivalen a actas firmadas. [Fuentes](SOURCE_INDEX.md).

## Decisiones humanas existentes

| ID | Decisión | Quién / procedencia verificable | Fecha conocida y evidencia | Alcance de lo decidido |
|---|---|---|---|---|
| DEC-01 | D1 es la delimitación aprobada por el grupo y fuente principal de alcance | Marco comunicó la aprobación del grupo | Incorporada en este chat 03/10/2026: U2; no se conoce fecha del acto grupal | Aprobación del alcance, no permiso de implementar en esta fase |
| DEC-02 | Torneos, ligas y reputación fuera del producto objetivo | Marco en U2; conversación con PO aportada en U3 | U2/U3 compartidos 03/10/2026 | Retirada funcional; resuelve alternativas contradictorias de D1 |
| DEC-03 | Merit fuera: moneda, ganancia y todos sus usos, incluido pago para Ranked | PO en la captura aportada por Marco | U3 compartida 03/10/2026; no se fija fecha del mensaje sólo con la hora de la imagen | La respuesta «volarlos» incluye Merit. Resuelve la duda de la transcripción anterior; no define sustituto |
| DEC-04 | Incorporar temporadas por estar en HU y faltar en base | Marco en U2; PO en U3 | U2/U3; I1, HU 3.27/3.28 | Inclusión aprobada; reglas de funcionamiento siguen abiertas |
| DEC-05 | Practice, Ranked, escritura/evaluación, autenticación, perfil básico, persistencia/estados y rating permanecen dentro del núcleo | D1, aprobado según DEC-01 | D1/U2; U3 no elimina Ranked ni rating | Preservación dentro del alcance; separar retiradas que afecten el flujo |
| DEC-06 | Marco y Renzo coordinan directamente la propuesta y ejecución con el equipo | Documento D2 | D2 actualizado 02/10/2026 | No asigna por sí mismo cada método, test o autoridad final de aceptación |
| DEC-07 | Baseline mínimo aislado JVM, compilando fuentes reales sin cambiar producción | Autorización humana registrada en A1; contrastada con C1/B1 | Historia y fuentes de la auditoría | Permiso histórico de esa tarea específica; no autorización general de código |
| DEC-08 | Crear ahora AGENTS inicial y docs de trabajo, sin comenzar refactorización | Usuario Marco en este chat | U4, 03/10/2026: «ok iniciemos el siguiente paso que me acabas de recomendar.» | Habilita la base documental recomendada. Sustituye la prohibición temporal anterior de escribir esos documentos |
| DEC-09 | Priorizar problemas concretos de responsabilidad/acoplamiento/duplicación y justificar patrones con alternativas e impacto | D2 | D2 | Principio de trabajo; no selección concreta de patrones, arquitectura o framework |
| DEC-10 | Excluir detalle de partida para revisar errores, penalización por abandono, visualización de ranking/posición global y leaderboard de liga, y perfil de jugador desde leaderboard | D1 aprobado según DEC-01 | D1, apartado 3, HU a eliminar | No reabrir estas exclusiones al concretar resultados inmediatos, perfil básico, cierre de sesión o temporadas |
| DEC-11 | Publicar AGENTS y documentación de trabajo de la manera propuesta | Marco, instrucción U5 en este chat | 03/10/2026: enlace al repositorio y solicitud de subirlo correctamente | Commit exclusivamente documental en rama y PR hacia master; sin originales completos, captura privada ni cambios de producción |

La documentación registra estas decisiones; no las crea la IA. Sus fuentes prevalecen sobre recomendaciones previas de asistencia. Las reglas académicas del profesor gobiernan qué autoría y evidencia pueden acreditarse.

## Aspectos que siguen abiertos

| ID | Qué falta | Por qué condiciona trabajo posterior | Fuente / cómo resolver |
|---|---|---|---|
| OPEN-01 | Criterios originales completos de HU y backlog Anexo A | Los títulos del informe no reemplazan aceptación detallada | I1 y fuente original del backlog; no se encontró copia independiente en la auditoría |
| OPEN-02 | Reglas de temporadas | Inclusión no define período, ranking, cierre, historial o pendientes | AC-04–AC-06 en REFACTOR_SCOPE; decisión del equipo/PO |
| OPEN-03 | Ranked tras retirada de ligas, reputación y Merit | Falta regla de ejercicio y controles/estado de sesión no económicos; penalización de abandono ya está excluida por D1 | AC-01–AC-03; caracterización C1 y regla humana sin reabrir DEC-10 |
| OPEN-04 | Compatibilidad de datos/borradores y tratamiento histórico | Retirada funcional no autoriza borrado físico | AC-07; inventario antes de proponer cambios |
| OPEN-05 | Clases concretas y problemas de diseño a intervenir | No se hizo diagnóstico exhaustivo ni selección final | Análisis humano y evidencia del mapa; sin imponer patrones |
| OPEN-06 | Alternativas/patrones/arquitectura final | Requieren justificación y autoría humana según P1/D2 | Etapa de diseño posterior autorizada |
| OPEN-07 | Responsables y procedimiento final de aprobación | La tabla A/B/C de D2 es una **propuesta** | Ratificación por el equipo; no hacerla obligatoria por inferencia |
| OPEN-08 | Configuración original y fuente auténtica de R8 | Falta build/integración reproducible | Recuperar fuentes no secretas indicadas en STATUS |
| OPEN-09 | Correspondencia de releases/sprints heredados con entrega ISW2 | No inventar cronología académica | D2 y aclaración del profesor |
| OPEN-10 | Pruebas por integrante y evidencia IA individual | Baseline no acredita participación ni todos los umbrales | P1/P3, asignaciones y ejecuciones reales |

**Resuelto:** la permanencia de Merit dejó de estar abierta tras U3. No reabrir DEC-02/03/04 salvo que llegue una instrucción humana que cambie el alcance.

## Propuestas documentales de esta versión

La estructura de docs, sus IDs y el protocolo inicial de evidencia son organización propuesta por IA para la tarea autorizada. No se registran como decisiones grupales sobre arquitectura, diseño o producto. Toda selección técnica pendiente seguirá identificada como tal.

## Cómo registrar una nueva decisión

Esta ficha es una plantilla; no contiene una decisión tomada:

- ID y fecha conocida:
- Pregunta/problema:
- Fuentes y comportamiento actual comprobado:
- Alternativas consideradas por el equipo:
- Decisión humana y su justificación:
- Quién decidió y evidencia de aceptación:
- Clasificación: preservación / retirada / incorporación / fuera de intervención:
- Criterios verificables y pruebas:
- Impacto y decisiones anteriores que reemplaza:
- Aporte de IA y revisión humana:
- Estado: propuesta / confirmada / reemplazada:

No rellenar fechas, responsables, análisis humano o aprobaciones que no estén disponibles. Una nueva petición autorizada se registra con su alcance; la ausencia de un flujo A/B/C ratificado no exige por sí sola pedir aprobación para cada acción documental.

**Dirección humana posterior — 05/10/2026:** a la consulta de desbloqueo A05–A07, Marco respondió literalmente «sigue la mejor opcion posible». Autoriza continuar el apoyo/recomendación y las acciones rutinarias permitidas. No se registra como justificación arquitectónica escrita por el estudiante, autoría manual de código o ratificación de todas las propuestas. [AUD-001](LOCAL_AUDIT_REVIEW.md) identifica lo realizado y el aporte auténtico pendiente; no se solicitan aprobaciones por función/archivo.

**Dirección humana posterior — 05/10/2026, corrección de P1:** Marco autoriza comprobar e implementar la extracción mecánica canSubmit conservando lecturas/contratos y continuar partes mecánicas A06/A07. Rechaza copiar código como supuesto crédito académico. [Registro](MECHANICAL_CORE_REFACTOR.md) y mensaje literal en evidencia IMPL-007-009/HUMAN_INPUT.txt. Codex realiza extracciones/pruebas; esto no ratifica diseño mayor, reglas o aportes estudiantiles inexistentes. Sustituye la interpretación general de aporte manual para cualquier extracción, sin cambiar las reservas exactas de P1.

**05/10/2026 — decisión funcional de borrador recibida:** Marco: «debe conservarse texto y borrador hasta un guardado confirmado». Resuelve el tratamiento frente a persistencia no confirmada/error, no la asociación de cuenta/ejercicio. La respuesta sobre espera «como esta planteado actualmente, guiate por el mejor camino» autoriza orientación/continuidad, sin inventar una regla de identidad/duración/notificación. Ranked sin respuesta específica. [VER-001](EXECUTION_READINESS.md) distingue expected aprobado, aporte estudiantil de conexión pendiente y cambios Codex sólo de verificación. No se atribuye implementación o ratificación de propuestas IA al equipo.

**Vigente AND-002 — 06/10/2026:** WHPX/AVD operativos y pantallas ejecutadas en
demo-inkr8-local. 14 métodos Android únicos: 13 PASS y 1 FAIL real (isPlaced no
llega al modelo); tres contrastes centrales antes/después PASS; cinco escenarios
transaccionales iguales y entrega automática local/callable PASS, R8 HTTP doble.
No código productivo nuevo; soporte/fixtures corregidos. Borrador anticipado,
resolución por B, idempotencia/carrera y payload gamemode siguen como defectos o
contratos pendientes. Ranked/rating/Merit permanecen. [Resultados, APK, límites y
aportes concretos](ANDROID_EXECUTION.md). No cierre integral ni autoría estudiantil.


### Publicación ejecutada FIN-002

Procedencia: prompt de Marco de09/10/2026 autoriza rama/PR, sin merge/deploy.
Ejecutado por Codex: commits de Codex <codex@local.invalid>, push normal a rama
codex/refactorizacion-por-bloques, PR2 abierto en borrador hacia master con cuenta
autenticada MACOABC. Esto no acredita revisión/aceptación del equipo ni una
aprobación nueva de reglas Firestore/cierre de cuenta. No se modificó master.


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


### INT-001 — fusión de la copia académica

Procedencia: prompt actual de Marco,09/10/2026 America/Lima: «Autorizo fusionar
el PR #2 de Rnz5/Inkr8-ISW2 hacia master». Autoriza merge commit, mantener rama y
registrar integración; revisión del equipo posterior. No autorización de deploy
Firebase, cambios de datos reales, reglas nuevas o cumplimiento individual.
Codex marca ready y fusiona PR2 en6165415, con ambos padres7d879c2/180837d;
13 commits originales preservados, rama disponible. No se atribuye revisión humana.
Organización acotada: sin patrones/interfaces/formateo masivo nuevos para esta
fusión; no hay necesidad demostrada que justifique modificar código ya probado.
