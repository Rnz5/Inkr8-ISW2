# A05–A07: decisiones y preparación de la intervención

**Vigente — IMPL-007–009 / AUD-002, 05/10/2026:** extracciones mecánicas locales de admisión, decisiones escalares de espera y fórmula de rating; [registro y límites](MECHANICAL_CORE_REFACTOR.md). Pruebas pertinentes antes/después: Writing 20→28 PASS, espera 15→15, rating 21→21; auditor 135 PASS y cuatro controles negativos rechazados. Corrección explícita: P1 no exige copiar código para cualquier extracción. Todo código/pruebas es Codex; aporte humano = encargo/límites/autorización, sin atribuir elección arquitectónica o implementación estudiantil. A05–A07 sólo parcialmente abordados; entorno/integración/decisiones funcionales pendientes. Los estados anteriores que siguen son históricos.



05/10/2026, America/Lima. Apoyo de Codex solicitado en este chat. Checkout `codex/refactorizacion-por-bloques`, HEAD `7d879c22030527e1d5f5003ad84b76b750d3b29e`. IMPL-001–006, RET-001, baseline y evidencia anterior preservados. Esta etapa prepara las intervenciones: no implementa lógica crítica, no adopta alternativas por el estudiante y no cierra A05–A07.

La tabla reutiliza DG-01/07/13, ALT-01/07/13, CT-01–16 y AC-01/02/03/07. Las líneas son del checkout actual; las matrices originales siguen citando C1. P1 §2.3 exige juicio arquitectónico estudiantil; §2.7 reserva algoritmos críticos, BD/relaciones e integración manual. OCP/LSP/ISP no se fuerzan sin evidencia de variación, sustitución o consumidores perjudicados.

| Bloque | Responsabilidades y evidencia actual | SOLID posible y contraargumento | Alternativa mínima documentada y conservación | Decisión y aporte estudiantil faltantes | Pruebas aprovechables y comprobación adicional |
|---|---|---|---|---|---|
| A05 | `Writing.kt:117–137`: contar, reconocer y admitir; `315–349`: calidad, fábrica, filtro y limpieza/envío; `85–95`: cargar/autoguardar. `DraftManager.kt:23–25`: clave. | SRP: restricciones y recuperación tienen motivos distintos de cambio. El editor puede coordinar legítimamente eventos; validar también en servidor es defensa necesaria. | ALT-01-1: separar decisiones existentes reutilizando validación/fábrica y conservar el evento. ALT-01-0: mantener coordinación; ALT-01-2 necesita combinaciones reales. Conservar límites inclusivos, tokenización, omisiones actuales y orden del borrador. | Elegir opción y fragmento, justificar un cambio independiente y aportar manualmente lógica/conexión. Cambiar clave o limpieza requiere decisión funcional distinta. | 20 casos IMPL-002/003; claves en IMPL-005. Tras el cambio: equivalencia de fragmentos y evento real de rechazo/payload/borrador/errores. |
| A06 | `AppViewModel.kt:165–168`: alta inicia espera sin ID; `278–327`: listener/sondeo/estado/navegación; `460–468`: limpieza. `FirestoreSubmissionRepository.kt:137–172`: último envío/cuenta/SDK. | SRP/DIP: política de espera depende de concreto y `ListenerRegistration`. El ViewModel puede ser adaptador deliberado; el repositorio ya encapsula consultas/mapeo. Constructor concreto no demuestra DIP. | ALT-07-1: separar decisiones locales conservando conexión; ALT-07-2: contrato pequeño desde el consumidor, si se justifica; ALT-07-0: mantener adaptador. Conservar estados, navegación y contador observado. Seguir por ID sería corrección funcional separada. | Elegir frontera y consumidor; definir/conectar manualmente el contrato si se adopta. Especificar aparte identidad/corrección si se decide abordarla. | IMPL-005: 15 casos JVM compartidos con A05, no 15 por bloque. Tras el cambio: A/B, callbacks tardíos, errores, cancelación/cuenta, timeout y orden de placement con infraestructura auténtica. |
| A07 | `submissionEvaluationEngine.ts:216`: R8; `237–369`: transacción de evaluación; `420–550`: match/cálculo/efectos; `477–539`: rating. | SRP: proveedor, cálculo y persistencia tienen motivos distintos de cambio; DIP sólo con necesidad de consumidor probada. Un trigger coordina efectos y debe conservar atomicidad; rating ya está delegado parcialmente. | ALT-13-1: separar un cálculo conservado identificado, con entradas/salidas explícitas; ALT-13-0: mantener coordinación; ALT-13-2 requiere contrato R8 auténtico. Conservar redondeo, mínimos, orden y límites de transacción. | Identificar cálculo e invariantes, justificar independencia y aportar manualmente algoritmo/integración. No elegir reglas Ranked/temporadas ni retirar Merit dentro de esta refactorización. | 21 casos Node IMPL-005 para rating y mínimo cero. Tras el cambio: equivalencia del cálculo elegido y, con entorno auténtico, repetición/concurrencia/transacciones/error R8. |

