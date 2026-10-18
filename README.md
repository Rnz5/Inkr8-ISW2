# Inkr8

Aplicación Android Kotlin/Compose con backend TypeScript/Firebase Functions. Login con Google, Home con palabra del día, Practice, Ranked y Profile son los únicos destinos. Escritura, espera y resultado están integrados en cada modo de juego. El perfil incluye username único y permanente, rendimiento competitivo, historial y temporadas anteriores.

## Arquitectura

```text
domain/                         Módulo Kotlin JVM sin Android/Firebase
  src/main/kotlin/com/inkr8/domain/
    model/ policy/ repository/ usecase/
app/src/main/java/com/inkr8/
  data/                         DTOs, mappers, fuentes y repositorios
    dto/ mapper/ source/ repository/
  presentation/                 Compose, StateFlow y ViewModels por responsabilidad
    state/ ui/ viewmodel/
  di/                           Composition root e inyección por constructor
  MainActivity.kt / Inkr8App.kt  Entradas Android
functions/src/
  domain/                       Entidades, puertos, reglas y casos de uso TypeScript
    model/ policy/ repository/ usecase/
  data/                         Firestore y evaluador OpenAI
    dto/ mapper/ source/ repository/
  presentation/                 Callables, eventos y scheduler
    handlers/ scheduler/
  index.ts                      Composition root y exports públicos
```

Las reglas están en Domain, los adaptadores en Data y el estado/UI en Presentation. Android importa el dominio JVM; Presentation no importa Firebase ni implementaciones Data. El backend sigue la misma dirección de dependencias. `scripts/verify-architecture.ps1` verifica estas fronteras y la ausencia de sistemas retirados.

Los borradores pasan por `LoadDraft`, `SaveDraft`, `ClearDraft` y `FindDraft`. `GameViewModel` publica `wordCount` y `canSubmit` en `GameUiState`, recalculándolos al editar, restaurar, abrir o recibir una partida; Compose sólo los consume. `AppGraph(context)` mantiene la configuración predeterminada y permite sustituir los seis repositorios mediante parámetros opcionales de sus interfaces. Las fuentes Firebase y Google se crean de forma diferida cuando se requieren.

En el backend, `FirebaseMatchRepository` conserva el contrato público como fachada. `Matchmaker` selecciona y empareja jugadores, `MatchBaseline` resuelve contra R8 al cumplirse 48 horas y `MatchRecovery` caduca partidas anteriores y reintenta las pendientes. La confirmación de rating y resultados sigue siendo transaccional e idempotente.

Las políticas puras `matchingPolicy`, `userPolicy` y `WORDS_PER_MODE` centralizan los límites solicitados de emparejamiento, referencia R8, nombres y palabras por modalidad. Los handlers de juego y contenido ejecutan casos de uso; mantienen la autenticación, las entradas y las respuestas públicas. `SeasonRepository` conserva sus métodos y extiende `SeasonReader` y `SeasonResetter`; `ResetLeagues` requiere sólo la capacidad de reinicio.

La persistencia del backend se construye mediante `PersistenceProvider`: su método de creación delega en la implementación seleccionada y devuelve los cinco repositorios por sus contratos. `FirestoreProvider` conserva Firestore como único proveedor disponible; `DATABASE_PROVIDER` permite seleccionarlo con el valor `firestore`, que también es el predeterminado si la variable no existe. Un proveedor desconocido detiene la inicialización. Para incorporar otra base de datos se añade su proveedor en la carpeta Data/Source existente y se registra en el punto de composición, sin cambiar los casos de uso ni los handlers.

`submissionIdentity` es una única instancia inmutable por proceso, inyectada en los handlers de inicio, envío y reintento. Valida y conserva el ID recibido de Android; no genera otro ID ni guarda estado de partidas. La unicidad entre instancias y la idempotencia siguen protegidas por los documentos y transacciones de Firestore. No se añade identificación ni historial de movimientos de Merit.

`MainActivity` obtiene el token mediante `GoogleTokenProvider`, una interfaz de Presentation que recibe el Context de la Activity. `AppGraph` permite inyectar ese proveedor y conecta su implementación predeterminada con el Data Source privado de Google. El adaptador está en el composition root para que Data no importe Presentation y el módulo Domain siga sin dependencias Android.

## Reglas de producto

