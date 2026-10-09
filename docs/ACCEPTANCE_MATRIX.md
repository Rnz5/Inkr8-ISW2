# Matriz de aceptación operativa de Inkr8

**Vigente FIN-002 — 09/10/2026:** cierre y entrega revisable documentados en
[FINAL_DELIVERY.md](FINAL_DELIVERY.md), con 6 Android PASS, reglas nuevas
propuestas y límites externos/académicos explícitos. Los estados inferiores son
antecedentes preservados; no revocan DEC-AI-AUTH-001, las decisiones respondidas
ni el permiso actual de publicar rama/PR, sin fusión o despliegue.

**Actualización RET-006/SEA-001:** decisiones Ranked/temporadas respondidas por
Marco y ejecutadas localmente; criterios históricos conservados como antecedentes.
Aceptación de retiradas y nuevos contratos comprobados en [FUNCTIONAL_CLOSURE](FUNCTIONAL_CLOSURE.md);
37 métodos Android únicos, 10 SDK de retirada y 21 SDK temporadas PASS, límites
de dobles/seguridad/ejecución externa explícitos. No aceptación integral ni autoría
estudiantil inferida.

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
resultados y reproducción](CORE_CLOSURE_AUDIT.md), [diff consolidado](evidence/CORE-CLOSE-001/CORE-current.patch).

Los registros anteriores que indican modo sin resolver o aporte manual requerido
son antecedentes de sus versiones, superados por DEC-MODE-001/DEC-AI-AUTH-001.

**Alcance posterior SCP-001 — registro 05/10/2026:** Marco comunica una nueva captura/mensaje de Renzo: «Ranked y Merit permanecen»; «Torneos, ligas y reputación se retiran». La imagen original y su fecha no están adjuntas al contexto disponible; no se infiere aprobación grupal ni fecha de subida. Esta instrucción humana posterior sustituye la retirada total de Merit de U3/DEC-03 para el trabajo vigente. Se conservan Ranked/rating/Merit y las reglas económicas existentes; no se inventan usos, precios o recompensas nuevos. Temporadas permanece incluida con reglas pendientes. RET-001–005 y sus evidencias describen el alcance anterior y no se reescriben. [Recuperación y restitución](RECOVERED_ENVIRONMENT.md).

**Impacto de alcance:** las exclusiones de Merit basadas únicamente en U3 quedan superadas; sus criterios H1 no amarillos vuelven a ser fuente pertinente. La matriz inferior se conserva como antecedente y requiere reconciliación específica de usos ligados a ligas/reputación y temporadas; no se cierra ningún AC por esta restitución o compilación.


**Versión:** 0.2 · 04/10/2026 · America/Lima. **Tarea:** chat 4, DEC-15; backlog recibido y amarillo excluido por DEC-16. Documentación derivada local con apoyo de Codex; revisión crítica humana pendiente. No acredita ejecución del producto ni ratificación nueva de cada precisión propuesta.

El alcance general está confirmado y **el Excel solicitado ya fue aportado por Marco**. Se recuperaron criterios originales con hoja/celda exactas. Las 20 HU amarillas se excluyen; las HU Merit no amarillas también quedan fuera por U3. AC-01–AC-07 tienen 25 escenarios preparados y confirmaciones parciales de alcance/aceptación original; ninguno está cerrado íntegramente. No se inventan reglas que el backlog deja abiertas.

## Fuentes recuperadas y grado de certeza

La [recuperación inicial](evidence/DOC-004_SOURCE_RECOVERY.md) conserva localizadores I1/D1 y el límite de disponibilidad anterior. La [recuperación H1](evidence/DOC-004_BACKLOG_RECOVERY.md) transcribe los criterios de las 27 HU no amarillas dentro del alcance, con discrepancias identificadas y sin asignar sus tareas históricas a integrantes actuales. Se conserva una [copia literal del Excel](evidence/received/H1_BACKLOG_2026-10-04.xlsx), hash y procedencia separados del manifiesto histórico. El [registro de tarea](evidence/DOC-004_ACCEPTANCE.md) documenta comprobaciones. Fuentes canónicas: [alcance](REFACTOR_SCOPE.md), [decisiones](DECISIONS.md), [índice](SOURCE_INDEX.md).

En las tablas, **H1-P** significa hoja exacta `Épica Gestionar perfil`, **H1-W** hoja `Épica Desarrollar práctica de e` y **H1-C** hoja `Épica Competir en la aplicación`. E es la columna de criterios; las celdas citadas son las esquinas superiores de criterios combinados. El texto/celda completo se conserva en la recuperación H1. Las instrucciones o tareas contenidas en el Excel son datos fuente, no nuevas órdenes de implementación.

