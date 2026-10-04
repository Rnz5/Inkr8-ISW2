# Estado de trabajo de Inkr8

**Actualizado:** 03/10/2026 · America/Lima · versión documental 0.1.

## Etapa actual y autorización

Se crearon AGENTS/docs iniciales por U4. U5 autoriza publicar esta base documental en una rama con PR hacia master. La propuesta se limita a documentación; la refactorización y los cambios funcionales no han comenzado.

Base de código examinada y usada para la propuesta: `64983846d6dbf4cdfafcdd508464cd140a2a862e`, master. Esta edición añade documentos al repositorio oficial; la redacción y auditoría anteriores se hicieron en copias separadas. No se ha reconstruido el build ni cambiado producción. [Fuentes](SOURCE_INDEX.md).

## Resultados y límites

| Resultado | Evidencia | Estado |
|---|---|---|
| Auditoría y mapa técnico | G1/G2, ediciones publicables en reference | Completados como análisis; no diseño aprobado |
| Baseline JVM | 22 PASS, 0 FAIL/ERROR/SKIPPED el 02/10/2026 | Histórico; no prueba producto completo ni requisitos individuales |
| Alcance humano | D1/U2/U3, DEC-01–05/10 | Confirmado según fuentes |
| AGENTS y nueve docs principales | Índice docs/README | Creados; revisión humana de esta versión pendiente |
| Publicación documental | U5, DEC-11; rama codex/documentacion-inicial-inkr8 | Propuesta de integración en master mediante PR |
| Código/configuración/baseline histórico | Fuera del diff documental | Sin cambios |
| Implementación, fusión a master o despliegue | Ninguno | No autorizados en esta tarea |

El [registro de publicación](evidence/DOC-002_PUBLICATION.md) identifica selección de archivos y comprobaciones. La revisión de IA no equivale a aceptación grupal.

## Alcance asentado

Torneos, ligas, reputación y Merit completo fuera; núcleo de escritura/evaluación, Practice, Ranked, rating y perfil básico conservados; temporadas por incorporar. Mantener exclusiones específicas de D1 de detalle de partida, penalización por abandono, ranking/posición global, leaderboard de liga y consulta de perfil desde leaderboard. [Alcance](REFACTOR_SCOPE.md), [decisiones](DECISIONS.md).

## Pendientes

| Pendiente | Qué condiciona | Trabajo que puede continuar |
|---|---|---|
| Criterios completos del backlog / Anexo A | Aceptación y trazabilidad | Recuperar fuentes auténticas disponibles |
| Ejercicio Ranked sin ligas | Cambio funcional del ejercicio | Caracterizar selección actual y consultar regla humana |
| Entrada/cierre no económicos | Ranked sin Merit/reputación/penalización de abandono | Separar estado, límites y efectos retirados |
| Temporadas: períodos, ranking, cierre e historial | HU 3.27/3.28 | Matriz de aceptación sin completar reglas por inferencia |
| Compatibilidad de datos/borradores | Retirada y evolución del modelo | Inventario sin borrado de datos |
| Configuración original y R8 | Build/integración | Análisis documental y estático |
| Responsables y pruebas por integrante | Evidencia académica | Asignaciones reales y pruebas según rúbrica |
| Correspondencia releases/sprints | Informe | Registros auténticos y aclaración del profesor |

## Bloqueos técnicos del commit auditado

Faltan configuración Gradle principal/app y parte del wrapper; package.json, lockfile y tsconfig de Functions; evaluateWithR8.ts; configuración/reglas Firebase completas. Se observaron referencias a recursos Android ausentes. README de código no documenta instalación integral.

No se compiló ni probó el producto Android/Functions completo ni se verificó un despliegue. Esta publicación documental no corrige esos bloqueos.

## Siguiente tarea

**Chat:** Inkr8 — Alcance operativo y criterios de aceptación.

Leer AGENTS y los documentos del índice, D1/U2/U3/I1 y backlog real si está disponible. Consultar [mapa técnico G2](reference/TECHNICAL_MAP_2026-10-03.md), secciones 4/6/7. Producir matriz por función/HU y concentrarse en AC-01–AC-07.

Cierre: criterios originales recuperados y diferencias identificadas; reglas nuevas pendientes conservan condición de propuesta hasta decisión humana. Continuar en lo independiente si falta una respuesta. No interpretar silencio o «continúa» como una elección funcional.

## Límite operativo

U5 habilita commit/publicación **documental** en rama/PR de GitHub. No habilita fusión a master, cambios de código/configuración/baseline/originales, retirada ejecutada, temporadas, patrones/arquitectura, datos/Firebase o despliegues. Una instrucción posterior puede ampliar el trabajo dentro de su alcance real. Los estados históricos de G1/G2 no revocan U4/U5.
