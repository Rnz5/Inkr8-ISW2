# Registro de implementación por bloques — Inkr8

## FIN-002 — cierre local y entrega revisable

Problema: dos sondas Android FIN-001 no alcanzaban aserciones; faltaba entrega/publicación.
Fuente: suite roja preservada y consumidores reales, decisiones/prompt actual de Marco.
Cambio: esperar respuesta/history y editor; fixture Standard válido; API Compose correcta;
batch≤450 writes. Producción Android intacta. Token de Philosopher retirado de log,
placeholder y economía intactos. Inicializer FIN-001 reconocido con delta exacto.
Pruebas: 6 Android PASS; operación14 PASS; nombre atómico6 PASS; token1/1roja→2verde;
Functions build y lab build PASS; auditor285 estáticos, no funcionales.
Resultados: textos/borrador preservados bajo permisos reales; historial propio callable
con colección administrada denegada. Reporte13 capítulos P4 y UML/métodos finales.
Autor: Codex. Permisos: DEC-AI-AUTH-001 y publicación explícita actual, sin merge/deploy.
Límites: rules nuevas, R8 doble, no cloud/OAuth/Ads/billing; compra previa placeholder,
antiabuso servidor/cierre de cuenta fuera del perfil básico y evidencias académicas reales.
Ver FINAL_DELIVERY.md, delivery/README.md y evidencia FIN-002; historia/baseline preservados.

**AND-001 — ejecución Android y comprobación ampliada, 05/10/2026:** SDK oficial API 36/Build Tools 36.0.0 instalado tras aceptación humana «claro». Android compila con Gradle/AGP/versiones recibidas intactas, APK debug firmado generado. 16 métodos pertinentes del módulo real + 1 ejemplo PASS; incluyen dos casos de snapshots Compose, no render/recomposición de pantallas. Cuatro escenarios del motor Ranked en Firestore local reales antes/después dan resultados idénticos; R8 doble y CloudEvent sintético. Auth local REST operativo, sin Google OAuth. Emulador Android sin controlador de aceleración, intento por software permanece offline: cero pantallas ejecutadas. Auditor 158 contrastes estáticos, expectativas anteriores conservadas. Sin cambio de producción, reglas, integración, retiradas o temporadas. [Resultados, reproducción y bloqueo](ANDROID_EXECUTION.md).


**Alcance posterior SCP-001 — registro 05/10/2026:** Marco comunica una nueva captura/mensaje de Renzo: «Ranked y Merit permanecen»; «Torneos, ligas y reputación se retiran». La imagen original y su fecha no están adjuntas al contexto disponible; no se infiere aprobación grupal ni fecha de subida. Esta instrucción humana posterior sustituye la retirada total de Merit de U3/DEC-03 para el trabajo vigente. Se conservan Ranked/rating/Merit y las reglas económicas existentes; no se inventan usos, precios o recompensas nuevos. Temporadas permanece incluida con reglas pendientes. RET-001–005 y sus evidencias describen el alcance anterior y no se reescriben. [Recuperación y restitución](RECOVERED_ENVIRONMENT.md).

**REC-002/SCP-001 — resultado local:** fuentes/configuración recuperadas sin sobrescribir los 193 archivos previos; Functions compila con TS 5.9.3/Node 20 antes y después de IMPL-009, 21 escenarios rating PASS sobre JS del build completo. Seis casos del evaluador auténtico con SDK real y transporte simulado PASS como caracterización; uno reproduce NaN previo, no aceptación. Android primer build real falla por SDK ausente antes de compilar Kotlin/Compose. Restituidos tres indicadores RET-001 de Merit, wallet propio Profile, controles de capacidad Settings y textos de racha Home; sin tocar ligas/reputación/reglas/backend. PSI 0 errores antes/después; auditor actualizado con fuente humana y hashes auténticos: 155 contrastes estáticos, no pruebas funcionales. Resultados/fallos iniciales y evidencias anteriores conservados. [Detalle, comandos y dependencias](RECOVERED_ENVIRONMENT.md).


04/10/2026 · America/Lima · chat 13 · U19/DEC-26. Registro asistido por Codex; revisión, justificación de adopción e intervención estudiantiles pendientes. El permiso de trabajo local no acredita aprobación del diseño ni autoría del alumno.

## IMPL-001 — reutilización de patrones del filtro cliente

**Estado:** cambio local aplicado y verificado en Kotlin/JVM aislado. Sin commit, push, PR, merge o despliegue de este bloque. No declara producto completo ni AC cerrado.

**Base exacta:** `7d879c22030527e1d5f5003ad84b76b750d3b29e`, igual a HEAD y master remotos consultados. Origin: `https://github.com/Rnz5/Inkr8-ISW2.git`. Rama local: `codex/refactorizacion-por-bloques`. La diferencia desde C1 `64983846d6dbf4cdfafcdd508464cd140a2a862e` hasta la base son exclusivamente 13 altas documentales; app/src/main, functions/src y arnés originales son idénticos. Checkout persistente separado bajo checkout/Inkr8-ISW2 en coordinación; copia histórica C1 intacta.

**Problema y fuente:** `ValidationUtils.isContentLowQuality` recompilaba los literales `\s+` y `[^a-zA-Z]` en cada llamada que alcanzaba split/replace. DG-01 y CT-03 del análisis local identifican el filtro cliente usado por Writing (HU 2.1/2.2, 3.14–3.16; BR-03). Esta intervención no ratifica el diagnóstico SRP ni selecciona ALT-01 o un patrón.

**Propuesta IA y alternativas:** dos propiedades privadas inmutables del objeto reutilizan las mismas Regex. Conservar la compilación por llamada tiene menos desplazamiento y sería suficiente si su coste fuese irrelevante; una nueva abstracción añade alcance sin necesidad comprobada. El ensayo local de reutilización está dentro de U19. No hay decisión estudiantil específica de adopción registrada. No se mide ni se afirma ganancia de latencia.

**Cambio:** sólo ubicación de compilaciones y referencias en split/replace. El método mantiene firma, entrada String, salida Pair<Boolean, String?>, trim, opciones Regex por defecto, tokenización, filtro ASCII, algoritmo, orden, umbrales y mensajes. La compilación ahora ocurre una vez al inicializar el objeto, incluso si la primera entrada es corta; son dos literales válidos. No se guardan Matchers ni texto entre llamadas. P1 reserva lógica crítica, BD/relaciones e integración: no se escribe una solución del evaluador ni se cambia conexión o política; este aporte IA no se atribuye al estudiante.

**Pruebas:** arnés auténtico Kotlin/JVM 2.4.20 + JUnit 4.13.2, Gradle Wrapper 8.14.5; JDK Temurin 17.0.20.1+1 comprobado con java/javac. Caché existente y modo offline. Desde la raíz del checkout con JAVA_HOME del JDK:

```powershell
.\testing-baseline\gradlew.bat -p testing-baseline clean test --no-daemon --offline
```

| Ejecución | Fuente del filtro | Suites | Casos | Fallos/errores/omitidos | Resultado |
|---|---|---:|---:|---|---|
| Antes | Original, sin cambio de producción; nueva suite ya añadida | 7 | 38 | 0/0/0 | PASS, proceso 0 |
| Después | Reutilización de las dos Regex | 7 | 38 | 0/0/0 | PASS, proceso 0 |

Los 22 nombres históricos coinciden con sus seis XML custodiados. La séptima suite añade 16 métodos JUnit: límites 49/50, trim, palabra 35/36, espacios ASCII/NBSP/comas, repetición 9/10 y 0.35, mayúsculas, letras 30/31 y 60/61, vocales 0.15/0.8, diversidad 7/8, Unicode, prioridad de errores, llamadas sucesivas y 200 llamadas concurrentes. Expected caracteriza el antes, incluyendo entradas numéricas aceptadas por el filtro; no ratifica su idoneidad funcional. Los mismos casos y la misma fuente de tests se usaron en ambas ejecuciones. 200 invocaciones son un escenario dentro de un método, no 200 tests.

**Evidencia nueva:** carpeta de coordinación `docs/evidence/IMPL-001`, con propuesta, base/origin/entorno/disponibilidad, snapshots del filtro, consola, XML y HTML completos de cada ejecución, comparación, integridad y diff. Los 17 archivos originales de arnés/documentación de baseline y 156 archivos previos protegidos de coordinación conservan su SHA-256. DOC-001–012, fuentes originales y seis XML históricos no fueron reescritos. La task Gradle marcada SKIPPED es un control de configuración, no una prueba omitida: XML skipped=0.

**Límites:** sólo siete fuentes Kotlin del sourceSet JVM; no se construyó Android, TypeScript/Functions ni se ejecutó Firebase/R8, UI, persistencia o integración. La configuración original y evaluateWithR8 siguen ausentes. El filtro del servidor sigue intacto y su equivalencia con cliente no se demuestra. Ni cobertura integral, S01–S25, aceptación completa, autoría/pruebas individuales, arquitectura final o cumplimiento académico quedan acreditados.

**Siguiente bloque propuesto (IA, sin ejecutar ni adoptar):** A02, caracterización acotada del evento de envío de Writing para distinguir canSubmit, filtro de calidad y wordsUsed (DG-01/CT-03). Registrar qué fragmento merece extracción y qué inputs permanecen iguales; el estudiante debe justificar e intervenir si afecta lógica crítica o integración. El contrato auténtico R8 condiciona comprobar su evaluación. No completar Standard/Ranked, fórmulas, temporadas o compatibilidad por inferencia.

Q-R/Q-T/Q-D/Q-C, historia ISW1/ISW2 y revisión/elección/intervención académicas siguen pendientes. Retiradas aprobadas y temporadas continúan separadas como cambios funcionales posteriores; no se borraron datos ni se tocaron servicios reales.

## IMPL-002 / A02 — caracterización del envío y reutilización de Regex en Writing

04/10/2026 · continuación humana: «continua con el siguiente bloque». Asistencia IA; autorización local vigente, sin adopción estudiantil de diseño o elección funcional acreditadas.

**Base:** HEAD/origin/master comprobados en 7d879c22030527e1d5f5003ad84b76b750d3b29e, rama codex/refactorizacion-por-bloques, sobre el estado local de IMPL-001 preservado. Writing antes coincide byte por byte con la copia C1; los parches de A02 se calculan sobre snapshots del estado inicial de este bloque.

