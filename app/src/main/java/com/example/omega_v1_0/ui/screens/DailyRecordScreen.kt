package com.example.omega_v1_0.ui.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.models.TodoCategory
import com.example.omega_v1_0.ui.components.OmegaScreen
import com.example.omega_v1_0.ui.components.common.CircularIconButton
import com.example.omega_v1_0.ui.model.DailyRecordRecentsSessionUiModel
import com.example.omega_v1_0.ui.model.ToDoListUiModel
import com.example.omega_v1_0.ui.theme.OmegaDarkTheme
import com.example.omega_v1_0.ui.theme.OmegaRedTheme
import com.example.omega_v1_0.ui.theme.StopwatchTextStyle
import com.example.omega_v1_0.ui.utils.formatDuration


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyRecordScreen(

    todaystotalSeconds: Int = 0,
    sessionStatus: SessionStatus? = null,
    sessionName: String = "",
    activeSessionName: String? = null,
    selectedEstimateMinutes: Int? = null,
    onEstimateSelected: (Int?) -> Unit = {},
    onSessionNameChange: (String) -> Unit = {},
    // onExpectedDurationChange: (String) -> Unit = {},
    onStartSession: () -> Unit = {},
    onPauseSession: () -> Unit = {},

    onResumeSession: () -> Unit = {},

    onStopSession: () -> Unit = {},
    stopwatchSeconds: Int = 0,
    recentSessions: List<DailyRecordRecentsSessionUiModel> = emptyList(),
    onHistoryClick: () -> Unit = {},
    navigateToDeskOmega: () -> Unit = {},

    // ----------To do list variables -----
    todoItems: List<ToDoListUiModel> = emptyList(),
    newTodoText: String = "",
    onTodoTextChanged: (String) -> Unit = {},
    onAddTodo: () -> Unit = {},
    onToggleTodo: (ToDoListUiModel) -> Unit = {},
    onDeleteTodo: (Long) -> Unit = {},
    selectedTodoCategory: TodoCategory = TodoCategory.TODAY,
    onTodoCategoryChanged: (TodoCategory) -> Unit = {},

    //------------- break section variables -----------

    isBreakRunning: Boolean = false,
    currentBreakSeconds: Int = 0,
    todaysBreakSeconds: Int = 0,
    todaysBreakCount: Int = 0,
    onEndBreak: () -> Unit = {},
    onStartBreak: () -> Unit = {},

    selectedBreakMinutes: Int? = null,
    onBreakDurationSelected: (Int?) -> Unit = {},

    ) {

    val focusManager = LocalFocusManager.current

    val estimateOptions = listOf(5, 15, 30, 45, null, 60, 90, 105, 120)
    val breakOptions = listOf(2, 5, 20, null, 40, 90)
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 2
    )
    val listState2 = rememberLazyListState(
        initialFirstVisibleItemIndex = 1
    )

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

    //-------------------------- Lock portrait---TODO------------------------------------------
    val view = LocalView.current
    val activity = view.context as Activity
    activity.requestedOrientation =
        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


    // ---------------- variables for to-do list ----------------
    var showFocusSheet by remember {
        mutableStateOf(false)
    }

    val groupedSessions = recentSessions.chunked(3)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val groupWidth = screenWidth * 0.85f

    //-----------------------------------------

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background) // === temp theme =====
        .pointerInput(Unit) {
            detectTapGestures {
                focusManager.clearFocus()
                // ---- above line main - that no one in the box should have focus with-> tap gesture it activates
            }
        }

    ) {
//        Surface(
//            modifier = Modifier.fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//        ) {//---- TODO -- expermiental just remove it
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp), // Only side padding for the main column
                horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
            ) {

                // Zone 1: Header/Summary
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.25f),
                    verticalArrangement = Arrangement.Center, // Center content vertically within this zone
                    horizontalAlignment = Alignment.Start
                ) {
                    if (sessionStatus == null) {
                        Text(
                            text = "Daily Record",
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 19.sp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Small internal spacer
                    if (sessionStatus == null) {
                        Text(
                            text = "Today's Total time",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = formatDuration(todaystotalSeconds),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onSurface // Apply purple color
                        )
                    }
                }

                // Zone 2: Session Input & Estimates
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.20f),
                    verticalArrangement = Arrangement.Center, // Center content vertically
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = sessionName,
                        onValueChange = onSessionNameChange,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                        placeholder = {
                            Text(
                                text = if (
                                    sessionStatus != null &&
                                    sessionName.isBlank()
                                ) {
                                    activeSessionName ?: "Session Name"
                                } else {
                                    "Session Name"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Session Name",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp)) // Small internal spacer

                    LazyRow(
                        state = listState,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(estimateOptions) { option ->
                            FilterChip(
                                selected = selectedEstimateMinutes == option,
                                onClick = { onEstimateSelected(option) },
                                label = { Text(text = option?.let { "${it}m" } ?: "○") }
                            )
                        }
                    }
                    if (selectedEstimateMinutes != null) {
                        Text(
                            text = "Expected Durations",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Zone 3: Stopwatch Display
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.20f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = formatDuration(stopwatchSeconds),
                        // style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
                        style = StopwatchTextStyle,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "HH : MM : SS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Zone 4: Session Control Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.15f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (sessionStatus) {
                        null -> {
                            CircularIconButton(
                                onClick = onStartSession,
                                icon = Icons.Filled.PlayArrow,
                                contentDescription = "Start Session",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                iconTint = MaterialTheme.colorScheme.onPrimary,
                                size = 64.dp
                            )
                        }

                        SessionStatus.RUNNING -> {
                            CircularIconButton(
                                onClick = onPauseSession,
                                icon = Icons.Filled.Pause,
                                contentDescription = "Pause Session",
                                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                                iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                                size = 56.dp
                            )
                            Spacer(modifier = Modifier.width(86.dp))
                            CircularIconButton(
                                onClick = onStopSession,
                                icon = Icons.Filled.Stop,
                                contentDescription = "Stop Session",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                iconTint = MaterialTheme.colorScheme.onPrimary,
                                size = 56.dp
                            )
                        }

                        SessionStatus.PAUSED -> {
                            CircularIconButton(
                                onClick = onResumeSession,
                                icon = Icons.Filled.PlayArrow,
                                contentDescription = "Resume Session",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                iconTint = MaterialTheme.colorScheme.onPrimary,
                                size = 56.dp
                            )
                            Spacer(modifier = Modifier.width(86.dp))
                            CircularIconButton(
                                onClick = onStopSession,
                                icon = Icons.Filled.Stop,
                                contentDescription = "Stop Session",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                iconTint = MaterialTheme.colorScheme.onPrimary,
                                size = 56.dp
                            )
                        }
                    }
                }

                // Zone 5: Utility & Action Buttons
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.13f),
                    verticalArrangement = Arrangement.SpaceEvenly // Distribute content evenly
                ) {
                    if (sessionStatus == null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onHistoryClick)
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.BarChart,
                                contentDescription = "View History",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "View History",
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 12.sp),
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
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier.weight(1f).height(40.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (
                                !isBreakRunning &&
                                sessionStatus == null
                            ) {
                                Button(
                                    onClick = onStartBreak,
                                ) {
                                    Text("Start Break")
                                }
                            }
                            if (sessionStatus != null) {
                                IconButton(
                                    onClick = navigateToDeskOmega
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Computer,
                                        contentDescription = "Desk Mode",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        Box(
                            modifier = Modifier.weight(1f).height(40.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Button(
                                onClick = { showFocusSheet = true }
                            ) {
                                if (selectedTodoCategory == TodoCategory.TODAY)
                                    Text("TODY ($todayTasksLeft)")
                                if (selectedTodoCategory == TodoCategory.FUTURE)
                                    Text("FUTY ($futureTasksLeft)")
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.02f)
                ) { }

                // Zone 6: Recent Sessions List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.2f), // Fills remaining space proportionally

                ) {
                    if (sessionStatus == null) {
                        Text(
                            text = "PAST SESSIONS",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(0.17f)
                        )

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f) // Takes up remaining space in this zone
                            ,
                            color = Color.Transparent
                        ) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(groupedSessions) { sessionGroup ->
                                    Column(
                                        modifier = Modifier.width(groupWidth), // Use proportional width
                                    ) {
                                        sessionGroup.forEach { session ->
                                            RecentSessionItem(session = session)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ============================ BREAK PART (Overlay) =================================
            if (isBreakRunning) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.70f))
                        .pointerInput(Unit) { detectTapGestures { /* consume taps */ } },
                    contentAlignment = Alignment.Center
                ) {
                    ActiveBreakCard(
                        currentBreakSeconds = currentBreakSeconds,
                        todaysBreakSeconds = todaysBreakSeconds,
                        todaysBreakCount = todaysBreakCount,
                        onEndBreak = onEndBreak,
                        onFocusClick = { showFocusSheet = true },
                        selectedTodoCategory = selectedTodoCategory,
                        todayTasksLeft = todayTasksLeft,
                        futureTasksLeft = futureTasksLeft,
                        listState = listState2,
                        selectedBreakMinutes = selectedBreakMinutes,
                        onBrakeSelected = onBreakDurationSelected,
                        BreakOptions = breakOptions
                    )
                }
            }

            // ============================ ToDoList Modal (Overlay) =================================
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
            } // ---------- to do list bracket close
        //}
    }
}

@Composable
fun RecentSessionItem(
    session: DailyRecordRecentsSessionUiModel
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

@Composable
fun ActiveBreakCard(
    currentBreakSeconds: Int,
    todaysBreakSeconds: Int,
    todaysBreakCount: Int,
    onEndBreak: () -> Unit,
    onFocusClick: () -> Unit,
    selectedTodoCategory: TodoCategory,
    todayTasksLeft: Int?,
    futureTasksLeft: Int?,
    listState: LazyListState,
    selectedBreakMinutes: Int?,
    onBrakeSelected: (Int?) -> Unit,
    BreakOptions: List<Int?>
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.85f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp) // Use arrangement for spacing
        ) {
            Text(
                text = "BREAK ACTIVE",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = formatDuration(currentBreakSeconds.toInt()),
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "Today's Recovery"
            )
            Text(
                text = formatDuration(todaysBreakSeconds)
            )
            Text(
                text = "Breaks Taken"
            )
            Text(
                text = todaysBreakCount.toString()
            )
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(BreakOptions) { option ->
                    FilterChip(
                        selected = selectedBreakMinutes == option,
                        onClick = {
                            onBrakeSelected(option)
                            Log.d("from breakcard section ❌❌❌❌", "chip clikced $selectedBreakMinutes")
                        },
                        label = { Text(text = option?.let { "${it}m" } ?: "○") }
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onEndBreak,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) { Text("End Break") }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = onFocusClick,
                    modifier = Modifier.weight(1f).height(52.dp)
                ) {
                    if (selectedTodoCategory == TodoCategory.TODAY)
                        Text("TODY ($todayTasksLeft)")
                    if (selectedTodoCategory == TodoCategory.FUTURE)
                        Text("FUTY ($futureTasksLeft)")
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DailyRecordScreenPreview() {
    MaterialTheme {
        DailyRecordScreen(
            todaystotalSeconds = 15642,
            sessionStatus = null,
            sessionName = "",
            activeSessionName = "Session 4",
            selectedEstimateMinutes = 15,
            stopwatchSeconds = 873,
            recentSessions = listOf(
                DailyRecordRecentsSessionUiModel(id = 1, sessionName = "Android UI", durationSeconds = 1800),
                DailyRecordRecentsSessionUiModel(id = 2, sessionName = "Signals Study", durationSeconds = 2700),
                DailyRecordRecentsSessionUiModel(id = 3, sessionName = "Session 3", durationSeconds = 1200),
                DailyRecordRecentsSessionUiModel(id = 1, sessionName = "Android UI", durationSeconds = 1800),
                DailyRecordRecentsSessionUiModel(id = 2, sessionName = "Signals Study", durationSeconds = 2700),
                DailyRecordRecentsSessionUiModel(id = 3, sessionName = "Session 3", durationSeconds = 1200)
            )
        )
    }
}