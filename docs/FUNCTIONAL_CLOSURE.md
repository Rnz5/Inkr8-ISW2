# Cierre funcional local — RET-006 / SEA-001

Registro de implementación y revisión del agente: 06–07/10/2026, America/Lima.
Base local CORE-FINAL-001, HEAD `7d879c22030527e1d5f5003ad84b76b750d3b29e`, rama
`codex/refactorizacion-por-bloques`. Sin commits/publicación/despliegue. Autor del
código, soporte, pruebas y revisión: **Codex**, bajo DEC-AI-AUTH-001. Marco eligió
las reglas en las dos respuestas a la consulta agrupada; no se atribuye escritura,
revisión manual ni aprobación grupal adicional.

## Resultado y alcance

| Bloque | Cambio efectivo | Comprobado | Límite real |
|---|---|---|---|
| RET-006 Android | Retiradas rutas, pantallas, modelos y repositorio exclusivos de torneos; leaderboard/Pantheon/consulta y compra de reputación; placement muestra rating | Compilación auténtica; Profile, Ranked, placement y consumidores Compose; Practice y Ranked hasta Results | Campos históricos permanecen en DTO/initializer y claves de borradores; no son funciones activas |
| RET-006 Functions | Sin exports/jobs/efectos de torneos, ligas, tips y reputación; sin penalización de abandono/deuda por reputación; limpieza de sesión conservada | 10 sondas SDK de retirada/economía; 11 Ranked, 2 ghost, 1 lifecycle PASS | Servicios ya desplegados no se operaron ni retiraron; versión local preparada |
| Selección Ranked | Standard u On-Topic elegidos por usuario; contexto real antes de cobrar, fallo visible conserva elección; sin fallback a Standard | Entrada Standard hasta envío/Results y On-Topic con cargo real local; rechazo SDK de tema/tópico sin cargo ni navegación | SDK de Firebase en demo; OAuth/Ads externos fuera de comprobación |
| SEA-001 | Mes UTC, participación con placement vía Ranked; registro por createTime confirmado; delta real/clamp, empates compartidos, cierre tras pendientes e historial propio | 21 casos SDK + 1 comprobación de entrega automática sobre 6 documentos; ranking/historial en Compose/callables Auth | Fin de mes/reloj y estados terminales controlados; cron invocado directamente, no scheduler cloud ni 48 h de espera real |
| Núcleo A01–A07 | Se conserva cierre CORE-FINAL-001, sin nuevas extracciones cosméticas | 22 regresiones borrador/persistencia/reintento; STANDARD y ON_TOPIC hasta Results; transacciones repetidas/concurrentes | No se suma cobertura repetida a los resultados históricos ni se declara proveedor real |

La retirada e incorporación son **cambios funcionales**, no prueba de SOLID ni
refactorización conservadora. Ranked, rating, Merit y sus fórmulas permanecen.
Reputación histórica sólo alimenta el precio existente: no se muestra, compra,
recompensa o penaliza. No se borraron colecciones/documentos históricos ni se
migraron partidas anteriores. Torneos históricos son ignorados por el evaluador.
Las estadísticas nuevas excluyen torneos; no reescriben snapshots anteriores.

## Decisiones con procedencia

**DEC-RANKED-SELECT-001:** Marco responde «me parece muy buena tu propuesta
aplicala» a la opción recomendada de la consulta agrupada. Aprueba elección
Standard/On-Topic, error sin fallback si no se carga contexto y congelar reputación
histórica como factor del precio actual. Evidencia: [respuesta y propuesta
exactas](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/RET-006/RANKED_HUMAN_DECISION.json>).

**DEC-SEASON-001:** Marco responde «me gusta tu propuesta» a temporadas mensuales
UTC, usuarios con placement por Ranked, variación de rating sin reiniciar rating
ni placement, orden descendente/igual posición en empate, pertenencia por guardado
confirmado, cierre tras pendientes (incluido ghost 48 h), historial de rating/
posición/Merit ya ganado, sin premios nuevos ni asignación histórica automática.
Evidencia: [reglas aceptadas](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SEA-001/HUMAN_DECISION.json>). Se registra la
procedencia del chat, no una fecha de acto del profesor o nueva aprobación grupal.

## Implementación y revisión separada

