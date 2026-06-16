package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.omega_v1_0.models.TodoCategory

@Entity(tableName = "todolist_entity")
data class ToDoListEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val text: String,

    val isCompleted: Boolean = false,

    val category: TodoCategory
)