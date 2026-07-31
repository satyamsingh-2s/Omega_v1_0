package com.example.omega_v1_0.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.R
import com.example.omega_v1_0.ui.components.OmegaScreen
import com.example.omega_v1_0.ui.theme.OmegaDarkTheme
import com.example.omega_v1_0.ui.theme.OmegaRedTheme

@Composable
fun MainScreen(
    onPlannedWorkClick: () -> Unit,
    onUnplannedWorkClick: () -> Unit,
    onDailyRecordClick: () -> Unit
) {

        Surface(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "OMEGA",
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(modifier = Modifier.height(48.dp))
                val context= LocalContext.current

                Button(
                    onClick = {
                        Toast.makeText(context, "Under Construction 🚧", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Planned Work")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onUnplannedWorkClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Unplanned Work")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDailyRecordClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Daily Record")
                }
            }
        }
    }
