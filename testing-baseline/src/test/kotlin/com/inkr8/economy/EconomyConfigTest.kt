package com.inkr8.economy

import kotlin.test.Test
import kotlin.test.assertEquals

class EconomyConfigTest {

    @Test
    fun `save-submission cost increases at each group of three`() {
        val expected = mapOf(
            0 to 2_000L,
            2 to 2_000L,
            3 to 2_200L,
            5 to 2_200L,
            6 to 2_400L,
        )

        expected.forEach { (savedCount, cost) ->
            assertEquals(cost, EconomyConfig.getSaveSubmissionCost(savedCount))
        }
    }

    @Test
    fun `streak multiplier follows every current boundary`() {
        val expected = mapOf(
            1 to 1.0,
            2 to 1.03,
            3 to 1.05,
            4 to 1.06,
            5 to 1.08,
            6 to 1.10,
            7 to 1.12,
            20 to 1.12,
        )

        expected.forEach { (streak, multiplier) ->
            assertEquals(multiplier, EconomyConfig.getStreakMultiplier(streak), 0.0)
        }
    }
}
