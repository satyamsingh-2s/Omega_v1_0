package com.example.omega_v1_0.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.ui.model.PhaseTimerUiModel

@Composable
fun PhaseTimerScreen(
    uiState: PhaseTimerUiModel,
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    elapsedSeconds: Int

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        // ---------- Header ----------
        Text(
            text = "TIMER",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = uiState.phaseName,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(32.dp))

        // ---------- Estimated ----------
        Text(
            text = "ESTIMATED TIME",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${uiState.estimatedMinutes}",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "min",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // ---------- Actual ----------
        Text(
            text = "ACTUAL TIME",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "${uiState.actualMinutes}",
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = "min",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        Spacer(Modifier.height(32.dp))

        // ---------- elapsed ----------
        if(isRunning) {
            val minutes = elapsedSeconds / 60
            val seconds = elapsedSeconds % 60

            Text(
                text = "ElAPSED TIME",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = " $minutes",
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "min",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    text = " $seconds",
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "sec",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }

        Spacer(Modifier.height(32.dp))


        // ---------- Buttons ----------
        if(!isRunning) {
            Button(
                onClick = onStart,
                enabled = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("START")
            }
        }
        else {
        Button(
            onClick = onStop,
            enabled = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Text("STOP")
        }
    }

        Spacer(Modifier.height(32.dp))

        Divider()

        Spacer(Modifier.height(24.dp))

        // ---------- Back ----------
        Text(
            text = "← BACK TO DASHBOARD",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                Log.d("Omega", "Back to dashboard feature not implemented")
            }
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PhaseTimerScreenPreview() {
//    MaterialTheme {
//        PhaseTimerScreen(
//            uiState = PhaseTimerUiModel(
//                phaseName = "IDEA",
//                estimatedMinutes = 120,
//                actualMinutes = 0,
//                differencePercent = 0
//            ),
//            isRunning = false,
//            onStart = {},
//            onStop = {}
//        )
//    }
//}
