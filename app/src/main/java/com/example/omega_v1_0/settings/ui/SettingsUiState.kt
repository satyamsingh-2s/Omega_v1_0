package com.example.omega_v1_0.settings.ui

data class SettingsUiState(

    // Pomodoro Settings
    val workDurationMinutes: Int = 25,
    val shortBreakMinutes: Int = 5,
    val longBreakMinutes: Int = 15,
    val workCyclesBeforeLongBreak: Int = 4,

    // UI State
    val isLoading: Boolean = true,
    val hasUnsavedChanges: Boolean = false
)