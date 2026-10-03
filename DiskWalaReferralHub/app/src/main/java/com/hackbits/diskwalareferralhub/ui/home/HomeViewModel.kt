package com.hackbits.diskwalareferralhub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hackbits.diskwalareferralhub.data.UnavailableReferralStatsRepository
import com.hackbits.diskwalareferralhub.domain.model.StatsResult
import com.hackbits.diskwalareferralhub.domain.usecase.GetReferralStatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val result: StatsResult? = null,
)

class HomeViewModel(private val getStats: GetReferralStatsUseCase) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init { refresh() }

    fun refresh() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = getStats()
            _state.update { HomeUiState(isLoading = false, result = result) }
        }
    }

    companion object {
        /** Tiny manual wiring; swap in Hilt/Koin if the app grows. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                HomeViewModel(GetReferralStatsUseCase(UnavailableReferralStatsRepository()))
            }
        }
    }
}
