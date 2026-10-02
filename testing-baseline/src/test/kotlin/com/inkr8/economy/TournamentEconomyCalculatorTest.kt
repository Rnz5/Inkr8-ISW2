package com.inkr8.economy

import kotlin.test.Test
import kotlin.test.assertEquals

class TournamentEconomyCalculatorTest {

    @Test
    fun `nominal projection preserves current fee and profit calculations`() {
        assertEquals(
            TournamentEconomyProjection(
                prizePool = 10_000L,
                maxPlayers = 20,
                entranceFee = 560L,
                totalRevenue = 11_200L,
                systemFee = 340L,
                netProfit = 860L,
                breakEvenPlayers = 19,
            ),
            TournamentEconomyCalculator.calculateProjection(10_000L, 20),
        )
    }

    @Test
    fun `invalid values are coerced to current safe minimums`() {
        assertEquals(
            TournamentEconomyProjection(
                prizePool = 1L,
                maxPlayers = 2,
                entranceFee = 1L,
                totalRevenue = 2L,
                systemFee = 0L,
                netProfit = 1L,
                breakEvenPlayers = 1,
            ),
            TournamentEconomyCalculator.calculateProjection(0L, 1),
        )
    }

    @Test
    fun `entrance fee rounds upward when target revenue is not divisible`() {
        assertEquals(
            TournamentEconomyProjection(
                prizePool = 1_001L,
                maxPlayers = 3,
                entranceFee = 374L,
                totalRevenue = 1_122L,
                systemFee = 34L,
                netProfit = 87L,
                breakEvenPlayers = 3,
            ),
            TournamentEconomyCalculator.calculateProjection(1_001L, 3),
        )
    }
}
