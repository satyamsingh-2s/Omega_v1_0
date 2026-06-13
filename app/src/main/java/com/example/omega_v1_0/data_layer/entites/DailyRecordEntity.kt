package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(
    tableName = "daily_records",
    indices = [Index(value = ["recordDate"], unique = true)]
)
data class DailyRecordEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val recordDate: LocalDate,

    val createdAt: Long,

    val totalDurationSeconds: Int = 0,
    val totalSessionCount: Int = 0

    // 📛📛 1. record date -use local date not string - only con - need a type convertor, with string - switching back and forth
    // 2. logic to lock is if (record date<today date) ....
    // 📛📛
)
// after adding 2 colums , total duration of day and total session count
//SessionEntity
//↓
//Detailed history
//
//DailyRecordEntity
//↓
//Daily summary