package com.inkr8.data.mapper

import com.inkr8.data.dto.*
import com.inkr8.domain.model.*

internal fun UserDto.toDomain() = User(id, name, email, merit, rating, league)
internal fun WireMap.toWord() = Word(string("text"), string("definition"), string("sentence"))
internal fun WireMap.toSeason() = Season(string("id"), number("start"), number("end"))
internal fun WireMap.toHome() = Home(this["word"].wire().toWord(), this["season"].wire().toSeason(), number("rankedCost"))
internal fun WireMap.toRanking() = SeasonRanking(this["season"].wire().toSeason(), maps("members").map {
    SeasonMember(it.string("userId"), it.string("name"), it.number("rating"), it.number("league").toInt())
})
internal fun GameDto.toDomain(): Game {
    val challenge = fields["challenge"].wire()
    val evaluation = fields["evaluation"]?.wire()?.let {
        Evaluation((it["score"] as Number).toDouble(), it.string("feedback"), it.number("meritEarned"))
    }
    val match = fields["match"]?.wire()?.let {
        MatchResult(it.string("opponentName"), (it["opponentScore"] as Number).toDouble(), it.string("outcome"), it.number("ratingChange"))
    }
    return Game(id, PlayMode.valueOf(fields.string("mode")), WritingMode.valueOf(fields.string("writingMode")),
        Challenge(challenge.maps("words").map { it.toWord() }, challenge["topic"] as? String, challenge["theme"] as? String),
        GameStatus.valueOf(fields.string("status")), fields.string("content"), evaluation, match,
        fields["error"] as? String, "s${fields.number("seasonIndex")}",
        MatchStatus.valueOf(fields["matchStatus"] as? String ?: "NONE"))
}
