# Matriz local de contratos de Inkr8

**Vigente FIN-002 — 09/10/2026:** cierre y entrega revisable documentados en
[FINAL_DELIVERY.md](FINAL_DELIVERY.md), con 6 Android PASS, reglas nuevas
propuestas y límites externos/académicos explícitos. Los estados inferiores son
antecedentes preservados; no revocan DEC-AI-AUTH-001, las decisiones respondidas
ni el permiso actual de publicar rama/PR, sin fusión o despliegue.

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

**Versión:** 0.1 · 04/10/2026 · America/Lima · chat 5, U9/DEC-17. Documento derivado con apoyo de Codex; revisión crítica humana pendiente. [Mapa técnico](TECHNICAL_MAP.md), [aceptación 0.2](ACCEPTANCE_MATRIX.md), [decisiones](DECISIONS.md), [registro DOC-005](evidence/DOC-005/record.md).

## Versión y autoridad

Todos los localizadores de código de esta matriz corresponden a **C1 64983846d6dbf4cdfafcdd508464cd140a2a862e**, comprobado en la copia externa disponible: origin Rnz5/Inkr8-ISW2, HEAD exacto y sin diferencias en archivos versionados ni no versionados según status. No se consultó vigencia remota ni despliegue. Los enlaces al commit son localizadores derivados de los archivos leídos, no una consulta nueva a GitHub. [Verificación documental](evidence/DOC-005/VERIFICATION.json).

**HE:** hecho de lectura estática. **RI:** riesgo inferido, no fallo reproducido. **OR:** criterio original H1. **AL:** alcance humano vigente. **PR:** propuesta de verificación, sin ejecución ni nueva aprobación funcional. Cada ficha distingue estos niveles; ninguna es un test aprobado.

H1-P = hoja exacta Épica Gestionar perfil; H1-W = Épica Desarrollar práctica de e; H1-C = Épica Competir en la aplicación. E identifica la celda de aceptación. [Texto y localizadores recuperados](evidence/DOC-004_BACKLOG_RECOVERY.md). Las 20 HU amarillas y Merit completo están excluidos; 3.7 se lee sin sus cláusulas económicas. Las HU 3.27/3.28 incluyen temporadas, con reglas todavía pendientes. No se restablece HU 1.8 (detalle posterior) al conservar feedback inmediato.

CT-01–CT-16 son IDs nuevos de organización documental; no reemplazan ni alteran C01–C06/F01–F12 del [snapshot G2](reference/TECHNICAL_MAP_2026-10-03.md). Correspondencia: G2 C01→CT-01; C02→CT-02; C03→CT-04/06; C04→CT-05/07/08; C05→CT-09/10/12; C06→CT-15.

## Índice de la matriz

