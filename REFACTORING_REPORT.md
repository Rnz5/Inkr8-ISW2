# Informe de refactorización de Inkr8

Fecha: 9 de octubre de 2026, America/Lima.

## Resultado implementado

Se reconstruyó la organización del código por capas. Kotlin/Compose y TypeScript se conservan. Domain Android es un módulo Kotlin JVM separado, con entidades, políticas, interfaces y casos de uso sin dependencias Android/Firebase. Data implementa fuentes, DTOs, mappers y repositorios; Presentation utiliza ViewModels por responsabilidad, StateFlow y Compose. Composition roots realizan la inyección por constructor. Backend dispone de las mismas capas y un reloj inyectado para verificar fronteras de temporada dentro de transacciones.

Los únicos destinos de producto son Login, Home, Practice, Ranked y Profile. Se incluyen palabra del día persistida por fecha UTC, escritura estándar y por tema, evaluación R8, resultados, clasificación de temporada, ligas numéricas 1–6, edición de nombre, cierre de sesión y restauración de borradores/partidas. Escritura, espera y resultados permanecen dentro de los dos modos de juego.

Se eliminaron las fuentes anteriores y los sistemas de torneos, reputación, estadísticas, publicidad y economía compleja, junto con dependencias y recursos sin uso. Las reglas Firestore deniegan escrituras cliente y lecturas de perfiles/partidas ajenas. Las pruebas verifican también cuentas cerradas.

## Reinicio y economía

Temporadas de 14 días UTC, ancla `2026-10-12T00:00:00Z`; el período anterior al ancla también está definido. El scheduler comprueba el período cada cinco minutos y procesa páginas de 100, hasta 500 usuarios por ejecución, guardando cursor para reanudar. La actualización de todos los documentos converge gradualmente; la normalización transaccional de usuarios y partidas evita depender de que el barrido haya finalizado.

El reinicio conserva Merit y archiva el rating previo. Saltarse varias temporadas también normaliza a cero. Repetir el trabajo no borra puntos ya ganados en el período nuevo. Los usuarios antiguos sin marcador de temporada se normalizan a cero al activar el nuevo sistema.

Saldo inicial 1000, entrada Ranked fija de 100, recompensa básica al finalizar. IDs persistidos permiten reintentar sin repetir el débito. Recompensa y evaluación se confirman atómicamente y una sola vez. Emparejamientos concurrentes confirman ambos resultados juntos. Partidas tardías conservan recompensa pero no alteran la temporada nueva; pendientes antiguas expiran. La referencia competitiva tras 48 horas sin rival es 65 puntos.

## Evidencia local

| Verificación | Resultado |
|---|---|
| TypeScript `npm --prefix functions test` con Node 20 | 8 pruebas aprobadas; compilación strict |
| Firestore real en emulador, `test:integration` | 9 pruebas aprobadas |
| `npm --prefix functions run lint` | TypeScript sin errores, sin variables/parámetros sin uso |
| `:domain:test` | 3 pruebas Kotlin aprobadas |
| `:app:testDebugUnitTest` | 4 pruebas de ViewModel aprobadas |
| `scripts/verify-architecture.ps1` | Fronteras de capas y ausencia de sistemas excluidos: PASS |
| Carga local de exports del backend | Los 9 endpoints/triggers nuevos cargan correctamente |
| `git diff --check` | Sin errores de whitespace |
| `:app:assembleDebug` y `:app:assembleRelease` | BUILD SUCCESSFUL; APK debug y release sin firma de distribución generados |
| `:app:lintDebug` y lint vital release | Aprobados; reporte debug: `No issues found.` |

Las pruebas Firestore comprueban débito concurrente único, saldo insuficiente, envío inmutable, recompensa concurrente única, reintento sin costo, evaluación tardía, emparejamiento simultáneo, reinicio de 512 perfiles en varias ejecuciones, conservación de puntuación nueva/Merit, palabra del día estable, compatibilidad de temas y denegación de escrituras económicas/lecturas no autorizadas.

Las pruebas Android comprueban ID estable tras pérdida de conexión, doble envío bloqueado, restauración tras recreación, aislamiento por usuario, recuperación de texto al abrir otra vez una partida y cancelación de observadores. No se conectaron cuentas reales ni se consumió la API externa de evaluación.

Total: 24 pruebas aprobadas, ninguna fallida ni omitida. Última verificación Gradle final: `BUILD SUCCESSFUL in 23s`. APK debug: 16 832 318 bytes; APK release sin firma de distribución: 12 012 826 bytes. El emulador Firestore creado para las pruebas se detuvo después de la validación.

