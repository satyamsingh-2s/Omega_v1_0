package com.example.omega_v1_0.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.data_layer.entites.ProjectEntity
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.ui.model.ActiveSessionUiModel

@Composable
fun CreateProjectScreen(
    recentProjects: List<ProjectEntity>,
    activeSession: ActiveSessionUiModel?,
    onCreateClicked: (String, Experience) -> Unit,
    onRecentProjectClicked: (Long) -> Unit,
    onStopActiveSession: () -> Unit
) {
    var projectName by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf(Experience.INTERMEDIATE) }

    Box(modifier = Modifier.fillMaxSize()) {

        // ---------- Main Content ----------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .alpha(if (activeSession != null) 0.4f else 1f)
        ) {

            // ---------- Header ----------
            Text(
                text = "Omega",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "v1.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            // ---------- Recent Projects ----------
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {

                    Text(
                        text = "RECENT PROJECTS",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    if (recentProjects.isEmpty()) {
                        Text(
                            text = "No recent projects",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        recentProjects.forEach { project ->
                            Text(
                                text = project.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = activeSession == null) {
                                        onRecentProjectClicked(project.id)
                                    }
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(44.dp))

            // ---------- Project Name ----------
            Text(
                text = "PROJECT NAME",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = projectName,
                onValueChange = { projectName = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = activeSession == null
            )

            Spacer(Modifier.height(24.dp))

            // ---------- Experience ----------
            Text(
                text = "EXPERIENCE LEVEL",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            ExperienceDropdown(
                selected = experience,
                onSelected = { experience = it },
                enabled = activeSession == null
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ---------- Create Button ----------
            Button(
                onClick = { onCreateClicked(projectName, experience) },
                enabled = projectName.isNotBlank() && activeSession == null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("CREATE PROJECT")
            }
        }

        // ---------- Active Session Bottom Box ----------
        if (activeSession != null) {
            ActiveSessionBottomBox(
                activeSession = activeSession,
                onStopSession = onStopActiveSession,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceDropdown(
    selected: Experience,
    onSelected: (Experience) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected.name.lowercase().replaceFirstChar { it.uppercase() },
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Experience.entries.forEach { level ->
                DropdownMenuItem(
                    text = {
                        Text(level.name.lowercase().replaceFirstChar { it.uppercase() })
                    },
                    onClick = {
                        onSelected(level)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ActiveSessionBottomBox(
    activeSession: ActiveSessionUiModel,
    onStopSession: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "ACTIVE SESSION",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Project: ${activeSession.projectName}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Phase: ${activeSession.phaseName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "This session is still running.\nPlease stop it to continue.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onStopSession,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("STOP SESSION")
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Create Project - Normal"
)
@Composable
fun CreateProjectScreenPreview_Normal() {
    MaterialTheme {
        CreateProjectScreen(
            recentProjects = emptyList(),
            activeSession = ActiveSessionUiModel(
                sessionId = 10L,
                projectId = 1L,
                projectName = "Omega v1",
                phaseName = "ESTIMATION",
                startedAt = System.currentTimeMillis()
            ),
            onCreateClicked = { _, _ -> },
            onRecentProjectClicked = {},
            onStopActiveSession = {}
        )
    }
}
