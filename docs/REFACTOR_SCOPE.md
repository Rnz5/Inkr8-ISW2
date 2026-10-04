# Alcance vigente de Inkr8

**Versión:** 0.1 · 03/10/2026. **Fuentes:** [D1, U2, U3, U4 y U5](SOURCE_INDEX.md). D1 fue aprobado por el grupo según Marco. U2/U3 aclaran sus contradicciones. U4 autoriza esta documentación inicial y U5 su publicación documental en rama/PR, sin iniciar la refactorización.

## Tres categorías obligatorias

- **Refactorización:** cambio interno que preserva el comportamiento acordado.
- **Cambio funcional aprobado:** retirada o incorporación deliberada. Debe tener aceptación propia.
- **Fuera de intervención:** aspectos que no corresponden a la tarea vigente.

No presentar la eliminación de una función como preservación de su comportamiento. No confundir la aprobación de su retirada del producto objetivo con permiso para borrar datos o alterar servicios desplegados.

## Matriz del producto objetivo

| Área | Decisión vigente | Tipo de trabajo | Qué todavía debe concretarse |
|---|---|---|---|
| Escritura en inglés y evaluación | Conservar núcleo y analizar su estructura | Refactorización del recorrido conservado; separar economía retirada | Contratos, validaciones y resultados esperados; fuente auténtica de R8 |
| Practice | Conservar Standard/On-Topic y evaluación | Refactorización, más retirada de efectos Merit | Criterios existentes de evaluación y resultados sin ganancias Merit |
| Ranked y rating | Conservar ambos según D1 | Refactorización, más retirada de dependencias funcionales | Selección de ejercicio sin ligas, condiciones no económicas de entrada/cierre y presentación de placement |
| Autenticación y perfil básico | Conservar dentro de D1 | Refactorización; quitar presentación/acciones retiradas | Límites del perfil básico y compatibilidad de datos |
| Persistencia y estados | Conservar el soporte del núcleo | Refactorización de contratos autorizados | Modelo real, identidad de envío, estados de evaluación y emparejamiento |
| Torneos | Retirar del producto objetivo | Cambio funcional aprobado U2/U3 | Dependencias compartidas, navegación, jobs, datos históricos |
| Ligas | Retirar; no conservar la alternativa numérica contradictoria de D1 | Cambio funcional aprobado U2/U3 | Selección del ejercicio Ranked y mensajes de rating sin ligas |
| Reputación | Retirar, incluida su consulta y efectos | Cambio funcional aprobado U2/U3 | Separación de precio/penalización y estado de sesión conservado |
| Merit | Retirar moneda, ganancias y **todos** sus usos, incluido Ranked | Cambio funcional aprobado U3 | Separar evaluación, guardado/perfil y sesión de sus costes; compatibilidad histórica |
| Temporadas | Incorporar; HU 3.27/3.28 ya estaban en ISW1 y no se localizaron implementadas | Incorporación funcional aprobada U2/U3 | Períodos, participación, ranking, cierre, historial y pendientes de evaluación |
| Ranking global, posición global y leaderboard de liga | Excluir sus visualizaciones según D1 | Retirada funcional delimitada | Distinguirlos del ranking por temporada; revisar dependencias heredadas de Pantheon sin aprobar su conservación |
| Detalle de una partida para revisar errores | Eliminar esa HU del alcance según D1 | Retirada funcional delimitada | No confundir esa consulta posterior con Results Screen del flujo inmediato, conservada por D1 |
| Perfil de otro jugador desde leaderboard | Eliminar esa consulta según D1 | Retirada funcional delimitada | No confundirla con perfil básico propio conservado |
| Penalización por abandonar una partida | Eliminar según D1 | Retirada funcional delimitada | Cierre/limpieza de sesión puede requerir criterio; la penalización no se reabre |
| Rediseño visual/responsividad | Excluido como objetivo de intervención por D1 | Fuera de intervención | Ajustes mínimos que necesite una retirada deben justificarse como parte de ésta |
| Reescritura completa, funciones ajenas y patrones sin problema demostrado | Excluidos | Fuera de intervención | No habilitarlos desde este documento |
| Baseline histórico | Conservar como referencia del antes | Custodia de evidencia | No reinterpretar sus tests de funciones retiradas como requisitos futuros |

