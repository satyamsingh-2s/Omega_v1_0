package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// here we create a table for sessions data
@Entity(
    tableName ="sessions",
    foreignKeys = [
        ForeignKey(
            entity= PhaseEntity::class,
            parentColumns = ["id"],
            childColumns = ["phaseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("phaseId")]
)

data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long =0L,
    val phaseId: Long,
    val startTime: Long,
    val endTime: Long?, // if endTime is null then it means Active Session,0 can be considerd as timestamp that why 0 not used
    // it makes endTime nullable, so that it can store null, if value is not given or produced yet.
    val durationTime: Int
)