package com.example.omega_v1_0.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.entites.ProjectEntity
import com.example.omega_v1_0.data_layer.entites.SessionEntity
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.ui.model.ActiveSessionUiModel
import com.example.omega_v1_0.ui.model.AllProjectsUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

// viewmodel - for logic, connecting repositor, handling navigation, everything, we leave ouly ui part to ui
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

    private val _recentProjects =
        MutableStateFlow<List<ProjectEntity>>(emptyList())

    val recentProjects: StateFlow<List<ProjectEntity>> =
        _recentProjects


    // now we create the latest_experince variable to be used by OmegaNavGraph
  private var latestExperience: Experience? = null

    fun createProject(
        name: String,
        experience: Experience
    ){ /** below use viewModelScope to launch a coroutine
        * as viewMdodelScope is a lifecycle aware and the createProject
 *       * function in rpeository is suspend function
        */
        viewModelScope.launch {
            latestExperience = experience
            val projectId = repository.createProject(
                name = name,
                experience = experience
            )
            _createProjectId.value = projectId
        }

    }
    // function to get all projects
    // Expose an immutable StateFlow to the UI
    val allProjects: StateFlow<List<AllProjectsUiModel>> = repository.getAllProjects()
        .map { entities ->
            // Transform the database entities into a model the UI can use
            entities.map { AllProjectsUiModel(
                id=it.id,
                projectName = it.name,
                createdAt = Date(it.createdAt)) }
            // here to convert long to date object, simply wrap it.createdAt to the Date(it.createdAt) ⭕
        }
        .stateIn(
            scope = viewModelScope, // The lifecycle of the subscription
            started = SharingStarted.WhileSubscribed(5000), // Keep the subscription alive for 5s after UI stops collecting
            initialValue = emptyList() // The initial state before the first value from the DB arrives
        )

    // now function is created to give the latest_experienced value
    fun getLatestExperience(): Experience? = latestExperience

    // function to get the recent projects

    fun loadRecentProjects() {
        viewModelScope.launch {
            _recentProjects.value = repository.getRecentProjects(3)
        }
    }
    // ----------------- function for the invisible Active Session, basically mapping of datalayer to its ui model------
    private val _activeSession =
        MutableStateFlow<ActiveSessionUiModel?>(null)

    val activeSession: StateFlow<ActiveSessionUiModel?> =
        _activeSession

    fun checkActiveSession() {
        viewModelScope.launch {
            val session = repository.getActiveSession()

            if (session == null) {
                _activeSession.value = null
                return@launch  // exit the coroutine
            }

            _activeSession.value = mapToActiveSessionUiModel(session)
        }
    }




    // ----- reading data from table is allowed but not modifying----------
    private suspend fun mapToActiveSessionUiModel(
        session: SessionEntity
    ): ActiveSessionUiModel {
        val phase = repository.getPhaseById(session.phaseId)
        val project = repository.getProjectById(phase.projectId)

        return ActiveSessionUiModel(
            sessionId = session.id,
            projectId = project.id,
            projectName = project.name,
            phaseName = phase.phaseType.name,
            startedAt = session.startTime
        )
    }

    fun stopActiveSession() {
        viewModelScope.launch {

            repository.stopSession()

            // Clear blocking UI state
            _activeSession.value = null

            // Optional but recommended: refresh data
            loadRecentProjects()
        }
    }


}