La retirada de Merit resuelve la duda anterior: no es un punto abierto de permanencia. No se ha aprobado una moneda sustituta. El permiso sobre datos existentes y despliegues sigue siendo distinto del alcance funcional.

## Dependencias que deben separarse

Estas son observaciones de C1/G2; no una lista aprobada de clases a modificar.

| Frontera | Evidencia de código auditado | Consecuencia para aceptación |
|---|---|---|
| Ranked dentro de Competitions | [selección por liga](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Competitions.kt#L85) y [entrada](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/screens/Competitions.kt#L216) | La pantalla no se puede clasificar entera como torneos. Falta la regla de ejercicio sin ligas. |
| Entrada y abandono mezclados con economía | [applyMeritAction](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/users/applyMeritAction.ts#L164) | Retirar Merit no elimina automáticamente autenticación, límite diario, rachas ni estado de sesión. El equipo confirma cuáles se conservan. |
| Evaluación con economía, placement y reputación | [submissionEvaluationEngine](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L303) | Preservar score/evaluación/rating y retirar los efectos aprobados con pruebas distintas. |
| Rating con contadores de ligas | [emparejamiento](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/submissionEvaluationEngine.ts#L529) y [ghostMatchProcessor](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/submissions/ghostMatchProcessor.ts#L75) | Retirar ligas no implica retirar rating ni el mecanismo que resuelve Ranked pendiente. |
| Limpieza y penalización juntas | [rankedSessionCleaner](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/functions/src/users/rankedSessionCleaner.ts#L21) | Separar limpieza de sesión del efecto de reputación. |
| Cabecera/perfil/reveal con League | [UserHeaderCard](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/utils/UserHeaderCard.kt#L31) | Afecta pantallas conservadas; definir presentación sin decidir un rediseño general. |
| Modelo compartido | [Users](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/app/src/main/java/com/inkr8/data/Users.kt#L3) | Compatibilidad de lecturas y retirada lógica son distintas de eliminación física de campos/datos. |

El [mapa técnico completo](reference/TECHNICAL_MAP_2026-10-03.md) conserva las fronteras F01–F12 y contratos C01–C06 del análisis. Algunas rutas de consulta allí son temporales; los enlaces fijados aquí apuntan al commit auditado.

## Criterios todavía pendientes

| ID | Criterio que falta | Evidencia necesaria | Estado |
|---|---|---|---|
| AC-01 | Selección de Standard/On-Topic en Ranked sin ligas | Regla del equipo, ejemplos y criterios previos que correspondan | PENDIENTE |
| AC-02 | Entrada/cierre de Ranked sin pago Merit, efectos de reputación ni penalización de abandono | Confirmar límites diarios, rachas, estado/limpieza de sesión abandonada y errores, sin restablecer penalizaciones excluidas | PENDIENTE |
| AC-03 | Resultados inmediatos, placement y perfil básico sin funciones retiradas | Criterios existentes; excluir detalle de partida para revisar errores y consulta de perfil desde leaderboard. Historial de temporadas se concreta en AC-06 | PENDIENTE |
| AC-04 | Temporadas: períodos, zona horaria, participantes y envíos admitidos | Criterios originales de HU 3.27/3.28 o decisión humana identificada | PENDIENTE |
| AC-05 | Temporadas: magnitud del ranking, elegibilidad, desempates y relación con rating/placement | Regla humana verificable | PENDIENTE |
| AC-06 | Temporadas: cierre, historial y envíos/evaluaciones tardíos | Regla humana verificable | PENDIENTE |
| AC-07 | Compatibilidad con documentos y borradores previos | Inventario y tratamiento decidido por el equipo | PENDIENTE |

No asignar responsables ficticios ni completar estas reglas por inferencia. No se eligen colecciones, resets, fórmulas, jobs, índices, patrones ni arquitectura futura.

## Etapa autorizada actual

Crear/revisar AGENTS/docs derivados, consultar código y preparar aceptación. U5 permite un commit y publicación de documentación en rama/PR de GitHub. No autoriza modificar producción/configuración, ejecutar retiradas, añadir temporadas, editar originales/baseline, fusionar a master o operar Firebase. Consultar [STATUS](STATUS.md) ante nuevas instrucciones y [DECISIONS](DECISIONS.md) para registrar la evolución.
