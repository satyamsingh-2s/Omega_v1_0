package com.example.omega_v1_0.navigation.workspace

import com.example.omega_v1_0.navigation.Screen

object WorkspaceRoutes {

    val Workspace.route: String
        get() = when (this) {
            Workspace.DAILY_RECORD -> Screen.DailyRecord.route
            Workspace.PLANNED -> Screen.MainScreen.route
            Workspace.UNPLANNED -> Screen.UnplannedProject.route
        }
}