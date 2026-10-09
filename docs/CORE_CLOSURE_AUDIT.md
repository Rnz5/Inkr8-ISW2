# CORE-CLOSE-001 — auditoría técnica acotada A01–A07

06/10/2026, America/Lima. Implementación, pruebas y revisión separada: **Codex**.
Permiso docente comunicado por Marco: DEC-AI-AUTH-001; no nueva edición o revisión
estudiantil atribuida. Decisión explícita de Marco en este chat: **DEC-MODE-001**.
No se acredita acto docente/grupal adicional. Originales, baseline y registros
IMPL-001–009, RET, REC, AND, CORE y auditorías anteriores son antecedentes intactos.

| Bloque | Implementado / responsabilidad | Comprobado | Pendiente / límite |
|---|---|---|---|
| A01 | Regex inmutables del filtro, algoritmo/mensajes originales. No se presenta como solución SOLID central. | 38 JVM antes/después históricos; fuente íntegra reconstruida por auditor. Build Android actual. | No nueva medición de rendimiento ni aceptación del filtro servidor. |
| A02 | Regex de conteo/tokenización reutilizadas; palabras remitidas mantienen orden/duplicados/filtro. | 20 JVM antes/después históricos; observación real de Writing en laboratorio. | No decide enviar restricciones ausentes ni exige todas las palabras: regla/payload aparte. |
| A03 | Cinco componentes informativos fuera del evento de Writing, firmas/paquete conservados. SRP: estilo/información y envío cambian por razones distintas. | Cuerpos completos conservados, consumidores compilados y pantalla Writing STANDARD/ON_TOPIC realmente ejecutada. | No cada diálogo/clic ni compatibilidad de clientes binarios externos; conservar archivo único también era razonable. |
| A04 | Score/feedback de Results separados de efectos/navegación, expresiones conservadas. SRP acotado. | Fuente íntegra y build Compose; score 80.00%, feedback y rating/Merit visibles en recorridos locales. | El doble responde 80; no demuestra evaluación temática real, todos los umbrales o totalidad visual. RET permanece aparte. |
| A05 | Admisión de producción extraída, conteo diferido/nullable/límites inclusivos preservados; callback limpia texto/borrador tras guardado confirmado. | 28 JVM de IMPL-007; 5 sondas A05-CON previas pendiente/éxito/rechazo SDK. STANDARD/ON_TOPIC reales actuales limpian tras éxito. | Una cuenta/ejercicio/revisión. Nueva revisión durante guardado se pierde: sonda roja preservada. Contexto, segundo envío y reintento no resueltos por silencio. |
| A06 | Política escalar separada; UUID confirmado en listener/sondeo, generación protege esperas obsoletas. Results refresca mismo documento MATCHED o GHOST. | 15 JVM previos; 8 sondas A06-CON previas (timer real incluido). Cuatro guardias anteriores + GHOST pasan; matching posterior automático pasa, con R8 HTTP doble. | SDK sigue como dependencia concreta, no DIP integral. H1 notificación/reintento no implementados. GHOST UI usa callback reflejado, no cron automático de 48 h. |
| A07 | Cálculo rating literal y matching existente separados; control atómico de evaluación/pareja y ghost; trigger espera matching con catch existente. | 21 casos de fórmula previos; 11 invariantes SDK actuales; ghost 0/2→2/2; límite de promesa 0/1→1/1; dos recorridos Ranked automáticos PASS. | No exactamente una llamada al proveedor, cron cloud, entrada/cobro Home, OAuth/Ads, placement automático completo o durabilidad global de stats legacy. |

**Conclusión de revisión:** no se encontró regresión nueva de las extracciones en
el alcance ejecutado. Separaciones SRP justificadas y fórmulas originales
conservadas; no se fuerza OCP/LSP/ISP/DIP ni se equipara atomicidad con SOLID.
Compatibilidad y controles de persistencia/identidad/transacción son correcciones
funcionales declaradas, distintas de las extracciones conservadoras. La auditoría
es del agente, en una fase separada; no una auditoría independiente externa,
aceptación integral, autoría estudiantil o cierre de todos los frentes.

