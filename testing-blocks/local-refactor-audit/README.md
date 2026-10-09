# Auditoría local reproducible

AUD-001. Herramienta Codex de lectura y contraste de fuentes ya existentes. No implementa A05–A07 ni sustituye juicio, aporte, ejecución o revisión estudiantiles.

Desde la raíz del checkout, con Python 3 disponible:

```powershell
python .\testing-blocks\local-refactor-audit\verify_local_refactor.py --repo . --report-dir ..\Inkr8-audit-revision-01
```

Usar una carpeta de reporte nueva, preferentemente fuera del checkout. Se registra audit.json y el proceso devuelve 0 si pasan todos los contrastes o 1 si hay una diferencia/fuente ausente. No modifica fuentes, Git, SDKs ni servicios. Con --candidate-root se puede inspeccionar una copia aislada usando el checkout Git como referencia.

El spec fija HEAD/origin y fragmentos auténticos de IMPL-001–004/RET-001. Reconstruye cambios conservadores y retiradas por separado; compara todas las otras fuentes Kotlin/TypeScript originales; comprueba 20 blobs/tamaños originales de recursos. UTF-8 explícito, CRLF/LF normalizados sólo al comparar código. Los metadatos de recursos provienen de Inkr8-Android 9ff46a1e690ec8e3b8b79b64da2e47134dd26c15.

Son 132 contrastes estáticos en el estado auditado, no 130 pruebas funcionales ni cobertura académica. No compila/resuelve tipos/Compose, no ejecuta remember/recomposición, ViewModel/Firebase/R8/transacciones ni valida decisiones del estudiante. Las fuentes originales de build siguen faltando.

Si existe una intervención posterior legítima, esta auditoría fijada al estado actual debe revisarse en un nuevo bloque con trazabilidad; no cambiar expected sólo para hacerla pasar. El control negativo AUD-001 altera >= por > en una copia, y el auditor debe rechazarla. Las pruebas JVM/Node anteriores mantienen su alcance separado.

## Extensión AUD-002 — 05/10/2026

IMPL-007–009 declaran tres extracciones mecánicas. El auditor obtiene la admisión,
la espera y el rating originales de Git HEAD y reconstruye los archivos completos:
no cambia límites, reglas ni expectativas para aceptar un hash nuevo. Las comprobaciones
de recursos/componentes anteriores siguen vigentes. La versión/spec anterior y el
resultado de 132 comprobaciones se conservan en evidencia; no representan el resultado vigente.
La ampliación exige controles negativos de cada módulo nuevo y rechazo de cambios
en efectos/transacciones. Su ejecución no valida Compose, Firebase, R8 o autoría humana.

## Extensión de retiradas RET-002–004 — 05/10/2026

`approved_presentation_changes` contiene diecinueve transformaciones exactas de cuatro
archivos (una con dos ocurrencias del subtítulo rating). Cada fragmento/count debe
existir en Git HEAD y la aplicación de las retiradas debe reproducir el archivo
completo actual: no se acepta simplemente un nuevo hash candidato. Las fuentes
originales restantes, extracciones del núcleo, recursos y componentes mantienen
sus contrastes previos. Siguen siendo 135 contrastes estáticos: sustituir cuatro
comprobaciones de archivos intactos por cuatro reconstrucciones no aumenta la
cantidad ni la convierte en pruebas funcionales.

Versión/spec/resultados anteriores preservados en snapshots de RET-002-003. Cuatro
controles negativos independientes deben rechazar tanto la reintroducción de una
presentación retirada como un cambio a Ranked, logout o racha. No se flexibiliza
la conservación de callbacks, listeners, datos, contratos o reglas pendientes.

## RET-005 — placement sin widgets de liga

Dos regiones adicionales declaradas del archivo PlacementRevealScreen: cálculo
del veredicto y widgets de liga, conservando firma, secuencia de reveal/continuación.
Se reconstruye el archivo completo desde HEAD. Siguen 135 contrastes estáticos,
no pruebas Compose. Spec/resultados RET-002–004 preservados; controles negativos
del callback y delay nuevo deben ser rechazados.


## SCP-001 / REC-002 — alcance y fuentes posteriores

La instrucción posterior de Marco conserva Merit. El spec conserva las retiradas
de ligas/reputación/torneos y reconstruye las restituciones exactas de presentación,
sin cambiar contratos o reglas. Spec/tool/report anteriores están en SCP-001/before
y las auditorías originales permanecen intactas. Dos fallos de preparación del
auditor (decodificación UTF-8 y dominio de whitelist) se conservan junto con sus
correcciones; no son defectos de producción ocultados.

