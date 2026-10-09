package com.inkr8.viewmodel

import com.inkr8.data.SubmissionStatus

// Existing scalar wait decisions; scheduling, snapshot reads and effects stay in AppViewModel.
internal fun resultWaitElapsedSeconds(pollCount: Int): Int = pollCount * 3

internal fun hasResultWaitTimedOut(loadingElapsedSeconds: Int): Boolean = loadingElapsedSeconds > 90

internal fun isEvaluatedResult(status: SubmissionStatus): Boolean = status == SubmissionStatus.EVALUATED

internal fun isFailedResult(status: SubmissionStatus): Boolean = status == SubmissionStatus.FAILED
