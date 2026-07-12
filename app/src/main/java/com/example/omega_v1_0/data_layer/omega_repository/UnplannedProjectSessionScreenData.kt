package com.example.omega_v1_0.data_layer.omega_repository
import com.example.omega_v1_0.data_layer.entites.SessionEntity


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