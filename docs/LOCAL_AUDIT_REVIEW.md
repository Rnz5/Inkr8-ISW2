# Revisión local separada de implementación — AUD-001

**Vigente CORE-FINAL-001 — 06/10/2026:** cierre técnico del núcleo A01–A07 en los
contratos explícitos y laboratorio autorizado. Revisión exacta del texto,
bloqueo de persistencia, borradores por UID/ejercicio/palabras y reintento sólo de
consulta del ID confirmado implementados por Codex. 46 métodos Android únicos y
19 unit tests PASS; 11 Ranked +2 ghost +1 lifecycle SDK PASS; fórmulas intactas.
Reentrada/metadata y espera anterior corregidas; fallos de soporte conservados.
R8/eventos/barreras dobles declarados; no producto completo ni aceptación externa.
[Tabla final, criterios, diffs, APK y comandos](CORE_FINAL_AUDIT.md).
Los pendientes anteriores de estos contratos son antecedentes superados; los
frentes de temporadas, retiradas completas, validación externa y academia siguen.

**CORE-CLOSE-001 / DEC-MODE-001 — revisión 06/10/2026:** compatibilidad
gamemode→gamemodeName en siete lectores Android, motor/Merit y estadísticas,
escrituras/historia intactas. Correcciones funcionales ghost atómico, Results del
mismo ID GHOST y promesa de matching esperada; fórmulas originales conservadas.
Android/Functions/lab compilan. 13 métodos Android únicos PASS; matriz Functions
15 PASS, ghost2 PASS, lifecycle1 PASS, invariantes Ranked11 PASS (sin sumar
repeticiones). Android→Firestore→evaluador→Results STANDARD/ON_TOPIC comprobado
localmente, R8 HTTP doble. Auditoría separada del agente; no cierre integral.
Nueva revisión durante guardado pierde borrador: roja preservada, consulta única
pendiente; contexto/reintento y ejecución externa siguen abiertos. Ranked/rating/
Merit permanecen; retiradas/temporadas separadas. [Tabla A01–A07, alcance,
resultados y reproducción](CORE_CLOSURE_AUDIT.md), [diff consolidado](../../../docs/evidence/CORE-CLOSE-001/CORE-current.patch).

Los registros anteriores que indican modo sin resolver o aporte manual requerido
son antecedentes de sus versiones, superados por DEC-MODE-001/DEC-AI-AUTH-001.

**RET-005 — 05/10/2026:** retirados nombre y veredicto de liga de PlacementReveal; conserva «Calibration Complete», secuencia/espera/botón y firma/conexión anteriores. Sintaxis Kotlin PSI 0 errores antes/después; auditor 135 contrastes estáticos finales y dos controles negativos nuevos rechazados. Continúa RET-002–004 sin modificar sus entregas/evidencias. No nueva regla o sustituto de placement ni prueba Compose/Firebase. [Registro y límites](IMPLEMENTATION_LOG.md).


**RET-002–004 / revisión trazable — 05/10/2026:** retiradas de presentación aprobadas aplicadas en Profile, Competitions, Settings y Home; separadas de IMPL-001–009 y temporadas. Perfil básico/rating, controles/selección Ranked, auth y persistencia conservados. Kotlin PSI 0 errores antes/después; auditor 135 contrastes estáticos finales y cuatro controles negativos rechazados, sin Compose/Firebase funcional. [Cambios, límites y evidencia](IMPLEMENTATION_LOG.md). Cobros, listeners/rutas, placement ligado a ligas, integración de borrador confirmado y reglas/entorno originales siguen pendientes. Renzo confirmará fuentes; no búsqueda repetida ni automatización.


