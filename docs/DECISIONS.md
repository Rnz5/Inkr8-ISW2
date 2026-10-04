# Registro de decisiones y pendientes

**Versión inicial:** 0.1 · 03/10/2026. Los IDs son referencias documentales creadas para ordenar evidencia; no equivalen a actas firmadas. [Fuentes](SOURCE_INDEX.md).

## Decisiones humanas existentes

| ID | Decisión | Quién / procedencia verificable | Fecha conocida y evidencia | Alcance de lo decidido |
|---|---|---|---|---|
| DEC-01 | D1 es la delimitación aprobada por el grupo y fuente principal de alcance | Marco comunicó la aprobación del grupo | Incorporada en este chat 03/10/2026: U2; no se conoce fecha del acto grupal | Aprobación del alcance, no permiso de implementar en esta fase |
| DEC-02 | Torneos, ligas y reputación fuera del producto objetivo | Marco en U2; conversación con PO aportada en U3 | U2/U3 compartidos 03/10/2026 | Retirada funcional; resuelve alternativas contradictorias de D1 |
| DEC-03 | Merit fuera: moneda, ganancia y todos sus usos, incluido pago para Ranked | PO en la captura aportada por Marco | U3 compartida 03/10/2026; no se fija fecha del mensaje sólo con la hora de la imagen | La respuesta «volarlos» incluye Merit. Resuelve la duda de la transcripción anterior; no define sustituto |
| DEC-04 | Incorporar temporadas por estar en HU y faltar en base | Marco en U2; PO en U3 | U2/U3; I1, HU 3.27/3.28 | Inclusión aprobada; reglas de funcionamiento siguen abiertas |
| DEC-05 | Practice, Ranked, escritura/evaluación, autenticación, perfil básico, persistencia/estados y rating permanecen dentro del núcleo | D1, aprobado según DEC-01 | D1/U2; U3 no elimina Ranked ni rating | Preservación dentro del alcance; separar retiradas que afecten el flujo |
| DEC-06 | Marco y Renzo coordinan directamente la propuesta y ejecución con el equipo | Documento D2 | D2 actualizado 02/10/2026 | No asigna por sí mismo cada método, test o autoridad final de aceptación |
| DEC-07 | Baseline mínimo aislado JVM, compilando fuentes reales sin cambiar producción | Autorización humana registrada en A1; contrastada con C1/B1 | Historia y fuentes de la auditoría | Permiso histórico de esa tarea específica; no autorización general de código |
| DEC-08 | Crear ahora AGENTS inicial y docs de trabajo, sin comenzar refactorización | Usuario Marco en este chat | U4, 03/10/2026: «ok iniciemos el siguiente paso que me acabas de recomendar.» | Habilita la base documental recomendada. Sustituye la prohibición temporal anterior de escribir esos documentos |
| DEC-09 | Priorizar problemas concretos de responsabilidad/acoplamiento/duplicación y justificar patrones con alternativas e impacto | D2 | D2 | Principio de trabajo; no selección concreta de patrones, arquitectura o framework |
| DEC-10 | Excluir detalle de partida para revisar errores, penalización por abandono, visualización de ranking/posición global y leaderboard de liga, y perfil de jugador desde leaderboard | D1 aprobado según DEC-01 | D1, apartado 3, HU a eliminar | No reabrir estas exclusiones al concretar resultados inmediatos, perfil básico, cierre de sesión o temporadas |
| DEC-11 | Publicar AGENTS y documentación de trabajo de la manera propuesta | Marco, instrucción U5 en este chat | 03/10/2026: enlace al repositorio y solicitud de subirlo correctamente | Commit exclusivamente documental en rama y PR hacia master; sin originales completos, captura privada ni cambios de producción |

La documentación registra estas decisiones; no las crea la IA. Sus fuentes prevalecen sobre recomendaciones previas de asistencia. Las reglas académicas del profesor gobiernan qué autoría y evidencia pueden acreditarse.

## Aspectos que siguen abiertos

| ID | Qué falta | Por qué condiciona trabajo posterior | Fuente / cómo resolver |
|---|---|---|---|
| OPEN-01 | Criterios originales completos de HU y backlog Anexo A | Los títulos del informe no reemplazan aceptación detallada | I1 y fuente original del backlog; no se encontró copia independiente en la auditoría |
| OPEN-02 | Reglas de temporadas | Inclusión no define período, ranking, cierre, historial o pendientes | AC-04–AC-06 en REFACTOR_SCOPE; decisión del equipo/PO |
| OPEN-03 | Ranked tras retirada de ligas, reputación y Merit | Falta regla de ejercicio y controles/estado de sesión no económicos; penalización de abandono ya está excluida por D1 | AC-01–AC-03; caracterización C1 y regla humana sin reabrir DEC-10 |
| OPEN-04 | Compatibilidad de datos/borradores y tratamiento histórico | Retirada funcional no autoriza borrado físico | AC-07; inventario antes de proponer cambios |
| OPEN-05 | Clases concretas y problemas de diseño a intervenir | No se hizo diagnóstico exhaustivo ni selección final | Análisis humano y evidencia del mapa; sin imponer patrones |
| OPEN-06 | Alternativas/patrones/arquitectura final | Requieren justificación y autoría humana según P1/D2 | Etapa de diseño posterior autorizada |
| OPEN-07 | Responsables y procedimiento final de aprobación | La tabla A/B/C de D2 es una **propuesta** | Ratificación por el equipo; no hacerla obligatoria por inferencia |
| OPEN-08 | Configuración original y fuente auténtica de R8 | Falta build/integración reproducible | Recuperar fuentes no secretas indicadas en STATUS |
| OPEN-09 | Correspondencia de releases/sprints heredados con entrega ISW2 | No inventar cronología académica | D2 y aclaración del profesor |
| OPEN-10 | Pruebas por integrante y evidencia IA individual | Baseline no acredita participación ni todos los umbrales | P1/P3, asignaciones y ejecuciones reales |

**Resuelto:** la permanencia de Merit dejó de estar abierta tras U3. No reabrir DEC-02/03/04 salvo que llegue una instrucción humana que cambie el alcance.

## Propuestas documentales de esta versión

La estructura de docs, sus IDs y el protocolo inicial de evidencia son organización propuesta por IA para la tarea autorizada. No se registran como decisiones grupales sobre arquitectura, diseño o producto. Toda selección técnica pendiente seguirá identificada como tal.

## Cómo registrar una nueva decisión

Esta ficha es una plantilla; no contiene una decisión tomada:

- ID y fecha conocida:
- Pregunta/problema:
- Fuentes y comportamiento actual comprobado:
- Alternativas consideradas por el equipo:
- Decisión humana y su justificación:
- Quién decidió y evidencia de aceptación:
- Clasificación: preservación / retirada / incorporación / fuera de intervención:
- Criterios verificables y pruebas:
- Impacto y decisiones anteriores que reemplaza:
- Aporte de IA y revisión humana:
- Estado: propuesta / confirmada / reemplazada:

No rellenar fechas, responsables, análisis humano o aprobaciones que no estén disponibles. Una nueva petición autorizada se registra con su alcance; la ausencia de un flujo A/B/C ratificado no exige por sí sola pedir aprobación para cada acción documental.
