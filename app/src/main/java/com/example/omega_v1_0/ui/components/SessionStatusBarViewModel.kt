package com.example.omega_v1_0.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.models.SessionStatusBarModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SessionStatusBarViewModel(
    private val omegaRepository: Omega_Repository
) : ViewModel() {

    val sessionStatusBar: StateFlow<SessionStatusBarModel?> =
        omegaRepository
            .getSessionStatusBar()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
}