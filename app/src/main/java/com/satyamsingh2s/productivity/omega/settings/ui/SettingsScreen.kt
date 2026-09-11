package com.satyamsingh2s.productivity.omega.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.settings.ui.components.SettingStepper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text("Settings")
                }
            )

        }

    ) { innerPadding ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

            contentPadding = PaddingValues(16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            item {

                Text(
                    text = "Pomodoro",
                    style = MaterialTheme.typography.headlineSmall
                )

            }

            item {

                SettingStepper(
                    title = "Work Duration",
                    value = uiState.workDurationMinutes,
                    min = 5,
                    max = 120,
                    step = 5,
                    suffix = "min",
                    onValueChanged = viewModel::updateWorkDuration
                )

            }

            item {

                SettingStepper(
                    title = "Short Break",
                    value = uiState.shortBreakMinutes,
                    min = 1,
                    max = 30,
                    step = 1,
                    suffix = "min",
                    onValueChanged = viewModel::updateShortBreak
                )

            }

            item {

                SettingStepper(
                    title = "Long Break",
                    value = uiState.longBreakMinutes,
                    min = 5,
                    max = 60,
                    step = 5,
                    suffix = "min",
                    onValueChanged = viewModel::updateLongBreak
                )

            }

            item {

                SettingStepper(
                    title = "Work Cycles",
                    value = uiState.workCyclesBeforeLongBreak,
                    min = 1,
                    max = 10,
                    step = 1,
                    suffix = "cycles",
                    onValueChanged = viewModel::updateWorkCycles
                )

            }

            item {

                OutlinedButton(

                    modifier = Modifier.fillMaxWidth(),

                    onClick = {
                        viewModel.resetDefaults()
                    }

                ) {

                    Text("Reset Defaults")

                }

            }

            item {

                Button(

                    modifier = Modifier.fillMaxWidth(),

                    enabled = uiState.hasUnsavedChanges,

                    onClick = {
                        viewModel.saveSettings()
                    }

                ) {

                    Text("Apply")

                }

            }

        }

    }

}