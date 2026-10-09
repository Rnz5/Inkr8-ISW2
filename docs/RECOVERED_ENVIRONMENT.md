# Inkr8 — REC-002 / SCP-001, resultado comprobado

**AND-001 — ejecución Android y comprobación ampliada, 05/10/2026:** SDK oficial API 36/Build Tools 36.0.0 instalado tras aceptación humana «claro». Android compila con Gradle/AGP/versiones recibidas intactas, APK debug firmado generado. 16 métodos pertinentes del módulo real + 1 ejemplo PASS; incluyen dos casos de snapshots Compose, no render/recomposición de pantallas. Cuatro escenarios del motor Ranked en Firestore local reales antes/después dan resultados idénticos; R8 doble y CloudEvent sintético. Auth local REST operativo, sin Google OAuth. Emulador Android sin controlador de aceleración, intento por software permanece offline: cero pantallas ejecutadas. Auditor 158 contrastes estáticos, expectativas anteriores conservadas. Sin cambio de producción, reglas, integración, retiradas o temporadas. [Resultados, reproducción y bloqueo](ANDROID_EXECUTION.md).


Registro 05/10/2026, America/Lima. Cambios locales de Codex; HEAD permanece
`7d879c22030527e1d5f5003ad84b76b750d3b29e`, rama `codex/refactorizacion-por-bloques`, índice vacío.
Sin publicación, push/merge, despliegue o datos reales. El aporte humano nuevo
es la instrucción de Marco que comunica el mensaje/captura posterior de Renzo;
no se recibió la imagen binaria ni su fecha original/aprobación grupal.
No se atribuye código, análisis o integración Codex al estudiante.

| Bloque/capa | Hecho observado | Límite o dependencia concreta |
|---|---|---|
| Recuperación REC-002 | Descargado `Inkr8 - Backup.zip`, 116.969.901 bytes, CRC correcto; 106 archivos de producción compartidos iguales a HEAD; 21 archivos faltantes incorporados. | El paquete es un snapshot con Git interno 4abf009… (commit pre-beta-v0.6.6, 2026-08-27), no una actualización del HEAD local. No se importó su .git ni se sobrescribió el código local. |
| Android | Gradle 9.1.0 original arranca y descarga plugins/dependencias. Primer `assembleDebug`/`testDebugUnitTest` termina exit 1: SDK location not found. | SDK API 36/Build Tools 36.0.0 y aceptación humana de licencia. No se alcanzó compilación Kotlin/Compose, tests Android, APK o ejecución visual. |
| Functions | `npm ci` con lock original: 711 paquetes. `npm run build` antes y después de IMPL-009: exit 0 con TS 5.9.3, Node 20.20.2, firebase-admin 13.7.0, firebase-functions 7.1.1, openai 6.32.0. | Se eligió un patch oficial Node 20 para engines=20; el patch original de Renzo no se conoce. No se sustituyeron SDKs por stubs para compilar. |
| Rating / IMPL-009 | 21 escenarios existentes PASS sobre JS producido por el build completo auténtico. Fórmula/call-sites/transacciones conservados por revisión de fuente. | El probe de mínimo cero sigue siendo una expresión extraída, no una transacción ejecutada. No suma cobertura nueva por repetir escenarios con un compilador distinto. |
| Firebase | Coinciden proyecto/configuración Android; OAuth web presente. SDK auténtico carga el backend completo y descubre 24 endpoints: cero conexiones intentadas. | Discovery offline, no emulador/eventos/transacciones. `firebase.json` sólo aporta índices/Functions; no reglas Firestore ni fixtures originales. Hay endpoints heredados de torneos/reputación: retirada backend pendiente. |
| R8 | `evaluateWithR8.ts` recibido, prompt/modelo en el mismo archivo e imports reales OpenAI/Functions. Seis casos de parseo/límites/fallback ejecutados con el SDK recibido y transporte local simulado. | Sin proveedor real, claves o calidad semántica evaluados. Un caso caracteriza un defecto previo: puntuación no numérica devuelve NaN/source=real; no acredita aceptación. Falta procedimiento local auténtico de provisión de la clave, no su valor en el chat. |
| Alcance SCP-001 | Ranked/rating/Merit conservados; torneos/ligas/reputación retirados del objetivo. Restituida presentación Merit existente en cinco archivos. | Temporadas sigue incluida, reglas pendientes. Usos Merit ligados a funciones retiradas abiertos; no se restablecen ganancias/costes nuevos ni se declara retirada integral. |
| Auditor/revisión separada | 155 contrastes estáticos PASS, dos controles negativos nuevos rechazados exactamente; cinco fuentes Kotlin con 0 errores PSI antes/después y firmas públicas iguales. | No son 155 pruebas funcionales, validación Compose, auditoría humana independiente o SOLID integral. |

