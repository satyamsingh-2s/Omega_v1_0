package com.satyamsingh2s.productivity.omega.data_layer.pemissions


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

class OmegaPermissionManager(

    private val context: Context

) {

    fun hasNotificationPermission(): Boolean {

        if (

            Build.VERSION.SDK_INT <
            Build.VERSION_CODES.TIRAMISU

        ) {

            return true
        }

        return ContextCompat.checkSelfPermission(

            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
}