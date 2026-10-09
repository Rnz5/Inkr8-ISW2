# CORE-FINAL-001 — cierre técnico acotado del núcleo A01–A07

Registro 06/10/2026, America/Lima. Código, soporte, pruebas y revisión separada:
**Codex**. DEC-AI-AUTH-001 es permiso docente comunicado por Marco; no se atribuye
edición/revisión estudiantil ni aprobación grupal/acto docente adicional.
DEC-DRAFT-REV-001, DEC-DRAFT-SCOPE-001 y DEC-WAIT-RETRY-001 proceden del prompt
humano de continuación desde CORE-CLOSE-001. DEC-MODE-001 y DEC-RES-001 permanecen.

**Conclusión:** cierre técnico del núcleo en los contratos explícitos de esta
etapa y el laboratorio local autorizado. No quedan defectos demostrados abiertos
de esos criterios tras las correcciones y regresiones afectadas. Esto no cierra
el producto completo, la evaluación externa, las retiradas, temporadas o entrega
académica; tampoco afirma SOLID integral ni ausencia universal de fallos.

| Bloque | Implementado / motivo de separación | Comprobado | Pendiente o límite |
|---|---|---|---|
| A01 | Regex reutilizadas; algoritmo/mensajes originales. Mejora local, sin forzar SOLID. | 38 casos JVM históricos, oráculo de fuente completo preservado y build Android actual. | Sin nueva medición de rendimiento ni validación externa del filtro. |
| A02 | Conteo, tokenización y orden/filtro de palabras conservados. | 20 casos históricos y admisión de producción; Compose real con 49/50/150/151, signos/case y omisiones. | Payload temático/restricciones originales conservados, sin imponer todas las palabras o reglas nuevas. |
| A03 | Cinco componentes informativos extraídos. SRP: presentación cambia por motivo distinto al envío. | Cuerpos completos conservados; consumidores compilados; Writing STANDARD/ON_TOPIC ejecutados. | No cada diálogo, dispositivo o variación visual. |
| A04 | Score/feedback fuera de coordinación de Results. SRP acotado. | Fuente conservada; Compose, score, feedback, rating/Merit y refresco real del mismo envío. | R8 HTTP simulado; no evaluación externa/temática real ni todos los umbrales visuales. |
| A05 | Admisión pura conservada. Asignación/contexto estable antes de restaurar; editor separado por su vida útil. DraftManager almacena revisión/confirmación; UI posee edición. | B, A→B→A, callback repetido/error, pendiente/éxito/rechazo SDK, bloqueo/liberación, reentrada, Auth A/B/A, ejercicio/palabras y recomposición con metadata. | Muerte de proceso durante guardado no ensayada; históricos sin propietario quedan retenidos sin migración. No defecto abierto del contrato aprobado. |
| A06 | Política escalar conservada; VM posee identidad/generación/persistencia/espera, repositorio confirma Task, Root conecta y Loading presenta. | Mismo ID en listener/sondeo; B/esperas antiguas ignoradas; timer real 93 s; error/demora/reintento sólo lectura; tardío disponible recuperado; FAILED conserva vuelta Home. | SDK concreto conservado, sin afirmar DIP integral. No todo fallo de red/dispositivo/cloud. |
| A07 | Cálculo literal y matching separados del motor; controles atómicos de evaluación/pareja/ghost y await del matching. Atomicidad es corrección, no un principio SOLID. | 11 invariantes SDK, ghost 2, lifecycle 1; 21 casos de fórmula previos; Ranked con rival previo y posterior automáticos locales. | No garantía de una sola llamada al proveedor, cron cloud, durabilidad global de stats legacy o entrada/cobro Home. |

## Resultado de los contratos de este prompt

| Criterio humano | Resultado de producción y aceptación |
|---|---|
| Editar B mientras A persiste | Editor habilitado para escribir; botón y VM impiden otro envío. Confirmación limpia sólo la revisión exacta, no igualdad de texto: A→B→A se conserva. B y vacío B quedan protegidos; fallo libera bloqueo y conserva texto/borrador. |
| Callbacks/reentrada | Guardas de intento evitan doble notificación. Confirmación de revisión en preferencias notifica al editor montado; composiciones anteriores no regraban texto confirmado. Dos sondas de reentrada: antes 1 PASS/1 FAIL (fallo UI con callback doble; SDK ya PASS), después ambas PASS, aserciones intactas. |
| Cuenta/contexto | Namespace v2 por UID autenticado, modo, Playmode, torneo y tema/tópico existentes; draft añade IDs de palabras en orden (texto existente si ID vacío). No UUID de ejercicio inventado. Metadata completa de asignación se conserva para reentrada; nombres/descripciones no reinician el editor. Auth local A/B/A y ejercicios distintos PASS. |
| Históricos | getDraftKey y entradas antiguas permanecen. Writing no los reclama ni restaura automáticamente. Sin UID no se crea almacenamiento compartido. No migración/atribución ni borrado de históricos. Limpieza tras confirmación afecta sólo la revisión propia y su asignación local. |
| Demora/error | Mensaje comprensible y botón Retry result check; intervalo 3 s y umbral >90 intactos, primer timeout nominal 93 s realmente esperado. |
| Reintento | Nueva generación, listener y lectura inmediata/poll del mismo ID confirmado. Sin nueva submission, evaluación, llamada de cobro o efecto. Documento/usuario completos iguales antes/después de consulta; contador de R8 HTTP fue 0 durante fase de reintento. Log ResultWait registra sólo ID/generación, no contenido/secretos. |
| Terminal/tardío | FAILED mantiene Home; EVALUATED conserva navegación inmediata y refresco del mismo Ranked. Resultado tardío no resuelve la generación vencida; reintento lo recupera. NOT_EVALUABLE mantiene su tratamiento anterior, no se inventa terminal nuevo. |
| STANDARD/ON_TOPIC | APK final: AppRoot→Writing→repositorio→Firestore→evento automático local→evaluateWithR8 auténtico→doble HTTP→Results PASS. Alias/prevalencia y Playmode separados; nombres temáticos/payload originales no se presentan como evaluación temática real. |

