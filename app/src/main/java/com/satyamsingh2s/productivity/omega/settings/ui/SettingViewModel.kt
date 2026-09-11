package com.satyamsingh2s.productivity.omega.settings.ui

import androidx.lifecycle.ViewModel
import com.satyamsingh2s.productivity.omega.data_layer.omega_repository.Omega_Repository
import com.satyamsingh2s.productivity.omega.omega_engines.pomodoro_engine.PomodoroConfig
import com.satyamsingh2s.productivity.omega.settings.models.PomodoroDefaults
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(
    private val repository: Omega_Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    /**
     * Loads the saved Pomodoro settings from repository.
     */
    private fun loadSettings() {
        val config = repository.getPomodoroConfig()

        _uiState.update {
            it.copy(
                workDurationMinutes = config.workDurationSeconds / 60,
                shortBreakMinutes = config.shortBreakDurationSeconds / 60,
                longBreakMinutes = config.longBreakDurationSeconds / 60,
                workCyclesBeforeLongBreak = config.workCyclesBeforeLongBreak,
                isLoading = false,
                hasUnsavedChanges = false
            )
        }
    }

    fun updateWorkDuration(minutes: Int) {
        _uiState.update {
            it.copy(
                workDurationMinutes = minutes,
                hasUnsavedChanges = true
            )
        }
    }

    fun updateShortBreak(minutes: Int) {
        _uiState.update {
            it.copy(
                shortBreakMinutes = minutes,
                hasUnsavedChanges = true
            )
        }
    }

    fun updateLongBreak(minutes: Int) {
        _uiState.update {
            it.copy(
                longBreakMinutes = minutes,
                hasUnsavedChanges = true
            )
        }
    }

    fun updateWorkCycles(cycles: Int) {
        _uiState.update {
            it.copy(
                workCyclesBeforeLongBreak = cycles,
                hasUnsavedChanges = true
            )
        }
    }

    /**
     * Saves the current UI values to SharedPreferences.
     */
    fun saveSettings() {

        val state = _uiState.value

        val config = PomodoroConfig(
            workDurationSeconds = state.workDurationMinutes * 60,
            shortBreakDurationSeconds = state.shortBreakMinutes * 60,
            longBreakDurationSeconds = state.longBreakMinutes * 60,
            workCyclesBeforeLongBreak = state.workCyclesBeforeLongBreak
        )

        repository.savePomodoroConfig(config)

        _uiState.update {
            it.copy(
                hasUnsavedChanges = false
            )
        }
    }

    /**
     * Restores the default values into the UI.
     * These are NOT saved until saveSettings() is called.
     */
    fun resetDefaults() {

        val defaults = PomodoroDefaults.defaultConfig()

        _uiState.update {
            it.copy(
                workDurationMinutes = defaults.workDurationSeconds / 60,
                shortBreakMinutes = defaults.shortBreakDurationSeconds / 60,
                longBreakMinutes = defaults.longBreakDurationSeconds / 60,
                workCyclesBeforeLongBreak = defaults.workCyclesBeforeLongBreak,
                hasUnsavedChanges = true
            )
        }
    }
}