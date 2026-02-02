package com.example.omega_v1_0.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.models.Experience

@Composable
fun CreateProjectScreen(
    onCreateClicked: (String, Experience) -> Unit
) {
    var projectName by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf(Experience.INTERMEDIATE) }

    // temporary recent projects
    val recentProjects = listOf("badd")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
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
        Text(
            text = "RECENT PROJECTS",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(recentProjects) { project ->
                Text(
                    text = project,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Log.d("Omega", "Recent project feature not implemented")
                        }
                        .padding(vertical = 12.dp)
                )
                Divider()
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
            singleLine = true
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
            onSelected = { experience = it }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // ---------- Create Button ----------
        Button(
            onClick = { onCreateClicked(projectName, experience) },
            enabled = projectName.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text("CREATE PROJECT")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExperienceDropdown(
    selected: Experience,
    onSelected: (Experience) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected.name.lowercase().replaceFirstChar { it.uppercase() },
            onValueChange = {},
            readOnly = true,
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


@Preview(showBackground = true)
@Composable
fun CreateProjectScreenPreview() {
    MaterialTheme {
        CreateProjectScreen(
            onCreateClicked = { _, _ -> }
        )
    }
}

//@Preview(
//    showBackground = true,
//    device = Devices.PIXEL_7
//)
//@Composable
//fun CreateProjectScreenPixelPreview() {
//    MaterialTheme {
//        CreateProjectScreen(
//            onCreateClicked = { _, _ -> }
//        )
//    }
//}
