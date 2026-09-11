package com.satyamsingh2s.productivity.omega.data_layer.omega_repository

import com.satyamsingh2s.productivity.omega.data_layer.dao.ActiveSessionDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.DailyRecordDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.PlannedProjectDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.PhaseDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.SessionDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.UnplannedProjectDao
import com.satyamsingh2s.productivity.omega.data_layer.entites.SessionEntity
import com.satyamsingh2s.productivity.omega.models_enums.SessionStatusBarModel
import com.satyamsingh2s.productivity.omega.models_enums.SessionType

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionStatusBarRepository(

    private val activeSessionDao: ActiveSessionDao,
    private val sessionDao: SessionDao,
    private val projectDao: PlannedProjectDao,
    private val phaseDao: PhaseDao,
    private val unplannedProjectDao: UnplannedProjectDao,
    private val dailyRecordDao: DailyRecordDao

) {

    fun getSessionStatusBar(): Flow<SessionStatusBarModel?> =
        activeSessionDao.observeActiveSession().map { activeSession ->

            if (activeSession == null) {
                return@map null
            }

            // Get the running session
            val session = sessionDao.getSessionById(activeSession.sessionId)

            when (session.parentType) {

                SessionType.PLANNED -> {
                    buildPlannedStatus(session)
                }

                SessionType.UNPLANNED -> {
                    buildUnplannedStatus(session)
                }

                SessionType.DAILY_RECORD -> {
                    buildDailyStatus(session)
                }
            }
        }

    // ----- helper functions -----------------
    private suspend fun buildPlannedStatus(
        session: SessionEntity
    ): SessionStatusBarModel {
        val phase =
            phaseDao.getPhaseById(session.parentId)
                ?: return unknownStatus(session)

        val project =
            projectDao.getProjectById(phase.projectId)
                ?: return unknownStatus(session)

        return SessionStatusBarModel(
            sessionType = session.parentType,
            parentTitle = project.name,
            sessionName = session.sessionName
        )
    }

    private suspend fun buildUnplannedStatus(
        session: SessionEntity
    ): SessionStatusBarModel {
        val node = unplannedProjectDao.getNodeById(session.parentId)

        return SessionStatusBarModel(
            sessionType = session.parentType,
            parentTitle = node?.title ?: "Unknown",
            sessionName = session.sessionName
        )
    }

    private suspend fun buildDailyStatus(
        session: SessionEntity
    ): SessionStatusBarModel {

        val record =
            dailyRecordDao.getRecordById(session.parentId)
                ?: return unknownStatus(session)

        return SessionStatusBarModel(
            sessionType = session.parentType,
            parentTitle = "Today",
            sessionName = session.sessionName
        )
    }

    private fun unknownStatus(
        session: SessionEntity
    ): SessionStatusBarModel {

        return SessionStatusBarModel(
            sessionType = session.parentType,
            parentTitle = "Unknown",
            sessionName = session.sessionName
        )
    }

}

