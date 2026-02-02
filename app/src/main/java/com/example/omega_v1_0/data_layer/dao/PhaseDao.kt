package com.example.omega_v1_0.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.omega_v1_0.data_layer.entites.PhaseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhaseDao {

    @Insert
    suspend fun insertAll(phases: List<PhaseEntity>)

    @Query("""
        SELECT * FROM phases 
        WHERE projectId =:projectId 
        ORDER BY orderIndex ASC""")
    fun getPhaseForProject(projectId: Long): Flow<List<PhaseEntity>>

    @Query("""
    SELECT * FROM phases
    WHERE id = :phaseId
""")
    suspend fun getPhaseById(phaseId: Long): PhaseEntity
}

/**
 * " " for single line -> kotlin syntax
 * """ """ for multiple line -> kotlin syntax
 */