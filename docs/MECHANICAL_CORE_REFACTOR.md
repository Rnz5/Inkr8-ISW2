# IMPL-007–009 — extracciones conservadoras y revisión AUD-002

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

**Continuación 06/10/2026 — separada de IMPL-007–009:**
Los módulos mecánicos originales y evidencias se conservan. A05-CON/A06-CON
corrigen integración aprobada, no se contabilizan como simples extracciones.
A07-CORE separa matching verbatim (SRP/ALT-13) y registra aparte las guardias
atómicas funcionales; fórmulas sin cambios. DEC-RES-001 habilita refresco del
mismo Results y cinco métodos posteriores PASS. Código/revisión del agente,
sin intervención manual nueva exigida por DEC-AI-AUTH-001. Ver
[trazabilidad vigente](CORE_IMPLEMENTATION.md); no cierre integral.

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


**VER-001 — 05/10/2026:** revisión separada de IMPL-007–009 sin defecto nuevo de extracción; [ejecución y límites](EXECUTION_READINESS.md). Productor rating typechecked y call-sites compilados con TS 7.0.2 disponible: 1.156 pares/2.312 deltas sin diferencias; control UNKNOWN rechazado TS2345. Gradle/JDK del arnés operativos; Android/Functions/Firebase/R8 siguen sin fuentes/configuración auténticas. [Lista única para el equipo](ORIGINAL_ENVIRONMENT_REQUEST.md). Decisión humana: conservar texto/borrador hasta persistencia confirmada; conexión de confirmación UI pendiente según P1 §2.7. Espera: orientación general recibida, sin regla nueva asumida; asociación cuenta/ejercicio y Ranked pendientes. Sin nuevas suites JVM/auditor repetidos ni cierre integral.



05/10/2026, America/Lima. Cambios locales de Codex en `codex/refactorizacion-por-bloques`,
HEAD `7d879c22030527e1d5f5003ad84b76b750d3b29e`; sin cambios del índice, publicación,
despliegue o datos. IMPL-001–006, RET-001, AUD-001, originales y baseline preservados.
El mensaje humano posterior autoriza explícitamente evaluar e implementar las
extracciones mecánicas conservadoras. No se exige copiar código para acreditar autoría.

| Bloque | Aplicado / motivo concreto | Antes → después comprobado | Lo que sigue abierto |
|---|---|---|---|
| IMPL-007 / A05 parcial | Admisión → `evaluation/WritingAdmission.kt`. La restricción textual puede revisarse/probarse independientemente del editor; ALT-01-1 acotada, DG-01, CT-01/03/14, AC-01/03/07. | 20 → 28 PASS JVM; función de producción, 8 casos de lecturas/nullable/excepciones y 240 comparaciones con el predicado previo. Writing completo reconstruido al revertir la extracción. | Observación/recomposición Compose y evento real envío/borrador/Firebase sin ejecutar. Política de recuperación/contexto Q-D no definida. |
| IMPL-008 / A06 parcial | Decisiones escalares de espera → `viewmodel/ResultWaitPolicy.kt`. Se pueden probar sin cargar el ViewModel/SDK; ALT-07-1 acotada, CT-04/06/15, AC-03/07. | 15 → 15 PASS JVM; 5 métodos prueban política de producción, 10 siguen caracterizando clave/guardia/placement. ViewModel completo reconstruido. | Identidad sigue consultando último envío; errores/cancelación/cuenta/placement real y contrato del consumidor requieren decisiones/verificación propias. Sin interfaz o integración nueva. |
| IMPL-009 / A07 parcial | `calculateDynamicRatingChange` → `utils/dynamicRatingChange.ts`, literalmente con export. Fórmula contrastable sin importar trigger/Firebase/R8; ALT-13-1 acotada, CT-08/10/11. | 21 → 21 PASS Node; todos llaman a producción directamente, dos además comprueban la expresión de mínimo cero extraída. Motor completo reconstruido; ambas llamadas siguen dentro de la transacción. | R8/configuración y transacciones/concurrencia/idempotencia no ejecutados. El motor aún coordina evaluación y efectos; separación parcial, no DG-13 resuelto. |
| AUD-002 | Ampliación legítima del auditor tras cambios de producción; fuente de expectativas = expresiones y archivos completos de Git HEAD. | 135 contrastes estáticos PASS y 4 controles negativos rechazados en copias: límite admisión, timeout, fórmula, efecto transaccional. 4 archivos Kotlin sin errores sintácticos PSI. | Auditoría independiente y compilación/aceptación integral pendientes. PSI no comprueba tipos ni Compose. |

