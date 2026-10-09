package com.inkr8.utils

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals

/** Characterizes the existing client filter; these values are not new product rules. */
class ValidationUtilsRegexCharacterizationTest {
    private val accepted = false to null
    private val short = true to "Transmission too short (min 50 chars)"
    private val longWord = true to "Nonsense detected (excessive word length)"
    private val repetitive = true to "Repetitive content detected"
    private val unnatural = true to "Unnatural character distribution (nonsense)"
    private val lowDiversity = true to "Low character diversity (nonsense)"
    private val readable = "Bright rivers carry patient stories across quiet valleys every morning."

    @Test
    fun `trimmed character length retains the forty-nine and fifty boundary`() {
        val fortyNine = "1234567890 ".repeat(4) + "12345"
        assertEquals(49, fortyNine.length)
        assertEquals(short, ValidationUtils.isContentLowQuality(fortyNine))
        assertEquals(accepted, ValidationUtils.isContentLowQuality(fortyNine + "6"))
    }

    @Test
    fun `outer whitespace does not contribute to the minimum length`() {
        val fortyNine = "1234567890 ".repeat(4) + "12345"
        assertEquals(short, ValidationUtils.isContentLowQuality("\t\n $fortyNine \r\n"))
        assertEquals(accepted, ValidationUtils.isContentLowQuality("\t\n $readable \r\n"))
    }

    @Test
    fun `a token of thirty-five characters passes and thirty-six fails`() {
        assertEquals(accepted, ValidationUtils.isContentLowQuality("1".repeat(35) + " 1234567890 67890"))
        assertEquals(longWord, ValidationUtils.isContentLowQuality("1".repeat(36) + " 1234567890 67890"))
    }

    @Test
    fun `default regex splits ASCII whitespace including repeated separators`() {
        val words = readable.split(" ")
        for (separator in listOf(" ", "\t", "\n", "\r", "\u000B", "\u000C", "\r\n", " \t\n")) {
            assertEquals(accepted, ValidationUtils.isContentLowQuality(words.joinToString(separator)), separator)
        }
    }

    @Test
    fun `internal nonbreaking spaces and commas retain their token semantics`() {
        val numericWords = List(10) { (10000 + it).toString() }
        assertEquals(accepted, ValidationUtils.isContentLowQuality(numericWords.joinToString(" ")))
        assertEquals(longWord, ValidationUtils.isContentLowQuality(numericWords.joinToString("\u00A0")))
        assertEquals(longWord, ValidationUtils.isContentLowQuality(numericWords.joinToString(",")))
    }

    @Test
    fun `repetition starts at ten tokens and not nine`() {
        assertEquals(accepted, ValidationUtils.isContentLowQuality(List(9) { "12345" }.joinToString(" ")))
        assertEquals(repetitive, ValidationUtils.isContentLowQuality(List(10) { "12345" }.joinToString(" ")))
    }

    @Test
    fun `unique word ratio at point thirty-five passes and below fails`() {
        fun content(unique: Int) = List(20) { (it % unique).toString().padStart(6, '0') }.joinToString(" ")
        assertEquals(accepted, ValidationUtils.isContentLowQuality(content(7)))
        assertEquals(repetitive, ValidationUtils.isContentLowQuality(content(6)))
    }

    @Test
    fun `word repetition remains case insensitive`() {
        val content = List(10) { listOf("REPEAT", "Repeat", "repeat")[it % 3] }.joinToString(" ")
        assertEquals(repetitive, ValidationUtils.isContentLowQuality(content))
    }

    @Test
    fun `vowel checking starts after thirty ASCII letters`() {
        assertEquals(accepted, ValidationUtils.isContentLowQuality(asTokens("b".repeat(30))))
        assertEquals(unnatural, ValidationUtils.isContentLowQuality(asTokens("b".repeat(31))))
    }

    @Test
    fun `vowel ratio boundaries are inclusive`() {
        fun content(vowels: Int): String {
            val consonants = "bcdfghjklmnpqrstvwxyz".repeat(3).take(40 - vowels)
            return asTokens("a".repeat(vowels) + consonants)
        }
        assertEquals(unnatural, ValidationUtils.isContentLowQuality(content(5)))
        assertEquals(accepted, ValidationUtils.isContentLowQuality(content(6)))
        assertEquals(accepted, ValidationUtils.isContentLowQuality(content(32)))
        assertEquals(unnatural, ValidationUtils.isContentLowQuality(content(33)))
    }

    @Test
    fun `character diversity starts after sixty ASCII letters`() {
        val letters = "abcdefg".repeat(9)
        assertEquals(accepted, ValidationUtils.isContentLowQuality(asTokens(letters.take(60))))
        assertEquals(lowDiversity, ValidationUtils.isContentLowQuality(asTokens(letters.take(61))))
    }

    @Test
    fun `eight distinct ASCII letters pass where seven fail`() {
        assertEquals(lowDiversity, ValidationUtils.isContentLowQuality(asTokens("abcdefg".repeat(9).take(61))))
        assertEquals(accepted, ValidationUtils.isContentLowQuality(asTokens("abcdefgh".repeat(8).take(61))))
    }

    @Test
    fun `accented characters ideographs and emoji do not become ASCII letters`() {
        assertEquals(accepted, ValidationUtils.isContentLowQuality("é".repeat(35) + " " + "漢".repeat(20)))
        assertEquals(accepted, ValidationUtils.isContentLowQuality("é".repeat(35) + " " + "😀".repeat(10)))
        assertEquals(accepted, ValidationUtils.isContentLowQuality(readable.uppercase()))
    }

    @Test
    fun `first failing check keeps its existing message`() {
        assertEquals(short, ValidationUtils.isContentLowQuality("b".repeat(36)))
        assertEquals(longWord, ValidationUtils.isContentLowQuality("b".repeat(36) + " " + List(10) { "repeat" }.joinToString(" ")))
        assertEquals(repetitive, ValidationUtils.isContentLowQuality(List(10) { "bcdfghjkl" }.joinToString(" ")))
    }

    @Test
    fun `successive calls do not retain previous input state`() {
        repeat(50) {
            assertEquals(accepted, ValidationUtils.isContentLowQuality(readable))
            assertEquals(repetitive, ValidationUtils.isContentLowQuality(List(10) { "repeat" }.joinToString(" ")))
            assertEquals(accepted, ValidationUtils.isContentLowQuality(readable))
        }
    }

    @Test
    fun `concurrent calls preserve independent results`() {
        val executor = Executors.newFixedThreadPool(4)
        try {
            val futures = (0 until 200).map { index ->
                executor.submit(Callable {
                    val content = if (index % 2 == 0) readable else List(10) { "repeat" }.joinToString(" ")
                    ValidationUtils.isContentLowQuality(content)
                })
            }
            futures.forEachIndexed { index, result ->
                assertEquals(if (index % 2 == 0) accepted else repetitive, result.get(10, TimeUnit.SECONDS))
            }
        } finally {
            executor.shutdownNow()
        }
    }

    private fun asTokens(letters: String): String = letters.chunked(20).joinToString(" ") + " 12345678901234567890"
}
