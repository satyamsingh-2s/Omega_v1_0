package com.example.omega_v1_0.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

 fun formatDate(
    timestamp: Long
): String {

    return SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    ).format(Date(timestamp))
}