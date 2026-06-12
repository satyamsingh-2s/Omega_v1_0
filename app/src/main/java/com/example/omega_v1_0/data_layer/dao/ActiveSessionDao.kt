package com.example.omega_v1_0.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.omega_v1_0.data_layer.entites.ActiveSessionEntity

@Dao
interface ActiveSessionDao {

    @Query("SELECT * FROM active_session LIMIT 1")
    suspend fun getActiveSession(): ActiveSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replace the existing session if it already exists
    // but why OnConflict strategy is for?
    suspend fun insert(activeSession: ActiveSessionEntity)

    @Update
    suspend fun update(activeSession: ActiveSessionEntity)

    @Query("DELETE FROM active_session")
    suspend fun clear()
}