package com.example.omega_v1_0.ui.model

import kotlin.math.roundToInt

data class PhaseTimerUiModel(
val phaseName: String = "",
val estimatedMinutes: Int = 0,
val actualMinutes: Int =0,
val actualSeconds: Int = 0,
val differencePercent: Int = 0)
//) {
//    val actualMinutes: Int
//        get() = (actualMinutes/60.0).roundToInt()
//}