## Consumidores revisados y consecuencias concretas

Se inventariaron referencias literales en 105 fuentes Kotlin/TypeScript. Es inspección de fuentes, no resolución de tipos ni traza de ejecución. El inventario guarda ruta, línea, texto y SHA-256 de once fuentes relevantes.

- **A05:** `DraftManager` tiene cuatro referencias, todas en Writing: clave (`63`), carga (`87`), guardado (`94`) y limpieza (`346`). No se propone trasladar almacenamiento a la función que decide admisión. `onAddSubmission` devuelve Unit (`44`); AppRoot lo conecta con `submitWriting` (`84–87`). El editor limpia el borrador antes del callback y vacía el texto después (`346–349`), antes de conocer el resultado de Firestore. El comentario «successful submission» no prueba éxito de persistencia. Preservar ese orden es refactorización; corregir recuperación sería otro cambio.
- **A06:** la fábrica genera UUID (`SubmissionFactory.kt:20`); el ViewModel copia autor/estado/evaluación sin cambiar ID (`140–145`); el repositorio escribe en `document(firestoreSubmission.id)` (`29–34`). La identidad existe en el alta, pero `startLoadingResult()` no la recibe. `getLastSubmission` tiene dos consumidores además de su declaración: sondeo (`303`) y `loadLatestSubmission` (`239–243`). AppRoot utiliza este último en **Results sin resultado cargado** (`164–174`); no es el historial. Una corrección de seguimiento debe acotarse al consumidor de espera y mantener explícito el contrato del otro consumidor.
- **A06:** la cuenta se resuelve implícitamente en cada consulta (`Repository:141/155`). Sin usuario, la consulta termina sin callback y el listener retorna null. Errores de espera sólo se imprimen (`ViewModel:289/307`). El contador aumenta de tres en tres y expira al superar 90 (`296–301`): 93 segundos nominales del contador, no medición garantizada de tiempo real. Estas son observaciones del antes, no decisiones humanas de error/reintento/timeout.
- **A07:** `calculateDynamicRatingChange` tiene una declaración y dos llamadas, ambas dentro de la transacción de match (`24`, `477`, `483`). Las lecturas de ambos usuarios (`464–475`) preceden al cálculo; las escrituras conservadas (`489–550`) siguen dentro. Extraer la función ya pura, sin mejorar una dependencia concreta, no basta para resolver DG-13.
- **A07:** `ghostMatchProcessor.ts:25–57` calcula el rival promedio y deltas distintos, con lecturas antes de su transacción (`33–59`, `61–95`). No es consumidor de `calculateDynamicRatingChange`. Unificar fórmulas o trasladar lecturas cambiaría comportamiento y exigiría intervención funcional/estudiantil separada.
- **A07:** `utils/ratingCalculator.ts:1–12` declara otra fórmula, `calculateNewRating`. No se encontraron llamadas ni imports en las fuentes revisadas. No se introduce como sustituto por su nombre; tampoco se elimina basándose sólo en esta búsqueda.

## Consulta única y estado humano

Una consulta agrupada A05/A06/A07 se presentó en el chat, con opciones ALT-01-0/1/2, ALT-07-0/1/2 y ALT-13-0/1/2, sus consecuencias y solicitud de fragmento, razón y aporte manual. **Sin respuesta registrada al cierre de esta preparación.** Continuar el trabajo local no equivale a elegir alternativas. Ninguna opción aparece como aprobación del equipo ni se atribuye código automático a estudiantes.

