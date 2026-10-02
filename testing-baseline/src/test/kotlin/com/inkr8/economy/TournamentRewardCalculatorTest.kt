package com.inkr8.economy

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TournamentRewardCalculatorTest {

    @Test
    fun `non-positive player counts produce no rewards`() {
        assertEquals(emptyList(), TournamentRewardCalculator.calculateRewardPercentages(0))
        assertEquals(emptyList(), TournamentRewardCalculator.calculateRewardPercentages(-3))
    }

    @Test
    fun `a single player receives the complete reward`() {
        assertEquals(listOf(1.0), TournamentRewardCalculator.calculateRewardPercentages(1))
    }

    @Test
    fun `ten-player rewards preserve winner count order and total`() {
        val rewards = TournamentRewardCalculator.calculateRewardPercentages(10)

        assertEquals(10, rewards.size)
        assertEquals(0.45, rewards.first(), 0.0)
        assertEquals(0.0, rewards[8], 0.0)
        assertEquals(0.0, rewards[9], 0.0)
        assertEquals(1.0, rewards.sum(), 1e-12)
        assertTrue(rewards.take(8).zipWithNext().all { (left, right) -> left > right })
    }
}
