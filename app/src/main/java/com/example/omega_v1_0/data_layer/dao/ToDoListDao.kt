package com.example.omega_v1_0.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.omega_v1_0.data_layer.entites.ToDoListEntity
import com.example.omega_v1_0.models.TodoCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoListDao {
    @Query("""
    SELECT *
    FROM todolist_entity
    ORDER BY id ASC
""")
    fun getAllFocusItems(): Flow<List<ToDoListEntity>>

    @Insert
    suspend fun insert(item: ToDoListEntity)

    @Query("""
    UPDATE todolist_entity
    SET isCompleted = :isCompleted
    WHERE id = :itemId
""")
    suspend fun updateCompleted(
        itemId: Long, isCompleted: Boolean)

    @Query("""
    DELETE FROM todolist_entity
    WHERE id = :itemId
""")
    suspend fun delete(itemId: Long)

    @Query("""
    SELECT COUNT(*)
    FROM todolist_entity
    WHERE category = :category
""")
    suspend fun getItemCountCategory(category: TodoCategory): Int

    @Query("""
SELECT *
FROM todolist_entity
WHERE category = :category
ORDER BY id ASC
""")
    fun getTodosByCategory(category: TodoCategory
    ): Flow<List<ToDoListEntity>>
    
}