## Correcciones de esta etapa y contratos

DEC-MODE-001 conserva escrituras/historia y prioriza gamemode; gamemodeName es
fallback de lectura. Android aplica el adaptador de snapshot en **siete sitios**:
historial, guardados, reciente Ranked, lectura/listener por ID y dos lectores de
último envío aún consumidos. FirestoreSubmission/toFirestore no cambiaron;
Android sigue escribiendo gamemodeName y el servidor conserva sus escrituras.
Funciones comparten la elección en entrada de R8/Merit; dailyStatsSnapshot la
usa para tema difícil. Playmode se conserva separado. Documentos Tournament
tienen otro contrato; no se modifican sus motores como supuesto lector Submission.

| Datos excepcionales | Android | Evaluador | Estadísticas |
|---|---|---|---|
| Ambos ausentes | Cadena vacía original del DTO | STANDARD original | Sin conteo ON_TOPIC |
| Principal null/número, alias ON_TOPIC | Alias como antes | null→STANDARD; número pasa sin normalizar, como antes | Alias como antes |
| Principal cadena desconocida/vacía | Cadena principal según precedencia | Misma cadena sin nueva validación | Sólo compara literalmente ON_TOPIC |
| Alias null/número | Errores previos de DTO/SDK preservados, incluso con principal válido | null→STANDARD/número sin validar | Sin conteo ON_TOPIC si no hay principal string |

No se impuso un fallback común ni se migraron documentos. Un alias corrupto puede
seguir haciendo ilegible el documento Android; no se declara robustez integral.
La matriz válida cubre STANDARD/ON_TOPIC, cada campo, coincidencia, conflicto y
ausencia. Excepciones se caracterizan aparte, sin convertirlas en reglas nuevas.

Ghost antes leía rating fuera del commit y podía reemplazar un MATCHED ya
confirmado. Dos rojas SDK conservadas: reclamar documento ya emparejado y perder
una actualización de rating concurrente. Ahora lee documento/usuario dentro de
la transacción y exige EVALUATED/PENDING. Cálculo 65/promedio reciente, margen ±2,
deltas +2/-4/+1, clamp 0, plazo 48 h, lote 50 y horario original intactos.
Results ya presenta GHOST; el refresco del mismo ID admite ese estado final bajo
DEC-RES-001, sin navegación ni nuevo timeout. Guardia GHOST comprobada por doble
de callback explícito; no se simuló cron como funcionamiento cloud.

El trigger devolvía antes de concluir matching. Una barrera explícita alrededor
del matching de producción reproduce esa salida anticipada; tras añadir **await**
la misma aserción pasa. Se conserva el catch local/log de rechazo, sin convertir
fallo de matching en FAILED. Evaluación/recompensa se confirman primero, y Android
puede mostrar Results inmediatamente. Pruning existente se inicia después de que
matching concluya, en vez de correr en paralelo; no hay nueva política/operación
de borrado. Stats y pruning aún son efectos legacy fuera de transacción: sin
garantía durable o exactamente una vez. No se operó sobre datos reales.

Rating/Merit, DTO escritor, clave de borrador, validación, mensajes, UUID, conteo,
tokenización, callback de guardado y sesión conservan las fuentes protegidas.
La relectura de ambos documentos protege reciprocidad y una recompensa por ID,
no define deduplicación de dos UUID distintos ni nueva elegibilidad/rango al commit.
Selección ±20 sobre snapshots anteriores al commit sigue como antes.

## Evidencia realmente ejecutada

