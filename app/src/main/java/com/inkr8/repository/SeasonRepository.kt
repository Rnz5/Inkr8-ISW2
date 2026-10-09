package com.inkr8.repository

import com.google.firebase.functions.FirebaseFunctions
import com.inkr8.data.*
import kotlinx.coroutines.tasks.await

class SeasonRepository(private val functions: FirebaseFunctions = FirebaseFunctions.getInstance()) {
    private fun Map<*, *>.number(key: String): Long = (get(key) as Number).toLong()
    suspend fun ranking(): SeasonRanking {
        val result = functions.getHttpsCallable("getSeasonRanking").call().await().data as Map<*, *>
        val members = (result["members"] as List<*>).map { raw ->
            val member = raw as Map<*, *>
            SeasonMember(member["userId"] as String, member["name"] as String,
                member.number("rating"), member.number("position"), member.number("meritEarned"))
        }
        return SeasonRanking(result["id"] as String, result["status"] as String, members)
    }
    suspend fun history(): List<SeasonHistoryEntry> {
        val result = functions.getHttpsCallable("getSeasonHistory").call().await().data as Map<*, *>
        return (result["history"] as List<*>).map { raw ->
            val row = raw as Map<*, *>
            SeasonHistoryEntry(row["id"] as String, row.number("position"), row.number("rating"), row.number("meritEarned"))
        }
    }
}
