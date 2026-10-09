# Evidencias de IA y decisiones — Inkr8

**Vigente FIN-002 — 09/10/2026:** cierre y entrega revisable documentados en
[FINAL_DELIVERY.md](FINAL_DELIVERY.md), con 6 Android PASS, reglas nuevas
propuestas y límites externos/académicos explícitos. Los estados inferiores son
antecedentes preservados; no revocan DEC-AI-AUTH-001, las decisiones respondidas
ni el permiso actual de publicar rama/PR, sin fusión o despliegue.

**Versión 0.1 · 04/10/2026 · America/Lima · chat 11 · U17/DEC-24.** Inventario documental asistido por Codex, con revisión crítica humana pendiente. Sin cambio de código, pruebas nuevas, implementación ni aceptación cerrada. Este inventario **no acredita cumplimiento individual**.

Se recuperaron nueve archivos auténticamente localizados: siete artefactos retrospectivos A1 y dos adjuntos del baseline. Se conservaron selecciones de mensajes devueltos por la aplicación para baseline, chat 10 y coordinación. El prompt adjunto del baseline coincide con el bloque de A1/02 al normalizar saltos de línea y espacios exteriores. **La conversación previa de refinamiento con ChatGPT sigue sin exportación original disponible.** Las seis identidades/roles están confirmadas; aportes técnicos, revisión crítica, ejecución, declaraciones e influencia individuales continúan sin evidencia suficiente.

Fuentes canónicas: [índice de procedencia](SOURCE_INDEX.md), [decisiones](DECISIONS.md), [estado](STATUS.md), [alcance](REFACTOR_SCOPE.md), [aceptación](ACCEPTANCE_MATRIX.md), [requisitos](PROFESSOR_REQUIREMENTS.md), [matriz académica](ACADEMIC_COMPLIANCE_MATRIX.md), [protocolo propuesto](EVIDENCE_PROTOCOL.md), [trazabilidad individual](INDIVIDUAL_TEST_TRACEABILITY.md) y [registro de esta tarea](evidence/DOC-011/record.md). Los documentos anteriores conservan su etapa; A1 estaba sin copia en el paquete antes de esta recuperación.

## 1. Requisitos oficiales contrastados

Lectura directa de [P1](evidence/sources/P1.pdf), [P2](evidence/sources/P2.pdf), [P3](evidence/sources/P3.xlsx), [P4](evidence/sources/P4.docx) y [P5](evidence/sources/P5.docx). [OFFICIAL_READ](evidence/DOC-011/OFFICIAL_READ.json) conserva páginas físicas, celdas y párrafos XML exactos con hash. No se comprobó vigencia posterior en aula virtual.

| Requisito / IDs académicos | Fuente oficial exacta | Qué se requiere y qué no demuestra este inventario |
|---|---|---|
| Transparencia y proceso: IA-01/IA-13 | P1 pp. físicas 1–3 §§1.1–1.4, 10–11 §3.2.c–g y 21 «Conductas indebidas» §7.1 | Conservar prompts/respuestas y secuencia, distinguir IA/estudiante, declarar limitaciones. No fabricar conversaciones, autoría o revisiones. Las copias parciales deben identificarse como tales. |
| Declaración individual: IA-02 | P1 pp. 14–15 §§5.1–5.4; P2 p. 4 §1.2.9 «Declaración» | Herramienta, objetivo, prompts, respuestas e influencia estimada; apartado del entregable, anexo y bitácora cuando corresponda. P2 exige declaración de cada integrante. No declarar uso o ausencia de uso por una persona sin su fuente. |
| Influencia estimada | P1 p. 14 §5.2 | **Baja ≤30%, media 31–70%, alta >70%**. Son escalas oficiales. El porcentaje concreto lo estima y fundamenta el estudiante sobre su trabajo real; no se calcula con número de mensajes, archivos, líneas, commits o tests. |
| Análisis crítico e intervención: IA-03 | P1 pp. 16–17 §§6.1–6.4 y 10–11 §3.2 | Evaluar corrección/completitud, errores/supuestos/limitaciones, alternativas, adopción/descarte justificado y correcciones/adaptaciones humanas evidenciadas. Las razones redactadas por IA no son análisis del estudiante. |
| Pruebas asistidas: IA-08 | P1 p. 6 §2.5 y p. 7 §2.7 | Validación y ejecución estudiantiles, casos sobre lógica escrita por el alumno. El inventario y los cálculos CC propuestos no prueban cobertura, ejecución individual ni autoría de lógica. |
| Autoría estructural y crítica: IA-11/IA-12 | P1 p. 7 §2.7 | BD/relaciones definidas por estudiantes, algoritmos críticos escritos por ellos e integración frontend/backend manual. C1, un commit con nombre o un diagrama asistido no certifican esos actos. |
| Evidencia de trabajo y anexos | P2 p. 3 §§1.2.5–1.2.6; p. 4 «Estructura de la entrega» | Archivos identificados, relacionados con anexos y trabajo real. La estructura de ZIP es una guía de organización, no un paquete ya entregado. |
| Contribución y pares: IN-01/IN-02 | P4 §2.4.5, párrafos XML 284–287, y §12.2, 2088–2094; P2 pp. 3–4 §1.2.8; P5 instrucción general párrafo 3 e indicaciones 97–105; P3 Rubrica!D7/D8 | Resumen individual sustentado y una ficha por integrante para R1 y otra para R2, valorada con hechos observables. P4 Juan Pérez/Ana Torres y P5 ejemplo 19/20 son ficticios, sin valor probatorio para Inkr8. Los roles no acreditan colaboración ni aportes. |
| Pruebas e impacto individuales vinculados | P3 Rubrica!D18/D20; P4 §§10.1–10.5 | Paquetes sustentados de todos; no repartir los 22 tests históricos. CC >4, caja negra >4 campos, un método con ≥4 casos. Impacto individual tampoco se acredita por inventario. |
| Dominio y sustentación: SU-03/SU-04 | P1 pp. 8, 12–13 §3.3, 18 §§7.3–7.4 y 20 «Restricciones» §12.2.b; P3 Rubrica!F28/G28 | Explicar, justificar, modificar y resolver variaciones autónomamente. Preparación documental no es evaluación de dominio. **No usar IA durante la sustentación real.** |