**VER-001 — 05/10/2026:** revisión separada de IMPL-007–009 sin defecto nuevo de extracción; [ejecución y límites](EXECUTION_READINESS.md). Productor rating typechecked y call-sites compilados con TS 7.0.2 disponible: 1.156 pares/2.312 deltas sin diferencias; control UNKNOWN rechazado TS2345. Gradle/JDK del arnés operativos; Android/Functions/Firebase/R8 siguen sin fuentes/configuración auténticas. [Lista única para el equipo](ORIGINAL_ENVIRONMENT_REQUEST.md). Decisión humana: conservar texto/borrador hasta persistencia confirmada; conexión de confirmación UI pendiente según P1 §2.7. Espera: orientación general recibida, sin regla nueva asumida; asociación cuenta/ejercicio y Ranked pendientes. Sin nuevas suites JVM/auditor repetidos ni cierre integral.



**Vigente — IMPL-007–009 / AUD-002, 05/10/2026:** extracciones mecánicas locales de admisión, decisiones escalares de espera y fórmula de rating; [registro y límites](MECHANICAL_CORE_REFACTOR.md). Pruebas pertinentes antes/después: Writing 20→28 PASS, espera 15→15, rating 21→21; auditor 135 PASS y cuatro controles negativos rechazados. Corrección explícita: P1 no exige copiar código para cualquier extracción. Todo código/pruebas es Codex; aporte humano = encargo/límites/autorización, sin atribuir elección arquitectónica o implementación estudiantil. A05–A07 sólo parcialmente abordados; entorno/integración/decisiones funcionales pendientes. Los estados anteriores que siguen son históricos.



05/10/2026, America/Lima. Revisión de Codex para preparar una auditoría independiente posterior. HEAD `7d879c22030527e1d5f5003ad84b76b750d3b29e`, rama `codex/refactorizacion-por-bloques`, origin oficial. No es revisión de un tercero ni aceptación integral del producto.

## Autorización y límite real

El encargo humano permite extracciones mecánicas y ajustes rutinarios locales, conserva P1 y exige revisión final. A la consulta agrupada se recibió literalmente **«sigue la mejor opcion posible»**. Esa respuesta autoriza continuar el apoyo y la recomendación; no contiene una justificación arquitectónica estudiantil ni un aporte manual localizable. Se registra la respuesta real y no se vuelve a solicitar permiso por archivo o función. La propuesta mínima A05 permanece como recomendación IA, con evaluación/aporte estudiantil por acreditar.

Las intervenciones conservadoras anteriores IMPL-001–004 están aplicadas localmente. IMPL-005 caracteriza fragmentos existentes e IMPL-006 recupera recursos; RET-001 retira presentación aprobada. Ninguna de estas últimas intervenciones resuelve por sí sola A05–A07. No hay nuevo bloque crítico habilitado por aporte auténtico en esta revisión: no se ha recibido archivo manual, relación de BD, lógica crítica o conexión estudiantil. No se sustituye esa ausencia por código automático.

## Cambio independiente realizado

Se añadió `testing-blocks/local-refactor-audit/` con tres archivos: `verify_local_refactor.py`, `audit-spec.json` y `README.md`. Python estándar, Git de lectura y salidas en una carpeta nueva; no modifica producción o servicios ni inventa versiones Android/Functions.

La herramienta contrasta el estado con Git y fragmentos históricos comprobados:

- Recompone ValidationUtils retirando las declaraciones Regex/referencias introducidas; el algoritmo y mensajes resultantes coinciden con HEAD.
- Recompone Writing retirando las dos declaraciones Regex y reinsertando los cinco componentes originales. Coinciden editor, admisión, tokenización, borrador, evento y preview. Comprueba además que los componentes trasladados permanecen literales en su archivo.
- Recompone Results desde HEAD aplicando las dos extracciones registradas y, por separado, la retirada RET-001. Los cuerpos trasladados coinciden después de normalizar sólo su indentación; el resto de Results coincide con lo esperado.
- Aplica únicamente los seis spans UI de RET-001 a UserHeaderCard. No legitima una retirada integral de economía, reputación o torneos.
- Contrasta las otras 99 fuentes originales Kotlin/TypeScript con Git; no detecta cambios. Inventaría además las dos fuentes nuevas de componentes y rechaza adiciones de producción no revisadas.
- Verifica tamaño y blob Git de los 20 recursos restaurados contra la procedencia original fijada en IMPL-006.

