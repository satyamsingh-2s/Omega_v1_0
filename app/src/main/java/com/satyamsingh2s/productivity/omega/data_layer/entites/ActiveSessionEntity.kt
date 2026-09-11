package com.satyamsingh2s.productivity.omega.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.satyamsingh2s.productivity.omega.models_enums.SessionStatus

@Entity(tableName = "active_session")
data class ActiveSessionEntity(

    @PrimaryKey
    val id: Int = 1,

    val sessionId: Long,

    val status: SessionStatus,

    val currentStartTime: Long,

    val accumulatedDurationSeconds: Int
)