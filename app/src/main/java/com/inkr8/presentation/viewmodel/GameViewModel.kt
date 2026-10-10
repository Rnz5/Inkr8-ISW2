package com.inkr8.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inkr8.domain.model.*
import com.inkr8.domain.policy.GamePolicy
import com.inkr8.domain.repository.Draft
import com.inkr8.domain.usecase.*
import com.inkr8.presentation.state.GameUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class GameViewModel(
    private val userId: String, private val mode: PlayMode, private val startGame: StartGame,
    private val submit: SubmitWriting, private val observeGame: ObserveGame, private val recentGames: GetRecentGames,
    private val retry: RetryEvaluation,
    private val loadDraft: LoadDraft, private val saveDraft: SaveDraft,
    private val clearDraft: ClearDraft, private val findDraft: FindDraft
) : ViewModel() {
    private val mutableState = MutableStateFlow(GameUiState())
    val state = mutableState.asStateFlow()
    private var gameJob: Job? = null
    init {
        loadDraft(userId, mode)?.let { draft ->
            updateState { GameUiState(requestId = draft.id, writingMode = draft.writingMode, text = draft.text) }
            watch(draft.id)
        }
        refreshRecent()
    }
    fun select(writingMode: WritingMode) {
        if (state.value.requestId == null && !state.value.busy) updateState { it.copy(writingMode = writingMode) }
    }
    fun edit(text: String) {
        if (state.value.game?.status != GameStatus.DRAFT) return
        updateState { it.copy(text = text.take(10000), error = null) }
        persist()
    }
    private fun updateState(transform: (GameUiState) -> GameUiState) {
        mutableState.update { current ->
            val next = transform(current)
            next.copy(
                wordCount = GamePolicy.wordCount(next.text),
                canSubmit = next.game?.let { game ->
                    game.status == GameStatus.DRAFT && GamePolicy.validateWriting(next.text, game.writingMode) == null
                } ?: false
            )
        }
    }
    private fun persist() { state.value.requestId?.let { saveDraft(userId, mode, Draft(it, state.value.writingMode, state.value.text)) } }
    fun start() {
        if (state.value.busy || state.value.game != null) return
        val id = state.value.requestId ?: UUID.randomUUID().toString()
        updateState { it.copy(requestId = id, busy = true, error = null) }
        persist()
        viewModelScope.launch {
            try {
                val game = startGame(id, mode, state.value.writingMode)
                updateState { it.copy(game = game, busy = false, error = null) }
                watch(id)
            } catch (error: CancellationException) { throw error }
            catch (error: Exception) { fail(error) }
        }
    }
    fun send() {
        val game = state.value.game ?: return
        if (state.value.busy || !state.value.canSubmit) return
        updateState { it.copy(busy = true, error = null) }
        viewModelScope.launch {
            try { submit(game, state.value.text); updateState { it.copy(busy = false) }; refreshRecent() }
            catch (error: CancellationException) { throw error }
            catch (error: Exception) { fail(error) }
        }
    }
    fun retryEvaluation() {
        val id = state.value.game?.id ?: return
        if (state.value.busy) return
        updateState { it.copy(busy = true, error = null) }
        viewModelScope.launch {
            try { retry(id); updateState { it.copy(busy = false) }; watch(id) }
            catch (error: CancellationException) { throw error }
            catch (error: Exception) { fail(error) }
        }
    }
    fun open(game: Game) {
        if (state.value.busy) return
        val text = if (game.status == GameStatus.DRAFT) findDraft(userId, game.id)?.text ?: game.content else game.content
        updateState { it.copy(game = game, requestId = game.id, writingMode = game.writingMode, text = text, error = null) }
        persist(); watch(game.id)
    }
    fun newGame() {
        if (state.value.busy) return
        gameJob?.cancel(); clearDraft(userId, mode)
        updateState { GameUiState(recent = it.recent) }
        refreshRecent()
    }
    fun reconnect() { state.value.requestId?.let { watch(it) }; refreshRecent() }
    private fun watch(id: String) {
        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            try { observeGame(id).collect { game ->
                updateState { it.copy(game = game, writingMode = game.writingMode,
                    text = if (game.status == GameStatus.DRAFT) it.text else game.content, error = null) }
            } } catch (error: CancellationException) { throw error }
            catch (error: Exception) { fail(error) }
        }
    }
    fun refreshRecent() { viewModelScope.launch {
        updateState { it.copy(loadingRecent = true) }
        try {
            val recent = recentGames().filter { game -> game.mode == mode }
            updateState { it.copy(recent = recent, loadingRecent = false) }
        }
        catch (error: CancellationException) { throw error }
        catch (error: Exception) { updateState { it.copy(loadingRecent = false, error = error.message ?: "No se pudieron cargar las partidas.") } }
    } }
    private fun fail(error: Exception) { updateState { it.copy(busy = false, error = error.message ?: "No se pudo completar la operación.") } }
}