- `Competitions`: la liga dejó de decidir ejercicio. Carga tema/tópico antes del
  callable existente; conserva límites diarios, precio, mensajes económicos,
  sesión y motivo ENTER_RANKED. El error de contexto desbloquea y conserva modo.
  La coroutine UI usa Main.immediate: la nueva prueba roja de Toast fuera de
  Looper permanece en evidencia y pasa con la corrección del agente.
- `AppViewModel/AppRoot/Writing`: desaparece únicamente la coordinación de torneos.
  Contrato de persistencia/generaciones/onError último, UUID, validación,
  tokenización, revisiones y namespace de borrador conservados. Durante revisión
  se restauró la escritura de palabras asignadas accidentalmente omitida; los
  casos de reentrada/asignación real y cambios de cuenta/ejercicio pasan.
- Matching/ghost conservan cálculos, reciprocidad y límites de transacción. Añaden
  `seasonRatingChange` **dentro** de la transacción original: movimiento después
  del clamp real, separado del delta de presentación. Sonda ghost -4 mostrado,
  rating general 0 y movimiento estacional 0 PASS. Ninguna recompensa nueva.
- `seasonLedger` separa registro/liquidación/cierre de la evaluación y del precio.
  Transacciones acusan registro una vez, contabilizan terminal una vez y no
  actualizan usuarios. EVALUATED sin match sigue pendiente; FAILED/NOT_EVALUABLE
  cierran sin rating adicional. Liquidación contabiliza Merit ya registrado.
  El delta pertenece al mes del guardado, aunque el resultado llegue después.
- `seasonFunctions` ofrece ranking actual e historial cerrado propio mediante
  callables autenticados; listeners creados/actualizados con retry. El registro
  previo a evaluación y eventos redundantes evitan perder el settlement ante
  entregas repetidas; Practice no incorpora lecturas estacionales innecesarias.
- El cierre revisa pendientes y acuses de documentos guardados antes de cerrar.
  Una revisión posterior detectó reapertura por update CLOSING fuera de la
  transacción: eliminado, transiciones dentro del control atómico. Repetición y
  concurrencia no cambian CLOSED/closedAt. Posiciones finales se derivan de
  miembros asentados; no bulk rewrite ni nueva concesión de Merit.
- Revisión adicional de poda: el archivo de diez podía eliminar un envío con
  ledger pendiente. Reproducción roja en DB dedicada, aserción intacta. Se protege
  Ranked posterior a activación hasta acuse y liquidación terminal; no cambia
  archivo histórico previo, costes, rating ni reglas de timeout. Nueva sonda
  eleva a 21 casos; Functions recompiladas y auditor/diff actualizados.
- `SeasonRepository` y `SeasonsScreen` son lectores/presentación independientes:
  posición propia, ranking, historial seleccionable y errores de carga. Se revisan
  consumidores reales de rutas/firmas y desaparecen imports de módulos retirados.
  Campos DTO, initializer y DraftManager legacy permanecen por compatibilidad.

Contrargumento: separar cada condición habría añadido dependencias sin necesidad.
Se conserva coordinación del núcleo cuando está justificada; la separación nueva
responde a períodos/contabilidad/historial con motivos de cambio propios. No se
fuerzan cinco principios ni se declara una auditoría independiente humana.

## Pruebas realmente ejecutadas

| Comprobación | Resultado actual | Evidencia |
|---|---|---|
| Android auténtico, assembleDebug + testDebugUnitTest | PASS, 19 métodos unitarios | SEA-001/runs/android-functional-final |
| Functions TypeScript con package/scripts auténticos | PASS | SEA-001/runs/functions-prune-protection-final |
| APK laboratorio + instrumentación | Compilan; **37 métodos únicos PASS** | [Métodos y fallos previos](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SEA-001/EXECUTED_RESULTS.json>) |
| Retirada/economía antes/después | Antes 4 PASS/6 FAIL esperados; después **10 PASS** | RET-006/sdk-retirement-before.json y sdk-retirement-after.json |
| Ranked idempotencia/concurrencia/terminales/hold | **11 PASS** sobre código final | SEA-001/final-ranked-main.json, final-ranked-hold.json, final-ranked-terminal.json |
| Ghost + lifecycle | **2 + 1 PASS** sobre código final | SEA-001/final-ghost-atomic.json, final-matching-lifecycle.json |
| Temporadas SDK | **21 PASS** sin sumar las ejecuciones iniciales, incluida roja de poda corregida | [Detalle](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SEA-001/season-prune-after.json>) |
| Eventos estacionales automáticos locales | **1 PASS**, 6 documentos liquidados contrastados con ledger y miembros | [Detalle](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SEA-001/automatic-season-settlement.json>) |
| Auditor estático | **281 PASS**; criterios anteriores intactos; 2 controles negativos rechazados | SEA-001/runs/audit-prune-final/report/audit.json y AUDITOR_NEGATIVE_CONTROLS.json |
| Diff funcional aplicable al snapshot CORE-FINAL | PASS con git apply --check | SEA-001/runs/functional-patch-prune-final |

