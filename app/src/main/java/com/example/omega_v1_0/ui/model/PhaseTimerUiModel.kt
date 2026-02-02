package com.example.omega_v1_0.ui.model

data class PhaseTimerUiModel(
val phaseName: String = "",
val estimatedMinutes: Int = 0,
val actualMinutes: Int = 0,
val differencePercent: Int = 0
)