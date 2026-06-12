package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.omega_v1_0.models.SessionStatus

@Entity(tableName = "active_session")
data class ActiveSessionEntity(

    @PrimaryKey
    val id: Int = 1,

    val sessionId: Long,

    val status: SessionStatus,

    val currentStartTime: Long,

    val accumulatedDurationSeconds: Int
)