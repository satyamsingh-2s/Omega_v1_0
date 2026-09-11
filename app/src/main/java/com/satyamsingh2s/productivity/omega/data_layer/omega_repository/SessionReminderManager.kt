package com.satyamsingh2s.productivity.omega.data_layer.omega_repository

import com.satyamsingh2s.productivity.omega.notification.OmegaNotificationManager

class SessionReminderManager(private val omegaNotificationManager: OmegaNotificationManager) {

    private var hasExpectedReminderFired = false

    fun checkNotifications(
        currentDurationSeconds: Int,
        expectedDurationSeconds: Int?

    ) {

        if (
            !hasExpectedReminderFired &&
            expectedDurationSeconds != null &&
            currentDurationSeconds >= expectedDurationSeconds

        ) {

            sendExpectedNotification()
            hasExpectedReminderFired = true
        }
    }

    fun reset() {

        hasExpectedReminderFired = false
    }

    private fun sendExpectedNotification() {

        omegaNotificationManager.showNotification(
            title = "SESSION REMINDER",
            message = "Expected Duration Reached"
        )
        //omegaNotificationManager.playTimedSound()
    }
}

