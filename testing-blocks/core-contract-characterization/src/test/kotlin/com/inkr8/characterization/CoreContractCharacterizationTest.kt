package com.inkr8.characterization

import com.inkr8.data.SubmissionStatus
import com.inkr8.viewmodel.*
import org.junit.Assert.*
import org.junit.Test

class CoreContractCharacterizationTest {
    @Test fun practiceKeyUsesOnlySuppliedModeFields() {
        assertEquals("draft_STANDARD_PRACTICE_none", DraftKeySourceProbe.getDraftKey("STANDARD", "PRACTICE", null))
    }
    @Test fun rankedAndPracticeKeysAreDistinct() {
        assertNotEquals(DraftKeySourceProbe.getDraftKey("STANDARD", "PRACTICE", null),
            DraftKeySourceProbe.getDraftKey("STANDARD", "RANKED", null))
    }
    @Test fun standardAndOnTopicKeysAreDistinct() {
        assertNotEquals(DraftKeySourceProbe.getDraftKey("STANDARD", "PRACTICE", null),
            DraftKeySourceProbe.getDraftKey("ON_TOPIC", "PRACTICE", null))
    }
    @Test fun nullAndLiteralNoneAlias() {
        assertEquals(DraftKeySourceProbe.getDraftKey("STANDARD", "RANKED", null),
            DraftKeySourceProbe.getDraftKey("STANDARD", "RANKED", "none"))
    }
    @Test fun emptyTournamentIdIsNotNullFallback() {
        assertEquals("draft_STANDARD_RANKED_", DraftKeySourceProbe.getDraftKey("STANDARD", "RANKED", ""))
    }
    @Test fun keyPreservesCaseAndSeparators() {
        assertEquals("draft_standard_ranked_a_b", DraftKeySourceProbe.getDraftKey("standard", "ranked", "a_b"))
    }
    @Test fun unescapedSeparatorsCanAliasInputs() {
        assertEquals(DraftKeySourceProbe.getDraftKey("A_B", "C", "D"), DraftKeySourceProbe.getDraftKey("A", "B_C", "D"))
    }
    @Test fun waitAcceptsOnlyWhenNeitherResolvedNorTimedOut() {
        assertTrue(WaitSourceProbe.acceptsUpdate(false, false))
        assertFalse(WaitSourceProbe.acceptsUpdate(true, false))
        assertFalse(WaitSourceProbe.acceptsUpdate(false, true))
        assertFalse(WaitSourceProbe.acceptsUpdate(true, true))
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
    @Test fun placementRevealNeedsAllThreeAuthenticConditions() {
        assertTrue(WaitSourceProbe.revealsPlacement(false, PlacementFixture(true, false)))
        assertFalse(WaitSourceProbe.revealsPlacement(true, PlacementFixture(true, false)))
        assertFalse(WaitSourceProbe.revealsPlacement(false, PlacementFixture(false, false)))
        assertFalse(WaitSourceProbe.revealsPlacement(false, PlacementFixture(true, true)))
    }
    @Test fun arrivalOrderChangesPlacementDecisionForSamePreviousFlag() {
        assertFalse(WaitSourceProbe.revealsPlacement(false, PlacementFixture(false, false)))
        assertTrue(WaitSourceProbe.revealsPlacement(false, PlacementFixture(true, false)))
    }
}
