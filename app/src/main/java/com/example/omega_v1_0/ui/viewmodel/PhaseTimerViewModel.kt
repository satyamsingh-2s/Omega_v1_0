package com.example.omega_v1_0.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.ui.model.PhaseTimerUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PhaseTimerViewModel(
    private val repository: Omega_Repository
) : ViewModel() {

    // ---------- Running state ----------
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    //  ----------- Running phase name -------------------------
    private val _runningPhaseName = MutableStateFlow<String?>(null)
    val runningPhaseName: StateFlow<String?> = _runningPhaseName

    // ---------- UI truth ----------
    private val _uiState = MutableStateFlow(PhaseTimerUiModel())
    val uiState: StateFlow<PhaseTimerUiModel> = _uiState

    // ---------- TICKING TIMER ----------
    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds


    // ---------- Load phase info ----------
    fun loadPhase(phaseId: Long) {
        viewModelScope.launch {

            val phase = repository.getPhaseById(phaseId)
           val totalSeconds= repository.getActualSecondsForPhase(phaseId)
            // ✅ Single source of conversion
             val minutes = totalSeconds / 60
             val seconds = totalSeconds % 60


            // Percentage logic (still minute-based, as you want)
            val diff = minutes - phase.estimatedMinutes
            val percent =
                if (phase.estimatedMinutes == 0) 0
                else (diff * 100) / phase.estimatedMinutes

            // here the value of ui model is updated and from this the ui updates
            // FIX 2: Update the model with the new 'actualSeconds' property
            _uiState.value = PhaseTimerUiModel(
                phaseName = phase.phaseType.name,
                estimatedMinutes = phase.estimatedMinutes,
                actualMinutes = minutes,
                actualSeconds = seconds,
                differencePercent = percent
            )
        }
    }

    // --------- check if the session is active in database
    fun syncRunningState(phaseId: Long) {
        viewModelScope.launch {
             val runningPhaseId = repository.getRunningPhaseId()

            if (runningPhaseId == null) {
                _isRunning.value = false
                _runningPhaseName.value = null
                return@launch
            }

            if (runningPhaseId == phaseId) {
                // This phase is running
                _isRunning.value = true
                _runningPhaseName.value = null
            } else {
                // Another phase is running
                _isRunning.value = false
                _runningPhaseName.value =
                    repository.getRunningPhaseName()
            }
        }
    }


    // ---------- Start session ----------
    private var tickerJob: Job? = null

    fun start(phaseId: Long) {
        viewModelScope.launch {

            if (repository.hasActiveSession()) return@launch

            repository.startSession(phaseId)
            _isRunning.value = true
            startTicker()
        }
    }

    // ---------- Stop session ----------
    fun stop(phaseId: Long) {
        viewModelScope.launch {

            tickerJob?.cancel()
            tickerJob = null

            _elapsedSeconds.value = 0

            repository.stopSession()
            _isRunning.value = false

            // refresh truth immediately
            loadPhase(phaseId)
        }
    }

    private fun startTicker() {
        tickerJob?.cancel()

        tickerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000)
                _elapsedSeconds.value += 1
            }
        }
    }

}
