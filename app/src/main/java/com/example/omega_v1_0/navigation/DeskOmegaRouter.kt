package com.example.omega_v1_0.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.omega_v1_0.models_enums.SessionStatusBarModel
import com.example.omega_v1_0.models_enums.SessionType

@Composable
fun DeskOmegaRouter(

    sessionStatusBar: SessionStatusBarModel?,

    navController: NavHostController

) {

    LaunchedEffect(Unit) {

        when (sessionStatusBar?.sessionType) {

            SessionType.DAILY_RECORD -> {

                navController.navigate(Screen.DailyRecord.route) {
                    popUpTo(Screen.DeskOmegaRouter.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

                navController.navigate(Screen.DeskOmega.route) {
                    launchSingleTop = true
                }
            }

            SessionType.UNPLANNED -> {

                navController.navigate(
                    Screen.UnplannedProjectSessionScreen.route
                ) {
                    popUpTo(Screen.DeskOmegaRouter.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

                navController.navigate(Screen.DeskOmega.route) {
                    launchSingleTop = true
                }
            }

            SessionType.PLANNED -> {
                // v1
            }

            null -> {
                navController.popBackStack()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize())
    }
}