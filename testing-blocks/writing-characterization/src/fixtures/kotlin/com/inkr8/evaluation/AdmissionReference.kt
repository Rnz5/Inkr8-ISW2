package com.inkr8.evaluation

import com.inkr8.data.Gamemode

// Historical canSubmit body from IMPL-007 before snapshot; test-only lazy accessor substitution.
internal fun originalAdmission(userText: String, gamemode: Gamemode, wordCount: () -> Int): Boolean {
    return if (userText.isBlank()) false
    else {
        val meetsMinWords = gamemode.minWords?.let { wordCount() >= it } ?: true
        val meetsMaxWords = gamemode.maxWords?.let { wordCount() <= it } ?: true
        meetsMinWords && meetsMaxWords
    }
}
