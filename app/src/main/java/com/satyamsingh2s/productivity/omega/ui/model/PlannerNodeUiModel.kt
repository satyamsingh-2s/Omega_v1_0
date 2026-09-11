package com.satyamsingh2s.productivity.omega.planner.ui

import com.satyamsingh2s.productivity.omega.models_enums.PlannerPriority

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