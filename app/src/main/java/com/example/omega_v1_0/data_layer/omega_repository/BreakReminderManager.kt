package com.example.omega_v1_0.data_layer.omega_repository

import android.util.Log
import com.example.omega_v1_0.notification.OmegaNotificationManager

class BreakReminderManager(private val omegaNotificationManager: OmegaNotificationManager) {
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
        omegaNotificationManager.showNotification(
            title = "BREAK REMINDER",
            message = "Recovery Session Ended"
        )
        omegaNotificationManager.playTimedSound()

    }
}