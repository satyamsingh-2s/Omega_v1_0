package com.example.omega_v1_0.data_layer.omega_repository

import android.util.Log
import com.example.omega_v1_0.data_layer.dao.ActiveBreakDao
import com.example.omega_v1_0.data_layer.dao.ActiveSessionDao
import com.example.omega_v1_0.data_layer.dao.PhaseDao
import com.example.omega_v1_0.data_layer.dao.PlannedProjectDao
import com.example.omega_v1_0.data_layer.dao.SessionDao
import com.example.omega_v1_0.data_layer.entites.DailyRecordEntity
import com.example.omega_v1_0.data_layer.entites.PhaseEntity
import com.example.omega_v1_0.data_layer.entites.PlannedProjectEntity
import com.example.omega_v1_0.data_layer.entites.SessionEntity
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.models.PhaseType
import com.example.omega_v1_0.models.SessionType
import com.example.omega_v1_0.data_layer.dao.DailyRecordDao
import com.example.omega_v1_0.data_layer.dao.ToDoListDao
import com.example.omega_v1_0.data_layer.dao.UnplannedProjectDao
import com.example.omega_v1_0.data_layer.entites.ActiveBreakEntity
import com.example.omega_v1_0.data_layer.entites.ActiveSessionEntity
import com.example.omega_v1_0.data_layer.entites.ToDoListEntity
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.models.TodoCategory
import com.example.omega_v1_0.ui.model.DailyRecordHistoryUiModel
import com.example.omega_v1_0.ui.model.DailyRecordSessionDetailsUiModel
import com.example.omega_v1_0.ui.model.ToDoListUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.time.LocalDate


