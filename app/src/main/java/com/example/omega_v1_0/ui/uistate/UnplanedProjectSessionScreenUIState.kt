package com.example.omega_v1_0.ui.uistate

import com.example.omega_v1_0.omega_engines.pomodoro_engine.PomodoroPhase
import com.example.omega_v1_0.omega_engines.pomodoro_engine.PomodoroState
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.ui.model.UnplannedProjectRecentSessionUiModel

data class UnplannedProjectSessionScreenUiState(

    // ---------- Node Information ----------
    val projectName: String = "",
    val breadcrumb: String = "",
    val currentDurationSeconds: Int = 0,
    val expectedDurationSeconds: Int = 0,
    val totalSessions: Int = 0,

    // ---------- Current Session ----------
    val sessionNameInput: String = "",
    val activeSessionName: String? = null,
    val accumulatedDurationSeconds: Int = 0,
    val currentStartTime: Long? = null,

    // ---------- Stopwatch ----------
    val stopwatchSeconds: Int = 0,
    val sessionStatus: SessionStatus? = null,
    val selectedEstimateMinutes:Int? = null,

    // ---------- Pomodoro ----------
    val pomodoroPhase: PomodoroPhase? = null,
    val pomodoroRemainingSeconds: Int = 0,
    val pomodoroCompletedWorkCycles: Int = 0,
    val isPomodoroRunning: Boolean = false,
    val isPomodoroEnabled: Boolean = false,

    // ---------- Recent Sessions ----------
    val recentSessions: List<UnplannedProjectRecentSessionUiModel> = emptyList(),

    // ----------- Pomdodoro featue -----
    // it has own sate of varaible in the file name pomodorstate, so instead of copying we bring the object
    val pomodoroState: PomodoroState? = null,
    val workCyclesBeforeLongBreak: Int = 4,

    // -------- for future -------
    val runningNodeId: Long? = null
)