**Problema/fuente/propuesta:** Writing recompilaba los mismos literales \s+ y \W+ en sus expresiones derivadas de conteo y normalización (DG-01/CT-03; HU 2.1/2.2, 3.14–3.16). Se reutilizan como dos propiedades privadas inmutables en el mismo archivo. Conservar las compilaciones evita desplazamiento; reutilizarlas evita repetir ese trabajo sin añadir una capa; extraer decisiones de admisión/payload exige una justificación distinta e intervención estudiantil si afecta lógica crítica/integración. No se elige ALT-01 ni patrón/arquitectura final, no se mide latencia.

**Cambio:** dos declaraciones y dos referencias; firma, algoritmos, reglas, flags Regex, lowercasing, conteos, filtro, palabras enviadas y orden del evento intactos. La compilación ocurre al inicializar WritingKt, incluidos otros entry points del mismo archivo; son patrones literales válidos. No se guardan Matchers ni datos de entrada. Las fuentes de IMPL-001 y del backend permanecen intactas.

**Verificación nueva:** [arnés aislado](../testing-blocks/writing-characterization/README.md), derivado del wrapper/versiones/dependencias auténticos sin modificar testing-baseline. La task extractWritingSources extrae literalmente cuatro fragmentos de Writing, con líneas/hashes, y falla si sus marcadores u orden de efectos cambian. Compila esos fragmentos en envolturas de prueba junto con Gamemodes, Theme, Topics y ValidationUtils auténticos. SelectedWordFixture sólo proporciona id/word para el predicado: no es Words, DTO o serialización. La pantalla Android no se compila.

```powershell
.\testing-baseline\gradlew.bat -p testing-blocks/writing-characterization clean test --no-daemon --offline
```

| Ejecución A02 | Métodos JUnit | Fallos/errores/omitidos | Proceso |
|---|---:|---|---:|
| Antes del cambio en Writing | 20 PASS | 0/0/0 | 0 |
| Después del cambio en Writing | 20 PASS | 0/0/0 | 0 |

Los mismos veinte nombres y la misma suite se mantienen; canSubmit y wordsUsed tienen hashes de fragmento idénticos. Revertir sólo las dos compilaciones/referencias reconstruye todos los bytes iniciales de Writing. Se contrastó el orden del evento estáticamente; no se ejecutó el callback o borrador. El salto de líneas por las declaraciones queda registrado en SOURCE_MANIFEST.

**Hechos observados en fragmentos:** los rangos 50–150 y 50–200 son inclusivos en los modos auténticos; canSubmit puede ser true con texto que luego rechaza ValidationUtils; un texto aceptado por el filtro puede incumplir el rango; las palabras asignadas ausentes no cambian canSubmit y quedan fuera de wordsUsed. Espacios no separables, apóstrofes, comas, guiones bajos, dígitos y caracteres Unicode producen diferencias entre conteo y coincidencia. El filtro conserva el orden y duplicados de la lista seleccionada. Son caracterización del antes, no criterios ratificados ni correcciones del producto.

**Límites:** expresiones escalares y modelos Kotlin seleccionados, no Compose/recomposición/remember/derivedStateOf, Firebase, Words auténtico, SubmissionFactory, borradores, DTO, backend o R8. Sólo los predicados del envío y el orden estático fueron comprobados; no hay serialización ni garantía de entrega. La lectura TypeScript confirma que requiredWords deriva de wordsUsed, pero no se ejecutó ese backend ni se observó la puntuación del evaluador ausente. No cierra CT-03 integralmente, S01–S25, AC o rúbrica individual. Los 38 PASS de IMPL-001 corresponden a su ejecución anterior conservada, no a una suite A02 de 58 casos ni una nueva ejecución Android.

**Evidencia:** nueva carpeta de coordinación docs/evidence/IMPL-002, con base/origin/entorno, mensaje humano, propuesta, snapshots, fragmentos/manifest, consola/XML/HTML completos, comparación e integridad. Se conservaron todos los archivos previos protegidos (228 en coordinación y 136 del repo), incluido IMPL-001/DOC-001–012/baseline, sin sobrescribir evidencia antigua. Sólo se mantuvieron los cuatro documentos canónicos previstos y se añadió este arnés. Cambios locales sin commit, push, PR, merge, despliegue o datos reales.

**Dependencia para el bloque siguiente:** justificar con intervención estudiantil si conviene extraer las decisiones ya caracterizadas de Writing o conservarlas ahí. La corrección del payload completo de restricciones es trabajo diferente, crítico/de integración; necesita criterio humano y contrato R8 auténtico antes de cambiar qué se envía o penaliza. Q-R/Q-T/Q-D/Q-C, configuración, autoría/validación/intervención y aceptación integrales pendientes. La autorización de continuación no los resuelve.

## IMPL-003 / A03 — separación de componentes informativos de Writing

04/10/2026, America/Lima · continuidad local sostenida solicitada por Marco en este chat. Propuesta, cambio, revisión técnica y ejecución de Codex; no se atribuyen a estudiantes. Base HEAD 7d879c22030527e1d5f5003ad84b76b750d3b29e, rama codex/refactorizacion-por-bloques; cambios anteriores sin commit preservados.

**Problema y evidencia:** Writing reunía el evento del editor con cinco componentes de información y estilo ya autónomos. La lectura DG-01/E01 distingue lo mostrado de lo decidido; CT-03/14 obliga a conservar envío/borrador. Este bloque permite trabajar sobre estilos/textos de esos componentes sin editar el archivo del evento. No resuelve el candidato crítico de admisibilidad.

**Alternativas/impacto:** conservar inline/archivo único minimiza desplazamiento; una frontera local de presentación reduce el área que se necesita revisar para cambios visuales. No se añaden clases, contratos de BD, adaptadores o patrones. No se fragmenta cada Text y no se tocan componentes de funciones retiradas. P1 §2.7 permite componentes aislados; la adopción, análisis crítico estudiantil y validación/ejecución exigidos por §§2.5/2.6 siguen pendientes. No es selección automática de ALT-01/07/13.

**Cambio:** Se trasladaron DirectiveCard, LexiconChip, WordInfoDialog, ThemeInfoDialog y TopicInfoDialog a WritingComponents.kt en el mismo paquete, con nombres y firmas públicos intactos. Se conservaron literalmente los cinco componentes y las funciones Writing/WritingPreview al normalizar sólo CRLF/LF; los finales de línea del archivo original se mantienen. El editor, derivedStateOf/remember, callbacks, filtros, payload, DraftManager, repositorio y orden del envío permanecen en Writing sin cambios. Se movieron 186 líneas de presentación (conteo descriptivo, sin afirmar mejora medida de calidad/rendimiento). [Plan y dependencias](CONSERVATIVE_BLOCK_PLAN.md).

**Pruebas reales:** La misma suite auténtica de IMPL-002 registró **20 PASS antes y 20 PASS después, 0 fallos/errores/omitidos y proceso 0**. Las expresiones extraídas y el orden estático del envío se verifican; no se ejecutó pantalla, callback o SDK. Consola, XML, HTML y SOURCE_MANIFEST nuevos están en runs/fase/writing_regression. La inspección Kotlin PSI 2.4.20 tuvo 0 errores de sintaxis antes/después y confirmó identidad de los siete textos de función y sus tokens, sin duplicación de definiciones. Son veinte métodos de regresión, no pruebas UI adicionales.

**Límites/riesgos:** sin build Android original, resolución de tipos/imports, plugin Compose, renderizado, recomposición, instrumentación, Firebase/backend o R8. La extracción añade fronteras de composición que deben revisarse visualmente con entorno auténtico. El traslado de funciones públicas de Writing cambia su clase JVM de archivo (WritingKt → WritingComponentsKt); el código Kotlin conserva nombres/paquete, pero consumidores binarios previos requerirían recompilar. No se localizaron consumidores Java/fachada de estas funciones en el checkout. No se declara aceptación AC-03/CT integral, S01–S25, compatibilidad externa ni producto completo. Sin publicación/despliegue/datos reales.

**Integridad:** 298 archivos previos de coordinación y 142 del repositorio protegidos sin cambios, incluida la evidencia histórica y de IMPL-001/002. Evidencia nueva IMPL-003: snapshots, base, herramienta/entorno, inspección, comparación, parche y verificación; no se reescriben resultados anteriores. README/STATUS/DECISIONS/IMPLEMENTATION_LOG y plan se mantienen como estado actual, sin reescribir diagnóstico histórico.

**Pendiente concreto:** A05 requiere decisión estudiantil sobre conservar/extraer admisibilidad o reconocimiento, su razón independiente de cambio y aporte manual; A06 requiere elección del contrato de espera e integración manual; A07 exige cálculo conservado/invariantes y lógica crítica escrita por estudiante. R8/configuración y reglas Q correspondientes condicionan aceptación integral. Retiradas y temporadas continúan en carriles separados.

## IMPL-004 / A04 — separación de puntuación y feedback de Results

04/10/2026, America/Lima · continuidad local sostenida solicitada por Marco en este chat. Propuesta, cambio, revisión técnica y ejecución de Codex; no se atribuyen a estudiantes. Base HEAD 7d879c22030527e1d5f5003ad84b76b750d3b29e, rama codex/refactorizacion-por-bloques; cambios anteriores sin commit preservados.

**Problema y evidencia:** Results reunía la coordinación de efectos de pantalla con dos regiones cohesionadas de score/feedback. AC-03 y HU 2.3/2.4 conservan resultados inmediatos. Separar ambas regiones permite revisar presentación sin editar guardias o efectos; no altera el cálculo ni la fuente del score. DG-13/ALT-13 se refieren al motor y siguen pendientes: este bloque no los sustituye.

**Alternativas/impacto:** conservar inline/archivo único minimiza desplazamiento; una frontera local de presentación reduce el área que se necesita revisar para cambios visuales. No se añaden clases, contratos de BD, adaptadores o patrones. No se fragmenta cada Text y no se tocan componentes de funciones retiradas. P1 §2.7 permite componentes aislados; la adopción, análisis crítico estudiantil y validación/ejecución exigidos por §§2.5/2.6 siguen pendientes. No es selección automática de ALT-01/07/13.

**Cambio:** Se trasladaron los dos bloques inline de puntuación y tarjeta feedback a ResultsScoreSummary y ResultsFeedbackCard, funciones Compose internal en ResultsComponents.kt. Evaluation ya existente es el parámetro; feedbackToShow sigue derivándose en Results. Expresiones y tokens de ambos cuerpos coinciden con los bloques anteriores. Reinsertar los bloques originales en las dos llamadas reconstruye todo Results previo (LF); sus finales de línea existentes se mantienen. La guardia evaluation nula, analytics/LaunchedEffect/Toast, preview, contenido enviado, navegación y bloque mixto match/Merit/rating quedan intactos. Se movieron 57 líneas de presentación (conteo descriptivo, sin afirmar mejora medida de calidad/rendimiento). [Plan y dependencias](CONSERVATIVE_BLOCK_PLAN.md).

