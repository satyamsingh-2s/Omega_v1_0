package com.example.omega_v1_0.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.models.*

@Composable
fun EstimateScreen(
    projectName: String,
    experience: Experience,
    onEstimateClicked: (
        phaseInputs: Map<PhaseType, Pair<Complexity, Scope>>,
        experience: Experience
    ) -> Unit
) {

    val phaseInputs = remember {
        PhaseType.entries.associateWith {
            mutableStateOf(Complexity.MEDIUM to Scope.MEDIUM)
        }
    }

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
            text = "ESTIMATE",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = projectName,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(24.dp))

        // ---------- Phases ----------
        PhaseType.entries.forEach { phase ->

            Text(
                text = phase.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "COMPLEXITY",
                        style = MaterialTheme.typography.labelSmall
                    )
                    EnumDropdown(
                        selected = phaseInputs[phase]!!.value.first,
                        values = Complexity.entries,
                        onSelected = {
                            val current = phaseInputs[phase]!!.value
                            phaseInputs[phase]!!.value = it to current.second
                        }
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "SCOPE",
                        style = MaterialTheme.typography.labelSmall
                    )
                    EnumDropdown(
                        selected = phaseInputs[phase]!!.value.second,
                        values = Scope.entries,
                        onSelected = {
                            val current = phaseInputs[phase]!!.value
                            phaseInputs[phase]!!.value = current.first to it
                        }
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))
        }

        Spacer(Modifier.height(24.dp))

        // ---------- Button ----------
        Button(
            onClick = {
                val finalPhaseInputs =
                    phaseInputs.mapValues { it.value.value }
                onEstimateClicked(finalPhaseInputs, experience)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text("CALCULATE & SAVE")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Enum<T>> EnumDropdown(
    selected: T,
    values: List<T>,
    onSelected: (T) -> Unit
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
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            values.forEach {
                DropdownMenuItem(
                    text = {
                        Text(it.name.lowercase().replaceFirstChar { c -> c.uppercase() })
                    },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun EstimateScreenPreview() {
//    MaterialTheme {
//        EstimateScreen(
//            experience = Experience.INTERMEDIATE,
//            onEstimateClicked = { _, _ -> }
//        )
//    }
//}
//

