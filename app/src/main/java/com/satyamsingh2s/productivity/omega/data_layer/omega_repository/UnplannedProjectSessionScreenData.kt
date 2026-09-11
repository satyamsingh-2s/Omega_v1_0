package com.satyamsingh2s.productivity.omega.data_layer.omega_repository
import com.satyamsingh2s.productivity.omega.data_layer.entites.SessionEntity


data class UnplannedProjectSessionScreenData(

    // ---------- Header ----------
    val projectName: String,

    val breadcrumb: String,

    // ---------- Progress ----------
    val currentDurationSeconds: Int,

    val expectedDurationSeconds: Int,

    val totalSessions: Int,

    // ---------- Recent Sessions ----------
    val recentSessions: List<SessionEntity>
)