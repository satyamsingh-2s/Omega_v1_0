package com.example.omega_v1_0.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.models.PhaseType
import com.example.omega_v1_0.ui.model.DashboardPhaseItem

@Composable
fun ProjectDashboardScreen(
    projectName: String,
    phases: List<DashboardPhaseItem>,
    onPhaseClicked: (Long) -> Unit
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
            Text(
                text = "PHASE",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "EST.",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "ACTUAL",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(1f)
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // ---------- Rows ----------
        phases.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPhaseClicked(item.phaseId) }
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

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

        // ---------- Back to Projects ----------
        Text(
            text = "← BACK TO PROJECTS",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                Log.d("Omega", "Back to projects feature not implemented")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectDashboardScreenPreview() {
    MaterialTheme {
        ProjectDashboardScreen(
            projectName = "thinking......",
            phases = listOf(
                DashboardPhaseItem(1, PhaseType.IDEA, 120, 0),
                DashboardPhaseItem(2, PhaseType.RESEARCH, 120, 0),
                DashboardPhaseItem(3, PhaseType.DEVELOPMENT, 120, 0),
                DashboardPhaseItem(4, PhaseType.DEBUG, 120, 0),
                DashboardPhaseItem(5, PhaseType.POLISH, 120, 0),
            ),
            onPhaseClicked = {}
        )
    }
}
