# Criterios de aceptación de Inkr8

Continuación del 17 de octubre de 2026 (America/Lima), a partir de `prompt_picas_e_historias_de_usuario.md`.

Se preservaron los cambios locales previos. Las funciones se incorporan a los cinco destinos existentes, con dominio independiente y adaptadores Firebase. La HU 1.1 describe Google, pero repite los criterios de ejemplo de 1.6: se conservó Google y se atendieron ambos criterios del ejemplo. Las estadísticas retiradas en la refactorización original se reincorporaron por este nuevo alcance.

| Historias | Implementación |
|---|---|
| 1.1, 1.5, 1.6 | Home con fonética, tipo, rareza y definición; audio TTS; ejemplo solicitado al pulsar el botón. Día estable para evitar cambiar la palabra al cruzar medianoche. Errores y reintentos independientes. Datos incompletos provocan error, sin inventar metadatos. |
| 1.2 | Username de 2–20 caracteres, reserva transaccional con normalización Unicode y sin distinguir mayúsculas. Dos usuarios concurrentes no pueden reservarlo. Se conserva en la cuenta y no se permite cambiarlo por otro. Mensaje de duplicado con el nombre solicitado. |
| 1.3, 1.4, 1.11 | Sesión Firebase persistente, restauración por observador, cierre manual, errores de cierre/recuperación y progreso almacenado por UID. Cerrar sesión cancela observadores y no elimina partidas remotas. |
| 1.7, 1.9, 1.10 | Perfil con partidas Ranked evaluadas, fecha, modalidad, puntaje y resultado. Partidas jugadas, promedio, mejor puntaje y victorias entre encuentros resueltos. Los datos se derivan de envíos persistidos, se refrescan al abrir Perfil y al recibir una evaluación/resultado de la partida observada. Fallos de consulta y de confirmación se muestran explícitamente. |
| 2.1, 2.2 | Restricciones y extensión visibles; editor y validación tanto cliente como servidor. Envío bloqueado y mensaje visible si el texto no cumple el mínimo. Reintentos de entrada conservan el mismo ID. |
| 2.3 | Evaluación real 0–100 con coherencia, vocabulario, creatividad y cumplimiento en el prompt de R8. Timeout a 90 segundos, envío persistido para reintento automático, recuperación por scheduler y aviso en aplicación al llegar un resultado observado. |
| 2.4 | El mismo destino cambia al resultado con puntaje, comentarios por categoría y restricciones. Una estructura de detalle incompleta conserva el puntaje válido y muestra el mensaje correspondiente. |
| 2.5 | Practice con evaluación y reintento sin entrada; excluida del historial y las estadísticas competitivas. Se conserva el envío técnico para recuperar resultados. |
| 3.1–3.3, 3.7 | Merit de servidor, entrada Ranked transaccional, saldo insuficiente bloqueado, recompensa atómica e idempotente y monto mostrado. Los errores de saldo/confirmación se exponen en UI. |
| 3.8 | Cola asíncrona de párrafos evaluados; usuarios diferentes, misma modalidad y delta de rating máximo 30. Tras 48 horas de cola se compara con el promedio Ranked del jugador; con una sola partida, la referencia coincide con su puntuación. |
| 3.9–3.12 | Rating actual, resultado traducido a victoria/derrota/empate y variación aplicada. Un error de emparejamiento se guarda para mostrarlo y se limpia al recuperar el encuentro. Datos de resultado dañados no se convierten silenciosamente en una variación cero. |
| 3.13–3.16 | On-Topic con tópico, categoría, extensión, dos palabras y reglas de primera persona/pregunta. Validación de palabras completas, extensión y reglas en servidor; pertinencia temática por R8. Resultado por restricción y errores de validación/detalle. |
| 3.27 | Ranking de temporada con posiciones compartidas en empates y posición propia resaltada, incluso fuera del primer grupo de 50. |
| 3.28 | Selección de temporadas cerradas en Perfil: posición, rating de cierre y Merit obtenido durante esa temporada. Error y reintento separados. |

## Condiciones de operación

- Requiere configuración Firebase/OAuth, contenido completo, secreto OpenAI y despliegue de los doce exports e índices actuales. No se ejecutó un despliegue ni se modificaron datos remotos.
- Los avisos de resultados son dentro de la aplicación mientras mantiene el observador; no hay notificaciones push con la aplicación cerrada.
- Se conserva la política de cierre de temporada: los emparejamientos pendientes de temporadas anteriores expiran y no alteran el rating de la nueva. Las evaluaciones tardías conservan Merit.
- Los registros anteriores sin `schemaVersion: 2` permanecen fuera del contrato actual. Las recompensas antiguas de temporada no contabilizadas no se reconstruyen. El historial nuevo conserva todas las partidas competitivas del esquema actual, con lecturas paginadas en backend.
- La evidencia local no sustituye la comprobación en dispositivo de OAuth, voz instalada, conectividad real ni la evaluación externa de R8.

## Validación

47 pruebas aprobadas: 17 de dominio/backend, 17 de integración Firestore, 3 de dominio Kotlin y 10 Android. Compilación debug y ambos lints aprobados; Android informa `No issues found.` Arquitectura y whitespace aprobados. La evidencia y las condiciones del entorno se registran en `REFACTORING_REPORT.md`, en la sección de continuación de criterios de aceptación.