**Pruebas reales:** Inspección Kotlin PSI 2.4.20: **0 errores de sintaxis antes/después**; tokens de ambos bloques iguales a los cuerpos nuevos y ResultsPreview idéntico. La restauración literal reproduce Results anterior y su estructura íntegra. No se añadieron tests JUnit que repitan el traslado ni se volvió a ejecutar una suite JVM ajena a la UI. La revisión constató los mismos umbrales 95/90/80/70/60, literales, FormatUtils.formatPercentage, feedback recibido y mock, modificadores, dimensiones y orden entre Spacer. No se compilaron ni ejecutaron estas funciones Compose; la igualdad de tokens no demuestra comportamiento visual/recomposición.

**Límites/riesgos:** sin build Android original, resolución de tipos/imports, plugin Compose, renderizado, recomposición, instrumentación, Firebase/backend o R8. La extracción añade fronteras de composición que deben revisarse visualmente con entorno auténtico. El traslado de funciones públicas de Writing cambia su clase JVM de archivo (WritingKt → WritingComponentsKt); el código Kotlin conserva nombres/paquete, pero consumidores binarios previos requerirían recompilar. No se localizaron consumidores Java/fachada de estas funciones en el checkout. No se declara aceptación AC-03/CT integral, S01–S25, compatibilidad externa ni producto completo. Sin publicación/despliegue/datos reales.

**Integridad:** 383 archivos previos de coordinación y 143 del repositorio protegidos sin cambios, incluida la evidencia histórica y de IMPL-001/002. Evidencia nueva IMPL-004: snapshots, base, herramienta/entorno, inspección, comparación, parche y verificación; no se reescriben resultados anteriores. README/STATUS/DECISIONS/IMPLEMENTATION_LOG y plan se mantienen como estado actual, sin reescribir diagnóstico histórico.

**Pendiente concreto:** A05 requiere decisión estudiantil sobre conservar/extraer admisibilidad o reconocimiento, su razón independiente de cambio y aporte manual; A06 requiere elección del contrato de espera e integración manual; A07 exige cálculo conservado/invariantes y lógica crítica escrita por estudiante. R8/configuración y reglas Q correspondientes condicionan aceptación integral. Retiradas y temporadas continúan en carriles separados.

## Cierre de esta continuación conservadora local — 04/10/2026

A03/IMPL-003 y A04/IMPL-004 aplicados y revisados a nivel de fuentes. IMPL-001/002 y sus evidencias/entregas anteriores conservan hashes; la regresión Writing se volvió a ejecutar sólo por su traslado (20 PASS antes/después). Results tiene comprobación de sintaxis/estructura, sin ejecución Compose. No se suman ejecuciones repetidas como cobertura adicional de la aplicación.

Los cuatro candidatos estructurales críticos documentados no se declaran resueltos por particionar UI. A05 (DG-01) necesita elección/conservación justificada por estudiante y aporte manual; A06 (DG-07) necesita contrato de espera/identidad e integración manual; A07 (DG-09/13) necesita reglas/fuente R8 y lógica crítica humana. Se consultó la elección A05 durante el trabajo, sin respuesta registrada al cierre; no se interpreta silencio como decisión. La ausencia de builds Android/Functions, recursos/configuración auténticos, R8 y fixtures impide comprobación integral. Retiradas funcionales y temporadas no implementadas en esta continuación. Q-R/Q-T/Q-D/Q-C y aceptación integral siguen pendientes.

Trabajo independiente acotado de presentación realizado; siguientes intervenciones estructurales del núcleo dependen de esos aportes. No se declara refactorización completa, funcionamiento integral, cierre AC/S01–S25 o crédito individual. Sin commit/stage/push, publicación, despliegue ni operaciones de datos/servicios.

## IMPL-005 — caracterización previa acotada de A05–A07

04/10/2026 · apoyo Codex a A05–A07 dentro de la continuación humana sostenida. **No resolución de bloques centrales ni cambio de producción.** Base HEAD 7d879c2/rama codex/refactorizacion-por-bloques confirmados; todas las fuentes/evidencias previas protegidas.

**Problema/fuente/decisión:** falta elección estudiantil ALT-01/07/13 y aporte manual P1 §2.7. Consulta agrupada por fragmento, opciones, consecuencias y aporte enviada, sin respuesta registrada. CT-14 pide caracterizar clave/borrador; CT-06/10 estados/tiempo/placement; CT-11 rating/delta. La autorización habilita tests sobre lógica existente, no reemplaza la decisión o integración manual.

**Cambio permitido:** arnés nuevo testing-blocks/core-contract-characterization. JVM extrae literalmente getDraftKey, guardia/predicados/contador/timeout/placement de fuentes auténticas y compila SubmissionStatus real; fixtures sólo dos campos placement o enum status, sin Users/Submissions/SDK. Node extrae calculateDynamicRatingChange existente y expresión de mínimo cero; elimina sólo tipos TS mediante runtime Node 24.20.0 disponible, sin type checking/build Functions. Wrappers de prueba no son módulos nuevos del producto ni relaciones BD.

**Pruebas reales:** prototipo JVM 16 PASS; revisión elimina una tautología, y versión final ejecutada 15 PASS (0 fallos/errores/omitidos). Node 21 PASS, proceso 0, una sola ejecución; metadatos de su reporter se corrigieron sin repetir casos. No se repitieron los 38/20 casos anteriores ajenos. Fragmentos y hashes idénticos entre las ejecuciones JVM; todas las fuentes de producción intactas. Comandos, XML/HTML y manifiestos en evidencia IMPL-005.

**Hechos acotados:** clave conserva modos y separadores; null y literal none coinciden, vacíos y mayúsculas se conservan. Account/topic no están en esa firma; no se probó reanudación real. Guardia no acepta resolved/timeout; EVALUATED/FAILED se distinguen de PENDING/NOT_EVALUABLE; contador 30→90 aún no vence y 31→93 vence, sin medir tiempo real. Placement depende de tres flags recibidos, sin demostrar orden Firebase. Rating: fronteras 150/180, clamp ±5, redondeo JS, DRAW +1 y delta no necesariamente igual a variación cuando mínimo cero. Estos expected caracterizan el antes y no ratifican Q ni fórmulas.

**Búsqueda ejecutada:** raíces locales/ZIP de Drive y candidatos, copia previa C1, historial local no shallow y árboles públicos ISW2/Android/Content. Sin builds originales/R8/Firebase completo. Wrapper properties producto 9.1.0 existe, incompleto; no confundir con arnés 8.14.5. Se encontraron 20 recursos originales de Android, con 72/72 blobs compartidos iguales (incluido manifest); restauración separada IMPL-006 siguiente. No se reconstruyen versiones ni se reemplaza R8.

**Resultado/dependencia:** apoyo de caracterización listo, extracción central pendiente de decisiones/aportes agrupados. No callbacks/coroutines/SharedPreferences/Firebase/transacciones/integración, UI, idempotencia o concurrencia probados. P1 pide validación/ejecución estudiantil y autoría auténtica; no se acredita con ejecución Codex. Retiradas/temporadas separadas y Q abiertos. Sin datos/servicios/publicación/despliegue.


## IMPL-006 — recuperación literal de recursos Android originales

04/10/2026 · fuente Rnz5/Inkr8-Android, commit 9ff46a1e690ec8e3b8b79b64da2e47134dd26c15. **20 recursos ausentes restaurados; entorno integral aún incompleto.** Aporte Codex dentro de la búsqueda/restauración local autorizada, sin elección estudiantil de arquitectura o crédito individual.

**Problema y fuente:** manifest y fuentes Kotlin referencian Theme.Inkr8, XML de backup, iconos y avatares que faltaban. Dos recursos ya existentes no se sustituyen. El árbol fuente recuperado no está truncado; los 72 archivos compartidos (incluido AndroidManifest.xml) tienen exactamente el mismo blob Git que HEAD 7d879c2/C1 de producción. No se asume compatibilidad sólo por el nombre del repositorio.

**Decisión/cambio:** recuperar literalmente archivos originales ausentes; no crear estilos/iconos alternativos ni deducir versiones. Descarga de blobs inmutables por GitHub API, validación SHA-1 de objeto Git/tamaño y SHA-256, copia de originales en evidencia y adición local de 20 recursos: defaultpng/r8pfp, iconos y capas launcher, colors/themes y backup/data_extraction XML. No sobrescribe strings.xml/pfpexample.png ni código. P1 §2.7 permite configuración inicial/apoyo acotado; BD, lógica crítica e integración permanecen reservadas.

**Pruebas/comprobaciones reales:** antes siete IDs ausentes; después esos siete disponibles por inventario de archivos/XML y XML bien formado. Continúa ausente string/default_web_client_id, dependiente de configuración/plugin Firebase auténticos. Todos los bytes importados coinciden con sus blobs; archivos previos protegidos intactos. Parche binario/documental git apply --check y aplicación en copia reproducen el resultado. No se repiten suites JVM/Node ajenas a recursos.

**Límites/pendientes:** sin AAPT2/APK, resolución de plataforma/librerías, renderizado Compose ni ejecución de backup/Firebase/Functions/R8. Faltan settings/build root y app, versiones/plugins/dependencias originales, scripts/jar root wrapper (properties 9.1.0 existe), configuración Firebase original, functions/package/lock/tsconfig y evaluateWithR8. La restauración resuelve la ausencia de recursos, no habilita por sí sola el producto ni cierra A05–A07/AC/S01–S25. Consulta estudiantil agrupada sigue pendiente. Retiradas/temporadas no implementadas, sin datos/publicación/despliegue.


## RET-001 — retirada de indicadores económicos/clasificación en cabecera y resultados

04/10/2026 · **retirada funcional local y parcial de presentación**, separada de IMPL-001–006 y A05–A07. Decisiones humanas existentes U2/U3/D1, DEC-02/03/05: Merit completo, ligas y clasificación global fuera; perfil propio, rating y resultados inmediatos conservados. No se reabren esas retiradas ni se inventa una moneda/regla sustituta.

**Problema/fuente:** UserHeaderCard mostraba Liquid Merit/capacidad/hold y League/Pantheon; Results mostraba Merit Gain en una tarjeta mixta con rating/lexicon. REFACTOR_SCOPE/AC-03 identifica retirar esas presentaciones sin eliminar perfil propio/resultados/rating. Componentes aislados/maquetación son apoyo permitido P1 §2.7; no se conecta frontend/backend.

