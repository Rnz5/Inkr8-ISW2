package com.inkr8.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inkr8.domain.usecase.*
import com.inkr8.presentation.state.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val getHome: GetHome) : ViewModel() {
    private val mutableState = MutableStateFlow(HomeUiState())
    val state = mutableState.asStateFlow()
    private var refreshJob: Job? = null
    init { refresh() }
    fun refresh() { refreshJob?.cancel(); refreshJob = viewModelScope.launch {
        mutableState.update { it.copy(loading = true, error = null) }
        try { mutableState.value = HomeUiState(home = getHome(), loading = false) }
        catch (error: CancellationException) { throw error }
        catch (error: Exception) { mutableState.update { it.copy(loading = false, error = error.message ?: "No se pudo cargar el inicio.") } }
    } }
}
class RankingViewModel(private val getRanking: GetSeasonRanking) : ViewModel() {
    private val mutableState = MutableStateFlow(RankingUiState())
    val state = mutableState.asStateFlow()
    private var refreshJob: Job? = null
    init { refresh() }
    fun refresh() { refreshJob?.cancel(); refreshJob = viewModelScope.launch {
        mutableState.update { it.copy(loading = true, error = null) }
        try { mutableState.value = RankingUiState(ranking = getRanking(), loading = false) }
        catch (error: CancellationException) { throw error }
        catch (error: Exception) { mutableState.update { it.copy(loading = false, error = error.message ?: "No se pudo cargar la temporada.") } }
    } }
}
class ProfileViewModel(private val updateName: UpdateName) : ViewModel() {
    private val mutableState = MutableStateFlow(ProfileUiState())
    val state = mutableState.asStateFlow()
    fun save(name: String) {
        if (state.value.saving) return
        mutableState.value = ProfileUiState(saving = true)
        viewModelScope.launch {
            try { updateName(name); mutableState.value = ProfileUiState(saved = true) }
            catch (error: CancellationException) { throw error }
            catch (error: Exception) { mutableState.value = ProfileUiState(error = error.message ?: "No se pudo actualizar el nombre.") }
        }
    }
}