EVIDENCE_PROTOCOL sigue siendo propuesta operativa; D2 A/B/C sigue propuesto. Ninguno exige por sí mismo una confirmación universal para cada acción documental autorizada.

## 2. Clases de evidencia y disponibilidad

| Clase | Significado en este paquete | Límite |
|---|---|---|
| Copia literal de archivo disponible | Bytes conservados desde una ubicación comprobada; hash origen/destino en manifiesto | Igualdad/integridad, sin autenticación independiente de autoría o aprobación |
| Texto recibido/transcrito | U9/U11/U12/U13/U14 y texto U17 según procedencia de cada registro | Puede conservar un mensaje completo; no toda su conversación/contexto/metadatos |
| Extracto seleccionado | SESSION_RECORD, DOC-003/004/006, HUMAN_INPUT DOC-010 | Omisiones declaradas; no reconstruir lo omitido |
| Selección de herramienta de chat | Mensajes userMessage/agentMessage devueltos por read_thread, con IDs/secuencia/paginación | Vista de aplicación, no exportación íntegra independiente; comandos/diffs/herramientas/razonamiento omitidos |
| Artefacto/síntesis IA | A1, B1, G1/G2 y documentos DOC | Un resultado producido o resumen retrospectivo; no convierte narración en interacción original ni en crítica humana |
| Resultado de comprobador | VERIFICATION, hashes, lectura XML/Git | Resultado documental o histórico acotado; no resultado actual del producto ni revisión humana |

### Recuperación efectivamente realizada

[RECOVERY_MANIFEST](evidence/DOC-011/RECOVERY_MANIFEST.json) fija los nueve localizadores, copias, hashes, tamaños y fecha de lectura. No se consultó Drive ni GitHub en esta tarea. La carpeta A1 local fue localizada desde el chat del baseline, no deducida de un resumen de Drive.

