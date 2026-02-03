package com.example.omega_v1_0.data_layer.omega_repository

import android.util.Log
import com.example.omega_v1_0.data_layer.dao.PhaseDao
import com.example.omega_v1_0.data_layer.dao.ProjectDao
import com.example.omega_v1_0.data_layer.dao.SessionDao
import com.example.omega_v1_0.data_layer.entites.PhaseEntity
import com.example.omega_v1_0.data_layer.entites.ProjectEntity
import com.example.omega_v1_0.data_layer.entites.SessionEntity
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.models.PhaseType


class Omega_Repository (
    // creating dao variable to access the database
    private val projectDao: ProjectDao,
            private val phaseDao: PhaseDao,
                    private val sessionDao: SessionDao
){

    // ----- project related operations -----

    suspend fun createProject(   // function to create a new project
        name: String,
        experience: Experience
    ): Long {
        return projectDao.insert(
            ProjectEntity(
                name = name,
                experience = experience,
                createdAt = System.currentTimeMillis(),
            )
        )
    }
    // -------- for recents projects function ------------
    suspend fun getRecentProjects(limit: Int = 5): List<ProjectEntity> {
        return projectDao.getRecentProjects(limit)
    }


    //----- phase related operations -----

            // function to create phases for a project, indirect  way.
    suspend fun savePhaseEstimates(
        projectId: Long,
        estimates: Map<PhaseType, Int>
    ) {
        val phases = estimates.entries.mapIndexed { index, entry ->
            PhaseEntity(
                projectId = projectId,
                phaseType = entry.key,
                orderIndex = index,
                estimatedMinutes = entry.value
            )
        }
        phaseDao.insertAll(phases)  // here phases are inserted into table
    }

    suspend fun getActualMinutesForPhase(phaseId: Long): Int {
        return sessionDao.getTotalMinutesForPhase(phaseId) ?: 0
    }

    // -------------------- session related operations -----------------------

    // indirect way to create a session
    suspend fun startSession(phaseId: Long){
        if(hasActiveSession()){
            return // now showoing of messaged
        }

        val session = SessionEntity(
            phaseId= phaseId,
            startTime= System.currentTimeMillis(),
            endTime= null,
            durationTime = 0
        )

        sessionDao.insertSession(session)
    }

    suspend fun hasActiveSession(): Boolean {
        return sessionDao.getActiveSessionCount() > 0
    }

    suspend fun getActiveSession(): SessionEntity? {
        return sessionDao.getActiveSession()
    }


    suspend fun stopSession() {

        val activeSession = sessionDao.getActiveSession()

        val endTime= System.currentTimeMillis()
        val durationMinutes = ((endTime - activeSession!!.startTime) / 60000).toInt()
        // ----<< for now i am saying active session will always be not null, later handle it properly >>----

        sessionDao.endSession(
            sessionId = activeSession.id,
            endTime = endTime,
            durationTime = durationMinutes
        )
    }
// --------- used in DashboardViewmodel.kt file ------------------------------
    fun getPhaseForProject(projectId: Long) =
        phaseDao.getPhaseForProject(projectId)

    // ------------------ used in phasetimerViewmodel.kt file--------------
    suspend fun getPhaseById(phaseId: Long): PhaseEntity {
        return phaseDao.getPhaseById(phaseId)
    }

    // -------------------- used for getting the projectname for estimate and dashboard scrren ------------------
    suspend fun getProjectById(projectId: Long): ProjectEntity {
        return projectDao.getProjectById(projectId)
    }




}