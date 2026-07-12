package com.example.omega_v1_0.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.omega_v1_0.data_layer.entites.PomodoroEntity

@Dao
interface PomodoroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePomodoro(
        pomodoro: PomodoroEntity
    )

    @Query("SELECT * FROM pomodoro WHERE id = 1")
    suspend fun getPomodoro(): PomodoroEntity?

    @Query("DELETE FROM pomodoro")
    suspend fun deletePomodoro()
}