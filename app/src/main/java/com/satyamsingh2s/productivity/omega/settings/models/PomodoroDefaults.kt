package com.satyamsingh2s.productivity.omega.settings.models

import com.satyamsingh2s.productivity.omega.omega_engines.pomodoro_engine.PomodoroConfig

// -- this is the default configurtin for pomodoro engine
object PomodoroDefaults {

    const val WORK_DURATION_SECONDS = 25 * 60
    const val SHORT_BREAK_DURATION_SECONDS = 5 * 60
    const val LONG_BREAK_DURATION_SECONDS = 15 * 60
    const val WORK_CYCLES_BEFORE_LONG_BREAK = 4

    fun defaultConfig() = PomodoroConfig(
        workDurationSeconds = WORK_DURATION_SECONDS,
        shortBreakDurationSeconds = SHORT_BREAK_DURATION_SECONDS,
        longBreakDurationSeconds = LONG_BREAK_DURATION_SECONDS,
        workCyclesBeforeLongBreak = WORK_CYCLES_BEFORE_LONG_BREAK
    )
}