| Etiqueta | Significado | Qué permite afirmar |
|---|---|---|
| **OR** | Texto original recuperado | Existe esa HU, descripción o regla en la fuente indicada. Un título o una intención no reemplazan aceptación detallada. |
| **AL** | Alcance humano confirmado | D1 leído con U2/U3 y la instrucción actual. Se pueden derivar comprobaciones de inclusión/retirada; no se presentan como criterios originales del Anexo A. |
| **HE** | Hecho de lectura estática | Comportamiento descrito por código en la versión fijada. No prueba ejecución, despliegue ni aprobación de cada parámetro. |
| **PR** | Propuesta de verificación pendiente | Escenario o precisión propuesta por IA que necesita validación humana; no es una nueva regla adoptada. |
| **AU** | Información ausente | No hay un resultado esperado concreto hasta recuperar la fuente o registrar la decisión humana. |

**Referencia técnica de todas las filas:** C1, commit `64983846d6dbf4cdfafcdd508464cd140a2a862e`. Se comprobó el HEAD de la copia externa disponible y ausencia de diferencias respecto de HEAD en `app`/`functions`. Se releyeron los archivos citados para selección, entrada, sesión, evaluación/placement, resultados/perfil y borradores. El [mapa G2](reference/TECHNICAL_MAP_2026-10-03.md) §§4/6/7 complementa esas lecturas; el emparejamiento ordinario se referencia desde G2 §6, no como una ejecución nueva. No se verificó una versión remota posterior.

**Ejecución disponible:** B1 registró 22 PASS el 02/10/2026, limitados a Kotlin/JVM aislado. No se ejecutó ningún escenario de esta matriz. Ni B1 ni la lectura estática validan Android, Functions, Firebase, R8 o temporadas. [Límites del baseline](TESTING_BASELINE.md).

## Matriz principal AC-01–AC-07

Los identificadores de escenario remiten al catálogo de la sección siguiente. La columna de criterio distingue origen, aprobación y ausencia; “confirmado” nunca significa “test aprobado”.

