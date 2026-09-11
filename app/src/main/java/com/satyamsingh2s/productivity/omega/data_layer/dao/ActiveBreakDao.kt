package com.satyamsingh2s.productivity.omega.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.satyamsingh2s.productivity.omega.data_layer.entites.ActiveBreakEntity

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