package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.omega_v1_0.models.Experience

// here we create the table for projects data

@Entity(tableName = "projects") // this function creates a table named "projects" in the database
data class ProjectEntity( // this is the data class for the project entity
    @PrimaryKey(autoGenerate = true)  // primary key for the table, auto generates an id
   val id: Long =0L,
    val name: String,
    val experience: Experience,
    val createdAt: Long
)