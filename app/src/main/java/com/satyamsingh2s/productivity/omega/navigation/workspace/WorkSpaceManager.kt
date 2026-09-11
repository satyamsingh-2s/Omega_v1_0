package com.satyamsingh2s.productivity.omega.navigation.workspace

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkspaceManager {

    /**
     * Holds the currently active workspace.
     *
     * Phase 1:
     * Default is DAILY.
     *
     * Future:
     * Initialize this value from SettingsRepository.
     */
    private val _currentWorkspace =
        MutableStateFlow(Workspace.DAILY_RECORD)

    /**
     * Public immutable StateFlow observed by the UI.
     */
    val currentWorkspace: StateFlow<Workspace> =
        _currentWorkspace.asStateFlow()

    /**
     * Switches the current workspace.
     *
     * Future:
     * Persist the selected workspace through SettingsRepository.
     */

    fun setCurrentWorkspace(
        workspace: Workspace
    ) {
        _currentWorkspace.value = workspace
    }

    fun switchWorkspace(
        workspace: Workspace
    ) {
        if (_currentWorkspace.value == workspace) {
            return
        }

        _currentWorkspace.value = workspace
    }

    /**
     * Returns the current workspace synchronously.
     * Useful for navigation and business logic.
     */
    fun getCurrentWorkspace(): Workspace {
        return _currentWorkspace.value
    }
}