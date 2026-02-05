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

    private val _projectName = MutableStateFlow("")
    val projectName: StateFlow<String> = _projectName

    private val _runningPhaseId = MutableStateFlow<Long?>(null)
    val runningPhaseId: StateFlow<Long?> = _runningPhaseId


    fun syncRunningState() {
        viewModelScope.launch {
            _runningPhaseId.value = repository.getRunningPhaseId()
        }
    }



    fun loadDashboard(projectId: Long) {
        viewModelScope.launch {

            repository.getPhaseForProject(projectId).collect { phaseEntities ->

                val items = phaseEntities.map { phase ->
                    DashboardPhaseItem(
                        phaseId = phase.id,
                        phaseType = phase.phaseType,
                        estimatedMinutes = phase.estimatedMinutes,
                        actualMinutes = repository.getActualSecondsForPhase(phase.id)
                    )
                }

                _phases.value = items
            }
        }
    }
    // -------------- function to load project naem from database ----------------------
    fun loadProject(projectId: Long) {
        viewModelScope.launch {
            val project = repository.getProjectById(projectId)
            _projectName.value = project.name
        }
    }

}
