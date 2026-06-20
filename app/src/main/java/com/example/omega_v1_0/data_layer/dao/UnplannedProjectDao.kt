package com.example.omega_v1_0.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.omega_v1_0.data_layer.entites.UnplannedProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UnplannedProjectDao {

    @Query("""
        SELECT * 
        FROM Unplanned_projects
        ORDER BY sortOrder ASC
    """)
    fun getAllNodes(): Flow<List<UnplannedProjectEntity>>

    @Query("""
        SELECT *
        FROM Unplanned_projects
        WHERE nodeId = :nodeId
    """)
    suspend fun getNodeById(
        nodeId: Long
    ): UnplannedProjectEntity?

    @Insert
    suspend fun insertNode(
        node: UnplannedProjectEntity
    ): Long

    @Update
    suspend fun updateNode(
        node: UnplannedProjectEntity
    )

    @Query("""
    SELECT COUNT(*)
    FROM Unplanned_projects
    WHERE parentNodeId = :nodeId
""")
    suspend fun getChildCount(nodeId: Long): Int

    @Query("""
SELECT COALESCE(MAX(sortOrder), -1)
FROM Unplanned_projects
WHERE parentNodeId IS NULL
""")
    suspend fun getMaxRootSortOrder(): Int

    @Query("""
SELECT COALESCE(MAX(sortOrder), -1)
FROM Unplanned_projects
WHERE parentNodeId = :parentId
""")
    suspend fun getMaxChildSortOrder(parentId: Long): Int

}