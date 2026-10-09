El núcleo perdía texto ante rechazo de persistencia y podía resolver una espera con otro envío. Esta rama consolida A01–A07, corrige esos contratos y añade las funciones aprobadas: retiro activo de torneos/ligas/reputación y temporadas mensuales UTC. Ranked, rating y Merit conservan sus fórmulas y economía; los datos históricos no se migran ni se borran.

## Cambios para revisión

- Fuentes/Gradle/Functions/evaluateWithR8 auténticos recuperados; credenciales originales excluidas.
- Borradores por UID/ejercicio/revisión, confirmación de guardado, bloqueo de segundo envío y espera/reintento del mismo UUID; refresco Results del mismo ID.
- Matching/evaluación/ghost atómicos, temporadas con ACK/liquidación/cierre tras pendientes y sin premios nuevos/backfill. Selección Ranked Standard/On-Topic y factor histórico de precio conservado.
- Propuesta **nueva** de reglas Firestore (no original/no desplegada), initializer idempotente y eliminación del token sensible del log. Borrado Ranked mediante callable que protege ACK/liquidación; API Android conserva callbacks.
- Informe P4 DOCX/PDF, seis UML editables, trazabilidad de métodos, APK demo y AndroidTest, evidencia roja/verde y comandos en `delivery/README.md`.

## Validación ejecutada

- Android auténtico assembleDebug/testDebugUnitTest y Functions npm run build: PASS. 19 métodos unitarios reales conservados.
- Android/Compose final **7 métodos PASS**: Practice/Ranked Standard/On-Topic, historial propio mediante callable con lectura directa denegada, rechazo real conserva texto/borrador, callback del repositorio protegido.
- Reglas finales:39 REST/Auth PASS; registro atómico de nombre:6 PASS. Temporadas/UTC/tardíos/cierre concurrente y repetido/volumen/initializer:14 SDK PASS, con relojes/metadata controlados.
- Borrado estacional:roja REST200 frente a403 →16 casos SDK/REST/HTTP PASS. Token log:roja1/1 →2 PASS, comportamiento económico previo intacto.
- Auditor de conservación:287 contrastes **estáticos** PASS y3 mutaciones aisladas detectadas; copia limpia con autocrlf=true conserva artefactos/oráculos. No se cuentan repeticiones como cobertura adicional.
- Evidencia original CORE/SEA reutilizada: Ranked11, ghost2, lifecycle1 y contratos de borrador/identidad/reintento. Informe23 páginas revisadas; manifiesto SHA y protección del baseline.

## Autoría y límites indispensables (PR en borrador)

Código, tests, revisión y documentación derivados: **Codex**, bajo DEC-AI-AUTH-001 comunicado por Marco. Los commits consolidan snapshots reales; no reconstruyen sprints ni aportes/manualidad/revisión humana.

R8 usa evaluateWithR8/SDK genuinos pero **HTTP double fijo80**. Auth es anónimo demo; fixtures Admin, transporte/relojes controlados y host que omite OAuth/Ads están declarados. El APK com.inkr8.lab abre sin instrumentación, pero requiere emuladores locales y ADB reverse; AndroidTest sólo para sondas. No es APK productivo cloud.

Faltan reglas auténticas si existen y configuración/entorno R8 de prueba autorizado. Philosopher conserva un placeholder previo de compras que bloquea aceptación cloud; OAuth/Ads/billing/cron cloud no validados. Las reglas propuestas no acreditan antiabuso/reserva única del servidor. Cierre de cuenta anterior queda denegado y requiere ciclo Auth/política seguros; no existe aprobación de retirarlo. Pendientes individuales de P3/P5, métricas/actas e informe humano real se identifican sin inventarlos.

No se han desplegado servicios ni modificado datos reales. **No fusionar ni desplegar este PR** antes de revisión del equipo y resolución de las validaciones pertinentes. `docs/FINAL_DELIVERY.md` separa cierre local, defectos previos, cobertura y dependencias.
