package com.satyamsingh2s.productivity.omega.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satyamsingh2s.productivity.omega.data_layer.omega_repository.Omega_Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class UnplannedProjectHistroyViewModel (
    private val repository: Omega_Repository
    ) : ViewModel() {

        val historyRecords =
            repository.getUnplannedProjectHistory()
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                )
}