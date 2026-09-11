package com.satyamsingh2s.productivity.omega.ui.model

data class DailyRecordSessionDetailsUiModel(

    val sessionId: Long,

    val sessionName: String,

    val durationSeconds: Int,

    val expectedDurationMinutes: Int?
)