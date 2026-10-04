# Protocolo de evidencia y trazabilidad

Fecha: 3 de octubre de 2026. **Propuesta operativa inicial para la documentación local autorizada en U4**; no consta ratificación grupal de este protocolo ni de la propuesta de aprobaciones de D2. Las obligaciones académicas proceden de P1/P2/P3, no de esta propuesta.

Fuentes y procedencia: [SOURCE_INDEX.md](SOURCE_INDEX.md). Reglas: [PROFESSOR_REQUIREMENTS.md](PROFESSOR_REQUIREMENTS.md). Alcance vigente: [REFACTOR_SCOPE.md](REFACTOR_SCOPE.md). Decisiones: [DECISIONS.md](DECISIONS.md). Estado: [STATUS.md](STATUS.md).

## Finalidad y reglas oficiales

P1 exige transparencia, responsabilidad individual y análisis crítico: declarar herramienta/objetivo, guardar prompts/respuestas, diferenciar contribuciones, justificar adopción o descarte y evidenciar intervención humana (pp. 1–3, §§1.1–1.4; pp. 10–11, §3.2; pp. 14–18, §§5–7). P2 exige evidencia del trabajo real, anexos identificados y declaración por integrante (pp. 3–4, §§1.2.5 y 1.2.9).

La guía prohíbe fabricar prompts/respuestas y alterar evidencias (P1, p. 21, “Conductas indebidas”, apartado de manipulación). Si falta una conversación original, registrar **“no disponible”** y conservar lo existente; un resumen posterior puede orientar, pero no equivale al registro original.

La IA puede apoyar documentación, sugerencias y pruebas dentro de P1. Las decisiones de arquitectura/BD, la lógica crítica y la integración deben mantener la autoría humana exigida por §2.7 (p. 7). La asistencia termina antes de una sustentación o evaluación real de dominio (P1, pp. 12–13 y 20).

## Cadena mínima de trazabilidad

`Fuente/HU + aceptación → decisión humana → intervención → verificación → evidencia → informe/anexo`

Cada eslabón debe tener un enlace verificable o indicar que está pendiente. Si la intervención sólo documenta o analiza, registrar **“sin cambio de código”**. No inventar HU, criterios de aceptación, responsables, fechas, sprints, commits, logs ni resultados para completar la cadena.

| Elemento | Qué conservar | Cómo evitar una afirmación falsa |
|---|---|---|
| Fuente | ID de SOURCE_INDEX, versión/fecha y ubicación o copia. | Separar oficial, histórico, decisión humana, código y resultado observado. |
| HU/aceptación | ID y texto real o enlace al backlog; tratamiento dentro del alcance. | Si sólo está el título, indicar que falta aceptación detallada. |
| Decisión | Problema, alternativas pertinentes, elección/justificación humana, autor, fecha y evidencia. | Propuesta IA y decisión humana son estados distintos. La ratificación se registra sólo cuando existe. |
| Intervención | Propósito y archivos/artefactos realmente afectados; versión de referencia. | Separar refactorización, retirada funcional e incorporación funcional. No confundir propuesta con trabajo realizado. |
| Verificación | Caso, esperado, observado, entorno, versión, fecha, resultado y salidas. | No presentar revisión estática como ejecución ni tests JVM como funcionamiento de Android/Firebase. |
| IA | Herramienta y modelo si se conoce, objetivo, secuencia íntegra disponible de prompts/respuestas, artefactos producidos. | “Modelo no registrado” es preferible a inferirlo. Guardar el material original antes de resumirlo. |
| Revisión humana | Correcciones/adaptaciones, limitaciones detectadas, comparación, aceptación o descarte justificado. | El texto generado por IA no acredita por sí mismo una revisión del estudiante. |
| Entrega | Capítulo/anexo correspondiente y contribución individual verificada. | Distinguir entrega grupal de obligaciones por integrante. |

## Procedimiento propuesto para cada tarea

