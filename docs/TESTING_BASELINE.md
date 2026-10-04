# Baseline histórico de Inkr8

**Estado documental:** 03/10/2026. **Ejecución observada:** 02/10/2026. [Fuentes C1/B1/A1/G1](SOURCE_INDEX.md). Este índice no modifica el baseline histórico ni acredita pruebas de la aplicación completa.

## Referencias fijadas

| Referencia | Valor |
|---|---|
| Repositorio / rama auditada | [Inkr8-ISW2](https://github.com/Rnz5/Inkr8-ISW2), master |
| Producción anterior al arnés | [3d107ae6bb93f7c9d4bf9e103eccac1ae2d931de](https://github.com/Rnz5/Inkr8-ISW2/commit/3d107ae6bb93f7c9d4bf9e103eccac1ae2d931de) |
| Commit con arnés y documentación | [64983846d6dbf4cdfafcdd508464cd140a2a862e](https://github.com/Rnz5/Inkr8-ISW2/commit/64983846d6dbf4cdfafcdd508464cd140a2a862e) |
| Relación entre commits | 17 archivos añadidos: 4 docs, 7 infraestructura y 6 tests. Cero cambios en app/src/main o functions/src |
| Ejecución auditada | 22 casos, 0 fallos, 0 errores y 0 omitidos; BUILD SUCCESSFUL en 47 s |
| Entorno utilizado | Windows, Temurin JDK 17.0.20.1+1, Gradle 8.14.5, Kotlin 2.4.20, JUnit 4.13.2, target JVM 1.8 |
| Integridad del checkout después | Sin cambios versionados; salidas de build ignoradas |

El commit anterior no contiene el arnés. Repetir esa suite exige el commit con baseline o su arnés auténtico. La suite debe ejecutarse desde un checkout con el arnés auténtico de C1 y JDK completo. Una copia aislada de docs no contiene por sí sola el arnés.

Desde un checkout adecuado, con JAVA_HOME apuntando a un JDK completo, el comando que se comprobó fue:

```powershell
.\testing-baseline\gradlew.bat -p testing-baseline clean test --no-daemon
```

Se comprobó el entorno usado entonces; no una instalación limpia sin caché/red ni todos los equipos del grupo. Esta creación documental no volvió a ejecutar los tests.

## Clases y resultados

| Suite / fuentes | Casos | Resultado histórico | Relación con producto objetivo |
|---|---:|---|---|
| EconomyConfig | 2 | PASS | Economía Merit retirada; referencia del antes |
| RankedCostCalculator | 5 | PASS | Precio Merit con reputación, ambos retirados |
| TournamentEconomyCalculator + Projection | 3 | PASS | Torneos retirados |
| TournamentRewardCalculator | 3 | PASS | Torneos retirados |
| ReputationManager | 5 | PASS | Reputación retirada |
| ValidationUtils | 4 | PASS | Parte conservada de validación Kotlin, sin pantalla completa ni filtro TS |
| Total | **22** | **0 FAIL / 0 ERROR / 0 SKIPPED** | Caracterización histórica de siete fuentes reales |

El sourceSet del arnés compila directamente siete archivos de producción, no copias recreadas. [Configuración auditada](https://github.com/Rnz5/Inkr8-ISW2/blob/64983846d6dbf4cdfafcdd508464cd140a2a862e/testing-baseline/build.gradle.kts).

## Evidencia conservada

Copias literales de los cuatro documentos del repositorio:

- [BASELINE_PRE_REFACTOR.md](testing-baseline/BASELINE_PRE_REFACTOR.md)
- [CASOS_DE_PRUEBA.md](testing-baseline/CASOS_DE_PRUEBA.md)
- [RESULTADOS_PRE_REFACTOR.md](testing-baseline/RESULTADOS_PRE_REFACTOR.md)
- [BLOQUEOS_Y_OBSERVACIONES.md](testing-baseline/BLOQUEOS_Y_OBSERVACIONES.md)

Los resultados históricos descritos por sus autores y la reejecución auditada del 2 de octubre son evidencias relacionadas pero distintas. No sobrescribir los documentos para fingir que registraron esta creación de docs.

Los seis XML de la reejecución del 2 de octubre fueron conservados en el paquete local del equipo, sin modificar sus bytes; no se duplican en esta publicación. El resumen de resultados y sus límites está en G1 y en este índice. No se ejecutó otra vez la suite para publicar documentación.

## Límites y verificación futura

No se probaron UI/Compose, AppViewModel, repositorios, Auth/Firestore, anuncios, red, TypeScript, R8, integración o extremo a extremo, ni temporadas. Los tests existentes tampoco cubren cada método de toda la lógica pura.

La versión objetivo exige propósitos separados: caracterización del núcleo conservado, aceptación de retiradas aprobadas y aceptación de temporadas nuevas. Mantener el baseline del antes no obliga a conservar sus reglas económicas, torneos o reputación. No borrarlo para ocultar esas diferencias.

Las pruebas individuales de P3 —caja blanca CC >4; caja negra >4 campos; método unitario con al menos 4 casos— requieren asignación, ejecución y explicación por integrante. **22 PASS no demuestran cumplimiento individual, cobertura global ni producto ejecutable.** Consultar [requisitos](PROFESSOR_REQUIREMENTS.md) y [bloqueos](STATUS.md).
