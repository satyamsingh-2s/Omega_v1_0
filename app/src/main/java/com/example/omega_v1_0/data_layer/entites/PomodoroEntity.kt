package com.example.omega_v1_0.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.omega_v1_0.omega_engines.pomodoro_engine.PomodoroPhase

@Entity(tableName = "pomodoro")
data class PomodoroEntity(

    @PrimaryKey
    val id: Int = 1,

    // ---------- Runtime State ----------
    val phase: PomodoroPhase,
    val remainingSeconds: Int,
    val completedWorkCycles: Int,
    val isRunning: Boolean,
    val startedAt: Long,

    // ---------- Configuration ----------
    val workDurationSeconds: Int,
    val shortBreakDurationSeconds: Int,
    val longBreakDurationSeconds: Int,
    val workCyclesBeforeLongBreak: Int
)