package com.inkr8.rating

import kotlin.test.Test
import kotlin.test.assertEquals

class ReputationManagerTest {

    @Test
    fun `reputation is clamped at both limits`() {
        assertEquals(1_000L, ReputationManager.adjustReputation(995L, 20L))
        assertEquals(-1_000L, ReputationManager.adjustReputation(-995L, -20L))
    }

    @Test
    fun `positive delta that reaches zero crosses to positive one`() {
        assertEquals(1L, ReputationManager.adjustReputation(-1L, 1L))
    }

    @Test
    fun `negative delta that reaches zero crosses to negative one`() {
        assertEquals(-1L, ReputationManager.adjustReputation(1L, -1L))
    }

    @Test
    fun `event helpers preserve their current deltas`() {
        assertEquals(103L, ReputationManager.onRankedCompleted(100L))
        assertEquals(88L, ReputationManager.onRankedAbandoned(100L))
        assertEquals(108L, ReputationManager.consistencyBonus(100L))
    }

    @Test
    fun `daily drift moves one point toward zero`() {
        assertEquals(4L, ReputationManager.dailyDrift(5L))
        assertEquals(-4L, ReputationManager.dailyDrift(-5L))
        assertEquals(0L, ReputationManager.dailyDrift(0L))
    }
}
