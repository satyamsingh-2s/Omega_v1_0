package com.satyamsingh2s.productivity.omega.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.satyamsingh2s.productivity.omega.models_enums.TodoCategory

@Entity(tableName = "todolist_entity")
data class ToDoListEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val text: String,

    val isCompleted: Boolean = false,

    val category: TodoCategory
)