package com.example.omega_v1_0.planner.ui

import com.example.omega_v1_0.models_enums.PlannerPriority

data class PlannerNodeUiModel(

    // Identity
    val nodeId: Long,

    // Display
    val title: String,

    // Tree
    val parentNodeId: Long?,
    val depth: Int,
    val isLeaf: Boolean,

    // Planner
    val priority: PlannerPriority,
    val priorityOrder: Int,
    val todayOrder: Int?,

    // Progress
    val isCompleted: Boolean,
    val expectedDurationSeconds: Int?
)