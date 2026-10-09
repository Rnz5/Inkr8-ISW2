# VER-001 — revisión separada y alcance de ejecución

**Vigente AND-002 — 06/10/2026:** WHPX/AVD operativos y pantallas ejecutadas en
demo-inkr8-local. 14 métodos Android únicos: 13 PASS y 1 FAIL real (isPlaced no
llega al modelo); tres contrastes centrales antes/después PASS; cinco escenarios
transaccionales iguales y entrega automática local/callable PASS, R8 HTTP doble.
No código productivo nuevo; soporte/fixtures corregidos. Borrador anticipado,
resolución por B, idempotencia/carrera y payload gamemode siguen como defectos o
contratos pendientes. Ranked/rating/Merit permanecen. [Resultados, APK, límites y
aportes concretos](ANDROID_EXECUTION.md). No cierre integral ni autoría estudiantil.

05/10/2026, America/Lima. Continuación de IMPL-007–009/AUD-002. Código de producción,
baseline y evidencias anteriores preservados. Esta revisión no vuelve a contar
los 135 contrastes estáticos como pruebas funcionales ni cierra A05–A07.

## Revisión de las extracciones

| Extracción | Comprobación independiente y resultado | Riesgo pendiente de ejecución |
|---|---|---|
| WritingAdmission | Una importación y un consumidor productivo en Writing:26/132, dentro de remember/derivedStateOf conservados. Firma interna String/Gamemode/()→Int. El texto se lee antes de entrar; el lambda se crea sin leer count. Blank no lee límites/count; nullable limita cada lectura; mínimo y máximo se calculan antes del &&. No cambian tokens, conteo, mensajes ni el evento. | No hay runtime Compose/SDK auténtico para observar snapshots o recomposición. Remember sigue capturando el contexto actual como antes; no se corrige su política. |
| ResultWaitPolicy | Cuatro consumidores AppViewModel:298/299/315/323; mismo paquete, sin importar SDK. Int/count y status no nullable (Submissions:13) coinciden con firmas. Guardia, lectura de status en cada rama, estados, registro listener/cancelación/sondeo, placement y navegación conservados. | No se ejecutan corrutinas, cancelación/cuenta, callbacks ni consulta Firestore. Identidad y errores mantienen el comportamiento existente. |
| dynamicRatingChange | Import relativo único motor:8 resuelve al módulo nuevo; dos llamadas motor:452/458 conservan argumentos y ternarios. Tx empieza antes de leer ratings y llamar; escrituras/efectos siguen después dentro de la misma transacción. Módulo sin imports/estado/IO y cuerpo literal. | Falta grafo/configuración Functions original, SDK, R8 y prueba de transacciones/concurrencia. No se demuestra consistencia distribuida por la extracción. |

La revisión nueva comparó consumidores/firmas y límites con fuentes vigentes,
manifiestos y resultados preservados. **No encontró defecto nuevo de extracción.**
Los riesgos de Compose, SDK, identidad, reloj y transacciones siguen siendo límites
de ejecución; no se etiquetan como regresiones reproducidas de IMPL-007–009.

## Comprobaciones realmente ejecutadas

1. Comprobación de tipos del archivo **de producción** dynamicRatingChange.ts con
   TypeScript 7.0.2 existente en caché: pasa. Compilación de copia byte idéntica y
   dos expresiones de llamada reales bajo su import relativo: pasa. Se comprueba
   correspondencia contra Git `7d879c2`, no una fórmula reescrita para el test.
2. Ejecución del resultado compilado para **1.156 pares / 2.312 deltas** comparados
   con la función anterior: cero diferencias. Incluye umbrales, fracciones y bordes
   numéricos JS, que no se convierten en nuevas reglas/datos válidos de Ranked.
3. Control negativo: outcome UNKNOWN rechazado por el compilador **TS2345**;
   no se acepta un fallo debido a dependencia ausente como control de tipos.
4. Node --check de motor y productor: sintaxis válida; no resuelve/ejecuta imports
   Firebase/R8. Gradle auténtico --version --offline: **8.14.5**, launcher JDK
   **17.0.20.1+1**. Sólo arnés disponible; wrapper de producto continúa en 9.1.0.
5. Búsqueda adicional de originales: otra ref ISW2 sin configuración de producto,
   cero releases/forks y cero matches evaluateWithR8 bajo Rnz5; tres ZIPs docentes
   anidados sin candidatos. Git fsck sin objetos inaccesibles. Ningún original importado.

`strict`, ES2020 y CommonJS son opciones de **la prueba aislada**, no configuración
original. El compilador disponible no identifica la versión usada por Functions.
Se intentó npm exec offline para 5.9.3; falló por metadatos de caché ENOTCACHED.
Se usó después el compilador 7.0.2 ya instalado, sin instalar dependencias ni generar
package/lock/tsconfig del producto. Todas las ejecuciones son Codex, no pruebas individuales humanas.

No se repitieron suites JVM 28/15 ni los 21 casos Node ni el auditor de 135 checks:
no cambió su código productivo y no había hallazgo que justificara repetirlos.
La comprobación nueva agrega tipos, resolución del import aislado y ejecución de
llamadas reales; no agrega una simulación de SDK ni una transacción de reemplazo.

## Estado de ejecución por capa