- Estándar: cuatro palabras requeridas, texto de 50–150 palabras. Sobre un tema: dos palabras y texto de 50–200 palabras. Ambos disponibles en Practice y Ranked; el servidor selecciona el desafío.
- Saldo inicial: 1000 Merit. Practice no cobra entrada; Ranked descuenta 100 al empezar, incluyendo partidas abandonadas. Los reintentos usan el mismo ID y no repiten el cobro.
- Merit al finalizar: recompensa básica por calidad y extensión; Ranked conserva el multiplicador 1.5. No hay otros gastos, retenciones, impuestos, membresías, transferencias o bonificaciones por comportamiento.
- Ligas 1–6: umbrales de rating 0, 30, 60, 90, 120 y 150. El resultado Ranked se decide por puntuación frente a un rival de la misma modalidad de escritura y rating cercano. A las 48 horas en cola sin rival se utiliza el promedio de las puntuaciones Ranked evaluadas del jugador, incluida la actual; se identifica expresamente en el resultado. Una primera partida sin historial se compara con su propia puntuación.
- Temporadas de exactamente 14 días UTC, ancladas en `2026-10-12T00:00:00Z` (11 de octubre, 19:00 en Lima). La fórmula también define los períodos anteriores al ancla.
- `leagueReset` comprueba el período cada cinco minutos. Procesa hasta 500 usuarios por ejecución, guarda cursor y reanuda páginas pendientes; la propagación física a todos los documentos es gradual. Los accesos y partidas normalizan el usuario dentro de transacciones sin esperar al barrido global.
- Cada nueva temporada comienza en rating 0 y liga 1; se conserva Merit. El reinicio nunca sobrescribe puntos de usuarios ya normalizados en la temporada nueva. Las evaluaciones tardías conservan la recompensa y no afectan el rating nuevo. Partidas sin emparejamiento al cierre pasan a `EXPIRED`.
- R8 usa evaluación real y devuelve comentarios de ortografía, vocabulario, coherencia y creatividad. El servidor comprueba palabras, extensión y restricciones avanzadas; la pertinencia temática la evalúa R8. Un fallo en el detalle conserva un puntaje base válido y muestra el error correspondiente.
- Las evaluaciones tienen un timeout de 90 segundos. Un timeout conserva el envío como `FAILED` con `nextRetryAt`; el scheduler lo reencola después de cinco minutos, sin nuevo cobro. También recupera envíos `PENDING` interrumpidos durante diez minutos. Otros fallos permiten reintento explícito. La aplicación avisa de resultados nuevos mientras mantiene el observador activo; no se incorporaron notificaciones push.
- Las estadísticas e historial competitivo se derivan de las partidas Ranked evaluadas, conservadas por cuenta. Las prácticas no los afectan. Las recompensas de temporada se acumulan transaccionalmente en su miembro. Los empates de rating comparten posición y se muestra la posición propia aunque esté fuera de los primeros 50.

## Configuración y compilación

Requisitos: JDK 21, Android SDK 36 con Build Tools 36.0.0, Node 20, Firebase CLI para emuladores/despliegue. Gradle 9.1.0 se descarga mediante wrapper; las versiones están centralizadas en el catálogo.

```powershell
# Desde la raíz, con JAVA_HOME y ANDROID_HOME configurados:
.\gradlew.bat :app:assembleDebug :app:assembleRelease :app:testDebugUnitTest :domain:test :app:lintDebug
npm --prefix functions ci
npm --prefix functions test
npm --prefix functions run lint
.\scripts\verify-architecture.ps1
```

En macOS/Linux: `sh ./gradlew` y los mismos targets. APK debug en `app/build/outputs/apk/debug/app-debug.apk`; release sin firma de distribución en `app/build/outputs/apk/release/app-release-unsigned.apk`.

La copia recibida no incluye `app/google-services.json`. Sin ese archivo compila y muestra un estado de servicio sin configurar; no puede autenticar usuarios reales. Para activar Firebase:

1. Registrar `com.inkr8` en el proyecto Firebase, habilitar Google y registrar SHA-1/SHA-256 del certificado utilizado. Descargar el archivo con OAuth web configurado y colocarlo en `app/google-services.json` (ignorado por Git).
2. Configurar el secreto `OPENAI_API_KEY` del backend en el proyecto Firebase. Ninguna clave debe entrar al repositorio.
3. Tener al menos cuatro documentos activos en `words` con `word`, `definition`, `sentence`, `phonetic`, `partOfSpeech`, `rarity`, `isActive: true`. La oración debe contener la palabra completa. Home no inventa metadatos: rechaza una palabra incompleta. La caché diaria anterior puede completarse desde su palabra activa conservando el texto original. El audio usa el motor TTS inglés del dispositivo. Para escritura por tema: documentos `topics` con `name` y `themeId`, y `themes` con `name`; los temas antiguos sin `isActive` siguen siendo válidos. On-Topic solicita primera persona y una pregunta, además del tópico, palabras y extensión.
4. Publicar reglas, índices y funciones del proyecto correcto, luego recompilar Android con su configuración. El reinicio remoto requiere el despliegue de `leagueReset` y su Cloud Scheduler.

