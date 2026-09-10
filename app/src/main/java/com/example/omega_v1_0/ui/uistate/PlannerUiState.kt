package com.example.omega_v1_0.planner.ui

import com.example.omega_v1_0.models_enums.PlannerPriority

data class PlannerUiState(

    val isLoading: Boolean = true,

    val criticalTasks: List<PlannerNodeUiModel> = emptyList(),

    val highTasks: List<PlannerNodeUiModel> = emptyList(),

    val mediumTasks: List<PlannerNodeUiModel> = emptyList(),

    val lowTasks: List<PlannerNodeUiModel> = emptyList(),

    val backlogTasks: List<PlannerNodeUiModel> = emptyList(),

    val todayQueue: List<PlannerNodeUiModel> = emptyList(),
)