package com.satyamsingh2s.productivity.omega.navigation.workspace

import com.satyamsingh2s.productivity.omega.navigation.Screen

object WorkspaceRoutes {

    val Workspace.route: String
        get() = when (this) {
            Workspace.DAILY_RECORD -> Screen.DailyRecord.route
            Workspace.PLANNED -> Screen.MainScreen.route
            Workspace.UNPLANNED -> Screen.UnplannedProject.route
        }
}