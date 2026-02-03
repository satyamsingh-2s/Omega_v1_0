package com.example.omega_v1_0.ui.model

data class ActiveSessionUiModel(
    val sessionId: Long,
    val projectId: Long,
    val projectName: String,
    val phaseName: String,
    val startedAt: Long
)

// the purpose of this ui model is to give data for active invisible session to the CreateProject Screen
