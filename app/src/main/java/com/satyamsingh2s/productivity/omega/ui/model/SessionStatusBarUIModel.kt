package com.satyamsingh2s.productivity.omega.ui.model

import com.satyamsingh2s.productivity.omega.models_enums.SessionType

data class SessionStatusBarUIModel (
    val sessionType: SessionType,
    val parentTitle: String,
    val sessionName: String?

)