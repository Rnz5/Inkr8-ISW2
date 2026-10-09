package com.inkr8.mappers

import com.inkr8.data.*
import com.inkr8.repository.FirestoreSubmission
import com.google.firebase.firestore.DocumentSnapshot

fun FirestoreSubmission.toDomain(gamemode: String? = null): Submissions {
    return Submissions(
        id = id,
        authorId = authorId,
        content = content,
        timestamp = timestamp,
        wordCount = wordCount,
        characterCount = characterCount,
        wordsUsed = wordsUsed,
        gamemode = gamemode ?: gamemodeName,
        playmode = playmode,
        topicId = topicId,
        themeId = themeId,
        isSaved = isSaved,
        evaluation = evaluation?.toDomain(),
        status = try { SubmissionStatus.valueOf(status) } catch (e: Exception) { SubmissionStatus.PENDING },
        matchStatus = matchStatus,
        matchResult = matchResult?.let { map ->
            MatchResult(
                opponentId = map["opponentId"] as? String ?: "",
                opponentName = map["opponentName"] as? String ?: "",
                opponentScore = (map["opponentScore"] as? Number)?.toDouble() ?: 0.0,
                outcome = map["outcome"] as? String ?: "",
                ratingChange = (map["ratingChange"] as? Number)?.toLong() ?: 0L
            )
        }
    )
}

fun Submissions.toFirestore(): FirestoreSubmission {
    return FirestoreSubmission(
        id = id,
        authorId = authorId,
        content = content,
        timestamp = timestamp,
        wordCount = wordCount,
        characterCount = characterCount,
        wordsUsed = wordsUsed,
        gamemodeName = gamemode,
        playmode = playmode,
        topicId = topicId,
        themeId = themeId,
        isSaved = isSaved,
        evaluation = evaluation?.toFirestore(),
        status = status.name,
        matchStatus = matchStatus,
        matchResult = matchResult?.let {
            mapOf(
                "opponentId" to it.opponentId,
                "opponentName" to it.opponentName,
                "opponentScore" to it.opponentScore,
                "outcome" to it.outcome,
                "ratingChange" to it.ratingChange
            )
        }
    )
}

// Read compatibility belongs at the snapshot boundary; the DTO writer stays unchanged.
// Non-string/null primary fields keep Android's old gamemodeName behavior.
fun DocumentSnapshot.toSubmission(): Submissions? =
    toObject(FirestoreSubmission::class.java)?.copy(id = id)?.toDomain(
        gamemode = get("gamemode") as? String
    )
