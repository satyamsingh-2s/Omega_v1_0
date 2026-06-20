package com.example.omega_v1_0.data_layer.omega_repository

import android.util.Log

class BreakReminderManager {
    private var hasBreakReminderFired = false

    fun checkNotifications(
        currentBreakSeconds: Int,
        expectedBreakSeconds: Int?

    ) {

        if (
            !hasBreakReminderFired &&
            expectedBreakSeconds != null &&
            currentBreakSeconds >= expectedBreakSeconds

        ) {

            sendBreakNotification()
            hasBreakReminderFired = true
        }
    }

    fun reset() {
        hasBreakReminderFired = false
    }

    private fun sendBreakNotification() {
        Log.d(
            "DailyRecord",
            "Break Duration Reached"
        )
    }
}