**Cambio:** se eliminan sólo esos widgets y el separador decorativo asociado. Se conserva nombre/avatar/callback y badge Philosopher ya existente; no se redefine su política. Firmas públicas y parámetro pantheonPosition se mantienen para no modificar conexiones actuales; su cálculo/listeners aún requieren retirada de integración manual. Results conserva score/feedback, null guard, analytics/Toast, match/rating/placement/Pending/calibración/lexicon y navegación. Preview/modelos/datos Merit históricos no se borran. Es conducta deliberada de presentación; no se declara equivalencia visual con el antes ni refactorización central resuelta.

**Comprobación real:** Kotlin PSI 2.4.20, 0 errores de sintaxis antes/después. El diff consta sólo de siete regiones UI borradas; la comparación del resto de fuentes y firmas conserva lo indicado. No nuevos tests triviales ni repetición de suites JVM/Node ajenas. Parche aplicado en copia reproduce el resultado. Sin plugin Compose, type checking o ejecución UI/Firebase; revisión visual/funcional pendiente con entorno original.

**Pendientes:** esta retirada no desactiva cobros/escrituras/cálculos Merit, reputación o rutas de torneos, ni limpia modelos/BD/datos históricos. Competitions mezcla selección por League con applyMeritAction/entrada, por lo que su retirada exige integración estudiantil y regla no económica Q-R. Perfil detallado/reveal/conexiones y backend todavía requieren su bloque de retirada; temporadas Q-T aparte. A05–A07 siguen esperando consulta/aporte auténtico, sin crédito individual, publicación/despliegue/datos reales.


## Cierre de continuación A05–A07 y entorno — 04/10/2026

IMPL-005 añadió apoyo ejecutado (15 PASS JVM y 21 PASS Node) sobre claves/decisiones/contador/rating existentes, sin alterar producción ni elegir alternativas por estudiantes. IMPL-006 recuperó literalmente 20 recursos originales; quedan builds/configuración/R8. RET-001 retiró widgets económicos/clasificación sólo en cabecera/resultados, en carril funcional separado; no desactivó economía/backend ni eliminó datos. IMPL-001–004 y todas sus evidencias preservados.

La consulta agrupada sobre fragmento/opción/razón/aporte manual A05–A07 sigue sin respuesta registrada. P1 exige ese aporte auténtico para lógica crítica e integración; no se sustituye con código automático o aprobación inferida. Retiradas integradas requieren decisión Q-R no económica en Competitions y montaje/manual del equipo; temporadas requieren Q-T y compatibilidad Q-D. No se creó diagnóstico general adicional ni se declararon AC/S01–S25/autoría individual cumplidos.

Para comprobar el producto: recuperar settings/builds/plugins/dependencias y wrapper completo del proyecto Android original (properties 9.1.0 ya presente), configuración Firebase que genera default_web_client_id, functions/package/lock/tsconfig y evaluateWithR8; comprobar tareas/scripts auténticos, compilar en entorno de prueba y ejecutar fixtures A/B, estados/error/timeout/placement/cuenta/draft y rating/transacciones contra expected humano. Hasta entonces sólo es reproducible el alcance aislado documentado. Sin stage/commit/push/publicación/despliegue/datos reales.

## Preparación SOLID A05–A07 — 05/10/2026

**Problema/fuente → justificación:** DG-01/07/13, alternativas ALT-01/07/13 y contratos vigentes se contrastaron con el checkout 7d879c2 y sus cambios locales. [Tabla y preparación](SOLID_BLOCK_PREPARATION.md) registra responsabilidades/contraargumentos y conservación. SRP y DIP son hipótesis justificables por bloque; no se fuerzan los cinco principios.

**Decisión humana:** el encargo actual autoriza trabajo local y exige primero tabla/consulta. Se presentó una sola consulta agrupada con opciones, fragmento, razón y aporte manual. Sigue sin respuesta registrada; no existe elección estudiantil nueva. P1 §2.3/2.7 reserva juicio arquitectónico, BD/relaciones, lógica crítica y conexión manual. No se acredita aporte individual.

**Cambio independiente:** revisión de referencias/consumidores en 105 fuentes Kotlin/TypeScript, inventario con líneas y hashes, preparación acotada de verificación, ampliación de búsqueda original. El alta conserva ID pero la espera no lo recibe; la consulta al último también abastece Results vacío y no puede reemplazarse globalmente sin analizar ese consumidor. Match humano y ghost aplican cálculos diferentes; calculateNewRating no tiene consumidores encontrados. No se introduce interfaz, algoritmo, relación de BD ni conexión automática.

**Fuentes adicionales:** 15 consultas de rutas de configuración al historial Android sin resultados; una rama main y sin tags. Website 0f2f567c947c9ecc6f8efe81c9cb43d177392cd8 contiene 45 archivos React/Vite, sin configuración Functions recuperable por sustitución. Cinco carpetas locales habituales adicionales no existen. Cero archivos recuperados; fuentes/configuración Android/Functions/Firebase/R8 originales siguen pendientes. La búsqueda no abarca todas las máquinas/backups.

**Comprobación antes/después y límites:** snapshots/fingerprints previos, integridad de IMPL-001–006/RET-001 y cierre anterior; comparación posterior de fuentes/baseline/recursos/arneses protegidos sin cambios; revisión de enlaces y parche documental aplicado en copia. No hay cambio de producción ni ejecución funcional, JVM o Node nueva. Los 20 casos Writing, 15 JVM compartidos A05/A06 y 21 Node anteriores son evidencia reutilizable, no nuevas pruebas ni aceptación de integración.

**Pendiente por bloque:** A05, selección/justificación y aporte manual sobre decisión existente; borrador funcional aparte. A06, frontera de consumidor/infraestructura y contrato/conexión estudiantiles; identidad/error/timeout deliberados aparte. A07, cálculo conservado e invariantes identificados y código/integración auténticos; R8 original para su adaptador. Las verificaciones adicionales concretas están en la preparación. Ninguno de A05–A07 se cierra. RET-001 y temporadas permanecen separados. Sin datos reales, stage/commit/push, publicación o despliegue.

## AUD-001 — revisión separada y herramienta reproducible — 05/10/2026

**Encargo/respuesta humanos:** autonomía local y revisión final, conservando P1/propuestas/evidencia. A la consulta agrupada se recibió literalmente «sigue la mejor opcion posible». Se registra esa autorización de continuar; no se inventa elección razonada o aporte manual del estudiante. [Revisión y pasos concretos](LOCAL_AUDIT_REVIEW.md). No nuevas consultas por cambios rutinarios.

**Intervención independiente:** tres archivos de auditoría en testing-blocks/local-refactor-audit. Contraste de Git/fuentes originales, reconstrucción de Regex/traslados y retiradas separadas, archivos completos de componentes y 20 blobs de recursos. No se escribe/interconecta un algoritmo crítico, BD o SDK. No se implementa automáticamente la propuesta A05–A07 ni se le atribuye autoría estudiantil.

**Antes/después y comprobaciones:** inicial interrumpida tras 109 contrastes por lectura UTF-8 incorrecta del preparador, preservada. Corregida: 130 PASS estáticos. Revisión del auditor reforzó comparación completa de componentes: 132 PASS estáticos finales, proceso 0. Dos controles negativos en copia: límite >= alterado a > y declaración no revisada en un componente, ambos rechazados exactamente, proceso 1. No modificación de producción. Parches conservador/RET-001 separados y diff acumulado aplicados en copia; auditor ejecutado sobre la copia reconstruida. Reportes/manifests en AUD-001.

**Hallazgos:** dos defectos de la herramienta auxiliar corregidos (codificación y alcance del contraste de componentes). Sin diferencia no declarada en fuentes/productores/consumidores protegidos o recursos. Esto prueba conservación de fuentes, no Compose/render/Firebase/R8. Fachada JVM externa de funciones Writing movidas y tipos/integración siguen sin acreditar. Economía/backend/Philosopher/rutas pendientes son alcance explícito RET-001, no cierre de retirada integral.

**Límites/pendientes:** A05 necesita juicio propio y extracción/aporte manual acotados para cumplir P1; A06/A07 necesitan frontera/cálculo/invariantes y conexión/lógica estudiantiles. La respuesta general no sustituye esos aportes. Configuración/builds/R8 originales siguen ausentes; no se repite su búsqueda ya agotada sin nueva fuente. Cero suites JVM/Node nuevas y cero pruebas de producto integral: evidencia anterior reutilizable intacta, no nuevos PASS funcionales. Refactorización, aceptación y cumplimiento individual no completos. Sin datos, stage/commit/push, publicación/despliegue.

## IMPL-007–009 y AUD-002 — 05/10/2026

Problema → justificación → autorización humana → cambio → comprobación → límites,
registrados en [MECHANICAL_CORE_REFACTOR.md](MECHANICAL_CORE_REFACTOR.md) y
[evidencia nueva](evidence/IMPL-007-009/RESULT.json). IMPL-007 extrae canSubmit
con conteo diferido; IMPL-008 separa cuatro decisiones escalares de espera;
IMPL-009 traslada literalmente rating a módulo sin SDK. No corrige reglas.
Writing 20→28 PASS, espera 15→15 y rating 21→21; auditor extendido 135 PASS,
4 controles negativos rechazados; reconstrucción de tres archivos completos;
PSI 4 archivos sin errores, sin validación Compose/Firebase/transacciones.
P1: ya no se exige edición manual para todo traslado. La intervención de Codex
está declarada; diseño/algoritmos nuevos críticos/BD/conexión reservados mantienen
sus aportes humanos pendientes, que no se acreditan por copiar estos cambios.
Historial, originales, baseline, IMPL-001–006, RET-001 y AUD-001 preservados.

## VER-001 — revisión separada y habilitación de ejecución (05/10/2026)

Problema: extracciones aún no comprobadas en el producto y configuración original
ausente. Fuente: IMPL-007–009/AUD-002, consumidores actuales, registros de búsquedas,
H1/AC/S10/S22 y respuesta humana nueva. Revisión sin defecto nuevo de extracción;
ninguna modificación de producción. Typecheck productor, compilación de llamadas
reales en aislamiento y 1.156 pares/2.312 deltas PASS con TS 7.0.2/Node disponibles;
UNKNOWN rechazado por TS2345. Parseo engine/producer válido y launcher Gradle 8.14.5
JDK17 comprobado. No son pruebas Firebase/R8/transacciones/Compose ni aceptación.
Búsqueda nueva en refs/releases/forks/code search y tres ZIPs docentes anidados:
cero originales importados. Preservadas suites previas, baseline, fuentes y evidencia;
no se vuelven a ejecutar 135 contrastes ni suites sin justificación.

