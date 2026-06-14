package com.example.omega_v1_0.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.omega_v1_0.data_layer.entites.ActiveBreakEntity

@Dao
interface ActiveBreakDao {
    @Insert
    suspend fun insert(activeBreak: ActiveBreakEntity)

    @Query(
        """
    SELECT *
    FROM active_break
    LIMIT 1
"""
    )
    suspend fun getActiveBreak(): ActiveBreakEntity?

    @Query(
        """
    DELETE FROM active_break
"""
    )
    suspend fun clear()


    @Query(
        """
    SELECT startTime
    FROM active_break
    LIMIT 1
"""
    )
    suspend fun getActiveBreakStartTime(): Long?


}