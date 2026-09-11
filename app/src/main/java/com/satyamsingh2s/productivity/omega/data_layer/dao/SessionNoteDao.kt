package com.satyamsingh2s.productivity.omega.data_layer.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.satyamsingh2s.productivity.omega.data_layer.entites.SessionNoteEntity
import com.satyamsingh2s.productivity.omega.models_enums.RevisionNoteItem
import com.satyamsingh2s.productivity.omega.models_enums.SessionType
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionNoteDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertNote(
        note: SessionNoteEntity
    ): Long

    @Update
    suspend fun updateNote(
        note: SessionNoteEntity
    )

    @Delete
    suspend fun deleteNote(
        note: SessionNoteEntity
    )

    @Query("""
        SELECT *
        FROM session_notes
        WHERE sessionId = :sessionId
    """)
    suspend fun getNoteBySessionId(
        sessionId: Long
    ): SessionNoteEntity?

    @Query("""
        SELECT *
        FROM session_notes
        WHERE sessionId = :sessionId
    """)
    fun observeNoteBySessionId(
        sessionId: Long
    ): Flow<SessionNoteEntity?>

    @Query("""
    SELECT
        s.id AS sessionId,
        s.sessionName AS sessionName,
        s.startTime AS sessionStartTime,
        s.durationSeconds AS durationSeconds,
        sn.summary AS summary
    FROM session_notes sn
    INNER JOIN sessions s
        ON sn.sessionId = s.id
    WHERE s.parentId = :nodeId
      AND s.parentType = :parentType
    ORDER BY s.startTime DESC
""")
    fun observeRevisionNotes(
        nodeId: Long,
        parentType: SessionType
    ): Flow<List<RevisionNoteItem>>

    //--- here there business logic is there---------
    @Transaction
    suspend fun saveRevisionNote(
        sessionId: Long,
        summary: String,
        currentTime: Long,
    ) {

        val normalizedSummary = summary.trim()

        if (normalizedSummary.isEmpty()) {

            getNoteBySessionId(sessionId)?.let {
                deleteNote(it)
            }

            return
        }

        val existingNote = getNoteBySessionId(sessionId)

        // Empty summary = delete note
        if (normalizedSummary.isEmpty()) {

            if (existingNote != null) {
                deleteNote(existingNote)
            }

            return
        }

        // No changes -> avoid unnecessary update
        if (existingNote != null &&
            existingNote.summary == normalizedSummary
        ) {
            return
        }

        if (existingNote == null) {

            insertNote(
                SessionNoteEntity(
                    sessionId = sessionId,
                    summary = normalizedSummary,
                    createdAt = currentTime,
                    updatedAt = currentTime
                )
            )

        } else {

            updateNote(
                existingNote.copy(
                    summary = normalizedSummary,
                    updatedAt = currentTime
                )
            )
        }
    }


}