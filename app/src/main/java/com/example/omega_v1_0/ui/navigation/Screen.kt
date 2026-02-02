package com.example.omega_v1_0.ui.navigation

import com.example.omega_v1_0.models.Experience


sealed class Screen(val route:String) {
// here we define all the navigation routes as objects .
    object CreateProject : Screen("create_project")

    object Estimate : Screen("estimate/{projectId}/{experience}") {
        fun createRoute(projectId: Long, experience: Experience) = "estimate/$projectId/${experience.name}"
    }


    object Dashboard : Screen("dashboard/{projectId}") {
        fun createRoute(projectId: Long) ="dashboard/$projectId"
    }

    object PhaseTimer : Screen("phase_timer/{phaseId}") {
        fun createRoute(phaseId: Long) = "phase_timer/$phaseId"
    }
}

// navigation done
// here we deifne the route strings for each screen, or we can say routes