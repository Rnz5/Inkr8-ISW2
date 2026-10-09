package com.inkr8.blocks

import com.inkr8.data.Gamemode
import com.inkr8.data.OnTopicWriting
import com.inkr8.data.StandardWriting
import com.inkr8.data.Theme
import com.inkr8.data.Topic
import com.inkr8.evaluation.isWritingAdmitted
import com.inkr8.utils.ValidationUtils
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Existing expression behavior, not new acceptance rules or an Android UI test. */
class WritingCharacterizationTest {
    private val onTopic = OnTopicWriting(Theme(id = "theme-fixture"), Topic(id = "topic-fixture"))
    private fun canSubmit(text: String, mode: Gamemode = StandardWriting) =
        isWritingAdmitted(text, mode) { WritingSourceProbe.wordCount(text) }
    private fun paragraph(size: Int) = (listOf("Bright") + (1 until size).map { "word$it" }).joinToString(" ")
    private fun used(text: String, words: List<SelectedWordFixture>) =
        WritingSourceProbe.wordsUsed(WritingSourceProbe.normalizedUserWords(text), words)

    @Test
    fun `blank content counts zero and cannot submit`() {
        for (text in listOf("", " ", "\t\r\n")) {
            assertEquals(0, WritingSourceProbe.wordCount(text))
            assertEquals(emptySet(), WritingSourceProbe.normalizedUserWords(text))
            assertFalse(canSubmit(text))
        }
    }

    @Test
    fun `word count splits ASCII whitespace and trims ends`() {
        assertEquals(3, WritingSourceProbe.wordCount(" \tBright\n\nrivers\r\ncarry \t"))
    }

    @Test
    fun `punctuation does not add whitespace counted words`() {
        assertEquals(2, WritingSourceProbe.wordCount("bright,rivers carry!"))
        assertEquals(setOf("bright", "rivers", "carry"), WritingSourceProbe.normalizedUserWords("bright,rivers carry!"))
    }

    @Test
    fun `internal nonbreaking spaces distinguish count and matching tokens`() {
        assertEquals(1, WritingSourceProbe.wordCount("bright\u00A0rivers"))
        assertEquals(setOf("bright", "rivers"), WritingSourceProbe.normalizedUserWords("bright\u00A0rivers"))
    }

    @Test
    fun `underscores remain inside matching tokens`() {
        assertEquals(setOf("bright_rivers"), WritingSourceProbe.normalizedUserWords("bright_rivers"))
        assertEquals(listOf("compound"), used("bright_rivers", listOf(
            SelectedWordFixture("single", "bright"), SelectedWordFixture("compound", "BRIGHT_RIVERS"),
        )).map { it.id })
    }

    @Test
    fun `apostrophes split matching tokens`() {
        assertEquals(setOf("writer", "s"), WritingSourceProbe.normalizedUserWords("Writer's"))
        assertEquals(listOf("root"), used("Writer's", listOf(
            SelectedWordFixture("possessive", "writer's"), SelectedWordFixture("root", "writer"),
        )).map { it.id })
    }

    @Test
    fun `default word boundary regex retains ASCII behavior with Unicode input`() {
        assertEquals(3, WritingSourceProbe.wordCount("CAFÉ naïve 中文"))
        assertEquals(setOf("caf", "na", "ve"), WritingSourceProbe.normalizedUserWords("CAFÉ naïve 中文"))
        assertEquals(listOf("ascii"), used("CAFÉ", listOf(
            SelectedWordFixture("accented", "café"), SelectedWordFixture("ascii", "caf"),
        )).map { it.id })
    }

    @Test
    fun `letters and digits remain together in matching tokens`() {
        assertEquals(setOf("r8", "42"), WritingSourceProbe.normalizedUserWords("R8, 42!"))
    }

