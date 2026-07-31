package com.example.omega_v1_0.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega_v1_0.omega_engines.pomodoro_engine.PomodoroPhase
import com.example.omega_v1_0.omega_engines.pomodoro_engine.PomodoroState
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.models.TodoCategory
import com.example.omega_v1_0.ui.components.PomodoroCard
import com.example.omega_v1_0.ui.components.PomodoroCardStyle
import com.example.omega_v1_0.ui.components.SessionControlCard
import com.example.omega_v1_0.ui.components.SessionControlCardStyle
import com.example.omega_v1_0.ui.components.common.CircularIconButton
import com.example.omega_v1_0.ui.model.ToDoListUiModel
import com.example.omega_v1_0.ui.model.UnplannedProjectRecentSessionUiModel
import com.example.omega_v1_0.ui.theme.StopwatchTextStyle
import com.example.omega_v1_0.ui.utils.formatDuration

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
    recentSessions: List<com.example.omega_v1_0.ui.model.UnplannedProjectRecentSessionUiModel>,
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

            // Zone 1 (18%): Top App Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.18f),
                verticalArrangement = Arrangement.Center
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = projectName,
                                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 19.sp),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(4.dp))
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

            // Zone 2 (18%): Project Progress
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.18f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
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
                        horizontalAlignment = Alignment.End
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
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
                Spacer(modifier = Modifier.height(8.dp))
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

//            // Zone 3 (12%): Session Name
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(0.12f),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                OutlinedTextField(
//                    value = sessionName,
//                    onValueChange = onSessionNameChanged,
//                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
//                    placeholder = {
//                        Text(
//                            text = if (sessionName.isBlank() && activeSessionName != null) {
//                                activeSessionName
//                            } else {
//                                "Session Name"
//                            },
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    singleLine = true,
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedBorderColor = Color.Transparent,
//                        unfocusedBorderColor = Color.Transparent,
//                        focusedContainerColor = MaterialTheme.colorScheme.background,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.background
//                    )
//                )
//            }
//
//            // Zone 4 (24%): Large Stopwatch
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(0.24f),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = formatDuration(stopwatchSeconds),
//                   // style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
//                    style = StopwatchTextStyle,
//                    textAlign = TextAlign.Center,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Text(
//                    text = "HH : MM : SS",
//                    style = MaterialTheme.typography.labelSmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//
//            PomodoroCard(
//                pomodoroState = pomodoroState,
//                workCyclesBeforeLongBreak =
//                    workCyclesBeforeLongBreak,
//                onSkipBreak = onSkipBreak
//            )
//
//
//            // Zone 5 (12%): Session Controls
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(0.12f),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                when (sessionStatus) {
//
//                    null -> {
//
//                        FilledTonalButton(
//                            onClick = onStartSession
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.PlayArrow,
//                                contentDescription = null
//                            )
//
//                            Spacer(
//                                modifier = Modifier.width(8.dp)
//                            )
//
//                            Text("Start Session")
//                        }
//                    }
//
//                    SessionStatus.RUNNING -> {
//
//                        CircularIconButton(
//                            onClick = onPauseSession,
//                            icon = Icons.Filled.Pause,
//                            contentDescription = "Pause Session",
//                            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
//                            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
//                            size = 56.dp
//                        )
//
//                        Spacer(
//                            modifier = Modifier.width(86.dp)
//                        )
//
//                        CircularIconButton(
//                            onClick = onStopSession,
//                            icon = Icons.Filled.Stop,
//                            contentDescription = "Stop Session",
//                            backgroundColor = MaterialTheme.colorScheme.primary,
//                            iconTint = MaterialTheme.colorScheme.onPrimary,
//                            size = 56.dp
//                        )
//                    }
//
//                    SessionStatus.PAUSED -> {
//
//                        CircularIconButton(
//                            onClick = onResumeSession,
//                            icon = Icons.Filled.PlayArrow,
//                            contentDescription = "Resume Session",
//                            backgroundColor = MaterialTheme.colorScheme.primary,
//                            iconTint = MaterialTheme.colorScheme.onPrimary,
//                            size = 56.dp
//                        )
//
//                        Spacer(
//                            modifier = Modifier.width(86.dp)
//                        )
//
//                        CircularIconButton(
//                            onClick = onStopSession,
//                            icon = Icons.Filled.Stop,
//                            contentDescription = "Stop Session",
//                            backgroundColor = MaterialTheme.colorScheme.primary,
//                            iconTint = MaterialTheme.colorScheme.onPrimary,
//                            size = 56.dp
//                        )
//                    }
//                }
//            }

//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)
//                    .verticalScroll(rememberScrollState())
//            ) {
            Box( modifier = Modifier
                .fillMaxWidth()
                .weight(0.40f),
                ) {
                if (pomodoroState?.isEnabled == true) {
                    //-------------- PomodoroControlCard -------
                    PomodoroCard(
                        sessionName = sessionName,
                        totalSessionSeconds = expectedDurationSeconds,
                        sessionStatus = sessionStatus,
                        pomodoroState = pomodoroState,
                        workCyclesBeforeLongBreak = workCyclesBeforeLongBreak,
                        onStart = onStartSession,
                        onPause = onPauseSession,
                        onResume = onResumeSession,
                        onStop = onStopSession,
                        onSkipBreak = onSkipBreak,
                        onMenuClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        style = PomodoroCardStyle(0.8f, 0.5f, 0.5f, 0.6f, 1f)
                    )
                } else {

                    // ----- now we bring sessioncontrol card or stopwatch card
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
                        modifier = Modifier.fillMaxWidth(),
                        style = SessionControlCardStyle(1f, 1f, 1f, 1f)

                    )
                }
            }

            // zone for utitlity buttons
            Row(
                modifier = Modifier.fillMaxWidth()
                    .weight(0.04f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f).height(40.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(
                        onClick = { showFocusSheet = true }
                    ) {
                        if (selectedTodoCategory == TodoCategory.TODAY)
                            Text("TODY [ $todayTasksLeft ]")
                        if (selectedTodoCategory == TodoCategory.FUTURE)
                            Text("FUTY [ $futureTasksLeft ]")
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.03f)
            ){

            }

                // Zone 6 (18%): View Statistics & Past Sessions
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.18f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onStatsClick)
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "View Statistics",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowForwardIos,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                   // Spacer(modifier = Modifier.height(8.dp))
//                    if (sessionStatus != null) {
//                        IconButton(
//                            onClick = navigateToDeskOmega
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Computer,
//                                contentDescription = "Desk Mode",
//                                tint = MaterialTheme.colorScheme.onSurfaceVariant
//                            )
//                        }
//                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "PAST SESSIONS",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(groupedSessions) { sessionGroup ->
                                Column(
                                    modifier = Modifier.width(groupWidth)
                                ) {
                                    sessionGroup.forEach { session ->
                                        UnplannedRecentSessionItem(session = session)
                                    }
                                }
                            }
                        }
                    }
                }
            // -- USING AS SPACER FOR dynamics
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.007f)
            ){

            }
         //   } // ------- making things scrollable
        }
    }

    // to do button --------------
    if (showFocusSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFocusSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
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

                Spacer(modifier = Modifier.height(20.dp))

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

                Spacer(modifier = Modifier.height(20.dp))

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
}