| Función/HU y fuente exacta | Decisión de alcance vigente | Tipo de intervención | Comportamiento actual comprobado y versión | Criterio original, propuesta o ausencia | Escenario verificable y resultado esperado | Evidencia, faltantes y decisión humana necesaria |
|---|---|---|---|---|---|---|
| **AC-01. Ejercicio Ranked.** HU 3.13, H1-C E53/E55; 3.14–3.16, E57/E59/E61/E63/E65/E67; 2.1/2.2, H1-W E2/E4/E6/E8. I1 §§3.2.2/4.4.2. | Ranked/escritura se conservan; ligas retiradas (DEC-02/05/16). Selección sin liga. | **Preservación** del ejercicio/editor; **retirada** de selección por ligas; diferencia entre selector implementado y HU a resolver como cambio deliberado. | **HE/C1:** Competitions:85, SCRIBE/STYLIST ⇒ Standard; otras ligas ⇒ aleatorio Standard/On-Topic; sin tema/topic ⇒ Standard. Gamemodes: Standard 4 palabras/50–150; On-Topic 2/50–200; timeLimit nulo. G2 F03/C01/C02. | **OR confirmado:** el usuario selecciona Ranked On-Topic; inicia con sus reglas, muestra tópico/límite/restricciones y cumplimiento; errores específicos H1. **AL:** sin ligas. **AU:** oferta/selección Standard, fallback/contexto y restricciones exactas. HU 2.1 dice “cuatro restricciones” sin enumerarlas; 2.2 habla de caracteres sin mínimo numérico. | S01 sin liga; S02 selección On-Topic y fallo conforme a E53/E55; S03 restricciones/contexto y validación conforme a H1, con parámetros incompletos. | H1 recuperado; C1/G2 estáticos. Faltan R8/contrato y precisión de Standard/fallback/restricciones. **Q-R:** resolver divergencia selección explícita frente a aleatoriedad por liga; no volver a preguntar si existe HU On-Topic. |
| **AC-02. Entrada, cierre y sesión.** HU 3.7, H1-C E27/E29; 1.2–1.4, H1-P E6/E8/E10/E12/E14/E16. HU 1.1 D2/E2/E4 inconsistente. No HU de limpieza localizada. | Sin Merit/reputación/penalización de abandono (DEC-02/03/10/16); conservar autenticación/estado. HU 3.7 no amarilla no restablece su cobro. | **Preservación** de acceso/estado; **retirada** de economía y penalizaciones. | **HE/C1:** applyMeritAction:39/164 autentica; cuenta todos los envíos Ranked desde 00:00 UTC, rechaza ≥5; si falla conteo conserva 0. Sesión previa altera reputación/rachas. ABANDON limpia estado y altera rachas/reputación. Cleaner cada 15 min, inicio hace ≥60 min. Evaluación/error limpian estado. | **OR:** acceso/sesión competitiva en 3.7, con cláusulas económicas **superadas**. 1.3/1.4 definen restaurar/cerrar autenticación, distintos de sesión Ranked. **AL:** acceso sin cobro/castigo. **AU:** controles/límite/UTC/rachas/reentrada/cierre. 1.1 tiene criterios de oración de ejemplo copiados; no acredita registro. | S04 acceso autenticado; S05 sin economía; S06 límite/reentrada/error; S07 cierre por causa sin castigo. Resultado no económico exacto pendiente. | H1-C/P y C1/G2. **Q-R:** controles/cierre; **Q-C:** fuente correcta de registro Google. No reabrir penalización ni usar los criterios erróneos de 1.1 como si fueran autenticación. |
| **AC-03. Resultados inmediatos, rating/placement y perfil propio.** HU 2.3/2.4, H1-W E10/E12/E14/E16; 2.5 E18/E20; 3.8–3.12, H1-C E31/E33/E36/E38/E40/E42/E44/E46/E48/E50; 1.9/1.10 H1-P E37/E39/E42/E44. Placement sin HU propia. | Conservar score/feedback/Ranked/rating/perfil básico; retirar economía/ligas/reputación y consultas excluidas. No excluir feedback inmediato por ser detallado: se retira HU 1.8, no 2.4/3.16. | **Preservación** de resultados/estado/rating; **retirada** de contenido/acciones excluidos. Brechas requisito/código necesitan análisis posterior, no corrección ahora. | **HE/C1:** placement 6 evaluaciones, rating `min(120, floor((promedio/100)*120))`; AppViewModel consulta última submission cada 3 s, timeout >90 s; EVALUATED ⇒ Results/reveal, FAILED ⇒ Home; EVALUATED ≠ MATCHED. Results/reveal/Profile incluyen retiradas. Ghost resuelve pendientes ≥48 h con parámetros propios. | **OR confirmado:** score 0–100; puntaje/cumplimiento/feedback y fallback de error; timeout con mensaje y registro de reintento; match sin rival 48 h contra promedio propio; resultado victoria/derrota/empate y variación de rating. Perfil: partidas/promedio/mejor puntaje/% victorias y actualización. Practice no entra en historial competitivo. **AU:** fórmulas remitidas a “reglas establecidas”, placement, población de métricas y recuperación/notificación exacta. | S08 criterios de evaluación; S09 rating/placement/match; S10 timeout/fallo/identidad; S11 perfil con métricas originales y exclusiones. La fórmula de placement C1 es caracterización, no criterio original confirmado. | H1 recuperado, G2 C03/C04 y lectura C1. **Q-R:** población de estadísticas generales/oficiales, presentación placement y recuperación pendiente; fuente R8/formulas exactas aún ausentes. No tratar mensajes originales como ya implementados sin ejecución. |
| **AC-04. Períodos y participación.** HU 3.27/3.28, H1-C D109/E109/E111/D113/E113/E115; I1 §4.5.2/D1 §3. | Temporadas incluidas; sin requisitos de torneos/ligas/economía. | **Incorporación funcional**; sin arquitectura/BD. | **HE/C1:** búsqueda season/temporada sin coincidencias en Kotlin de producción/functions/src. G2 §8/F12: estadísticas agregadas no definen temporadas. No ejecución. | **OR:** temporada activa y anteriores finalizadas registradas como precondiciones. **AU:** inicio/fin/duración/zona/límites, usuarios, envíos y evento de pertenencia; los criterios recuperados no los precisan. | S12 límites/evento/zona; S13 participación/admisión. Resultado concreto pendiente de Q-T/Q-D. | H1-C original disponible; no implementación identificable. **Q-T:** calendario y pertenencia/admisión; **Q-D:** previos. No inferir mes/UTC ni trasladar calendario de estadísticas. |
| **AC-05. Ranking de temporada.** HU 3.27, H1-C E109/E111; rating general HU 3.9/3.10, E36/E40. | Ranking/posición estacional incluidos; global/ligas excluidos; rating general permanece. | **Incorporación funcional**, distinta del ranking retirado. | **HE/C1/G2:** ranking de temporada no identificado. Rating/placement y match ordinario/ghost son mecanismos existentes separados; no definen rating estacional. | **OR confirmado:** clasificación ordenada por **rating de temporada**, posición propia destacada y error “No se pudo cargar el ranking de la temporada”. **AU:** cálculo/agregación/dirección/elegibilidad/empates/extensión y relación con rating/placement. No se pregunta de nuevo si la magnitud es score u otra: la fuente dice rating de temporada. | S14 ordenar por rating estacional/resaltar posición; S15 elegibilidad/vacío; S16 transición; S24 error de consulta original. Orden de empates y valores de rating necesitan Q-T. | H1-C criterios específicos recuperados. **Q-T:** definición del rating de temporada y relación con general/placement; dirección/empates/elegibilidad. No reset/fórmula/desempate nuevo elegido. |
| **AC-06. Cierre/historial/tardíos.** HU 3.28, H1-C E113/E115; 3.27 E109; D1 §3. | Historial incluido; detalle de partida para errores y perfil ajeno desde leaderboard excluidos. Merit no se restablece por la palabra “recompensas”. | **Incorporación funcional** con precisión pendiente frente a economía retirada. | **HE/C1:** evaluación/match distintos; ghost puede resolver ≥48 h; espera no resuelve tras timeout. Cierre/historial estacional no identificado. G2 C03/C04. | **OR confirmado:** elegir temporada anterior y mostrar **posición final y rating de cierre**; fallo “No se pudo cargar el historial de temporadas”. E113 también dice “recompensas obtenidas”: **parcialmente pendiente/superado en su posible parte Merit**. **AU:** cierre/tardíos/vacíos/estabilidad histórica. No se inventa recompensa alternativa. | S17 consulta de campos originales; S18 cierre/pendientes; S19 tardíos/repetición; S25 error histórico original. Tratamiento de recompensa y eventos temporales pendiente. | H1-C disponible, C1/G2 estáticos. **Q-T:** cierre/tardíos y definición de dato de recompensa sin Merit (consulta nueva específica); **Q-D:** históricos. No asumir congelación/gracia/arrastre. |
| **AC-07. Documentos/borradores previos.** D1 §3; HU 1.7/1.11, H1-P E27/E29/E47/E49. Sin HU de migración/borrador localizada. | Persistencia del núcleo conservada; retirada no autoriza borrar datos ni modificar servicios. | **Preservación**/compatibilidad; **retirada lógica**; incorporación estacional sólo con política humana. Sin migración diseñada. | **HE/C1:** Users mezcla campos conservados/retirados/defaults. DraftManager:23, clave `draft_{gamemode}_{playmode}_{tournamentId o none}`; texto sin usuario/tema/topic/temporada. Writing carga texto/regenera contexto. G2 C04/C05/C06. | **OR confirmado:** historial cronológico con puntaje/modo/fecha/resultado, recuperación de historial/estadísticas al volver a iniciar sesión y mensajes de error E29/E49. **AL:** no borrado por retirada. **AU:** campos faltantes/retirados, asignación estacional y borradores. | S20 lectura antigua; S21 sin temporada; S22 borrador del núcleo; S23 de torneo. Recuperar historial entre sesiones según H1; sin reactivar detalle/torneos. Compatibilidad específica depende de Q-D. | H1-P disponible; modelos/claves estáticos. Faltan inventario anonimizado/productores/lectores y política **Q-D**. No deducir migración de historial conservado ni copiar tareas históricas como aportes actuales. |

