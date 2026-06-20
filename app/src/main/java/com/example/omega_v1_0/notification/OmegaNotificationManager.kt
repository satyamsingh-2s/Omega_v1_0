package com.example.omega_v1_0.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.omega_v1_0.R

class OmegaNotificationManager(
    private val context: Context
)
{
   // private lateinit var contentTitle: String

    private companion object {
        const val CHANNEL_ID = "omega_channel"
        const val CHANNEL_NAME = "Omega Notifications"
    }


    fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
        manager.createNotificationChannel(channel)
    }

//    fun showNotification(
//
//        title: String,
//
//        message: String
//
//    ) {
//
//        val notification =
//
//            NotificationCompat.Builder(
//
//                context,
//
//                CHANNEL_ID
//
//            )
//
//                .setSmallIcon(
//                    R.drawable.ic_launcher_foreground
//                )
//
//                .setContentTitle(title)
//
//                .setContentText(message)
//
//                .setPriority(
//                    NotificationCompat.PRIORITY_DEFAULT
//                )
//
//                .setAutoCancel(true)
//
//                .build()
//
//        if (
//
//            ContextCompat.checkSelfPermission(
//
//                context,
//
//                Manifest.permission.POST_NOTIFICATIONS
//
//            )
//
//            != PackageManager.PERMISSION_GRANTED
//
//        ) {
//
//            return
//        }
//
//        NotificationManagerCompat
//
//            .from(context)
//
//            .notify(
//
//                System.currentTimeMillis().toInt(),
//
//                notification
//            )
//    }




}