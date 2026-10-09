# Fuentes originales necesarias para ejecutar Inkr8

**AND-001 — ejecución Android y comprobación ampliada, 05/10/2026:** SDK oficial API 36/Build Tools 36.0.0 instalado tras aceptación humana «claro». Android compila con Gradle/AGP/versiones recibidas intactas, APK debug firmado generado. 16 métodos pertinentes del módulo real + 1 ejemplo PASS; incluyen dos casos de snapshots Compose, no render/recomposición de pantallas. Cuatro escenarios del motor Ranked en Firestore local reales antes/después dan resultados idénticos; R8 doble y CloudEvent sintético. Auth local REST operativo, sin Google OAuth. Emulador Android sin controlador de aceleración, intento por software permanece offline: cero pantallas ejecutadas. Auditor 158 contrastes estáticos, expectativas anteriores conservadas. Sin cambio de producción, reglas, integración, retiradas o temporadas. [Resultados, reproducción y bloqueo](ANDROID_EXECUTION.md).


**Alcance posterior SCP-001 — registro 05/10/2026:** Marco comunica una nueva captura/mensaje de Renzo: «Ranked y Merit permanecen»; «Torneos, ligas y reputación se retiran». La imagen original y su fecha no están adjuntas al contexto disponible; no se infiere aprobación grupal ni fecha de subida. Esta instrucción humana posterior sustituye la retirada total de Merit de U3/DEC-03 para el trabajo vigente. Se conservan Ranked/rating/Merit y las reglas económicas existentes; no se inventan usos, precios o recompensas nuevos. Temporadas permanece incluida con reglas pendientes. RET-001–005 y sus evidencias describen el alcance anterior y no se reescriben. [Recuperación y restitución](RECOVERED_ENVIRONMENT.md).

**REC-002 recibido:** Gradle raíz/app/catalog/wrapper, dependencias y tsconfig Functions, Firebase project/Google Services local y evaluateWithR8 con SDK OpenAI. 21 archivos faltantes añadidos; 106 fuentes compartidas iguales a HEAD; wrapper properties preexistente idéntico. No se importan node_modules, .git, cachés, builds ni local.properties de Renzo. Siguen ausentes las reglas Firestore auténticas, fixtures/configuración de prueba si existen y el procedimiento local de provisión R8; Storage sólo si el equipo confirma su uso (no se encontró consumidor en este recorrido). SDK API 36 no instalado; herramientas oficiales preparadas, aceptación de licencia humana pendiente. La tabla inferior conserva la solicitud histórica anterior al paquete.


05/10/2026. Solicitud agrupada para el equipo. Checkout de destino:
`C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/checkout/Inkr8-ISW2`.
Fuente de correspondencia: HEAD `7d879c22030527e1d5f5003ad84b76b750d3b29e`, código
previo C1 `64983846d6dbf4cdfafcdd508464cd140a2a862e`, más cambios locales registrados.
Entregar el proyecto original de una máquina del equipo, con commit/fecha y
versiones realmente usadas. Si es otra versión, indicar sus diferencias antes
de importar configuración. No copiar configuración de otros proyectos.

| Capa / ubicación de destino | Archivos o fuente que falta | Correspondencia que debe comprobarse |
|---|---|---|
| Android, raíz y app/ | `settings.gradle` o `.kts`; `build.gradle` o `.kts`; `app/build.gradle` o `.kts`; `gradle.properties` y `gradle/libs.versions.toml` si el original los usa. | applicationId/namespace, plugins, Kotlin/Compose, SDK y dependencias reales contra este código y manifest. No adoptar versiones del arnés JVM. |
| Android, raíz y gradle/wrapper/ | `gradlew`, `gradlew.bat`, `gradle/wrapper/gradle-wrapper.jar` originales. | **properties de producto 9.1.0 ya existe**, con checksum; conservarlo. Scripts/jar deben corresponder al original, no al arnés 8.14.5. |
| Android/Firebase, app/ y configuración local | `app/google-services.json` o procedimiento original de provisión; configuración/plugin Google Services original; ubicación del SDK Android y paquetes/JDK usados. | Proyecto Firebase/applicationId y generación de `default_web_client_id`. No se localizó SDK en ruta por defecto ni variables Android configuradas; no se afirma ausencia en todas las máquinas. |
| Functions, functions/ | `package.json`, lock original (`package-lock.json`, yarn o pnpm según corresponda), `tsconfig.json` y cualquier configuración referenciada. | Versiones Node/SDK/TypeScript, scripts y target/module reales. El TypeScript 7.0.2 encontrado en caché sólo es herramienta de comprobación, no la versión del producto. |
| Firebase, raíz / rutas que indique el original | `firebase.json`, `.firebaserc` o información original de proyecto de prueba; reglas Firestore y Storage si se usa; configuración/fixtures de emuladores originales si existen. | Rutas/colecciones, permisos/auth y entorno de prueba; `firestore.indexes.json` ya existe y se conserva. Un listado de índices no sustituye reglas/configuración. |
| R8, functions/src/r8/ y dependencias locales | `evaluateWithR8.ts` auténtico, sus imports/assets/prompt/modelo/esquema y fuente del contrato; procedimiento original para configurar `OPENAI_API_KEY`. | Firma/exportaciones reales que consume el motor, salida válida, fallback y errores frente a requisitos recuperados. No crear un evaluador sustituto para hacer pasar la compilación. |

No enviar claves de servicio, contraseñas, tokens ni valor de OPENAI_API_KEY en el
chat o en Git. Entregar configuración sensible por el medio privado del equipo
y documentar su provisión local. No hace falta imprimirla para comprobar versiones.

**Búsqueda ya realizada:** árboles e historial de rutas Android, ISW2/Content,
Website (configuración React/Vite descartada), carpetas y archivos Drive/Downloads
registrados en IMPL-005 y SOLID-PREPARATION. VER-001 añadió refs ISW2, releases/forks,
búsqueda evaluateWithR8 bajo Rnz5 y tres archivos ZIP anidados del material fuente.
No hay releases/forks ni resultados del evaluador; la otra rama ISW2 es documental,
sin configuración original de producto. ZIPs de material docente sin candidatos;
no se importan ejemplos. No se encontraron objetos Git inaccesibles en el checkout.
Cero archivos originales recuperados en esta etapa; no se vuelven a pedir recursos
Android restaurados por IMPL-006 ni criterios H1 ya disponibles.

**Vigente AND-002 — 06/10/2026:** WHPX/AVD operativos y pantallas ejecutadas en
demo-inkr8-local. 14 métodos Android únicos: 13 PASS y 1 FAIL real (isPlaced no
llega al modelo); tres contrastes centrales antes/después PASS; cinco escenarios
transaccionales iguales y entrega automática local/callable PASS, R8 HTTP doble.
No código productivo nuevo; soporte/fixtures corregidos. Borrador anticipado,
resolución por B, idempotencia/carrera y payload gamemode siguen como defectos o
contratos pendientes. Ranked/rating/Merit permanecen. [Resultados, APK, límites y
aportes concretos](ANDROID_EXECUTION.md). No cierre integral ni autoría estudiantil.
