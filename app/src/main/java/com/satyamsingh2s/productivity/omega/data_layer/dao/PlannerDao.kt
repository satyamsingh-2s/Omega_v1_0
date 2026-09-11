package com.satyamsingh2s.productivity.omega.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.satyamsingh2s.productivity.omega.data_layer.entites.PlannerNodeEntity
import com.satyamsingh2s.productivity.omega.models_enums.PlannerPriority
import kotlinx.coroutines.flow.Flow

@Dao
interface PlannerDao {

    /* -------------------------------------------------------------------------
     * Insert
     * ------------------------------------------------------------------------- */

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertPlannerNode(
        plannerNode: PlannerNodeEntity
    )

    /* -------------------------------------------------------------------------
     * Update
     * ------------------------------------------------------------------------- */

    @Update
    suspend fun updatePlannerNode(
        plannerNode: PlannerNodeEntity
    )

    /* -------------------------------------------------------------------------
     * Delete
     * ------------------------------------------------------------------------- */

    @Query("""
        DELETE FROM planner_nodes
        WHERE nodeId = :nodeId
    """)
    suspend fun deletePlannerNode(
        nodeId: Long
    )

    /* -------------------------------------------------------------------------
     * Single Node
     * ------------------------------------------------------------------------- */

    @Query("""
        SELECT *
        FROM planner_nodes
        WHERE nodeId = :nodeId
    """)
    suspend fun getPlannerNode(
        nodeId: Long
    ): PlannerNodeEntity?

    @Query("""
        SELECT EXISTS(
            SELECT 1
            FROM planner_nodes
            WHERE nodeId = :nodeId
        )
    """)
    suspend fun exists(
        nodeId: Long
    ): Boolean

    /* -------------------------------------------------------------------------
     * Planner Queries
     * ------------------------------------------------------------------------- */

    @Query("""
        SELECT *
        FROM planner_nodes
        ORDER BY priority ASC,
                 priorityOrder ASC
    """)
    fun getAllPlannerNodes(): Flow<List<PlannerNodeEntity>>

    @Query("""
        SELECT *
        FROM planner_nodes
        WHERE priority = :priority
        ORDER BY priorityOrder ASC
    """)
    fun getPlannerNodesByPriority(
        priority: PlannerPriority
    ): Flow<List<PlannerNodeEntity>>

    @Query("""
        SELECT *
        FROM planner_nodes
        WHERE todayOrder IS NOT NULL
        ORDER BY todayOrder ASC
    """)
    fun getTodayQueue(): Flow<List<PlannerNodeEntity>>

    @Query("""
    SELECT COUNT(*)
    FROM planner_nodes
    WHERE priority = :priority
""")
    suspend fun getPriorityCount(
        priority: PlannerPriority
    ): Int

    @Query("""
SELECT *
FROM planner_nodes
WHERE priority = :priority
ORDER BY priorityOrder ASC
""")
    suspend fun getPriorityNodesOnce(
        priority: PlannerPriority
    ): List<PlannerNodeEntity>

    @Query("""
SELECT *
FROM planner_nodes
WHERE priority = :priority
ORDER BY priorityOrder DESC
LIMIT 1
""")
    suspend fun getLastNodeInPriority(
        priority: PlannerPriority
    ): PlannerNodeEntity?

    @Query("""
SELECT COUNT(*)
FROM planner_nodes
WHERE todayOrder IS NOT NULL
""")
    suspend fun getTodayQueueCount(): Int

    @Query("""
SELECT *
FROM planner_nodes
WHERE todayOrder IS NOT NULL
ORDER BY todayOrder ASC
""")
    suspend fun getTodayQueueOnce(): List<PlannerNodeEntity>

    @Query("""
SELECT *
FROM planner_nodes
WHERE nodeId = :nodeId
  AND todayOrder IS NOT NULL
""")
    suspend fun getTodayQueueNode(
        nodeId: Long
    ): PlannerNodeEntity?






}