package com.inkr8.economy

import kotlin.test.Test
import kotlin.test.assertEquals

class RankedCostCalculatorTest {

    @Test
    fun `neutral input preserves base cost`() {
        assertEquals(100L, RankedCostCalculator.calculateCost(100L, 0L, 0L, 0L))
    }

    @Test
    fun `streak modifiers stop at their current caps`() {
        assertEquals(175L, RankedCostCalculator.calculateCost(100L, 100L, 0L, 0L))
        assertEquals(60L, RankedCostCalculator.calculateCost(100L, 0L, 100L, 0L))
    }

    @Test
    fun `reputation modifiers change at current thresholds`() {
        val expectedByReputation = mapOf(
            199L to 100L,
            200L to 97L,
            400L to 94L,
            700L to 88L,
            900L to 80L,
            -199L to 100L,
            -200L to 112L,
            -400L to 120L,
            -700L to 130L,
            -900L to 140L,
        )

        expectedByReputation.forEach { (reputation, expected) ->
            assertEquals(
                expected,
                RankedCostCalculator.calculateCost(100L, 0L, 0L, reputation),
                "Unexpected cost at reputation $reputation",
            )
        }
    }

    @Test
    fun `combined streak and reputation modifiers are truncated to long`() {
        assertEquals(103L, RankedCostCalculator.calculateCost(100L, 4L, 2L, 400L))
    }

    @Test
    fun `calculated cost never falls below one`() {
        assertEquals(1L, RankedCostCalculator.calculateCost(0L, 0L, 100L, 900L))
    }
}
