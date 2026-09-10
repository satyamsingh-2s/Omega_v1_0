package com.example.omega_v1_0.ui.screens.PlannerBottomSheet.component

import com.example.omega_v1_0.planner.ui.PlannerNodeUiModel


data class PlannerTaskContextMenuState(

    val isVisible: Boolean = false,

    val selectedNode: PlannerNodeUiModel? = null
)