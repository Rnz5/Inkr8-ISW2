package com.inkr8.domain.model

enum class PlayMode { PRACTICE, RANKED }
enum class WritingMode(val requiredWords: Int, val minWords: Int, val maxWords: Int) {
    STANDARD(4, 50, 150), ON_TOPIC(2, 50, 200)
}
enum class GameStatus { DRAFT, PENDING, EVALUATED, FAILED }
enum class MatchStatus { NONE, PENDING, MATCHED, EXPIRED }
data class User(val id: String, val name: String, val email: String?, val merit: Long, val rating: Long, val league: Int)
data class Word(val text: String, val definition: String, val sentence: String)
data class Season(val id: String, val start: Long, val end: Long)
data class SeasonMember(val userId: String, val name: String, val rating: Long, val league: Int)
data class SeasonRanking(val season: Season, val members: List<SeasonMember>)
data class Home(val word: Word, val season: Season, val rankedCost: Long)
data class Challenge(val words: List<Word>, val topic: String?, val theme: String?)
data class Evaluation(val score: Double, val feedback: String, val meritEarned: Long)
data class MatchResult(val opponentName: String, val opponentScore: Double, val outcome: String, val ratingChange: Long)
data class Game(
    val id: String, val mode: PlayMode, val writingMode: WritingMode,
    val challenge: Challenge, val status: GameStatus, val content: String,
    val evaluation: Evaluation?, val match: MatchResult?, val error: String?, val seasonId: String,
    val matchStatus: MatchStatus = MatchStatus.NONE
)
