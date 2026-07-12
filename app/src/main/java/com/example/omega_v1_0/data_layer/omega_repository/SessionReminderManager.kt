package com.example.omega_v1_0.data_layer.omega_repository

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.omega_v1_0.notification.OmegaNotificationManager

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

