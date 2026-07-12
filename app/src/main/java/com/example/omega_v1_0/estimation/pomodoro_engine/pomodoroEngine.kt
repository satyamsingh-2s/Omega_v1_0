package com.example.omega_v1_0.estimation.pomodoro_engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// -- here we are emitting events instead of talking to repsoitory follwing the single resposibilty principle

class PomodoroEngine(

    private val config: PomodoroConfig,
    private val onStateChanged: (PomodoroState) -> Unit,
    private val onEvent: (PomodoroEvent) -> Unit
) {

    private var state = PomodoroState(
        phase = PomodoroPhase.WORK,
        remainingSeconds = config.workDurationSeconds,
        completedWorkCycles = 0,
        isRunning = false,
        isEnabled = false,
    )

    val isEnabled: Boolean // this isEnabled is used in viewmodel
        get() = state.isEnabled  // state.isEnabled -> is taking value from above

    // ---- coroutine scope ---- or -- it's own lifecycle ----
    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    private var tickerJob: Job? = null

    private fun startTicker() {

        tickerJob?.cancel()

        tickerJob = scope.launch {

            while (isActive) {

                if (!state.isRunning) {
                    break
                }

                delay(1000)
                state = state.copy(
                    remainingSeconds =
                        (state.remainingSeconds - 1)
                            .coerceAtLeast(0)
                )
                onStateChanged(state)
                onEvent(PomodoroEvent.Tick)

                // --- if ticker == 0, now transition to next phase
                if (state.remainingSeconds == 0) {
                    transitionToNextPhase()
                }

            }
        }
    }

    fun start() {

        if (state.isRunning) return
        state = state.copy(
            isRunning = true
        )
        onStateChanged(state)
        onEvent(PomodoroEvent.WorkStarted)

        startTicker()
    }

    fun pause() {

        if (!state.isRunning) return
        tickerJob?.cancel()
        tickerJob = null
        state = state.copy(
            isRunning = false
        )
        onStateChanged(state)
    }

    fun resume() {

        if (state.isRunning) return
        state = state.copy(
            isRunning = true
        )
        onStateChanged(state)
        startTicker()
    }

    fun stop() {

        tickerJob?.cancel()

        tickerJob = null

        state = PomodoroState(
            phase = PomodoroPhase.WORK,
            remainingSeconds = config.workDurationSeconds,
            completedWorkCycles = 0,
            isRunning = false,
            isEnabled = state.isEnabled
        )

        onStateChanged(state)
    }

    //---------- transition phase ------------
    // --- currently has 2 functions -> transition to another pahse and emit events
    private fun transitionToNextPhase() {

        when (state.phase) {

            PomodoroPhase.WORK -> {
                val completedCycles =
                    state.completedWorkCycles + 1
                state = state.copy(
                    completedWorkCycles = completedCycles
                )

                if (completedCycles >= config.workCyclesBeforeLongBreak) {

                    onEvent(PomodoroEvent.WorkCompleted)
                    onEvent(PomodoroEvent.LongBreakStarted)
                    startLongBreak()

                } else {

                    onEvent(PomodoroEvent.WorkCompleted)
                    onEvent(PomodoroEvent.ShortBreakStarted)
                    startShortBreak()
                }
            }

            PomodoroPhase.SHORT_BREAK -> {

                onEvent(PomodoroEvent.BreakCompleted)
                onEvent(PomodoroEvent.WorkStarted)
                startWork()
            }

            PomodoroPhase.LONG_BREAK -> {
                state = state.copy(
                    completedWorkCycles = 0
                )

                onEvent(PomodoroEvent.BreakCompleted)
                onEvent(PomodoroEvent.WorkStarted)
                startWork()
            }
        }
    }

    // ------------- helper functions for transition phase ------------
    private fun startWork() {
        state = state.copy(
            phase = PomodoroPhase.WORK,
            remainingSeconds = config.workDurationSeconds,
            isRunning = true
        )
        onStateChanged(state)
    }

    private fun startShortBreak() {

        state = state.copy(
            phase = PomodoroPhase.SHORT_BREAK,
            remainingSeconds = config.shortBreakDurationSeconds,
            isRunning = true
        )
        onStateChanged(state)
    }

    private fun startLongBreak() {

        state = state.copy(
            phase = PomodoroPhase.LONG_BREAK,
            remainingSeconds = config.longBreakDurationSeconds,
            isRunning = true
        )
        onStateChanged(state)
    }


    fun skipBreak() {

        when (state.phase) {

            PomodoroPhase.SHORT_BREAK,
            PomodoroPhase.LONG_BREAK -> {
                onEvent(PomodoroEvent.BreakSkipped)
                onEvent(PomodoroEvent.BreakCompleted)
                onEvent(PomodoroEvent.WorkStarted)
                startWork()
            }
            PomodoroPhase.WORK -> {
                // Nothing to skip
            }
        }
    }

    fun restoreState(
        phase: PomodoroPhase,
        remainingSeconds: Int,
        completedWorkCycles: Int,
        isRunning: Boolean
    ) {

        tickerJob?.cancel()
        // it cacencls the ticker to make sure no two coroutine ticker run simulatenously

        state = PomodoroState(
            phase = phase,
            remainingSeconds = remainingSeconds,
            completedWorkCycles = completedWorkCycles,
            isRunning = isRunning,
            isEnabled = true
        )

        onStateChanged(state)

        if (isRunning) {
            startTicker()
        }
    }

    // ------- for manually engine cleanup ----- no need, but good practise
    fun release() {
        tickerJob?.cancel()
        scope.cancel()
    }

}