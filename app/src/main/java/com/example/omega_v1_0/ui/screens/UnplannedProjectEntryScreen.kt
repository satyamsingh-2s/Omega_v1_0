package com.example.omega_v1_0.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.ai.branch_b.state.AiUiState
import com.example.omega_v1_0.ui.utils.OmegaJsonParser
import com.example.omega_v1_0.ui.utils.OmegaPrompts
import com.example.omega_v1_0.ui.viewmodel.UnplannedProjectEntryScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnplannedProjectEntryScreen(
    viewModel: UnplannedProjectEntryScreenViewModel,
    onSkip: () -> Unit,
    navigateToWorkspace: () -> Unit,
) {
    var aiMode by rememberSaveable { mutableStateOf(true) }

    // Form Field States
    var topic by rememberSaveable { mutableStateOf("") }
    var goal by rememberSaveable { mutableStateOf("") }
    var currentLevel by rememberSaveable { mutableStateOf("") }
    var targetDuration by rememberSaveable { mutableStateOf("") }
    var learningStyle by rememberSaveable { mutableStateOf("") }
    var finalDeliverable by rememberSaveable { mutableStateOf("") }

    // Manual State
    var jsonInput by rememberSaveable { mutableStateOf("") }

    val aiUiState by viewModel.aiUiState.collectAsState()
    val loading = aiUiState is AiUiState.Loading

    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(aiUiState) {
        if (aiUiState is AiUiState.Error) {
            snackbarHostState.showSnackbar((aiUiState as AiUiState.Error).message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header Section
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "New Workspace",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Setup your learning path automatically using AI or import manually.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            // Segmented Mode Control
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    selected = aiMode,
                    onClick = { aiMode = true },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                ) {
                    Text("AI Generator", fontWeight = FontWeight.Medium)
                }
                SegmentedButton(
                    selected = !aiMode,
                    onClick = { aiMode = false },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.DataObject,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                ) {
                    Text("Manual Import", fontWeight = FontWeight.Medium)
                }
            }

            // Animated Form Card Container
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = spring())
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AnimatedContent(
                        targetState = aiMode,
                        transitionSpec = {
                            fadeIn() + scaleIn(initialScale = 0.96f) togetherWith fadeOut() + scaleOut(targetScale = 0.96f)
                        },
                        label = "ModeTransition"
                    ) { isAi ->
                        if (isAi) {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                CustomOutlinedTextField(
                                    value = topic,
                                    onValueChange = { topic = it },
                                    enabled = !loading,
                                    label = "Topic *",
                                    placeholder = "e.g., Quantum Computing",
                                    singleLine = true
                                )

                                CustomOutlinedTextField(
                                    value = goal,
                                    onValueChange = { goal = it },
                                    enabled = !loading,
                                    label = "Primary Goal (Optional)",
                                    placeholder = "e.g., Master basic principles",
                                    singleLine = true
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    CustomOutlinedTextField(
                                        value = currentLevel,
                                        onValueChange = { currentLevel = it },
                                        enabled = !loading,
                                        label = "Level",
                                        placeholder = "Beginner",
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    CustomOutlinedTextField(
                                        value = targetDuration,
                                        onValueChange = { targetDuration = it },
                                        enabled = !loading,
                                        label = "Duration",
                                        placeholder = "3 Months",
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    CustomOutlinedTextField(
                                        value = learningStyle,
                                        onValueChange = { learningStyle = it },
                                        enabled = !loading,
                                        label = "Style",
                                        placeholder = "Project-based",
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                    CustomOutlinedTextField(
                                        value = finalDeliverable,
                                        onValueChange = { finalDeliverable = it },
                                        enabled = !loading,
                                        label = "Deliverable",
                                        placeholder = "App Demo",
                                        modifier = Modifier.weight(1f),
                                        singleLine = true
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Button(
                                    enabled = !loading,
                                    onClick = {
                                        if (topic.isBlank()) {
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Topic is required.")
                                            }
                                            return@Button
                                        }
                                        viewModel.generateWorkspace(
                                            topic = topic,
                                            goal = goal,
                                            currentLevel = currentLevel,
                                            targetDuration = targetDuration,
                                            learningStyle = learningStyle,
                                            finalDeliverable = finalDeliverable,
                                            onSuccess = navigateToWorkspace
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    if (loading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(22.dp),
                                            strokeWidth = 2.5.dp,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Rounded.AutoAwesome,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text("Generate Workspace", fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(horizontal = 14.dp, vertical = 6.dp)
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Prompt Blueprint Template",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        TextButton(
                                            onClick = {
                                                clipboardManager.setText(
                                                    AnnotatedString(OmegaPrompts.STRUCTURE_GENERATOR_V1)
                                                )
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Prompt copied to clipboard")
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.ContentCopy,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.size(6.dp))
                                            Text(
                                                text = "Copy Prompt",
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }

                                CustomOutlinedTextField(
                                    value = jsonInput,
                                    onValueChange = { jsonInput = it },
                                    label = "JSON Schema Input",
                                    placeholder = "Paste structured JSON payload here...",
                                    modifier = Modifier.height(180.dp),
                                    singleLine = false
                                )

                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    enabled = jsonInput.isNotBlank(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    onClick = {
                                        val result = OmegaJsonParser.decode(jsonInput)
                                        if (result.isSuccess) {
                                            result.getOrNull()?.let {
                                                viewModel.importWorkspace(it)
                                                navigateToWorkspace()
                                            }
                                        } else {
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Invalid JSON structure")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text("Import Payload", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer Skip Action
            OutlinedButton(
                onClick = onSkip,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.EditNote,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text("Project Workspace", fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
private fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = {
            Text(
                text = placeholder,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        },
        singleLine = singleLine,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}