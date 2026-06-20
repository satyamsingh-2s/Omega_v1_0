package com.example.omega_v1_0.ui.screens

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega_v1_0.ui.deskOmega.DeskOmegaSkin
import com.example.omega_v1_0.ui.deskOmega.getDeskOmegaSkin
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.omega_v1_0.models.SessionStatus


//DailyRecordViewModel
//↓
//uiState
//↓
//DashboardScreen

@Composable
fun DeskOmegaScreen(

    stopwatchSeconds: Int,

    activeSessionName: String?,

    expectedDurationMinutes: Int?,
    sessionStatus: SessionStatus?,
    onPauseSession: () -> Unit,
    onResumeSession: () -> Unit,

    skin: DeskOmegaSkin

) {

    val view = LocalView.current

    DisposableEffect(Unit) {

        val window =
            (view.context as Activity).window

        val controller =
            WindowInsetsControllerCompat(
                window,
                view
            )

        // Hide status bar
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat
                .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        controller.hide(WindowInsetsCompat.Type.systemBars())

        // Keep screen on
        view.keepScreenOn = true

        onDispose {
            // Show status bar again
            controller.show(WindowInsetsCompat.Type.statusBars())
            // Disable keep screen on
            view.keepScreenOn = false
        }
    }

    val colors = getDeskOmegaSkin(skin)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colors.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            // Session Name
            Text(
                text = activeSessionName ?: "Session",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 15.sp),
                color = colors.content
            )

            // Expected Duration
            expectedDurationMinutes?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "exp. ${it}m",
                    style = MaterialTheme.typography.headlineSmall,
                    color = colors.secondary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            // Stopwatch
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = formatDuration(stopwatchSeconds),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displayLarge,
                color = colors.content
            )

            Spacer(
                modifier = Modifier.height(60.dp)
            )

            Surface(modifier = Modifier.align(Alignment.CenterHorizontally),
                color = colors.background)
            {
                when (sessionStatus) {
                    null -> {
                        Log.d("DeskOmegaScreen", "Session status is null")
                    }

                    SessionStatus.RUNNING -> {
                        CircularIconButton(
                            onClick = onPauseSession,
                            icon = Icons.Filled.Pause,
                            contentDescription = "Pause Session",
                            backgroundColor = colors.background,
                            iconTint = colors.content,
                            size = 56.dp,
                        )
                    }

                    SessionStatus.PAUSED -> {
                        CircularIconButton(
                            onClick = onResumeSession,
                            icon = Icons.Filled.PlayArrow,
                            contentDescription = "Resume Session",
                            backgroundColor = colors.content,
                            iconTint = colors.background,
                            size = 56.dp
                        )
                    }

                }
            }
        }
    }
}
