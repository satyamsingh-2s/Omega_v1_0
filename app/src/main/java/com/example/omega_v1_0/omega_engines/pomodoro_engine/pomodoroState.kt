package com.example.omega_v1_0.omega_engines.pomodoro_engine

data class PomodoroState(

    val phase: PomodoroPhase,

    val remainingSeconds: Int,

    val completedWorkCycles: Int,

    val isRunning: Boolean,
    val isEnabled: Boolean,
)