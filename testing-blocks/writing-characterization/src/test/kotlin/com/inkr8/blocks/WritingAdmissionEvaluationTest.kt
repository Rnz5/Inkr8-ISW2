package com.inkr8.blocks

import com.inkr8.data.AdmissionModeFixture
import com.inkr8.evaluation.isWritingAdmitted
import com.inkr8.evaluation.originalAdmission
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** Access order is part of the existing derived-state expression, not new admission rules. */
class WritingAdmissionEvaluationTest {
    @Test fun blankTextDoesNotReadBoundsOrCount() {
        for (text in listOf("", " ", "\t\r\n")) {
            val trace = mutableListOf<String>()
            assertFalse(isWritingAdmitted(text, AdmissionModeFixture(50, 150, trace)) {
                error("Blank text must not read count")
            })
            assertEquals(emptyList(), trace)
        }
    }

    @Test fun twoNullBoundsDoNotReadCount() {
        val trace = mutableListOf<String>()
        assertTrue(isWritingAdmitted("text", AdmissionModeFixture(null, null, trace)) {
            error("Null bounds must not read count")
        })
        assertEquals(listOf("min", "max"), trace)
    }

    @Test fun onlyMinimumReadsCountBeforeMaximum() {
        val trace = mutableListOf<String>()
        assertTrue(isWritingAdmitted("text", AdmissionModeFixture(50, null, trace)) {
            trace.add("count"); 50
        })
        assertEquals(listOf("min", "count", "max"), trace)
    }

    @Test fun onlyMaximumReadsCountAfterMinimum() {
        val trace = mutableListOf<String>()
        assertTrue(isWritingAdmitted("text", AdmissionModeFixture(null, 150, trace)) {
            trace.add("count"); 150
        })
        assertEquals(listOf("min", "max", "count"), trace)
    }

    @Test fun failedMinimumStillEvaluatesMaximum() {
        val trace = mutableListOf<String>()
        assertFalse(isWritingAdmitted("text", AdmissionModeFixture(50, 150, trace)) {
            trace.add("count"); 49
        })
        assertEquals(listOf("min", "count", "max", "count"), trace)
    }

    @Test fun eachNonnullBoundReadsCountSeparately() {
        val trace = mutableListOf<String>()
        var reads = 0
        assertTrue(isWritingAdmitted("text", AdmissionModeFixture(50, 150, trace)) {
            trace.add("count"); if (reads++ == 0) 50 else 150
        })
        assertEquals(2, reads)
        assertEquals(listOf("min", "count", "max", "count"), trace)
    }

    @Test fun countExceptionPreservesEvaluationCutoff() {
        val trace = mutableListOf<String>()
        assertFailsWith<IllegalStateException> {
            isWritingAdmitted("text", AdmissionModeFixture(50, 150, trace)) {
                trace.add("count"); error("count failure")
            }
        }
        assertEquals(listOf("min", "count"), trace)
    }

    @Test fun productionMatchesHistoricalExpressionAndAccessTrace() {
        // 3 texts x 4 nullable minima x 4 nullable maxima x 5 counts = 240 comparisons.
        for (text in listOf("", "\t ", "text")) {
            for (min in listOf(null, 0, 1, 50)) for (max in listOf(null, 0, 1, 50)) {
                for (count in listOf(0, 1, 49, 50, 51)) {
                    val before = mutableListOf<String>()
                    val after = mutableListOf<String>()
                    val expected = originalAdmission(text, AdmissionModeFixture(min, max, before)) {
                        before.add("count"); count
                    }
                    val actual = isWritingAdmitted(text, AdmissionModeFixture(min, max, after)) {
                        after.add("count"); count
                    }
                    assertEquals(expected, actual, "text=$text min=$min max=$max count=$count")
                    assertEquals(before, after)
                }
            }
        }
    }
}
