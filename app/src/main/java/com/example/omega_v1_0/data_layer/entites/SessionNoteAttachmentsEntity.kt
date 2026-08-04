package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "session_note_attachments",
    foreignKeys = [
        ForeignKey(
            entity = SessionEntity::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("sessionId","createdAt")
    ]
)
data class SessionNoteAttachmentEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val sessionId: Long,

    /**
     * Gallery URI for now.
     * Later this will point to Omega's private storage.
     */
    val imageUri: String,

    val createdAt: Long
)