| Comprobación de esta etapa | Resultado | Alcance |
|---|---|---|
| Android original assembleDebug | PASS; versión final 127.50 s | Gradle/AGP/Compose auténticos, API36. APK original no conectado a cloud. |
| Lab APK + androidTest | PASS; versión final 105.08 s | Variante aislada com.inkr8.lab, no reemplaza configuración original. |
| Functions build tras último await | PASS | Node20/TS auténticos; tipa todo el proyecto, sin despliegue. |
| Modo Functions/estadísticas | Antes 8 PASS/7 FAIL; después 15 PASS/0 FAIL | SDK/lectores compilados; evento/cron/R8-función son dobles. |
| Modo Android | 6 métodos PASS | Matriz 9 payloads × siete API de repositorio, excepciones SDK, dos AppRoot→Results y dos contratos existentes. |
| Results | 6 métodos PASS | Cinco guardias reflejadas/estado fixture y una entrega automática de match posterior; no timer real repetido. |
| Ranked con await | Dos métodos PASS; match previo y posterior | SDK/Functions/entrega automática locales; candidato/segundo escritor fixtures, HTTP R8 doble. |
| Ghost | Antes 0 PASS/2 FAIL; después 2 PASS | Transacciones SDK genuinas, barrera/commit concurrente fixtures. |
| Ciclo del trigger | Antes 0 PASS/1 FAIL; después 1 PASS | Promise real; barrera de scheduling explícita. No fallo cloud observado. |
| Invariantes Ranked | 11 PASS en versión final | Repetición, concurrencia, reserva Merit, placement y estados terminales; aserciones anteriores intactas. |

Hay **13 métodos Android únicos PASS** en la versión pertinente; repetir un método
por un hallazgo/cambio no amplía cobertura. Dos sondas separadas de Writing sin
persistencia real: segundo clic produce dos UUID (caracterización PASS), nueva
revisión se pierde al confirmar A (aceptación FAIL). No sumar caracterizaciones
como aceptación del comportamiento deseado. No se repitieron suites JVM/fórmula
sin cambios. [Resultados](../../../docs/evidence/CORE-CLOSE-001/EXECUTED_RESULTS.json).

AVD y servicios locales se interrumpieron tras una tanda completada. La causa no
está establecida. Se conservan logs/JSON/JUnit; un intento con servicios caídos
falla en Auth y otro comienza antes de que Functions registre el trigger: seis
fallos de entorno, no regresiones del producto. Tras verificar registro y
destinos pasan sólo las comprobaciones afectadas. Capturas HTTP originales en
memoria no se recuperaron; se repiten dos recorridos y se verifican sus prompts
STANDARD/ON_TOPIC. SDK/AVD originales reutilizados, sin wipes o reinstalación.

R8 externo no ejecutado: endpoint HTTP doble con respuesta fija 80; aun si el
campo del motor dice source:real sigue siendo un doble. Auth anónimo y Firestore
son SDK/emuladores reales demo-inkr8-local; eventos de los recorridos Android son
entrega automática local. OAuth Google/Ads/MainActivity/entrada Home se omiten en
LabHost. Tema/tópico IDs llegan al documento ON_TOPIC, pero nombres siguen null
en la entrada R8 original (CT-02); no prueba semántica temática ni corrige esa brecha.

## Casos abiertos clasificados

| Caso | Clasificación / evidencia | Aporte mínimo para otro cambio funcional |
|---|---|---|
| Guardado pendiente o rechazado, misma revisión | Corregido y comprobado A05-CON; decisión humana vigente | Ninguna edición manual nueva. |
| Editar B mientras se guarda A, luego éxito A | Defecto reproducido en Writing real con callback explícito: borrador B queda vacío | Consulta única ya enviada: bloquear edición y segundo envío, o conservar nueva revisión y bloquear segundo envío. Sin respuesta, no se elige por silencio. |
| Segundo clic mientras A persiste | Dos intenciones/UUID reales, sin dos escrituras probadas por esa sonda; decidir deduplicación/edición es funcional | Misma consulta anterior. La idempotencia A07 de un ID no prohíbe dos UUID legítimos. |
| Cambio de cuenta/ejercicio o reanudación | Clave por modo/modalidad/torneo, sin uid/contexto; contrato funcional pendiente, no cambio automático de formato | Definir qué contexto se recupera y tratamiento de borradores previos; fuera de extracción conservadora actual. |
| H1 reintento/notificación tras demora | Brecha respecto de intención H1 E12; política actual preservada: tick 3 s, >90 nominal93, error log, FAILED home, tardío tras timeout ignorado | Definir acción/registro/notificación e identidad de reintento; no inventar nueva duración ni reenvío. |
| Proveedor real, producción y aceptación integral | Fuera de ejecución aislada autorizada; no defecto probado por dobles | Entorno/credenciales de prueba externo autorizado y fuente/criterios reales. No usar cloud original por defecto. |

