package com.example.omega_v1_0.ui.model

import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.ui.deskOmega.DeskOmegaSkin

data class DeskOmegaUiModel(

    val title: String,
    val subtitle: String?,

    val stopwatchSeconds: Int,
    val expectedDurationSeconds: Int?,

    val sessionStatus: SessionStatus?,

)