Decisión humana: conservar texto/borrador hasta guardado confirmado; actual evento
contradice ese expected por fuente, no se afirma fallo SDK reproducido. Aporte
indispensable: conexión estudiantil de confirmación backend hacia UI (P1 §2.7 p7),
sin exigir copiar código mecánico. Asociación cuenta/ejercicio y precisiones espera/
Ranked siguen pendientes; respuestas literales y límites en evidencia VER-001.
Codex aporta herramienta, revisión, ejecución y documentos; no autoría/pruebas humanas.
[Resultado y comandos](EXECUTION_READINESS.md), [originales requeridos](ORIGINAL_ENVIRONMENT_REQUEST.md).

## RET-002 — retirada local de presentación — 05/10/2026

**Problema/fuente:** Profile.kt mezcla perfil básico/rating con tarjeta de saldo/capacidad/tasa/hold Merit, reputación/pago de revelación, propinas y contadores de torneos. D1/U2/U3 y REFACTOR_SCOPE/AC-03 ya aprueban esas retiradas. Consumidores AppRoot (perfil propio y consultado) y Preview conservados; SectionTitle también se usa en Settings. Los dos helpers de propinas de Profile son privados; los homónimos privados de TournamentResultsScreen son distintos y permanecen.

**Decisión humana / clasificación:** retirada funcional de presentación aprobada, no refactorización SOLID ni nueva decisión arquitectónica. El encargo actual autoriza cambios locales separables; no se exige copiar ediciones mecánicas. P1 §2.7 permite apoyo de frontend en maquetación/componentes aislados; no se diseña BD ni algoritmo crítico ni se modifica la conexión frontend/backend.

**Cambio Codex:** retirar tarjeta económica, reputación y su revelación/pago, botones/dialogs de propinas y contadores de torneos; quitar el subtítulo League/Pantheon de ambos ratings sin alterar el número. Se conservan firmas/parámetros/callbacks para los consumidores actuales, nombre/avatar/fecha/badge existente, rating, submissionsCount, bestScore, curva, archivo/guardados/settings y sus condiciones de propietario. Se retiran sólo helpers privados de propinas sin consumidores restantes; los componentes compartidos son literales. Se mantiene la leyenda de procedencia de bestScore («Only counts Ranked and Tournaments scores»): quitarla o afirmar «sólo Ranked» ocultaría la semántica de los datos históricos/currentes que no se han recalculado.

**Antes/después real:** Kotlin PSI de archivos originales y modificados, 0 errores de sintaxis. Diez transformaciones fuente declaradas, con dos ocurrencias explícitas del subtítulo; reconstrucción de Profile completo desde HEAD, resto de archivos/componentes y firmas contrastados. Auditor estático 135 PASS antes y después, no tests funcionales. No se repiten suites JVM/Node ajenas; no Compose typecheck/render ni ejecución Firebase. Resultado fuente sin modificación adicional no declarada.

**Límites/pendientes:** los callbacks de compra/propina siguen declarados y conectados en AppRoot, aunque esta pantalla ya no los invoca; rutas y backend histórico no se retiran aquí. Compatibilidad/datos no se eliminan. Aceptación visual del perfil/espaciado y retirada de integración pendientes del entorno y aporte auténticos. Todo código/verificación es Codex; no se atribuye revisión o implementación al estudiante.

## RET-003 — retirada local de presentación — 05/10/2026

**Problema/fuente:** Competitions.kt combina Ranked conservado con tarjetas de creación/feed de torneos, botón de leaderboard global y badge de League. Sus regiones visuales están separadas del selector, entrada y estados Ranked; REFACTOR_SCOPE advierte que no puede eliminarse la pantalla entera.

**Decisión humana / clasificación:** aplicar la retirada de presentación ya aprobada D1/U2/U3, separada de refactorización y temporadas; no elegir Q-R. Apoyo P1 §2.7 de componentes aislados/layout; no cambia relación de componentes ni conexión SDK.

**Cambio Codex:** retirar host/feed de torneos y sus launchers UI, botón leaderboard y badge de liga; quitar sólo el import de TournamentCard que los sustentaba. Mantener firma/Preview, Header/perfil/volver, numeric rating/calibración, lista y estados de envíos recientes, League.fromRating y selección Standard/OnTopic, carga de tema/tópico, RankedCostCalculator, guardia coste, isEnteringRanked, callback/error/analytics de entrada y orden de navegación. No se elimina TournamentCard compartida ni modelos/repositorios/usuarios.

**Antes/después real:** cuatro transformaciones declaradas; Kotlin PSI 0 errores de sintaxis antes/después. Contraste de fuente completa desde HEAD sólo permite esas retiradas; auditor 135 PASS posterior, incluidas las extracciones IMPL-001–009 y recursos auténticos conservados. Firmas/consumidores AppRoot/MainPager intactos. Sin pruebas Compose/Firebase o aceptación Ranked por este contraste.

**Límites/pendientes:** el listener de torneos continúa registrado y se libera como antes, aunque ya no tiene feed visible. Suscripción, callbacks/rutas históricas y backend quedan para la retirada de integración; no se proclama retirada completa ni ahorro de consultas. Cobro/controles/selección ligada a League siguen implementados; retirar Merit no habilita inventar selección/límites no económicos. Intervención estudiantil específica de frontend/backend (P1 §2.7), Q-R y entorno original pendientes.

## RET-004 — retirada local de presentación — 05/10/2026

**Problema/fuente:** Settings ofrece ampliar capacidad Merit con confirmación/coste; Home promete +12%/+3% Merit según racha. Son presentaciones de la economía retirada que pueden separarse de identidad, auth y cálculo de racha.

**Decisión humana / clasificación:** retirada funcional aprobada D1/U2/U3 y encargo actual; no reglas nuevas de racha, precio gratis ni moneda sustituta. Apoyo P1 §2.7 sobre UI aislada, sin algoritmo/BD/integración nuevos.

**Cambio Codex:** eliminar estado/dialog/launcher de ampliar capacidad Merit; conservar firma Settings/onExpandCap y conexiones AppRoot, cambio de nombre y su coste actual, logout/confirmación de borrado y navegación. Quitar únicamente dos sufijos promocionales Merit de streakLabel, conservando condiciones >=7, >=3, ==2, else y resto de texto/días. No se modifica el control/cobro de hints/widgets ni economía backend. La acción de cambio de nombre seguirá mostrando su coste real mientras su backend siga cobrándolo; no se oculta ni se inventa gratuidad.

**Antes/después real:** Kotlin PSI 0 errores en Settings/Home antes/después; cinco transformaciones exactas y comparación completa de ambos archivos desde HEAD. Auditor estático final 135 PASS; el antes reutiliza el reporte inmediato después de RET-003 (fuentes RET-004 entonces intactas), sin repetirlo sin motivo. Consumidores, helpers y funciones/Preview conservados. No nuevos tests triviales o suites JVM/Node ajenas, no Compose/SDK.

**Límites/pendientes:** callbacks/backend/modelos/data económicos aún presentes; P1 reserva conexión frontend/backend al estudiante cuando se retire realmente la integración. Comprobación visual de Settings y cuatro ramas de racha con entorno original pendiente. Código y ejecución de comprobaciones exclusivamente Codex.

**Revisión separada posterior RET-002–004:** cuatro controles negativos en copias rechazados por comparación completa: reputación reinsertada, guardia coste Ranked modificada, callback logout modificado, rama de racha modificada. Diff sin errores con los filtros normales del checkout; fuentes CRLF preservadas. El contraste core.autocrlf=false produce falsos cambios de líneas/CR y no se interpreta como defecto de whitespace. 1.338 archivos de evidencia previa y todas las entregas anteriores preservados por hash, HEAD/index intactos. Fuente no declarada, baseline/configuración/arneses/Functions/recursos/otros componentes intactos. Evidencia RET-002/003 en carpeta evidence/RET-002-003 y RET-004 en evidence/RET-004; scripts/snapshots/comandos/spec anterior/nuevo/resultados/hashes disponibles. Parches incrementales aplicados en copia y comparación de bytes; sin publicación/despliegue/datos/recordatorios.

## RET-005 — retirada de widgets de liga en placement — 05/10/2026

**Problema/fuente:** PlacementRevealScreen todavía muestra nombre de League y siete veredictos ligados a esas categorías; D1/U2/U3/AC-03 ya aprobaron retirar ligas conservando placement. El consumidor AppRoot calcula League.fromRating(currentUser.rating), recibe onContinue y navega como antes; no se altera esa conexión. No hace falta inventar rating o texto sustituto para retirar los dos widgets: ya existe «Calibration Complete».

**Decisión/clasificación:** retirada funcional de presentación aprobada, no refactorización ni elección de nueva regla de placement. Apoyo P1 §2.7 de widgets/layout aislados; no lógica crítica, relación BD ni integración backend/frontend nueva. Codex realiza los cambios/checks; no aporte manual o revisión estudiantil atribuidos.

**Cambio:** borrar sólo el cálculo de veredicto League sin consumidores restantes y los dos widgets nombre/veredicto con sus separadores. Mantener la firma League/onContinue, import/type, título Calibration Complete, estilos existentes de fondo/título/botón, condición showButton y secuencia LaunchedEffect delay(600), showLeague=true, delay(1200), showVerdict=true, delay(900), showButton=true. Las variables/animaciones de presentación previas se conservan aunque sin widgets consumidores; su limpieza no es necesaria para esta retirada y no se usan para afirmar ahorro de trabajo. La conexión AppRoot y su derivación de League quedan como deuda de integración separada; no nueva llamada ni texto/número de rating inventado.

**Antes/después real:** Kotlin PSI 0 errores en la fuente antes/después; dos regiones declaradas reproducen el archivo completo desde HEAD, contrato y orden/valores de la secuencia conservados. Auditor final 135 contrastes estáticos PASS, usando el reporte inmediato RET-004 como antes sin reejecutarlo. Dos controles negativos en copias —onContinue sustituido y delay(900) alterado— rechazados exactamente. Sin tests JVM/Node ajenos, plugin Compose, render, reloj real, aceptación visual, Firebase/R8 o autoría humana acreditados.

**Límites:** sólo título y botón conservados quedan visibles en esta pantalla; la disposición/espera debe revisarse con Compose original. La sustitución por información nueva de rating u otra presentación requeriría elección/contrato y aporte de conexión; no se implementa por suposición. No retirar aún el parámetro League o estado/listener/route en AppRoot, no eliminar datos/modelos compartidos. Temporadas/Ranked/cobros y confirmación de borrador independientes.

