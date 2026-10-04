# Inkr8 — instrucciones de trabajo

Versión inicial 0.1 · 03/10/2026 · documentación y publicación documental autorizadas por Marco (U4/U5).
Este archivo organiza el trabajo; no fija arquitectura ni reglas funcionales pendientes.

## Contexto y lectura inicial

Inkr8 es un proyecto académico Android/Kotlin + Firebase Functions/TypeScript de Ingeniería de Software II. Este repositorio contiene app, functions y un arnés JVM aislado. El baseline tiene ejecución histórica comprobada, pero el build del producto completo sigue incompleto en la versión auditada. Verificar la versión/archivos antes de ejecutar instrucciones.

Consultar [docs/README.md](docs/README.md) y [docs/STATUS.md](docs/STATUS.md). Para la tarea concreta, leer [alcance](docs/REFACTOR_SCOPE.md), [decisiones](docs/DECISIONS.md) y [reglas del profesor](docs/PROFESSOR_REQUIREMENTS.md). Usar [SOURCE_INDEX](docs/SOURCE_INDEX.md) para localizar fuentes originales; no cargar toda la evidencia si sólo hace falta una parte.

Repositorio de código: https://github.com/Rnz5/Inkr8-ISW2
Commit auditado con baseline: `64983846d6dbf4cdfafcdd508464cd140a2a862e`.
Producción anterior al arnés: `3d107ae6bb93f7c9d4bf9e103eccac1ae2d931de`.
Las referencias de código en documentos describen ese snapshot; verificar la versión antes de atribuirles vigencia en otro checkout.

## Alcance humano confirmado

D1 fue aprobado por el grupo según U2. U3 resuelve la duda anterior sobre Merit:

- Conservar núcleo de escritura/evaluación, Practice, Ranked, rating, autenticación, perfil básico y soporte de persistencia/estados dentro de D1.
- Retirar del producto objetivo torneos, ligas, reputación y **Merit completo: moneda, ganancias y todos sus usos**, incluido pago para Ranked.
- Incorporar temporadas por las HU 3.27/3.28; se aprobó su inclusión, no sus reglas detalladas.
- No elegir una moneda sustituta ni inferir la eliminación de Ranked/rating.
- D1 excluye detalle de partida para revisar errores, penalización por abandono, ranking/posición global, leaderboard de liga y perfil de jugador desde leaderboard. No confundir esas exclusiones con resultados inmediatos, perfil básico propio, limpieza de sesión o ranking/historial por temporada.
- No reabrir estas decisiones salvo nueva instrucción humana que las cambie. Siguen abiertos selección del ejercicio Ranked sin ligas, reglas no económicas de entrada/cierre, períodos/ranking/historial de temporadas y compatibilidad de datos.

Separar siempre refactorización que conserva comportamiento, retirada/incorporación funcional aprobada y asuntos fuera de intervención. Registrar cambios deliberados como tales. La aprobación de una retirada no equivale a permiso de borrar datos existentes.

## Permiso de la etapa actual

U4 autorizó crear AGENTS/docs iniciales y U5 autorizó publicarlos mediante commit exclusivamente documental en rama/PR hacia master. Estas autorizaciones sustituyen las prohibiciones temporales correspondientes de la contextualización. Se puede continuar lectura, análisis, documentación derivada y preparación de aceptación.

El permiso de publicación se limita a esta documentación; no autoriza fusión a master, refactorización, cambios de producción/configuración/baseline/originales, datos/servicios, Firebase, arquitectura/patrones finales ni implementación de temporadas. Una petición posterior del usuario puede ampliar el trabajo: interpretar su alcance real y registrarlo, sin exigir otra aprobación para tareas que ya haya autorizado.

Si falta una decisión necesaria, avanzar en lo independiente, identificar el punto pendiente y consultar sólo lo que no esté resuelto. No interpretar silencio, tiempo transcurrido o «continúa» como una elección funcional. Antes de pedir permiso o volver a preguntar, revisar las autorizaciones y decisiones vigentes de este chat y DECISIONS.

