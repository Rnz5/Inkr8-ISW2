package com.inkr8.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.Timestamp
import com.inkr8.data.Words
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest

object DraftManager {
    private const val PREFS_NAME = "inkr8_drafts"
    
    private fun getPrefs(context: Context) = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveDraft(context: Context, key: String, text: String) {
        getPrefs(context).edit().putString(key, text).apply()
    }

    fun getDraft(context: Context, key: String): String {
        return getPrefs(context).getString(key, "") ?: ""
    }

    fun clearDraft(context: Context, key: String) {
        getPrefs(context).edit().remove(key).apply()
    }

    fun getDraftKey(gamemode: String, playmode: String, tournamentId: String?): String {
        return "draft_${gamemode}_${playmode}_${tournamentId ?: "none"}"
    }

    // Legacy keys are deliberately retained above, with no inferred owner/migration.
    fun getExerciseKey(
        userId: String, gamemode: String, playmode: String, tournamentId: String?,
        themeId: String? = null, topicId: String? = null
    ): String? = if (userId.isBlank()) null else "exercise_v2_" + fingerprint(
        listOf(userId, gamemode, playmode, tournamentId, themeId, topicId)
    )

    fun getScopedDraftKey(exerciseKey: String, words: List<Words>): String =
        "draft_v2_" + fingerprint(listOf(exerciseKey) + words.map {
            if (it.id.isNotBlank()) "id:${it.id}" else "word:${it.word}"
        })

    private fun fingerprint(parts: List<String?>): String {
        val encoded = parts.joinToString("") { if (it == null) "-1:" else "${it.length}:$it" }
        return MessageDigest.getInstance("SHA-256").digest(encoded.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }

    fun getRevision(context: Context, key: String): Long =
        getPrefs(context).getLong("revision_$key", 0L)

    fun recordRevision(context: Context, key: String): Long {
        val next = getRevision(context, key) + 1L
        getPrefs(context).edit().putLong("revision_$key", next).apply()
        return next
    }

    fun getConfirmedRevision(context: Context, key: String): Long =
        getPrefs(context).getLong("confirmed_$key", -1L)

    fun confirmRevision(context: Context, key: String, revision: Long): Boolean {
        if (getRevision(context, key) != revision) return false
        getPrefs(context).edit().remove(key).putLong("confirmed_$key", revision).apply()
        return true
    }

    // A persistence callback can outlive its original composition. Notify the
    // currently mounted editor of the same scoped revision, without owning UI state.
    fun observeConfirmation(context: Context, key: String, onConfirmed: (Long) -> Unit): () -> Unit {
        val prefs = getPrefs(context)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changed ->
            if (changed == "confirmed_$key") onConfirmed(getConfirmedRevision(context, key))
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        return { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    // Store the existing assignment, not a new exercise ID or backend schema.
    fun saveExerciseWords(context: Context, exerciseKey: String, words: List<Words>) {
        val array = JSONArray()
        words.forEach { w -> array.put(JSONObject().apply {
            put("id", w.id); put("word", w.word); put("type", w.type)
            put("definition", w.definition); put("pronunciation", w.pronunciation)
            put("sentence", w.sentence); put("frequencyScore", w.frequencyScore)
            put("isActive", w.isActive); put("randomIndex", w.randomIndex)
            w.createdAt?.let { put("createdSeconds", it.seconds); put("createdNanos", it.nanoseconds) }
        }) }
        getPrefs(context).edit().putString(exerciseKey, array.toString()).apply()
    }

    fun getExerciseWords(context: Context, exerciseKey: String): List<Words>? {
        val stored = getPrefs(context).getString(exerciseKey, null) ?: return null
        return runCatching {
            val array = JSONArray(stored)
            List(array.length()) { index ->
                val w = array.getJSONObject(index)
                Words(id = w.getString("id"), word = w.getString("word"), type = w.getString("type"),
                    definition = w.getString("definition"), pronunciation = w.getString("pronunciation"),
                    sentence = w.getString("sentence"), frequencyScore = w.getInt("frequencyScore"),
                    isActive = w.getBoolean("isActive"), randomIndex = w.getDouble("randomIndex"),
                    createdAt = if (w.has("createdSeconds")) Timestamp(w.getLong("createdSeconds"), w.getInt("createdNanos")) else null)
            }
        }.getOrNull()
    }

    fun releaseExercise(context: Context, exerciseKey: String) {
        getPrefs(context).edit().remove(exerciseKey).apply()
    }
}