## Pruebas y hallazgos

**46 métodos Android únicos PASS** (último resultado por método, sin sumar
repeticiones), **19 unit tests del módulo real PASS** (18 pertinentes + 1 ejemplo).
**11 Ranked + 2 ghost + 1 lifecycle** usan SDK/transacciones reales; eventos,
respuesta R8 y barreras de planificación son dobles explícitos en esos scripts.
Las 28 pruebas escalares de admisión previas se reutilizan: esta etapa regeneró
procedencia y la task test estaba UP-TO-DATE, por lo que no se cuenta como otra
ejecución. Android debug y laboratorio compilan con versiones auténticas.
Functions no cambió en esta etapa: build CORE-CLOSE-001 válido reutilizado,
fuentes/lib y fórmulas byte a byte conservadas; SDK actual sí se ejecutó.

Hallazgo corregido: startWriting no invalidaba una espera anterior; roja real del
límite UI/VM con entrega de callback explícita (writing→results indebido), luego
PASS sin cambiar la aserción. Se cancela listener/job y generación al abrir el
nuevo editor. Otro hallazgo: confirmación no alcanzaba la composición recién
montada; marcador/observación nativa de revisión lo corrige. Las claves Compose
ahora usan identidad estable, no cambios descriptivos del modelo.

Fallos de soporte conservados: una aserción de restauración se adelantó a la
asignación asíncrona (espera del nodo real, aserción vacío intacta); una siembra
SDK de palabra agotó 15 s antes de la aceptación (causa no establecida, sonda
repetida sola PASS). Retirar un export ya registrado dejó un endpoint de emulador
sin función: corregido en fixture con no-op y metadata auténtica durante fase
sin evaluación. Un filtro mal escrito seleccionó 0 métodos Ranked; se ejecutó
el método existente por separado. No se atribuyen esos fallos al producto ni se
ocultan en los resultados finales. Caracterizaciones antiguas que exigían pérdida
o segundo UUID quedan Ignore con sus aserciones, sin contarlas PASS.

Revisión separada: **206 contrastes estáticos PASS**, oráculos completos anteriores
conservados y cambios funcionales invertidos antes de ellos; cuatro manipulaciones
privadas rechazadas. No son pruebas funcionales ni auditoría externa. No se
introdujeron interfaces/patrones para aparentar OCP/LSP/ISP/DIP; if/else por sí
solo no supone infracción. No se detectó regresión nueva en lo efectivamente
ensayado. Fórmulas rating/Merit, UUID, validación, límites, mensajes previos,
conteo/tokenización y escritores de modo conservados.

[Resultados finales](../../../docs/evidence/CORE-FINAL-001/EXECUTED_RESULTS_FINAL.json),
[revisión](../../../docs/evidence/CORE-FINAL-001/FINAL_REVIEW.json),
[auditor estático](../../../docs/evidence/CORE-FINAL-001/audit-final/audit.json),
[rojas/reentrada](../../../docs/evidence/CORE-FINAL-001/REENTRY_REVIEW.json),
[log de reintento](../../../docs/evidence/CORE-FINAL-001/RETRY_LOG_PRIVACY.json).

## Diferencias y APK

[Diff consolidado desde HEAD preservado](../../../docs/evidence/CORE-FINAL-001/CORE-final.patch)
comprende cambios anteriores del núcleo y presentación; configuración/secretos no
incluidos. [Sólo esta etapa](../../../docs/evidence/CORE-FINAL-001/production-stage-final.patch)
modifica cinco fuentes: Writing, DraftManager, AppRoot, AppViewModel y LoadingScreen.
Ambos diffs se aplicaron/compararon en una copia privada, no en el checkout real.
Las retiradas históricas se identifican por sus registros; no hay retirada o
regla de temporadas nueva en esta etapa. HEAD e índice permanecen intactos.