El registro posterior de cada intervención habilitada deberá distinguir: problema → justificación SOLID → elección/razón humanas → aporte manual auténtico → apoyo Codex → comprobación antes/después → límites. No hay bloque crítico cerrado en esta etapa.

## Búsqueda adicional de originales

Se amplió la búsqueda anterior, sin repetir sus inventarios locales:

- Consultas al historial de `Rnz5/Inkr8-Android` para 15 rutas concretas de settings/build root/app, propiedades/scripts/jar, Firebase, package/tsconfig y R8: ninguna devolvió commits para esas rutas. Referencias disponibles: una rama `main`, sin tags. Es búsqueda por esas rutas, no prueba de ausencia en todas las máquinas/backups o bajo otros nombres.
- `Rnz5/Inkr8-Website`, commit `0f2f567c947c9ecc6f8efe81c9cb43d177392cd8`, árbol `5ba89bffdc3d8eb8e5b08854ab2d4ed551d2a567`, completo: 45 archivos de frontend React/Vite. `package.json`/lock pertenecen a ese frontend; no se importan como configuración Functions ni se deducen versiones.
- No existen las carpetas adicionales `C:/Users/marco/AndroidStudioProjects`, `StudioProjects`, `source/repos`, `Projects` e `IdeaProjects`. Se comprobó existencia; no se afirma inspección de contenido de carpetas ausentes.

No se recuperaron nuevos archivos originales. Siguen haciendo falta fuentes del proyecto auténtico en estas ubicaciones del checkout:

| Ubicación | Aporte original necesario |
|---|---|
| Raíz / `app/` | settings/build Gradle originales, plugins y dependencias; propiedades/catálogo si los utiliza el original. |
| Raíz / `gradle/wrapper/` | scripts `gradlew`/`gradlew.bat` y jar del wrapper. **properties 9.1.0 ya existe**; no sustituirlo por el wrapper del arnés JVM. |
| `app/` / configuración Firebase local | Configuración Google Services auténtica y plugin que generan `default_web_client_id`; entorno de prueba y acceso del equipo, sin imprimir ni publicar secretos. |
| `functions/` | package, lock y tsconfig originales; no los del frontend web. |
| `functions/src/r8/evaluateWithR8.ts` | Fuente/contrato auténticos del evaluador y configuración local de su secreto; no inventar implementación R8. |
| Raíz / Firebase | Configuración/reglas de acceso auténticas. Los índices existentes no reconstruyen el entorno. |

## Verificación preparada, todavía pendiente de ejecución

Los arneses JVM existentes, JDK y Node están disponibles. Los comandos documentados en sus README permiten repetir **sólo la suite pertinente cuando haya cambios**; un extractor que deje de reconocer un fragmento requiere revisar el alcance, no sustituir el algoritmo por otro en el test. Las versiones del arnés no definen versiones Android/Functions.

1. Registrar elección/justificación y ubicación del aporte manual; fijar fuente antes del cambio.
2. Comparar el cálculo/decisión seleccionado con el antes caracterizado. Reutilizar la suite correspondiente sin sumar ejecuciones anteriores como casos nuevos.
3. Con fuentes originales recuperadas, comprobar tareas Gradle/scripts npm auténticos, compilar y conectar manualmente en un entorno de prueba. No ejecutar comandos de producto inventados.
4. Verificar evento Writing/persistencia/borrador; espera A/B/estados/errores/timeout/cancelación/cuenta/placement; motor/match humano/ghost/transacciones/repetición/errores R8 contra expected humano. Las reglas no especificadas siguen abiertas.

**Ejecutado en esta etapa:** inventario de consumidores, búsqueda adicional, comprobación de disponibilidad e integridad de evidencia y diferencias documentales. **Cero pruebas funcionales/JVM/Node nuevas**: no hubo cambios de producción que justificaran repetirlas. No se acredita APK, renderizado Compose, Firebase/R8, aceptación integral, cumplimiento individual ni aplicación integral de SOLID.

Evidencia canónica de esta preparación: `C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/SOLID-PREPARATION-2026-10-05`. [Estado](STATUS.md), [registro](IMPLEMENTATION_LOG.md) y [plan](CONSERVATIVE_BLOCK_PLAN.md) distinguen trabajo realizado y dependencias. RET-001 y temporadas conservan sus carriles separados. Sin datos reales, stage/commit/push, publicación o despliegue.
