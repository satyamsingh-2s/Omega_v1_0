package com.example.omega_v1_0.ui.viewmodel

import com.example.omega_v1_0.models_enums.SessionStatusBarModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DeskOmegaViewModel(

    repository: Omega_Repository

) : ViewModel() {

    val sessionStatusBar: StateFlow<SessionStatusBarModel?> =
        repository
            .getSessionStatusBar()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

}