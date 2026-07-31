package com.example.omega_v1_0.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.omega_v1_0.R.drawable.omega_background11
import com.example.omega_v1_0.R
import com.example.omega_v1_0.R.drawable.omega_background5

@Composable
fun OmegaScreen(

    content: @Composable BoxScope.() -> Unit

) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Background Pattern

        Image(
            painter = painterResource(omega_background11),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        // Theme Background

        Surface(
            modifier = Modifier.fillMaxSize(),
           color = MaterialTheme.colorScheme.background.copy(alpha = 0.96f)
        ) {

        }

        // Screen Content

        content()
    }
}