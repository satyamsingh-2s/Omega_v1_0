package com.satyamsingh2s.productivity.omega.models_enums

data class RevisionNoteItem(

    val sessionId: Long,

    val sessionName: String?,

    val sessionStartTime: Long,

    val durationSeconds: Int,

    val summary: String
)