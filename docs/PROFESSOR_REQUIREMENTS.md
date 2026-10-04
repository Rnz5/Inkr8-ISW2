# Requisitos del profesor

Fecha: 3 de octubre de 2026. Síntesis de consulta; las fuentes oficiales prevalecen sobre este documento. Identificadores, procedencia y copias: [SOURCE_INDEX.md](SOURCE_INDEX.md).

Esta documentación inicial fue autorizada por Marco en U4. No acredita cumplimiento del curso ni autoriza implementar arquitectura, lógica crítica o integración mediante IA. El protocolo de trabajo local es una propuesta operativa; no se presenta como política ratificada por el grupo.

## Fuentes oficiales

- [P1 — Guía de uso de IA](SOURCE_INDEX.md#fuentes-principales): permisos, restricciones, declaración y análisis crítico.
- [P2 — Enunciado](SOURCE_INDEX.md#fuentes-principales): propósito y entregables.
- [P3 — Rúbrica](SOURCE_INDEX.md#fuentes-principales): criterios y participación individual, hoja `Rubrica`.
- [P4 — Plantilla del informe](SOURCE_INDEX.md#fuentes-principales): contenido esperado. Sus ejemplos son referenciales, no evidencia de Inkr8.
- [P5 — Evaluación por pares](SOURCE_INDEX.md#fuentes-principales): formato individual obligatorio.
- [P6 — Sílabo](SOURCE_INDEX.md#fuentes-principales): logros, contenidos y evaluación.

Las páginas citadas son las páginas físicas del PDF. P1 tiene numeración de apartados irregular al final; allí se indica también el título para localizar la regla.

## Límites de IA que condicionan el trabajo

| Actividad | Regla oficial | Fuente |
|---|---|---|
| POO y SOLID | Apoyo conceptual permitido; generación de soluciones completas restringida. | P1, p. 4, §2.1 |
| Patrones | Análisis comparativo permitido; selección automática sin justificación restringida. El estudiante identifica el problema y justifica su elección. | P1, pp. 4–5, §2.2 |
| Arquitectura | Se pueden explorar alternativas; su evaluación y justificación son obligatorias. La IA no sustituye el juicio arquitectónico del estudiante. | P1, p. 5, §2.3 |
| UML | Borradores permitidos; rediseño y explicación obligatorios por el estudiante. | P1, pp. 5–6, §2.4 |
| Pruebas | Se pueden generar casos; el estudiante debe validarlos y ejecutarlos. La generación no garantiza cobertura. | P1, p. 6, §2.5 |
| Refactorización | Sugerencias permitidas; análisis de impacto obligatorio. El cambio estructural debe conservar comportamiento. | P1, pp. 6–7, §2.6 |
| Proyecto final: apoyo permitido | Maquetación/estilos básicos, componentes aislados, configuración inicial, comentarios, pruebas sobre lógica ya escrita y sugerencias de optimización de consultas existentes. Esto no concede permiso general para desarrollar el núcleo. | P1, p. 7, §2.7 |
| Proyecto final: autoría humana | El alumno define la BD y relaciones entre componentes; la IA no dicta clases ni entidad-relación. El estudiante escribe algoritmos críticos y conecta manualmente frontend y backend. | P1, p. 7, §2.7 |
| Trabajo supervisado | Declarar uso, conservar prompts y respuestas, analizar errores/limitaciones y evidenciar corrección, mejora o adaptación humana. No presentar resultados copiados ni soluciones no comprendidas. | P1, pp. 10–11, §3.2 |
| Defensa y evaluación de dominio | Sin asistencia IA durante defensas, sustentaciones, exámenes y evaluaciones prácticas en vivo. Los estudiantes deben explicar, justificar, modificar y resolver variaciones del sistema. | P1, pp. 8, 12–13, §3.3; p. 18, §7.3; p. 20, “Restricciones y prohibiciones” |

La retirada de torneos, ligas, reputación y Merit y la incorporación de temporadas son cambios funcionales aprobados en D1/U2/U3, separados de la refactorización del comportamiento conservado. La aprobación del alcance no elimina los límites de autoría de P1. Véase [REFACTOR_SCOPE.md](REFACTOR_SCOPE.md).

## Matriz de cumplimiento y evidencia

Los estados reflejan lo revisado en la auditoría del 2–3 de octubre. “Existente” significa fuente localizada o ejecución observada; no implica que el entregable final esté aceptado.

| Requisito verificable | Fuente exacta | Evidencia existente | Evidencia o trabajo faltante |
|---|---|---|---|
| Evolucionar el producto ISW1 y mantener trazabilidad con requisitos, HU, modelos y decisiones. | P2, p. 1, §1.1 | I1 contiene historia del producto y HU; M1 aporta UML histórico; C1 identifica código. | Backlog completo con aceptación y correspondencia vigente HU → diseño → código → pruebas. |
| Evidenciar Scrum y evolución incremental de R1/R2: backlog, sprints, objetivos, incrementos y ceremonias. | P2, pp. 1–2, §§1.1–1.2; P3, `Rubrica!D8,D12`; P4, caps. 2–5 | I1 contiene planificación histórica; D2 identifica pendientes. | Aclarar la relación R1/R2 con ISW2 y reunir registros reales de ejecución, actas, métricas y decisiones. No convertir planificación en ejecución. |
| Aplicar y justificar principios/patrones en problemas concretos; documentar componentes, responsabilidades, relaciones y calidad arquitectónica. | P2, p. 1, §1.1; P3, `Rubrica!D14`; P4, caps. 6–7 | M1 e I1 son antecedentes; mapa técnico describe C1. | Diagnóstico y decisiones humanas para el alcance objetivo; modelos coherentes con implementación final. |
| Vincular HU y aceptación con clases, secuencias, componentes, código, integración y pruebas. | P3, `Rubrica!B5:G6,D16`; P4, §§5.6 y 8.1.2.5 | HU y diagramas históricos localizados; C1 disponible como referencia. | Matriz verificable del producto final y demostración funcional. I2 aún conserva contenido de plantilla. |
| Por integrante: caja blanca sobre módulo/método con complejidad ciclomática **>4**; caja negra sobre funcionalidad con **>4 campos de entrada**; prueba unitaria de método con **≥4 casos**. | P3, `Rubrica!D18` | B1: 22 pruebas JVM reproducidas; A1 registra el trabajo histórico de IA. | Asignaciones humanas y evidencia individual de selección, diseño, ejecución, resultados, defectos y explicación. Los 22 tests no acreditan estos umbrales por integrante. |
| Todos los integrantes completan la evidencia de pruebas de forma sustentada y equitativa. Si falta uno, el criterio obtiene 0 puntos. | P3, `Rubrica!D18` | No se verificó un paquete individual completo. | Evidencia de cada integrante; no atribuir automáticamente tests históricos a personas. |
| Entregar informe PDF completo conforme a plantilla, considerando R1/R2. | P2, p. 2, §1.2.1 | P4 es plantilla; I2 es borrador ISW2 incompleto. | Informe final basado en trabajo real, con todos los capítulos/anexos pertinentes. |
| Entregar código completo y actualizado en ZIP, pruebas, configuración/dependencias y README para instalar, ejecutar y probar. | P2, p. 2, §1.2.2 | C1 y arnés B1 localizados; arnés JVM ejecutado. | Recuperar archivos de build Android/Functions, R8 y configuración Firebase; documentar ejecución del producto. Véase [TESTING_BASELINE.md](TESTING_BASELINE.md). |
| Proporcionar versión funcional, acceso e instrucciones/credenciales de prueba cuando corresponda; excluir secretos reales del repositorio. | P2, p. 2, §1.2.3 | No se validó despliegue ni ejecución completa. | Demostración y acceso reproducibles del producto final; datos de prueba y configuración sin secretos reales. |
| Entregar UML en informe y archivos editables elaborados con herramienta CASE/especializada; sólo imágenes no bastan. | P2, p. 3, §1.2.4 | M1: diagramas UML editables históricos; BD localizada sólo como PNG. | Modelos finales coherentes; archivo editable de BD si corresponde. |
| Identificar anexos y relacionarlos con lo documentado; evidencias del trabajo real. Mantener repositorio disponible y evolución/contribuciones verificables. | P2, p. 3, §§1.2.5–1.2.6 | C1 y A1 contienen trazabilidad histórica; fuentes inventariadas. | Paquete final indexado y evidencias reales de nuevas intervenciones. |
| Entregar modelo de BD, creación/actualización y configuración necesarios para reproducir la estructura, cuando corresponda. | P2, p. 3, §1.2.7 | Modelo histórico en M1; índices Firestore en C1. | Configuración/reglas completas y correspondencia humana del modelo con la estructura objetivo. Los índices solos no reproducen Firebase. |
| Cada integrante completa evaluación por pares en formato oficial; faltar uno anula el criterio de visión/metodología/equipo, valorado en 3 puntos. | P2, pp. 3–4, §1.2.8; P3, `Rubrica!D8`; P5 | Formato P5 localizado. | Formularios individuales cumplimentados y verificables; no rellenarlos con aportes inventados. |
| Cada integrante declara IA: herramienta, objetivo, prompts, respuestas, influencia estimada, análisis crítico y aporte humano. | P2, p. 4, §1.2.9; P1, pp. 14–17, §§5–6 | A1 aporta registro histórico; esta documentación identifica apoyo de Codex. | Declaraciones individuales completas y revisión crítica de esta etapa y de etapas posteriores. |
| Documentar impacto ético/profesional, social, económico, ambiental/global y mitigaciones; informe individual de cada integrante. Si falta uno, el criterio obtiene 0 puntos. | P3, `Rubrica!D20`; P4, cap. 11 | Estructura exigida localizada, sin evidencia individual completa verificada. | Análisis del producto real e informes individuales sustentados. |
| Cada integrante demuestra una o más HU, aceptación, clases, secuencia, código y las tres clases de pruebas; dominio autónomo. | P3, `Rubrica!C28:G41`; P1, pp. 8, 12–13, 18 | Sin demostración de dominio evaluada en esta etapa. | Preparación previa por estudiantes y sustentación real sin IA. |

## Comprobaciones antes de presentar cumplimiento

1. Cada afirmación enlaza una fuente o resultado conservado y tiene fecha/versión.
2. Una prueba propuesta se diferencia de una prueba ejecutada; los resultados incluyen entorno, alcance y fallos.
3. Una decisión técnica identifica autor humano y justificación real; las alternativas IA se marcan como propuestas.
4. Cada integrante aporta su evidencia; ni la suite ni el documento grupal sustituyen obligaciones individuales.
5. Una duda del enunciado se registra como pendiente de aclaración docente. No se encontró autorización oficial para entregar sólo un informe de refactorización en lugar del producto completo.

La rúbrica del informe suma 20 puntos. El sílabo establece para el proyecto 40% grupal y 60% individual (P6, p. 4, “Evaluación”). No se calculan notas ni porcentajes de cumplimiento a partir de documentos incompletos.

Documento elaborado con apoyo de Codex a partir de las fuentes citadas. La revisión crítica humana y la aceptación académica siguen pendientes; el documento no las sustituye.
