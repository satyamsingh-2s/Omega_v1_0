package com.example.omega_v1_0.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.estimation.pomodoro_engine.PomodoroConfig
import com.example.omega_v1_0.estimation.pomodoro_engine.PomodoroEngine
import com.example.omega_v1_0.estimation.pomodoro_engine.PomodoroEvent
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.ui.model.UnplannedProjectRecentSessionUiModel
import com.example.omega_v1_0.ui.uistate.UnplannedProjectSessionScreenUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class UnplannedProjectSessionViewModel(
    private val repository: Omega_Repository
): ViewModel() {

    init {
        syncActiveSession()
        loadScreenData()
    }

    private val _uiState =
        MutableStateFlow(UnplannedProjectSessionScreenUiState())

    val uiState: StateFlow<UnplannedProjectSessionScreenUiState> =
        _uiState.asStateFlow()

    //------------ stopwatch ticker -------------------------------
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

                val runningSeconds = ((System.currentTimeMillis() - state.currentStartTime) / 1000).toInt()

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

    fun syncActiveSession() {
        viewModelScope.launch {

            val activeSession =
                repository.getUnplannedProjectActiveSession()

            val session =
                repository.getActiveSession()

            if (activeSession == null) {

                stopStopwatchTicker()

                _uiState.update {
                    it.copy(
                        sessionStatus = null,
                        activeSessionName = null,
                        sessionNameInput = "",
                        stopwatchSeconds = 0,
                        accumulatedDurationSeconds = 0,
                        currentStartTime = null
                    )
                }

                loadScreenData()
                return@launch
            }

            _uiState.update {
                it.copy(
                    sessionStatus = activeSession.status,
                    accumulatedDurationSeconds = activeSession.accumulatedDurationSeconds,
                    currentStartTime = activeSession.currentStartTime,
                    activeSessionName = session?.sessionName,
                    // update to notice here
                    sessionNameInput =
                        if (it.sessionNameInput.isBlank())
                            session?.sessionName ?: ""
                        else
                            it.sessionNameInput,
                    stopwatchSeconds = activeSession.accumulatedDurationSeconds
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
        }
    }


    fun startSession() {

        val nodeId =
            repository.getSelectedNode()
                ?: return

        viewModelScope.launch {

            repository.startUnplannedSession(
                nodeId = nodeId,
                sessionName =
                    uiState.value.sessionNameInput
                        .takeIf { it.isNotBlank() },
                expectedDurationMinutes = null
            )

            if (pomodoroEngine.isEnabled) {
                pomodoroEngine.start()
            }

            syncActiveSession()
            loadScreenData()
        }
    }

    fun pauseSession() {
        viewModelScope.launch {
            repository.pauseUnplannedSession()

            if (pomodoroEngine.isEnabled) {
                pomodoroEngine.pause()
            }
            syncActiveSession()
            loadScreenData()
        }
    }

    fun resumeSession() {

        viewModelScope.launch {
            repository.resumeUnplannedSession()

            if (pomodoroEngine.isEnabled) {
                pomodoroEngine.resume()
            }
            syncActiveSession()
            loadScreenData()
        }
    }

    fun stopSession() {
        viewModelScope.launch {
            val sessionName =
                uiState.value.sessionNameInput.trim()

//            repository.updateUnplannedSessionName(
//                if (sessionName.isBlank())
//                    uiState.value.activeSessionName ?: ""
//                else
//                    sessionName
//            )

            repository.stopUnplannedSession()

            if (pomodoroEngine.isEnabled) {
                pomodoroEngine.stop()
            }
            loadScreenData()
            syncActiveSession()
        }
    }


    fun onSessionNameChanged(
        value: String
    ) {
        _uiState.update {
            it.copy(
                sessionNameInput = value
            )
        }
    }

    fun loadScreenData() {

        viewModelScope.launch {

            val screenData =
                repository.getCurrentSessionScreenData()

            _uiState.update {

                it.copy(
                    projectName =
                        screenData.projectName,
                    breadcrumb =
                        screenData.breadcrumb,
                    currentDurationSeconds =
                        screenData.currentDurationSeconds,
                    expectedDurationSeconds =
                        screenData.expectedDurationSeconds,
                    totalSessions =
                        screenData.totalSessions,
                    recentSessions =
                        screenData.recentSessions.map { session ->

                            UnplannedProjectRecentSessionUiModel(
                                id = session.id,
                                sessionName =
                                    session.sessionName.orEmpty(),
                                durationSeconds =
                                    session.durationSeconds
                            )
                        }
                )
            }
        }
    }

    // ------ pomodoro engine part --------------------------
    private val pomodoroEngine = PomodoroEngine(

        config = PomodoroConfig(
            workDurationSeconds = 25 * 60,
            shortBreakDurationSeconds = 5 * 60,
            longBreakDurationSeconds = 15 * 60,
            workCyclesBeforeLongBreak = 4
        ),// --- it updates ui
        onStateChanged = { state ->
            _uiState.update {
                it.copy(
                    pomodoroPhase =
                        state.phase,
                    pomodoroRemainingSeconds =
                        state.remainingSeconds,
                    pomodoroCompletedWorkCycles =
                        state.completedWorkCycles,
                    isPomodoroRunning =
                        state.isRunning,
                    isPomodoroEnabled =
                        state.isEnabled
                )
            }
        },
    // ----- it performs actions
        onEvent = { event ->
            when (event) {
                PomodoroEvent.Tick -> {
                }
                PomodoroEvent.WorkStarted -> {
                }
                PomodoroEvent.WorkCompleted -> {
                }
                PomodoroEvent.ShortBreakStarted -> {
                }
                PomodoroEvent.LongBreakStarted -> {
                }
                PomodoroEvent.BreakCompleted -> {
                }
                PomodoroEvent.BreakSkipped -> {
                }
            }
        }
    )




    // --- automatically call this funcitons, when this viewmodel is destoryed
    override fun onCleared() {

        pomodoroEngine.release()
        repository.clearSelectedNode()
        stopStopwatchTicker()
        super.onCleared()
    }
}