Los 22 métodos de borrador/espera comprueban pendiente, éxito y rechazo real de
Firestore, revisión B y A→B→A, callbacks repetidos, bloqueo de segundo envío,
restauración/cambio de usuario/contexto/reentrada, error de consulta y reintento
sin escritura/efectos y generación antigua, incluidos intervalo 3 s/umbral >90 s.
Se ejecutan con entrega de evaluación explícitamente no-op; después se restaura
el evaluador auténtico y se confirma la entrada pagada Standard hasta Results.
Las aserciones rojas históricas y de aceptación no se debilitan.

Fallos conservados: showButton omitido (regresión de retirada, corregida), errores
TS de la implementación nueva (corregidos), copia de fuentes retiradas en espejo
privado (corregida), Timeout SDK al preparar fixture, acceso incorrecto a nodos
LazyColumn (soporte corregido sin cambiar rank/rating), Toast sin Looper (corregido),
y un timeout UI de STANDARD aunque Firestore ya evaluó (causa única no demostrada;
sonda conservada, ejecución posterior PASS). No se presentan esos fallos como
resueltos por static hashes. [Resultados consolidados](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SEA-001/EXECUTED_RESULTS.json>).

## Dobles y límites

Auth anónimo, SDK Android/Admin, Firestore, Functions/callables, transacciones y
Compose son reales **dentro de demo-inkr8-local**. R8 usa HTTP local fijo 80:
no acredita evaluación del proveedor externo. OAuth/Ads/MainActivity externos no
se ejecutan. Eventos/barreras/callbacks/relojes/estados terminales controlados se
identifican en cada sonda. El cierre programado se invoca con SDK, no Cloud Scheduler.
En las sondas Node, CloudFunctions `.run` reciben CloudEvent/auth-request
construidos explícitamente: validan su cuerpo y transacciones SDK, no el transporte
de autenticación. Los callables desde Android sí atraviesan HTTP/Auth del demo.
La corrección de FieldValue del loader local usa la clase SDK modular genuina;
no modifica la configuración ni versiones originales.

Para habilitar temporadas fuera del demo, el responsable necesita fijar
**SEASON_ACTIVATED_AT_MS**, fecha de activación nueva de esta funcionalidad (no
configuración original recuperada). Sin valor, no se registra ni cierra; los
callables informan error. El filtro usa createTime de servidor, no timestamp del
cliente; no backfill. Un import de emuladores cambia createTime: al reanudar este
laboratorio debe actualizarse el cutoff local después del import antes de admitir
nuevos envíos; los acuses/ledger exportados permanecen. Eso no altera la regla del
producto ni justifica reasignar historia.

Dependencias técnicas concretas para aceptación externa:
1. Reglas Firestore originales/autorizadas (`firestore.rules` o ruta original) y
   seguridad de nuevas colecciones: clientes no deben escribir assignments,
   miembros o temporadas. El ZIP no aportó rules; el demo tiene reglas de prueba
   permisivas/rechazos controlados. No se acredita inmutabilidad frente a ataques.
2. Proveedor/configuración R8 de prueba, OAuth/Ads y entrega/scheduler cloud de
   prueba autorizados; no usar datos reales. Ningún despliegue se realizó.
3. Validación operativa de fin de mes, pendientes reales durante 48 h y escala.
   El cierre escanea Ranked para detectar creaciones aún no acusadas; falta
   medir ese coste con volumen real. La poda protege ahora Ranked nuevos hasta
   acuse/liquidación; prueba roja real de eliminación y luego PASS conservadas.
   Se conservan límites/índices y tratamiento de archivo anterior a activación.
4. Auditoría independiente posterior, informe académico y sustentación/autoría
   individual auténtica. Esta revisión del agente no los reemplaza.

