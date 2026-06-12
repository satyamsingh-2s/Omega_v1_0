package com.example.omega_v1_0.ui.model

import java.time.LocalDate

data class DailyRecordHistoryUiModel(

    val recordId: Long,

    val recordDate: LocalDate,

    val totalDurationSeconds: Int,

    val sessionCount: Int
)