[APK/rutas/hashes](../../../docs/evidence/CORE-FINAL-001/APK_FINAL.json):
`C:/Users/marco/Documents/Codex/2026-10-04/inkr8-refactorizacion-por-bloques/outputs/CORE-FINAL-001/inkr8-lab-debug-final.apk`
y `inkr8-lab-androidTest-final.apk`. Paquete com.inkr8.lab/demo-inkr8-local;
**requiere el runner**: LabHost es vacío al lanzarlo solo y los tests seleccionan
las pantallas reales. OAuth/Ads/MainActivity se omiten. APK original cloud compila
pero no fue instalado ni usado contra datos reales.

## Reproducción en el entorno existente

No recuperar/reinstalar SDK, AVD o fuentes. Con JDK17 y SDK ya disponibles:

```powershell
$env:JAVA_HOME = 'C:/Users/marco/AppData/Local/Temp/inkr8-baseline-jdk17/expanded/jdk-17.0.20.1+1'
$env:ANDROID_HOME = 'C:/Users/marco/Documents/Codex/2026-10-04/inkr8-refactorizacion-por-bloques/work/REC-002-private/android-sdk'
Set-Location -LiteralPath 'C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/checkout/Inkr8-ISW2'
.\gradlew.bat --offline --no-daemon :app:assembleDebug :app:testDebugUnitTest --console=plain
```

Para laboratorio usar el export final/configuración aislada de
[RUNTIME_PRESERVATION](../../../docs/evidence/CORE-FINAL-001/RUNTIME_PRESERVATION.json), Node20,
Java21 y CLI existentes. No usar el google-services/APK original. Auth9099,
Firestore8080, Functions5001 y R8 doble5010 exclusivamente loopback; verificar
LabApplication/project ID, definición de funciones y firewall del UID IPv4/IPv6.
Reanudar el mismo AVD emulator-5560. Para sondas de pendiente/rechazo/timer mantener
entrega del evaluador desactivada mediante fixture declarado; para recorridos
completos activar el motor auténtico con fetch externo bloqueado.

```powershell
# Desde la carpeta del task, con servicios demo y fase apropiada comprobados:
$inkr8Adb = '.\work\REC-002-private\android-sdk\platform-tools\adb.exe'
9099,8080,5001 | ForEach-Object { & $inkr8Adb -s emulator-5560 reverse "tcp:$_" "tcp:$_" }
& $inkr8Adb -s emulator-5560 install -r '.\outputs\CORE-FINAL-001\inkr8-lab-debug-final.apk'
& $inkr8Adb -s emulator-5560 install -r '.\outputs\CORE-FINAL-001\inkr8-lab-androidTest-final.apk'
& $inkr8Adb -s emulator-5560 shell am instrument -w -r -e class 'com.inkr8.lab.LabDraftContractTest,com.inkr8.lab.LabRootFlowTest' com.inkr8.lab.test/androidx.test.runner.AndroidJUnitRunner
# Fase sin evaluación: LabRetryTest incluye espera real de 93 s.
# Fase con evaluación: los dos métodos Root de LabModeCompatibilityTest y LabIntegratedFlowTest.
```

Interpretar JUnit (OK/FAILURES), no adb exit 0. Los scripts Node SDK están en
testing-blocks/rating-characterization y android-emulator-lab; exigir proyecto
demo y host127.0.0.1:8080, sin credenciales cloud, prefijo nuevo de letras/database
nueva y reporte nuevo. [Comandos reales](../../../docs/evidence/CORE-FINAL-001/runs) conservan
argumentos/entorno por capa. Para auditor usar verify_local_refactor.py con
--repo y --report-dir nuevo. No sobrescribir evidencias anteriores.

## Clasificación de lo restante

**Contratos aprobados de esta etapa:** sin defecto demostrado pendiente después
de las correcciones. **Comportamiento histórico conservado:** alias mal tipado
puede fallar Android; fallback inválido/ausente por capa y payload temático
original se caracterizan, no se redefinen. Su endurecimiento/semántica requeriría
contrato adicional, no se oculta como cobertura verde.

**Límites de cobertura:** proveedor externo R8, OAuth/Ads, cron/infra cloud,
Home/entrada Ranked, placement automático completo, muerte de proceso/crash
mid-write y todos los dispositivos/diálogos. R8 fijo80 no acredita calidad
lingüística o evaluación temática. **Mejoras opcionales:** reducir duplicación de
llamadas externas, durabilidad de efectos legacy, abstracción SDK adicional y
cobertura visual/propiedades más amplia, con razón independiente y contrato.
**Frentes separados:** retiradas completas de torneos/ligas/reputación (Ranked,
rating y Merit permanecen), reglas/implementación de temporadas, aceptación
externa e informe/defensa/evidencia individual académica. No código automático
atribuido al equipo, publicación, despliegue o datos reales.
