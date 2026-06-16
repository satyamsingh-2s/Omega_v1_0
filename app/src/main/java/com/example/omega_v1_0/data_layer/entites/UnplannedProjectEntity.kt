package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(
    tableName = "Unplanned_projects" ,// NodeEntity
            indices = [
        Index("parentNodeId")
    ]
)

data class UnplannedProjectEntity(

    @PrimaryKey(autoGenerate = true)
    val nodeId: Long = 0L,

    val parentNodeId: Long?,

    val title: String,

    val sortOrder: Int,

    val createdAt: Long,

    // Leaf nodes only
    val isCompleted: Boolean?,

    // Leaf nodes only
    val expectedDurationSeconds: Int?
)


// ---- rules----------
//NodeEntity(
//parentNodeId = null,
//title = "Android",
//isCompleted = null,
//expectedDurationMinutes = null
//)
//
//NodeEntity(
//title = "Architecture",
//isCompleted = null,
//expectedDurationMinutes = null
//)
//
//NodeEntity(
//title = "MVVM",
//isCompleted = false,
//expectedDurationMinutes = 20
//)
