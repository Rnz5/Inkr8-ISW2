package com.inkr8.characterization

import com.inkr8.data.SubmissionStatus
import com.inkr8.utils.DraftManager
import com.inkr8.viewmodel.*
import org.junit.Assert.*
import org.junit.Test

/** Twelve existing IMPL-005/008 scenarios, now using actual app classes; no Firebase IO. */
class ProductCoreContractTest {
    @Test fun practiceKeyUsesOnlySuppliedModeFields() {
        assertEquals("draft_STANDARD_PRACTICE_none", DraftManager.getDraftKey("STANDARD", "PRACTICE", null))
    }
    @Test fun rankedAndPracticeKeysAreDistinct() {
        assertNotEquals(DraftManager.getDraftKey("STANDARD", "PRACTICE", null),
            DraftManager.getDraftKey("STANDARD", "RANKED", null))
    }
    @Test fun standardAndOnTopicKeysAreDistinct() {
        assertNotEquals(DraftManager.getDraftKey("STANDARD", "PRACTICE", null),
            DraftManager.getDraftKey("ON_TOPIC", "PRACTICE", null))
    }
    @Test fun nullAndLiteralNoneAlias() {
        assertEquals(DraftManager.getDraftKey("STANDARD", "RANKED", null),
            DraftManager.getDraftKey("STANDARD", "RANKED", "none"))
    }
    @Test fun emptyTournamentIdIsNotNullFallback() {
        assertEquals("draft_STANDARD_RANKED_", DraftManager.getDraftKey("STANDARD", "RANKED", ""))
    }
    @Test fun keyPreservesCaseAndSeparators() {
        assertEquals("draft_standard_ranked_a_b", DraftManager.getDraftKey("standard", "ranked", "a_b"))
    }
    @Test fun unescapedSeparatorsCanAliasInputs() {
        assertEquals(DraftManager.getDraftKey("A_B", "C", "D"), DraftManager.getDraftKey("A", "B_C", "D"))
    }
    @Test fun evaluatedPredicateUsesAuthenticEnum() {
        assertTrue(isEvaluatedResult(SubmissionStatus.EVALUATED))
        assertFalse(isEvaluatedResult(SubmissionStatus.PENDING))
        assertFalse(isEvaluatedResult(SubmissionStatus.NOT_EVALUABLE))
        assertFalse(isEvaluatedResult(SubmissionStatus.FAILED))
    }
    @Test fun failedPredicateDoesNotTreatNotEvaluableAsFailure() {
        assertTrue(isFailedResult(SubmissionStatus.FAILED))
        assertFalse(isFailedResult(SubmissionStatus.NOT_EVALUABLE))
        assertFalse(isFailedResult(SubmissionStatus.EVALUATED))
        assertFalse(isFailedResult(SubmissionStatus.PENDING))
    }
    @Test fun timeoutIsFalseAtNinetySeconds() { assertFalse(hasResultWaitTimedOut(90)) }
    @Test fun timeoutIsTrueAfterNinetySeconds() { assertTrue(hasResultWaitTimedOut(91)) }
    @Test fun firstTimedOutPollCounterIsThirtyOne() {
        assertEquals(90, resultWaitElapsedSeconds(30))
        assertFalse(hasResultWaitTimedOut(resultWaitElapsedSeconds(30)))
        assertEquals(93, resultWaitElapsedSeconds(31))
        assertTrue(hasResultWaitTimedOut(resultWaitElapsedSeconds(31)))
    }

    @Test fun scopedOwnerAndExerciseKeysAreStableAndDistinct() {
        val a=DraftManager.getExerciseKey("owner-A","STANDARD","PRACTICE",null)!!
        assertEquals(a,DraftManager.getExerciseKey("owner-A","STANDARD","PRACTICE",null))
        assertNotEquals(a,DraftManager.getExerciseKey("owner-B","STANDARD","PRACTICE",null))
        assertNotEquals(a,DraftManager.getExerciseKey("owner-A","STANDARD","RANKED",null))
        assertNotEquals(DraftManager.getExerciseKey("A_B","C","D",null),DraftManager.getExerciseKey("A","B_C","D",null))
        assertNotEquals(DraftManager.getExerciseKey("A","STANDARD","PRACTICE",null),DraftManager.getExerciseKey("A","STANDARD","PRACTICE",""))
        assertNull(DraftManager.getExerciseKey("","STANDARD","PRACTICE",null))
    }
    @Test fun scopedWordAndTopicIdentityUsesExistingOrderedData() {
        val a=DraftManager.getExerciseKey("owner","ON_TOPIC","PRACTICE",null,"theme","topic-a")!!
        val b=DraftManager.getExerciseKey("owner","ON_TOPIC","PRACTICE",null,"theme","topic-b")!!
        assertNotEquals(a,b)
        val words=listOf(com.inkr8.data.Words(id="existing-1",word="bright"),com.inkr8.data.Words(word="rivers"))
        assertEquals(DraftManager.getScopedDraftKey(a,words),DraftManager.getScopedDraftKey(a,words))
        assertNotEquals(DraftManager.getScopedDraftKey(a,words),DraftManager.getScopedDraftKey(a,words.reversed()))
        assertNotEquals(DraftManager.getScopedDraftKey(a,words),DraftManager.getScopedDraftKey(b,words))
    }
}
