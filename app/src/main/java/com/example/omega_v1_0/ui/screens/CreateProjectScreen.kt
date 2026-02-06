package com.example.omega_v1_0.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
// import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.data_layer.entites.ProjectEntity
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.ui.model.ActiveSessionUiModel
import com.example.omega_v1_0.ui.model.AllProjectsUiModel
import java.text.SimpleDateFormat
import java.util.Locale.getDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProjectScreen(
    recentProjects: List<ProjectEntity>,
    activeSession: ActiveSessionUiModel?,
    allProjects: List<AllProjectsUiModel>,
    onCreateClicked: (String, Experience) -> Unit,
    onRecentProjectClicked: (Long) -> Unit,
    onStopActiveSession: () -> Unit,

    onAllProjectLongPressed: (AllProjectsUiModel) -> Unit,
    projectToDelete: AllProjectsUiModel?,
    onConfirmDelete: () -> Unit,
    onCancelDelete: () -> Unit
) {
    var projectName by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf(Experience.INTERMEDIATE) }
    var showAllProjectsSheet by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        // ---------- All Projects Popup ----------
        if (showAllProjectsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAllProjectsSheet = false },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                ),

                ) {
                // FIX 2: Apply a modifier to the content to control its height
                Box(modifier = Modifier.fillMaxHeight(0.67f)) {
                    AllProjectsBottomSheetContent(
                        allProjects = allProjects,
                        onProjectClick = { projectId ->
                            showAllProjectsSheet = false
                            onRecentProjectClicked(projectId)
                        },
                        onProjectLongPressed = onAllProjectLongPressed
                    )
                }
            }
        }


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

            // ---------- Recent Projects (read only)----------
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
                        recentProjects.forEachIndexed { index, project ->
                            val textSize =
                                if (index == 0) MaterialTheme.typography.titleMedium
                                else MaterialTheme.typography.bodyMedium

                            Text(
                                text = project.name,
                                style = textSize,
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

                    if (recentProjects.isNotEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            TextButton(
                                onClick = { showAllProjectsSheet = true },
                                modifier = Modifier.align(Alignment.CenterEnd),
                                enabled = activeSession == null
                            ) {
                                Text("Show all projects")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(44.dp))

            // ----------Create Project ----------
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
        //--------------- Delete Confirmation Dialog----------
        if (projectToDelete != null) {
            AlertDialog(
                onDismissRequest = onCancelDelete,
                title = {
                    Text("Delete Project")
                },
                text = {
                    Text("This will permanently delete the project and all its data.")
                },
                confirmButton = {
                    TextButton(onClick = onConfirmDelete) {
                        Text(
                            "Delete",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = onCancelDelete) {
                        Text("Cancel")
                    }
                }
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

/**
 * Dialog popup showing all projects (read-only).
 */
/* ---------------- ALL PROJECTS BOTTOM SHEET ---------------- */

@Composable
fun AllProjectsBottomSheetContent(
    allProjects: List<AllProjectsUiModel>,
    onProjectClick: (Long) -> Unit,
    onProjectLongPressed: (AllProjectsUiModel) -> Unit
) {
    val formatter = remember {
        SimpleDateFormat("dd MMM yyyy", getDefault())
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {

        item {
            Text(
                text = "All Projects",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(12.dp))
        }

        if (allProjects.isEmpty()) {
            item {
                Text(
                    text = "No projects found",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        } else {
            items(allProjects) { project ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { onProjectClick(project.id) },
                            onLongClick = { onProjectLongPressed(project) }
                        )
                        .padding(vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = project.projectName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = formatter.format(project.createdAt),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Divider()
            }
        }
        //item {
       /// Spacer(Modifier.height(32.dp))
        // bottom padding for gesture nav
       //  }
    }
}