## Conservación y revisión separada

A05 conserva `remember` sin nuevas claves y `derivedStateOf`. La llamada se evalúa
dentro del mismo cálculo: primero texto; con blanco no se leen límites/conteo; con
texto no blanco se lee mínimo, conteo sólo si mínimo no nulo, máximo, conteo sólo
si máximo no nulo. Se evalúan ambos límites aunque falle mínimo; `>=`/`<=` intactos.
El lambda `{ wordCount }` evita evaluación anticipada y conserva la lectura de
estado dentro de derivedStateOf. No se afirma haber ejecutado el seguimiento
de snapshots Compose. Conteo, normalización, filtro, calidad, mensajes, fábrica,
limpieza del borrador antes del callback y vaciado posterior siguen literalmente iguales.

A06 conserva la guardia con cortocircuito `!loadingResolved && !loadingTimeout`,
las lecturas de placement y sus efectos. Sólo sustituye cuatro expresiones por
llamadas puras: contador Int `pollCount * 3`, timeout `> 90`, igualdad EVALUATED/FAILED.
Sin alterar delay 3000, orden registro/cancelación, callbacks o navegación; primer
contador expirado sigue siendo 93, no una medición garantizada de 93 segundos reales.
No se corrige identidad por ID, error silencioso o NOT_EVALUABLE en esta refactorización.

A07 conserva tipos/inputs, clamp, Math.round (incluido redondeo negativo), mínimos,
techos, DRAW y cuerpo íntegro de la fórmula. La extracción añade export/import;
no mueve lecturas, cálculos de llamadas, writes o efectos fuera de ninguna transacción.
No unifica ghost/ratingCalculator ni toca Merit, sesiones, poda o temporadas.
El módulo nuevo no tiene imports/IO/estado. Su resolución dentro del build original
de Functions todavía requiere ese entorno: Node aislado no demuestra compilación del producto.

La revisión posterior a implementar examinó fuentes completas, consumidores de
cada función (Writing: 1; espera: 4 llamadas en los mismos métodos; rating: 2 llamadas
en match), tipos visibles, orden, guardias y límites de SDK. No se detectó regresión
en el alcance comprobado. La separación beneficia SRP/testabilidad; no se declara
DIP/OCP/LSP/ISP cumplido ni violado por inferencia. El contraargumento de conservar
coordinadores pequeños sigue válido: por eso se mantienen todos los efectos y no
se introducen interfaces, Strategy, Specification o un nuevo modelo de estados.

Dos automatizaciones se detuvieron por precondiciones antes de terminar: un import
de Gamemode supuesto explícito era wildcard en Writing; la cadena extract_core.py
aparecía dos veces en Gradle. Se corrigieron los marcadores y se continuó sólo con
la transformación exacta del snapshot. No hubo fallos de tests; ambos incidentes
son de herramientas Codex, no correcciones funcionales ni aportes del estudiante.

## P1 y autoría real

Fuente exacta conservada: P1, pp. físicas 6–7, §§2.6/2.7 (`P1_pages_6_7.txt`).
§2.6 exige «análisis de impacto» y mejorar sin alterar comportamiento. §2.7 reserva:
«Los algoritmos que resuelven el problema principal del proyecto deben ser escritos
por el estudiante»; la conexión Backend/Frontend debe ser manual y BD/relaciones
definidas por el alumno. La reserva no dice que toda función, condición o traslado
literal requiera reescritura manual. Esta interpretación corrige el bloqueo general
anterior, conservando sus registros como antecedentes.

Estas intervenciones trasladan decisiones ya existentes; no crean un algoritmo
crítico, no diseñan BD/relaciones, no establecen una arquitectura final ni conectan
frontend/backend nuevos. Su evaluación académica exige comprensión/revisión humana,
no copiar la edición de Codex. P1 §2.3 mantiene el juicio arquitectónico estudiantil,
§2.5 la validación/ejecución humana de casos. No se les atribuye cumplimiento ahora.

