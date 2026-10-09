package com.inkr8.lab

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.inkr8.data.*
import com.inkr8.utils.DraftManager

// Seed existing assignment data explicitly; no random-word or preference double.
object LabDraftFixtures {
    fun exercise(uid: String = FirebaseAuth.getInstance().currentUser!!.uid,
        mode: Gamemode = StandardWriting, play: PlayMode = PlayMode.Practice): String =
        DraftManager.getExerciseKey(uid, if (mode is OnTopicWriting) "ON_TOPIC" else "STANDARD",
            when(play) {PlayMode.Practice -> "PRACTICE"; PlayMode.Ranked -> "RANKED"},
            null, (mode as? OnTopicWriting)?.theme?.id,
            (mode as? OnTopicWriting)?.topic?.id)!!
    fun seed(context: Context, text: String, uid: String = FirebaseAuth.getInstance().currentUser!!.uid,
        mode: Gamemode = StandardWriting, play: PlayMode = PlayMode.Practice,
        words: List<Words> = emptyList()): String {
        val scope = exercise(uid, mode, play)
        DraftManager.saveExerciseWords(context, scope, words)
        val key = DraftManager.getScopedDraftKey(scope, words)
        DraftManager.saveDraft(context, key, text)
        return key
    }
}