| Fuente recuperada | Copia / procedencia | Estado y límite |
|---|---|---|
| A1/00 | [Resumen del proceso](evidence/DOC-011/recovered/00_RESUMEN_DEL_PROCESO.md) | Artefacto retrospectivo Codex; relata actos del equipo sin identificar revisores ni análisis individual. |
| A1/01 | [Idea inicial](evidence/DOC-011/recovered/01_IDEA_INICIAL_DEL_EQUIPO.md) | Formulación retrospectiva asistida solicitada por el usuario como idea aproximada. Su primera persona no prueba texto original escrito por estudiantes. |
| A1/02 | [Prompt refinado](evidence/DOC-011/recovered/02_PROMPT_REFINADO_CON_CHATGPT.md) | Contiene instrucción final y declara que no es transcripción completa de ChatGPT. Bloque contrastado con adjunto disponible, sin reconstruir conversación de refinamiento. |
| A1/03 | [Inspección inicial](evidence/DOC-011/recovered/03_RESPUESTA_INICIAL_DE_CODEX.md) | Síntesis retrospectiva de respuesta/propuesta; la respuesta de herramienta disponible se conserva por separado. |
| A1/04 | [Decisión/autorización](evidence/DOC-011/recovered/04_DECISION_Y_AUTORIZACION_HUMANA.md) | Síntesis de autorización; mensaje humano literal disponible en APP_READ_BASELINE respalda restricciones. No identifica revisión técnica por integrante. |
| A1/05 | [Resultado final](evidence/DOC-011/recovered/05_RESULTADO_FINAL_DE_CODEX.md) | Resultado histórico sintetizado, no nueva ejecución. |
| A1/06 | [Trazabilidad Git](evidence/DOC-011/recovered/06_TRAZABILIDAD_Y_EVIDENCIA_GIT.md) | Síntesis histórica; la fila «Equipo + Codex» no basta para atribuir validación a personas. |
| Adjunto del encargo inicial baseline | [BASELINE_INITIAL_TASK](evidence/DOC-011/recovered/BASELINE_INITIAL_TASK.txt) | Archivo auténtico disponible desde localizador adjunto del chat; copia byte a byte. Prompt final a Codex, sin conversación de ChatGPT. |
| Adjunto de cierre documental baseline | [BASELINE_DOCUMENTARY_CLOSURE_TASK](evidence/DOC-011/recovered/BASELINE_DOCUMENTARY_CLOSURE_TASK.txt) | Instrucción auténtica para producir A1; distingue idea aproximada, narración solicitada y evidencia exacta faltante. No prueba por sí sola revisión del equipo. |
| Chat «Crear baseline de testing de Inkr8» | [APP_READ_BASELINE](evidence/DOC-011/APP_READ_BASELINE.json), ID 01a0fddd-5f98-7c70-bb1c-0eab564a0d80 | Cuatro turnos devueltos; 33 mensajes seleccionados. hasMore=false sólo en esta vista. Incluye autorización y aprobación/versionado humanos, respuestas Codex y localizadores de adjuntos. No se conservan todos los outputs; había truncamientos en salidas devueltas. |
| Chat «Inkr8 — Pruebas y trazabilidad por integrante» | [APP_READ_CHAT10](evidence/DOC-011/APP_READ_CHAT10.json), ID 01a1089c-2200-7942-a331-23ae41e56209 | Dos turnos devueltos; 16 mensajes seleccionados, U15/U16/corrección y transición U17. includeOutputs=false. No exportación íntegra ni prueba adicional de contribución. |
| Chat «Inkr8 — Coordinación y estado» | [APP_READ_COORDINATION](evidence/DOC-011/APP_READ_COORDINATION.json), ID 01a0fed0-f861-74d0-ba0f-786324cf8e05 | Tres páginas/11 turnos devueltos; selección de siete turnos pertinentes. Última página hasMore=true; lectura acotada. Corrobora U5/U6, historia pendiente y propuestas IA. No captura privada copiada. |
| Inicialización chat 11 / U17 | [U17_TASK](evidence/DOC-011/U17_TASK_2026-10-04.txt) y [entrada humana/procedencia](evidence/DOC-011/HUMAN_INPUT_2026-10-04.md) | Texto del encargo recibido por el puente, conservado desde argumento prompt de create_thread del origen; autorización humana es el mensaje de transición, no el prompt técnico redactado por IA. |
| Conversación de refinamiento con ChatGPT | Localizador previo: SOURCE_INDEX A1, carpeta Drive 09/01; A1/02 y adjunto inicial comprobados | **No disponible como conversación original/exportación completa en las ubicaciones leídas.** Se presentó consulta agrupada de ubicación; no se reconstruye ni se presume inexistencia en otras cuentas. |

Los archivos A1 son untracked en la copia externa inspeccionada; no pertenecen al árbol del commit C1. Se preservan como archivos disponibles, sin afirmar igualdad con Drive ni autoría por Git. [GIT_READ](evidence/DOC-011/GIT_READ.json) identifica por separado C1 limpio y la copia local A1 con archivos ajenos sin inspeccionar. No se tocaron esas carpetas.

Los cuatro enlaces relativos internos de A1/05 conservan sus bytes originales y se verifican desde la ubicación de origen, donde resuelven a B1. Desde la copia recovered no son enlaces operativos; usar [B1 en este paquete](testing-baseline/) o los localizadores del manifiesto. No se reescriben artefactos históricos para adaptar sus rutas.

## 3. Inventario por tarea y aporte humano disponible

En todas las filas documentales la herramienta registrada es **Codex; modelo exacto no registrado en la evidencia de interacción conservada**. ChatGPT figura en A1 como refinamiento declarado, sin modelo confirmado. El ID de chat o título no permite inferir modelo. Objetivo, artefacto y verificación son de la tarea; no se asignan a un integrante como paquete académico.

