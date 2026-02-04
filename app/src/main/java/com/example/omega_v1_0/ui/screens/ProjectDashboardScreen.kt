package com.example.omega_v1_0.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.models.PhaseType
import com.example.omega_v1_0.ui.model.DashboardPhaseItem

@Composable
fun ProjectDashboardScreen(
    projectName: String,
    phases: List<DashboardPhaseItem>,
    runningPhaseId: Long?,
    onPhaseClicked: (Long) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
    ) {

        // ---------- Header ----------
        Text(
            text = "DASHBOARD",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = projectName,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(24.dp))

        // ---------- Table Header ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("PHASE", modifier = Modifier.weight(1f))
            Text("EST.", modifier = Modifier.weight(1f))
            Text("ACTUAL", modifier = Modifier.weight(1f))
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // ---------- Rows ----------
        phases.forEach { item ->

            val isRunning = item.phaseId == runningPhaseId

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPhaseClicked(item.phaseId) }
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (isRunning) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Running session",
                        tint = Color.Green,
                        modifier = Modifier
                            .size(22.dp)
                            .padding(end = 4.dp)
                    )
                } else {
                    Spacer(modifier = Modifier.width(26.dp))
                }

                Text(
                    text = item.phaseType.name,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${item.estimatedMinutes}m",
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${item.actualMinutes}m",
                    modifier = Modifier.weight(1f)
                )
            }

            Divider()
        }

        Spacer(Modifier.height(32.dp))

        // ---------- Back ----------
        Text(
            text = "← BACK TO PROJECTS",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onBack() }
        )
    }
}