## Catálogo de escenarios preparados

Todos están **NO EJECUTADOS**. “AL” indica resultado derivado del alcance ya aprobado, no un criterio literal recuperado del Anexo A. “PR/AU” indica que el caso se puede preparar, pero falta validar el escenario o completar su resultado esperado. Datos de ejemplo se crearían en una etapa de pruebas autorizada; no son documentos de producción inventados.

| ID / AC | Dado / cuando | Resultado esperado y estado | Evidencia futura necesaria / dependencia |
|---|---|---|---|
| S01 / 01 | Dadas cuentas equivalentes salvo datos heredados de liga, al entrar a Ranked. | **AL:** selección no consulta ni exige ligas. La comparación del modo debe controlar cualquier aleatoriedad según regla humana. | Recorrido y resultado observados; Q-R define selector. |
| S02 / 01 | Seleccionar Ranked On-Topic con contexto válido; repetir fallo de carga/tema o topic ausente. | **OR:** inicia según reglas On-Topic; ante fallo “No se pudo iniciar la partida On-Topic. Inténtalo nuevamente” (H1-C E53/E55). **AU:** oferta Standard y reacción específica sin contexto; no adoptar fallback C1 por inferencia. | Original recuperado; Q-R para vacíos, contrato del ejercicio. |
| S03 / 01 | Cargar restricciones; redactar/enviar texto vacío o insuficiente; recuperar cumplimiento de On-Topic. | **OR:** mostrar tópico/límite/restricciones, bloquear envío no válido con mensaje H1-W E8, validar y mostrar cumplidas/incumplidas (H1-C E57/E61/E65); errores E59/E63/E67. **AU:** restricciones exhaustivas y mínimo en caracteres frente a palabras. **HE:** límites C1 sólo caracterizados. | H1-W/C; Q-R, R8/contrato C01/C02. No fijar números faltantes ni corregir texto residual E67. |
| S04 / 02 | Sin usuario autenticado, intentar entrar o enviar como ese usuario. | **AL:** autenticación sigue siendo requisito del núcleo. **PR:** rechazo observable sin crear sesión/envío atribuible; mensaje concreto no recuperado. | Ejecución futura de acceso/identidad; fuente de aceptación. |
| S05 / 02 | Usuario autenticado que cumple controles acordados, con Merit heredado cero o ausente, entra/juega. | **AL:** no bloqueo, cobro, ganancia, retención, consulta de reputación ni operación de Merit; ninguna moneda sustituta. El resto del acceso depende de controles acordados. | Estado anterior/posterior, resultados y operaciones; Q-R para controles. |
| S06 / 02 | Próximo al límite diario y cambio de día; consulta de conteo falla; dos intentos o sesión previa activa. | **PR/AU:** admitir/rechazar/reintentar y qué cuenta conforme a Q-R. Los 5 envíos/día UTC y el catch del código son hechos históricos, no reglas confirmadas aquí. | Ejecución de límites/error/concurrencia; decisión de condición no económica. |
| S07 / 02 | Tras éxito, error de envío/evaluación, abandono voluntario o sesión caducada. | **AL:** ninguna penalización por abandono/reputación/economía. **PR/AU:** liberar, conservar o reanudar sesión/borrador según causa, momento y regla Q-R/Q-D; no añadir una derrota o reducción de rating por abandonar. | Estado de sesión y rachas antes/después, intento posterior; Q-R/Q-D. |
| S08 / 03 | Envío recibe evaluación válida; repetir error de estructura de feedback. | **OR:** score 0–100 (H1-W E10); resultado con puntaje/restricciones/comentarios de ortografía, vocabulario y coherencia (E14). Error de estructura ⇒ puntaje base y “No se pudo cargar el feedback detallado en este momento” (E16). **AL:** sin retiradas ni detalle posterior HU 1.8. **PR:** vincular al envío correcto. | H1-W; ID/evaluación/pantalla y R8 auténtico. No atribuir ejecución de mensajes/cálculo a C1. |
| S09 / 03 | Completar placement; obtener match ordinario o no encontrar rival durante 48 h; resultado competitivo y variación. | **OR:** match asincrónico y después de 48 h comparación con promedio propio (H1-C E31/E33); actualizar rating según reglas establecidas y mostrar victoria/derrota/empate/variación y errores (E40–E50). **PR/HE:** placement 6 scores de 50 ⇒ 60 según C1, cálculo estático no ejecutado. **AU:** promedio sin historial/fórmulas/placement no descritos en H1. | H1-C, fuente de reglas y caracterización real; sin ligas/reputación. No adoptar benchmark 65 de C1 como criterio original. |
| S10 / 03 | Envío A, llega B o actualización tardía; evaluación/match pendientes, FAILED o timeout. | **OR:** ante timeout de evaluación, mensaje “La evaluación está tomando más de lo esperado. Te notificaremos cuando tu resultado esté listo” y registro para reintento posterior (H1-W E12). **AU:** duración/notificación/reintento y match pendiente exactos. **PR:** identidad del resultado; EVALUATED ≠ MATCHED. | H1-W, caracterización C03/C04 y Q-R; sin ejecución ni fallo reproducido. |
| S11 / 03 | Perfil propio con actividad y tras evaluación; repetir fallo de estadísticas/rating. Intentar rutas excluidas. | **OR:** partidas jugadas/promedio/mejor puntaje/% victorias y actualización (H1-P E37/E42), rating (H1-C E36), errores E39/E44/E38. **AL:** sin funciones retiradas. **AU:** población/definición de métricas y límites básicos/acciones; Practice sin historial competitivo (H1-W E18). | H1-P/C/W; Q-R sobre estadísticas generales/oficiales/Practice y placement. No adoptar toda pantalla o compras históricas. |
| S12 / 04 | Evento justo antes, en y después de inicio/fin, representado en zona acordada y otra zona. | **AU:** pertenencia inequívoca y período mostrado conforme a Q-T. Falta evento de referencia y convención de límites. | Calendario humano con ejemplos y valores esperados; sin fijar duración. |
| S13 / 04 | Usuarios con/sin placement, nuevos/previos; envíos Practice/Ranked, Standard/On-Topic, pendientes/fallidos. | **AU:** participación y admisión según Q-T. Torneos/economía/ligas no se reintroducen como condiciones. | Tabla humana usuario × modo × estado; no presuponer que Practice puntúa. |
| S14 / 05 | Temporada activa; participantes con rating de temporada distinto/igual/incompleto; cambiar orden de llegada. | **OR:** clasificación ordenada por rating de temporada y posición propia resaltada (H1-C E109). **AU:** cálculo, dirección, empates y datos incompletos. Ningún desempate/suma/promedio/límite inferido. | H1-C; Q-T completa definición y ejemplos de ranking. |
| S15 / 05 | Usuario sin placement o sin envíos elegibles consulta ranking/posición; ranking vacío. | **AU:** inclusión/exclusión y representación de ausencia según Q-T. No atribuir posición cero ni rating estacional por defecto. | Regla de elegibilidad y estados vacíos; presentación a precisar. |
| S16 / 05 | Cambia el período con rating y progreso de placement existentes. | **AL:** rating no se elimina por retirar ligas. **AU:** relación con nueva temporada según Q-T. No se aplica reset, recalibración ni transferencia por inferencia. | Decisión humana y comparación entre períodos. |
| S17 / 06 | Existen temporadas finalizadas; seleccionar una; repetir sin participación. | **OR:** mostrar posición final y rating de cierre propios (H1-C E113). “Recompensas obtenidas” requiere ajuste explícito sin Merit; ningún reconocimiento nuevo elegido. **AU:** caso vacío/estabilidad histórica; no detalle de partida. | H1-C; Q-T sobre recompensa/cierre/vacíos y Q-D sobre previos. |
| S18 / 06 | Termina el período con envíos sin evaluar, evaluados sin rival y fallidos. | **AU:** momento del cierre y resultado registrado según Q-T; no asumir que espera, congela o descarta. | Estado antes/después y política de pendientes/cierre. |
| S19 / 06 | Envío, evaluación o match llega tras fin; se repite o llega fuera de orden. | **PR/AU:** pertenencia y efecto sobre ranking/historial/rating según Q-T. Preparar comparación tras repetición para detectar duplicación; prohibición o tolerancia funcional debe validarse, no se declara garantía actual. | Identidad/eventos/tiempos y resultado esperado para cada caso; Q-T. |
| S20 / 07 | Leer usuario/envío antiguo; cerrar autenticación y volver a ingresar; repetir fallo de recuperación, campos faltantes o status desconocido. | **OR:** recuperar historial/estadísticas guardados entre sesiones; listado cronológico con puntaje/modo/fecha/resultado y errores H1-P E27/E29/E47/E49. **AL:** sin borrado ni reactivación de retiradas. **AU:** respuesta a campos/status desconocidos según Q-D. | H1-P, inventario anonimizado y lectores C04/C05; sin datos reales editados ni ejecución. |
| S21 / 07 | Documento previo no contiene pertenencia de temporada; se consulta o recibe evaluación tardía. | **AU:** tratarlo según Q-D/Q-T sin inventar historial, backfill o asignación automática al período actual. | Política explícita de históricos y evento de pertenencia. |
| S22 / 07 | Reabrir borrador Practice/Ranked tras actualización, cambio de cuenta/contexto/período o fallo de envío. | **PR/AU:** conservar/reanudar y asociar contexto según Q-D/Q-R. Clave actual guarda texto sin esas identidades; riesgo de asociación, no fallo reproducido. | Inventario de claves/texto/contexto anonimizado; criterio humano y prueba futura. |
| S23 / 07 | Existe borrador TOURNAMENT al abrir producto objetivo. | **AL:** no reactivar torneo ni borrarlo por la retirada. **AU:** disponibilidad/consulta/retención según Q-D. No convertirlo a Ranked/Practice automáticamente. | Política de borradores retirados; sin migración ni limpieza ejecutada. |
| S24 / 05 | Temporada activa, consulta del ranking estacional falla. | **OR:** “No se pudo cargar el ranking de la temporada” (H1-C E111). | Prueba futura de consulta fallida y mensaje; no ejecutada. |
| S25 / 06 | Temporadas finalizadas registradas, recuperación del historial falla. | **OR:** “No se pudo cargar el historial de temporadas” (H1-C E115). | Prueba futura de historial fallido y mensaje; no ejecutada. |