    @Test
    fun `normalization is case insensitive and deduplicates text tokens`() {
        assertEquals(setOf("bright", "rivers"), WritingSourceProbe.normalizedUserWords("Bright BRIGHT bright Rivers"))
    }

    @Test
    fun `Standard word range keeps inclusive fifty and one hundred fifty boundaries`() {
        assertFalse(canSubmit(paragraph(49)))
        assertTrue(canSubmit(paragraph(50)))
        assertTrue(canSubmit(paragraph(150)))
        assertFalse(canSubmit(paragraph(151)))
    }

    @Test
    fun `On Topic word range keeps inclusive fifty and two hundred boundaries`() {
        assertFalse(canSubmit(paragraph(49), onTopic))
        assertTrue(canSubmit(paragraph(50), onTopic))
        assertTrue(canSubmit(paragraph(200), onTopic))
        assertFalse(canSubmit(paragraph(201), onTopic))
    }

    @Test
    fun `canSubmit can be true while existing quality filter rejects text`() {
        val text = List(50) { "repeat" }.joinToString(" ")
        assertTrue(canSubmit(text))
        assertEquals(true to "Repetitive content detected", ValidationUtils.isContentLowQuality(text))
    }

    @Test
    fun `quality filter acceptance does not imply canSubmit`() {
        val text = "Bright rivers carry patient stories across quiet valleys every morning."
        assertFalse(canSubmit(text))
        assertEquals(false to null, ValidationUtils.isContentLowQuality(text))
    }

    @Test
    fun `assigned words missing from valid sized text do not alter canSubmit`() {
        val text = paragraph(50)
        assertTrue(canSubmit(text))
        assertEquals(false to null, ValidationUtils.isContentLowQuality(text))
        assertEquals(emptyList(), used(text, listOf(SelectedWordFixture("missing", "river"))))
    }

    @Test
    fun `used words retain selected order rather than text order`() {
        val selected = listOf(SelectedWordFixture("r", "RIVERS"), SelectedWordFixture("b", "bright"))
        assertEquals(selected, used("Bright rivers", selected))
    }

    @Test
    fun `matching assigned duplicates retain their individual identifiers`() {
        val selected = listOf(
            SelectedWordFixture("one", "Bright"), SelectedWordFixture("two", "absent"),
            SelectedWordFixture("three", "rivers"), SelectedWordFixture("four", "BRIGHT"),
        )
        assertEquals(listOf("one", "three", "four"), used("Bright rivers", selected).map { it.id })
    }

    @Test
    fun `empty assigned list remains empty`() {
        assertEquals(emptyList(), used(paragraph(50), emptyList()))
    }

    @Test
    fun `assigned multiword phrases and empty strings do not match individual tokens`() {
        assertEquals(emptyList(), used("quiet valleys", listOf(
            SelectedWordFixture("phrase", "quiet valleys"), SelectedWordFixture("empty", ""),
        )))
    }

    @Test
    fun `authentic modes keep different upper bounds and assigned word counts`() {
        assertEquals(4, StandardWriting.requiredWords)
        assertEquals(2, onTopic.requiredWords)
        assertFalse(canSubmit(paragraph(175)))
        assertTrue(canSubmit(paragraph(175), onTopic))
    }

    @Test
    fun `concurrent expression calls keep independent counts and token sets`() {
        val executor = Executors.newFixedThreadPool(4)
        try {
            val futures = (0 until 100).map { index -> executor.submit(Callable {
                val text = if (index % 2 == 0) "Bright rivers" else "writer's bright_rivers"
                WritingSourceProbe.wordCount(text) to WritingSourceProbe.normalizedUserWords(text)
            }) }
            futures.forEachIndexed { index, result ->
                val expected = if (index % 2 == 0) 2 to setOf("bright", "rivers")
                    else 2 to setOf("writer", "s", "bright_rivers")
                assertEquals(expected, result.get(10, TimeUnit.SECONDS))
            }
        } finally {
            executor.shutdownNow()
        }
    }
}
