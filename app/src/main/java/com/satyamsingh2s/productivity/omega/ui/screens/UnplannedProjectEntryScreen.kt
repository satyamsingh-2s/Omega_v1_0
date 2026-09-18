package com.satyamsingh2s.productivity.omega.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.rounded.Workspaces
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.DataObject
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.UploadFile
import androidx.compose.material.icons.rounded.Workspaces
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.ai.branch_b.state.AiUiState
import com.satyamsingh2s.productivity.omega.ui.components.PersonalizationBottomSheet
import com.satyamsingh2s.productivity.omega.ui.components.PersonalizationCard
import com.satyamsingh2s.productivity.omega.ui.model.PersonalizationDefinitions
import com.satyamsingh2s.productivity.omega.ui.model.PersonalizationType
import com.satyamsingh2s.productivity.omega.ui.utils.OmegaJsonParser
import com.satyamsingh2s.productivity.omega.ui.utils.OmegaPrompts
import com.satyamsingh2s.productivity.omega.ui.viewmodel.UnplannedProjectEntryScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnplannedProjectEntryScreen(
    viewModel: UnplannedProjectEntryScreenViewModel,
    onSkip: () -> Unit,
    navigateToWorkspace: () -> Unit,
) {
    // -------------------------------------------------------------------------
    // SCREEN STATE
    // -------------------------------------------------------------------------

    var isCreateWorkspaceExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var showAllPersonalization by rememberSaveable {
        mutableStateOf(false)
    }

    var showManualImport by rememberSaveable {
        mutableStateOf(false)
    }

    // -------------------------------------------------------------------------
    // NEW WORKSPACE STATE
    // -------------------------------------------------------------------------

    var goal by rememberSaveable {
        mutableStateOf("")
    }

    var additionalContext by rememberSaveable {
        mutableStateOf("")
    }

    var personalizations by remember {
        mutableStateOf(
            mapOf<PersonalizationType, String>()
        )
    }

    var activePersonalizationSheet by remember {
        mutableStateOf<PersonalizationType?>(null)
    }

    // -------------------------------------------------------------------------
    // MANUAL IMPORT STATE
    // -------------------------------------------------------------------------

    var jsonInput by rememberSaveable {
        mutableStateOf("")
    }

    // -------------------------------------------------------------------------
    // VIEWMODEL STATE
    // -------------------------------------------------------------------------

    val aiUiState by viewModel.aiUiState.collectAsState()
    val loading = aiUiState is AiUiState.Loading

    // -------------------------------------------------------------------------
    // UI HELPERS
    // -------------------------------------------------------------------------

    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    LaunchedEffect(aiUiState) {
        if (aiUiState is AiUiState.Error) {
            snackbarHostState.showSnackbar(
                (aiUiState as AiUiState.Error).message
            )
        }
    }

    // -------------------------------------------------------------------------
    // SCREEN
    // -------------------------------------------------------------------------

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(
                    horizontal = 20.dp,
                    vertical = 24.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // =================================================================
            // HEADER
            // =================================================================

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Workspace",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "What would you like to do?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = 0.7f
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            // =================================================================
            // NEW WORKSPACE
            // =================================================================

            NewWorkspaceCard(
                expanded = isCreateWorkspaceExpanded,
                onClick = {
                    isCreateWorkspaceExpanded =
                        !isCreateWorkspaceExpanded
                }
            ) {

                NewWorkspaceContent(
                    goal = goal,
                    onGoalChange = { value ->
                        val words = value
                            .split("\\s+".toRegex())
                            .filter { it.isNotBlank() }

                        if (words.size <= 20) {
                            goal = value
                        } else if (value.length < goal.length) {
                            goal = value
                        }
                    },

                    additionalContext = additionalContext,
                    onAdditionalContextChange = { value ->
                        val words = value
                            .split("\\s+".toRegex())
                            .filter { it.isNotBlank() }

                        if (words.size <= 50) {
                            additionalContext = value
                        } else if (value.length < additionalContext.length) {
                            additionalContext = value
                        }
                    },

                    showAllPersonalization = showAllPersonalization,

                    onToggleShowAll = {
                        showAllPersonalization =
                            !showAllPersonalization
                    },

                    personalizations = personalizations,

                    onPersonalizationClick = { type ->
                        activePersonalizationSheet = type
                    },

                    loading = loading,

                    onCreateWorkspace = {

                        if (goal.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Tell Omega what you want to achieve."
                                )
                            }
                            return@NewWorkspaceContent
                        }

                        viewModel.generateWorkspace(
                            topic = goal,
                            goal = goal,
                            currentLevel =
                                personalizations[
                                    PersonalizationType.SKILL_LEVEL
                                ],
                            targetDuration =
                                personalizations[
                                    PersonalizationType.TIME_COMMITMENT
                                ],
                            learningStyle =
                                personalizations[
                                    PersonalizationType.APPROACH
                                ],
                            finalDeliverable =
                                personalizations[
                                    PersonalizationType.DESIRED_OUTCOME
                                ],
                            additionalContext = additionalContext,
                            onSuccess = navigateToWorkspace
                        )
                    }
                )
            }

            // =================================================================
            // PROJECT WORKSPACE
            // =================================================================
            Spacer(
                modifier = Modifier.height(3.dp)
            )

            ExistingWorkspaceActionCard(
                title = "Project Workspace",
                description = "See my existing work",
                leadingIcon = Icons.Rounded.Workspaces,
                onClick = onSkip
            )

            // =================================================================
            // ADVANCED
            // =================================================================
            Spacer(
                modifier = Modifier.height(14.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "ADVANCED",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )

                WorkspaceActionCard(
                    title = "Import Workspace",
                    description = "Import an existing workspace from JSON",
                    trailingIcon =
                        if (showManualImport) {
                            Icons.Rounded.KeyboardArrowDown
                        } else {
                            Icons.Rounded.KeyboardArrowRight
                        },
                    onClick = {
                        showManualImport = !showManualImport
                    }
                )

                // -------------------------------------------------------------
                // MANUAL IMPORT CONTENT
                // -------------------------------------------------------------

                if (showManualImport) {

                    ManualImportContent(
                        jsonInput = jsonInput,
                        onJsonChange = {
                            jsonInput = it
                        },
                        enabled = !loading,
                        clipboardManager = clipboardManager,
                        snackbarHostState = snackbarHostState,
                        scope = scope,
                        viewModel = viewModel,
                        onImportSuccess = {
                            navigateToWorkspace()
                        }
                    )
                }
            }
        }
    }

    // =========================================================================
    // PERSONALIZATION BOTTOM SHEET
    // =========================================================================

    activePersonalizationSheet?.let { type ->

        val config =
            PersonalizationDefinitions.getConfig(type)

        PersonalizationBottomSheet(
            config = config,
            initialValue = personalizations[type],

            onDismiss = {
                activePersonalizationSheet = null
            },

            onValueSelected = { value ->
                personalizations =
                    personalizations + (type to value)
            }
        )
    }
}


