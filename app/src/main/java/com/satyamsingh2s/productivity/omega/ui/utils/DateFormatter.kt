package com.satyamsingh2s.productivity.omega.ui.utils

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