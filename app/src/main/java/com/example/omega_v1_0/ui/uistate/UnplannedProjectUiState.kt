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
    val sessionStatus: SessionStatus? = null,
    val runningNodeId: Long? = null

)