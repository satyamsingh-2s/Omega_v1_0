package com.satyamsingh2s.productivity.omega.ui.model

import com.satyamsingh2s.productivity.omega.models_enums.PhaseType

data class DashboardPhaseItem(
    val phaseId: Long,
    val phaseType: PhaseType,
    val estimatedMinutes: Int,
    val actualMinutes: Int
)