**Aporte humano registrado:** encargo, límites y corrección de interpretación;
autorización explícita de canSubmit bajo conservación. La frase previa «sigue la mejor
opcion posible» no se convierte en razón arquitectónica del equipo.
**Aporte Codex:** análisis de impacto, decisión de alcance mecánico, todo el código,
adaptación/oráculo/casos, ejecuciones, revisión y documentación. No se acredita
edición, algoritmo, integración, aceptación ni ejecución individual humana.
Las alternativas estructurales mayores siguen propuestas mientras falte elección
justificada; no condicionan estas transformaciones autorizadas.

## Dependencias concretas para seguir

- A05: decidir con ejemplos qué texto/contexto debe recuperar un borrador tras
  persistencia fallida/cambio de cuenta o ejercicio (Q-D). El tratamiento actual
  permanece; cambiarlo sería corrección funcional/integración aparte.
- A06: definir el expected para envío A y llegada de B, resultado tardío y error,
  y la frontera del consumidor de espera. La consulta actual sigue siendo «último».
  Si se adopta seguimiento por ID/contrato nuevo, definir/conectar con el estudiante
  y probar la persistencia real; `loadLatestSubmission` de Results es otro consumidor.
- A07: contrato/fuente R8 y escenarios humanos de atomicidad, repetición y concurrencia
  para los escritores actuales; no inventar nuevas fórmulas/placement/locks. Juicio
  arquitectónico y cualquier algoritmo nuevo crítico, BD/relaciones o conexión nuevos
  requieren aportes propios según las reglas citadas.
- Entorno auténtico: settings/build Gradle raíz/app; scripts y jar wrapper de producto
  (properties 9.1.0 ya existe); Google Services auténtico en app y plugin/configuración
  que genera `default_web_client_id`; functions/package.json, lock y tsconfig;
  functions/src/r8/evaluateWithR8.ts y configuración local de su secreto; Firebase
  configuración/reglas/entorno de prueba. Las búsquedas anteriores no aportaron esos
  archivos. No se reconstruyen desde las versiones del arnés ni del frontend Website.
- RET-001 sigue parcial; retirada backend/otras pantallas pendiente separada, sin
  reabrir la aprobación ni borrar históricos. Q-R/Q-T/Q-D/Q-C conservan asuntos abiertos;
  temporadas no implementadas. No hay producto completo o aceptación integral acreditados.

## Ejecución reproducible y revisión

Desde el checkout, con JAVA_HOME = JDK Temurin auténtico 17.0.20.1+1:

```powershell
.\testing-baseline\gradlew.bat -p testing-blocks/writing-characterization clean test --no-daemon --offline
.\testing-baseline\gradlew.bat -p testing-blocks/core-contract-characterization clean test --no-daemon --offline
```

Desde testing-blocks/core-contract-characterization, Node comprobado v24.20.0:

```powershell
node extract_rating.mjs
node --test --test-reporter=junit src/test/node/rating.test.mjs
```

Desde el checkout, Python disponible y salida nueva (no sobrescribir evidencia):

```powershell
python testing-blocks/local-refactor-audit/verify_local_refactor.py --repo . --report-dir C:/ruta/nueva/audit
```

Los XML, comandos, consolas, manifiestos y fuentes antes/después están en
`docs/evidence/IMPL-007-009`. El auditor anterior/spec y resultado AUD-001 se conservan;
AUD-002 cambia las fuentes permitidas sólo para declarar las extracciones demostradas,
mantiene comparación completa y rechaza las cuatro alteraciones. No ajusta expected
para aceptar cambios de regla. Auditoría independiente: revisar diff incremental,
oráculo/snapshots y XML; ejecutar suites y auditor en copia/checkout local.
Con originales auténticos, usar tareas/scripts de producto efectivamente existentes,
compilar y comprobar Writing/persistencia/borrador, A/B/timeout/errores/cancelación/cuenta,
placement, match/ghost/transacciones/repetición/R8 en entorno de prueba acordado.
No se proporciona un comando de build Android/Functions inventado.