// =============================================================================
// NEW WORKSPACE CARD
// =============================================================================

@Composable
private fun NewWorkspaceCard(
    expanded: Boolean,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.3f
                )
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(
                alpha = 0.5f
            )
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // -----------------------------------------------------------------
            // CARD HEADER
            // -----------------------------------------------------------------

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // -----------------------------------------------------------------
                // CREATE ICON
                // -----------------------------------------------------------------

                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )

                // -----------------------------------------------------------------
                // TEXT
                // -----------------------------------------------------------------

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "New Workspace",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Create a personalized roadmap",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // -----------------------------------------------------------------
                // EXPAND / COLLAPSE
                // -----------------------------------------------------------------

                Icon(
                    imageVector =
                        if (expanded) {
                            Icons.Rounded.KeyboardArrowDown
                        } else {
                            Icons.Rounded.KeyboardArrowRight
                        },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // -----------------------------------------------------------------
            // EXPANDED CONTENT
            // -----------------------------------------------------------------

            if (expanded) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                content()
            }
        }
    }
}


// =============================================================================
// NEW WORKSPACE CONTENT
// =============================================================================

@Composable
private fun NewWorkspaceContent(
    goal: String,
    onGoalChange: (String) -> Unit,

    additionalContext: String,
    onAdditionalContextChange: (String) -> Unit,

    showAllPersonalization: Boolean,
    onToggleShowAll: () -> Unit,

    personalizations: Map<PersonalizationType, String>,
    onPersonalizationClick: (PersonalizationType) -> Unit,

    loading: Boolean,
    onCreateWorkspace: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // =====================================================================
        // GOAL
        // =====================================================================

        Text(
            text = "Goal",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )

        val goalWordCount =
            goal
                .split("\\s+".toRegex())
                .count { it.isNotBlank() }

        OutlinedTextField(
            value = goal,
            onValueChange = onGoalChange,
            enabled = !loading,

            placeholder = {
                Text(
                    text = "What do you want to achieve?"
                )
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),

            shape = RoundedCornerShape(12.dp),

            supportingText = {

                // Show counter only after 50% of the limit
                if (goalWordCount >= 10) {

                    Text(
                        text = "$goalWordCount/20 words",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            },

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor =
                    MaterialTheme.colorScheme.surface,

                unfocusedContainerColor =
                    MaterialTheme.colorScheme.surface,

                focusedBorderColor =
                    MaterialTheme.colorScheme.primary,

                unfocusedBorderColor =
                    MaterialTheme.colorScheme.outlineVariant,

                focusedTextColor =
                    MaterialTheme.colorScheme.onSurface,

                unfocusedTextColor =
                    MaterialTheme.colorScheme.onSurface,

                focusedLabelColor =
                    MaterialTheme.colorScheme.primary,

                unfocusedLabelColor =
                    MaterialTheme.colorScheme.onSurfaceVariant,

                cursorColor =
                    MaterialTheme.colorScheme.primary
            )
        )


        // =====================================================================
        // PERSONALIZATION HEADER
        // =====================================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Personalize",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = onToggleShowAll
            ) {
                Text(
                    text = if (showAllPersonalization) {
                        "Show fewer"
                    } else {
                        "See all"
                    }
                )
            }
        }


        // =====================================================================
        // COMPACT PERSONALIZATION
        // =====================================================================

        if (!showAllPersonalization) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(
                        rememberScrollState()
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                PersonalizationDefinitions.definitions
                    .forEach { config ->

                        PersonalizationCard(
                            config = config,

                            selectedValue =
                                personalizations[config.type],

                            onClick = {
                                onPersonalizationClick(config.type)
                            },

                            modifier = Modifier.width(200.dp)
                        )
                    }
            }
        }


        // =====================================================================
        // FULL PERSONALIZATION
        // =====================================================================

        if (showAllPersonalization) {

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                PersonalizationDefinitions.definitions
                    .forEach { config ->

                        PersonalizationCard(
                            config = config,

                            selectedValue =
                                personalizations[config.type],

                            onClick = {
                                onPersonalizationClick(config.type)
                            },

                            modifier = Modifier.fillMaxWidth()
                        )
                    }
            }


            // -----------------------------------------------------------------
            // ADDITIONAL CONTEXT
            // -----------------------------------------------------------------

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Anything else?",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )

            val contextWordCount =
                additionalContext
                    .split("\\s+".toRegex())
                    .count { it.isNotBlank() }

            OutlinedTextField(
                value = additionalContext,
                onValueChange = onAdditionalContextChange,
                enabled = !loading,

                placeholder = {
                    Text(
                        text = "Anything else Omega should know?"
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(12.dp),

                supportingText = {

                    // Show counter only after 50% of the limit
                    if (contextWordCount >= 25) {

                        Text(
                            text = "$contextWordCount/50 words",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                },

                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor =
                        MaterialTheme.colorScheme.surface,

                    unfocusedContainerColor =
                        MaterialTheme.colorScheme.surface,

                    focusedBorderColor =
                        MaterialTheme.colorScheme.primary,

                    unfocusedBorderColor =
                        MaterialTheme.colorScheme.outlineVariant,

                    focusedTextColor =
                        MaterialTheme.colorScheme.onSurface,

                    unfocusedTextColor =
                        MaterialTheme.colorScheme.onSurface,

                    focusedLabelColor =
                        MaterialTheme.colorScheme.primary,

                    unfocusedLabelColor =
                        MaterialTheme.colorScheme.onSurfaceVariant,

                    cursorColor =
                        MaterialTheme.colorScheme.primary
                )
            )
        }


        // =====================================================================
        // CREATE WORKSPACE
        // =====================================================================

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Button(
            enabled = !loading && goal.isNotBlank(),

            onClick = onCreateWorkspace,

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            shape = RoundedCornerShape(12.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor =
                    MaterialTheme.colorScheme.primary,

                contentColor =
                    MaterialTheme.colorScheme.onPrimary
            )
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

                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                Text(
                    text = "Create Workspace",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}


// =============================================================================
// PROJECT / IMPORT ACTION CARD
// =============================================================================

@Composable
private fun ExistingWorkspaceActionCard(
    title: String,
    description: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),

        border = BorderStroke(
            1.5.dp,
            MaterialTheme.colorScheme.outlineVariant
        ),

        shape = RoundedCornerShape(20.dp),

        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),

            horizontalArrangement =
                Arrangement.spacedBy(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            // -----------------------------------------------------------------
            // WORKSPACE ICON
            // -----------------------------------------------------------------

            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            // -----------------------------------------------------------------
            // TEXT
            // -----------------------------------------------------------------

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
@Composable
private fun WorkspaceActionCard(
    title: String,
    description: String,
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),

        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        ),

        shape = RoundedCornerShape(20.dp),

        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),

            horizontalArrangement =
                Arrangement.spacedBy(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color =
                        MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


// =============================================================================
// MANUAL IMPORT
// =============================================================================

@Composable
private fun ManualImportContent(
    jsonInput: String,
    onJsonChange: (String) -> Unit,
    enabled: Boolean,
    clipboardManager: androidx.compose.ui.platform.ClipboardManager,
    snackbarHostState: SnackbarHostState,
    scope: kotlinx.coroutines.CoroutineScope,
    viewModel: UnplannedProjectEntryScreenViewModel,
    onImportSuccess: () -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.3f
                )
        ),

        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(
                alpha = 0.5f
            )
        ),

        shape = RoundedCornerShape(20.dp),

        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            // -----------------------------------------------------------------
            // PROMPT TEMPLATE
            // -----------------------------------------------------------------

            Card(
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surfaceVariant
                ),

                shape = RoundedCornerShape(12.dp),

                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 14.dp,
                            vertical = 6.dp
                        ),

                    horizontalArrangement =
                        Arrangement.SpaceBetween,

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "Prompt Blueprint Template",
                        style =
                            MaterialTheme.typography.labelLarge,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    TextButton(
                        onClick = {

                            clipboardManager.setText(
                                AnnotatedString(
                                    OmegaPrompts
                                        .STRUCTURE_GENERATOR_V1
                                )
                            )

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Prompt copied to clipboard"
                                )
                            }
                        }
                    ) {

                        Icon(
                            imageVector =
                                Icons.Rounded.ContentCopy,
                            contentDescription = null,
                            tint =
                                MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(
                            modifier = Modifier.size(6.dp)
                        )

                        Text(
                            text = "Copy Prompt",
                            color =
                                MaterialTheme.colorScheme.primary,
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }
            }

            // -----------------------------------------------------------------
            // JSON INPUT
            // -----------------------------------------------------------------

            OutlinedTextField(
                value = jsonInput,
                onValueChange = onJsonChange,

                enabled = enabled,

                label = {
                    Text("JSON Schema Input")
                },

                placeholder = {
                    Text(
                        "Paste structured JSON payload here..."
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),

                singleLine = false,

                shape = RoundedCornerShape(12.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor =
                        MaterialTheme.colorScheme.surface,

                    unfocusedContainerColor =
                        MaterialTheme.colorScheme.surface,

                    focusedBorderColor =
                        MaterialTheme.colorScheme.primary,

                    unfocusedBorderColor =
                        MaterialTheme.colorScheme.outlineVariant,

                    focusedTextColor =
                        MaterialTheme.colorScheme.onSurface,

                    unfocusedTextColor =
                        MaterialTheme.colorScheme.onSurface
                )
            )

            // -----------------------------------------------------------------
            // IMPORT BUTTON
            // -----------------------------------------------------------------

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),

                enabled =
                    enabled &&
                            jsonInput.isNotBlank(),

                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary,

                    contentColor =
                        MaterialTheme.colorScheme.onPrimary
                ),

                shape = RoundedCornerShape(12.dp),

                onClick = {

                    val result = OmegaJsonParser.decode(jsonInput)

                    if (result.isSuccess) {

                        result.getOrNull()?.let { omegaImport ->

                            viewModel.importWorkspace(omegaImport)

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Workspace imported successfully"
                                )
                            }

                            onImportSuccess()
                        }

                    } else {

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "Invalid JSON structure"
                            )
                        }
                    }
                }
            ) {

                Icon(
                    imageVector =
                        Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                Text(
                    text = "Import Workspace",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}