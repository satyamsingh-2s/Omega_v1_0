package com.example.omega_v1_0.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.ui.model.DashboardPhaseItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class DashboardViewModel(
    private val repository: Omega_Repository
) : ViewModel() {

    private val _phases = MutableStateFlow<List<DashboardPhaseItem>>(emptyList())
    val phases: StateFlow<List<DashboardPhaseItem>> = _phases

    fun loadDashboard(projectId: Long) {
        viewModelScope.launch {

            repository.getPhaseForProject(projectId).collect { phaseEntities ->

                val items = phaseEntities.map { phase ->
                    DashboardPhaseItem(
                        phaseId = phase.id,
                        phaseType = phase.phaseType,
                        estimatedMinutes = phase.estimatedMinutes,
                        actualMinutes = repository.getActualMinutesForPhase(phase.id)
                    )
                }

                _phases.value = items
            }
        }
    }
}
