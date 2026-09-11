package com.satyamsingh2s.productivity.omega.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "active_break")
data class ActiveBreakEntity(

    @PrimaryKey
    val id: Int = 1,

    val startTime: Long
)