`authentic_recovered_files` comprueba hashes de veinte archivos públicos recuperados
del ZIP, incluyendo R8. Sólo las fuentes Kotlin/TypeScript recibidas se admiten en
el inventario correspondiente; configuración privada Google Services queda fuera
del spec y del diff de entrega. Son 155 contrastes estáticos actuales. Functions
compila por separado con configuración/SDK auténticos; Android/Compose no se
valida por este auditor. [Estado](../../docs/RECOVERED_ENVIRONMENT.md).

## AND-001 — verificación del módulo Android y transacciones locales

Se añaden tres hashes de fuentes de verificación Codex, sin modificar los 155
contrastes previos ni los hashes de configuración original. Spec/tool anteriores
preservados en AND-001/before-audit. Total: 158 contrastes estáticos. Este auditor
no ejecuta las pruebas ni acredita Compose/Firebase por hashes. Las ejecuciones
del módulo Android y Firestore están registradas por separado en
[ANDROID_EXECUTION](../../docs/ANDROID_EXECUTION.md); R8 es un doble en los casos
transaccionales, no el proveedor real. No se acredita autoría estudiantil.

## AND-002 — ejecución local ampliada

Se conservan las 158 expectativas previas y se añaden hashes del arnés aislado
y las pruebas de repetición/concurrencia. Las expectativas de pruebas anteriores
no cambian. El probe real de isPlaced permanece rojo; este auditor no lo ejecuta
ni lo convierte en PASS. Los resultados JUnit y de emuladores se registran por
separado en ANDROID_EXECUTION. Spec/tool anteriores: AND-002/before-audit.

## A05-SUP-001 / A05-CON-001

Codex callback support and approved draft-retention correction are inverted exactly
before all prior conservation checks. Reconstructed preimages receive separate SHA
checks. Previous spec, tool and failing audit retained in A05-CON-001. Existing
acceptance assertions are unchanged; static checks do not execute Firebase/Compose
or establish manual student integration.

## A06-CON-001 / A07-CORE-001

La asociación de documento/generación y los controles atómicos declarados se
invierten exactamente antes de las comparaciones completas anteriores. Matching
se deriva de la función original de Git; fórmulas intactas. Spec/tool previos
conservados en A07/before-audit. Los controles negativos rechazan guardia alterada
y aserción histórica debilitada. DEC-AI-AUTH-001 permite cambios del agente; no
se infiere autoría estudiantil. El auditor no ejecuta transacciones/pantallas: la
sonda de Results posterior al emparejamiento sigue roja pese al PASS estático.

## CORE-CLOSE-001 — trazabilidad adicional

Se invierten compatibilidad/ghost/refresco GHOST/await declarados antes de los
oráculos completos anteriores. Nuevas pruebas conservan aserciones rojas y se
registran con hash; antecedentes spec/tool/report intactos en CORE-CLOSE-001.
Los 198 contrastes estáticos y negativos no prueban Compose/Firebase. El informe
CORE_CLOSURE_AUDIT distingue 13 métodos Android PASS, dobles y borrador rojo.

## CORE-FINAL-001 — decisiones de revisión, contexto y reintento

Se conserva el spec/tool anterior en CORE-FINAL-001/before-audit. Cinco fuentes
completas revierten sólo los cambios funcionales aprobados antes de todos los
oráculos anteriores; las fórmulas, originales y aserciones rojas se conservan.
Se registran las adaptaciones de API/fixtures, pruebas estrictas nuevas y reparación
del extractor de orden obsoleto. Las dos caracterizaciones que exigían pérdida o
segundo UUID conservan sus aserciones como antecedentes Ignore, sin contarlas PASS.
Este auditor sigue siendo estático: el cierre funcional usa JUnit/SDK por separado.

## RET-006 / SEA-001 — retirada e incorporación funcional

Se conserva el spec/tool anterior y cada expectativa CORE-FINAL-001. La capa
exterior declara bytes anteriores y actuales de cada cambio funcional aprobado;
comprueba hashes actuales y sólo invierte esa diferencia para ejecutar los
oráculos conservados. No presenta las retiradas ni temporadas como extracción
conservadora. Las pruebas nuevas reales SDK/Compose tienen resultados separados
en FUNCTIONAL_CLOSURE.md y evidencias RET-006/SEA-001. El auditor estático no
compila, no envía, no prueba Firebase/Compose ni acredita cierre funcional.
Dos controles negativos privados rechazan precio alterado y aserción debilitada.
