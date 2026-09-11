package com.satyamsingh2s.productivity.omega.omega_engines.pomodoro_engine


// --- here enginge will emit event to viewmodel ---- ----
sealed interface PomodoroEvent {

    data object Tick : PomodoroEvent
    data object WorkCompleted : PomodoroEvent
    data object ShortBreakStarted : PomodoroEvent
    data object LongBreakStarted : PomodoroEvent
    data object BreakCompleted : PomodoroEvent
    data object WorkStarted : PomodoroEvent
    data object BreakSkipped : PomodoroEvent // for debugging and distinguishing between them...
}