package com.example.omega_v1_0.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DailyRecordDetailsViewModel(

    repository: Omega_Repository,

    recordId: Long

) : ViewModel()
{
    val sessions =
        repository
            .getSessionsForDailyRecord(
                recordId
            )
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}