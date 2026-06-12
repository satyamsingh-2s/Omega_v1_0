package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.omega_v1_0.models.SessionType

// here we create a table for sessions data
@Entity(
    tableName ="sessions",
    indices = [
        Index(value = ["parentId", "parentType"]),
        Index(value = ["endTime"])
    ]
)

data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long =0L,
    val parentId: Long,
    // PLANNED      -> PhaseEntity.id  ⭕⭕⭕⭕⭕⭕⭕
// UNPLANNED    -> UnplannedProjectEntity.id
// DAILY_RECORD -> DailyRecordEntity.id
    val parentType: SessionType,

    val startTime: Long,
    val endTime: Long?, // if endTime is null then it means Active Session,0 can be considerd as timestamp that why 0 not used
    // it makes endTime nullable, so that it can store null, if value is not given or produced yet.
    val durationSeconds: Int,

    val sessionName: String? = null,
    val expectedDurationMinutes: Int? = null
)