Se compara CRLF/LF en código, sin normalizar bytes de recursos. El spec contiene fragmentos auténticos y sus propósitos, no una implementación del motor o una configuración sustitutiva. Los contrastes fallan ante cambios posteriores: deben revisarse con trazabilidad, no ajustarse para ocultar diferencias.

## Resultados ejecutados

| Comprobación nueva | Resultado y alcance |
|---|---|
| Primera ejecución del auditor | Interrumpida tras 109 contrastes por un metadato Unicode mal leído por el nuevo preparador. Reporte/spec inicial conservados. No fue un fallo del producto. |
| Ejecución corregida | 130/130 contrastes estáticos pasan, proceso 0. Se conservan el reporte y spec de esa versión. |
| Revisión de la herramienta | Se reforzó la comparación de los archivos completos de componentes: revisar sólo cuerpos trasladados no rechazaba declaraciones nuevas fuera de ellos. **132/132 contrastes finales pasan**, proceso 0. |
| Control negativo | En una copia aislada se cambió `wordCount >= it` por `wordCount > it`. El auditor devolvió proceso 1 y rechazó exactamente la conservación del evento/admisión de Writing. El checkout real no se modificó. |
| Segundo control negativo | Se añadió una declaración no revisada en la copia de WritingComponents. La versión reforzada rechazó exactamente ese archivo, proceso 1. No se modificó producción. |
| Parches y copia reconstruida | Parches conservador y RET-001 preparados por separado; diff acumulado con herramientas/documentos/recursos. Aplicación y comparación en copia registradas en VERIFICATION.json. |
| Evidencias anteriores | Manifests IMPL-001–006, RET-001, cierres y preparación SOLID preservados; hashes y fuentes protegidas contrastados. |

**Cero nuevas ejecuciones JVM/Node, APK, Compose o Firebase/R8.** No hubo cambio del algoritmo de producto que justificara repetir las suites anteriores. Los 20 casos Writing, 15 JVM compartidos A05/A06 y 21 Node son evidencia previa, sin sumarlos como pruebas nuevas. Los 132 contrastes no son 132 métodos funcionales, cobertura o aceptación académica.

## Hallazgos y decisión de revisión

| Hallazgo | Clasificación / acción |
|---|---|
| Lectura de JSON histórico UTF-8 con codificación Windows por defecto en el preparador nuevo. | **Corregido en herramienta auxiliar**: UTF-8 explícito, spec nuevo regenerado desde metadato auténtico. Original y fallo conservados. |
| La comprobación inicial de componentes no abarcaba posibles declaraciones añadidas fuera de los cuerpos trasladados. | **Corregido en herramienta auxiliar**: comparación del archivo completo con snapshots IMPL-003/004 preservados y un control negativo adicional. |
| No se detecta diferencia no declarada en reglas, mensajes, orden del envío, borrador, otros productores/consumidores o recursos. | Resultado de conservación de fuente. No demuestra comportamiento de Compose o efectos Firebase en ejecución. |
| Writing mantiene admisión/reconocimiento/borrador junto a coordinación UI. | **A05 pendiente**: SRP puede justificar separar la decisión; if/else y archivo grande no bastan. Límites numéricos ya están en Gamemode; una razón útil propuesta es probar la decisión de producción directamente. Falta evaluación/aporte manual estudiantil. |
| ViewModel sigue usando repositorio concreto, ListenerRegistration y cuenta implícita; la espera consulta último envío. | **A06 pendiente**, anterior a esta etapa. SRP/DIP por justificar desde consumidor; corrección por ID/error/timeout distinta de refactorización. Results vacío también necesita su consulta al último. |
| Motor sigue mezclando evaluación, métricas/placement y efectos; rating humano ya tiene helper local y ghost aplica otra fórmula. | **A07 pendiente**. SRP prioritario; mover un helper ya puro no cierra DG-13. No se unifican fórmulas ni se sacan lecturas de transacción por conveniencia. |
| Funciones públicas movidas de Writing conservan paquete/nombre/firma Kotlin, pero cambia la fachada JVM del archivo. | **Límite pendiente**: no se encontraron consumidores Java/reflexión en el checkout; compatibilidad binaria externa no acreditada. Recompilar con entorno auténtico y revisar consumidores externos si existen. |
| Cabecera conserva condición/badge Philosopher y parámetro pantheonPosition; backend mantiene economía y torneos. | **Retirada funcional pendiente**, explícita en RET-001. No cuenta como regresión introducida por refactorización ni se borra historial. |
| Faltan builds/plugins/dependencias/wrapper completo, configuración Firebase, package/lock/tsconfig Functions y R8. | **Dependencia técnica** para tipos/build/render/integración. properties 9.1.0 ya existe; no usar versiones del arnés como originales. Fuentes exactas en SOLID_BLOCK_PREPARATION. |

