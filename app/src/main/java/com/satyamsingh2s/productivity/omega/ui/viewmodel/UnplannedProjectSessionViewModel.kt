package com.satyamsingh2s.productivity.omega.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satyamsingh2s.productivity.omega.data_layer.omega_repository.Omega_Repository
import com.satyamsingh2s.productivity.omega.omega_engines.pomodoro_engine.PomodoroEngine
import com.satyamsingh2s.productivity.omega.omega_engines.pomodoro_engine.PomodoroEvent
import com.satyamsingh2s.productivity.omega.models_enums.SessionStatus
import com.satyamsingh2s.productivity.omega.ui.model.DeskOmegaUiModel
import com.satyamsingh2s.productivity.omega.ui.model.UnplannedProjectRecentSessionUiModel
import com.satyamsingh2s.productivity.omega.ui.uistate.UnplannedProjectSessionScreenUiState
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
        restorPomodoro()
        syncActiveSession()
        loadScreenData()
    }

    private val _uiState =
        MutableStateFlow(UnplannedProjectSessionScreenUiState())

    val uiState: StateFlow<UnplannedProjectSessionScreenUiState> =
        _uiState.asStateFlow()

    val deskOmegaUiModel: DeskOmegaUiModel
        get() = DeskOmegaUiModel(
            title = uiState.value.projectName,
            subtitle = uiState.value.activeSessionName,
            stopwatchSeconds = uiState.value.stopwatchSeconds,
            expectedDurationSeconds = uiState.value.expectedDurationSeconds,
            sessionStatus = uiState.value.sessionStatus
        )

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

    fun stopSession(
        onSessionCompleted: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            val sessionName =
                uiState.value.sessionNameInput.trim()

//            repository.updateUnplannedSessionName(
//                if (sessionName.isBlank())
//                    uiState.value.activeSessionName ?: ""
//                else
//                    sessionName
//            )

            val completedSessionId =
                repository.stopUnplannedSession()

            if (pomodoroEngine.isEnabled) {
                pomodoroEngine.stop()
            }

            loadScreenData()
            syncActiveSession()

            completedSessionId?.let {
                onSessionCompleted(it)
            }
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

            val workingNodeId = repository.getSelectedNode()

            _uiState.update {

                it.copy(
                    workingNodeId = workingNodeId,
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
                        },
                )
            }
        }
    }

    // ------ pomodoro engine part --------------------------
    private val pomodoroEngine = PomodoroEngine(
        config = repository.getPomodoroConfig(),

        // --- it updates ui
        onStateChanged = { state ->
            _uiState.update {
                it.copy(
                    pomodoroState = state
                )
            }
        },
    // ----- it performs actions

        onEvent = { event,state,config ->
            when (event) {
                PomodoroEvent.Tick -> {
                }
                PomodoroEvent.WorkStarted -> {
                }
                PomodoroEvent.WorkCompleted -> {
                }
                PomodoroEvent.ShortBreakStarted -> {
                    viewModelScope.launch {
                        repository.savePomodoroRuntime(
                            state = state,
                            config = config,
                            startedAt = System.currentTimeMillis()
                        )

                        repository.pauseUnplannedSession()
                        syncActiveSession()
                    }
                }

                PomodoroEvent.LongBreakStarted -> {

                    viewModelScope.launch {
                        repository.savePomodoroRuntime(
                            state = state,
                            config = config,
                            startedAt = System.currentTimeMillis()
                        )

                        repository.pauseUnplannedSession()
                        syncActiveSession()
                    }
                }

                PomodoroEvent.BreakCompleted -> {
                    viewModelScope.launch {
                        repository.deletePomodoroRuntime()

                        repository.resumeUnplannedSession()
                        syncActiveSession()
                    }
                }

                PomodoroEvent.BreakSkipped -> {
                    viewModelScope.launch {
                        repository.deletePomodoroRuntime()

                        repository.resumeUnplannedSession()
                        syncActiveSession()
                    }
                }
            }
        }
    )
    fun skipBreak() {
        pomodoroEngine.skipBreak()
    }

    private fun restorPomodoro() {
        viewModelScope.launch {
            val state =
                repository.getPomodoroRuntimeState()
            val config =
                repository.getPomodoroRuntimeConfig()
            if (state == null || config == null) {
                return@launch
            }
            pomodoroEngine.restoreState(
                state,
                config
            )
        }
    }

    // ----- from daily record part -----------------------
    // ----- selection handler for estimated miutes -------
    // ---- it is useful when app is crashed in midway -----------
    fun onEstimateSelected(
        minutes: Int?
    ) {
        _uiState.update {
            it.copy(
                selectedEstimateMinutes =
                    if (it.selectedEstimateMinutes == minutes
                    ) {
                        null
                    } else {
                        minutes
                    }
            )
        }
        // here updating the moment it is selected
        viewModelScope.launch {
            val sessionName = uiState.value.sessionNameInput.trim()
            val expectedMinutes = uiState.value.selectedEstimateMinutes

            if (sessionName.isNotBlank()) {
                repository.updateDailySessionName(
                    sessionName,
                    expectedMinutes
                )
            }
            else
            {
                repository.updateDailySessionName(_uiState.value.activeSessionName.toString(), expectedMinutes)
            }
            // ---- it is updateing, only issue in the session name
        }
    }



    // --- automatically call this funcitons, when this viewmodel is destoryed
    override fun onCleared() {

        pomodoroEngine.release()
        repository.clearSelectedNode()
        stopStopwatchTicker()
        super.onCleared()
    }
}