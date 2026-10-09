package com.inkr8.evaluation

import com.inkr8.data.Gamemode

// Count is read only in the same nullable-limit branches as the original derived state.
internal fun isWritingAdmitted(userText: String, gamemode: Gamemode, wordCount: () -> Int): Boolean {
    return if (userText.isBlank()) false
    else {
        val meetsMinWords = gamemode.minWords?.let { wordCount() >= it } ?: true
        val meetsMaxWords = gamemode.maxWords?.let { wordCount() <= it } ?: true
        meetsMinWords && meetsMaxWords
    }
}
