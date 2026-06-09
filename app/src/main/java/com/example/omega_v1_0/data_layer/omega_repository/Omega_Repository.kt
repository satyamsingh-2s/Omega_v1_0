package com.example.omega_v1_0.data_layer.omega_repository

import com.example.omega_v1_0.data_layer.dao.PhaseDao
import com.example.omega_v1_0.data_layer.dao.PlannedProjectDao
import com.example.omega_v1_0.data_layer.dao.SessionDao
import com.example.omega_v1_0.data_layer.entites.PhaseEntity
import com.example.omega_v1_0.data_layer.entites.PlannedProjectEntity
import com.example.omega_v1_0.data_layer.entites.SessionEntity
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.models.PhaseType
import com.example.omega_v1_0.models.SessionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first


class Omega_Repository (
    // creating dao variable to access the database
    private val plannedProjectDao: PlannedProjectDao,
    private val phaseDao: PhaseDao,
    private val sessionDao: SessionDao
){

    // ----- project related operations -----

    suspend fun createProject(   // function to create a new project
        name: String,
        experience: Experience
    ): Long {
        return plannedProjectDao.insert(
            PlannedProjectEntity(
                name = name,
                experience = experience,
                createdAt = System.currentTimeMillis(),
            )
        )
    }
    // -------- for recents projects function ------------
    suspend fun getRecentProjects(limit: Int = 3): List<PlannedProjectEntity> {
        return plannedProjectDao.getRecentProjects(limit)
    }

    // ----- Function to get all projects for the "Show All" feature -----
    // here i am sticking with the flow, to get automatically updated from any changes
    fun getAllProjects(): Flow<List<PlannedProjectEntity>> {
        return plannedProjectDao.getAllProjects()
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

    // ---------- PLANNED WORK -----------------

    suspend fun getActualSecondsForPhase(phaseId: Long): Int {
        return sessionDao.getActualSecondForPhase(phaseId, parentType = SessionType.PLANNED) ?: 0
    }

    // -------------------- session related operations -----------------------

    // indirect way to create a session
    suspend fun startSession(phaseId: Long){
        if(hasActiveSession()){
            return // now showing of messaged
        }

        val session = SessionEntity(
            parentId = phaseId,
            parentType = SessionType.PLANNED,
            startTime = System.currentTimeMillis(),
            endTime = null,
            durationSeconds = 0
        )

        sessionDao.insertSession(session)
    }

    suspend fun hasActiveSession(): Boolean {
        return sessionDao.getActiveSessionCount() > 0
    }

    // for the particular session which is active in database
    suspend fun hasActiveSessionForPhase(phaseId: Long): Boolean {
        return sessionDao.getActiveSessionForParent(phaseId, SessionType.PLANNED) != null
    }

    // for icon
    suspend fun getRunningPhaseId(): Long? {
        return sessionDao.getRunningParentId()
    }

    suspend fun getRunningPhaseName(): String? {
        val phaseId = sessionDao.getRunningParentId() ?: return null
        return phaseDao.getPhaseById(phaseId).phaseType.name
    }


    suspend fun getActiveSession(): SessionEntity? {
        return sessionDao.getActiveSession()
    }

    // mainly for ticker
    suspend fun getActiveSessionStartTime(): Long? {
        return sessionDao.getActiveSessionStartTime()
    }



    suspend fun stopSession() {

        val activeSession = sessionDao.getActiveSession()

        val endTime= System.currentTimeMillis()
        val durationSeconds = ((endTime - activeSession!!.startTime) / 1000).toInt()
        // ----<< for now i am saying active session will always be not null, later handle it properly >>----

        sessionDao.endSession(
            sessionId = activeSession.id,
            endTime = endTime,
            durationTime = durationSeconds
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
    suspend fun getProjectById(projectId: Long): PlannedProjectEntity {
        return plannedProjectDao.getProjectById(projectId)
    }

    // -------------------- used for deleting the project by cascade feature ------------------
    // 📛📛 -- in v1.0 it is done by cascading by the help of foreign key, but in v2 cascading is only possible to project to phase,
    // so we have to delete the session manually
    // algo - first have all phases, for unplanned it is one session, for daily it is one session
    // then delete session of each pahses and then delete project
    suspend fun deleteProject(projectId: Long) {

        val phases = phaseDao.getPhaseForProject(projectId).first()
        for (phase in phases) {
            sessionDao.deleteSessionForPhase(phase.id, SessionType.PLANNED)
        }
        plannedProjectDao.deleteProjectById(projectId)

        // ---------------- Session cleanup during project deletion ---------------- 📛📛
//
// getPhaseForProject() returns Flow<List<PhaseEntity>>.
//
// Flow is ideal for UI because it continuously emits updates,
// but deletion only needs the current snapshot of phases.
//
// Possible approaches:
//
// 1. Create a separate DAO function returning List<PhaseEntity>
//    Pros: Clear intent, repository doesn't depend on Flow.
//    Cons: Extra DAO method to maintain.
//
// 2. Use .first() on the Flow (chosen approach)
//    Pros: No extra DAO function, less code.
//    Cons: Repository becomes aware that DAO returns Flow.
//
// Omega uses Option 2:
//
// val phases = phaseDao.getPhaseForProject(projectId).first()
//
// Flow<List<PhaseEntity>>
//          ↓
// List<PhaseEntity>
//
// This provides a one-time snapshot for session cleanup before
// deleting the project.
//
// -------------------------------------------------------------------------📛📛📛📛
    }





}