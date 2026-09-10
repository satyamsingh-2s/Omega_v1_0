package com.example.omega_v1_0.data_layer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.omega_v1_0.data_layer.entites.SessionNoteAttachmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionNoteAttachmentDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAttachment(
        attachment: SessionNoteAttachmentEntity
    ): Long

    @Delete
    suspend fun deleteAttachment(
        attachment: SessionNoteAttachmentEntity
    )

    @Query("""
        SELECT *
        FROM session_note_attachments
        WHERE sessionId = :sessionId
        ORDER BY createdAt ASC
    """)
    suspend fun getAttachmentsBySessionId(
        sessionId: Long
    ): List<SessionNoteAttachmentEntity>

    @Query("""
        SELECT *
        FROM session_note_attachments
        WHERE sessionId = :sessionId
        ORDER BY createdAt ASC
    """)
    fun observeAttachmentsBySessionId(
        sessionId: Long
    ): Flow<List<SessionNoteAttachmentEntity>>

    @Query("""
        DELETE FROM session_note_attachments
        WHERE sessionId = :sessionId
    """)
    suspend fun deleteAttachmentsBySessionId(
        sessionId: Long
    )

    @Query(
        "SELECT * FROM session_note_attachments WHERE id = :attachmentId"
    )
    suspend fun getAttachmentById(
        attachmentId: Long
    ): SessionNoteAttachmentEntity?


}