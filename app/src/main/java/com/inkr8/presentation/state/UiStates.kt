package com.inkr8.presentation.state

import com.inkr8.domain.model.*

data class SessionUiState(val user: User? = null, val signedIn: Boolean = false, val loading: Boolean = true, val error: String? = null)
data class HomeUiState(val home: Home? = null, val loading: Boolean = true, val error: String? = null)
data class ProfileUiState(val saving: Boolean = false, val error: String? = null, val saved: Boolean = false)
data class RankingUiState(val ranking: SeasonRanking? = null, val loading: Boolean = true, val error: String? = null)
data class GameUiState(
    val game: Game? = null, val text: String = "", val writingMode: WritingMode = WritingMode.STANDARD,
    val busy: Boolean = false, val loadingRecent: Boolean = false, val recent: List<Game> = emptyList(),
    val error: String? = null, val requestId: String? = null,
    val wordCount: Int = 0, val canSubmit: Boolean = false
)
enum class Destination(val title: String) { HOME("Inicio"), PRACTICE("Práctica"), RANKED("Ranked"), PROFILE("Perfil") }
