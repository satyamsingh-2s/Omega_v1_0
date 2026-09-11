package com.satyamsingh2s.productivity.omega.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.satyamsingh2s.productivity.omega.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OmegaNotificationManager(
    private val context: Context
)
{

    private var mediaPlayer: MediaPlayer? = null
    // for autostop after 30 seconds
    private val scope = CoroutineScope(
        Dispatchers.Main
    )

    private companion object {
        const val CHANNEL_ID = "omega_channel"
        const val CHANNEL_NAME = "Omega Notifications"

        const val STOP_ACTION = "OMEGA_STOP_NOTIFICATION"
        const val NOTIFICATION_ID = 1001
    }

//    val stopIntent = Intent(
//        context,
//        OmegaStopReceiver::class.java
//
//    ).apply {
//
//        action = STOP_ACTION
//    }
//
//    val stopPendingIntent = PendingIntent.getBroadcast(
//
//        context,
//
//        0,
//
//        stopIntent,
//
//        PendingIntent.FLAG_UPDATE_CURRENT or
//
//                PendingIntent.FLAG_IMMUTABLE
//    )

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

    fun showNotification(
        title: String,
        message: String

    ) {

        val notification =
            NotificationCompat.Builder(
                context,
                CHANNEL_ID

            )
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(
                    NotificationCompat.PRIORITY_DEFAULT
                )
                .setAutoCancel(true)
                .build()

        if (
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        NotificationManagerCompat
            .from(context)
            .notify(
                System.currentTimeMillis().toInt(),
                notification
            )
    }

    fun playSound() {
        if (mediaPlayer?.isPlaying == true) {
            return
        }
        mediaPlayer = MediaPlayer.create(context,
            R.raw.omega_alarm_calming
        )
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun playTimedSound() {

        stopSound()
        playSound()
        scope.launch {
            delay(10_000)
            stopSound()
        }
    }

    fun playIntroSound() {
        if (mediaPlayer?.isPlaying == true) {
            return
        }
        mediaPlayer = MediaPlayer.create(
            context,
            R.raw.inside_the_fire
        )
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
        scope.launch {
        //    delay(14_100)
            smoothFadeVolume()
        }
    }

    private suspend fun fadeOutSound() {
        for (i in 7 downTo 0) {
            val volume = i / 10f
            mediaPlayer?.setVolume(
                volume,
                volume
            )
            delay(1500)
        }
        stopSound()
    }

    private suspend fun smoothFadeVolume() {

        val interval = 250L

        // ---------- Fade In ----------
        val fadeInSteps = 28
        for (i in 0..fadeInSteps) {
            val volume =
                0.3f + ((0.8f - 0.3f) * i / fadeInSteps)
            mediaPlayer?.setVolume(
                volume,
                volume
            )
            delay(interval)
        }
        // ---------- Fade Out ----------
        val fadeOutSteps = 28
        for (i in fadeOutSteps downTo 0) {
            val volume =
                0.8f *
                        (i / fadeOutSteps.toFloat())
            mediaPlayer?.setVolume(
                volume,
                volume
            )
            delay(interval)
        }
        stopSound()
    }



}