package com.satyamsingh2s.productivity.omega.ui.model

import com.satyamsingh2s.productivity.omega.models_enums.SessionStatus

data class DeskOmegaUiModel(

    val title: String,
    val subtitle: String?,

    val stopwatchSeconds: Int,
    val expectedDurationSeconds: Int?,

    val sessionStatus: SessionStatus?,

)