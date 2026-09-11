package com.satyamsingh2s.productivity.omega.planner.ui

data class PlannerUiState(

    val isLoading: Boolean = true,

    val criticalTasks: List<PlannerNodeUiModel> = emptyList(),

    val highTasks: List<PlannerNodeUiModel> = emptyList(),

    val mediumTasks: List<PlannerNodeUiModel> = emptyList(),

    val lowTasks: List<PlannerNodeUiModel> = emptyList(),

    val backlogTasks: List<PlannerNodeUiModel> = emptyList(),

    val todayQueue: List<PlannerNodeUiModel> = emptyList(),
)