## Consultas humanas necesarias

Las consultas iniciales se agruparon el 04/10/2026. Marco **respondió Q-F con el Excel y aclaró la exclusión de amarillo** (DEC-16). Los criterios recuperados reducen Q-R/Q-T; no se vuelve a pedir fuente, magnitud del ranking o campos históricos ya especificados. Se consultaron después sólo la referencia a recompensas de HU 3.28 y la inconsistencia de HU 1.1. Las demás reglas siguen sin respuesta explícita. La ausencia de respuesta no confirma opciones; no se impone A/B/C de D2.

| Grupo | Información o decisión solicitada | Criterios que dependen de ella |
|---|---|---|
| **Q-F. Fuente — RESUELTA** | H1 aportado por Marco, original y copia literal disponibles. No se acredita fecha/ratificación/versionado histórico del workbook sólo por recepción. | Se recuperan criterios por hoja/celda; no se solicitan de nuevo. |
| **Q-R. Ranked y presentación — parcial** | H1 resuelve selección explícita On-Topic, score/feedback/resultados, timeout/reintento como intención, métricas de perfil y fallback 48 h. Falta oferta Standard/fallback temático, restricciones exhaustivas/mínimo caracteres-palabras, límite diario/UTC/error/reentrada/cierre y rachas no económicas; fórmulas/placement/promedio sin historial, recuperación/notificación y población de métricas generales/oficiales/Practice. | AC-01–AC-03, S01–S11/S22. Usar originales recuperados como punto de partida; preguntar sólo precisiones o cambios deliberados. |
| **Q-T. Temporadas — parcial** | H1 fija rating de temporada/posición resaltada, posición final/rating de cierre y errores de consulta. Faltan períodos/zona/límites/evento, admisión/elegibilidad, cálculo/dirección/empates/extensión del ranking, vínculo rating/placement, cierre/tardíos/vacíos. Resolver referencia a recompensas H1 E113 sin Merit; omitirla o aportar decisión humana previa no económica, sin sustituto inventado. | AC-04–AC-06, S12–S19/S21/S24/S25. No preguntar de nuevo la magnitud o esos campos; no elegir reset/fórmula/gracia. |
| **Q-D. Compatibilidad** | Tratamiento de documentos antiguos/faltantes/campos retirados y datos sin temporada; consulta/reanudación de borradores de núcleo y de torneos; participación de previos. Primero inventario auténtico anonimizado, después política. | AC-07, S20–S23; coordinar con Q-R/Q-T sin borrar ni convertir datos automáticamente. |
| **Q-C. Corrección de fuente** | HU 1.1 H1-P D2 describe Google, pero E2/E4 son oración de ejemplo de 1.6. Recuperar criterios correctos, sin reconstruirlos automáticamente. | Fuente específica de autenticación; AC-02/S04. Autenticación permanece por alcance aunque esos criterios sean defectuosos. |

