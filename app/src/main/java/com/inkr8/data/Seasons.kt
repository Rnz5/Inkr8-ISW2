package com.inkr8.data

data class SeasonMember(val userId: String, val name: String, val rating: Long, val position: Long, val meritEarned: Long)
data class SeasonRanking(val id: String, val status: String, val members: List<SeasonMember>)
data class SeasonHistoryEntry(val id: String, val position: Long, val rating: Long, val meritEarned: Long)
