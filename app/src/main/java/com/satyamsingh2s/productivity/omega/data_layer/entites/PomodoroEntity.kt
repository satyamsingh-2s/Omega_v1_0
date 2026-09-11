package com.satyamsingh2s.productivity.omega.data_layer.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.satyamsingh2s.productivity.omega.omega_engines.pomodoro_engine.PomodoroPhase

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