El backlog recibido se contrastó con D1/U2/U3 y amarillo: los criterios de funciones retiradas no son obligaciones de conservación. La recepción no ratifica tareas técnicas, responsables históricos ni todos sus textos contradictorios. Registrar ajustes deliberados y aprobación humana; cerrar sólo subcriterios efectivamente resueltos.

## Confirmaciones y pendientes para el traspaso

| AC | Confirmado por fuentes/alcance | Sigue pendiente |
|---|---|---|
| 01 | Ranked sin ligas; selección explícita On-Topic, restricciones/cumplimiento y errores originales. | Standard/fallback, lista/parámetros de restricciones y contrato. |
| 02 | Acceso/sesión Ranked sin economía/castigo; criterios de autenticación 1.3/1.4 disponibles. | Límite/UTC/rachas/reentrada/cierre; fuente 1.1 inconsistente. |
| 03 | Score 0–100, feedback/fallback, resultados/variación, 48 h sin rival, timeout/reintento y métricas originales. | Fórmulas/placement/promedio sin historial, identidad/contrato, población de métricas y recuperación/notificación. |
| 04 | Temporada activa y anteriores finalizadas como precondiciones originales. | Calendario/zona/límites/participantes/envíos/evento. |
| 05 | Rating de temporada como magnitud, posición resaltada y mensaje de error originales. | Definición de rating estacional, dirección/elegibilidad/empates/extensión y vínculo rating/placement. |
| 06 | Posición final/rating de cierre y mensaje de fallo histórico originales. | Recompensas sin Merit, cierre/vacíos/estabilidad/tardíos. |
| 07 | Historial cronológico y conservación entre sesiones originales; sin borrado por retirada. | Inventario/política de lectura/consulta/reanudación/pertenencia estacional de previos. |