| ID | Contrato | Productor | Consumidor | Comprobación |
|---|---|---|---|---|
| CT-01 | [Modo de ejercicio y modalidad de participación](#ct-01) | Writing → SubmissionFactory → SubmissionMapper/FirestoreSubmissionRepository. | submissionEvaluationEngine; SubmissionMapper al leer; dailyStatsSnapshot. | Lectura estática; sin ejecución |
| CT-02 | [Contexto temático: identidad frente a nombres](#ct-02) | ThemeRepository/TopicRepository → OnTopicWriting → Writing/SubmissionFactory/DTO. | Writing muestra el contexto; submissionEvaluationEngine prepara argumentos para R8; dailyStats agrupa por themeId. | Lectura estática; sin ejecución |
| CT-03 | [Texto, restricciones y palabras remitidas al evaluador](#ct-03) | Gamemodes/WordRepository → Writing → SubmissionFactory. | ValidationUtils en cliente; filtro isContentLowQuality y requiredWords en submissionEvaluationEngine; R8. | Lectura estática; sin ejecución |
| CT-04 | [Identidad persistida y asociación del resultado](#ct-04) | SubmissionFactory UUID; AppViewModel fija authorId; FirestoreSubmissionRepository escribe el documento. | Trigger usa snapshot.id; AppViewModel espera getLastSubmission/getLastSubmissionRealtime. | Lectura estática; sin ejecución |
| CT-05 | [Evaluación, errores y deserialización](#ct-05) | submissionEvaluationEngine y resultado de evaluateWithR8. | FirestoreEvaluation/SubmissionMapper/EvaluationMapper → AppViewModel/Results. | Lectura estática; sin ejecución |
| CT-06 | [Espera, timeout, reintento y actualizaciones tardías](#ct-06) | Listener y sondeo de AppViewModel; reloj del bucle; cambios backend. | handleSubmissionUpdate → Screen.results/placementReveal/home; LoadingScreen. | Lectura estática; sin ejecución |
| CT-07 | [Emparejamiento ordinario y concurrencia](#ct-07) | tryMatchRankedSubmission, invocado tras evaluación Ranked. | Dos documentos submissions, dos users y presentación matchResult en Results/Competitions. | Lectura estática; sin ejecución |
| CT-08 | [Resolución ghost de Ranked sin rival](#ct-08) | ghostMatchProcessor programado. | Submission.matchStatus/matchResult/evaluation.ratingChange; users.rating; Results. | Lectura estática; sin ejecución |
| CT-09 | [Entrada, abandono y limpieza de sesión Ranked](#ct-09) | Competitions → UserRepository.applyMeritAction; ABANDON_RANKED servidor; evaluador; finishRankedSession; rankedSessionCleaner. | Documento users, UI de entrada/editor y posteriores intentos. | Lectura estática; sin ejecución |
| CT-10 | [Placement y revelación del rating](#ct-10) | submissionEvaluationEngine incrementa placement; listener users actualiza currentUser. | AppViewModel decide placementReveal; PlacementRevealScreen y markPlacementRevealSeen. | Lectura estática; sin ejecución |
| CT-11 | [Rating general y resultados inmediatos](#ct-11) | calculateDynamicRatingChange/matcher; ghost; placement; evaluación R8. | users.rating; Results usa matchResult y evaluation; Profile/reveal/cabecera. | Lectura estática; sin ejecución |
| CT-12 | [Usuario compartido, lectores y escritores anteriores](#ct-12) | ensureUserExists y createUserProfile; claimUsername/updateEmail/finishSession/markReveal; motores/jobs/callables existentes. | UserRepository escucha/lee users → AppViewModel → pantallas; matcher/ghost/entrada y otros procesos leen el mismo documento. | Lectura estática; sin ejecución |
| CT-13 | [Envíos previos, consultas, guardado y retención](#ct-13) | Escritor Kotlin, evaluación/matcher/ghost; SAVE_SUBMISSION; borrado explícito y pruneOldSubmissions. | Lista/historial/recentes/último; dailyStats; contadores guardados; consultas de entrada/ghost/matcher. | Lectura estática; sin ejecución |
| CT-14 | [Borrador local e identidad de reanudación](#ct-14) | Writing autoguarda y limpia; DraftManager almacena SharedPreferences. | Writing inicializa userText desde getDraft. | Lectura estática; sin ejecución |
| CT-15 | [Correspondencia con UML y BD históricos](#ct-15) | I1, figuras 5.20, 5.22, 5.23 (modelos históricos; M1 editable no consultado de nuevo). | Documentación y revisión de trazabilidad; comparación con C1, no consumidor ejecutable. | Lectura estática y visual I1; sin ejecución |
| CT-16 | [Estadísticas periódicas y ausencia de contrato estacional](#ct-16) | dailyStatsSnapshot; weeklyStatsSnapshot/monthlyStatsSnapshot agregan registros diarios. | Registros stats/daily|weekly|monthly/records; no se identificó en lo leído un consumidor que implemente HU 3.27/3.28. | Lectura estática; sin ejecución |

## Fichas verificables

Todas las referencias indican archivo, línea inicial/final y versión fijada. Para I1, fuente no textual de código, el localizador exacto es documento/figura y relación interna del DOCX, conservada en el registro. No se inventan números de línea para imágenes.

<a id="ct-01"></a>

### CT-01. Modo de ejercicio y modalidad de participación

**Fuentes C1:** [Writing.kt:324–340](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Writing.kt#L324); [SubmissionFactory.kt:9–32](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/evaluation/SubmissionFactory.kt#L9); [SubmissionMapper.kt:6–21](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/mappers/SubmissionMapper.kt#L6); [SubmissionMapper.kt:35–51](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/mappers/SubmissionMapper.kt#L35); [FirestoreSubmission.kt:6–24](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/FirestoreSubmission.kt#L6); [submissionEvaluationEngine.ts:117–126](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L117); [submissionEvaluationEngine.ts:216–226](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L216); [dailyStatsSnapshot.ts:50–60](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/stats/dailyStatsSnapshot.ts#L50).

| Campo | Contrato observado y límite |
|---|---|
| Productor | Writing → SubmissionFactory → SubmissionMapper/FirestoreSubmissionRepository. |
| Consumidor | submissionEvaluationEngine; SubmissionMapper al leer; dailyStatsSnapshot. |
| Datos, tipos, identidad y significado | Gamemode del dominio: String STANDARD/ON_TOPIC. El DTO escribe gamemodeName:String; el evaluador lee data.gamemode y, si falta, pasa STANDARD a R8. DailyStats lee gamemodeName. Playmode:String PRACTICE/RANKED/TOURNAMENT es una dimensión distinta; el servidor usa PRACTICE si falta. Gamemode.name contiene etiquetas de pantalla, no los códigos enviados. |
| Estados, errores y eventos temporales | Valor fijado al construir el envío; el trigger omite TOURNAMENT. No hay validación exhaustiva de códigos en la entrada mostrada a R8. |
| Comportamiento comprobado | HE: nombres diferentes entre escritor Kotlin y lector TypeScript; el DTO examinado no declara gamemode. No se ejecutó la serialización ni R8. |
| HU, alcance y aceptación | AC-01/03/07; S01–S03/S08/S20. H1-W E2/E6/E10/E18 (HU 2.1/2.2/2.3/2.5); H1-C E53/E57/E61/E65 (3.13–3.16). Conservación del ejercicio; retirada de selección por liga. |
| Discrepancia e impacto posible | RI: un envío ON_TOPIC de este productor podría llegar a R8 como STANDARD. No demuestra que todos los documentos desplegados tengan esa forma ni cuál sería su puntuación. |
| Verificación necesaria | PR: observar payload serializado y argumentos recibidos por R8 para Standard/On-Topic × Practice/Ranked; comparar también lectura histórica. Ninguna corrección elegida. |
| Información ausente / decisión humana | Fuente: implementación y contrato auténticos de R8 y documentos anonimizados. Decisión Q-R: oferta Standard/fallback sin ligas; H1 ya fija selección explícita On-Topic. |

<a id="ct-02"></a>

### CT-02. Contexto temático: identidad frente a nombres

**Fuentes C1:** [Gamemodes.kt:32–42](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/Gamemodes.kt#L32); [Theme.kt:3–9](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/Theme.kt#L3); [Topics.kt:3–10](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/Topics.kt#L3); [ThemeRepository.kt:13–34](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/ThemeRepository.kt#L13); [TopicRepository.kt:13–35](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/TopicRepository.kt#L13); [Writing.kt:335–340](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Writing.kt#L335); [Writing.kt:391–419](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Writing.kt#L391); [FirestoreSubmission.kt:14–17](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/FirestoreSubmission.kt#L14); [submissionEvaluationEngine.ts:216–226](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L216).

| Campo | Contrato observado y límite |
|---|---|
| Productor | ThemeRepository/TopicRepository → OnTopicWriting → Writing/SubmissionFactory/DTO. |
| Consumidor | Writing muestra el contexto; submissionEvaluationEngine prepara argumentos para R8; dailyStats agrupa por themeId. |
| Datos, tipos, identidad y significado | Theme.id/Topic.id/Topic.themeId y nombres: String. DTO: themeId/topicId:String?; no themeName/topicName. Topic se busca en colección raíz topics, filtrada por themeId. R8 recibe data.themeName/data.topicName o null; en esa llamada no recibe IDs. |
| Estados, errores y eventos temporales | Contexto elegido antes de escribir; Ranked vuelve a Standard si la carga devuelve null (Competitions:90–96). Las consultas suspendidas no muestran aquí un catch/reintento propio. El editor no persiste el contexto en su borrador. |
| Comportamiento comprobado | HE: nombres existen en los objetos de pantalla, pero no en el DTO del envío; no se identificó en el evaluador mostrado una consulta a themes/topics que traduzca esos IDs. |
| HU, alcance y aceptación | AC-01/03/07; S02/S03/S08/S22. H1-C E53/E55/E57/E59/E61/E63/E65/E67; H1-W E2/E4. Contexto On-Topic conservado. |
| Discrepancia e impacto posible | RI: evaluación temática sin los nombres mostrados al usuario; asociación histórica ambigua si se cambia un tópico. No se conoce si R8 resuelve contexto por otro medio. |
| Verificación necesaria | PR: cotejar IDs de catálogo, nombre visible, documento guardado y entrada real R8; casos null/error/contexto cambiado sin modificar datos existentes. |
| Información ausente / decisión humana | Fuente: R8 y ejemplos auténticos anonimizados. Q-R: fallback/contexto exigido; Q-D: lectura de contexto y reanudación de borradores previos. No se eligió duplicación de nombres ni relación futura de BD. |

<a id="ct-03"></a>

### CT-03. Texto, restricciones y palabras remitidas al evaluador

**Fuentes C1:** [Gamemodes.kt:19–42](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/Gamemodes.kt#L19); [Writing.kt:98–134](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Writing.kt#L98); [Writing.kt:310–340](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Writing.kt#L310); [ValidationUtils.kt:9–40](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/utils/ValidationUtils.kt#L9); [SubmissionFactory.kt:18–32](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/evaluation/SubmissionFactory.kt#L18); [submissionEvaluationEngine.ts:50–82](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L50); [submissionEvaluationEngine.ts:160–189](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L160).

| Campo | Contrato observado y límite |
|---|---|
| Productor | Gamemodes/WordRepository → Writing → SubmissionFactory. |
| Consumidor | ValidationUtils en cliente; filtro isContentLowQuality y requiredWords en submissionEvaluationEngine; R8. |
| Datos, tipos, identidad y significado | Content:String; wordCount/characterCount:Int en Kotlin, timestamp:Long de reloj cliente. Standard 4 palabras, 50–150; On-Topic 2, 50–200; timeLimit:Int?=null. Writing envía wordsUsed:List<Words> filtrada a palabras presentes. TS la convierte a string[] requiredWords, aceptando objetos con word o strings. |
| Estados, errores y eventos temporales | CanSubmit sólo comprueba no vacío y rango de palabras; al pulsar corre filtro de calidad. Cliente y TS rechazan <50 caracteres, palabras >35 caracteres y ciertos patrones repetidos/vocales. Rechazo servidor escribe FAILED/evaluationError y limpia sesión Ranked. |
| Comportamiento comprobado | HE: el conjunto asignado completo no viaja en wordsUsed cuando faltan palabras; el servidor deriva requiredWords de ese subconjunto. No se inspeccionó el algoritmo R8 ni se ejecutó la validación en esta tarea. |
| HU, alcance y aceptación | AC-01/03; S03/S08. H1-W E2/E4/E6/E8/E10/E14/E16; H1-C E57/E61/E65. B1 cubre sólo ValidationUtils históricamente, no este contrato integral. |
| Discrepancia e impacto posible | RI: el consumidor puede no conocer las palabras asignadas que faltaron; 50 caracteres y 50 palabras son controles distintos. H1 habla de cuatro restricciones y mínimo de caracteres sin definir todos sus parámetros. |
| Verificación necesaria | PR: comparar reto asignado, palabras usadas/enviadas, evaluación de omisiones, tokenización y error específico. Verificar fronteras de longitud y mensajes con R8 auténtico. |
| Información ausente / decisión humana | Q-R: lista exhaustiva de restricciones y parámetros válidos. Fuente: contrato R8 y estructura del cumplimiento/feedback. Los valores de código no son nuevos criterios ratificados. |

<a id="ct-04"></a>

### CT-04. Identidad persistida y asociación del resultado

**Fuentes C1:** [SubmissionFactory.kt:18–32](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/evaluation/SubmissionFactory.kt#L18); [AppViewModel.kt:140–175](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L140); [FirestoreSubmissionRepository.kt:19–34](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/FirestoreSubmissionRepository.kt#L19); [FirestoreSubmissionRepository.kt:137–172](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/FirestoreSubmissionRepository.kt#L137); [AppViewModel.kt:278–327](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L278); [submissionEvaluationEngine.ts:342–355](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L342).

| Campo | Contrato observado y límite |
|---|---|
| Productor | SubmissionFactory UUID; AppViewModel fija authorId; FirestoreSubmissionRepository escribe el documento. |
| Consumidor | Trigger usa snapshot.id; AppViewModel espera getLastSubmission/getLastSubmissionRealtime. |
| Datos, tipos, identidad y significado | ID UUID:String, autor:String=currentUser.id, timestamp:Long=System.currentTimeMillis(). Ruta submissions/{id}; DTO lleva id. Lectores copian doc.id sobre el campo id. Evaluation.submissionId se escribe con snapshot.id. La consulta de espera usa AuthManager.uid y timestamp DESC/limit(1), sin filtro playmode ni parámetro ID enviado. |
| Estados, errores y eventos temporales | onSuccess del guardado no devuelve ID y llama startLoadingResult() sin él. Dos envíos, reloj cliente, igualdad de timestamps u otra modalidad pueden cambiar qué es el último. La espera asigna el primer EVALUATED aceptado a latestSubmission. |
| Comportamiento comprobado | HE: existe identidad precisa en escritura/trigger; se pierde su seguimiento explícito en la consulta de espera. No se reprodujo una asociación incorrecta. |
| HU, alcance y aceptación | AC-03/07; S08/S10/S20/S22. H1-W E6/E10/E12/E14; H1-P E27/E29/E47/E49 (historial). Vincular resultado al envío es PR de la matriz, no criterio original adicional inventado. |
| Discrepancia e impacto posible | RI: resultado de B mostrado durante espera de A; el orden por tiempo de cliente no prueba causalidad ni unicidad. No se coteja evaluation.submissionId con el esperado en handleSubmissionUpdate. |
| Verificación necesaria | PR: enviar A/B, cambiar orden de evaluación/reloj y modalidad, comparar ID enviado/documento/evaluation/submission visible; incluir campo id ausente/diferente. |
| Información ausente / decisión humana | Fuente: documentos/ejecución autorizados. Q-D: identidad/documentos antiguos; Q-R: recuperación del envío esperado tras error/timeout. No se eligió una API futura. |

<a id="ct-05"></a>

### CT-05. Evaluación, errores y deserialización

**Fuentes C1:** [submissionEvaluationEngine.ts:95–180](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L95); [submissionEvaluationEngine.ts:342–356](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L342); [submissionEvaluationEngine.ts:399–415](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L399); [SubmissionStatus.kt:3–8](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/SubmissionStatus.kt#L3); [FirestoreEvaluation.kt:3–12](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/FirestoreEvaluation.kt#L3); [EvaluationMapper.kt:7–19](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/mappers/EvaluationMapper.kt#L7); [SubmissionMapper.kt:20–30](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/mappers/SubmissionMapper.kt#L20).

| Campo | Contrato observado y límite |
|---|---|
| Productor | submissionEvaluationEngine y resultado de evaluateWithR8. |
| Consumidor | FirestoreEvaluation/SubmissionMapper/EvaluationMapper → AppViewModel/Results. |
| Datos, tipos, identidad y significado | Status enum Kotlin PENDING/EVALUATED/NOT_EVALUABLE/FAILED; DTO String. Evaluation: submissionId:String?, finalScore:Double, feedback:String, resultStatus:String, ratingChange:Long, source:String?, Merit histórico. TS escribe evaluationError fuera de DTO, no declarado en FirestoreSubmission. Resultado R8 accedido como finalScore/feedback/source sin fuente de su tipo. |
| Estados, errores y eventos temporales | Trigger de creación omite TOURNAMENT y status explícito distinto de PENDING. Ausencia de autor/calidad/error → FAILED; éxito → EVALUATED y borra evaluationError. Ranked exitoso → matchStatus PENDING; Practice → UNMATCHED. No se halló escritura NOT_EVALUABLE en el motor. |
| Comportamiento comprobado | HE: status desconocido del envío cae a PENDING; resultStatus no vacío desconocido se pasa a valueOf sin catch en EvaluationMapper. Son dos tratamientos distintos. Campo evaluationError no llega por este DTO. |
| HU, alcance y aceptación | AC-03/07; S08/S10/S20. H1-W E10/E12/E14/E16; H1-C E61/E63/E65/E67. Conservación de evaluación/feedback; Merit y ranking global retirados. |
| Discrepancia e impacto posible | RI: estado desconocido ocultado como pendiente o excepción de conversión en evaluación; pérdida del motivo del error. El tipo String de feedback no demuestra un desglose estructurado por restricción. |
| Verificación necesaria | PR: fixtures auténticos con evaluación nula/válida/malformada, estados ausentes/desconocidos y score fuera de rango; observar DTO, error y pantalla. No se valida score 0–100 sólo por escribir result.finalScore. |
| Información ausente / decisión humana | Fuente: R8/fixtures. Q-D: conducta ante documentos incompletos; Q-R: recuperación y presentación mínima de errores; H1 ya exige score/feedback/fallback. |

<a id="ct-06"></a>

### CT-06. Espera, timeout, reintento y actualizaciones tardías

**Fuentes C1:** [AppViewModel.kt:278–327](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L278); [AppViewModel.kt:460–468](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L460); [LoadingScreen.kt:48–75](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/LoadingScreen.kt#L48); [AppRoot.kt:164–190](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/AppRoot.kt#L164); [AppRoot.kt:314–318](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/AppRoot.kt#L314); [submissionEvaluationEngine.ts:95–102](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L95); [submissionEvaluationEngine.ts:380–398](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L380).

| Campo | Contrato observado y límite |
|---|---|
| Productor | Listener y sondeo de AppViewModel; reloj del bucle; cambios backend. |
| Consumidor | handleSubmissionUpdate → Screen.results/placementReveal/home; LoadingScreen. |
| Datos, tipos, identidad y significado | loadingResolved/loadingTimeout:Boolean, loadingElapsedSeconds:Int; latestSubmission:Submissions?. Sondeo cada 3000 ms; elapsed=pollCount*3, timeout si >90. Nominalmente se marca en iteración 31 (93 s), sin garantía de tiempo real. Función declara timeoutSeconds=540, distinto del cliente. |
| Estados, errores y eventos temporales | EVALUATED navega sin esperar match; FAILED vuelve a Home. NOT_EVALUABLE/PENDING no resuelven. Al timeout termina sondeo; listener queda hasta nueva espera/onCleared, pero handler ignora actualizaciones si resolved o timeout. Results recibe latestSubmission fijada; listener allSubmissions actualiza otra lista. |
| Comportamiento comprobado | HE: no se ve reintento/notificación en startLoadingResult/LoadingScreen; timeout ofrece Return Home y texto de demora/historial. Fallos de consulta imprimen/loguean y no crean estado de error específico. Match ordinario se lanza con catch de log. |
| HU, alcance y aceptación | AC-03/06/07; S09/S10/S18/S19/S22. H1-W E12 pide mensaje/notificación prometida y registro para reintento; H1-C E33 espera 48 h para match. Son relojes distintos, sin temporadas implementadas. |
| Discrepancia e impacto posible | RI: match tardío o evaluación tras timeout no refresca este objeto de Results; no se acredita cumplimiento de recuperación H1. onCreate es la declaración disponible; no se identificó aquí un proceso de reevaluación por actualización a PENDING. |
| Verificación necesaria | PR: variar orden listener/sondeo/usuario-placement, llegar después de resolución/timeout, provocar error de consulta y repetir evento/entrada. Confirmar quién reintenta, con qué identidad y sin duplicar efectos. |
| Información ausente / decisión humana | Fuente: ejecución/configuración real de entrega y R8. Q-R: duración/recuperación/notificación/reintento; Q-T: efecto de tardíos en cierre. No se afirma política de retry del servicio desplegado. |

<a id="ct-07"></a>

### CT-07. Emparejamiento ordinario y concurrencia

**Fuentes C1:** [submissionEvaluationEngine.ts:380–393](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L380); [submissionEvaluationEngine.ts:420–578](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L420); [MatchResult.kt:3–9](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/MatchResult.kt#L3); [SubmissionMapper.kt:23–30](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/mappers/SubmissionMapper.kt#L23); [Results.kt:217–243](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Results.kt#L217); [FirestoreSubmissionRepository.kt:84–106](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/FirestoreSubmissionRepository.kt#L84).

| Campo | Contrato observado y límite |
|---|---|
| Productor | tryMatchRankedSubmission, invocado tras evaluación Ranked. |
| Consumidor | Dos documentos submissions, dos users y presentación matchResult en Results/Competitions. |
| Datos, tipos, identidad y significado | Candidatos: playmode RANKED/status EVALUATED/matchStatus PENDING/timestamp≥ahora−48 h; autor distinto y rating actual de users a distancia≤20. No filtra gamemode ni tema en esa consulta. Compara scores (igualdad exacta da DRAW). Resultado: opponentId/name:String, opponentScore:number, outcome WIN/LOSS/DRAW, ratingChange:number→Long Kotlin. ID de submission rival no se persiste en matchResult. |
| Estados, errores y eventos temporales | Tras evaluar escribe PENDING; si encuentra rival actualiza ambos a MATCHED y evaluation.ratingChange; sin candidato vuelve a PENDING. Consulta de candidatos fuera de transacción; dentro lee usuarios, no relee los documentos de envíos ni valida ahí su matchStatus. |
| Comportamiento comprobado | HE: EVALUATED y MATCHED son ejes separados. Matcher escribe rating sólo de usuarios isPlaced. No se ejecutó contención ni se midió orden de candidatos. |
| HU, alcance y aceptación | AC-03/06; S09/S10/S18/S19. H1-C E31/E33/E40/E44/E48. Retirada ligas afecta contadores; Ranked/rating continúan. No se crea elegibilidad estacional. |
| Discrepancia e impacto posible | RI: dos evaluaciones concurrentes podrían elegir el mismo candidato; matchResult identifica usuario rival, no inequívocamente su envío. Comparación entre ejercicios de contextos diferentes queda sin regla original precisa. |
| Verificación necesaria | PR: dos autores compiten por un candidato, evento repetido y match ordinario frente a ghost; registrar IDs, estados, deltas, orden y número de efectos. Probar tipos malformados y rival ya resuelto. |
| Información ausente / decisión humana | Fuente: integración y documentos anonimizados. Q-R: precisiones de compatibilidad de ejercicios y fórmulas; Q-T: admisión/tardíos. Ninguna política de emparejamiento nueva elegida. |

<a id="ct-08"></a>

### CT-08. Resolución ghost de Ranked sin rival

**Fuentes C1:** [ghostMatchProcessor.ts:5–98](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/ghostMatchProcessor.ts#L5); [submissionEvaluationEngine.ts:296–300](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L296); [Results.kt:217–243](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Results.kt#L217); [Results.kt:266–286](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Results.kt#L266).

| Campo | Contrato observado y límite |
|---|---|
| Productor | ghostMatchProcessor programado. |
| Consumidor | Submission.matchStatus/matchResult/evaluation.ratingChange; users.rating; Results. |
| Datos, tipos, identidad y significado | Cada hora consulta hasta 50 Ranked EVALUATED/PENDING con timestamp≤ahora−48 h. Promedio últimos 10 recentScores si hay ≥3; si no, benchmark 65. Margen ±2: WIN +2, LOSS −4, DRAW +1. Escribe matchStatus GHOST, opponentId GHOST, opponentName R8 Average; sólo cambia rating del usuario si isPlaced, mínimo 0. |
| Estados, errores y eventos temporales | La edad sale del timestamp cliente de envío, no de evaluatedAt. Ejecución horaria y límite 50 no garantizan resolución exactamente a las 48 h. recentScores se lee en el momento del job, fuera de la transacción; puede incluir el propio score y otras evaluaciones/torneos históricos. |
| Comportamiento comprobado | HE: ghost usa reglas distintas al match humano y no actualiza rankedWinStreak/rankedLossStreak en su escritura. No relee usuario/envío dentro del callback transaccional mostrado. |
| HU, alcance y aceptación | AC-03/06/07; S09/S10/S18/S19/S21. H1-C E33 confirma comparación con promedio propio tras 48 h; benchmark/muestra/margen/deltas no están precisados allí. Ligas/economía retiradas. |
| Discrepancia e impacto posible | RI: promedio mutable y población mezclada; rating calculado fuera de transacción puede sobrescribir otro cambio; carrera o repetición puede duplicar efectos. No son fallos reproducidos. |
| Verificación necesaria | PR: sin historial/<3/≥3/>10 scores, incluir propio envío, llegada de evaluación tardía, saturación del lote y carrera con matcher; comparar con H1 sin adoptar automáticamente el benchmark. |
| Información ausente / decisión humana | Q-R: promedio sin historial/población/fórmulas; Q-T: cierre y tardíos; Q-D: scores previos. Fuentes: datos/ejecución. No se convierte este job en cierre de temporada. |

<a id="ct-09"></a>

### CT-09. Entrada, abandono y limpieza de sesión Ranked

**Fuentes C1:** [Competitions.kt:82–110](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Competitions.kt#L82); [Competitions.kt:216–245](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Competitions.kt#L216); [UserRepository.kt:245–265](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/UserRepository.kt#L245); [UserRepository.kt:333–342](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/UserRepository.kt#L333); [AppViewModel.kt:165–173](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L165); [AppRoot.kt:89–94](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/AppRoot.kt#L89); [applyMeritAction.ts:34–64](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/users/applyMeritAction.ts#L34); [applyMeritAction.ts:164–230](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/users/applyMeritAction.ts#L164); [rankedSessionCleaner.ts:5–36](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/users/rankedSessionCleaner.ts#L5); [submissionEvaluationEngine.ts:303–339](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L303); [submissionEvaluationEngine.ts:405–414](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L405).

| Campo | Contrato observado y límite |
|---|---|
| Productor | Competitions → UserRepository.applyMeritAction; ABANDON_RANKED servidor; evaluador; finishRankedSession; rankedSessionCleaner. |
| Consumidor | Documento users, UI de entrada/editor y posteriores intentos. |
| Datos, tipos, identidad y significado | Callable action:String, uid de auth; usuario requerido. currentlyInRanked:Boolean y rankedSessionStartedAt:number ms/null/campo eliminado, sin ID de sesión/envío. Entrada cuenta todos los documentos Ranked desde 00:00 UTC (no filtra status), rechaza ≥5; error conteo deja 0. Cliente y servidor cobran Merit histórico. |
| Estados, errores y eventos temporales | Entrada con sesión previa modifica reputación/rachas y abre otra. ABANDON requiere activa, pone false/null y penaliza. Cleaner cada 15 min, inicio≤ahora−60 min: false/delete más reputación. Evaluación éxito/calidad/catch limpia; error al guardar cliente llama finishRankedSession incluso fuera de rama Ranked. Back del editor navega Home sin ABANDON en callback leído. |
| Comportamiento comprobado | HE: tres representaciones de inicio inactivo: null, ausencia y predeterminado del DTO. No se halló llamada cliente a ABANDON_RANKED ni startRankedSession; declarar métodos no prueba uso. Autenticación/cierre de cuenta no equivalen a sesión Ranked. |
| HU, alcance y aceptación | AC-01/02/07; S01/S04–S07/S22. H1-C E27/E29 (3.7, cláusulas económicas superadas); H1-P E10/E12/E14/E16 (1.3/1.4). AL: sin Merit/reputación/castigo de abandono; no reabrir HU amarillas. |
| Discrepancia e impacto posible | RI: una evaluación o error viejo puede cerrar una sesión nueva al escribir sólo por usuario; el conteo fallido puede admitir entrada. Borrar el callable/cleaner entero también quitaría estado y controles no económicos. |
| Verificación necesaria | PR: entrada sin auth/usuario, conteo fallido/límite, sesión previa, error guardado Practice/Ranked, abandono, cleaner y evaluación A tras entrada B; comparar estado con efectos retirados por separado. |
| Información ausente / decisión humana | Q-R: selector sin ligas y reglas no económicas de límite/UTC/reentrada/rachas/cierre; Q-D: borradores/sesiones previas. Valores observados no ratificados. No se eligió ID de sesión ni control sustituto. |

<a id="ct-10"></a>

### CT-10. Placement y revelación del rating

**Fuentes C1:** [submissionEvaluationEngine.ts:303–339](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L303); [Users.kt:30–36](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/Users.kt#L30); [AppViewModel.kt:49–53](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L49); [AppViewModel.kt:85–94](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L85); [AppViewModel.kt:313–325](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L313); [AppViewModel.kt:449–457](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L449); [UserRepository.kt:399–407](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/UserRepository.kt#L399); [PlacementRevealScreen.kt:16–40](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/PlacementRevealScreen.kt#L16).

| Campo | Contrato observado y límite |
|---|---|
| Productor | submissionEvaluationEngine incrementa placement; listener users actualiza currentUser. |
| Consumidor | AppViewModel decide placementReveal; PlacementRevealScreen y markPlacementRevealSeen. |
| Datos, tipos, identidad y significado | placementMatchesPlayed:Int, totalPlacementScore:Double, isPlaced/hasSeenPlacementReveal:Boolean, rating:Long. Seis evaluaciones Ranked; al played≥6 usa promedio totalScore/6 y min(120,floor(promedio/100*120)), marca placed y contador 6. evaluation.ratingChange inicial puede representar rating inicial, no delta de duelo. |
| Estados, errores y eventos temporales | Contador cambia al evaluar, antes de match. justGotPlaced depende de previousUserIsPlaced/currentUser/isSeen al llegar submission; users y submissions se observan por vías distintas. Reveal incluye League actualmente; confirmación escribe hasSeenPlacementReveal. |
| Comportamiento comprobado | HE: placement se basa en evaluaciones, no en seis duelos MATCHED. No se verificó el orden de llegada entre ambos listeners ni la revelación en dispositivo. |
| HU, alcance y aceptación | AC-03/05; S09/S11/S16. H1-C E36/E40/E48 y H1-P E37/E42. No se localizó HU propia para placement; fórmula/6 no se presentan como criterio original. Rating conservado, ligas retiradas. |
| Discrepancia e impacto posible | RI: carrera de usuarios/envíos cambia ruta elegida; presentación de rating inicial puede confundirse con variación del match; la fórmula divide por 6 incluso con contador previo atípico. |
| Verificación necesaria | PR: antes/en sexta evaluación, estado histórico inconsistente, orden de listener invertido y match posterior; comparar reveal/seen/rating. Cálculos estáticos no cuentan como test ejecutado. |
| Información ausente / decisión humana | Q-R: aceptación/presentación placement; Q-T: relación con rating estacional/transición. Fuente: ejemplos previos y ejecución. No se eligió reset ni recalibración. |

<a id="ct-11"></a>

### CT-11. Rating general y resultados inmediatos

**Fuentes C1:** [submissionEvaluationEngine.ts:24–48](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L24); [submissionEvaluationEngine.ts:477–526](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L477); [submissionEvaluationEngine.ts:538–551](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L538); [ghostMatchProcessor.ts:48–78](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/ghostMatchProcessor.ts#L48); [Results.kt:61–115](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Results.kt#L61); [Results.kt:217–287](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Results.kt#L217); [Profile.kt:280–326](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Profile.kt#L280); [ratingCalculator.ts:1–12](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/utils/ratingCalculator.ts#L1).

| Campo | Contrato observado y límite |
|---|---|
| Productor | calculateDynamicRatingChange/matcher; ghost; placement; evaluación R8. |
| Consumidor | users.rating; Results usa matchResult y evaluation; Profile/reveal/cabecera. |
| Datos, tipos, identidad y significado | Rating humano: gap=opponent−my; ajuste limitado [−5,5]=gap*0.05; WIN round(4+ajuste), caps 3 si rating≥150/2 si≥180, mínimo 1; LOSS min(−1,round(−6+ajuste)); DRAW +1; rating final mínimo 0, sólo si placed. Results prioriza delta de matchResult, luego evaluation, y muestra Pending si matchStatus PENDING. |
| Estados, errores y eventos temporales | Score/feedback llega al evaluar; resultado/delta competitivo puede llegar después. Se escriben deltas en ambas submissions aun si un usuario no placed no recibe ese cambio en users.rating. Rachas se actualizan en matcher placed, no en ghost. Método calculateNewRating existe pero búsqueda sólo encontró su definición. |
| Comportamiento comprobado | HE: fórmula invocada es local al motor; no asumir fórmula por nombre de utils. Results muestra texto/score/feedback String y Merit histórico. Perfil cuenta submissions y muestra victorias de torneos; recentScores/bestScore se escriben para Ranked/Tournament, mientras submissionsCount incluye Practice exitoso. |
| HU, alcance y aceptación | AC-03/05/06; S08/S09/S11/S16/S17. H1-W E10/E14/E16; H1-C E36/E38/E40/E42/E44/E46/E48/E50; H1-P E37/E39/E42/E44. HU 1.8 retirada no excluye este feedback inmediato. |
| Discrepancia e impacto posible | RI: delta mostrado puede diferir del realmente aplicado durante placement; métricas visibles no coinciden necesariamente con partidas/% victorias del perfil H1. No hay contrato de rating de temporada aquí. |
| Verificación necesaria | PR: comparar antes/después/delta para ambos usuarios placed/no placed, empate, tramos, ghost y placement; verificar score/feedback y población de perfil con criterios originales. |
| Información ausente / decisión humana | Q-R: reglas/fórmulas y métricas pendientes, fallback/error; Q-T: definición de rating estacional/vínculo general. No se adoptó fórmula futura ni ranking global. |

<a id="ct-12"></a>

### CT-12. Usuario compartido, lectores y escritores anteriores

**Fuentes C1:** [Users.kt:3–37](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/Users.kt#L3); [UserRepository.kt:31–50](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/UserRepository.kt#L31); [UserRepository.kt:76–101](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/UserRepository.kt#L76); [UserRepository.kt:124–173](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/UserRepository.kt#L124); [UserRepository.kt:267–342](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/UserRepository.kt#L267); [userInitializer.ts:4–30](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/users/userInitializer.ts#L4); [submissionEvaluationEngine.ts:281–339](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L281); [evaluationEngine.ts:129–145](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/tournaments/evaluationEngine.ts#L129).

| Campo | Contrato observado y límite |
|---|---|
| Productor | ensureUserExists y createUserProfile; claimUsername/updateEmail/finishSession/markReveal; motores/jobs/callables existentes. |
| Consumidor | UserRepository escucha/lee users → AppViewModel → pantallas; matcher/ghost/entrada y otros procesos leen el mismo documento. |
| Datos, tipos, identidad y significado | Ruta users/{uid}; id:String también embebido. Núcleo identidad/rating/placement/estado/métricas convive con Merit/reputación/torneos. Modelo usa defaults (id/name vacíos, rating=0, isPlaced=false, recentScores=[] etc.). listenToUser/getUserById usan toObject sin copy(id=doc.id); getUsersByIds consulta el campo id. |
| Estados, errores y eventos temporales | Inicializador Auth hace set sin merge; cliente ensure consulta y crea si falta. Orden/race entre ambos no verificado. Listener de usuario retorna sin emitir error cuando falla; AppViewModel puede conservar currentUser anterior. Muchos escritores parciales, algunos métodos declarados sin llamadas encontradas. |
| Comportamiento comprobado | HE: autores de escritura distintos y defaults no prueban datos completos o esquema histórico uniforme. Inventario detallado de lectores/escritores está en TECHNICAL_MAP §4. No se consultaron usuarios reales. |
| HU, alcance y aceptación | AC-02/03/07; S04/S07/S11/S20/S21. H1-P E6/E8/E10/E12/E14/E16/E37/E39/E42/E44/E47/E49; HU 1.1 inconsistente Q-C. AL conserva perfil propio/persistencia; retiradas no autorizan borrar datos. |
| Discrepancia e impacto posible | RI: defaults ocultan ausencia de identidad/rating/placement; writers retirados pueden alterar campos del núcleo; inicializaciones concurrentes pueden competir. Eliminar Users entero rompería consumidores conservados. |
| Verificación necesaria | PR: docs antiguos sin id/campos, deserialización numérica/null, auth inicializador frente a ensure, actualizaciones simultáneas y listener fallido; registrar identidad y cada escritor autorizado en entorno de prueba. |
| Información ausente / decisión humana | Q-D: inventario y política histórica; Q-R: población métricas/estado; Q-C: criterios correctos 1.1. Fuentes: reglas Firebase/fixtures; no se define entidad futura ni migración. |

<a id="ct-13"></a>

### CT-13. Envíos previos, consultas, guardado y retención

**Fuentes C1:** [FirestoreSubmissionRepository.kt:37–172](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/FirestoreSubmissionRepository.kt#L37); [applyMeritAction.ts:121–160](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/users/applyMeritAction.ts#L121); [submissionSavedTrigger.ts:7–37](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionSavedTrigger.ts#L7); [pruneOldSubmissions.ts:8–41](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/pruneOldSubmissions.ts#L8); [submissionEvaluationEngine.ts:396–398](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L396); [FirestoreTournamentRepository.kt:80–124](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/FirestoreTournamentRepository.kt#L80).

| Campo | Contrato observado y límite |
|---|---|
| Productor | Escritor Kotlin, evaluación/matcher/ghost; SAVE_SUBMISSION; borrado explícito y pruneOldSubmissions. |
| Consumidor | Lista/historial/recentes/último; dailyStats; contadores guardados; consultas de entrada/ghost/matcher. |
| Datos, tipos, identidad y significado | Raíz submissions por authorId/timestamp. Recientes Ranked: últimas 48 h, máximo 10; todas sin ese límite de modo. isSaved:Boolean, savedSubmissionsCount. Guardar usa submissionId, comprueba existencia/autor/no guardada, con cobro Merit histórico. Torneo escribe aparte tournaments/{tournamentId}/submissions/{userId}. |
| Estados, errores y eventos temporales | Tras éxito evaluación se lanza prune: consulta hasta 100 no guardadas, conserva primeras 10 y borra el resto de ese lote; no filtra status/playmode/matchStatus. Unsaved/deleted decrementan contador si correspondía. No hay una conservación ilimitada demostrada. |
| Comportamiento comprobado | HE: retención automática existente puede tocar Ranked pendiente de duelo y datos que lectores posteriores necesitan. Se documentó la lógica, no se ejecutó ningún borrado. Ruta de torneo no equivale a ruta de núcleo. |
| HU, alcance y aceptación | AC-03/06/07; S09/S10/S20/S21. H1-P E27/E29/E47/E49; H1-W E18/E20 (Practice sin historial competitivo). Guardado y compras previos se inventarían como compatibilidad; cobro retirado, sin asumir obligación de toda UI histórica. |
| Discrepancia e impacto posible | RI: poda antes de ghost elimina un pendiente; estadísticas/historial dependen de datos ya podados; contador puede desincronizarse ante repetición/error. No prueba pérdida real ni autoriza reparar/borrar históricos. |
| Verificación necesaria | PR: 10/11/>100 documentos mixtos, pending/failed/matched/saved, consultas con campos ausentes, contador tras cambios/repeticiones; verificar conservación exigida antes de ejecutar mantenimiento. |
| Información ausente / decisión humana | Q-D: retención/consulta de previos; Q-T: historial/tardíos; Q-R: población competitiva. Fuentes: inventario anonimizado e índices/reglas auténticos. No se eligen nuevas colecciones ni datos de backfill. |

<a id="ct-14"></a>

### CT-14. Borrador local e identidad de reanudación

**Fuentes C1:** [DraftManager.kt:5–25](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/utils/DraftManager.kt#L5); [Writing.kt:50–93](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Writing.kt#L50); [Writing.kt:98–112](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Writing.kt#L98); [Writing.kt:324–346](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Writing.kt#L324); [AppRoot.kt:84–94](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/AppRoot.kt#L84).

| Campo | Contrato observado y límite |
|---|---|
| Productor | Writing autoguarda y limpia; DraftManager almacena SharedPreferences. |
| Consumidor | Writing inicializa userText desde getDraft. |
| Datos, tipos, identidad y significado | Preferencias inkr8_drafts; clave draft_{gamemode}_{playmode}_{tournamentId o none}; valor String de texto. Clave sin uid/themeId/topicId/submissionId/período; valor sin versión, fecha, palabras asignadas ni contexto. TOURNAMENT sí incluye tournamentId. |
| Estados, errores y eventos temporales | Carga al inicializar pantalla; autoguardado tras 3 s si texto no vacío. Vaciar texto no limpia por esa rama. Al pulsar envío se limpia borrador y userText antes de confirmar persistencia; al reabrir se sortean palabras/contexto por otros caminos. |
| Comportamiento comprobado | HE: el comentario de Writing dice successful submission, pero la llamada clearDraft precede onAddSubmission. No se reprodujo pérdida o intercambio entre cuentas; es discrepancia de orden estático. |
| HU, alcance y aceptación | AC-02/07; S07/S22/S23. No HU de borrador propia localizada en H1; soporte de estado/persistencia permanece por AL. HU de torneos excluidas no se usan como obligación futura. |
| Discrepancia e impacto posible | RI: borrador compartido entre cuentas/contextos del mismo modo; pérdida ante guardado fallido y reanudación con restricciones diferentes. Retirada de torneos no decide retención de su texto existente. |
| Verificación necesaria | PR: cambio de cuenta/tema/palabras, texto vacío, fallo guardado, salir/reabrir y borrador de torneo previo, sin convertir ni borrar datos reales. |
| Información ausente / decisión humana | Q-D: política de reanudación/retención/consulta; Q-R: error de envío/cierre. Fuente: inventario local anonimizado. No se eligió un formato futuro ni conversión automática. |

<a id="ct-15"></a>

### CT-15. Correspondencia con UML y BD históricos

**Fuentes C1:** [SystemConfig.kt:12–19](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/utils/SystemConfig.kt#L12); [FirestoreSubmissionRepository.kt:16–32](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/FirestoreSubmissionRepository.kt#L16); [TopicRepository.kt:13–35](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/TopicRepository.kt#L13); [Submissions.kt:3–20](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/Submissions.kt#L3); [FirestoreEvaluation.kt:3–12](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/repository/FirestoreEvaluation.kt#L3); [AppViewModel.kt:140–175](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L140); [AppViewModel.kt:313–325](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/viewmodel/AppViewModel.kt#L313).

| Campo | Contrato observado y límite |
|---|---|
| Productor | I1, figuras 5.20, 5.22, 5.23 (modelos históricos; M1 editable no consultado de nuevo). |
| Consumidor | Documentación y revisión de trazabilidad; comparación con C1, no consumidor ejecutable. |
| Datos, tipos, identidad y significado | BD fig.5.23 dibuja users/{userId}/submissions y themes/{themeId}/topics, además username/elo/rank/score; C1 raíz submissions/topics con authorId/themeId, name/rating/evaluation.finalScore. UML clases fig.5.22 muestra User.rating y Evaluation.finalScore (distinto vocabulario de su propia BD), sin status/matchStatus/matchResult/placement completos. |
| Estados, errores y eventos temporales | Secuencia fig.5.20 separa EVALUATED/PENDING y MATCHED, y usa getLastSubmissionRealtime; representa orquestación en AppRoot y errores via ErrorScreen. C1 la reparte con AppViewModel y FAILED navega Home. No se afirma que el histórico confunda EVALUATED con MATCHED. |
| Comportamiento comprobado | HE + lectura visual directa I1: imágenes originales extraídas sin alteración, verificadas contra bytes del DOCX. Copias: DOC-005/I1_image52.jpg (5.20), I1_image29.jpg (5.22), I1_image10.png (5.23). El editable M1 no se recuperó; no se conoce otra revisión. |
| HU, alcance y aceptación | AC-01/02/03/07; S03/S08/S10/S20. Trazabilidad académica TR-07/TR-11/SU-01; figuras son antecedentes, no aceptación futura ni autorización para conservar áreas retiradas. |
| Discrepancia e impacto posible | RI: usar dibujo histórico como contrato de ruta/tipo/estado lleva a analizar otro modelo. Omitir estados en clases no demuestra su ausencia en secuencias/código. |
| Verificación necesaria | PR: reconciliar mensajes/campos/rutas de cada figura con código y criterios, dejando diferencias explícitas; revisar editable original si se propone actualizar modelos en etapa posterior. |
| Información ausente / decisión humana | Fuente: versión/editables M1 y modelo aceptado por estudiantes. Q-D: compatibilidad histórica; diseño futuro corresponde a chats 7/8. No se seleccionó modelo de BD ni clases nuevas. |

<a id="ct-16"></a>

### CT-16. Estadísticas periódicas y ausencia de contrato estacional

**Fuentes C1:** [dailyStatsSnapshot.ts:12–25](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/stats/dailyStatsSnapshot.ts#L12); [dailyStatsSnapshot.ts:50–65](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/stats/dailyStatsSnapshot.ts#L50); [dailyStatsSnapshot.ts:104–122](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/stats/dailyStatsSnapshot.ts#L104); [weeklyStatsSnapshot.ts:15–37](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/stats/weeklyStatsSnapshot.ts#L15); [weeklyStatsSnapshot.ts:87–104](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/stats/weeklyStatsSnapshot.ts#L87); [monthlyStatsSnapshot.ts:4–30](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/stats/monthlyStatsSnapshot.ts#L4); [monthlyStatsSnapshot.ts:76–92](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/stats/monthlyStatsSnapshot.ts#L76).

| Campo | Contrato observado y límite |
|---|---|
| Productor | dailyStatsSnapshot; weeklyStatsSnapshot/monthlyStatsSnapshot agregan registros diarios. |
| Consumidor | Registros stats/daily\|weekly\|monthly/records; no se identificó en lo leído un consumidor que implemente HU 3.27/3.28. |
| Datos, tipos, identidad y significado | Registros agregados con IDs día/semana/mes, totales de envíos/scores/usuarios/torneos/Merit. No definen seasonId, participación, rating estacional, clasificación/posición propia ni rating/posición de cierre por usuario. Ausencia de coincidencias season/temporada en producción Kotlin/functions es búsqueda, no prueba universal de inexistencia. |
| Estados, errores y eventos temporales | Diario consulta rango timestamp [inicio ayer UTC,inicio hoy UTC); semanales/mensuales leen días. Job periódico no constituye ciclo de temporada; score tardío o envío podado puede cambiar qué estaba disponible al agregarse. |
| Comportamiento comprobado | HE: lectura de jobs y búsqueda textual sin coincidencias. No se inspeccionó Firestore desplegado ni versiones posteriores. No hay ejecución estacional nueva. |
| HU, alcance y aceptación | AC-04–AC-06; S12–S19/S21/S24/S25. H1-C E109/E111/E113/E115: rating de temporada/posición destacada; posición final/rating de cierre. Recompensas E113 pendiente sin Merit. |
| Discrepancia e impacto posible | RI: reutilizar calendario o agregados como especificación estacional inventaría reglas; no se puede atribuir historiales personales estacionales a stats periódicas. |
| Verificación necesaria | PR: recuperar fuente adicional si alguien afirma implementación de temporadas; después contrastar contratos contra decisiones Q-T/Q-D, sin inferir reset/período/población. |
| Información ausente / decisión humana | Q-T: calendario/zona/evento/pertenencia/elegibilidad/cálculo/empates/vínculo rating-placement/cierre/tardíos/recompensas; Q-D: previos. No se eligieron colecciones, jobs o fórmulas de temporadas. |

## Uso en diagnóstico y aceptación

El inventario de escritores y los tres ejes de estado del mapa completan esta matriz. Puede examinarse la distribución de responsabilidades, los cambios que atraviesan un mismo método, el acoplamiento y las diferencias de contrato con evidencia estática. La matriz no declara violaciones SOLID por tamaño ni elige soluciones.

**Verificaciones técnicas pendientes:** R8/configuración auténticos; documentos y borradores anonimizados; serialización/deserialización; concurrencia, repetición, orden de eventos, error y ejecución Android/Functions/Firebase. B1 histórico no cubre estos contratos. Los 25 escenarios S01–S25 permanecen sin ejecutar y ningún AC queda cerrado íntegramente.

**Decisiones funcionales pendientes:** Q-R/Q-T/Q-D/Q-C del chat 4. No hace falta elegirlas para registrar el código actual; sí condicionan aceptación final e intervención. No se repite solicitud H1/Q-F, magnitud del ranking ni campos históricos ya recuperados. P1 §2.7 y la [matriz académica](ACADEMIC_COMPLIANCE_MATRIX.md) mantienen autoría, explicación y validación estudiantil.

## Vigencia CORE-FINAL-001 — contratos resueltos en alcance local

Marco decidió explícitamente revisión durante persistencia, bloqueo de segundo
envío, UID/contexto del borrador con históricos conservados y reintento sólo de
consulta del ID. Implementación/pruebas Codex; no decisiones pendientes por silencio
ni edición estudiantil exigida. CT-01/03/04/06/14/15 y AC-01/03/07 relacionados tienen
aceptación local de los criterios explícitos; no se redefine CT-02/payload/invalids,
proveedor real, temporadas o retirada completa. [Tabla, contratos exactos y límites](CORE_FINAL_AUDIT.md).
46 Android/19 unit/11 Ranked+2 ghost+1 lifecycle PASS, dobles declarados, sin cierre
integral del producto o rúbrica. Filas históricas se conservan como antecedentes.


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
