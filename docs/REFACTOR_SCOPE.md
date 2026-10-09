# Alcance vigente de Inkr8

**Vigente FIN-002 — 09/10/2026:** cierre y entrega revisable documentados en
[FINAL_DELIVERY.md](FINAL_DELIVERY.md), con 8 Android PASS, reglas nuevas
propuestas y límites externos/académicos explícitos. Los estados inferiores son
antecedentes preservados; no revocan DEC-AI-AUTH-001, las decisiones respondidas
ni el permiso actual de publicar rama/PR, sin fusión o despliegue.

**Actualización vigente RET-006/SEA-001 — 07/10/2026:** las retiradas aprobadas
se implementaron localmente en Android/Functions; datos y campos legacy intactos.
Ranked permite elección Standard/On-Topic y conserva reputación histórica sólo en
precio por DEC-RANKED-SELECT-001. Merit/rating/economía conservados. Temporadas
HU 3.27/3.28 implementadas con reglas explícitamente aceptadas por Marco en
DEC-SEASON-001. Las filas históricas inferiores con reglas pendientes describen
el estado anterior, no bloquean estas decisiones ya recibidas. Son cambios
funcionales separados del cierre conservador A01–A07. Validación externa,
seguridad/reglas originales, scheduler/escala y academia siguen independientes.
[Alcance comprobado y límites](FUNCTIONAL_CLOSURE.md).

**Versión:** 0.3 · registro 05/10/2026; fecha de aprobación grupal posterior no aportada. **Fuentes:** [D1, U2, U3, U4 y U7](SOURCE_INDEX.md). D1 fue aprobado por el grupo según Marco. U2/U3 aclaran sus contradicciones. U4 autorizó documentación inicial; U7/DEC-15 habilita la matriz de aceptación y mantenimiento local, sin iniciar refactorización ni decidir reglas pendientes.

**Alcance posterior SCP-001 — registro 05/10/2026:** Marco comunica una nueva captura/mensaje de Renzo: «Ranked y Merit permanecen»; «Torneos, ligas y reputación se retiran». La imagen original y su fecha no están adjuntas al contexto disponible; no se infiere aprobación grupal ni fecha de subida. Esta instrucción humana posterior sustituye la retirada total de Merit de U3/DEC-03 para el trabajo vigente. Se conservan Ranked/rating/Merit y las reglas económicas existentes; no se inventan usos, precios o recompensas nuevos. Temporadas permanece incluida con reglas pendientes. RET-001–005 y sus evidencias describen el alcance anterior y no se reescriben. [Recuperación y restitución](RECOVERED_ENVIRONMENT.md).

## Tres categorías obligatorias

- **Refactorización:** cambio interno que preserva el comportamiento acordado.
- **Cambio funcional aprobado:** retirada o incorporación deliberada. Debe tener aceptación propia.
- **Fuera de intervención:** aspectos que no corresponden a la tarea vigente.

No presentar la eliminación de una función como preservación de su comportamiento. No confundir la aprobación de su retirada del producto objetivo con permiso para borrar datos o alterar servicios desplegados.

## Matriz del producto objetivo

