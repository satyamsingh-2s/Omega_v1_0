package com.satyamsingh2s.productivity.omega.data_layer.omega_repository

import com.satyamsingh2s.productivity.omega.notification.OmegaNotificationManager

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