class Omega_Repository (
    // creating dao variable to access the database
    private val plannedProjectDao: PlannedProjectDao,
    private val phaseDao: PhaseDao,
    private val sessionDao: SessionDao,
    private val dailyRecordDao: DailyRecordDao,
    private val activeSessionDao: ActiveSessionDao,
    private val todolistDao: ToDoListDao,
    private val activeBreakDao: ActiveBreakDao,
    private val unplannedProjectDao: UnplannedProjectDao
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



    // 📛📛-------------- Daily Record related operations ----------------------

    // Create a new daily record, and returns its id,
    // here we are getting session id
    suspend fun getOrCreateTodayRecord(): Long {

        val today = LocalDate.now()

        val existingRecord =
            dailyRecordDao.getRecordByDate(today)

        if (existingRecord != null) {
            return existingRecord.id
        }

        return dailyRecordDao.insert(
            DailyRecordEntity(
                recordDate = today,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    // -------------------------------to start a new Daily Record session -----------------------------------------

    // here we create a session in session entity and in Activesesionentity
    suspend fun startDailySession(
        sessionName: String?,
        expectedDurationMinutes: Int?
    ) {

        // Global Omega rule:
        // only one active session at a time
        if (sessionDao.getActiveSessionCount() > 0) {
            throw IllegalStateException(
                "Another session is already running."
            )
        }
        if (
            activeBreakDao.getActiveBreak() != null
        ) {
            throw IllegalStateException(
                "Cannot start work session while a break is running."
            )
        }

        val dailyRecordId = getOrCreateTodayRecord()    // here we are getting id by daily recordid

        val finalName =
            if (sessionName.isNullOrBlank()) { // it is naming for session name, if not set by user..

                val sessionCount =
                    sessionDao.getSessionCountForParent(
                        parentId = dailyRecordId,
                        parentType = SessionType.DAILY_RECORD
                    )

                "Session ${sessionCount + 1}"

            } else {
                sessionName.trim()
            }

        // NOTE:
        // Using a single timestamp keeps SessionEntity.startTime
        // and ActiveSessionEntity.currentStartTime identical.
        val now = System.currentTimeMillis()

        val sessionId =
            sessionDao.insertSession(
                SessionEntity(
                    parentId = dailyRecordId,
                    parentType = SessionType.DAILY_RECORD,

                    sessionName = finalName,
                    expectedDurationMinutes = expectedDurationMinutes,

                    startTime = now,
                    endTime = null,
                    durationSeconds = 0
                )
            )

        activeSessionDao.insert(
            ActiveSessionEntity(
                id = 1, // Singleton row: only one active session allowed globally

                sessionId = sessionId,

                status = SessionStatus.RUNNING,

                currentStartTime = now,

                accumulatedDurationSeconds = 0
            )
        )

        // TODO:
        // SessionEntity insert and ActiveSessionEntity insert
        // should eventually be wrapped in a Room @Transaction
        // so both succeed or fail together.
    }


    // --------- for stopping the session of DailyRecord ------------------------------

    // here we are verfiying active session from the ActiveSessionEntity not from SessionEntity'end time📛📛
    // ⚠️⚠️⚠️⚠️  always be sure to clear ActiveSessionentity to avoid inconsitences..⚠️⚠️⚠️⚠️
    suspend fun stopDailySession() {

        val activeSession =
            activeSessionDao.getActiveSession()
                ?: throw IllegalStateException(
                    "No active daily session found."
                )

        val endTime = System.currentTimeMillis()

        val durationSeconds =
            when (activeSession.status) {

                SessionStatus.RUNNING -> {
                    activeSession.accumulatedDurationSeconds +
                            ((endTime - activeSession.currentStartTime) / 1000).toInt()
                }

                SessionStatus.PAUSED -> {
                    activeSession.accumulatedDurationSeconds
                }
            }

        sessionDao.endSession(
            sessionId = activeSession.sessionId,
            endTime = endTime,
            durationTime = durationSeconds
        )
        val session =
            sessionDao.getSessionById(
                activeSession.sessionId
            )

        // adding restriction for span sessions
        if(durationSeconds<90)
        {
            sessionDao.deleteSession(session.id)
        }
else {
            dailyRecordDao.updateDailySummary(
                recordId = session.parentId,
                durationSeconds = durationSeconds
            )
        }

        activeSessionDao.clear()
    }

    suspend fun getDailyActiveSession(): ActiveSessionEntity? {
        return activeSessionDao.getActiveSession()
    }

    // ---- updating sesion name after the stop button ------------------
    // here we are getting the active session whole row not id, so using getorCreateTodayRecord() for sessionId
    suspend fun updateDailySessionName(
        sessionName: String,expectedDurationMinutes: Int?
    ) {

               val activeSession =
            activeSessionDao.getActiveSession()
                ?: return

        sessionDao.updateSessionName(
            sessionId = activeSession.sessionId,  //getOrCreateTodayRecord(),  one issue with activesesion working but with getorcratedaileyrecord not📛📛📛📛📛
            sessionName = sessionName,
            expectedDurationMinutes=expectedDurationMinutes
        )
    }

    // ---------- for pause sessison ----
    suspend fun pauseDailySession() {

        val activeSession =
            activeSessionDao.getActiveSession()
                ?: throw IllegalStateException(
                    "No active session found."
                )

        if (activeSession.status != SessionStatus.RUNNING) {
            return
        }

        val now = System.currentTimeMillis()

        val currentSegmentSeconds =
            ((now - activeSession.currentStartTime) / 1000).toInt()

        activeSessionDao.update(
            activeSession.copy(
                status = SessionStatus.PAUSED,
                accumulatedDurationSeconds =
                    activeSession.accumulatedDurationSeconds +
                            currentSegmentSeconds
            )
        )
    }

    // -------- for resume session --------------
    suspend fun resumeDailySession() {

        val activeSession =
            activeSessionDao.getActiveSession()
                ?: throw IllegalStateException(
                    "No active session found."
                )

        if (activeSession.status != SessionStatus.PAUSED) {
            return
        }

        activeSessionDao.update( // .copy is the function of kotlin that creates the copy of the of the specified field only not the whole field
            activeSession.copy(
                status = SessionStatus.RUNNING,
                currentStartTime = System.currentTimeMillis()
            )
        )
    }

    // ------------- function to load todays total time
    suspend fun getTodaysTotalDuration(): Int {

        val todayRecord =
            dailyRecordDao.getRecordByDate(
                LocalDate.now()
            ) ?: return 0

        return sessionDao.getTotalDurationForParent(
            parentId = todayRecord.id,
            parentType = SessionType.DAILY_RECORD
        )
    }

    // -------- functions to load session wrt parent for all 3 types --------

    suspend fun getRecentDailyRecordSessions(
    ): List<SessionEntity> {

        val todayRecord =
            dailyRecordDao.getRecordByDate(
                LocalDate.now()
            ) ?: return emptyList()

        return sessionDao.getRecentSessionsForParent(
            parentId = todayRecord.id,
            parentType = SessionType.DAILY_RECORD,
        )
    }

    //------------------------ DailyRecordHistory ----------------
    suspend fun updateDailySummary(
        recordId: Long,
        durationSeconds: Int
    ) {

        dailyRecordDao.updateDailySummary(
            recordId = recordId,
            durationSeconds = durationSeconds
        )
    }

    //---- here mapping the dailyrecordhistory ui state and dailyrecord history table data
    fun getDailyRecordHistory():
            Flow<List<DailyRecordHistoryUiModel>> {

        return dailyRecordDao
            .getAllRecords()
            .map { records ->

                records.map { record ->

                    DailyRecordHistoryUiModel(

                        recordId = record.id,

                        recordDate = record.recordDate,

                        totalDurationSeconds =
                            record.totalDurationSeconds,

                        totalSessionCount =
                            record.totalSessionCount,

                        totalbreakseconds = record.totalBreakSeconds
                    )
                }
            }
    }

    fun getSessionsForDailyRecord(
        dailyRecordId: Long
    ): Flow<List<DailyRecordSessionDetailsUiModel>> {
        return sessionDao
            .getSessionsForDailyRecord(
                dailyRecordId,
                SessionType.DAILY_RECORD
            )
            .map { sessions ->

                sessions.map { session ->

                    DailyRecordSessionDetailsUiModel(

                        sessionId = session.id,

                        sessionName = session.sessionName.toString(), // ---------⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️ - argumnet mismatch - converted forcefully to string ---------

                        durationSeconds =
                            session.durationSeconds,

                        expectedDurationMinutes =
                            session.expectedDurationMinutes,


                    )
                }
            }
    }

    //---------------------- ToDoList ---------------------------------------------------------------

    suspend fun addToDoItem(
        text: String,
        category: TodoCategory
    ) {
// ----- here if the no. of items in list greater than 7 , simply stop taking the values
//        if (todolistDao.getItemCountCategory(TodoCategory.TODAY) >= 5) {
//            return
//        }
//        todolistDao.insert(ToDoListEntity(text = text, category = category)
//        )
//        if (todolistDao.getItemCountCategory(TodoCategory.FUTURE) >= 7) {
//            return
//        }
        todolistDao.insert(ToDoListEntity(text = text, category = category)
        )
    }

    suspend fun updateToDoCompleted(
        itemId: Long,
        isCompleted: Boolean
    ) {

        todolistDao.updateCompleted(
            itemId = itemId, isCompleted = isCompleted)
    }

    suspend fun deleteToDoItem(
        itemId: Long
    ) {

        todolistDao.delete(itemId)
    }

// ------------------- mapping to the ui variable and data vairable -------------------
    // 3 use cases - 1st map data entites to ui models(data variables to ui varaible)
    // 2nd - provides ui model/data flow to the ui
    // 3rd - its flow automatically emit value if underlying entites is updated
fun getAllToDoItems(category: TodoCategory
): Flow<List<ToDoListUiModel>> {

    return todolistDao
        .getTodosByCategory(category)
        .map { items ->

            items.map { item ->

                ToDoListUiModel(

                    id = item.id,

                    text = item.text,

                    isCompleted =
                        item.isCompleted
                )
            }
        }
    }

    //============================== Break Timer ====================================

    suspend fun startBreak() {

        if (sessionDao.getActiveSessionCount() > 0
            ) {
            throw IllegalStateException("Cannot start break while a work session is running.")
        }

        if (activeBreakDao.getActiveBreak() != null
        ) {
            throw IllegalStateException("Break already running.")
        }
        // -------------------------- TODO ----------------------------------------------------
        // later add catch exception in the code, so that app doen't crashes

        activeBreakDao.insert(
            ActiveBreakEntity(startTime = System.currentTimeMillis()
            )
        )
    }

    suspend fun stopBreak() {

        val activeBreak =
            activeBreakDao.getActiveBreak()
                ?: throw IllegalStateException("No active break found."
                )
        val endTime = System.currentTimeMillis()
        val durationSeconds = ((endTime - activeBreak.startTime)
                            / 1000).toInt()

        val todayRecordId = getOrCreateTodayRecord()

        // restriction for spam break sessions, spam session is considered < 25 seconds
        if(durationSeconds>25){
            dailyRecordDao.updateBreakSummary(
                recordId = todayRecordId,
                durationSeconds = durationSeconds
            )
        }
        activeBreakDao.clear()
    }

    suspend fun getActiveBreak(): ActiveBreakEntity? {
        return activeBreakDao.getActiveBreak()
    }

    suspend fun getActiveBreakStartTime():
            Long? {
        return activeBreakDao.getActiveBreakStartTime()
    }

    suspend fun getTodayBreakSeconds():
            Int {
        return getOrCreateTodayRecord().let { recordId ->
            dailyRecordDao
                    .getRecordById(recordId)
                    .totalBreakSeconds
            }
    }

    suspend fun getTodayBreakCount():
            Int {
        return getOrCreateTodayRecord().let { recordId ->
                dailyRecordDao
                    .getRecordById(recordId)
                    .totalBreakCount
            }
    }



}