## REC-001 — recuperación pendiente de ubicación de originales — 05/10/2026

**Encargo humano:** Renzo confirmó que subió originales; priorizar recuperación y ejecución con fuentes auténticas, preservando cambios/evidencias. La confirmación no se interpreta como paquete recibido o completo.

**Preservación previa:** 193 archivos tracked/untracked y cambios locales archivados con comprobación de bytes; historial Git completo en bundle privado verificado por Git. HEAD 7d879c22030527e1d5f5003ad84b76b750d3b29e, rama codex/refactorizacion-por-bloques e index vacío conservados. Copia privada en work/REC-001-private-backup del workspace de este chat; hashes/metadatos en evidence/REC-001. IMPL-001–009, RET-001–005, auditorías, baseline, 1.527 archivos de evidencia anterior y entregas previas protegidos.

**Fuente nueva comprobada:** API autenticada del repositorio oficial, ramas y últimos ocho commits al 05/10/2026. Master sigue 7d879c2; rama documental sigue 1951443; no PR abiertos. No nueva versión de producción/configuración identificada, no fetch/pull/reset/stash/clean ni imports realizados. Consulta nueva motivada por la confirmación humana, no repetición de búsquedas históricas.

**Drive registrado en SOURCE_INDEX:** carpeta 00 - Contexto IA y Refactorización accesible en navegador autenticado. 05 - Código y repositorio contiene su LEEME (1 oct) y REPOSITORIO_GITHUB.txt (2 oct); 04 - Proyecto ISW2 - Estado actual contiene su LEEME y el informe final Word (1 oct). Sin paquete/configuración nuevo visible en esas ubicaciones. Consulta web inicial no accesible, navegador sí; no se recorren archivos docentes/ZIPs/historial/otras cuentas ya agotados. Ningún contenido de credenciales impreso/copied/publicado.

**Inventario solicitado en el checkout:** de las rutas de ORIGINAL_ENVIRONMENT_REQUEST sólo wrapper properties producto y firestore.indexes.json están presentes; permanecen ausentes configuración/scripts/jar Gradle Android, package/lock/tsconfig Functions, configuración/reglas/entorno Firebase y evaluateWithR8. Alternativas/archivos opcionales no se tratan todos como requisitos obligatorios. Cero originales nuevos recuperados, cero modificaciones productivas/configuración.

**Comprobaciones realmente nuevas:** integridad del snapshot local/ZIP/bundle y metadatos upstream/Drive, presencia de rutas solicitadas. No nuevas compilaciones Android/Functions, emuladores, tests de núcleo/Compose/SDK o R8: repetir intentos sin los archivos no ampliaría su alcance. Se conserva evidencia y resultados anteriores; no se contabiliza preparación como validación funcional.

**Dependencia exacta / consulta única:** enlace de la subida o rama/commit donde Renzo los publicó. Se preguntó en el chat porque no aparecen en las ubicaciones conocidas. No se solicita de nuevo cada archivo ni reglas ya decididas. Actualización controlada, diferencias de producción/configuración, compilación y errores quedan pendientes hasta poder recuperar esa fuente concreta. No publicación/push/merge/despliegue/servicios o datos reales, recordatorios/automatizaciones. Trabajo de preservación/revisión por Codex; no autoría o ejecución estudiantil atribuida.

## AND-001 — 05/10/2026: habilitar build y comprobar integración conservada

Problema → Android estaba detenido antes de Kotlin por SDK inexistente.
Fuente/decisión → configuración auténtica REC-002, encargo posterior de Marco;
aceptación SDK «claro» y elección de laboratorio demo autorizada; sin regla funcional nueva.
Cambio Codex → SDK/local.properties, dos tests reales app (12 casos reutilizados + 4
métodos de admisión/snapshots), test transaccional Firestore y ampliación de auditor con
trazabilidad; cero modificación de producción/versiones. Las extracciones IMPL-007–009
y restitución Merit SCP-001 se conservaron íntegramente.
Comprobación → build Android real PASS/17 métodos incluido ejemplo; firma APK PASS.
Cuatro escenarios de engine-before/after, con SDK/transacciones locales, idénticos.
Auth REST local PASS. Auditor 158 estáticos y un control negativo rechazado; expectativas
previas intactas. No repetir suites antiguas ni contar los dos runs como escenarios nuevos.
Resultado/límites → Kotlin/Compose tipados, snapshots reales de admisión y transacciones
secuenciales probadas; no pantalla, polling/identity completo, evento automático Functions,
reglas originales, R8 real o concurrencia. AVD sin aceleración/offline con software; datos
de lab exportados y procesos propios detenidos. No se atribuye código/tests a estudiantes.
Dependencia → hipervisor Windows/dispositivo de prueba y conexión Android explícita al
lab antes de ejecutar pantalla; confirmación borrador y demás reglas siguen separadas.
[Detalle y comandos](ANDROID_EXECUTION.md). Evidencia: docs/evidence/AND-001.

## AND-002 — ejecución ampliada, 06/10/2026

Problema: AND-001 compilaba pero no ejecutaba pantallas; faltaba comprobar
consumidores/corrutinas/transacciones con mayor alcance. Fuente: checkout auténtico,
IMPL-007–009, contratos y decisión de preservar borrador hasta confirmar guardado.
Justificación: verificar que separación SRP conserva decisiones y dependencias;
no añadir más traslados pequeños ni elegir arquitectura nueva. Decisión humana:
Marco autorizó elevación/laboratorio y reinició PC; no nueva regla ni código estudiantil.
Cambio: WHPX y arnés aislado/test fixtures; producción/configuración originales intactas.
Pruebas: 14 métodos Android únicos, 13 PASS/1 FAIL de isPlaced; tres casos centrales
antes/después iguales; cinco escenarios transaccionales iguales con defectos previos;
entrega automática/SDK R8 HTTP doble y callable auténtico PASS. Auditor 171 estáticos
PASS y control negativo rechazado; 158 expectativas anteriores intactas. Fallos de
preparación preservados/corregidos; probe rojo sin expectativa debilitada. Resultado:
Android ya ejecuta pantallas del laboratorio; conservaciones verificadas en alcance
acotado. Dependencias: confirmación borrador, identidad/errores, flags/payload de
perfil/envío y algoritmos idempotencia/emparejamiento requieren intervención concreta
estudiantil; reglas funcionales/temporadas/seguridad/OAuth/R8 real pendientes.
Todo soporte/código/pruebas/revisión es Codex. [Detalle](ANDROID_EXECUTION.md).


## CORE-001 / FIX-001 — 06/10/2026

Problema → contrato SDK incorrecto de booleanos de Users y fallos A05–A07
reproducidos por AND-002. Fuente → escritores/DTO/consumidores auténticos,
CT-01/04/06/07/10/12/14 y P1 pp.6–7; prioridad humana DEC-ACC-001.
Decisión existente → preservar núcleo/Ranked/rating/Merit; conservar borrador hasta
guardado confirmado; asociar espera a A. Ninguna elección nueva inferida.
Cambio → Codex añade import y dos @get:PropertyName en Users, soporte mecánico
de serialización, **corrección funcional FIX-001**; no extracción SOLID arbitraria.
Añade sondas rojas de contratos/confirmación/identidad/invariantes y actualiza
preparador/auditor con trazabilidad. Propuestas transaccionales/conexiones no
implementadas automáticamente ni atribuidas al estudiante.
Pruebas → flags antes 2 FAIL→2 PASS; roja original AND-002→PASS sin editar;
listener/VM/navegación placement PASS. Android auténtico y lab compilan.
Ocho métodos Android únicos 5 PASS/3 FAIL; Android→motor modo FAIL; ocho
invariantes Firestore 1 PASS/7 FAIL. Tests/SDK/DB reales, R8/CloudEvent/barrera
dobles explícitos. Primer teardown fallido del arnés conservado y corregido,
sin alterar aserciones/motor; sólo caso adicional cap/hold ejecutado aparte.
Auditor 175 estáticos PASS y 2 controles negativos rechazados, spec anterior
preservado. No suites anteriores ajenas/repetidas contadas como nueva cobertura.
Límites → no cloud/R8 real, autoría/validación humana o aceptación integral;
perfiles legacy arbitrarios y reglas originales de seguridad no comprobados.
Dependencia → elección compatibilidad modo/estrategia y aporte estudiantil
exacto en conexión confirmación/identidad y algoritmo atómico; cuenta/ejercicio,
timeout/reintento/tardíos siguen separados. Evidencia CORE-001, respaldo incremental,
originales/baseline/IMPL-001–009/RET/auditorías intactos. Sin datos reales,
publicación/despliegue/borrado. [Asistencia y reproducción](CORE_IMPLEMENTATION.md).


## CORE-002 — A05 guiado, en curso

Primera tarea estudiantil acotada definida sobre submitWriting:140/167; P1 §2.7 aplicable a conexión real de confirmación. No edición manual recibida ni código de integración automático. Consumers usan onError como lambda final; preservar ese contrato. Sólo respaldo/lectura/guía/documentación Codex; 0 cambios productivos, 0 tests/builds nuevos, laboratorio detenido. AppRoot+entrega automática pendiente, no prueba ejecutada. Recomendación gamemode primero con fallback gamemodeName no seleccionada; A06/A07 continúan tras aporte. [Tarea y revisión](CORE_IMPLEMENTATION.md).


### CORE-002 / A05 — revisión de primera edición, 06/10/2026

Solicitud: revisar el diff real antes de compilar y conectar AppRoot/Writing.
Fuente: AppViewModel del checkout vs backup CORE-002; SHA iguales y diff incremental
vacío. Git HEAD muestra sólo IMPL-008 previo. onPersisted ausente; onError último
y resto del método intacto. Sin atribuir aporte por presunción ni escribir integración
automática. 0 builds/suites/product edits nuevos; laboratorio no arrancado.
Próximo aporte: callback opcional antes de onError, invocado sólo en onSuccess
del repositorio antes de startLoadingResult. Revisar/compilar tras código real;
resto de A05 se guiará agrupando AppRoot/Writing y pruebas de pendiente/éxito/rechazo.
Alcance una cuenta/ejercicio/revisión, sin reglas concurrentes nuevas; no A06.
Evidencia CORE-002/A05_FIRST_EDIT_REVIEW_001.json y diff vacío conservados.

### A05 — revisión posterior a A05-SUP-001 y conexión guiada, 06/10/2026

