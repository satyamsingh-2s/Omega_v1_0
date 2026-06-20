package com.example.omega_v1_0.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.ui.uistate.UnplannedProjectUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UnplannedProjectViewModel(
    private val repository: Omega_Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UnplannedProjectUiState())  // used to change
    val uiState = _uiState.asStateFlow() // only access to read, so it is used by all to read the data

    init {
        observeTree()
        observeSession()
    }

    private fun observeTree() {

        viewModelScope.launch {
            repository
                .getUnplannedTree()
                .collect { tree ->
                    _uiState.update {
                        it.copy(tree = tree)
                    }
                }
        }
    }

    fun createRootNode(
        title: String
    ) {

        viewModelScope.launch {

            repository.createRootNode(
                title
            )
        }
    }

    fun createChildNode(

        parentId: Long,

        title: String

    ) {

        viewModelScope.launch {

            repository.createChildNode(

                parentId,

                title
            )
        }
    }

    fun renameNode(

        nodeId: Long,

        title: String

    ) {

        viewModelScope.launch {

            repository.renameNode(

                nodeId,

                title
            )
        }
    }

    fun updateExpectedDuration(
        nodeId: Long,
        expectedDurationSeconds: Int?
    ) {

        viewModelScope.launch {
            repository.updateExpectedDuration(
                nodeId,
                expectedDurationSeconds
            )
        }
    }

    fun markCompleted(nodeId: Long
    ) {
        viewModelScope.launch {
            repository.markNodeCompleted(nodeId)
        }
    }

    fun markIncomplete(nodeId: Long
    ) {
        viewModelScope.launch {
            repository.markNodeIncomplete(nodeId)
        }
    }

    fun toggleCompleted(nodeId: Long, isCompleted: Boolean
    ) {
        if (isCompleted) {
            markIncomplete(nodeId)
        } else {
            markCompleted(nodeId)
        }
    }

    fun startSession(
        nodeId: Long,
        sessionName: String?,
        expectedDurationMinutes: Int?

    ) {

        viewModelScope.launch {
            repository.startUnplannedSession(
                nodeId,
                sessionName,
                expectedDurationMinutes
            )
        }
    }

    fun pauseSession() {

        viewModelScope.launch {
            repository.pauseUnplannedSession()
        }
    }

    fun resumeSession() {

        viewModelScope.launch {
            repository.resumeUnplannedSession()
        }
    }

    fun stopSession() {

        viewModelScope.launch {
            repository.stopUnplannedSession()
        }
    }

    // ------------- root dilagoue
    fun showAddRootDialog() {

        _uiState.update {
            it.copy(
                showAddRootDialog = true,
                dialogInput = ""
            )
        }
    }

    fun hideAddRootDialog() {
        _uiState.update {
            it.copy(
                showAddRootDialog = false
            )
        }
    }

    // ------------- child dialogue ----------------
    fun showAddChildDialog(
        nodeId: Long
    ) {

        _uiState.update {

            it.copy(
                selectedNodeId = nodeId,
                showAddChildDialog = true,
                dialogInput = ""
            )
        }
    }

    fun hideAddChildDialog() {

        _uiState.update {
            it.copy(
                showAddChildDialog = false
            )
        }
    }

    // ------------Input handelr --------------
    fun onDialogInputChanged(
        value: String
    ) {

        _uiState.update {
            it.copy(
                dialogInput = value
            )
        }
    }
// ------------- confirm function -----------------
    fun confirmAddRoot() {

        val title = uiState.value.dialogInput
        if (title.isBlank()
        ) return

        createRootNode(title)

        hideAddRootDialog()
    }

    fun confirmAddChild() {

        val parentId = uiState.value.selectedNodeId ?: return

        val title = uiState.value.dialogInput
        if (
            title.isBlank()

        ) return
        createChildNode(parentId, title)
        hideAddChildDialog()
    }

    fun onNodeClick( nodeId: Long){

    }


    private fun observeSession() {

        viewModelScope.launch {

            repository
                .observeActiveSession()
                .collect { activeSession ->

                    if (activeSession == null) {
                        _uiState.update {
                            it.copy(
                                sessionStatus = null,
                                runningNodeId = null
                            )
                        }

                    } else {
                        _uiState.update {
                            it.copy(
                                sessionStatus = activeSession.status,
                                runningNodeId = repository.getRunningNodeId()
                            )
                        }
                    }
                }
        }
    }
}