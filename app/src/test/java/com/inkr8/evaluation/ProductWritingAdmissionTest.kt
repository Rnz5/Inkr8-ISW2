package com.inkr8.evaluation

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot
import com.inkr8.data.Gamemode
import com.inkr8.data.OnTopicWriting
import com.inkr8.data.StandardWriting
import com.inkr8.data.Theme
import com.inkr8.data.Topic
import org.junit.Assert.*
import org.junit.Test

/** Actual app modes and Compose snapshot runtime, not a screen/Firebase or recomposition test. */
class ProductWritingAdmissionTest {
    private val modes = listOf(StandardWriting, OnTopicWriting(Theme(id = "test-theme"), Topic(id = "test-topic")))

    // Unchanged expression from Git HEAD Writing.kt, kept only as a comparison oracle.
    private fun before(text: String, mode: Gamemode, count: () -> Int): Boolean {
        return if (text.isBlank()) false
        else {
            val meetsMinWords = mode.minWords?.let { count() >= it } ?: true
            val meetsMaxWords = mode.maxWords?.let { count() <= it } ?: true
            meetsMinWords && meetsMaxWords
        }
    }

    @Test fun actualModesPreserveBlankAndInclusiveBounds() {
        for (mode in modes) {
            for (text in listOf("", " ", "\t\r\n")) {
                assertFalse(isWritingAdmitted(text, mode) { error("Blank text read count") })
            }
            val minimum = mode.minWords!!
            val maximum = mode.maxWords!!
            for (count in listOf(minimum - 1, minimum, maximum, maximum + 1)) {
                val expected = count in minimum..maximum
                assertEquals(expected, isWritingAdmitted("text", mode) { count })
            }
        }
    }

    @Test fun countReadOrderMatchesBeforeOnActualModes() {
        for (mode in modes) for (text in listOf("", "\t ", "text")) {
            for (count in listOf(0, 49, 50, 150, 151, 200, 201)) {
                val oldReads = mutableListOf<Int>()
                val newReads = mutableListOf<Int>()
                val expected = before(text, mode) { oldReads.add(count); count }
                val actual = isWritingAdmitted(text, mode) { newReads.add(count); count }
                assertEquals(expected, actual)
                assertEquals(oldReads, newReads)
            }
        }
    }

    @Test fun composeDerivedStateObservesDeferredCountAndText() {
        val text = mutableStateOf("text")
        val count = mutableStateOf(49)
        val oldState = derivedStateOf { before(text.value, StandardWriting) { count.value } }
        val newState = derivedStateOf { isWritingAdmitted(text.value, StandardWriting) { count.value } }
        for ((newText, newCount, expected) in listOf(
            Triple("text", 49, false), Triple("text", 50, true),
            Triple("text", 151, false), Triple("text", 150, true),
            Triple("", 150, false), Triple("text", 150, true)
        )) {
            Snapshot.withMutableSnapshot { text.value = newText; count.value = newCount }
            assertEquals(expected, oldState.value)
            assertEquals(oldState.value, newState.value)
        }
    }

    @Test fun composeBlankStateDoesNotObserveOrReadCount() {
        val text = mutableStateOf("")
        val count = mutableStateOf(49)
        var reads = 0
        val state = derivedStateOf {
            isWritingAdmitted(text.value, StandardWriting) { reads++; count.value }
        }
        assertFalse(state.value)
        Snapshot.withMutableSnapshot { count.value = 50 }
        assertFalse(state.value)
        assertEquals(0, reads)
        Snapshot.withMutableSnapshot { text.value = "text" }
        assertTrue(state.value)
        assertTrue(reads > 0)
        Snapshot.withMutableSnapshot { count.value = 151 }
        assertFalse(state.value)
    }
}