Problema: limpieza anticipada aún presente; falta transportar una confirmación
ya implementada como soporte Codex. Fuente: fuentes actuales/hashes, SUP change.patch,
BUILD.json/log PASS 46 s y decisión humana previa de retención hasta éxito.
Cambio productivo nuevo: ninguno. Se verifica reconstrucción exacta del VM anterior
y consumidores protegidos intactos; no se repiten compilación/suites.
Aporte estudiantil pendiente: una intervención conjunta AppRoot/Writing que entregue
la continuación de éxito y limpie exclusivamente desde ella, manteniendo factory,
UUID, validación y errores. Codex aporta revisión/guía/registro, no conexión manual.
Resultado: 0 builds/tests nuevos; aceptación pendiente de código real y pruebas
pendiente/éxito/rechazo, con aserciones de conservación intactas. Una cuenta,
ejercicio y revisión; políticas simultáneas/claves y A06/A07 permanecen fuera.
Evidencia CORE-002/A05_CONNECTION_CHECKPOINT_001.json; historia y originales intactos.

## A05-CON-001 — conexión y retención hasta confirmación

06/10/2026. Corrección funcional del defecto previo, separada de IMPL-007.
Decisión humana existente: conservar texto/borrador hasta persistencia confirmada.
Codex escribe la conexión por instrucción humana explícita; posteriormente
DEC-AI-AUTH-001 elimina el bloqueo de nuevas ediciones manuales para A05–A07.
Procedencia: permiso del profesor comunicado por Marco, sin inferir fecha del
acto docente, documento adicional o revisión humana. Todo código y pruebas es
del agente; no se acredita autoría o evaluación individual por esta ejecución.

Writing:45 cambia el evento a envío + continuación; la limpieza existente pasa
dentro de esa continuación. AppRoot:84 la transmite como onPersisted; preview
adaptado a dos argumentos. ViewModel/repo permanecen intactos desde A05-SUP-001:
éxito Task.set → onPersisted → startLoadingResult. El error conserva Toast y
limpieza de sesión. Pendiente/rechazo no limpian. Revisión inversa exacta devuelve
ambos archivos a su preimagen; fábrica/UUID, validación/mensajes, tokenización,
conteo y claves intactos. API de pruebas adaptada sin cambiar aserciones.

Android original assembleDebug PASS 49 s; builds aislados antes/después PASS.
AppRoot real con VM/repositorio/Firebase SDK: antes pendiente/rechazo FAIL y
éxito PASS; después tres PASS. Éxito navega a loading: se comprueba borrador
vacío y editor restaurado al volver; sonda aislada existente comprueba editor
en vivo vacío tras éxito real. Probe rojo CORE-001 de rechazo ahora PASS intacto.
Total cinco métodos pertinentes únicos PASS. Rechazo por reglas locales y UUID
original; pendiente por red Firestore deshabilitada y metadata.hasPendingWrites.
Auth anónimo de emulador real, host/launcher fixtures; no OAuth/Ads/R8/evaluación.

Auditor conserva oráculos anteriores invirtiendo sólo callback/limpieza declarados;
179 contrastes estáticos PASS no equivalen a pruebas funcionales. Primera
adaptación leyó JSON con codificación Windows y alteró caracteres Unicode:
fallo conservado; se restauraron los campos originales con UTF-8. Controles
negativos rechazan pérdida del callback y debilitamiento del rechazo.
Versiones originales/HEAD/índice y toda evidencia histórica intactos; APK anterior
preservado. Laboratorio continúa abierto para A06, sin datos cloud/despliegue.

Alcance comprobado: Standard/Practice, una cuenta/ejercicio/revisión por caso;
no edición concurrente, segundo envío, claves por cuenta/ejercicio ni aceptación
integral del producto. No cierre total A05–A07/temporadas/retiradas.
[Diff](evidence/A05-CON-001/production-change.patch),
[resultados](evidence/A05-CON-001/EXECUTED_RESULTS.json),
[revisión](evidence/A05-CON-001/REVIEW.json), [APK](evidence/A05-CON-001/APKS.json).

## A06-CON-001 — ID confirmado y callbacks de la espera vigente

Problema: consultas al último documento permiten que B resuelva A; callbacks
ya encolados sobrevivían al cambio de espera o cierre del ViewModel. Decisión
existente: seguir el envío iniciado y conservar políticas; DEC-AI-AUTH-001 permite
la conexión/corrección del agente, sin nueva edición manual exigida. Autoría Codex.

submitWriting transmite finalSubmission.id después de onPersisted. Repositorio
añade getSubmission/listenToSubmission por document(id), preservando traducción
DTO, auth guard y errores. VM captura ID y generación; ambos caminos comprueban
generación y document.id. onCleared invalida respuestas ya encoladas. El sondeo
antiguo no modifica el contador de una espera nueva. getLastSubmission y el
fallback de Results permanecen. Validación, A05, sesión/torneos, scalar policy,
delay 3000, timeout >90 (=93 nominal), errores sin resolver, FAILED→home y
resultados tardíos ignorados siguen iguales. Corrección funcional, no nuevo retry.

Antes: tres sondas nuevas FAIL. Después: ocho métodos pertinentes PASS: esas
tres, roja original B≠A intacta, FAILED, error, timeout/tardío y regresión A05
por AppRoot. Sondeo se comprueba quitando listener y creando B más reciente;
callback antiguo/cierre usan reflexión explícita como doble de entrega, no se
presentan como eventos Firebase reales. SDK/Firestore/Auth locales reales;
evaluación se escribe como fixture, no R8. Android original y ambos builds lab PASS.
Revisión inversa completa conserva todas las operaciones ajenas y auditor 181
estáticos PASS. Originales, A05, pruebas rojas y evidencias anteriores intactos.

Alcance: asociación y ciclo de callbacks; no política de persistencias múltiples
simultáneas, cuenta/ejercicio, reintentos H1, cambio de duración/errores ni proveedor
real. Quedan independientes gamemode/gamemodeName, retirada de ligas/reputación
y temporadas. A07 sigue con control atómico sin cambiar fórmulas.
[Diff](evidence/A06-CON-001/production-change.patch),
[revisión](evidence/A06-CON-001/REVIEW.json),
[pruebas](evidence/A06-CON-001/EXECUTED_RESULTS.json).

## A07-CORE-001 y A06-RES-001 — control atómico y resultado vivo

06/10/2026. Código, revisión y pruebas: Codex bajo DEC-AI-AUTH-001. No nueva
edición/revisión humana atribuida. Marco decide sólo el refresco de Results del
mismo envío mediante respuesta explícita DEC-RES-001; precedencia de modo abierta.

**Problema → justificación → cambio.** ALT-13: evaluación/R8 y selección/commit
de pareja cambian por razones independientes. tryMatchRankedSubmission se mueve
verbatim a rankedMatching.ts como primer paso estructural comprobado. No nuevos
patrones ni cinco principios forzados. Correcciones funcionales separadas: el
motor lee el estado almacenado antes del proveedor y dentro de la transacción;
sólo contabiliza PENDING (o ausencia legacy ya admitida). Fallo tardío/repetido no
reemplaza terminal EVALUATED/FAILED. Matching lee ambos envíos dentro del commit,
exige EVALUATED/PENDING, aplica rating/resultados/rachas una vez y conserva
reciprocidad. Ajustes legacy de liga son locales al intento de transacción; se
retira el update externo que podía resucitar una pareja MATCHED como PENDING.
Fórmulas, recompensas, cap/reserva, placement, ±20/48h, esquema y efectos existentes
de sesión/ligas/reputación permanecen; su retirada requiere bloque separado.

**Antes/después y alcance real.** Las ocho invariantes CORE-001 (1 PASS/7 FAIL)
se conservan y ahora dan 8 PASS con transacciones Firestore SDK reales. Tres casos
nuevos: antes 1 PASS/2 FAIL, después 3 PASS (FAILED terminal, error tardío frente
a éxito/sesión posterior, dos envíos distintos del mismo autor). Total 11 casos
pertinentes; CloudEvents, respuesta R8 y barreras de planificación son dobles
explícitos, sin evento/proveedor cloud. Functions build auténtico PASS (22 s).

AppRoot→Writing→repositorio→Firestore→entrega automática Functions→R8 HTTP doble
→Results ejecutado en WHPX: Practice PASS y Ranked con candidato previo PASS,
score/feedback/Merit y rating/reciprocidad observados. El soporte inicial falla
por PlayMode.name inexistente y selector 80.0 ambiguo; logs conservados, corrección
del arnés sin cambiar producto/aserciones de aceptación. Un tercer método revela
un defecto previo: tras mostrar EVALUATED, un match posterior deja VM PENDING/0
aunque servidor MATCHED/+1. Guardia loadingResolved anterior a A06 explica el
defecto; no regresión atribuida a la extracción.

**Decisión humana → corrección → prueba.** Marco: «Actualizar Results del mismo
envío». A06-RES-001 añade una rama: ID/generación vigentes primero; Results visible,
ya resuelto, sin timeout, EVALUATED/RANKED/MATCHED y mismo latestSubmission.id.
Único efecto: actualizar latestSubmission; no navegar ni reiniciar espera. Android
auténtico PASS (41 s), lab PASS (48 s). La misma sonda automática roja sin cambios
pasa, +1 visible; cuatro pruebas de guardias también PASS (otro ID/generación,
salir de Results, timeout y ViewModel cerrado). Esas cuatro usan reflexión/estado
de timeout explícitos; no se presentan como entrega Firebase o timer de 93 s.
Evidencia previa del timer real se reutiliza, sin repetirla ni sumarla como nueva.
JSON observado antes del wait UI; JUnit final comprueba el refresco.

**Revisión y límites.** Inversión exacta de cambios declarados reconstruye motor,
función de matching y VM completos antes de los oráculos anteriores; consumidores,
importaciones y fronteras revisados por Codex en una fase separada. Auditor final
187 estáticos PASS, controles negativos rechazan guardia de candidato, aserción
histórica y guardia del resultado alteradas. No sustituye pruebas, auditor externo
ni cumplimiento individual. R8 puede ser llamado dos veces si dos workers llegan
antes del commit; sólo uno contabiliza. Stats legacy fuera de tx no son durable
exactly-once. No Home/cobro Ranked, OAuth/Ads, proveedor real o placement automático
completo. Original APK cloud no se instala; config auténtica conserva hashes.

Pendientes concretos: precedencia gamemode/gamemodeName si ambos difieren (consulta
ya enviada, escrituras/lecturas sin normalizar); cuenta/ejercicio/edición simultánea
y segundo envío; detalle H1/reintento; retiradas de torneos/ligas/reputación y reglas
de temporadas. Ranked/rating/Merit permanecen. No datos reales ni despliegue/push.

