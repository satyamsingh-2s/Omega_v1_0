package com.example.omega_v1_0.ui.model

import com.example.omega_v1_0.models.PhaseType

data class DashboardPhaseItem(
    val phaseId: Long,
    val phaseType: PhaseType,
    val estimatedMinutes: Int,
    val actualMinutes: Int
)