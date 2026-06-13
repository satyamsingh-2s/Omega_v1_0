package com.example.omega_v1_0.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.ui.model.DailyRecordRecentsSessionUiModel
import com.example.omega_v1_0.ui.uistate.DailyRecordUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class DailyRecordViewModel(
    private val repository: Omega_Repository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(DailyRecordUiState())

    val uiState: StateFlow<DailyRecordUiState> =
        _uiState.asStateFlow()

    private val estimateOptions = listOf(5, 15, 30, null, 45, 60, 90, 105, 120
    )

    private var stopwatchJob: Job? = null

    private fun startStopwatchTicker() {

        stopwatchJob?.cancel()

        stopwatchJob = viewModelScope.launch {

            while (isActive) {

                val state = uiState.value

                if (
                    state.sessionStatus != SessionStatus.RUNNING ||
                    state.currentStartTime == null
                ) {
                    break
                }

                val runningSeconds =
                    ((System.currentTimeMillis()
                            - state.currentStartTime) / 1000)
                        .toInt()

                _uiState.update {
                    it.copy(
                        stopwatchSeconds =
                            it.accumulatedDurationSeconds +
                                    runningSeconds
                    )
                }

                delay(1000)
            }
        }
    }

    private fun stopStopwatchTicker() {
        stopwatchJob?.cancel()
        stopwatchJob = null
    }

    // loading todays record
    fun loadTodaysTotal() {

        viewModelScope.launch {

            val totalSeconds =
                repository.getTodaysTotalDuration()

            _uiState.update {
                it.copy(
                    todaysTotalSeconds = totalSeconds
                )
            }
        }
    }


    fun onSessionNameChanged(value: String) {
        _uiState.update {
            it.copy(
                sessionNameInput = value
            )
        }
    }

    fun startSession() {
        viewModelScope.launch {

            repository.startDailySession(  // here the value of text field is sharing with the repository
                sessionName =
                    uiState.value.sessionNameInput
                        .ifBlank { null },

                expectedDurationMinutes =
                    uiState.value.selectedEstimateMinutes
            )
            syncActiveSession()
        }
    }

    fun pauseSession() {
        viewModelScope.launch {
            repository.pauseDailySession()
            syncActiveSession()
        }
    }

    fun resumeSession() {
        viewModelScope.launch {
            repository.resumeDailySession()
            syncActiveSession()
        }
    }

    fun stopSession() {
        viewModelScope.launch {
            val sessionName =
                uiState.value.sessionNameInput.trim()
            val expectedMinutes= uiState.value.selectedEstimateMinutes

            if (sessionName.isNotBlank()) {
                repository.updateDailySessionName(sessionName,expectedMinutes)
            }
            // ---- it is updateing, only issue in the session name
            if(expectedMinutes!=null) {
                repository.updateDailySessionName(_uiState.value.activeSessionName.toString(), expectedMinutes)
            }

            repository.stopDailySession()
            loadTodaysTotal()
            syncActiveSession()
            loadRecentSessions()

        }
    }
// ------------- HERE ONLY UI IS UPDATED , NOT VAIRABLES OF DATALAYER IS UPDATED,
    fun syncActiveSession() {

        viewModelScope.launch {

            val activeSession =
                repository.getDailyActiveSession()
            val session= repository.getActiveSession()

            if (activeSession == null) {

                _uiState.update {
                    it.copy(
                        sessionStatus = null,
                        activeSessionName = null,
                        sessionNameInput = "",
                        selectedEstimateMinutes = null,
                        stopwatchSeconds = 0
                    )
                }

                return@launch
            }

            _uiState.update {
                it.copy(
                    sessionStatus = activeSession.status,
                    accumulatedDurationSeconds = activeSession.accumulatedDurationSeconds,
                    currentStartTime = activeSession.currentStartTime,
                    activeSessionName = session?.sessionName,
                    selectedEstimateMinutes = session?.expectedDurationMinutes
                )
            }
            when (activeSession.status) {

                SessionStatus.RUNNING -> {
                    startStopwatchTicker()
                }

                SessionStatus.PAUSED -> {

                    stopStopwatchTicker()

                    _uiState.update {
                        it.copy(
                            stopwatchSeconds =
                                activeSession.accumulatedDurationSeconds
                        )
                    }
                }
            }

            if (activeSession == null) {
                stopStopwatchTicker()

                _uiState.update {
                    it.copy(
                        sessionStatus = null,
                        activeSessionName = null,
                    )
                }
            }
        }
    }

    fun loadRecentSessions() {

        viewModelScope.launch {

            val sessions =
                repository.getRecentDailyRecordSessions()

            _uiState.update { state ->
                state.copy(
                    recentSessions =
                        sessions.map { session ->

                            DailyRecordRecentsSessionUiModel(
                                id = session.id,
                                sessionName = session.sessionName ?:"",
                                durationSeconds =
                                    session.durationSeconds
                            )
                        }
                )
            }
            // there is dobut in this part -- how session is not in string previously? 📛📛📛
        }
    }

    // ----- selection handler for estimated miutes -------
    fun onEstimateSelected(
        minutes: Int?
    ) {

        _uiState.update {

            it.copy(
                selectedEstimateMinutes =
                    if (
                        it.selectedEstimateMinutes == minutes
                    ) {
                        null
                    } else {
                        minutes
                    }
            )
        }
        // here updating the moment it is selected
        viewModelScope.launch {
            val sessionName =
                uiState.value.sessionNameInput.trim()

            val expectedMinutes = uiState.value.selectedEstimateMinutes

            Log.d("⭕⭕⭕⭕⭕",
                "Session name = $sessionName")

            if (sessionName.isNotBlank()) {
                repository.updateDailySessionName(sessionName, expectedMinutes)
            }
            // ---- it is updateing, only issue in the session name
            if (expectedMinutes != null) {
                repository.updateDailySessionName(
                    _uiState.value.activeSessionName.toString(),
                    expectedMinutes
                )
            }
        }
    }

}