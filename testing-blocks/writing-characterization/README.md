# Caracterización aislada de Writing

**Vigente tras IMPL-007 (05/10/2026):** los 20 casos existentes llaman ahora a
`isWritingAdmitted` de producción, compilada directamente desde WritingAdmission.kt.
Ocho casos adicionales comprueban evaluación diferida, límites nullable/inclusivos,
excepciones y 240 comparaciones de resultado/lecturas con el cuerpo anterior.
AdmissionReference y AdmissionModeFixture son exclusivamente de prueba; la primera
conserva el predicado anterior y la segunda observa getters de límites dentro del
arnés aislado. No añaden modos al producto. Count, tokens y membership siguen
extraídos de Writing; el extractor exige la llamada de admisión dentro de derivedStateOf.
Resultado real: 20 PASS antes, 28 PASS después; sin prueba de observación/recomposición Compose.
La descripción IMPL-002 que sigue documenta el mecanismo anterior.

IMPL-002, 04/10/2026. Harness de pruebas creado con asistencia IA; revisión estudiantil pendiente. Las versiones/dependencias proceden del arnés auténtico testing-baseline, que no se modifica.

Con el JDK Temurin 17.0.20.1+1 ya disponible, desde la raíz del checkout:

```powershell
.\testing-baseline\gradlew.bat -p testing-blocks/writing-characterization clean test --no-daemon --offline
```

La task extractWritingSources extrae literalmente wordCount, normalizedUserWords, canSubmit y el predicado de wordsUsed de Writing.kt, verifica sus marcadores y registra líneas/hashes. Se compilan esos fragmentos en envolturas de prueba, junto con Gamemodes/Theme/Topics/ValidationUtils auténticos. El modelo SelectedWordFixture sólo aporta id/word al predicado; no es Words de Firebase ni un DTO. Los archivos generados van en build y no son utilizados por Android.

Se comprueban expresiones escalares y el orden del evento mediante inspección estática. No se construye Writing/Compose ni se simula recomposición, remember/derivedStateOf, callbacks, borradores, SubmissionFactory, serialización, Firebase o R8. Expected caracteriza el antes, no ratifica reglas funcionales. El evaluador/SDK y la configuración original siguen ausentes para integración.

No modificar manualmente el source generado: cambiar las fuentes exige regenerar y verificar procedencia. El extractor falla si un marcador no es único o cambia el orden de los efectos.
