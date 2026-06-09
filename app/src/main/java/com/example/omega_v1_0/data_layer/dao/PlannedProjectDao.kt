package com.example.omega_v1_0.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.omega_v1_0.data_layer.entites.PlannedProjectEntity
import kotlinx.coroutines.flow.Flow


// here we are creating bridge between database(i.e table) and app
@Dao
interface PlannedProjectDao{
    @Insert
    suspend fun insert(project: PlannedProjectEntity): Long

    @Query("SELECT * FROM planned_projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<PlannedProjectEntity>>

    // function to get the recent projects
    @Query("""
    SELECT p.*
    FROM planned_projects p
    LEFT JOIN phases ph ON ph.projectId = p.id
    LEFT JOIN sessions s ON s.parentId = ph.id
    AND s.parentType = 'PLANNED'
    GROUP BY p.id
    ORDER BY 
        MAX(s.endTime) DESC,
        p.createdAt DESC
    LIMIT :limit
""")
    suspend fun getRecentProjects(limit: Int): List<PlannedProjectEntity>

    // function to get project name from database
    @Query("SELECT * FROM planned_projects WHERE id = :projectId")
    suspend fun getProjectById(projectId: Long): PlannedProjectEntity

    // function to delete project from database
        @Query("DELETE FROM planned_projects WHERE id = :projectId")
        suspend fun deleteProjectById(projectId: Long)




}


/**Line	Purpose
 *
@Dao	Marks DB access interface
@Insert	Insert a project
suspend	Safe background execution
Long return	Get generated ID
@Query	Read projects
Flow	Auto-updating stream
Interface   	It makes the file iterface, in interface
We can define what is allowed and what is not.
ORDER BY createdAt DESC	It sorts the master table(projects) with latest projects
<List<PlannedProjectEntity>	It gives all the data of class name PlannedProjectEntity
For now, I stop my doubt and simply learn that
We created a table(i.e project etntiy) class, which has parametes or main purpose define sturture of table,
Also it converts every row to the instace of the projectEntity class
Flow	It is the type(interface, just like list and map) that function is to emit the type of data in<> , whenever change in data is there, if no change in data no emmision
Flow does NOT emit “what changed”.
Flow emits “the latest result of the query”.
Imp.
If collector ask, then flow will emit data otherwise on changed and when first time
**/