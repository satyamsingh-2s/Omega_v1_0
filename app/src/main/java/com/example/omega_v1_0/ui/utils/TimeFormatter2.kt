package com.example.omega_v1_0.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



fun formatDuration2(
    durationSeconds: Int
): String {

    val hours = durationSeconds / 3600
    val minutes = (durationSeconds % 3600) / 60
    val seconds = durationSeconds % 60

    return when {
        hours > 0 ->
            "%dh %02dm %02ds".format(hours, minutes, seconds)

        minutes > 0 ->
            "%dm %02ds".format(minutes, seconds)

        else ->
            "${seconds}s"
    }
}