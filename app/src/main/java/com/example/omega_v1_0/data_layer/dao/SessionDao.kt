package com.example.omega_v1_0.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.omega_v1_0.data_layer.entites.SessionEntity

/**
 * for now featur of session doa is limited to creating a session when we are working on that session, no support for pause session, just start and end session.
 */

@Dao
interface SessionDao{

    @Insert  // function to insert a new session into the database thats's all
    suspend fun insertSession(session: SessionEntity)



    @Query("""
        SELECT * FROM sessions
        WHERE endTime IS NULL
        LIMIT 1                           
    """) // for now i am limiting it to 1 because there should be only one active session at a time
    suspend fun getActiveSession(): SessionEntity?

    // for getting the session if it is active in database
    @Query("""
    SELECT * FROM sessions
    WHERE phaseId = :phaseId
    AND endTime IS NULL
    LIMIT 1
""")
    suspend fun getActiveSessionForPhase(phaseId: Long): SessionEntity?


    // now running query for getting active session count
    // for it will give only one, feature is limited`
    @Query("""
        SELECT COUNT(*)
        FROM sessions
        WHERE endTime IS NULL
    """)
    suspend fun getActiveSessionCount(): Int

    @Query("""
    SELECT phaseId
    FROM sessions
    WHERE endTime IS NULL
    LIMIT 1
""")
    suspend fun getRunningPhaseId(): Long?


    //function to end the active session
    @Query("""
        UPDATE sessions
        SET endTime =:endTime,
        durationTime =:durationTime
        WHERE id = :sessionId
    """)
    suspend fun endSession(
        sessionId: Long,
        endTime: Long,
        durationTime: Int
    )



    // this query will sum up the duration time of all sessions for a given phase id and it is for function getTotalMinutesForPhase
    @Query("""
        SELECT SUM(durationTime)
        FROM sessions
        where phaseId = :phaseId
        AND endTime IS NOT NULL
    """)
    suspend fun getActualSecondForPhase(phaseId: Long): Int?

    // for ticker
    @Query("""
    SELECT startTime
    FROM sessions
    WHERE endTime IS NULL
    LIMIT 1
""")
    suspend fun getActiveSessionStartTime(): Long?



}


/**
 * what is going, supose we have 2 phasees in a project p1 - idea phase and dev phase
 * idea phase has 3 session s1,s2,s2
 * while deve phase has 2 session s1,s2
 *
 * here query sum up the duration time of all sessions with the respective phase id and with condition that end time is not null
 * if end time is null then it means session is not completed yet.
 * **/