No nueva consulta sobre precedencia de modo ni aporte manual estudiantil.
Torneos/ligas/reputación siguen como retiradas funcionales separadas; rating/Ranked/
Merit permanecen. Temporadas incluidas, reglas Q-T abiertas; sin ganancias,
costes, sustitutos o períodos inventados.

## Revisar y reproducir

[Diff de esta etapa](../../../docs/evidence/CORE-CLOSE-001/production-final.patch) y
[diff consolidado A05-CON→actual](../../../docs/evidence/CORE-CLOSE-001/CORE-current.patch).
Este último se aplicó/verificó en copia privada contra su preimagen protegida;
ya contiene IMPL-001–009/RET/A05-SUP al inicio y no mezcla retiradas/configuración.
Para A01–A04: [parche conservador histórico](../../../docs/evidence/AUD-001/Inkr8_AUD-001_refactorizacion_conservadora.patch).
IMPL-007–009 conserva sus parches/evidencia en [registro](MECHANICAL_CORE_REFACTOR.md).

En checkout, JAVA_HOME JDK17 y ANDROID_HOME/ANDROID_SDK_ROOT del SDK existente:
`gradlew.bat --offline --no-daemon :app:assembleDebug --console=plain`.
Functions con PATH Node20: `npm.cmd run build`. No reinstalar o cambiar versiones.
APK debug: `app/build/outputs/apk/debug/app-debug.apk`; no instalar éste para
sondas: contiene configuración original cloud. Rutas/hashes [APK.json](../../../docs/evidence/CORE-CLOSE-001/APK.json).

Usar sólo com.inkr8.lab y demo-inkr8-local. Reanudar configuración del laboratorio
desde el export más reciente en [RUNTIME_PRESERVATION](../../../docs/evidence/CORE-CLOSE-001/RUNTIME_PRESERVATION.json),
verificar que Auth9099/Firestore8080/Functions5001 están listos **y** las dos
funciones exportadas cargadas. R8 HTTP doble5010/fetch externo bloqueado. Tras
reiniciar adbd restaurar reverse9099/8080/5001 y aislamiento IPv4/IPv6 del UID lab.
No exportar scheduler/pruning como nuevas funciones ni usar datos cloud.

Instalar los APK lab/test de APK.json y filtrar métodos: LabModeCompatibilityTest,
LabDataContractTest, LabResultRefreshTest, y LabIntegratedFlowTest por método.
Runner: `com.inkr8.lab.test/androidx.test.runner.AndroidJUnitRunner`. Comprobar JUnit,
no sólo exit ADB. Los tests de wait-policy se ejecutan sin evaluador automático
para que las fixtures pending no sean evaluadas por otro proceso; evidencia de
93 s previa suficiente mientras la política no cambie. Los scripts mode/ghost/
lifecycle/invariantes requieren FIRESTORE_EMULATOR_HOST127.0.0.1:8080,
GCLOUD_PROJECTdemo-inkr8-local, NODE_PATH functions/node_modules y prefijos nuevos;
crean fixtures aisladas, no borrar/importar datos reales. Comandos, arneses,
oráculos anteriores y negativos conservados en evidence/CORE-CLOSE-001.

Auditor final: **198 contrastes estáticos PASS**, cuatro controles negativos
privados rechazados (precedencia, guardia ghost, aserción roja y await de matching).
Oráculos completos y resultados previos preservados; no son 198 pruebas
funcionales. [Reporte](../../../docs/evidence/CORE-CLOSE-001/audit-final/audit.json),
[negativos](../../../docs/evidence/CORE-CLOSE-001/NEGATIVE_CONTROLS_FINAL.json) e
[integridad](../../../docs/evidence/CORE-CLOSE-001/PRESERVATION.json). Código sin publicación,
configuración/R8/baseline intactos. Estado demo exportado; servicios propios
detenidos, mismo AVD conservado.
