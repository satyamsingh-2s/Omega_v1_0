package com.example.omega_v1_0.ui.model

import com.example.omega_v1_0.models.SessionType

data class SessionStatusBarUIModel (
    val sessionType: SessionType,
    val parentTitle: String,
    val sessionName: String?

)