package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(
    tableName = "Unplanned_projects"
)
data class UnplannedProjectEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    //val recordDate: LocalDate,

    val createdAt: Long

    // 📛📛 1. record date -use local date not string - only con - need a type convertor, with string - switching back and forth
    // 2. logic to lock is if (record date<today date) ....
    // 📛📛
)
