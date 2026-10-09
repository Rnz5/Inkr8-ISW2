# FIN-002 entrega revisable y cierre local

**Publicación actualizada tras ACCOUNT-ACCESS-001:** [PR #2 en borrador](https://github.com/Rnz5/Inkr8-ISW2/pull/2)
y [rama publicada](https://github.com/Rnz5/Inkr8-ISW2/tree/codex/refactorizacion-por-bloques).
8 Android y19 cierre de acceso PASS; regresiones finales39/6/10/14/16/2 PASS.
289 contrastes estáticos separados. Copia limpia revisada de6d569 conserva121
artefactos previos a este registro, fuente/métodos y baseline; manifiesto final
incorpora resultados de esa revisión. Informe23 páginas, seis UML y APK demo
actualizados. Código/tests/revisión: Codex; publisher autorizado MACOABC.
Master conserva7d879c2; sin merge/deploy/datos reales. El ZIP identifica commit y
verifica todos los bytes de entrega. Límites externos/académicos en este documento.

## ACCOUNT-ACCESS-001 — decisión posterior aplicada

Marco respondió «aplica lo recomendable» a la consulta agrupada de FIN-002.
Se adopta el cierre de acceso Auth con conservación de perfil, nombre, historial
y registros económicos. Código/pruebas/revisión: Codex; no aprobación grupal o
fuentes externas inferidas. La denegación temporal anterior es antecedente.

`UserRepository.deleteAccount` conserva firma/callbacks y llama `closeAccount`.
Settings informa cierre de acceso y retención de datos. La función marca primero
`accountClosed/accountClosedAt` (campos nuevos sólo Functions), sin tocar saldos,
rating, sesión, reservas, username o historial. Después deshabilita Auth y revoca
refresh tokens. Rules/callables bloquean tokens previos; fallo parcial mantiene
acceso cerrado y permite completar el cierre mediante reintento autenticado.
Sin reactivación automática ni nueva política de borrado. Procesamiento Admin de
envíos pendientes y fórmulas económicas anteriores permanecen.

**8 métodos Android PASS**, incluido Settings→VM→repositorio→callable→Auth/signout;
**19 comprobaciones de cierre PASS** REST/Auth/SDK/HTTP: permiso, preservación,
reapertura denegada,8 concurrentes, tokens antiguos y refresh, acceso ajeno,
fallo parcial explícito de entrega Auth y reintento SDK real. Sonda previa sin
callable disponible (preparación, no aceptación funcional);
16/1 posterior detectó conversión indebida de403 a400 en manejador económico:
se conserva HttpsError, sin cambios de costes/fórmulas. Aserciones intactas.
Regresiones afectadas:39 permisos,6 nombre,10 retirada/economía,14 operación,
16 borrado y2 higiene de log PASS. Auditor289 contrastes estáticos PASS; mutación
de nueva fuente detectada y oráculos anteriores intactos. No se suman repeticiones.

No se aportaron reglas originales, credenciales/ID de prueba R8/Google ni pruebas
individuales/actas/P5. Esos límites siguen; Auth/IAM cloud y proveedor externo no
se validan con emuladores. La nueva política local no implica despliegue o datos
reales ni autoría humana del código.

Registro 09/10/2026 America/Lima. Implementación, pruebas, revisión, UML e informe:
**Codex**, bajo DEC-AI-AUTH-001 comunicado por Marco. El prompt actual autoriza
commits/push en `codex/refactorizacion-por-bloques` y PR a `master`, sin merge o
deploy. Los commits consolidan snapshots reales, no una historia estudiantil ficticia.

**Conclusión:** núcleo A01–A07 y funciones aprobadas implementadas y verificadas
en el laboratorio autorizado. FIN-002 resuelve las dos sondas Android pendientes y protege el borrado estacional.
El proyecto queda preparado para revisión; no se acredita operación cloud,
seguridad integral, proveedor externo ni cumplimiento individual del curso.

| Bloque | Implementado y motivo | Comprobado | Pendiente o límite |
|---|---|---|---|
| A01 | Regex reutilizadas, algoritmo/mensajes idénticos | 38 casos JVM históricos y oráculo conservado; build auténtico | Sin medición de rendimiento |
| A02 | Conteo/tokenización/reconocimiento conservados | 20 casos históricos, límites 49/50/150/151 y Compose en CORE | No todas las variantes visuales |
| A03 | Cinco componentes Writing; motivo de cambio de presentación distinto al envío | Cuerpos conservados, consumidores compilados y flujos UI | Sin cobertura visual de cada dispositivo |
| A04 | Score/feedback fuera de coordinación Results | Presentación, rating/Merit, refresco del mismo ID; flujos FIN | R8 externo no validado |
| A05 | Admisión literal, revisión monótona/contexto estable, confirmación exacta y bloqueo pendiente | Pendiente/éxito/rechazo, B y A→B→A, callback repetido, cuenta/ejercicio/reentrada; rechazo real bajo reglas estrictas FIN | Muerte de proceso en vuelo no ensayada; históricos sin propietario retenidos |
| A06 | UUID confirmado, misma identidad en listener/sondeo/reintento, nueva generación | B y generación anterior ignorados, 93 s, error/demora/reintento/tardío y Results mismo ID | SDK concreto permanece; no todos los fallos cloud |
| A07 | Cálculo literal y matching separados; evaluación/pareja/ghost atómicos | Ranked11, ghost2, lifecycle1 SEA; 14 operación FIN y flujos Ranked UI | No una sola llamada externa garantizada ni cron/cloud SLA |

## Criterios de FIN-002

| Comprobación | Resultado y evidencia |
|---|---|
| Historial propio con admin-read denegado | PASS Android; callable real HTTP/Auth, fila 1997-01 y rating 14; lectura SDK directa rechazada |
| Texto/borrador ante permiso denegado | PASS Android; texto válido, guardado rechazado, mismo texto/borrador y ninguna submission |
| Practice y Ranked Standard/On-Topic | 4 PASS Android en misma suite final, pipeline SDK→Firestore→Functions→Results; R8 HTTP doble fijo80 |
| Reglas propuestas y propietarios | FIN-001 39 PASS REST/Auth; FIN-002 6 PASS registro atómico de nombre/collision/rollback, economía intacta |
| Temporadas y operación | FIN-002 14 PASS: UTC/leap/year, tardíos, cierre8 concurrente/repetido, volumen1201/1203 y initializer8 concurrente |
| Token de compra sensible | Roja1 PASS/1 FAIL → verde2 PASS; se retiró sólo el log de token. Placeholder de verificación sigue como defecto previo externo |
| Auditoría separada | 289 contrastes estáticos PASS; oráculos previos preservados y deltas explícitos; no equivalen a casos funcionales |
| Compilación | Android auténtico assembleDebug/testDebugUnitTest, Functions y lab APK/testAPK PASS sobre cierre Auth final; 19 métodos unitarios PASS |
| Cierre de acceso aprobado | 19 REST/Auth/SDK/HTTP PASS y consumidor Android Settings→AppRoot→signout PASS; historia/economía retenidas, ocho cierres concurrentes y fallo parcial explícito |

### Causas corregidas y revisión

Historial esperaba encabezado/ellipsis antes de completar el callable; se espera
la fila de respuesta. Rechazo pulsaba durante carga y su fixture tenía menos de
50 palabras, por lo que el botón correcto era Incomplete. Se prepara texto dentro
del límite original y se espera editor. Se conserva cada aserción de aceptación;
no se cambió producción para ocultar esas rojas. El error inicial `hasText(exact=)`
era API de test incorrecta, corregida a `substring=false`, sin regla nueva.
El fixture de volumen usa ahora150 entradas×3=450 escrituras por batch.

FIN-001 ya corrigió initializer que sobrescribía progreso en entrega repetida,
con TX exists/create y mismos defaults. FIN-002 retiró la exposición de token
en el log de Philosopher, con efecto/economía caracterizados antes/después.
No se implementó ni simuló una verificación Google auténtica.

## Seguridad y dependencias indispensables

- `security/firestore.proposed.rules` es **NUEVO**, no original ni desplegado.
  El ZIP/Drive/repositorio comprobados no aportaron rules originales. Firebase
  original permanece; sólo la copia demo usa la propuesta. Colecciones estacionales
  y dinero/rating/placement sólo Admin/Functions; owner/cross-user contrastados.
- Philosopher: `verifyPurchaseWithGoogle` sigue aceptando token/product no vacíos
  sin Google API. Defecto previo confirmado, incompatible con aceptación cloud
  de compras. No se cambia economía por suposición. Credenciales y servicio de
  prueba auténticos necesarios si se mantiene esa integración.
- Antiabuso de metadata y exclusividad de submission por sesión requieren autoridad
  servidor adicional: las reglas propuestas no acreditan esos controles. El bloqueo
  UI/VM de segundo envío no es una reserva cloud antimaliciosa.
- ACCOUNT-ACCESS-001 resuelve el cierre de acceso: callable con marcador servidor,
  disable/revoke Auth y conservación de datos. 19 casos y consumidor Android PASS.
  Pendiente verificación de permisos IAM/operación Auth en entorno cloud autorizado.
  El delete directo histórico sigue denegado; no es una retirada de esa función.
- R8 externo: no credencial/configuración de prueba autorizada disponible. Se usa
  evaluador recuperado + SDK real + HTTP double80; no proveedor externo. OAuth,
  Ads, billing y Cloud Scheduler no validados. Activación estacional cloud requiere
  `SEASON_ACTIVATED_AT_MS` nuevo explícito y revisión de rules/índices; no deploy.
- Curso: evidencia individual de pruebas/reflexión/IA, pares R1/R2 y actas/métricas
  reales pendientes; modelos/informe del agente no acreditan revisión humana.

## Entregables y reproducción

[Índice de entrega y comandos](../delivery/README.md), informe P4 DOCX/PDF,
seis vistas UML `.puml/.svg/.png`, mapa de mensajes→métodos→líneas/SHA,
APK `com.inkr8.lab` y AndroidTest, evidencia seleccionada con hashes y ZIP final.
Fuentes/configuración/lockfile auténticos incluidos; secretos, originales privados,
cachés/dependencias/builds y exports excluidos. El APK abre el host demo **sin
instrumentación**; emuladores + ADB reverse necesarios. Las sondas automáticas
sí requieren AndroidTest. No instalarlo como cliente Firebase real.

El informe usa los13 capítulos P4 y visión/ODS de I2; HU3.27/3.28 contrastadas
con backlog vigente, mismas celdas E109/E111/E113/E115. No se inventaron sprints,
aprobación grupal, contribuciones o porcentajes IA. Las obligaciones individuales
se describen en capítulos2/3/4/10/11/13 y la matriz académica.

Las evidencias históricas y baseline permanecen; archivos protegidos se verifican
por hash antes de publicar. El auditor admite nuevos commits sólo como descendientes
del mismo baseline, sin reemplazar el oráculo. Los logs previos FAIL y controles
negativos se conservan. La revisión de Codex no es auditoría independiente del equipo.


### FIN-002 antecedente previo a ACCOUNT-ACCESS-001: protección de borrado

Suite final: **7 métodos Android PASS**, mismos seis objetivos de aceptación más
callback SDK de deleteSubmission. Android auténtico assembleDebug/testDebugUnitTest
y Functions compilan sobre el código final. Evidencias anteriores intactas.
`deletion-before.json`: la propuesta inicial aceptaba REST DELETE de Ranked sin
acuse estacional (200 frente a403 requerido). Corrección de soporte: callable
`functions/src/submissions/deleteSubmission.ts` comprueba owner/estado y createTime
del servidor, ACK y liquidación en una transacción; repositorio Android conserva
callbacks. Rules deniega el acceso directo Ranked. **16 casos PASS** REST/SDK/HTTP:
pendiente, sin ACK, sin liquidar, ajeno/anónimo, histórico preactivación, repetición,
8 concurrentes, Practice y efectos/asignación históricos intactos. Ninguna nueva
ganancia, coste o backfill. Un404 inicial de callable precedió a su recarga en el
emulador y se conserva como sincronización de soporte, no defecto productivo.
Auditor final: **287 contrastes estáticos PASS**, deltas exactos y oráculos anteriores
conservados;3 mutaciones aisladas detectadas. No son287 pruebas funcionales.
En esta comprobación anterior a la respuesta humana, Settings recibía denegación
y el cierre seguro estaba pendiente. ACCOUNT-ACCESS-001, descrito al inicio,
resuelve posteriormente esa política y conexión local; conserva la evidencia roja.


## Antecedente de publicación FIN-002 previo a cierre de acceso

**Publicación revisable FIN-002:** [PR #2 en borrador](https://github.com/Rnz5/Inkr8-ISW2/pull/2)
hacia master; [rama](https://github.com/Rnz5/Inkr8-ISW2/tree/codex/refactorizacion-por-bloques).
Código/pruebas/UML/informe: Codex; publicación con cuenta autorizada MACOABC.
7 Android,39 permisos,6 nombre,14 operación,16 borrado y2 log PASS;287 contrastes
estáticos separados. Copia limpia con autocrlf=true conserva recursos auténticos y
89 artefactos. Se corrigió versionado de bytes, sin modificar oráculos anteriores.
Sin merge/deploy/datos reales; dependencias externas/académicas en FINAL_DELIVERY.

Checkout previo y bundle privados conservados. Fuentes/propuesta/entrega agrupadas
en commits reales de Codex; no sprints o revisión estudiantil inventados. Dos fallos
de copia limpia por CRLF/LF quedaron rojos y se corrigieron con atributos y bytes
auténticos, sin redefinir hashes/aserciones. GitHub informa PR abierto en borrador,
base master, sin conflicto en la comprobación previa a este registro.