**Siguiente trabajo:** registrar precisiones de H1 y respuestas restantes en este chat 4. El siguiente chat de la secuencia es **5, “Inkr8 — Mapa técnico y contratos”**: usar esta matriz y G2 para precisar identidad/estados del envío, contexto del ejercicio, relación evaluación/match y lectores/escritores de datos previos. Puede analizar contratos existentes con pendientes visibles; no debe suplir Q-R/Q-T/Q-D con diseño. Diagnóstico SOLID (6), alternativas (7) y arquitectura/UML (8) permanecen posteriores y requieren alcance suficientemente definido y autoría humana. No se creó ni se envió mensaje a otro chat.

La [matriz académica](ACADEMIC_COMPLIANCE_MATRIX.md) TR-02/TR-07/TR-11 y SU-01 exige trazabilidad posterior HU/aceptación → diseño/código/pruebas; esta matriz prepara su primer eslabón, no acredita implementación. P1 §2.7 y PR-01/IA-08 mantienen validación y ejecución estudiantil; no se eligieron pruebas por integrante, se añadieron campos para cumplir umbrales ni se asignaron contribuciones. El [protocolo](EVIDENCE_PROTOCOL.md) sigue como propuesta operativa, con revisión humana y conservación de la interacción original pendientes.

## Vigencia CORE-FINAL-001 — contratos resueltos en alcance local

