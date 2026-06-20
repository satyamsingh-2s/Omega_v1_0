package com.example.omega_v1_0.data_layer.omega_repository

import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.example.omega_v1_0.notification.OmegaNotificationManager

class SessionReminderManager {

    private var hasExpectedReminderFired = false
  //  private val omegaNotificationManager = OmegaNotificationManager(LocalContext.current)

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

//        omegaNotificationManager.showNotification(
//            title = "Omega",
//            message = "Expected duration reached."

 //       )
    }
}