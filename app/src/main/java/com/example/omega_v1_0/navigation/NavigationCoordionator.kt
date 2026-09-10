package com.example.omega_v1_0.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.omega_v1_0.navigation.workspace.WorkspaceManager
import com.example.omega_v1_0.navigation.workspace.WorkspaceRoutes.route

object NavigationCoordinator {

    fun handle(
        action: NavigationAction,
        navController: NavHostController,
        workspaceManager: WorkspaceManager

    ) {

        when (action) {

            NavigationAction.OpenWorkspace -> {

                val route =
                    workspaceManager
                        .getCurrentWorkspace()
                        .route

                navController.navigateToTopLevel(route)

            }

            NavigationAction.OpenTodo -> {

                navController.navigateToTopLevel(
                    Screen.Planner.route
                )

            }

            NavigationAction.OpenHistory -> {

                // TODO

            }
        }


    }
    private fun NavHostController.navigateToTopLevel(
        route: String
    ) {

        navigate(route) {

            launchSingleTop = true

            restoreState = true

            popUpTo(graph.findStartDestination().id) {

                saveState = true

            }

        }

    }

    fun getSelectedAction(
        route: String?
    ): NavigationAction {

        return when (route) {

            Screen.Planner.route ->
                NavigationAction.OpenTodo


            Screen.DailyRecord.route,
            Screen.UnplannedProject.route,
            Screen.MainScreen.route ->
                NavigationAction.OpenWorkspace
            else ->
                NavigationAction.OpenWorkspace
        }

    }
}

