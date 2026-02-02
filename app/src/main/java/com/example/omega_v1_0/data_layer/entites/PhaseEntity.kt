package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.omega_v1_0.models.PhaseType


// here we create a table for phases data
@Entity(  // here by using foreign key we link phases table to projects table
    tableName= "phases",
    foreignKeys=[
        ForeignKey(
            entity= ProjectEntity::class, // defining the parent table: i.e project entity
            parentColumns=["id"],
            childColumns=["projectId"],
            onDelete= ForeignKey.CASCADE  // this deletes all phases table related to project table,
        )
    ],
    indices = [Index("projectId")]  // Inedex is the data stuructue, which is used to improve searching , if it is not used then it have to manually search each row
)
data class PhaseEntity(
    @PrimaryKey(autoGenerate =true)
    val id: Long=0L,
    val projectId: Long,
    val phaseType: PhaseType,
    val orderIndex: Int,
    val estimatedMinutes: Int
)