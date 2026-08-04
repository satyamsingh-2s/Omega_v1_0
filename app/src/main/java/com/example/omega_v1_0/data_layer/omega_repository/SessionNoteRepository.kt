package com.example.omega_v1_0.data_layer.omega_repository

import com.example.omega_v1_0.data_layer.dao.SessionNoteDao
import com.example.omega_v1_0.data_layer.entites.SessionNoteEntity
import com.example.omega_v1_0.models.RevisionNoteItem
import com.example.omega_v1_0.models.SessionType
import kotlinx.coroutines.flow.Flow

class SessionNoteRepository(
    private val sessionNoteDao: SessionNoteDao
) {

    // -- for this function business logic is in the dao
    suspend fun saveRevisionNote(
        sessionId: Long,
        summary: String
    ) {
        sessionNoteDao.saveRevisionNote(
            sessionId,
            summary,
            currentTime = System.currentTimeMillis(),
        )
    }

    suspend fun getRevisionNote(
        sessionId: Long
    ): SessionNoteEntity? {

        return sessionNoteDao.getNoteBySessionId(sessionId)
    }

    fun observeRevisionNote(
        sessionId: Long
    ): Flow<SessionNoteEntity?> {

        return sessionNoteDao.observeNoteBySessionId(sessionId)
    }

    suspend fun deleteRevisionNote(
        sessionId: Long
    ) {

        val note =
            sessionNoteDao.getNoteBySessionId(sessionId)

        if (note != null) {
            sessionNoteDao.deleteNote(note)
        }
    }

    fun observeRevisionNotes(
        nodeId: Long
    ): Flow<List<RevisionNoteItem>> {

        return sessionNoteDao.observeRevisionNotes(
            nodeId = nodeId,
            parentType = SessionType.UNPLANNED
        )
    }
}