@Composable
fun UnplannedRecentSessionItem(
    session: UnplannedProjectRecentSessionUiModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 3.dp, 0.dp, 0.dp),
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


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun UnplannedProjectSessionScreenPreview() {
        UnplannedProjectSessionScreen(

            // ---------- Header ----------
            projectName = "Omega v1.1",

            breadcrumb =
                "Android > Data Layer > Repository",

            // ---------- Progress ----------
            currentDurationSeconds = 29460,      // 08:11:00

            expectedDurationSeconds = 43200,     // 12:00:00

            totalSessions = 18,

            // ---------- Session ----------
            sessionName = "",

            activeSessionName = "Implement Session Screen",

            onSessionNameChanged = {},

            // ---------- Stopwatch ----------
            sessionStatus = SessionStatus.RUNNING,

            stopwatchSeconds = 2538,             // 00:42:18

            // ---------- Controls ----------
            onPauseSession = {},

            onResumeSession = {},

            onStopSession = {},

            // ---------- Recent Sessions ----------
            recentSessions = listOf(

                UnplannedProjectRecentSessionUiModel(
                    id = 1,
                    sessionName = "Repository Refactor",
                    durationSeconds = 3600
                ),

                UnplannedProjectRecentSessionUiModel(
                    id = 2,
                    sessionName = "Tree Traversal",
                    durationSeconds = 2700
                ),

                UnplannedProjectRecentSessionUiModel(
                    id = 3,
                    sessionName = "Session Screen UI",
                    durationSeconds = 1800
                ),

                UnplannedProjectRecentSessionUiModel(
                    id = 4,
                    sessionName = "ViewModel",
                    durationSeconds = 4200
                ),

                UnplannedProjectRecentSessionUiModel(
                    id = 5,
                    sessionName = "Navigation",
                    durationSeconds = 2400
                ),

                UnplannedProjectRecentSessionUiModel(
                    id = 6,
                    sessionName = "Repository Testing",
                    durationSeconds = 3000
                )
            ),

            // ---------- Navigation ----------
            onBack = {},

            onStatsClick = {},
            onStartSession = {},
            pomodoroState = PomodoroState(

                phase = PomodoroPhase.WORK,
                remainingSeconds = 25 * 60,
                completedWorkCycles = 2,
                isRunning = true,
                isEnabled = false
            ),
            workCyclesBeforeLongBreak = 4,
            onSkipBreak = {},
            selectedDurationMinutes = null,
            onDurationSelected = {}


        )
    }