| Área | Decisión vigente | Tipo de trabajo | Qué todavía debe concretarse |
|---|---|---|---|
| Escritura en inglés y evaluación | Conservar núcleo y analizar su estructura | Refactorización del recorrido conservado; conservar Merit y separar efectos de funciones retiradas | Contratos, validaciones y resultados esperados; fuente auténtica de R8 |
| Practice | Conservar Standard/On-Topic y evaluación | Refactorización conservando los efectos Merit existentes | Criterios originales de evaluación/resultados/Merit, sin nuevas ganancias |
| Ranked y rating | Conservar ambos según D1 | Refactorización, más retirada de dependencias funcionales | Selección de ejercicio sin ligas, condiciones no económicas de entrada/cierre y presentación de placement |
| Autenticación y perfil básico | Conservar dentro de D1 | Refactorización; quitar presentación/acciones retiradas | Límites del perfil básico y compatibilidad de datos |
| Persistencia y estados | Conservar el soporte del núcleo | Refactorización de contratos autorizados | Modelo real, identidad de envío, estados de evaluación y emparejamiento |
| Torneos | Retirar del producto objetivo | Cambio funcional aprobado U2/U3 | Dependencias compartidas, navegación, jobs, datos históricos |
| Ligas | Retirar; no conservar la alternativa numérica contradictoria de D1 | Cambio funcional aprobado U2/U3 | Selección del ejercicio Ranked y mensajes de rating sin ligas |
| Reputación | Retirar, incluida su consulta y efectos | Cambio funcional aprobado U2/U3 | Separación de precio/penalización y estado de sesión conservado |
| Merit | Conservar por instrucción humana posterior SCP-001 | Restitución de presentación retirada por el alcance anterior; refactorización conservadora | Usos ligados a reputación/Pantheon/ligas y compatibilidad histórica; no nuevos costes/recompensas |
| Temporadas | Incorporar; HU 3.27/3.28 ya estaban en ISW1 y no se localizaron implementadas | Incorporación funcional aprobada U2/U3 | Períodos, participación, ranking, cierre, historial y pendientes de evaluación |
| Ranking global, posición global y leaderboard de liga | Excluir sus visualizaciones según D1 | Retirada funcional delimitada | Distinguirlos del ranking por temporada; revisar dependencias heredadas de Pantheon sin aprobar su conservación |
| Detalle de una partida para revisar errores | Eliminar esa HU del alcance según D1 | Retirada funcional delimitada | No confundir esa consulta posterior con Results Screen del flujo inmediato, conservada por D1 |
| Perfil de otro jugador desde leaderboard | Eliminar esa consulta según D1 | Retirada funcional delimitada | No confundirla con perfil básico propio conservado |
| Penalización por abandonar una partida | Eliminar según D1 | Retirada funcional delimitada | Cierre/limpieza de sesión puede requerir criterio; la penalización no se reabre |
| Rediseño visual/responsividad | Excluido como objetivo de intervención por D1 | Fuera de intervención | Ajustes mínimos que necesite una retirada deben justificarse como parte de ésta |
| Reescritura completa, funciones ajenas y patrones sin problema demostrado | Excluidos | Fuera de intervención | No habilitarlos desde este documento |
| Baseline histórico | Conservar como referencia del antes | Custodia de evidencia | No reinterpretar sus tests de funciones retiradas como requisitos futuros |

La instrucción posterior SCP-001 conserva Merit y sustituye la retirada total anterior. No se ha definido ninguna ganancia, coste o sustituto nuevo. El permiso sobre datos existentes y despliegues sigue siendo distinto del alcance funcional.

## Dependencias que deben separarse

Estas son observaciones de C1/G2; no una lista aprobada de clases a modificar.

