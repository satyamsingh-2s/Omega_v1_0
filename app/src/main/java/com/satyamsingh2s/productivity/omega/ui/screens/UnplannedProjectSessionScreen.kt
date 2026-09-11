package com.satyamsingh2s.productivity.omega.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satyamsingh2s.productivity.omega.models_enums.RevisionNoteItem
import com.satyamsingh2s.productivity.omega.omega_engines.pomodoro_engine.PomodoroState
import com.satyamsingh2s.productivity.omega.models_enums.SessionStatus
import com.satyamsingh2s.productivity.omega.models_enums.TodoCategory
import com.satyamsingh2s.productivity.omega.ui.components.PomodoroCard
import com.satyamsingh2s.productivity.omega.ui.components.PomodoroCardStyle
import com.satyamsingh2s.productivity.omega.ui.components.SessionControlCard
import com.satyamsingh2s.productivity.omega.ui.components.SessionControlCardStyle
import com.satyamsingh2s.productivity.omega.ui.components.common.CircularIconButton
import com.satyamsingh2s.productivity.omega.ui.components.dialogs.RevisionNoteEditorDialog
import com.satyamsingh2s.productivity.omega.ui.components.dialogs.RevisionNoteViewerDialog
import com.satyamsingh2s.productivity.omega.ui.components.revision_notes.RevisionHistoryPanel
import com.satyamsingh2s.productivity.omega.ui.model.RevisionNoteMenuAction
import com.satyamsingh2s.productivity.omega.ui.model.ToDoListUiModel
import com.satyamsingh2s.productivity.omega.ui.model.UnplannedProjectRecentSessionUiModel
import com.satyamsingh2s.productivity.omega.ui.utils.formatDuration
import com.satyamsingh2s.productivity.omega.ui.viewmodel.RevisionNoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnplannedProjectSessionScreen(
    projectName: String,
    breadcrumb: String,
    currentDurationSeconds: Int,
    expectedDurationSeconds: Int,
    totalSessions: Int,
    sessionName: String,
    activeSessionName: String?,
    onSessionNameChanged: (String) -> Unit,
    sessionStatus: SessionStatus?,
    stopwatchSeconds: Int,
    onStartSession: () -> Unit,
    onPauseSession: () -> Unit,
    onResumeSession: () -> Unit,
    onStopSession: () -> Unit,
    recentSessions: List<com.satyamsingh2s.productivity.omega.ui.model.UnplannedProjectRecentSessionUiModel>,
    onBack: () -> Unit,
    onStatsClick: () -> Unit,
    // ----- estimated minutes section ---
    selectedDurationMinutes: Int?,
    onDurationSelected: (Int?) -> Unit,
    // -------------------------
    pomodoroState: PomodoroState?,
    workCyclesBeforeLongBreak: Int,
    onSkipBreak: () -> Unit,

    // ----------To do list variables -----
    todoItems: List<ToDoListUiModel> = emptyList(),
    newTodoText: String = "",
    onTodoTextChanged: (String) -> Unit = {},
    onAddTodo: () -> Unit = {},
    onToggleTodo: (ToDoListUiModel) -> Unit = {},
    onDeleteTodo: (Long) -> Unit = {},
    selectedTodoCategory: TodoCategory = TodoCategory.TODAY,
    onTodoCategoryChanged: (TodoCategory) -> Unit = {},

    revisionNoteViewModel: RevisionNoteViewModel,


    //----------------
    navigateToDeskOmega: () -> Unit = {},
) {
    val progress = if (expectedDurationSeconds == 0) 0f else (currentDurationSeconds.toFloat() / expectedDurationSeconds).coerceAtMost(1f)
    val groupedSessions = recentSessions.chunked(3)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val groupWidth = screenWidth * 0.85f

    // ------ to-do list part ------------

    var showFocusSheet by remember {
        mutableStateOf(false)
    }

    // --- notes ui state  -----------
    var showNotesSheet by remember {
        mutableStateOf(false)
    }
    var selectedRevisionNote by remember {
        mutableStateOf<RevisionNoteItem?>(null)
    }
    var showRevisionEditor by remember {
        mutableStateOf(false)
    }
    val revisionNoteUiState by
    revisionNoteViewModel.uiState.collectAsState()
    val revisionNotes by
    revisionNoteViewModel.revisionNotes.collectAsState()

    // for photopicker api -- it is the launcher for photopicker
    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->

            uri?.let {

                revisionNoteViewModel.addAttachment(
                    it.toString()
                )
            }
        }


    val todayTasksLeft =
        if (selectedTodoCategory == TodoCategory.TODAY) {
            todoItems.size
        } else {
            null
        }
    val futureTasksLeft =
        if (selectedTodoCategory == TodoCategory.FUTURE) {
            todoItems.size
        } else {
            null
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderZone(
                projectName = projectName,
                breadcrumb = breadcrumb,
                onBack = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            )

            Spacer(modifier = Modifier.height(14.dp))

            ProjectProgressZone(
                currentDurationSeconds = currentDurationSeconds,
                expectedDurationSeconds = expectedDurationSeconds,
                progress = progress,
                totalSessions = totalSessions,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
            )

            SessionControlZone(
                sessionName = sessionName,
                activeSessionName = activeSessionName,
                onSessionNameChanged = onSessionNameChanged,
                sessionStatus = sessionStatus,
                stopwatchSeconds = stopwatchSeconds,
                onStartSession = onStartSession,
                onPauseSession = onPauseSession,
                onResumeSession = onResumeSession,
                onStopSession = onStopSession,
                selectedDurationMinutes = selectedDurationMinutes,
                onDurationSelected = onDurationSelected,
                pomodoroState = pomodoroState,
                workCyclesBeforeLongBreak = workCyclesBeforeLongBreak,
                onSkipBreak = onSkipBreak,
                totalSessionSeconds = expectedDurationSeconds,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(5f)
            )

            Spacer(modifier = Modifier.height(22.dp))

            UtilityZone(
                onNotesClick = { showNotesSheet = true },
                onTodoClick = { showFocusSheet = true },
                selectedTodoCategory = selectedTodoCategory,
                todayTasksLeft = todayTasksLeft,
                futureTasksLeft = futureTasksLeft,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            StatisticsZone(
                onStatsClick = onStatsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            RecentSessionsZone(
                groupedSessions = groupedSessions,
                groupWidth = groupWidth,
                onSessionLongClick = { selectedSession ->
                    revisionNoteViewModel.loadRevisionNote(selectedSession.id)
                    showRevisionEditor = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
            )
        }
    }

    // --------------------------------------------------------------
    // Bottom sheets and dialogs (outside the workspace layout)
    // --------------------------------------------------------------
    // to do button --------------
    if (showFocusSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFocusSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Category Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = selectedTodoCategory == TodoCategory.TODAY,
                        onClick = { onTodoCategoryChanged(TodoCategory.TODAY) },
                        label = { Text("Today") }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    FilterChip(
                        selected = selectedTodoCategory == TodoCategory.FUTURE,
                        onClick = { onTodoCategoryChanged(TodoCategory.FUTURE) },
                        label = { Text("Future") }
                    )
                }

                // Add Todo Input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newTodoText,
                        onValueChange = onTodoTextChanged,
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "Add a new task...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        singleLine = true,
                        shape = MaterialTheme.shapes.large,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    CircularIconButton(
                        onClick = onAddTodo,
                        icon = Icons.Default.Add,
                        contentDescription = "Add Todo",
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        iconTint = MaterialTheme.colorScheme.onPrimary,
                        size = 52.dp
                    )
                }

                // Todo List
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(todoItems) { item ->
                        val checkboxColor =
                            if (item.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        val checkmarkColor = MaterialTheme.colorScheme.onPrimary
                        val textColor =
                            if (item.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                        val deleteColor = MaterialTheme.colorScheme.error

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Checkbox
                                androidx.compose.foundation.Canvas(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { onToggleTodo(item) },
                                    onDraw = {
                                        drawCircle(
                                            color = checkboxColor,
                                            radius = size.minDimension / 2
                                        )
                                        if (item.isCompleted) {
                                            drawCircle(
                                                color = checkmarkColor,
                                                radius = size.minDimension / 4
                                            )
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                // Todo Text
                                Text(
                                    text = item.text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = textColor,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                // Delete Button
                                IconButton(
                                    onClick = { onDeleteTodo(item.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = deleteColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNotesSheet) {

        ModalBottomSheet(
            onDismissRequest = {
                showNotesSheet = false
            }
        ) {

            RevisionHistoryPanel(
                //notes = revisionNotes,
                notes = revisionNotes,
                onNoteClick = {
                    revisionNoteViewModel.loadRevisionNote(
                        it.sessionId
                    )
                    selectedRevisionNote = it
                }
            )
        }

    }

    selectedRevisionNote?.let { note ->
        RevisionNoteViewerDialog(

            note = note,

            attachments = revisionNoteUiState.attachments,

            onEdit = {

                selectedRevisionNote = null

                showRevisionEditor = true
            },

            onDismiss = {

                selectedRevisionNote = null
            }
        )

    }
    if (showRevisionEditor) {

        RevisionNoteEditorDialog(

            summary = revisionNoteUiState.summary,

            attachments = revisionNoteUiState.attachments,

            onSummaryChange =
                revisionNoteViewModel::onSummaryChanged,

            onSave = {

                revisionNoteViewModel.saveRevisionNote {

                    showRevisionEditor = false

                    revisionNoteViewModel.clear()
                }

            },

            onDismiss = {

                showRevisionEditor = false

                revisionNoteViewModel.clear()
            },
            onMenuAction = { action ->
                when (action) {
                    RevisionNoteMenuAction.ADD_IMAGE -> {

                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                }
            },
            onDeleteAttachment = { attachmentId ->

                revisionNoteViewModel.deleteAttachment(
                    attachmentId
                )
            },


            )

    }

}

// ================================================================
// WORKSPACE ZONE COMPOSABLES
// Each zone owns its layout via Arrangement / Alignment / spacedBy.
// Zone sizes are controlled by the parent Column's Modifier.weight().
// ================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HeaderZone(
    projectName: String,
    breadcrumb: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        TopAppBar(
            title = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = projectName,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 19.sp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = breadcrumb,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
    }
}

@Composable
private fun ProjectProgressZone(
    currentDurationSeconds: Int,
    expectedDurationSeconds: Int,
    progress: Float,
    totalSessions: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Current",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDuration(currentDurationSeconds),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Expected",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDuration(expectedDurationSeconds),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            )
            Text(
                text = "$totalSessions Sessions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SessionControlZone(
    sessionName: String,
    activeSessionName: String?,
    onSessionNameChanged: (String) -> Unit,
    sessionStatus: SessionStatus?,
    stopwatchSeconds: Int,
    onStartSession: () -> Unit,
    onPauseSession: () -> Unit,
    onResumeSession: () -> Unit,
    onStopSession: () -> Unit,
    selectedDurationMinutes: Int?,
    onDurationSelected: (Int?) -> Unit,
    pomodoroState: PomodoroState?,
    workCyclesBeforeLongBreak: Int,
    onSkipBreak: () -> Unit,
    totalSessionSeconds: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (pomodoroState?.isEnabled == true) {
            PomodoroCard(
                sessionName = sessionName,
                totalSessionSeconds = totalSessionSeconds,
                sessionStatus = sessionStatus,
                pomodoroState = pomodoroState,
                workCyclesBeforeLongBreak = workCyclesBeforeLongBreak,
                onStart = onStartSession,
                onPause = onPauseSession,
                onResume = onResumeSession,
                onStop = onStopSession,
                onSkipBreak = onSkipBreak,
                onMenuClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                style = PomodoroCardStyle(0.8f, 0.5f, 0.5f, 0.6f, 1f)
            )
        } else {
            SessionControlCard(
                sessionName = sessionName,
                activeSessionName = activeSessionName,
                selectedDurationMinutes = selectedDurationMinutes,
                onDurationSelected = onDurationSelected,
                stopwatchSeconds = stopwatchSeconds,
                sessionStatus = sessionStatus,
                onSessionNameChanged = onSessionNameChanged,
                onStart = onStartSession,
                onPause = onPauseSession,
                onResume = onResumeSession,
                onStop = onStopSession,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                style = SessionControlCardStyle(1f, 1f, 1.3f, 1f)
            )
        }
    }
}

@Composable
private fun UtilityZone(
    onNotesClick: () -> Unit,
    onTodoClick: () -> Unit,
    selectedTodoCategory: TodoCategory,
    todayTasksLeft: Int?,
    futureTasksLeft: Int?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(82.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            onClick = onNotesClick
        ) {
            Text("Notes")
        }
        Button(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            onClick = onTodoClick
        ) {
            if (selectedTodoCategory == TodoCategory.TODAY)
                Text("TODY [ $todayTasksLeft ]")
            if (selectedTodoCategory == TodoCategory.FUTURE)
                Text("FUTY [ $futureTasksLeft ]")
        }
    }
}

@Composable
private fun StatisticsZone(
    onStatsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onStatsClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "View Statistics",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Icon(
            imageVector = Icons.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun RecentSessionsZone(
    groupedSessions: List<List<UnplannedProjectRecentSessionUiModel>>,
    groupWidth: androidx.compose.ui.unit.Dp,
    onSessionLongClick: (UnplannedProjectRecentSessionUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "PAST SESSIONS",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(groupedSessions) { sessionGroup ->
                    Column(
                        modifier = Modifier
                            .width(groupWidth)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        sessionGroup.forEach { session ->
                            UnplannedRecentSessionItem(
                                session = session,
                                onLongClick = onSessionLongClick
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun UnplannedRecentSessionItem(
    session: UnplannedProjectRecentSessionUiModel,
    onLongClick: (UnplannedProjectRecentSessionUiModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    // Reserved for future
                },
                onLongClick = {
                    onLongClick(session)
                }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            maxLines = 1,
            text = session.sessionName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = formatDuration(session.durationSeconds),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//private fun UnplannedProjectSessionScreenPreview() {
//        UnplannedProjectSessionScreen(
//
//            // ---------- Header ----------
//            projectName = "Omega v1.1",
//
//            breadcrumb =
//                "Android > Data Layer > Repository",
//
//            // ---------- Progress ----------
//            currentDurationSeconds = 29460,      // 08:11:00
//
//            expectedDurationSeconds = 43200,     // 12:00:00
//
//            totalSessions = 18,
//
//            // ---------- Session ----------
//            sessionName = "",
//
//            activeSessionName = "Implement Session Screen",
//
//            onSessionNameChanged = {},
//
//            // ---------- Stopwatch ----------
//            sessionStatus = SessionStatus.RUNNING,
//
//            stopwatchSeconds = 2538,             // 00:42:18
//
//            // ---------- Controls ----------
//            onPauseSession = {},
//
//            onResumeSession = {},
//
//            onStopSession = {},
//
//            // ---------- Recent Sessions ----------
//            recentSessions = listOf(
//
//                UnplannedProjectRecentSessionUiModel(
//                    id = 1,
//                    sessionName = "Repository Refactor",
//                    durationSeconds = 3600
//                ),
//
//                UnplannedProjectRecentSessionUiModel(
//                    id = 2,
//                    sessionName = "Tree Traversal",
//                    durationSeconds = 2700
//                ),
//
//                UnplannedProjectRecentSessionUiModel(
//                    id = 3,
//                    sessionName = "Session Screen UI",
//                    durationSeconds = 1800
//                ),
//
//                UnplannedProjectRecentSessionUiModel(
//                    id = 4,
//                    sessionName = "ViewModel",
//                    durationSeconds = 4200
//                ),
//
//                UnplannedProjectRecentSessionUiModel(
//                    id = 5,
//                    sessionName = "Navigation",
//                    durationSeconds = 2400
//                ),
//
//                UnplannedProjectRecentSessionUiModel(
//                    id = 6,
//                    sessionName = "Repository Testing",
//                    durationSeconds = 3000
//                )
//            ),
//
//            // ---------- Navigation ----------
//            onBack = {},
//
//            onStatsClick = {},
//            onStartSession = {},
//            pomodoroState = PomodoroState(
//
//                phase = PomodoroPhase.WORK,
//                remainingSeconds = 25 * 60,
//                completedWorkCycles = 2,
//                isRunning = true,
//                isEnabled = false
//            ),
//            workCyclesBeforeLongBreak = 4,
//            onSkipBreak = {},
//            selectedDurationMinutes = null,
//            onDurationSelected = {},
//         //   revisionNoteViewModel = {}
//
//
//        )
//    }