package com.satyamsingh2s.productivity.omega

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.satyamsingh2s.productivity.omega.notification.OmegaNotificationManager
import com.satyamsingh2s.productivity.omega.navigation.OmegaNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController() // creating the navController to control navigation through the app
            OmegaNavGraph(navController = navController)  // this line says here is the map of the app, us navController to navigate through it.
        }


        // notifcation part
        val omegaNotificationManager = OmegaNotificationManager(this)
        omegaNotificationManager.createChannel()
    }
}