No quedan decisiones humanas sin responder en la consulta de selección y
HU 3.27/3.28. Los puntos anteriores son cobertura/configuración operativa, no
nuevas reglas económicas ni tareas de copiar código.

## Diferencias, APK y reproducción

- [Diff funcional contra snapshot CORE-FINAL](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SEA-001/FUNCTIONAL-current.patch>):
  75 fuentes/verificaciones declaradas, 32 componentes exclusivos retirados.
- [Diff consolidado de fuentes](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SEA-001/CONSOLIDATED-source.patch>): incluye
  también cambios anteriores y fuentes auténticas recuperadas todavía untracked;
  no atribuir todas sus líneas a esta etapa. Documentación/auditoría adicional
  trazada en Git y registros de implementación.
- [Rutas/hashes de APK y entregables](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SEA-001/DELIVERABLES.json>).
- [Conservación de originales, baseline e historia](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SEA-001/PRESERVATION.json>).
- [Export y reanudación demo](<C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SEA-001/RUNTIME_PRESERVATION.json>).

En el checkout, con JDK17/SDK ya configurados y Node20 auténtico:

```powershell
.\gradlew.bat --offline --no-daemon :app:assembleDebug :app:testDebugUnitTest --console=plain
Set-Location functions
npm run build
```

El APK original compilado se conserva en `app/build/outputs/apk/debug/app-debug.apk`;
no instalarlo para pruebas conectadas a su Firebase original. APK aislado y tests
están en `outputs/functional-closure-2026-10-07` de la carpeta Codex de esta tarea,
package com.inkr8.lab. LabHostActivity muestra composables reales mediante las
sondas; no sustituye inicio OAuth/Ads del producto. Para repetir, reanudar el demo
según RUNTIME_PRESERVATION (Node20/Java21/loopback), actualizar sólo el cutoff local
after-import, verificar exports y el doble HTTP R8. Instalar ambos APK y ejecutar:

```powershell
adb -s emulator-5560 reverse tcp:9099 tcp:9099
adb -s emulator-5560 reverse tcp:8080 tcp:8080
adb -s emulator-5560 reverse tcp:5001 tcp:5001
adb -s emulator-5560 install -r inkr8-lab-debug.apk
adb -s emulator-5560 install -r inkr8-lab-tests.apk
adb -s emulator-5560 shell am instrument -w -e class com.inkr8.lab.LabFunctionalScopeTest com.inkr8.lab.test/androidx.test.runner.AndroidJUnitRunner
```

Para las sondas SDK, desde checkout con NODE_PATH apuntando a functions/node_modules,
GCLOUD_PROJECT=demo-inkr8-local y FIRESTORE_EMULATOR_HOST=127.0.0.1:8080:

```powershell
$productionFunctionsPath = (Resolve-Path .\functions).Path
$env:NODE_PATH = Join-Path $productionFunctionsPath 'node_modules'
$env:GCLOUD_PROJECT = 'demo-inkr8-local'
$env:FIRESTORE_EMULATOR_HOST = '127.0.0.1:8080'
node testing-blocks/functional-withdrawals/retirement-check.cjs $productionFunctionsPath identificador-nuevo C:/ruta/nueva/retirement.json
node testing-blocks/seasons/season-ledger-check.cjs $productionFunctionsPath identificador-nuevo C:/ruta/nueva/seasons.json
node testing-blocks/seasons/automatic-season-check.cjs $productionFunctionsPath C:/ruta/nueva/automatic.json
```

Usar un identificador de base y rutas de informe nuevos por ejecución; no reutilizar
fixtures de una corrida anterior. El tercer script requiere documentos emparejados creados por flujos locales reales;
no los fabrica. Cada script y log conserva sus dobles/precondiciones. Para A05/A06
usar el modo explícito no-op de CORE_REGRESSION_PHASE y restaurar luego la entrega;
no ejecutar toda la suite mezclando sus modos. Salida adb=0 no equivale a JUnit PASS.
Comandos exactos/entornos/logs de cada ejecución están en `runs/*/command.json`.

**Conclusión:** retirada funcional y temporadas implementadas y verificadas en el
alcance local descrito, con núcleo conservado. Cierre técnico local de esta etapa;
aceptación externa, operación productiva e informe académico siguen pendientes.
No se declara el producto completo terminado ni los servicios cloud retirados.