| Frontera | Evidencia de código auditado | Consecuencia para aceptación |
|---|---|---|
| Ranked dentro de Competitions | [selección por liga](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Competitions.kt#L85) y [entrada](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Competitions.kt#L216) | La pantalla no se puede clasificar entera como torneos. Falta la regla de ejercicio sin ligas. |
| Entrada y abandono mezclados con economía | [applyMeritAction](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/users/applyMeritAction.ts#L164) | Merit permanece; retirar funciones ligadas no elimina automáticamente autenticación, límites diarios, rachas o sesión. Los usos vinculados a reputación/Pantheon necesitan precisión. |
| Evaluación con economía, placement y reputación | [submissionEvaluationEngine](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L303) | Preservar score/evaluación/rating y retirar los efectos aprobados con pruebas distintas. |
| Rating con contadores de ligas | [emparejamiento](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L529) y [ghostMatchProcessor](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/ghostMatchProcessor.ts#L75) | Retirar ligas no implica retirar rating ni el mecanismo que resuelve Ranked pendiente. |
| Limpieza y penalización juntas | [rankedSessionCleaner](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/users/rankedSessionCleaner.ts#L21) | Separar limpieza de sesión del efecto de reputación. |
| Cabecera/perfil/reveal con League | [UserHeaderCard](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/utils/UserHeaderCard.kt#L31) | Afecta pantallas conservadas; definir presentación sin decidir un rediseño general. |
| Modelo compartido | [Users](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/Users.kt#L3) | Compatibilidad de lecturas y retirada lógica son distintas de eliminación física de campos/datos. |

El [mapa técnico completo](reference/TECHNICAL_MAP_2026-10-03.md) conserva las fronteras F01–F12 y contratos C01–C06 del análisis. Algunas rutas de consulta allí son temporales; los enlaces fijados aquí apuntan al commit auditado.

## Criterios todavía pendientes

La [matriz local 0.2 AC-01–AC-07](ACCEPTANCE_MATRIX.md) desarrolla siete campos por criterio y 25 escenarios no ejecutados. Marco aportó H1 y excluyó las 20 HU amarillas (DEC-16); Q-F quedó resuelta. La exclusión adicional de HU Merit por U3 queda superada por SCP-001; volver a usar sus criterios H1 originales, sin redefinirlos. Se recuperó aceptación original por hoja/celda; “PENDIENTE” significa que el AC completo aún tiene precisiones abiertas, no que todo criterio falte. Q-R/Q-T/Q-D y corrección de fuente Q-C reúnen sólo esos vacíos.

| ID | Criterio que falta | Evidencia necesaria | Estado |
|---|---|---|---|
| AC-01 | Selección de Standard/On-Topic en Ranked sin ligas | Regla del equipo, ejemplos y criterios previos que correspondan | PENDIENTE |
| AC-02 | Entrada/cierre de Ranked con Merit conservado; separar efectos de reputación y abandono excluidos | Confirmar límites diarios, rachas, estado/limpieza de sesión abandonada y errores, sin restablecer penalizaciones excluidas | PENDIENTE |
| AC-03 | Resultados inmediatos, placement y perfil básico sin funciones retiradas | Criterios existentes; excluir detalle de partida para revisar errores y consulta de perfil desde leaderboard. Historial de temporadas se concreta en AC-06 | PENDIENTE |
| AC-04 | Temporadas: períodos, zona horaria, participantes y envíos admitidos | Criterios originales de HU 3.27/3.28 o decisión humana identificada | PENDIENTE |
| AC-05 | Temporadas: definición/cálculo del rating de temporada, dirección/elegibilidad/desempates y relación con rating/placement | H1 HU 3.27 fija la magnitud rating de temporada y posición resaltada; faltan las precisiones de Q-T | PENDIENTE, parcial |
| AC-06 | Temporadas: cierre, historial y envíos/evaluaciones tardíos | Regla humana verificable | PENDIENTE |
| AC-07 | Compatibilidad con documentos y borradores previos | Inventario y tratamiento decidido por el equipo | PENDIENTE |

No asignar responsables ficticios ni completar estas reglas por inferencia. No se eligen colecciones, resets, fórmulas, jobs, índices, patrones ni arquitectura futura.

H1 HU 3.28 confirma posición final y rating de cierre históricos; su referencia a recompensas no define nuevas ganancias de Merit y requiere precisión humana. HU 3.13 confirma selección explícita On-Topic, distinta de la selección aleatoria ligada a ligas de C1. Estas diferencias y mensajes/escenarios originales se registran en la matriz; no son cambios de código realizados.

## Etapa autorizada actual

Crear/revisar documentación derivada, preservar evidencia, consultar fuentes/código y preparar aceptación. U5/U6 autorizaron publicación e integración del PR documental #1, ya completadas; consultar STATUS. U7/DEC-15 sólo autoriza análisis/matriz y actualizaciones locales de esta tarea. No autoriza nueva publicación/fusión, modificar producción/configuración, ejecutar retiradas/temporadas, editar originales/baseline, operar datos/Firebase ni elegir arquitectura/patrones/modelos de BD. Consultar [STATUS](STATUS.md) y [DECISIONS](DECISIONS.md) ante nueva evidencia.



**Etapa posterior vigente:** el usuario autoriza cambios locales conservadores, recuperación auténtica, compilación y pruebas; no publicación, despliegue, datos reales, reglas inventadas ni atribución de autoría automática al estudiante. Las restricciones concretas P1 siguen vigentes.

Cierre de acceso Auth aprobado por respuesta posterior de Marco: ACCOUNT-ACCESS-001,
perfil/historial/economía retenidos,8 Android y19 cuenta PASS; ver FINAL_DELIVERY.
