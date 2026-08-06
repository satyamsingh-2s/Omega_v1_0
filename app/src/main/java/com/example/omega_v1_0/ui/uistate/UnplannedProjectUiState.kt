package com.example.omega_v1_0.ui.uistate

import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.ui.model.UnplannedProjectUiModel


// -- it is bridge between viewmodel -- uistate -- screen
// --- it holds the data for the screen only...

data class UnplannedProjectUiState (

    val tree: List<UnplannedProjectUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val selectedNodeId: Long? = null,
    val activeSessionId: Long? = null,

    // ---------- dialogs ----------

    val showAddRootDialog: Boolean = false,
    val showAddChildDialog: Boolean = false,
    val dialogInput: String = "",

    // -------- session part ----
//    val sessionStatus: SessionStatus? = null,
//    val runningNodeId: Long? = null,

    // -------- expected durations part -----
    val showExpectedDurationDialog: Boolean = false,
    val selectedExpectedDurationNodeId: Long? = null,
    val expectedDurationInput: String = "",

    // ------------- rename part
    val showRenameDialog: Boolean = false,
    val selectedRenameNodeId: Long? = null,
    val renameInput: String = "",

    // ------------- delete part ---------
    val showDeleteDialog: Boolean = false,
    val selectedDeleteNodeId: Long? = null,

    val showStatsDialog: Boolean = false,
    val selectedStatsNode: UnplannedProjectUiModel? = null,

    // -----------------------
    // NEW FEATURE : SINGLE BRANCH EXPANSION
    // Stores the currently expanded hierarchy.
    // Example:
    // [Project2, Engine, Database]
    // -----------------------
    val expandedPath: List<Long> = emptyList(),

//    val activeNodeTitle: String? = null,
//    val activeExpectedDuration: Int? = null,
//    val activeCurrentDuration: Int? = null,
//    val stopwatchSeconds: Int = 0

    val showSessionAlreadyRunningDialog: Boolean = false,
    val pendingSessionNodeId: Long? = null,

    // ---------- Navigation ----------
    val focusedNode: UnplannedProjectUiModel? = null,
    val visibleChildren: List<UnplannedProjectUiModel> = emptyList(),
    val canNavigateBack: Boolean = false,

    // ---- for the notes feature
    val revisionNoteSummary: String = "",
    val workingNodeId: Long? =null,

    ){

    // -----------------------
    // NEW FEATURE : SINGLE BRANCH EXPANSION
    // Backward-compatible bridge for existing call sites.
    // This is derived from expandedPath (not stored separately).
    // -----------------------
    val expandedNodeIds: Set<Long>
        get() = expandedPath.toSet()
}
