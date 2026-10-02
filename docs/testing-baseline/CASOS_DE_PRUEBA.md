# Casos de Prueba del Baseline Pre-Refactor

| ID | Clase | Escenario | Entrada | Resultado esperado | Motivo |
|---|---|---|---|---|---|
| TEST-001 | `RankedCostCalculator` | Entrada neutral | base `100`, rachas `0/0`, reputación `0` | Coste `100` | Proteger el coste base sin modificadores |
| TEST-002 | `RankedCostCalculator` | Topes de rachas | base `100`; victoria `100` o derrota `100` | Costes `175` y `60` | Fijar los topes actuales de `+0.75` y `-0.40` |
| TEST-003 | `RankedCostCalculator` | Umbrales de reputación | `199/200/400/700/900` y `-199/-200/-400/-700/-900` | `100/97/94/88/80` y `100/112/120/130/140` | Proteger cada frontera de la tabla de reputación |
| TEST-004 | `RankedCostCalculator` | Modificadores combinados y truncado | base `100`, victoria `4`, derrota `2`, reputación `400` | Coste `103` | Capturar composición y conversión actual a `Long` |
| TEST-005 | `RankedCostCalculator` | Límite mínimo | base `0`, derrota `100`, reputación `900` | Coste `1` | Proteger `coerceAtLeast(1)` |
| TEST-006 | `TournamentEconomyCalculator` | Proyección nominal | premio `10000`, máximo `20` | premio `10000`, jugadores `20`, entrada `560`, ingreso `11200`, tasa `340`, beneficio `860`, equilibrio `19` | Proteger la proyección económica normal |
| TEST-007 | `TournamentEconomyCalculator` | Valores inválidos normalizados | premio `0`, máximo `1` | premio `1`, jugadores `2`, entrada `1`, ingreso `2`, tasa `0`, beneficio `1`, equilibrio `1` | Capturar los mínimos seguros actuales |
| TEST-008 | `TournamentEconomyCalculator` | División no exacta | premio `1001`, máximo `3` | entrada `374`, ingreso `1122`, tasa `34`, beneficio `87`, equilibrio `3` | Proteger redondeos y cálculo derivado |
| TEST-009 | `TournamentRewardCalculator` | Participantes no positivos | `0` y `-3` | Listas vacías | Proteger la salida para límites inválidos contemplados |
| TEST-010 | `TournamentRewardCalculator` | Participante único | `1` | Lista `[1.0]` | Proteger el caso especial de premio completo |
| TEST-011 | `TournamentRewardCalculator` | Torneo de diez participantes | `10` | 10 posiciones; primera `0.45`; últimas dos `0`; suma `1.0`; ganadores en orden decreciente | Proteger cantidad de ganadores, distribución y conservación del total |
| TEST-012 | `EconomyConfig` | Coste por grupos de tres guardados | conteos `0,2,3,5,6` | `2000,2000,2200,2200,2400` | Fijar los puntos de incremento actuales |
| TEST-013 | `EconomyConfig` | Multiplicadores de racha | rachas `1,2,3,4,5,6,7,20` | `1.0,1.03,1.05,1.06,1.08,1.10,1.12,1.12` | Proteger cada frontera y el tope |
| TEST-014 | `ReputationManager` | Saturación superior e inferior | `(995,+20)` y `(-995,-20)` | `1000` y `-1000` | Proteger los límites globales |
| TEST-015 | `ReputationManager` | Cruce positivo por cero | actual `-1`, delta `+1` | `+1` | Caracterizar la regla que evita devolver cero |
| TEST-016 | `ReputationManager` | Cruce negativo por cero | actual `+1`, delta `-1` | `-1` | Caracterizar la regla simétrica que evita devolver cero |
| TEST-017 | `ReputationManager` | Eventos de reputación | actual `100` | completado `103`, abandonado `88`, consistencia `108` | Proteger los deltas de los helpers públicos |
| TEST-018 | `ReputationManager` | Deriva diaria | `5`, `-5`, `0` | `4`, `-4`, `0` | Proteger el movimiento de un punto hacia cero |
| TEST-019 | `ValidationUtils` | Contenido corto | `"A short but otherwise readable message."` | Rechazado: `Transmission too short (min 50 chars)` | Proteger longitud mínima y mensaje actual |
| TEST-020 | `ValidationUtils` | Palabra demasiado larga | palabra de 36 letras dentro de contenido de al menos 50 caracteres | Rechazado: `Nonsense detected (excessive word length)` | Proteger detección y prioridad de esta regla |
| TEST-021 | `ValidationUtils` | Repetición alta | `repeat` diez veces | Rechazado: `Repetitive content detected` | Proteger el umbral de diversidad léxica |
| TEST-022 | `ValidationUtils` | Contenido legible y diverso | `Bright rivers carry patient stories across quiet valleys every morning.` | Aceptado: `(false, null)` | Incluir un camino válido representativo |
