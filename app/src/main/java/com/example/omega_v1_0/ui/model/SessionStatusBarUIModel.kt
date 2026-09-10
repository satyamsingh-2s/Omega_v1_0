package com.example.omega_v1_0.ui.model

import com.example.omega_v1_0.models_enums.SessionType

data class SessionStatusBarUIModel (
    val sessionType: SessionType,
    val parentTitle: String,
    val sessionName: String?

)