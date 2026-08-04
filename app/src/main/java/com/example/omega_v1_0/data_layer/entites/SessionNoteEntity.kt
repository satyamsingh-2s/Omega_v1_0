package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "session_notes",
    indices = [
        Index(value = ["sessionId"], unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SessionNoteEntity(

    @PrimaryKey(autoGenerate = true)
    val noteId: Long = 0,

    val sessionId: Long,

    val summary: String,

    val createdAt: Long,

    val updatedAt: Long
)