## Configuración y límites de entrega

La copia recibida carecía de configuración raíz Gradle, wrappers ejecutables y `app/google-services.json`; se restauró la configuración de build. La configuración Firebase real debe proporcionarse desde el proyecto del propietario. El APK sin configuración muestra un mensaje de servicio sin configurar y no inventa credenciales ni simula login/evaluaciones.

Queda pendiente fuera de la validación local: registrar/habilitar OAuth Google y certificados, colocar `google-services.json`, configurar `OPENAI_API_KEY`, desplegar funciones/reglas/índices y verificar scheduler y evaluación real en un entorno de prueba. No se modificó ningún dato ni función remoto. Los registros antiguos se mantienen fuera del cliente nuevo; el esquema nuevo es `schemaVersion: 2`. La transición debe retirar los triggers/schedulers anteriores para que no continúen ejecutando lógica eliminada. El worker nuevo se exporta como `gameEvaluationWorker` para no cambiar el tipo de trigger de una función existente durante despliegue.

Las versiones de dependencias están fijadas y centralizadas. Lint eleva advertencias de código a errores; únicamente omite avisos de disponibilidad de versiones nuevas (`GradleDependency`, `AndroidGradlePluginVersion`, `NewerVersionAvailable`), que se revisan en actualizaciones separadas. No se añadió una baseline para ocultar defectos.

Artefactos iniciales: `constitution.md`, `slices.md`. Carpeta externa `../refactoring_prompts`: exclusivamente los prompts del proceso. `README.md` detalla estructura, API, decisiones de producto, comandos reproducibles y transición.

## Corrección de observaciones de arquitectura y SOLID

Seguimiento del 9 de octubre de 2026. Se crearon los cuatro casos de uso de borradores en el módulo JVM y se eliminó la dependencia directa de GameViewModel hacia DraftRepository. El contador y la habilitación de envío se calculan al actualizar el estado del ViewModel, incluyendo restauración, apertura y cambios recibidos del servidor. GameScreen consume wordCount y canSubmit; conserva únicamente la lectura de la constante informativa de costo Ranked.

AppGraph acepta repositorios alternativos mediante sus interfaces y crea las fuentes SDK de forma diferida. La configuración predeterminada AppGraph(context) permanece disponible. FirebaseMatchRepository delega en Matchmaker, MatchBaseline y MatchRecovery sin cambiar las firmas MatchRepository ni el constructor público. Se conservaron las transacciones de emparejamiento y rating; la condición de 48 horas se comprueba dentro de la transacción de resolución contra R8.

El verificador de arquitectura rechaza ahora también el uso de DraftRepository en Presentation y las llamadas de validación/conteo de GamePolicy en Compose. No se modificaron dependencias Gradle/package.json ni servicios remotos.

| Verificación de seguimiento | Resultado |
|---|---|
| scripts/verify-architecture.ps1 | Architecture boundaries and excluded systems: PASS |
| :domain:test --no-daemon | 3 pruebas aprobadas |
| :app:testDebugUnitTest --no-daemon | 6 pruebas aprobadas |
| :app:lintDebug --no-daemon | No issues found. |
| :app:assembleDebug --no-daemon | BUILD SUCCESSFUL; APK debug actualizado |
| npm --prefix functions run lint, con Node 20.20.2 | tsc --noEmit aprobado |
| npm --prefix functions test, con Node 20.20.2 | 8 pruebas aprobadas |
| npm --prefix functions run test:integration, emulador local demo-inkr8 | 11 pruebas aprobadas |
| git diff --check | Sin errores de whitespace |

Total actual: 28 pruebas aprobadas, ninguna fallida ni omitida. Los cuatro targets Gradle se ejecutaron juntos con --no-daemon; BUILD SUCCESSFUL in 2m 20s. Las nuevas pruebas cubren límites de escritura, restauración del contador/validación, cambios de modalidad/estado del servidor, resolución exactamente a las 48 horas con reintentos concurrentes y caducidad/recuperación sin cruzar temporadas. El SDK local mostró el aviso de versiones de metadatos XML; no impidió compilación, pruebas ni lint. La validación usó únicamente Firestore local; se detuvo el emulador al finalizar. Prompt de seguimiento registrado en ../refactoring_prompts/007-correccion-observaciones-solid.txt.
