package com.inkr8.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class ValidationUtilsTest {

    @Test
    fun `content shorter than fifty trimmed characters is rejected`() {
        assertEquals(
            true to "Transmission too short (min 50 chars)",
            ValidationUtils.isContentLowQuality("A short but otherwise readable message."),
        )
    }

    @Test
    fun `a word longer than thirty-five characters is rejected`() {
        val content = "a".repeat(36) + " followed by enough readable words to pass the length check"

        assertEquals(
            true to "Nonsense detected (excessive word length)",
            ValidationUtils.isContentLowQuality(content),
        )
    }

    @Test
    fun `ten highly repetitive words are rejected`() {
        val content = List(10) { "repeat" }.joinToString(" ")

        assertEquals(
            true to "Repetitive content detected",
            ValidationUtils.isContentLowQuality(content),
        )
    }

    @Test
    fun `diverse readable content is accepted`() {
        val content = "Bright rivers carry patient stories across quiet valleys every morning."

        assertEquals(false to null, ValidationUtils.isContentLowQuality(content))
    }
}