No se acredita DIP porque haya un constructor, SRP porque se haya dividido un archivo, ni OCP/LSP/ISP sin evidencia específica. La separación UI anterior mejora cohesión de presentación; no resuelve las decisiones y efectos críticos pendientes. El resultado sirve para revisión independiente del diff y de sus límites.

## Aporte mínimo para reanudar A05

La recomendación sigue siendo ALT-01-1 acotada a la decisión existente, sin cambiar reglas. El trabajo estudiantil necesario puede concentrarse así, sin consultas por cada detalle:

1. Justificar con palabras propias qué motivo de cambio o verificación merece separar `canSubmit`, comparándolo con mantenerlo local.
2. Realizar una extracción manual acotada del cuerpo actual de `Writing.kt:131–135`, indicando archivo/ubicación elegidos y dependencia de texto, límites y conteo. No añadir patrones, entidades o reglas por obligación.
3. Mantener observación Compose, rama de texto vacío, límites inclusivos/nullable y lecturas necesarias; no cambiar reconocimiento, calidad/mensajes, construcción del envío o limpieza/autoguardado. Revisar la evaluación de argumentos para no introducir lecturas adicionales al extraer.
4. Indicar los archivos editados para revisar el aporte real. Codex puede hacer los ajustes mecánicos permitidos y adaptar/ejecutar la caracterización para ejercitar la producción extraída, conservando el antes.

No se exige recuperar R8 para caracterizar esa decisión aislada. La pantalla y su conexión real seguirán necesitando build auténtico. A06/A07/configuración/R8 continúan como dependencias independientes. No se ha registrado una extracción o justificación escrita por el estudiante que todavía no existe.

## Ejecución y revisión posterior

Desde el checkout, con Python 3 instalado y una carpeta de salida nueva:

```powershell
python .\testing-blocks\local-refactor-audit\verify_local_refactor.py --repo . --report-dir ..\Inkr8-audit-revision-01
```

Leer `audit.json`, el spec y el diff; el proceso 0 confirma sólo los contrastes declarados. Para una copia de parche, utilizar `--candidate-root` con la ruta de esa copia y `--repo` con el Git de referencia. No ejecutar scripts históricos de creación sobre carpetas de evidencia ya cerradas.

Cuando cambie lógica pertinente, repetir la suite de Writing con su extractor adaptado y registrar comparación antes/después; usar core-contract sólo si cambian los fragmentos A05/A06 pertinentes y Node sólo si cambia el cálculo correspondiente. No sumar ejecuciones antiguas ni simular aceptación Compose/Firebase mediante wrappers.

Para producto: recuperar originales, comprobar tareas/scripts auténticos, compilar y conectar manualmente en entorno de prueba; validar Writing, A/B, estados/error/timeout/cancelación/cuenta/placement y transacciones/R8 contra decisiones humanas. Las retiradas y temporadas tienen expected y bloques separados. Sin stage/commit/push, datos reales, publicación o despliegue.

Evidencia canónica: `C:/Users/marco/OneDrive/Documents/ChatGPT/Refactorizar inkr8/docs/evidence/AUD-001`. [Registro](IMPLEMENTATION_LOG.md), [estado](STATUS.md), [plan](CONSERVATIVE_BLOCK_PLAN.md), [preparación SOLID](SOLID_BLOCK_PREPARATION.md).
