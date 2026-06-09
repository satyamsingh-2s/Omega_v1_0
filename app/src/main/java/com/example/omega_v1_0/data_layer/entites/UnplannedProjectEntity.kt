package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey


    @Entity(tableName = "unplanned_projects")
    data class UnplannedProjectEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0L,

        val name: String,

        val createdAt: Long
    )
