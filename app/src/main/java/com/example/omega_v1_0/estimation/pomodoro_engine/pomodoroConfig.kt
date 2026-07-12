package com.example.omega_v1_0.estimation.pomodoro_engine

// basically a setting files for pomodoro engine

data class PomodoroConfig(
    val workDurationSeconds: Int,
    val shortBreakDurationSeconds: Int,
    val longBreakDurationSeconds: Int,
    val workCyclesBeforeLongBreak: Int
)