## Reglas académicas y autoridad de fuentes

Las instrucciones actuales del usuario gobiernan la tarea; estas instrucciones locales no pueden contradecirlas ni las de sistema/desarrollador. Para acreditar el proyecto académico, aplicar los requisitos oficiales del profesor documentados en PROFESSOR_REQUIREMENTS.

- Profesor: reglas de autoría, pruebas, evidencia, entrega y sustentación.
- Equipo/PO: decisiones humanas de alcance y diseño, con procedencia identificada.
- Código/ejecución: comportamiento implementado y resultados realmente observados.
- Síntesis de IA: apoyo y propuestas; nunca una aprobación humana por sí mismas.

P1 §2.7 reserva a estudiantes BD/relaciones, lógica crítica e integración frontend/backend. La asistencia y las pruebas deben seguir sus límites académicos, con validación y análisis humano. No presentar implementación automática como trabajo escrito o integrado manualmente por el estudiante. No usar IA durante la sustentación real.

La tabla de aprobaciones A/B/C de D2 es una propuesta, no un procedimiento ratificado. No convertirla en una exigencia de confirmación universal.

## Evidencia y documentación

No inventar HU, aceptación, sprints/releases, resultados, responsables, decisiones, porcentajes de influencia IA ni revisión humana. No usar ejemplos de la plantilla ISW2 como logros reales. Identificar qué es hecho, decisión, inferencia y propuesta, con fuente y fecha conocida.

Actualizar el documento canónico correspondiente: alcance en REFACTOR_SCOPE, decisiones en DECISIONS y estado en STATUS. Conservar los documentos fuente privados y el baseline histórico existente sin reescribirlos; nuevos registros van en documentos derivados. No editar snapshots históricos para que aparenten describir etapas posteriores.

Aplicar [EVIDENCE_PROTOCOL](docs/EVIDENCE_PROTOCOL.md) como propuesta operativa inicial de registro; no atribuirle ratificación grupal. No reconstruir conversaciones IA ni contribuciones ausentes. No almacenar secretos ni incluir capturas privadas/originales completos del profesor en esta publicación pública; usar SOURCE_INDEX para localizar fuentes.

## Pruebas y bloqueos

El baseline JVM registró 22 PASS, 0 FAIL/ERROR/SKIPPED el 02/10/2026. No prueba la aplicación completa, integración ni cumplimiento individual de la rúbrica. Sus suites económicas/de torneos/reputación son evidencia del antes, no obligaciones de conservar funciones retiradas.

Consultar [TESTING_BASELINE](docs/TESTING_BASELINE.md) para SHAs, comando comprobado, entorno y disponibilidad de evidencia. El comando JVM sólo corresponde al checkout con su arnés y JDK completo. No inventar un comando de app/Functions: faltan configuración original y evaluateWithR8 en el checkout auditado. Recuperar fuentes auténticas cuando una tarea posterior lo autorice; no reconstruir versiones o evaluador por suposición.

Para cambios documentales, verificar enlaces, coherencia del alcance, integridad de las copias y ausencia de afirmaciones no respaldadas. No ejecutar builds ajenos a esa tarea. Para cambios futuros, elegir pruebas según el riesgo y separar caracterización, aceptación de retiradas y pruebas de temporadas.

## Comunicación y siguiente trabajo

Responder en español claro, con resultados y límites concretos. Mantener pendientes visibles y evitar repetir preguntas ya contestadas. Al terminar, indicar qué se creó/cambió, cómo se comprobó y qué continúa abierto.

La siguiente tarea propuesta es el chat «Inkr8 — Alcance operativo y criterios de aceptación», usando el mensaje de docs/README. No crear ni enviar mensajes a otro chat sin autorización explícita del usuario.

Convención de este archivo contrastada con la [documentación oficial de AGENTS.md](https://learn.chatgpt.com/docs/agent-configuration/agents-md). Esa guía no aporta reglas de negocio ni autoriza trabajo académico.