## Paquete localizado y actualización controlada

Fuente: [carpeta principal del proyecto](https://drive.google.com/drive/folders/1Q8qxBKZFUkUrhdPp0EGVK8HLx7zlzu4k)
→ [Codigo base + gradle (backup)](https://drive.google.com/drive/folders/1XRnumcpsdbYlq6OMYgAcicnJco-d5f1A)
→ [Inkr8 - Backup.zip](https://drive.google.com/file/d/1PLS-M_o4qR4iRRn8RAGTCm1H0Elx1bbt/view).
El primer enlace de descarga en listado falló; la página propia abrió advertencia
de tamaño y permitió descarga autenticada. La respuesta sin sesión era HTML de
acceso y no se trató como ZIP. SHA256 del ZIP:
`9cbc37ebdd8e4cd60ed21b39d5d56afa5d826ec30e1c3ef00c244e3c0f127345`.
La metadata Drive «30 sept»/111,6 MB no acredita fecha de subida/aprobación.

Antes de importar se respaldaron/verificaron 193 archivos y el historial Git.
El ZIP se inspeccionó aparte, comprobando rutas/CRC; 23.511 archivos de caché o
generados y 1.995 de Git quedaron privados. No se copiaron node_modules, .gradle,
builds, lib, .idea, .kotlin, .git ni local.properties de Renzo. Los 20 recursos
IMPL-006 ya presentes coinciden; se preservaron. Wrapper properties 9.1.0 idéntico;
jar verificado contra SHA256 oficial. Configuración Firebase sensible permanece
local, ignorada y excluida de la entrega revisable. No se imprimieron claves.

Añadidos: settings/build raíz/app, catálogo/versiones/gradle.properties, scripts
y jar wrapper, proguard original, package/lock/tsconfigs/eslint/prettier Functions,
firebase.json/.firebaserc/Google Services local, R8 y dos tests de ejemplo
originales. Los ejemplos 2+2/instrumentado no se cuentan como cobertura del núcleo.
`IMPORTED_FILES.json` enumera acciones y hashes. Los 106 archivos compartidos
coinciden por blobs Git (LF normalizado para texto); no se mezcla producción de
otra versión. El primer informe de inspección comparó texto con bytes y su
correspondencia es inválida; se conserva como fallo de herramienta y se sustituye
como autoridad por `SOURCE_CORRESPONDENCE_BLOBS.json`, no por una suposición.

## Restitución Merit y revisión del diff

La dirección posterior supera la retirada total U3/DEC-03; antecedentes RET-001–005
intactos. Corrección funcional de alcance, separada de refactorización:

- RET-001: restituidos **Liquid Merit**, capacidad/hold de cabecera y **Merit Gain**
  de resultados con su divisor. Las cuatro retiradas de liga/posición/separador
  permanecen. Los componentes IMPL-004 no cambian.
- RET-002: restituida sólo la tarjeta de wallet propio Profile (saldo/deuda,
  capacidad, impuesto/hold) usando el bloque original. No se reintroducen
  reputación, torneos, subtítulos de liga o propinas de perfil ajeno.
- RET-004: restituidos estado/diálogo/launcher de expansión de capacidad Settings
  y los dos textos originales de recompensa de racha Home. Mantienen fórmulas,
  handlers, condiciones y firma; no se crea una ganancia nueva.
- RET-003/005 conservados. AppRoot/ViewModel/repositorios/backend no cambian;
  IMPL-001–009 y sus evidencias se preservan.

La revisión separada reconstruye archivos completos desde HEAD más cambios
declarados, comprueba firmas/consumidores y diferencias; no mueve orden de
operaciones, argumentos, lecturas diferidas, callbacks o fronteras transaccionales.
No hubo nueva extracción estructural sin necesidad. Las extracciones A05/A06
mantienen las evidencias previas; Android bloqueado impide elevarlas a Compose.

El auditor conserva su spec/tool/report anterior. Expectativas vigentes proceden
del alcance humano y fragmentos originales, más hashes del paquete. Se conservaron
dos fallos iniciales de preparación: decodificación cp1252 de JSON UTF-8 y mezcla
de configuración con inventario exclusivo de fuentes. Se corrigieron en el
auditor; no se cambió producción para ocultar resultados. Los controles en copias
rechazan quitar de nuevo Merit Gain y modificar R8 auténtico.

## Defecto reproducido y reglas abiertas

`functions/src/r8/evaluateWithR8.ts:119–131`: `Number(parsed.finalScore)` puede ser
NaN y el clamp lo conserva. La caracterización con respuesta SDK simulada
`finalScore: "not-a-number"` devuelve NaN/source=real. Defecto del evaluador
auténtico recibido, no regresión de IMPL-009. Elegir cero, fallo o mock altera la
política de evaluación; no se escoge por suposición. P1 §2.7 reserva: «Los algoritmos
que resuelven el problema principal del proyecto deben ser escritos por el
estudiante». La restricción se aplica al algoritmo/política nueva de evaluación,
no a toda condición ni extracción mecánica. Se aporta caso reproducible; faltan
decisión de respuesta inválida y la intervención crítica pertinente, sin exigir
copiar código para acreditar autoría.

Consulta agrupada pendiente: SDK/licencia y gastos Merit ligados a reputación/
Pantheon. Los nombres usados en la consulta son etiquetas conceptuales; rutas
reales: `functions/src/users/applyMeritAction.ts:104` (PURCHASE_REPUTATION_VIEW),
`functions/src/tips/tipProcessor.ts:15` (onTipCreated), callbacks AppRoot:132/272/277.
No se inventan funciones/archivos purchaseReputation.ts o tipPhilosopher.ts.
No se pregunta de nuevo por permanencia de Merit. Ranked conserva selección/coste
por League hasta la regla de retirada correspondiente; no se elige una alternativa.
Borrador hasta persistencia confirmada sigue decidido, pendiente de conexión
frontend/backend estudiantil; contexto cuenta/ejercicio, espera/identidad y
temporadas siguen independientes.

## Archivos/aportes concretos para reanudar ejecución

1. SDK local API 36/Build Tools 36.0.0 con licencias aceptadas, o aceptación del
   contrato para instalar las herramientas oficiales ya preparadas. Google exige
   [aceptar licencias SDK](https://developer.android.com/tools/sdkmanager#accept_licenses).
   No se ha aceptado por silencio ni se ha creado una licencia falsa.
2. Reglas Firestore auténticas (`firestore.rules` o su ruta/definición original;
   el firebase.json recibido no la referencia), procedimiento y fixtures de
   proyecto de prueba/emuladores si existen. Storage sólo si el equipo confirma
   su uso; no se encontró consumidor Storage en el código conservado.
3. Procedimiento privado original para provisionar OPENAI_API_KEY local/de prueba;
   no enviar su valor ni claves de servicio. La simulación no sustituye una
   evaluación real autorizada.
4. Conexión/procedimiento estudiantil de app a Auth/Firestore/Functions de prueba
   y comprobación de envío/borrador/resultados/transacciones. La configuración
   auténtica recibida no acredita que el proyecto sea de prueba; no se lanza la
   app contra datos reales.
5. Reglas abiertas ya identificadas: Merit ligado a funciones retiradas, Ranked
   sin ligas, identidad/errores/espera, contexto de borrador, respuesta inválida R8
   y temporadas. No hay aceptación integral o cierre de A05–A07.

## Comandos para reproducir y revisar

Checkout: `C:\Users\marco\OneDrive\Documents\ChatGPT\Refactorizar inkr8\checkout\Inkr8-ISW2`. Runtime de compilación Node: `C:\Users\marco\Documents\Codex\2026-10-04\inkr8-refactorizacion-por-bloques\work\REC-002-private\runtime\node-v20.20.2-win-x64`.
JDK: `C:\Users\marco\AppData\Local\Temp\inkr8-baseline-jdk17\expanded\jdk-17.0.20.1+1`. Herramientas SDK preparadas, sin licencia aceptada:
`C:\Users\marco\Documents\Codex\2026-10-04\inkr8-refactorizacion-por-bloques\work\REC-002-private\android-sdk`.

```powershell
Set-Location 'C:\Users\marco\OneDrive\Documents\ChatGPT\Refactorizar inkr8\checkout\Inkr8-ISW2\functions'
$taskNodeRoot = 'C:\Users\marco\Documents\Codex\2026-10-04\inkr8-refactorizacion-por-bloques\work\REC-002-private\runtime\node-v20.20.2-win-x64'
$env:PATH = "$taskNodeRoot;" + $env:PATH
& "$taskNodeRoot/npm.cmd" run build

# Después de provisionar el SDK/licencias, sólo compilación/pruebas locales:
Set-Location 'C:\Users\marco\OneDrive\Documents\ChatGPT\Refactorizar inkr8\checkout\Inkr8-ISW2'
$env:JAVA_HOME = 'C:\Users\marco\AppData\Local\Temp\inkr8-baseline-jdk17\expanded\jdk-17.0.20.1+1'
$env:ANDROID_HOME = 'RUTA_DEL_SDK_ACEPTADO'
.\gradlew.bat --no-daemon :app:assembleDebug :app:testDebugUnitTest --console=plain

# Auditor read-only; carpeta de reporte nueva:
python .\testing-blocks\local-refactor-audit\verify_local_refactor.py --repo . --report-dir RUTA_NUEVA_DE_REPORTE
```

Para ejecución funcional, preparar primero entorno/emuladores reales de prueba
y la conexión correspondiente P1. Revisar escrituras fallidas, recomposición,
cambio de usuario/ejercicio, envíos A/B/resultados tardíos, errores, evaluación
R8 válida/ inválida y transacciones Ranked; estos recorridos no se han ejecutado.
No iniciar deploy ni usar scripts de shell contra proyecto real.

La entrega contiene diff textual incremental y copias de archivos públicos
añadidos/cambiados (wrapper jar binario incluido en ZIP, no en diff textual).
Config privada, cachés y paquete original no se entregan. Resultados originales,
logs de fallos, snapshots y manifests están en REC-002/SCP-001.

**Vigente AND-002 — 06/10/2026:** WHPX/AVD operativos y pantallas ejecutadas en
demo-inkr8-local. 14 métodos Android únicos: 13 PASS y 1 FAIL real (isPlaced no
llega al modelo); tres contrastes centrales antes/después PASS; cinco escenarios
transaccionales iguales y entrega automática local/callable PASS, R8 HTTP doble.
No código productivo nuevo; soporte/fixtures corregidos. Borrador anticipado,
resolución por B, idempotencia/carrera y payload gamemode siguen como defectos o
contratos pendientes. Ranked/rating/Merit permanecen. [Resultados, APK, límites y
aportes concretos](ANDROID_EXECUTION.md). No cierre integral ni autoría estudiantil.
