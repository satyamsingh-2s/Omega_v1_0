package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey


    @Entity(tableName = "daily_records")
    data class DailyRecordEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L,

        val date: String
    )
