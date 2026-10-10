# Inkr8

Aplicación Android Kotlin/Compose con backend TypeScript/Firebase Functions. Login con Google, Home con palabra del día, Practice, Ranked y Profile son los únicos destinos. Escritura, espera y resultado están integrados en cada modo de juego.

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

## Reglas de producto

- Estándar: cuatro palabras requeridas, texto de 50–150 palabras. Sobre un tema: dos palabras y texto de 50–200 palabras. Ambos disponibles en Practice y Ranked; el servidor selecciona el desafío.
- Saldo inicial: 1000 Merit. Practice no cobra entrada; Ranked descuenta 100 al empezar, incluyendo partidas abandonadas. Los reintentos usan el mismo ID y no repiten el cobro.
- Merit al finalizar: recompensa básica por calidad y extensión; Ranked conserva el multiplicador 1.5. No hay otros gastos, retenciones, impuestos, membresías, transferencias o bonificaciones por comportamiento.
- Ligas 1–6: umbrales de rating 0, 30, 60, 90, 120 y 150. El resultado Ranked se decide por puntuación frente a un rival cercano en rating. A las 48 horas sin rival se utiliza una referencia R8 de 65 puntos, identificada expresamente en el resultado.
- Temporadas de exactamente 14 días UTC, ancladas en `2026-10-12T00:00:00Z` (11 de octubre, 19:00 en Lima). La fórmula también define los períodos anteriores al ancla.
- `leagueReset` comprueba el período cada cinco minutos. Procesa hasta 500 usuarios por ejecución, guarda cursor y reanuda páginas pendientes; la propagación física a todos los documentos es gradual. Los accesos y partidas normalizan el usuario dentro de transacciones sin esperar al barrido global.
- Cada nueva temporada comienza en rating 0 y liga 1; se conserva Merit. El reinicio nunca sobrescribe puntos de usuarios ya normalizados en la temporada nueva. Las evaluaciones tardías conservan la recompensa y no afectan el rating nuevo. Partidas sin emparejamiento al cierre pasan a `EXPIRED`.
- R8 usa evaluación real. Una falla deja la partida en `FAILED` con reintento explícito; no se inventan puntuaciones. Cobro, recompensa y resultado se protegen con transacciones idempotentes por ID.

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
3. Tener al menos cuatro documentos activos en `words` con `word`, `definition`, `sentence`, `isActive: true`. Para escritura por tema: documentos `topics` con `name` y `themeId`, y `themes` con `name`; los temas antiguos sin `isActive` siguen siendo válidos.
4. Publicar reglas, índices y funciones del proyecto correcto, luego recompilar Android con su configuración. El reinicio remoto requiere el despliegue de `leagueReset` y su Cloud Scheduler.

Configuración de Google basada en la [documentación oficial Firebase/Credential Manager](https://firebase.google.com/docs/auth/android/google-signin). La compatibilidad Gradle/Kotlin sigue la [documentación de AGP 9](https://developer.android.com/build/releases/agp-9-0-0-release-notes).

## Contrato público del backend

Todos los callables requieren autenticación y rechazan cuentas cerradas. Región: `us-central1`. La identidad, costo, rating y temporada se determinan en servidor.

| Endpoint | Entrada | Salida / efecto |
|---|---|---|
| `initializeUser` | `{}` | Perfil; inicializa saldo o normaliza temporada |
| `updateProfile` | `{name}` | Actualiza nombre de 2–20 caracteres visibles |
| `getHome` | `{}` | Palabra estable del día UTC, temporada y costo Ranked |
| `getSeasonRanking` | `{}` | Temporada vigente y primeros 50 participantes |
| `startGame` | `{id, mode, writingMode}` | Partida DRAFT; cobra Ranked una sola vez |
| `submitGame` | `{id, content}` | Valida y pasa la partida a PENDING |
| `retryEvaluation` | `{id}` | Reencola FAILED sin nuevo cobro |
| `gameEvaluationWorker` | Evento Firestore actualizado | Evalúa PENDING, confirma Merit y busca rival |
| `leagueReset` | Scheduler cada 5 min UTC | Reinicia usuarios por páginas y recupera emparejamientos |

`mode`: `PRACTICE` o `RANKED`; `writingMode`: `STANDARD` o `ON_TOPIC`. El ID de partida es un UUID generado una vez por cliente y persistido antes del primer intento. Los documentos nuevos llevan `schemaVersion: 2`. Android consulta sólo esas partidas; no interpreta registros antiguos con el esquema retirado.

Usuarios y partidas permiten lectura sólo por propietario; todos los writes cliente están denegados. Rankings y palabras se sirven por funciones autenticadas. Los borradores locales se aíslan por usuario y partida, y se excluyen de respaldo/transferencia.

## Pruebas de integración y transición

```powershell
# Emulador local, proyecto ficticio; no usar la base de producción:
firebase emulators:exec --project demo-inkr8 --only firestore "npm --prefix functions run test:integration"
```

También se puede iniciar Firestore por separado, definir `FIRESTORE_EMULATOR_HOST=127.0.0.1:8080` y ejecutar el script de integración. La suite exige esa variable para evitar ejecutar contra producción. Las pruebas usan repositorios reales y reglas reales; reemplazan únicamente el evaluador externo por resultados controlados.

El cambio de contrato requiere publicar el cliente nuevo junto al backend y retirar las funciones anteriores. `gameEvaluationWorker` tiene un nombre nuevo porque el trigger ahora es actualización, mientras el worker antiguo escuchaba creación. No conservar en producción triggers o schedulers anteriores de Merit, temporadas, estadísticas o evaluaciones: podrían seguir modificando documentos. Revisar la lista de funciones del proyecto antes del despliegue y retirar las que dejaron de exportarse. No se ha efectuado despliegue ni migración remota durante este trabajo.

Los perfiles antiguos sin `seasonIndex` se normalizan a rating 0 al activar este sistema y conservan Merit. Campos y colecciones remotos retirados no se consultan ni se eliminan automáticamente. Las partidas antiguas quedan retenidas fuera del cliente nuevo. Comprobar OAuth, evaluación real y scheduler desplegado en un entorno de prueba antes de publicar.

Los artefactos iniciales son `constitution.md` y `slices.md`. El registro de prompts está fuera del proyecto en `../refactoring_prompts`; esa carpeta contiene exclusivamente prompts. Evidencia de validación en `REFACTORING_REPORT.md`.