[Diff consolidado](evidence/A06-RES-001/CORE-current.patch),
[revisión A07](evidence/A07-CORE-001/REVIEW.json),
[11 casos](evidence/A07-CORE-001/EXECUTED_RESULTS.json),
[corrección y 5 métodos](evidence/A06-RES-001/EXECUTED_RESULTS.json).

### Reproducción acotada

En checkout, con JAVA_HOME JDK17 y ANDROID_HOME apuntando al SDK existente:
`gradlew.bat --offline --no-daemon :app:assembleDebug --console=plain`.
En functions, PATH del Node20 auténtico: `npm.cmd run build`. No reinstalar ni
sustituir versiones/configuración. APK original:
`app/build/outputs/apk/debug/app-debug.apk` (config cloud, no usado en sondas).

Para repetir una sonda, usar sólo com.inkr8.lab/APKs A06-RES-001-private,
emulator-5560 y demo-inkr8-local con puertos Auth9099/Firestore8080/Functions5001;
R8 HTTP doble5010 y fetch externo bloqueado. Instalación `adb -s emulator-5560
install -r <apk-lab>` y su androidTest; adb reverse para los tres puertos.
Filtro instrumental: `com.inkr8.lab.LabIntegratedFlowTest#laterAutomaticMatchUpdatesAlreadyDisplayedSameResult`
en runner `com.inkr8.lab.test/androidx.test.runner.AndroidJUnitRunner`. El otro
filtro `com.inkr8.lab.LabResultRefreshTest` prueba guardias mediante dobles.
Preparación/reanudación local documentada en RUNTIME_PRESERVATION.json; conservar
el export más reciente, no borrar fixtures ni arrancar con datos cloud.

## CORE-CLOSE-001 — compatibilidad, consumidores y control del cierre

Problema → decisión → cambio → comprobación → límite registrados en
[CORE_CLOSURE_AUDIT](CORE_CLOSURE_AUDIT.md). DEC-MODE-001 es elección explícita de
Marco en este chat; código/revisión/pruebas Codex. Compatibilidad es corrección
funcional, no cambio de escritor/esquema. Ghost lee estado/rating dentro del commit,
Results refresca GHOST del mismo ID y el trigger espera matching con catch intacto.
Fórmulas/tiempos/errores/claves conservados; diferimiento de pruning declarado.
Rojas anteriores y aserciones intactas, nuevas rojas antes/después y restricciones
de datos inválidos documentadas. Trece métodos Android únicos PASS, 15 casos de
modo SDK PASS, ghost2/lifecycle1 y once invariantes Ranked PASS. Build Android/lab/
Functions auténticos PASS; eventos Android automáticos locales y R8 HTTP doble.
Dos sondas Writing separadas: pérdida de nueva revisión FAIL, dos UUID PASS como
caracterización, no aceptación. Consulta única de edición/segundo envío pendiente;
sin manualidad estudiantil exigida. Auditoría estática y negativos van separados
de aceptación real. No aceptación integral/SOLID integral ni proveedor externo.

## CORE-FINAL-001 — revisión/contexto/reintento y cierre técnico del núcleo

Fuente/decisión: prompt explícito de Marco posterior a CORE-CLOSE-001, registrado
06/10/2026; DEC-DRAFT-REV-001, DEC-DRAFT-SCOPE-001, DEC-WAIT-RETRY-001. Codex implementa,
prueba y revisa bajo DEC-AI-AUTH-001; no nueva autoría/revisión estudiantil atribuida.

Problema → responsabilidad → cambio: revisión B/A→B→A no debe ser limpiada por A;
revisión monotónica y confirmación de revisión en almacenamiento local, observada
por editor vigente; no regrabar una confirmación desde composición antigua. VM
posee bloqueo/generación del ciclo real de persistencia; Root transmite éxito y
fallo; onError último y preparación/errores/sesión originales preservados. UID y
contexto/palabras existentes determinan namespace y vida del editor; históricos
sin dueño no se migran ni eliminan. Reintento de lectura del mismo ID con generación
nueva, mensajes y logs sin texto/secretos; 3 s/>90 s y terminales conservados.
Native SharedPreferences no impone Compose al gestor; separación acotada por SRP,
sin interfaces/patrones cosméticos. Son correcciones funcionales aprobadas y una
separación de vida del editor, distintas de IMPL-001–009 y las retiradas.

Antes/después: roja B previa conserva aserción y pasa; roja de espera anterior
writing→results pasa tras invalidar al abrir editor; reentrada 1 PASS/1 FAIL antes
(fallo UI/callback doble; SDK ya PASS) → ambas PASS tras notificación de revisión.
Metadata descriptiva no reinicia identidad/edición. Pendiente/éxito/rechazo real
SDK y Auth A/B/A comprobados. Dos sincronizaciones/fallos de soporte y filtro no
seleccionado conservados y corregidos sin debilitar aserciones. Auditor conserva
oráculos anteriores; 206 estáticos PASS +4 negativos privados, separados de
aceptación. Baseline/configuración/R8/fórmulas conservados, sin Git publicación.

Resultado real: 46 métodos Android únicos y 19 unit tests PASS, 11 Ranked +2 ghost
+1 lifecycle SDK PASS. SDK/AVD y configuración auténticos reutilizados; Functions
sin cambios, build previo válido reutilizado. APK actual ejecuta STANDARD/ON_TOPIC
y Practice/Ranked hasta Results con R8 HTTP doble y eventos automáticos locales.
No evaluación externa/OAuth/Ads/cron real. Cierre técnico dentro de criterios,
no producto/academia completos. [Registro final](CORE_FINAL_AUDIT.md).

## RET-006 — retirada funcional completa local / DEC-RANKED-SELECT-001

06–07/10/2026. Problema: funcionalidades exclusivas retiradas aún coordinan
Android/Functions y la liga elige ejercicio. Fuente: SCP-001/D1 y dos respuestas
humanas de consulta agrupada, preservadas en RET-006/SEA-001. Decisión: torneos/
ligas/reputación fuera; Merit/rating/Ranked dentro; elección Standard/On-Topic,
error sin fallback/cargo, factor histórico de precio congelado. Codex implementa
bajo DEC-AI-AUTH-001; no escritura/revisión humana acreditada.

Cambio: consumidores compartidos revisados, 32 componentes exclusivos retirados,
rutas/listeners/jobs/efectos eliminados, limpieza sesión conservada, economía sin
repricing; DTO/initializer/draft legacy y datos históricos intactos. Es retirada
funcional separada de IMPL/extracciones; no prueba de SOLID integral. Snapshot
antes, primera roja showButton, error hilo Toast, espejo obsoleto y soporte UI
conservados; corregidos y revisados.

Pruebas: retirada SDK 4 PASS/6 FAIL antes ->10 PASS después; Android/Functions
compilan, 19 unitarios y 37 métodos Android únicos pertinentes PASS sin sumar
repeticiones. Borrador/identidad/reintento22 PASS; Ranked11/ghost2/lifecycle1 PASS
sobre fuentes finales. Configuración/R8 y baseline auténticos preservados.
Retirada de servicios desplegados y seguridad/aceptación externa no realizadas.
Ver FUNCTIONAL_CLOSURE.md y evidencia RET-006/SEA-001.

## SEA-001 — temporadas HU 3.27/3.28 / DEC-SEASON-001

Marco acepta la propuesta completa («me gusta tu propuesta»), no código/revisión
manuales. Codex implementa registro por createTime/placement, ledger idempotente
por documento, variación real clamped, posición compartida, cierre atómico tras
pendientes/acuse, callables autenticados, ranking/historial UI; no reset/reward/
backfill. Nueva SEASON_ACTIVATED_AT_MS sólo demo, original no alterado.

Revisión separada: consumidores, límites transaccionales, efectos/economía,
lecturas Practice, firmeza CLOSED y hash/oráculos anteriores. Corrige TS y
reapertura concurrente por actualización CLOSING fuera de TX; pruebas estrictas
conservadas. 20 SDK PASS +1 comprobación de eventos automáticos sobre6documentos;
ranking propio/historial selectable y STANDARD/ON_TOPIC hasta Results locales PASS.
280 contrastes estáticos separados +2 mutaciones privadas rechazadas, sin cambiar
criterios anteriores; diff aplicable al snapshot preservado.

Límites: R8 HTTP doble; Auth anónimo SDK, fixtures/clock y no-op sólo en fase
persistencia/reintento declarados. Scheduler/48 h/fin de mes y escala no ejecutados
externamente; reglas Firestore originales faltan; combinación poda/pendiente por
validar. Export demo/APK/diffs y fuentes originales preservados; ningún despliegue
o borrado de datos reales. Cierre técnico local, no producto/academia completos.

### SEA-001 — hallazgo final poda/pendiente y corrección de soporte

07/10/2026. Prueba nueva real en base dedicada: archivo10 elimina un Ranked
con assignment pendiente (FAIL conservado). Contradice cierre tras resolver
pendientes aprobado; no nueva regla de recompensa/timeout. Codex protege Ranked
posteriores a activación hasta acuse de creación y, si elegibles, liquidación
terminal. Archivo anterior a activación intacto; no migración/borrado histórico.
Functions recompiladas PASS; aserción idéntica pasa con21casos SDK. Auditor281
estáticos PASS, expectativas originales intactas; diff75fuentes revisado/aplicable.
APK Android sin cambios desde37métodos PASS; no suite UI repetida por cambio sólo
backend. Export anterior preservado; nuevo export incorpora sondas finales.


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


## Publicación FIN-002

**Publicación revisable FIN-002:** [PR #2 en borrador](https://github.com/Rnz5/Inkr8-ISW2/pull/2)
hacia master; [rama](https://github.com/Rnz5/Inkr8-ISW2/tree/codex/refactorizacion-por-bloques).
Código/pruebas/UML/informe: Codex; publicación con cuenta autorizada MACOABC.
7 Android,39 permisos,6 nombre,14 operación,16 borrado y2 log PASS;287 contrastes
estáticos separados. Copia limpia con autocrlf=true conserva recursos auténticos y
89 artefactos. Se corrigió versionado de bytes, sin modificar oráculos anteriores.
Sin merge/deploy/datos reales; dependencias externas/académicas en FINAL_DELIVERY.

Checkout previo y bundle privados conservados. Fuentes/propuesta/entrega agrupadas
en commits reales de Codex; no sprints o revisión estudiantil inventados. Dos fallos
de copia limpia por CRLF/LF quedaron rojos y se corrigieron con atributos y bytes
auténticos, sin redefinir hashes/aserciones. GitHub informa PR abierto en borrador,
base master, sin conflicto en la comprobación previa a este registro.


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