| ID local / tarea, fecha conocida | Interacción/entradas y contexto disponible | Salida y fuente/versión | Intervención humana acreditada / decisión | Verificación y eslabones pendientes |
|---|---|---|---|---|
| IA-E01 · baseline y cierre A1 · 02/10/2026 según turnos y B1 | Adjunto inicial + cuatro turnos APP_READ_BASELINE + adjunto cierre; A1/00–06 retrospectivos | Arnés/tests B1 en C1, PRE anterior; siete A1 locales | Mensaje autoriza arnés con condiciones; otro declara baseline aprobado y autoriza versionado. Participación colectiva relatada, análisis por integrante no disponible | 22 resultados históricos; G1 registra reejecución auditada. No ejecución nueva. Refinamiento ChatGPT y revisión/autoría/ejecución estudiantiles ausentes |
| IA-E02 · contextualización/mapa histórico · 02–03/10/2026, G1/G2 | [U1](evidence/sources/U1.txt), U2/U3; exportación íntegra no disponible aquí | [G1](reference/AUDIT_2026-10-02.md), [G2](reference/TECHNICAL_MAP_2026-10-03.md), C1 | U2 comunica aprobación D1; U3 captura PO resuelve Merit. No aprobación automática de síntesis | Snapshots preservados y baseline histórico; no revisión exhaustiva ni producto completo |
| IA-E03 · DOC-001 · 03/10/2026 | U4/SESSION_RECORD: extractos seleccionados, sin conversación completa | [DOC-001](evidence/DOC-001_DOCUMENTATION.md), AGENTS y docs iniciales | Autorización de creación DEC-08; revisión adicional por IA, sin revisión grupal acreditada | Verificación documental histórica; síntesis actuales evolucionaron, no son salida congelada DOC-001 |
| IA-E04 · DOC-002/publicación/fusión · 03/10/2026 | U5/U6 corroborados por selección coordinación; [registro publicado](https://github.com/Rnz5/Inkr8-ISW2/blob/1951443b715c46daf91d0482b38fd159e43b9db4/docs/evidence/DOC-002_PUBLICATION.md) sin copia local leída de nuevo | PR #1, publicación 1951443b715c46daf91d0482b38fd159e43b9db4; merge 7d879c22030527e1d5f5003ad84b76b750d3b29e | Autoriza publicar y después integrar sólo docs; no aprobación grupal de cada página | 13 Markdown históricos según STATUS/chat; GitHub remoto no reconsultado en DOC-011 |
| IA-E05 · DOC-003 · 03/10/2026 | [Extracto auténtico y registro](evidence/DOC-003_ACADEMIC_COMPLIANCE.md), sin exportación íntegra | [Matriz académica](ACADEMIC_COMPLIANCE_MATRIX.md), P1–P6 | Encargo DEC-13, no responsables/notas elegidos | [Comprobaciones](evidence/DOC-003_VERIFICATION.json); análisis individual pendiente |
| IA-E06 · DOC-004 · 04/10/2026 | [Extractos U7/U8](evidence/DOC-004_ACCEPTANCE.md), H1 recibido/hash y recuperación | [AC-01–07](ACCEPTANCE_MATRIX.md) 0.2, S01–S25; H1/D1/C1 | U8 excluye amarillo; Q-F resuelta. No elección de vacíos Q-R/Q-T/Q-D/Q-C | [Verificación](evidence/DOC-004_VERIFICATION.json); 25 escenarios sin ejecutar, ningún AC cerrado íntegramente |
| IA-E07 · DOC-005 · 04/10/2026 | [U9 completo disponible y registro](evidence/DOC-005/record.md); sin toda la conversación | [Mapa](TECHNICAL_MAP.md)/[CT-01–16](CONTRACT_MATRIX.md), C1/I1 | Encargo DEC-17, sin revisión humana de contratos | [Verificación](evidence/DOC-005/VERIFICATION.json); estática/figuras, sin ejecución |
| IA-E08 · DOC-006 · 04/10/2026 | [U10 en extractos y consulta](evidence/DOC-006/record.md); sin exportación completa | [DG-01–17](SOLID_DIAGNOSIS.md), C1/CT/AC | Encargo DEC-18; pregunta de aprendizaje sin respuesta registrada | [Verificación](evidence/DOC-006/VERIFICATION.json); cuatro hipótesis estructurales por validar, no infracciones definitivas |
| IA-E09 · DOC-007 · 04/10/2026 | [U11 transcrito completo y consulta](evidence/DOC-007/record.md); no secuencia íntegra | [12 alternativas](DESIGN_ALTERNATIVES.md), DG-13/09/07/01 y restricciones | Encargo DEC-19; ninguna alternativa elegida/adoptada por el equipo | [Verificación](evidence/DOC-007/VERIFICATION.json); comparación provisional, análisis/correcciones humanos pendientes |
| IA-E10 · DOC-008 · 04/10/2026 | [U12 completo disponible y registro](evidence/DOC-008/record.md); no exportación íntegra | [12 vistas/14 bloques/31 mensajes](ARCHITECTURE_UML.md), C1/I1/CT | Encargo DEC-20; sin rediseño/selección estudiantiles | [Verificación](evidence/DOC-008/VERIFICATION.json); Mermaid no acredita CASE ni autoría final |
| IA-E11 · DOC-009 · 04/10/2026 | [U13 recibido por puente y registro](evidence/DOC-009/record.md); sin exportación íntegra de origen | [5 capas/23 brechas](BASELINE_REPRODUCIBILITY.md), PRE/C1/B1/XML | Encargo DEC-21; sin expected/reglas/entorno reconstruidos | [Verificación](evidence/DOC-009/VERIFICATION.json); contraste estático, no reejecución |
| IA-E12 · DOC-010 · 04/10/2026 | [U14](evidence/DOC-010/U14_TASK_2026-10-04.txt), [U15/U16](evidence/DOC-010/HUMAN_INPUT_2026-10-04.md), APP_READ_CHAT10 | [Candidatos/casos/matriz](INDIVIDUAL_TEST_TRACEABILITY.md), cinco métodos/tres frentes, C1/P1/P3/P4 | DEC-22/23: equipo confirmado y ausencia de reparto/evidencias; sin revisión/elección de casos | [Verificación](evidence/DOC-010/VERIFICATION.json) histórica: 147 enlaces/24 hashes/6 XML/C1. CC IA 8/10 filtros, 5 rating, 2 Factory; no cobertura dinámica |
| IA-E13 · DOC-011 · 04/10/2026 | U17, recuperación/localizadores y selecciones de herramienta; conversación actual sin exportación íntegra | Este inventario, matriz de decisiones, seis fichas vacías y registro | DEC-24 autoriza preparación; no ratifica el contenido. Sin asignaciones o revisión humana nuevas | [VERIFICATION](evidence/DOC-011/VERIFICATION.json), sólo documentación/integridad; IA individual pendiente |

Los documentos canónicos actuales son artefactos mantenidos, no transcripciones de respuestas originales. Los hashes de esta etapa fijan su versión leída/entregada; los manifiestos históricos fijan las copias que sí preservaban bytes. La ausencia de una exportación íntegra no demuestra ocultamiento ni ausencia de trabajo.

### Secuencia histórica del baseline que sí puede relacionarse

1. Usuario entrega prompt final a Codex mediante adjunto inicial; refinamiento previo ChatGPT sólo relatado en A1.
2. Codex informa bloqueo y propone arnés. Propuesta inicial incluía app/src/test; el mensaje humano posterior exige todo el arnés en testing-baseline y fuentes reales sin copiarlas.
3. Usuario autoriza exclusivamente ese arnés con restricciones. Ésta es la evidencia de DEC-07, sin atribuir análisis técnico a un estudiante específico.
4. Codex implementa, ajusta compatibilidad/configuración aislada y corrige una expectativa tras 21 PASS/1 FAIL. Es corrección de IA; no cambio de producción ni corrección estudiantil acreditada. Final histórico: 22 PASS.
5. Usuario declara baseline aprobado y pide comprobar/versionar/publicar las dos carpetas; Codex registra cierre/commit C1. La aprobación explícita no incluye un análisis crítico individual conservado.
6. Usuario pide cierre documental y artefactos A1, con idea base aproximada; Codex redacta los siete archivos. Por eso A1/01 y las menciones «equipo revisó/verificó» no son actas ni transcripción independiente de esa revisión.

## 4. Matriz de decisiones, autorizaciones y otros estados

Esta matriz deriva de DECISIONS; **no crea decisiones humanas nuevas**. Fechas de recepción/documento conocidas no se transforman en fechas de actos grupales. DH = decisión humana comunicada; AT = autorización de tarea; HO = hecho observado; RH = resultado histórico; PI = propuesta IA; CO = consulta; AE = ausencia de evidencia.

| ID | Fuente | Fecha conocida | Autoridad / clase | Alcance | Estado | Artefacto | Consecuencia y límite |
|---|---|---|---|---|---|---|---|
| DEC-01 | D1/U2/SESSION_RECORD | Recibido 03/10; acto grupal desconocido | Marco comunica grupo / DH | Delimitación D1 | Confirmada | REFACTOR_SCOPE | No permiso de implementación |
| DEC-02 | U2/U3 | Recibido 03/10 | Marco/PO / DH | Retirar torneos/ligas/reputación | Confirmada | REFACTOR_SCOPE | Retirada deliberada, datos sin borrado autorizado |
| DEC-03 | U3 | Recibido 03/10; fecha mensaje PO desconocida | PO comunicado / DH | Merit completo, ganancias/usos/Ranked | Confirmada | REFACTOR_SCOPE | Sin moneda sustituta; Ranked/rating permanecen |
| DEC-04 | U2/U3/I1/H1 | Recepción 03/10; H1 04/10 | Marco/PO / DH | Incorporar HU 3.27/3.28 | Inclusión confirmada | AC-04–06 | Reglas detalladas siguen abiertas |
| DEC-05 | D1/U2 | Aprobación grupal sin fecha exacta | Grupo comunicado / DH | Núcleo, Practice, Ranked/rating, auth, perfil, soporte | Confirmada | REFACTOR_SCOPE | Separar economía retirada de comportamiento conservado |
| DEC-06 | D2 | Actualizado 02/10 | Equipo según D2 / DH | Coordinación Marco/Renzo | Registrada | D2 | No autoría/test/revisor por método |
| DEC-07 | Mensaje humano APP_READ_BASELINE; A1/B1/C1 | Turno 02/10/2026 | Usuario del chat / AT | Arnés aislado contra fuentes reales | Histórica ejecutada por Codex | B1/C1 | Permiso acotado; revisión individual no acreditada |
| DEC-08 | U4/SESSION_RECORD | 03/10 | Marco / AT | AGENTS/docs iniciales | Ejecutada documentalmente | DOC-001 | Sin inicio de refactorización |
| DEC-09 | D2 | Documento 02/10 | Equipo según D2 / DH | Problemas concretos/alternativas/impacto | Enfoque registrado | DG/alternativas | No patrón o arquitectura elegidos |
| DEC-10 | D1 §3/U2 | Acto grupal sin fecha exacta | Grupo comunicado / DH | Exclusiones de detalle/abandono/global/league/perfil ajeno | Confirmada | AC/REFACTOR_SCOPE | No excluye resultados inmediatos, perfil propio o ranking estacional |
| DEC-11 | U5/APP_READ_COORDINATION | 03/10 | Marco / AT | Publicar documentación | Histórica completada | PR #1/1951443 | Sólo 13 Markdown, sin fuentes privadas |
| DEC-12 | U6 y recomendación precedente | 03/10 | Marco / AT | Revisar/fusionar ese PR | Histórica completada | 7d879c2/STATUS | «Sigue» tiene contexto concreto; no permiso funcional general |
| DEC-13 | Extracto DOC-003 | 03/10 | Marco / AT | Matriz académica local | Ejecutada documentalmente | IA-E05 | Sin nota/cumplimiento inferidos |
| DEC-14 | Lista Marco/README/coordinación | 04/10 | Marco / DH organización | Secuencia: alcance como siguiente entonces | Registrada | README | No autoriza abrir todos los chats o ejecutar todas las fases |
| DEC-15 | U7/extractos DOC-004 | 04/10 | Marco / AT | Recuperar HU/AC locales | Ejecutada documentalmente | IA-E06 | Q pendientes sin elección por silencio |
| DEC-16 | U8/H1 | 04/10 | Marco / DH+HO | Excel recibido/amarillo excluido | Confirmada/Q-F resuelta | H1/AC 0.2 | 20 amarillas fuera; tres Merit no amarillas también fuera por U3 |
| DEC-17 | U9/DOC-005 | 04/10 | Encargo de Marco / AT | Mapa/contratos existentes | Ejecutada documentalmente | IA-E07 | Sin verificación dinámica o diseño final |
| DEC-18 | U10/extractos DOC-006 | 04/10 | Marco / AT | Diagnóstico guiado | Ejecutada documentalmente | IA-E08 | Hipótesis y respuesta estudiantil pendientes |
| DEC-19 | U11/DOC-007 | 04/10 | Marco / AT | Comparar alternativas | Ejecutada documentalmente | IA-E09 | Sin adopción/ratificación del diagnóstico |
| DEC-20 | U12/DOC-008 | 04/10 | Encargo de Marco / AT | Borradores/modelo actual | Ejecutada documentalmente | IA-E10 | Sin BD/arquitectura/UML final estudiantil |
| DEC-21 | U13/puente/DOC-009 | 04/10 | Transición comunicada / AT | Baseline/reproducción documental | Ejecutada documentalmente | IA-E11 | Sin builds/tests nuevos, R8/versiones supuestos |
| DEC-22 | U14/puente/DOC-010 | 04/10 | Transición comunicada / AT | Candidatos/cálculos/casos/fichas | Ejecutada documentalmente | IA-E12 | Sin pruebas, elección o reparto |
| DEC-23 | U15/corrección/U16, DOC-010 y APP_READ_CHAT10 | Recibidos 04/10 | Marco / DH+AE | Seis nombres/roles y disponibilidad | Roster confirmado; reparto/evidencia ausentes | Matriz §5 | No inferir fecha de elección ni autoría/revisión/ejecución |
| DEC-24 | U17, transición origen y encargo recibido | 04/10 | Marco autoriza transición / AT | Inventario IA/decisiones, fichas y mantenimiento local | Preparación documental producida | IA-E13/DOC-011 | No ratifica salidas ni inicia implementación/chat 12 |
| EV-01 | C1/GIT_READ DOC-011 | Lectura 04/10 | Git local / HO | HEAD/origin/status/diff de C1 | Fijo/limpio comprobado | GIT_READ | Sin master remoto actual comprobado |
| EV-02 | B1/G1/6 XML/DOC-009/010 | Ejecución 02/10; lectura 04/10 | Código/ejecución histórica / RH | 22 PASS, 0 FAIL/ERROR/SKIPPED | Histórico | TESTING_BASELINE | 18 retirados + 4 filtro conservado; no paquetes individuales |
| PI-01 | DG/alternativas/UML/CC/casos DOC-006–010 | Artefactos 04/10 | Codex / PI | Hipótesis, opciones, modelos y pruebas sugeridos | Revisión/elección humanas pendientes | IA-E08–12 | Continuación de chats no es aprobación |
| PI-02 | D2 §5 | Documento 02/10 | Propuesta de equipo / propuesta | Flujo A/B/C | Sin ratificación disponible | D2 | No confirmación universal obligatoria |
| PI-03 | Respuestas IA de coordinación | Turnos 04/10 | Codex / PI | Insertar implementación entre chats 9/10 | Propuesta; no tarea ejecutada | APP_READ_COORDINATION | Preguntas del usuario sobre acceso/fases no la autorizan ni acreditan app refactorizada |
| CO-01 | AC/Q-R/Q-T/Q-D/Q-C; DOC-007; DOC-010 | Registros 04/10 | Consultas / CO | Funcionalidad, aprendizaje y elegibilidad caja negra | Pendientes; consulta docente preparada sin envío | AC/guías | No completar con fixtures, fórmulas C1 o silencio |
| AE-01 | U16 y paquete disponible | Recibido 04/10 | Marco/disponibilidad / AE | Aportes/revisión/ejecución individuales | No disponibles para acreditar | Matriz §5 | No significa que nadie haya trabajado |
| CO-02 | Consulta DOC-011 de localización de conversación ChatGPT | Presentada 04/10 | Codex pregunta / CO | Exportación/ubicación realmente faltante | Sin respuesta incorporada al cierre inicial | HUMAN_INPUT DOC-011 | No reabre roster/H1/reparto ni detiene trabajo independiente |

No se cerró Q-R/Q-T/Q-D/Q-C. H1/Q-F resuelta. C1 describe el antes; sus filtros/fórmula/rutas no son ratificación de reglas objetivo. La incorporación de temporadas y las retiradas aprobadas están separadas de refactorizar conservando comportamiento. No hay borrado de datos autorizado.

## 5. Matriz individual respaldada y fichas vacías

Fuente actual: [U15/U16/corrección PO](evidence/DOC-010/HUMAN_INPUT_2026-10-04.md), DEC-23 y selección APP_READ_CHAT10. No se asignan DOC-001–011 ni casos históricos retrospectivamente. Las fichas contienen sólo identidad/rol conocidos y campos vacíos para completar con evidencia real.

| Integrante / ficha propuesta | Rol confirmado | Evidencia humana disponible en este inventario | Uso/modelo/prompts individuales | Aporte técnico, crítica, corrección/adaptación y adopción | Ejecución/resultados, declaración e influencia |
|---|---|---|---|---|---|
| [Adrianzén Vía, Renzo Ubaldo](evidence/DOC-011/individual/RENZO.md) | Product Owner | U15/corrección; D2 coordinación Marco/Renzo, sin actos técnicos individuales acreditados | Pendiente | Pendiente; rol no acredita autoría o revisión | Pendiente |
| [Bocanegra Carrión, Marco Antonio](evidence/DOC-011/individual/MARCO.md) | Developer | Solicitudes/autorizaciones/comunicación de alcance, H1, roster y U16; no paquete técnico individual | Contexto de solicitudes disponible; declaración/uso académico individual por completar | No consta análisis crítico técnico/adaptación/autoría individual suficiente | Pendiente |
| [Camarena Paredes, Victor Manuel](evidence/DOC-011/individual/VICTOR.md) | Developer | Roster/rol U15 | Pendiente | Pendiente | Pendiente |
| [Pacheco Castro, Pedro Iván](evidence/DOC-011/individual/PEDRO.md) | Developer | Roster/rol U15 | Pendiente | Pendiente | Pendiente |
| [Rado Delgado, Jean Carlo de Jesús](evidence/DOC-011/individual/JEAN_CARLO.md) | Developer | Roster/rol U15 | Pendiente | Pendiente | Pendiente |
| [Villar Córdova Alva, Matías Gonzalo](evidence/DOC-011/individual/MATIAS.md) | Scrum Master | Roster/rol U15 | Pendiente | Pendiente | Pendiente |

Cada ficha propone declaración, registro por interacción/contenido, análisis crítico e intervención verificable, resultados y vínculo a informe/anexos. Se duplica el registro por cada contenido realmente usado. Ninguna ficha está escrita en primera persona ni contiene una declaración simulada, porcentaje, aprobación, actividad, fecha de revisión, evaluación por pares o selección de pruebas inventada.

## 6. Cadena para entrega: eslabones disponibles y ausentes

Cadena propuesta: `fuente/HU/AC/CT/M/método cuando aplique → interacción IA → artefacto → autorización/decisión → contribución y revisión humanas → verificación/evidencia → capítulo/anexo final`. Una tarea documental puede tener HU no aplicable y sin cambio de código; no se crea una HU ficticia para justificarla.

| Recorrido | Fuente y relación disponible | Interacción → artefacto → autorización | Humano y verificación | Destino propuesto / falta |
|---|---|---|---|---|
| Organización/evidencia | P1/P2/D1/D2/U; HU no aplicable | IA-E03–06/13 → documentos → DEC-08/11–16/24 | Encargos y comprobaciones documentales; crítica individual pendiente | DeclaracionIA/Anexos y P4 §§2.4.5/12.2/13.2; integración final no acreditada |
| Calidad Kotlin/TS | HU 2.1/2.2/2.3/3.14–16 → AC-01/03 → CT-03/05 → V04/V06/M16; CP-01/02 según DOC-010 | IA-E07/10/12 → CT/UML/casos UQ → DEC-17/20/22 | CC 8/10 propuesta; sólo cuatro XML Kotlin históricos. Sin elección/revisión/ejecución individual | P4 cap. 10 + anexo individual y declaración; caja negra aislada insuficiente |
| Rating conservado | HU 3.8–12 → AC-03 → CT-07/11 → V07/M24/M25 → CP-03 calculateDynamicRatingChange | IA-E07/10/12 → contratos/mensajes/casos UR → encargos | CC 5 propuesta; sin pruebas TS/autoría/revisión individual. Fórmula objetivo pendiente | P4 caps. 8/10 y anexo; no confundir delta calculado con aplicado |
| Construcción/envío | HU 2.2/3.14 → AC-03/07 → CT-01–04 → V04/M01 → CP-04 SubmissionFactory.create | IA-E07/10/12 → CT/UML/casos UF → encargos | CC 2, sin elegibilidad blanca; siete parámetros no equivalen a siete campos funcionales. Integración manual no acreditada | P4 §8.1.2.5/cap. 10; autoría/integración, caja negra y ejecución pendientes |
| Diseño guiado | C1/DG-13/09/07/01 y demás restricciones CT/AC | IA-E08/09/10 → hipótesis/alternativas/modelos → DEC-18–20 | No revisión/selección/rediseño estudiantiles; sin cambio de código | P4 caps. 6–8/12 y anexo de decisión; no arquitectura final |
| Retiradas/temporadas/compatibilidad | D1/U2/U3/U8/H1 → AC-01–07, HU 3.27/3.28 → S01–S25 | IA-E06–12 → matrices/propuestas → alcance confirmado y encargos | Reglas Q pendientes, escenarios no ejecutados; no implementación | Informe describe alcance aprobado y pendientes; no logros de producto objetivo |
| Baseline histórico | PRE/C1/B1/XML; 18 casos retirados y cuatro filtro Kotlin | IA-E01/11 → arnés histórico/inventario → DEC-07 | Ejecución de Codex/G1 histórica; no paquetes individuales | Cap. 10 como antes acotado/anexo histórico; no aceptación posterior a retiradas |

Los localizadores HU/CT/M/CP proceden de las matrices previas enlazadas; no se reatribuyen contribuciones ni se modifican sus snapshots. No todos los artefactos tienen todas las clases de relación.

## 7. Brechas y material recomendado para el chat 12

| Brecha concreta | Evidencia que falta | Material que ya puede llevarse |
|---|---|---|
| Refinamiento ChatGPT y exportaciones completas | Conversación original con secuencia/prompts/respuestas y procedencia; modelos si constan | A1 recuperado, adjuntos auténticos y selecciones APP con límites. Consulta de localizador pendiente; no reconstruir |
| Declaración/crítica por persona | Contenido realmente asistido, validación propia, errores/supuestos, alternativas, decisión humana y versiones corregidas/adaptadas; estimación fundada | Seis fichas vacías, matriz individual y escalas oficiales; sin porcentajes calculados |
| Autoría crítica/BD/integración | Evidencia auténtica de actos estudiantiles, revisión y explicación | C1/CT/UML como referencias del antes; no autoría manual acreditada |
| Pruebas individuales/elegibilidad | Asignaciones/selecciones reales, aclaración docente de campos contextuales/derivados, validación CC/casos, ejecución/resultados/defectos/cobertura | DOC-010 intacto, consulta preparada sin envío, B1/XML históricos separados |
| Producto reproducible/objetivo | Fuentes/versiones/configuración no secretas y R8 auténticos; ejecución Android/Functions/Firebase/integración; trabajo estudiantil real | DOC-009 y cinco capas de brechas. No app refactorizada ejecutable acreditada |
| Reglas/diagnóstico/diseño | Q-R/Q-T/Q-D/Q-C y revisión/descarte/adopción humana de DG/alternativas/UML | H1/Q-F resuelta, AC/CT/M y decisiones abiertas; no interpretar transiciones como selección |
| Historia y entrega | Cronología real ISW1/ISW2, correspondencia R1/R2/sprints, contribuciones, pares R1/R2, impacto/talleres y anexos reales | Historia pendiente corroborada por coordinación; requisitos/matriz académica, sin logros o fechas inventados |

**Traspaso recomendado:** este inventario y DOC-011, DECISIONS/STATUS, fichas que estudiantes completen auténticamente cuando haya evidencia, DOC-010/DOC-009, alcance/AC y requisitos. El chat 12 puede organizar índice y brechas del informe y aprendizaje previo; debe distinguir documentación disponible de producto/entrega pendiente. No redactar un informe final que simule implementación, aceptación o cumplimiento individual. No se inicia ni se envía mensaje a ese chat desde esta tarea.

## 8. Comprobación y límites del resultado

[VERIFICATION](evidence/DOC-011/VERIFICATION.json) conserva los resultados reales de enlaces/anclas/tablas, IDs/fichas, hashes históricos, preservación de todos los archivos previos salvo README/STATUS/DECISIONS, nueve copias recuperadas, seis XML históricos y C1 en lectura. [BEFORE](evidence/DOC-011/BEFORE.json) fija el paquete anterior completo fuera de .git. Los auxiliares de esta etapa son documentación/integridad, no código o tests del producto.

Se verificó la procedencia y se contrastaron requisitos/textos disponibles; este razonamiento del agente **no sustituye revisión crítica humana**. Sin builds/tests de producto o arnés, escenarios nuevos, modificación de originales/baseline/DOC-001–010/código/configuración/datos/servicios/GitHub, instalación de dependencias del producto, reconstrucción R8/versiones, arquitectura final, implementación de retiradas/temporadas o mensajes a otros chats. S01–S25 siguen sin ejecutar; ningún AC se cerró íntegramente.
