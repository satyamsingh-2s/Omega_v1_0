package com.example.omega_v1_0.settings.repository

import android.content.Context
import com.example.omega_v1_0.omega_engines.pomodoro_engine.PomodoroConfig
import com.example.omega_v1_0.settings.models.PomodoroDefaults
import com.example.omega_v1_0.settings.models.PomodoroPreferenceKeys

class SettingsRepository(
    context: Context
) {

    private val preferences =
        context.getSharedPreferences(
            PomodoroPreferenceKeys.PREFS_NAME,
            Context.MODE_PRIVATE
        )

    // ---------------- Pomodoro ----------------

    fun getPomodoroConfig(): PomodoroConfig {

        return PomodoroConfig(

            workDurationSeconds =
                preferences.getInt(
                    PomodoroPreferenceKeys.WORK_DURATION,
                    PomodoroDefaults.WORK_DURATION_SECONDS
                ),

            shortBreakDurationSeconds =
                preferences.getInt(
                    PomodoroPreferenceKeys.SHORT_BREAK_DURATION,
                    PomodoroDefaults.SHORT_BREAK_DURATION_SECONDS
                ),

            longBreakDurationSeconds =
                preferences.getInt(
                    PomodoroPreferenceKeys.LONG_BREAK_DURATION,
                    PomodoroDefaults.LONG_BREAK_DURATION_SECONDS
                ),

            workCyclesBeforeLongBreak =
                preferences.getInt(
                    PomodoroPreferenceKeys.WORK_CYCLES,
                    PomodoroDefaults.WORK_CYCLES_BEFORE_LONG_BREAK
                )
        )
    }

    fun savePomodoroConfig(
        config: PomodoroConfig
    ) {

        preferences.edit()
            .putInt(
                PomodoroPreferenceKeys.WORK_DURATION,
                config.workDurationSeconds
            )
            .putInt(
                PomodoroPreferenceKeys.SHORT_BREAK_DURATION,
                config.shortBreakDurationSeconds
            )
            .putInt(
                PomodoroPreferenceKeys.LONG_BREAK_DURATION,
                config.longBreakDurationSeconds
            )
            .putInt(
                PomodoroPreferenceKeys.WORK_CYCLES,
                config.workCyclesBeforeLongBreak
            )
            .apply()
    }

    fun resetPomodoroDefaults() {

        savePomodoroConfig(
            PomodoroDefaults.defaultConfig()
        )
    }
}