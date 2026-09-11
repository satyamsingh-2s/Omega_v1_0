package com.satyamsingh2s.productivity.omega.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.satyamsingh2s.productivity.omega.models_enums.Experience

// here we create the table for projects data

@Entity(tableName = "planned_projects") // this function creates a table named "projects" in the database
data class PlannedProjectEntity( // this is the data class for the project entity
    @PrimaryKey(autoGenerate = true)  // primary key for the table, auto generates an id
   val id: Long =0L,
    val name: String,
    val experience: Experience,
    val createdAt: Long
)