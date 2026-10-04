# Contexto de Inkr8

Actualizado: 03/10/2026, America/Lima. Documento de trabajo derivado; las fuentes y su autoridad están en [SOURCE_INDEX.md](SOURCE_INDEX.md).

## Propósito y etapa

Inkr8 es un proyecto académico de Ingeniería de Software II que continúa una aplicación Android de Ingeniería de Software I. La propuesta original consiste en practicar escritura en inglés con restricciones de palabras o temas, recibir evaluación y feedback de R8 y participar en competencia. Esta motivación proviene de I1 y D1; no acredita resultados educativos medidos.

La etapa actual organiza instrucciones, fuentes, alcance, decisiones y evidencia. U4 autoriza AGENTS y documentación inicial; U5 autoriza publicar esa documentación en una rama y PR del repositorio. No se autoriza iniciar la refactorización, retirar código, implementar temporadas o cambiar arquitectura.

## Producto original y producto objetivo

El código observado contiene Practice, Ranked, escritura Standard y On-Topic, autenticación, perfil, historial, torneos, ligas, reputación, economía Merit, rankings y servicios de mantenimiento. Su presencia en C1 no demuestra funcionamiento de un despliegue.

El alcance vigente se obtiene de D1 aprobado por el grupo, con las aclaraciones U2/U3:

| Área | Estado del producto objetivo |
|---|---|
| Escritura, evaluación y feedback | Conservar el núcleo; analizar mejoras de estructura. |
| Practice y Ranked | Conservar. Ranked no cobrará entrada en Merit. |
| Rating Ranked | Conservar según D1; rating y reputación son conceptos distintos. |
| Autenticación, perfil básico, persistencia y estados | Conservar dentro de la delimitación. |
| Torneos, ligas y reputación | Retirada funcional aprobada. |
| Merit | Retirada completa aprobada por U3: moneda, ganancias y todos sus usos. No existe una moneda sustituta aprobada. |
| Temporadas | Incorporación aprobada; HU 3.27/3.28 en I1. Falta concretar reglas y aceptación. |

La permanencia de Merit ya está resuelta. Las formulaciones de D1 que conservaban economía básica o liga numérica se interpretan con U2/U3. La retirada y la incorporación son cambios funcionales; no deben registrarse como mera refactorización. Los detalles y límites están en [REFACTOR_SCOPE.md](REFACTOR_SCOPE.md).

## Implementación observada

Referencia: [C1, commit 64983846…](https://github.com/Rnz5/Inkr8-ISW2/tree/64983846d6dbf4cdfafcdd508464cd140a2a862e), rama `master` consultada durante la auditoría.

- **Cliente:** Android/Kotlin y Jetpack Compose; navegación en `AppRoot`, coordinación en `AppViewModel`, pantallas, modelos, mappers y repositorios concretos.
- **Backend:** Firebase Functions/TypeScript; funciones invocables, triggers Firestore y tareas programadas.
- **Servicios:** Firebase Auth, Firestore y llamadas a Functions. Los motores importan `evaluateWithR8`, pero su fuente falta en el checkout.
- **Flujo principal:** acceso → elección Practice/Ranked → escritura → creación de submission → persistencia en Firestore → evaluación → actualización de estado → resultados en el cliente. Ranked añade placement y emparejamiento/rating.

Esto describe el código actual; no fija una arquitectura futura ni selecciona patrones. I1/D1 identifican GPT-4o mini como motor de R8, pero el código disponible no permite confirmar el modelo configurado, el prompt efectivo ni una llamada real a OpenAI.

Las funciones retiradas cruzan el núcleo: `Competitions` reúne Ranked y torneos; la liga decide actualmente el ejercicio Ranked; `applyMeritAction` mezcla cobros y gestión de sesiones; la evaluación combina feedback, economía, placement, reputación y emparejamiento. La limpieza de sesiones también penaliza reputación. Por ello, el alcance no se puede traducir a una eliminación automática de archivos. Véase [G2, mapa técnico histórico](reference/TECHNICAL_MAP_2026-10-03.md).

## Glosario mínimo

| Término | Uso en las fuentes |
|---|---|
| Practice | Modo de práctica de escritura. |
| Ranked | Modo competitivo conservado, vinculado al rating. |
| Standard / On-Topic | Tipos de ejercicio de escritura; no son ligas ni temporadas. |
| Submission | Texto enviado con autor, modo, restricciones, estado y evaluación. |
| R8 | Evaluador de textos identificado por el proyecto; fuente de integración ausente en C1. |
| Rating | Valor competitivo conservado. |
| Liga | Clasificación temática derivada del rating en el producto original; retirada. |
| Reputación | Magnitud distinta del rating, con eventos y penalizaciones propias; retirada. |
| Merit | Moneda del producto original; retirada con todos sus usos. |
| Placement | Proceso inicial de asignación de rating presente en el código; su aceptación objetivo debe concretarse. |
| Temporada | Periodo competitivo con ranking actual e historial solicitado en las HU; no hay implementación identificable en C1. |

## Documentación histórica y límites

Las carpetas de Drive de I1 e I2 están invertidas respecto del contenido: I1 es el informe ISW1 guardado en «04 - Proyecto ISW2 - Estado actual»; I2 es el borrador ISW2 guardado en «03 - Proyecto ISW1 - Estado original». I2 conserva mayormente la plantilla oficial y sus ejemplos: no son pruebas, sprints ni logros reales de Inkr8.

M1 aporta UML histórico editable, salvo el modelo de BD disponible en PNG. Hay diferencias entre ese modelo y las colecciones/campos del código. No se declara ninguna de esas versiones como diseño final ni se redibujan modelos en esta etapa.

El backlog completo con aceptación no se localizó entre las fuentes revisadas. I1 permite identificar HU, incluidas 3.27/3.28, pero no basta para completar reglas de temporadas. Tampoco se han confirmado patrones específicos, clases a intervenir ni una política definitiva de aprobación: D2 etiqueta su flujo como propuesta.

## Estado técnico comprobado

La auditoría se realizó en una copia separada de C1 y la documentación se redactó inicialmente en una carpeta local sin código de aplicación. Esta edición se integra como propuesta documental en el repositorio oficial; no recupera los archivos de build ausentes ni modifica producción.

El arnés JVM de C1 se reejecutó el 02/10/2026: **22 PASS, 0 FAIL, 0 SKIPPED y 0 errores**. Cubre siete archivos de lógica Kotlin. No valida Android, UI, Functions, Firebase, R8 ni integración, ni satisface por sí solo los requisitos individuales de la rúbrica. Véase [TESTING_BASELINE.md](TESTING_BASELINE.md).

El checkout carece de configuración Gradle principal/Android completa, manifiestos y configuración de Functions, fuente `evaluateWithR8.ts` y configuración/reglas Firebase completas. G2 registra también recursos Android faltantes respecto de referencias en el código. El producto completo no se construyó ni se ejecutó; no se verificó un despliegue.

La siguiente actividad es concretar aceptación de temporadas y del núcleo tras las retiradas, manteniendo visibles los bloqueos de ejecutabilidad. El trabajo autorizado y los pendientes se mantienen en [STATUS.md](STATUS.md); las decisiones en [DECISIONS.md](DECISIONS.md). Las reglas académicas se consultan en [PROFESSOR_REQUIREMENTS.md](PROFESSOR_REQUIREMENTS.md).