Marco decidió explícitamente revisión durante persistencia, bloqueo de segundo
envío, UID/contexto del borrador con históricos conservados y reintento sólo de
consulta del ID. Implementación/pruebas Codex; no decisiones pendientes por silencio
ni edición estudiantil exigida. CT-01/03/04/06/14/15 y AC-01/03/07 relacionados tienen
aceptación local de los criterios explícitos; no se redefine CT-02/payload/invalids,
proveedor real, temporadas o retirada completa. [Tabla, contratos exactos y límites](CORE_FINAL_AUDIT.md).
46 Android/19 unit/11 Ranked+2 ghost+1 lifecycle PASS, dobles declarados, sin cierre
integral del producto o rúbrica. Filas históricas se conservan como antecedentes.


### FIN-002 comprobación final y protección de borrado

Suite final: **7 métodos Android PASS**, mismos seis objetivos de aceptación más
callback SDK de deleteSubmission. Android auténtico assembleDebug/testDebugUnitTest
y Functions compilan sobre el código final. Evidencias anteriores intactas.
`deletion-before.json`: la propuesta inicial aceptaba REST DELETE de Ranked sin
acuse estacional (200 frente a403 requerido). Corrección de soporte: callable
`functions/src/submissions/deleteSubmission.ts` comprueba owner/estado y createTime
del servidor, ACK y liquidación en una transacción; repositorio Android conserva
callbacks. Rules deniega el acceso directo Ranked. **16 casos PASS** REST/SDK/HTTP:
pendiente, sin ACK, sin liquidar, ajeno/anónimo, histórico preactivación, repetición,
8 concurrentes, Practice y efectos/asignación históricos intactos. Ninguna nueva
ganancia, coste o backfill. Un404 inicial de callable precedió a su recarga en el
emulador y se conserva como sincronización de soporte, no defecto productivo.
Auditor final: **287 contrastes estáticos PASS**, deltas exactos y oráculos anteriores
conservados;3 mutaciones aisladas detectadas. No son287 pruebas funcionales.
La propuesta deniega el cierre de cuenta anterior; Settings recibe error.
Restituir un cierre seguro exige política/ciclo Auth confiables. **No se acredita
que el equipo haya retirado o excluido esa función del alcance.**


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
