El núcleo perdía texto ante rechazo de persistencia y podía resolver una espera con otro envío. Esta rama consolida A01–A07, corrige esos contratos y añade las funciones aprobadas: retiro activo de torneos/ligas/reputación y temporadas mensuales UTC. Ranked, rating y Merit conservan sus fórmulas y economía; los datos históricos se preservan.

## Cambios para revisión

- Fuentes/Gradle/Functions/evaluateWithR8 auténticos recuperados; credenciales originales excluidas.
- Borradores por UID/ejercicio/revisión, confirmación de guardado, bloqueo de segundo envío y espera/reintento del mismo UUID; Results se refresca sólo para ese ID.
- Matching/evaluación/ghost atómicos; temporadas con ACK/liquidación/cierre tras pendientes, sin premios nuevos ni backfill. Ranked Standard/On-Topic y factor histórico de precio conservado.
- Propuesta **nueva** de reglas Firestore (no original/no desplegada), initializer idempotente y retirada del token sensible del log. Borrado Ranked por callable que protege ACK/liquidación; API Android conserva callbacks.
- **Cierre de acceso aprobado por Marco**: marcador Functions-only, Auth disable/revoke y signout confirmado. Perfil, nombre, historial, saldos, rating, reservas y ledgers permanecen. Tokens previos denegados; fallo parcial Auth permite reintento sin reset. La corrección conserva HttpsError en la ruta económica.
- Informe P4 DOCX/PDF de23 páginas, seis UML editables, mapa de métodos, APK demo/AndroidTest, evidencia roja/verde y comandos en `delivery/README.md`.

## Validación ejecutada

- Android auténtico assembleDebug/testDebugUnitTest, Functions npm run build y lab APK/testAPK: PASS sobre el código final;19 métodos unitarios reales PASS.
- **8 métodos Android/Compose PASS**: Practice/Ranked Standard/On-Topic, historial propio con lectura admin directa denegada, rechazo real que conserva texto/borrador, callback de borrado protegido y cierre de acceso Settings→repositorio→callable→Auth/signout.
- Cierre de cuenta: **19 casos REST/Auth/SDK/HTTP PASS**: tokens anteriores, reapertura denegada,8 concurrentes/repetidos, refresh disabled, retención económica/histórica y fallo parcial Auth explícitamente doblado con reintento SDK real. Sonda previa con callable ausente y fallo400-vs403 intermedio conservados; la primera es preparación, no un caso funcional ejecutado.
- Regresiones afectadas finales:39 permisos,6 registro de nombre,10 retiradas/economía,14 operación/UTC/tardíos/concurrencia/volumen,16 borrado protegido y2 higiene de log PASS. Se mantienen fórmulas y aserciones; repeticiones no se suman como cobertura nueva.
- Auditor de conservación: **289 contrastes estáticos PASS**, oráculos anteriores y deltas explícitos;3 mutaciones aisladas previas y1 de fuente nueva detectadas. Copia limpia con autocrlf=true y hashes de entrega verificados. No son pruebas funcionales adicionales.
- Evidencia CORE/SEA reutilizada: Ranked11, ghost2, lifecycle1 y contratos de borrador/identidad/reintento. Originales/baseline/rojas y respaldo privado preservados.

## Autoría y límites indispensables (PR en borrador)

Código, pruebas, revisión y documentación derivados: **Codex**, bajo DEC-AI-AUTH-001 comunicado por Marco. Decisiones funcionales humanas tienen su procedencia; los commits no reconstruyen sprints ni aportes o revisión estudiantil.

evaluateWithR8/SDK son auténticos, pero **R8 HTTP es un doble fijo80**. Auth anónimo demo, fixtures Admin, relojes/metadata controlados y host sin OAuth/Ads están declarados. APK com.inkr8.lab abre sin instrumentación; requiere emuladores locales y ADB reverse. AndroidTest sólo automatiza sondas. No es APK productivo cloud.

No se aportaron reglas originales ni configuración/ID R8/Google Play de prueba autorizados. Philosopher conserva placeholder previo de compras que bloquea aceptación cloud; OAuth/Ads/billing/scheduler e IAM/Auth cloud no validados. La propuesta de reglas no acredita antiabuso de metadata ni reserva única servidor. Evidencia individual P3/P5, pares R1/R2 y actas/métricas reales siguen pendientes: la respuesta «aplica lo recomendable» aprueba la política de acceso, no aporta esas fuentes.

No se fusionó, desplegó ni modificaron datos reales. PR en borrador para revisión del equipo y resolución de validaciones indispensables. `docs/FINAL_DELIVERY.md` distingue cierre local, defectos previos, cobertura y dependencias.
