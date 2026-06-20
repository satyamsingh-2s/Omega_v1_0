package com.example.omega_v1_0.data_layer.omega_repository

import com.example.omega_v1_0.data_layer.dao.ActiveSessionDao
import com.example.omega_v1_0.data_layer.dao.SessionDao
import com.example.omega_v1_0.data_layer.entites.ActiveSessionEntity
import com.example.omega_v1_0.data_layer.entites.SessionEntity
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.models.SessionType

class SessionRepository(
    private val sessionDao: SessionDao,
    private val activeSessionDao: ActiveSessionDao
) {

    suspend fun startSession(

        parentId: Long,
        parentType: SessionType,
        sessionName: String?,
        expectedDurationMinutes: Int?

    ) {

        if (
            activeSessionDao.getActiveSession()
            != null
        ) {

            throw IllegalStateException(

                "A session is already running."
            )
        }

        val now =
            System.currentTimeMillis()

        val session = SessionEntity(
            parentId = parentId,
            parentType = parentType,
            startTime = now,
            endTime = null,
            durationSeconds = 0,
            sessionName = sessionName,
            expectedDurationMinutes = expectedDurationMinutes
        )
        val sessionId = sessionDao.insertSession(session)

        val activeSession = ActiveSessionEntity(
                sessionId = sessionId,
                status = SessionStatus.RUNNING,
                currentStartTime = now,
                accumulatedDurationSeconds = 0
            )
        activeSessionDao.insert(activeSession)
    }

    suspend fun stopSession() {

        val activeSession = activeSessionDao.getActiveSession() ?: return

        val now = System.currentTimeMillis()

        val runningSeconds =
            if (activeSession.status == SessionStatus.RUNNING

            ) {
                ((now - activeSession.currentStartTime) / 1000).toInt()
            }

            else {
                0
            }

        val totalDuration = activeSession.accumulatedDurationSeconds + runningSeconds

        sessionDao.endSession(

            sessionId = activeSession.sessionId,
            endTime = now,
            durationTime = totalDuration
        )
        activeSessionDao.clear()
    }

    suspend fun pauseSession() {
        val activeSession = activeSessionDao.getActiveSession() ?: return

        if (activeSession.status != SessionStatus.RUNNING
        ) {
            return
        }

        val now = System.currentTimeMillis()
        val elapsedSeconds = ((now - activeSession.currentStartTime) / 1000).toInt()

        activeSessionDao.update(
            activeSession.copy(
                status = SessionStatus.PAUSED,
                accumulatedDurationSeconds = activeSession.accumulatedDurationSeconds + elapsedSeconds
            )
        )
    }

    suspend fun resumeSession() {
        val activeSession = activeSessionDao.getActiveSession() ?: return
        if (
            activeSession.status != SessionStatus.PAUSED
        ) {
            return
        }
        activeSessionDao.update(
            activeSession.copy(
                status = SessionStatus.RUNNING,
                currentStartTime = System.currentTimeMillis())
        )
    }

    suspend fun hasActiveSession()
            : Boolean {

        return activeSessionDao.getActiveSession()!= null
    }

    suspend fun getSessionsForParent(
        parentId: Long,
        parentType: SessionType

    ): List<SessionEntity> {
        return sessionDao
            .getRecentSessionsForParent(parentId, parentType)
    }

    fun observeActiveSession() =
        activeSessionDao.observeActiveSession()

}