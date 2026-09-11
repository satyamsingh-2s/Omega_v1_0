package com.satyamsingh2s.productivity.omega.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satyamsingh2s.productivity.omega.data_layer.omega_repository.Omega_Repository
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