1. **Abrir una ficha real** cuando exista una tarea concreta. Vincular alcance y fuente; consignar pendientes que afecten al comportamiento. Consultar el registro de decisiones para no reabrir exclusiones aprobadas.
2. **Guardar la interacción original disponible** y las entradas que determinaron la salida. Mantener secuencia y contexto suficiente; una captura/resumen debe identificarse como tal. Los originales oficiales se conservan sin edición.
3. **Registrar el resultado producido**, diferenciándolo de sugerencias descartadas y acciones que no se ejecutaron. En esta etapa: documentación y publicación documental autorizada por U5 en rama/PR; sin cambios de producción ni despliegues.
4. **Añadir revisión crítica humana real**: corrección/completitud, supuestos, errores, alternativas, modificaciones y razón de adopción/descarte. Mientras falte, el artefacto se marca “revisión humana pendiente”.
5. **Verificar según la naturaleza del trabajo**. En documentos, revisar fuentes, enlaces y consistencia; en pruebas posteriores autorizadas, guardar salidas y entorno. Las comprobaciones necesarias no conceden permiso para implementar lógica restringida.
6. **Actualizar decisión y estado** sólo con evidencia. No imponer a todo el equipo un circuito de firmas que D2 todavía llama propuesta. La autorización explícita del usuario permite continuar el trabajo comprendido en ella.
7. **Relacionar la evidencia con el informe** y con el aporte individual cuando corresponda. Preparar la declaración IA con base en los registros, sin reconstrucciones ficticias.

## Organización de registros nuevos

Propuesta: `docs/evidence/<ID-tarea>/`, con identificador único que se asigne al abrir una tarea real. No se crean carpetas o fichas de tareas inexistentes para simular progreso.

| Archivo sugerido | Contenido |
|---|---|
| `record.md` | Ficha, relaciones con fuentes/HU, revisión crítica y estado. |
| `interaction.*` | Exportación/texto/capturas auténticas de prompts y respuestas, en su formato disponible. |
| `outputs/` | Artefactos generados o referencias a ellos; versión exacta. |
| `verification/` | Resultados originales, logs o capturas de verificaciones efectivamente realizadas. |

Los originales y copias privadas de las fuentes se consultan en Drive o en el paquete local del equipo; no se incluyen en esta publicación. SOURCE_INDEX identifica su procedencia. B1/A1 son históricos: conservarlos y enlazarlos, sin reescribir sus resultados. La suite de 22 PASS pertenece al arnés JVM y al commit indicado en [TESTING_BASELINE.md](TESTING_BASELINE.md); no es prueba del producto objetivo ni de cumplimiento individual de P3.

Las futuras pruebas del núcleo conservado, las retiradas aprobadas y las temporadas nuevas necesitan criterios distintos. Que un test histórico cubra una función retirada no obliga a preservarla en el producto ni autoriza alterar el baseline histórico en esta etapa.

## Plantilla vacía de ficha

**Plantilla sin completar. No representa una tarea ejecutada ni una declaración individual.** Copiarla al abrir una tarea real; rellenar sólo con datos comprobados y usar “pendiente”, “no disponible” o “no aplica” con motivo cuando corresponda.

```markdown
# Registro de tarea [ID por asignar]

- Fecha y zona horaria:
- Estado: propuesta / en curso / resultado producido / revisión humana pendiente / validado / descartado / bloqueado
- Objetivo concreto:
- Persona que realizó el aporte humano:
- Persona que revisó, si hubo revisión:
- Fuente y evidencia de autorización para esta tarea:
- Fuente/HU y criterios de aceptación existentes:
- Tipo: documentación / análisis / refactorización / retirada funcional / incorporación funcional / verificación
- Versión de referencia (commit o versión del artefacto, si aplica):
- Artefactos y archivos afectados realmente:

## Interacción con IA
- Herramienta y modelo registrado:
- Objetivo del uso:
- Enlace al prompt original y secuencia:
- Enlace a respuestas originales:
- Enlace a artefactos producidos:
- Datos originales no disponibles y límite de la evidencia:

## Análisis crítico humano
- Evaluación de corrección y completitud:
- Supuestos, limitaciones y errores identificados:
- Alternativas consideradas y consecuencias:
- Correcciones, adaptaciones o mejoras realizadas por el estudiante:
- Adopción/descarte y justificación humana:
- Evidencia de la decisión y fecha real:
- Influencia estimada de IA y fundamento de la estimación:

## Verificación efectiva
- Caso y criterio esperado:
- Entorno, versión, fecha y procedimiento realmente usado:
- Resultado observado y estado: no ejecutado / PASS / FAIL / bloqueado
- Evidencia original de salida:
- Defectos, acciones correctivas y límites:

## Trazabilidad de entrega
- Decisión enlazada:
- Capítulo/anexo:
- Contribución individual respaldada:
- Pendientes concretos:
```

P1 define influencia estimada **baja ≤30%, media 31–70%, alta >70%** (p. 14, §5.2). El estudiante debe estimarla y fundamentarla a partir del trabajo real; no asignar un porcentaje automático por cantidad de archivos, líneas o mensajes.

Documento elaborado con apoyo de Codex. Esta ficha y este protocolo no prueban revisión humana, autoría estudiantil de implementación ni cumplimiento académico; deben acompañarse de registros y análisis reales.
