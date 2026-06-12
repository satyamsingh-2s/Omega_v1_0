package com.example.omega_v1_0.ui.uistate

import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.ui.model.DailyRecordRecentsSessionUiModel

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

    )