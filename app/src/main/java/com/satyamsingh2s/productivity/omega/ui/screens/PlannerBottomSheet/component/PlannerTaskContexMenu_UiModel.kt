package com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component

import com.satyamsingh2s.productivity.omega.planner.ui.PlannerNodeUiModel


data class PlannerTaskContextMenuState(

    val isVisible: Boolean = false,

    val selectedNode: PlannerNodeUiModel? = null
)