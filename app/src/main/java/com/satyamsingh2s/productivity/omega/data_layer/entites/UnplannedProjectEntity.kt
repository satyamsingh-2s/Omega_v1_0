package com.satyamsingh2s.productivity.omega.data_layer.entites

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


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

    // Root projects only
    val accentIndex: Int?,

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
