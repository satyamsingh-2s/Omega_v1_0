package com.satyamsingh2s.productivity.omega.ui.model

import java.time.LocalDate

data class DailyRecordHistoryUiModel(

    val recordId: Long,

    val recordDate: LocalDate,

    val totalDurationSeconds: Int,

    val totalSessionCount: Int,

    val totalbreakseconds: Int
)
