package com.example.omega_v1_0.estimation.pomodoro_engine

data class PomodoroState(

    val phase: PomodoroPhase,

    val remainingSeconds: Int,

    val completedWorkCycles: Int,

    val isRunning: Boolean,
    val isEnabled: Boolean,
)