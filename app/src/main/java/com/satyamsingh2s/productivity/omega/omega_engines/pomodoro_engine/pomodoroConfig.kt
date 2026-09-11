package com.satyamsingh2s.productivity.omega.omega_engines.pomodoro_engine

// basically a setting files for pomodoro engine

data class PomodoroConfig(
    val workDurationSeconds: Int,
    val shortBreakDurationSeconds: Int,
    val longBreakDurationSeconds: Int,
    val workCyclesBeforeLongBreak: Int
)