Configuración de Google basada en la [documentación oficial Firebase/Credential Manager](https://firebase.google.com/docs/auth/android/google-signin). La compatibilidad Gradle/Kotlin sigue la [documentación de AGP 9](https://developer.android.com/build/releases/agp-9-0-0-release-notes).

## Contrato público del backend

Todos los callables requieren autenticación y rechazan cuentas cerradas. Región: `us-central1`. La identidad, costo, rating y temporada se determinan en servidor.

| Endpoint | Entrada | Salida / efecto |
|---|---|---|
| `initializeUser` | `{}` | Perfil; inicializa saldo o normaliza temporada |
| `updateProfile` | `{name}` | Reserva username permanente de 2–20 caracteres visibles, único sin distinguir mayúsculas y con normalización Unicode |
| `getHome` | `{}` | Palabra estable, `wordDay` UTC, temporada y costo Ranked |
| `getWordExample` | `{day}` | Ejemplo de la palabra del día indicado, solicitado al pulsar el botón |
| `getSeasonRanking` | `{}` | Temporada vigente, primeros 50 participantes y `own` con posición propia |
| `getProfileProgress` | `{}` | Historial Ranked evaluado en orden descendente y estadísticas acumuladas |
| `getSeasonHistory` | `{}` | Temporadas anteriores con posición final, rating y Merit obtenido |
| `startGame` | `{id, mode, writingMode}` | Partida DRAFT; cobra Ranked una sola vez |
| `submitGame` | `{id, content}` | Valida y pasa la partida a PENDING |
| `retryEvaluation` | `{id}` | Reencola FAILED sin nuevo cobro |
| `gameEvaluationWorker` | Evento Firestore actualizado | Evalúa PENDING, confirma Merit y busca rival |
| `leagueReset` | Scheduler cada 5 min UTC | Recupera evaluaciones diferidas, reinicia usuarios y recupera emparejamientos |

`mode`: `PRACTICE` o `RANKED`; `writingMode`: `STANDARD` o `ON_TOPIC`. El ID de partida es un UUID generado una vez por cliente y persistido antes del primer intento. Los documentos nuevos llevan `schemaVersion: 2`. Android consulta sólo esas partidas; no interpreta registros antiguos con el esquema retirado.

Usuarios y partidas permiten lectura sólo por propietario; todos los writes cliente están denegados. Rankings y palabras se sirven por funciones autenticadas. Los borradores locales se aíslan por usuario y partida, y se excluyen de respaldo/transferencia.

Las reservas de username sólo son accesibles por backend y se confirman en la misma transacción que el perfil. Cerrar sesión no borra progreso. Los usernames anteriores no se asignan automáticamente a partir del nombre de Google: se eligen en Perfil. Las recompensas antiguas de temporada que no se contabilizaron no se reconstruyen automáticamente. La ampliación de octubre está descrita por historia en `ACCEPTANCE_REPORT.md`.

## Pruebas de integración y transición

```powershell
# Emulador local, proyecto ficticio; no usar la base de producción:
firebase emulators:exec --project demo-inkr8 --only firestore "npm --prefix functions run test:integration"
```

También se puede iniciar Firestore por separado, definir `FIRESTORE_EMULATOR_HOST=127.0.0.1:8080` y ejecutar el script de integración. La suite exige esa variable para evitar ejecutar contra producción. Las pruebas usan repositorios reales y reglas reales; reemplazan únicamente el evaluador externo por resultados controlados.

El cambio de contrato requiere publicar el cliente nuevo junto al backend y retirar las funciones anteriores. `gameEvaluationWorker` tiene un nombre nuevo porque el trigger ahora es actualización, mientras el worker antiguo escuchaba creación. No conservar en producción triggers o schedulers anteriores de Merit, temporadas, estadísticas o evaluaciones: podrían seguir modificando documentos. Revisar la lista de funciones del proyecto antes del despliegue y retirar las que dejaron de exportarse. No se ha efectuado despliegue ni migración remota durante este trabajo.

Los perfiles antiguos sin `seasonIndex` se normalizan a rating 0 al activar este sistema y conservan Merit. Campos y colecciones remotos retirados no se consultan ni se eliminan automáticamente. Las partidas antiguas quedan retenidas fuera del cliente nuevo. Comprobar OAuth, evaluación real y scheduler desplegado en un entorno de prueba antes de publicar.

Los artefactos iniciales son `constitution.md` y `slices.md`. El registro de prompts está fuera del proyecto en `../refactoring_prompts`; esa carpeta contiene exclusivamente prompts. Evidencia de validación en `REFACTORING_REPORT.md`.
