package com.example.omega_v1_0.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.estimation.estimateProject
import com.example.omega_v1_0.models.Complexity
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.models.PhaseType
import com.example.omega_v1_0.models.Scope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EstimateScreenViewModel(
    private val repository: Omega_Repository
    ) : ViewModel() {

    private val _navigateToDashboard = MutableStateFlow(false)
    val navigateToDashboard: StateFlow<Boolean> = _navigateToDashboard
    /**
     * above 2 line have more to lear
     * stateFlow is for the data, that changes and it tells , mutalble be changeable
     */

    fun estimateAndSave(
        projectId: Long,
        experience: Experience,
        phaseInputs: Map<PhaseType, Pair<Complexity, Scope>>
    ){
        viewModelScope.launch {

            // 1. Base times (locked from phase 1)
            val baseTimes = mapOf(
                PhaseType.IDEA to 30,
                PhaseType.RESEARCH to 60,
                PhaseType.DEVELOPMENT to 120,
                PhaseType.DEBUG to 60,
                PhaseType.POLISH to 45
            )

            // 2. Run estimation logic (pure kotlin)
            val estimates = estimateProject(
                experience=experience,
                phaseInputs=phaseInputs,
                baseTimes=baseTimes
            )

            //3. Save to Db
            repository.savePhaseEstimates(
                projectId = projectId,
                estimates = estimates
            )

            //4. trigger navigation
            _navigateToDashboard.value = true

        }
    }

}