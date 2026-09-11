package com.satyamsingh2s.productivity.omega.ui.uistate

import com.satyamsingh2s.productivity.omega.models_enums.SessionStatus
import com.satyamsingh2s.productivity.omega.ui.model.DailyRecordRecentsSessionUiModel

data class DailyRecordUiState(

    val todaysTotalSeconds: Int = 0,

    val stopwatchSeconds: Int = 0,

    val sessionNameInput: String = "", // holds the value that user enters -- ui variable

    val selectedEstimateMinutes:Int? = null,

    val activeSessionName: String? = null, // holds the value use in database -- database variable

    val sessionStatus: SessionStatus? = null,

    val recentSessions: List<DailyRecordRecentsSessionUiModel> = emptyList(),

    val accumulatedDurationSeconds: Int = 0,

    val currentStartTime: Long? = null,
//-------------- break section ------------------
    val isBreakRunning: Boolean = false,
    val currentBreakSeconds: Int = 0,
    val todaysBreakSeconds: Int = 0,
    val todaysBreakCount: Int = 0,

    //---------------------------------------
    val selectedBreakMinutes: Int? = null,

    )