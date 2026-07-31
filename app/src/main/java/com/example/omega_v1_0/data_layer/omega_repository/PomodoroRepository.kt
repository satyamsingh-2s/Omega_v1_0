package com.example.omega_v1_0.data_layer.omega_repository

import com.example.omega_v1_0.data_layer.dao.PomodoroDao
import com.example.omega_v1_0.data_layer.entites.PomodoroEntity
import com.example.omega_v1_0.omega_engines.pomodoro_engine.PomodoroConfig
import com.example.omega_v1_0.omega_engines.pomodoro_engine.PomodoroState

class PomodoroRepository(

    private val pomodoroDao: PomodoroDao

) {

    // -------------------------------------------------------
    // Public API
    // -------------------------------------------------------

    /**
     * Saves the current Pomodoro runtime.
     *
     * Flow:
     * State + Config
     *      ↓
     * PomodoroEntity
     *      ↓
     * Room
     */
    suspend fun saveRuntime(
        state: PomodoroState,
        config: PomodoroConfig,
        startedAt: Long
    ) {

        pomodoroDao.savePomodoro(

            createEntity(
                state = state,
                config = config,
                startedAt = startedAt
            )
        )
    }

    /**
     * Returns the stored runtime entity.
     */
    suspend fun getRuntime(): PomodoroEntity? {

        return pomodoroDao.getPomodoro()
    }

    /**
     * Returns runtime as domain state.
     *
     * Room
     *   ↓
     * State
     */
    suspend fun getRuntimeState(): PomodoroState? {

        return getRuntime()?.toState()
    }

    /**
     * Returns runtime configuration.
     *
     * Room
     *   ↓
     * Config
     */
    suspend fun getRuntimeConfig(): PomodoroConfig? {

        return getRuntime()?.toConfig()
    }

    /**
     * Deletes saved runtime.
     */
    suspend fun deleteRuntime() {

        pomodoroDao.deletePomodoro()
    }

    /**
     * Checks whether a runtime exists.
     */
    suspend fun hasRuntime(): Boolean {

        return getRuntime() != null
    }

    // -------------------------------------------------------
    // Mapping Helpers
    // -------------------------------------------------------

    /**
     * State + Config
     *      ↓
     * Room Entity
     */
    private fun createEntity(

        state: PomodoroState,
        config: PomodoroConfig,
        startedAt: Long

    ): PomodoroEntity {

        return PomodoroEntity(

            phase = state.phase,
            remainingSeconds = state.remainingSeconds,
            completedWorkCycles = state.completedWorkCycles,
            isRunning = state.isRunning,
            startedAt = startedAt,

            workDurationSeconds = config.workDurationSeconds,
            shortBreakDurationSeconds = config.shortBreakDurationSeconds,
            longBreakDurationSeconds = config.longBreakDurationSeconds,
            workCyclesBeforeLongBreak =
                config.workCyclesBeforeLongBreak
        )
    }

    /**
     * Room Entity
     *      ↓
     * Domain State
     */
    private fun PomodoroEntity.toState(): PomodoroState {

        return PomodoroState(

            phase = phase,
            remainingSeconds = remainingSeconds,
            completedWorkCycles = completedWorkCycles,
            isRunning = isRunning,

            // Runtime exists ⇒ Pomodoro was enabled
            isEnabled = true
        )
    }

    /**
     * Room Entity
     *      ↓
     * Domain Config
     */
    private fun PomodoroEntity.toConfig(): PomodoroConfig {

        return PomodoroConfig(

            workDurationSeconds = workDurationSeconds,
            shortBreakDurationSeconds = shortBreakDurationSeconds,
            longBreakDurationSeconds = longBreakDurationSeconds,
            workCyclesBeforeLongBreak =
                workCyclesBeforeLongBreak
        )
    }
}