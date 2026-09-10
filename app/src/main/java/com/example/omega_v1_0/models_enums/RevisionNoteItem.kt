package com.example.omega_v1_0.models_enums

data class RevisionNoteItem(

    val sessionId: Long,

    val sessionName: String?,

    val sessionStartTime: Long,

    val durationSeconds: Int,

    val summary: String
)