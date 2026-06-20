package com.example.omega_v1_0.ui.model


// -- it is the bridge between repo -- ui model -- viewmodel
// - it represents the node,
data class UnplannedProjectUiModel(

    val nodeId: Long,

    val title: String,

    val children: List<UnplannedProjectUiModel>,

    val expectedDurationSeconds: Int,

    val currentDurationSeconds: Int,

    val isCompleted: Boolean
)