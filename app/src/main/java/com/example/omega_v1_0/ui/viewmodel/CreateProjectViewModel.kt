package com.example.omega_v1_0.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.models.Experience
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// viewmodel - for logic, connectng repositor, handling navigation, everything, we leave ouly ui part to ui
class CreateProjectViewModel (
    private val repository: Omega_Repository)
    : ViewModel()
    /** here above 2 lines says.
     * one is constructor with parameter repository of type Omega_Repository
     * second is class inhereting the properties of ViewModel class
     */
{
    private val _createProjectId = MutableStateFlow<Long?>(null)
    val createProjectId: StateFlow<Long?> = _createProjectId
    /** above 2 line have more to lear
     * stateFlow is for the data, that changes and it tells , mutalble be changeable
     */
    // now we create the latest_experince variable to be used by OmegaNavGraph
  private var latestExperience: Experience? = null

    fun createProject(
        name: String,
        experience: Experience
    ){ /** below use viewModelScope to launch a coroutine
        * as viewMdodelScope is a lifecycle aware and the createProject
 *       * function in rpeository is suspend function
        */
        // ❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌ checking it is called or not
        Log.d("OMEGA_DB", "⭕⭕⭕⭕⭕⭕⭕⭕⭕⭕⭕Creating project with experience=$experience")

        viewModelScope.launch {
            latestExperience = experience
            val projectId = repository.createProject(
                name = name,
                experience = experience
            )
            _createProjectId.value = projectId
            // ❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌ checking it is created or not
            Log.d("OMEGA_DB", "⭕⭕⭕⭕⭕⭕⭕⭕Project created with id=$projectId")
        }

    }
    // now function is created to give the latest_experienced value
    fun getLatestExperience(): Experience? = latestExperience
}

