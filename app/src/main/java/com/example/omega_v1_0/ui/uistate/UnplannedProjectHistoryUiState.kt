package com.example.omega_v1_0.ui.uistate

import java.time.LocalDate

data class UnplannedProjectHistoryUiState(

    val recordId: Long,

    val recordDate: LocalDate,

    val totalDurationSeconds: Int,

    val totalSessionCount: Int,

    val totalbreakseconds: Int
)