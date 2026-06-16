package com.example.omega_v1_0.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.ui.navigation.Screen

@Composable
fun LauncherScreen(repository: Omega_Repository, navController: NavController
) {

    LaunchedEffect(Unit) {

        val hasActiveSession =
            repository.getActiveSession() != null

        val hasActiveBreak =
            repository.getActiveBreak() != null

        if (
            hasActiveSession ||
            hasActiveBreak
        ) {

            navController.navigate(
                Screen.DailyRecord.route
            ){ popUpTo(
                Screen.Launcher.route
            ) {
                inclusive = true
            }}

        } else {

            navController.navigate(
                Screen.MainScreen.route
            ){
                // below 4 line code - it navigates to main screen and remove laucher screen from backstack
                popUpTo(
                    Screen.Launcher.route
                ) {
                    inclusive = true
                }
            }
        }

    }
    Box(
        modifier = Modifier.fillMaxSize()
            //.background(MaterialTheme.colorScheme.background),
        ,contentAlignment = Alignment.Center
    ) {

     //   Text("")
    }
 }