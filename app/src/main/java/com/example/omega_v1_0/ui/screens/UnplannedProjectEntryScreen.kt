package com.example.omega_v1_0.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.omega_v1_0.ui.navigation.Screen
import com.example.omega_v1_0.ui.utils.OmegaJsonParser
import com.example.omega_v1_0.ui.utils.OmegaPrompts
import com.example.omega_v1_0.ui.viewmodel.UnplannedProjectEntryScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun UnplannedProjectEntryScreen(
    viewModel: UnplannedProjectEntryScreenViewModel,
    onSkip: () -> Unit,
    navigateToWorkspace: () -> Unit,
) {

    var jsonInput by rememberSaveable {
        mutableStateOf("")
    }
    val canGenerate = jsonInput.isNotBlank()

    val clipboardManager = LocalClipboardManager.current

    val snackbarHostState =
        remember {
            SnackbarHostState()
        }
    val scope = rememberCoroutineScope()


    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)

        ) {

                Text(

                    text = "Unplanned Workspace",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Build your workspace manually or generate one using AI."
                )

                HorizontalDivider()

                Text(

                    text = "🤖 AI Workspace Import",
                    style = MaterialTheme.typography.titleMedium
                )

                Button(
                    onClick = {
                        clipboardManager.setText(
                            AnnotatedString(
                                OmegaPrompts.STRUCTURE_GENERATOR_V1
                            )
                        )

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Prompt copied"
                            )
                        }
                    }

                ) {
                    Text("Copy Prompt")
                }



                OutlinedTextField(
                    value = jsonInput,
                    onValueChange = {
                        jsonInput = it
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),

                    placeholder = {
                        Text("Paste AI generated JSON here...")
                    },
                    singleLine = false
                )



                Button(

                    onClick = {
                        val result = OmegaJsonParser.decode(
                            jsonInput
                        )

                        if (
                            result.isSuccess
                        ) {

                            val omegaImport = result.getOrNull()

                            if (
                                omegaImport != null
                            ) {
                                viewModel.importWorkspace(omegaImport)
                                navigateToWorkspace()
                            }
                        }

                        else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Invalid JSON"
                                )
                            }
                        }
                    }

                )
                 {

                    Text("Generate Workspace")
                }


                Spacer(modifier = Modifier.weight(1f))

                OutlinedButton(

                    onClick = onSkip,

                    modifier = Modifier.fillMaxWidth()

                ) {

                    Text("Skip & Create Manually")
                }

        }
    }
}