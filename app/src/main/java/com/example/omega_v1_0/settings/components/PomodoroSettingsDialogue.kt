package com.example.omega_v1_0.settings.components

import androidx.compose.runtime.Composable

@Composable
fun PomodoroSettingsDialog(

    workDuration: String,
    shortBreakDuration: String,
    longBreakDuration: String,
    workCycles: String,

    onWorkDurationChanged: (String) -> Unit,
    onShortBreakDurationChanged: (String) -> Unit,
    onLongBreakDurationChanged: (String) -> Unit,
    onWorkCyclesChanged: (String) -> Unit,

    onApply: () -> Unit,
    onDismiss: () -> Unit
){

}