| Capa | Disponible / observado hoy | Qué falta para ampliar |
|---|---|---|
| Núcleo JVM aislado | Wrapper/JDK/cache reales; resultados 28/15 de etapa anterior preservados, launcher comprobado ahora. | Estos arneses no ejecutan Writing/Compose ni ViewModel/DraftManager/Firebase. Words depende de Firebase Timestamp, por lo que ampliar Factory/modelos completos necesita SDK original. |
| Rating TypeScript aislado | Tipos, import/call-sites compilados, 1.156 pares ejecutados y control negativo pasan. Nueva herramienta check_rating_consumers.mjs reproducible. | Target/module/version de producto y efectos del motor no verificados. |
| Android/Compose | Fuentes, wrapper properties 9.1.0 y recursos recuperados; sin cambios en esta etapa. | Settings/build/scripts/jar originales, SDK/plugin/dependencias/Firebase. No SDK localizado en ruta predeterminada/variables; sin AndroidX/Compose en caché consultada. No APK/render/recomposición ejecutados. |
| Functions completo | Código del motor y firmas visibles; parseo nativo pasa. | package/lock/tsconfig originales, SDK/imports y R8; no compilación/carga del producto ni triggers ejecutados. |
| Firebase / R8 | Índices existentes y contratos parciales documentados. | Configuración/reglas/entorno de prueba auténticos y evaluador. No autenticación, lectura/escritura, emulador ni evaluación real ejecutados. |

La lista **única y agrupada** para solicitar fuentes al equipo está en
[ORIGINAL_ENVIRONMENT_REQUEST.md](ORIGINAL_ENVIRONMENT_REQUEST.md). Incluye archivos,
destinos y criterios de correspondencia; no presenta sustitutos como originales.

## Decisiones recibidas y corrección funcional del borrador

Una sola consulta agrupada preguntó borrador, espera y Ranked usando H1/AC/S10/S22.
Marco respondió sobre persistencia: **«debe conservarse texto y borrador hasta un
guardado confirmado»**. Queda decidido ese comportamiento; no se vuelve a preguntar.
No respondió a asociación por cuenta/ejercicio. Sobre espera respondió **«como esta
planteado actualmente, guiate por el mejor camino»**: orientación para continuar,
sin duración/notificación/reintento/A-B específicos nuevos. La refactorización
mantiene el comportamiento existente; no se registra una propuesta IA como regla
del equipo. Ranked sigue sin respuesta específica al cierre.

**Incompatibilidad con la nueva decisión, comprobada por fuente:** Writing:342/344/345 limpia DraftManager antes de onAddSubmission
y vacía userText después de ese callback Unit, sin recibir confirmación. AppRoot:84–87
sólo comunica error. AppViewModel:140/165–173 inicia espera al éxito, sin devolverlo
al editor. Repository:19–34 dispone de addOnSuccessListener/addOnFailureListener.
No se declara pérdida observada en Compose/Firebase: el orden fuente contradice
el comportamiento ahora solicitado y todavía debe reproducirse con entorno real.

**Intervención humana concreta para habilitar la corrección:** el estudiante define
y realiza la conexión de la confirmación de addSubmission hacia el editor a través
de submitWriting/AppRoot. P1 §2.7, página física 7: «La conexión entre el Backend y
el Frontend debe ser manual para demostrar que entienden el flujo de datos».
Aplica aquí porque hay que añadir una señal de persistencia que la UI todavía no
recibe. No aplica como requisito de copiar/repetir la extracción mecánica anterior.
Codex puede revisar su diff, adaptar pruebas y hacer ajustes mecánicos después;
no atribuye una conexión generada automáticamente al estudiante.

La intervención debe conservar rechazo de calidad, payload/ID, errores/mensajes
y navegación actuales, y mantener texto/borrador ante error o sin confirmación.
La limpieza se asocia al guardado confirmado; comprobar éxito, fallo, ausencia de
respuesta y cambios durante el envío. Asociación cuenta/ejercicio y conducta ante
edición simultánea requieren expected concreto antes de implementarse. No se añade
regla de bloqueo, reintento, identidad o migración por recomendación automática.

Pendientes mínimos restantes: asociación de borrador por cuenta/ejercicio; precisión
de A/B/tardío/error/reintento conforme a H1 (mensaje e intención ya recuperados);
fallback Standard y controles no económicos de Ranked. No se reabre retirada de
Merit/ligas ni se añade moneda o castigo por abandono. Temporadas separadas/Q-T abierto.

## Repetir y revisar

Desde el checkout, Node disponible y la ruta **real** a TypeScript ya instalado:

```powershell
node testing-blocks/core-contract-characterization/check_rating_consumers.mjs --tsc C:/ruta/typescript/lib/tsc.js --report-dir C:/ruta/nueva/rating-check
```

El script falla si se reutiliza un directorio de evidencia, cambia el cuerpo previo,
los call-sites o import declarado, fallan tipos/runtime, o el control no falla con
TS2345. `result.json` guarda hashes, versiones, flags y comandos; no reproduce SDKs.
Revisar el diff de la herramienta/README y los logs en evidencia VER-001, junto con
la lista de originales. Con originales entregados, contrastar commit/configuración,
usar tareas/scripts auténticos, compilar por capa y ejecutar escenarios UI/Firebase
con expected humano y conexión estudiantil. No se inventa un comando de build integral.

Cambios de esta etapa: herramienta de verificación y registro; **cero cambios de
producción, cero reglas nuevas, cero defectos productivos corregidos automáticamente**.
Se identificó una corrección funcional ya elegida para el borrador, pendiente de
la conexión/aporte indicado y verificación. Refactorización, retiradas y temporadas
no están completas; revisión actual no sustituye auditoría independiente o sustentación.
