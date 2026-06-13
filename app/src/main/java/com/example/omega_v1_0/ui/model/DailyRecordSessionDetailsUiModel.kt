package com.example.omega_v1_0.ui.model

data class DailyRecordSessionDetailsUiModel(

    val sessionId: Long,

    val sessionName: String,

    val durationSeconds: Int,

    val expectedDurationMinutes: Int?
)