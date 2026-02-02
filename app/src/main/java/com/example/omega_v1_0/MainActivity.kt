package com.example.omega_v1_0

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.omega_v1_0.ui.navigation.OmegaNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController() // creating the navController to control navigation through the app
            OmegaNavGraph(navController = navController)  // this line says here is the map of the app, us navController to navigate through it.
        }
    }
}

