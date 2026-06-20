package com.example.omega_v1_0.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega_v1_0.R
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.ui.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun OmegaSplashScreen(
    repository: Omega_Repository, navController: NavController
) {
    LaunchedEffect(Unit){

        delay(1800)


        val hasActiveSession =
            repository.getActiveSession() != null

        val hasActiveBreak =
            repository.getActiveBreak() != null

        if (
            hasActiveSession ||
            hasActiveBreak
        ) {

            navController.navigate(Screen.DailyRecord.route
            ){ popUpTo(
                Screen.OmegaSplashScreen.route)
            {
                inclusive = true
            }
            }

        } else {

            navController.navigate(
                Screen.MainScreen.route
            ){
                // below 4 line code - it navigates to main screen and remove laucher screen from backstack
                popUpTo(
                    Screen.OmegaSplashScreen.route) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(R.drawable.icon),
            contentDescription = null,
            modifier = Modifier.size(90.dp)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "OMEGA",
            fontSize = 38.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 8.sp,
            color = Color(0xFFE9E9E9)
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "PRODUCTIVITY APP",
            fontSize = 14.sp,
            letterSpacing = 6.sp,
            color = Color(0xB4B4B4FF)
        )

        Spacer(Modifier.height(80.dp))
        SplashWords()
    }
}

// ---- animated words
@Composable
private fun SplashWords() {

    var visibleIndex by remember {
        mutableIntStateOf(0)
    }

    val words = listOf(

        "Focus.",

        "Organize.",

        "Execute."
    )

    LaunchedEffect(Unit) {
        repeat(words.size) {
            delay(400)
            visibleIndex = it + 1
        }
    }

    Column(

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        words.forEachIndexed { index, word ->

            AnimatedVisibility(
                visible = index < visibleIndex,
                enter = fadeIn() + slideInVertically()
            ) {

                Text(

                    text = word,

                    fontSize = 26.sp,

                    fontWeight = FontWeight.Medium,

                    color = Color(0xE9E9E9FF)
                )

            }

            Spacer(Modifier.height(12.dp))
        }
    }
}