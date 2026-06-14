package com.example.omega_v1_0.ui.screens

import androidx.collection.emptyLongSet
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.ui.model.DailyRecordRecentsSessionUiModel
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.omega_v1_0.ui.model.ToDoListUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyRecordScreen(

    todaystotalSeconds: Int = 0,
    sessionStatus: SessionStatus? = null,
    sessionName: String = "",
    activeSessionName: String? =null,
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

    // ----------To do list variables -----
    todoItems: List<ToDoListUiModel> = emptyList(),
    newTodoText: String = "",
    onTodoTextChanged: (String) -> Unit = {},
    onAddTodo: () -> Unit = {},
    onToggleTodo: (ToDoListUiModel) -> Unit = {},
    onDeleteTodo: (Long) -> Unit = {}

    //------------- break section variables -----------
    ,
    isBreakRunning: Boolean = false,
    currentBreakSeconds: Long = 0,
    todaysBreakSeconds: Int = 0,
    todaysBreakCount: Int = 0,
    onEndBreak: () -> Unit = {},
    onStartBreak: () -> Unit = {}

    )
{

     val estimateOptions = listOf( 5, 15, 30, 45, null, 60, 90, 105, 120)
    val listState =
        rememberLazyListState(
            initialFirstVisibleItemIndex = 2)

    // ---------------- variables for todo list ----------------
    var showFocusSheet by remember { mutableStateOf(false)
    }
    val remainingCount = todoItems.count {
            !it.isCompleted
        }
    //-----------------------------------------

    Box() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)

        ) {

            Spacer(modifier = Modifier.height(16.dp))

            if (sessionStatus == null) {
                Text(
                    text = "Daily Record",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 19.sp),
                )
            } else {
                Spacer(modifier = Modifier.height(90.dp))
            }

            Spacer(modifier = Modifier.height(22.dp))

            //--------------------- BREAK SECTION ---------------


            //-------------------------------------------

            if (sessionStatus == null) {
                Text(
                    text = "Today's Total time",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = formatDuration(todaystotalSeconds),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary // Apply purple color
                )
            }
            Spacer(modifier = Modifier.height(34.dp))


            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedTextField(
                    value = sessionName,
                    onValueChange = onSessionNameChange,

                    textStyle =
                        MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),

                    placeholder = {

                        Text(
                            text =
                                if (
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
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )

                //------------------------------------------------------

                LazyRow(
                    state = listState,
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {

                    items(estimateOptions) { option ->

                        FilterChip(

                            selected =
                                selectedEstimateMinutes == option,

                            onClick = {
                                onEstimateSelected(option)
                            },

                            label = {

                                Text(
                                    text =
                                        option?.let {
                                            "${it}m"
                                        } ?: "○"
                                )
                            }
                        )
                    }
                }
                // FIX: Add the "Expected Minutes" label here


                //--------------------------------------------
            }
            if (selectedEstimateMinutes != null) { // Only show label if an estimate is selected
                // Spacer(modifier = Modifier.height(-12.dp))
                Text(
                    text = "Expected Durations",
                    style = MaterialTheme.typography.labelSmall, // A subtle, appropriate style
                    color = MaterialTheme.colorScheme.onSurfaceVariant, // A muted color
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Center the text horizontally
                )
            }

            Spacer(modifier = Modifier.height(44.dp))


            // ---------- stopwatch ------------------
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatDuration(stopwatchSeconds),
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface // Ensure good contrast
                )
                Text(
                    text = "HH : MM : SS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(44.dp))


// ------------------ buttons ---------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                when (sessionStatus) {

                    null -> {

                        CircularIconButton(
                            onClick = onStartSession,
                            icon = Icons.Filled.PlayArrow,
                            contentDescription = "Start Session",
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            iconTint = MaterialTheme.colorScheme.onPrimary,
                            size = 64.dp // Larger button for start
                        )
                    }

                    SessionStatus.RUNNING -> {

                        CircularIconButton(
                            onClick = onPauseSession,
                            icon = Icons.Filled.Pause,
                            contentDescription = "Pause Session",
                            backgroundColor = MaterialTheme.colorScheme.surfaceVariant, // Muted for pause
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

            // ignoring this for now ❌❌❌❌❌❌❌❌❌❌❌❌❌❌-----------------------------------------
            if (sessionStatus == null)
                Spacer(modifier = Modifier.height(28.dp))
            else
                Spacer(modifier = Modifier.height(36.dp))

            if (sessionStatus == null) {
                // View History button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onHistoryClick)
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BarChart,
                        contentDescription = "View History",
                        tint = MaterialTheme.colorScheme.primary, // Primary color for icon
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View History",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.primary, // Primary color for text
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(32.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier.weight(1f).height(52.dp),
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
                    Spacer(modifier = Modifier.width(12.dp))
                }
                Box(
                    modifier = Modifier.weight(1f).height(52.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {

                    Button(
                        onClick = {
                            showFocusSheet = true
                        }
                    ) {
                        Text(
                            "To Do (${todoItems.count { !it.isCompleted }})"
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }

//------------------------ Todo_list section -----------------------

            if (showFocusSheet) {
                ModalBottomSheet(

                    onDismissRequest = {
                        showFocusSheet = false
                    }

                ) {
                    Text(
                        text = "TO DO",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(6.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding( 10.dp)
                    ) {
                        TextField(
                            value = newTodoText,
                            onValueChange = onTodoTextChanged,
                            modifier = Modifier.weight(1f).padding(15.dp, 0.dp, 12.dp, 0.dp),
                            singleLine = true,
                            label = {
                                Text("........")
                            })
                        //  Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onAddTodo,
                            enabled = todoItems.size < 7,
                            modifier = Modifier.height(56.dp).padding(38.dp, 0.dp, 15.dp, 0.dp)
                        ) {
                            Text("+")
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f)
                      //      .height(100.dp)

                    ) {
                        items(todoItems) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 16.dp, vertical = 0.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = item.isCompleted,
                                    onCheckedChange = {
                                        onToggleTodo(item)
                                    }
                                )
                                Text(
                                    text = item.text,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        onDeleteTodo(item.id)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete"
                                    )
                                }
                            }
                        }
                    }
                //    Spacer(modifier = Modifier.height(6.dp))

                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            if (sessionStatus == null) {

                Text(
                    text = "PAST SESSIONS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))


                // ----- box for recent sessions -----------
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp) // Adjusted height to accommodate more items if necessary
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(4.dp)
                    ) {

                        items(recentSessions) { session ->

                            RecentSessionItem(
                                session = session
                            )
                            HorizontalDivider(modifier = Modifier.padding(horizontal = 1.dp))
                        }
                    }
                }
            }
        }

        // ============================ BREAK PART =================================
        if (isBreakRunning) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(alpha = 0.70f)
                    ) .clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) { },
                contentAlignment = Alignment.Center
            ) {

                ActiveBreakCard(
                    currentBreakSeconds = currentBreakSeconds,
                    todaysBreakSeconds = todaysBreakSeconds,
                    todaysBreakCount = todaysBreakCount,
                    onEndBreak = onEndBreak,
                    onFocusClick = {
                        showFocusSheet = true
                    },
                    remainingTodoCount = remainingCount
                )
            }
        }

        // ==============================xxxxxxxxxxxxxx================
    }

    }



@Composable
fun CircularIconButton(
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    backgroundColor: Color,
    iconTint: Color,
    size: Dp
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(size)
            .background(backgroundColor, CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(size / 2) // Icon size relative to button size
        )
    }
}


    fun formatDuration(
        totalSeconds: Int
    ): String {

        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return String.format(
            "%02d:%02d:%02d",
            hours,
            minutes,
            seconds
        )
    }

    @Composable
    fun RecentSessionItem(
        session: DailyRecordRecentsSessionUiModel
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = session.sessionName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = formatDuration(
                    session.durationSeconds
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

@Composable
fun ActiveBreakCard(
    currentBreakSeconds: Long,
    todaysBreakSeconds: Int,
    todaysBreakCount: Int,
    onEndBreak: () -> Unit,
    onFocusClick: () -> Unit,
    remainingTodoCount:Int

) {
    Card(

        modifier = Modifier
            .fillMaxWidth(0.85f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BREAK ACTIVE",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text =
                    formatDuration(
                        currentBreakSeconds.toInt()),
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(16.dp)
            )
            Text(
                text = "Today's Recovery")
            Text(
                text = formatDuration(todaysBreakSeconds)
            )
            Spacer( modifier =Modifier.height(8.dp)
            )
            Text(
                text = "Breaks Taken")
            Text(
                text = todaysBreakCount.toString())
            Spacer(modifier = Modifier.height(16.dp)
            )
            Button(
                onClick = onFocusClick
            ) {
                Text(
                    "TODO ($remainingTodoCount)"
                )
            }
            Button(
                onClick = onEndBreak
            ) {
                Text("End Break")
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

            selectedEstimateMinutes= 15,

            stopwatchSeconds = 873,

            recentSessions = listOf(

                DailyRecordRecentsSessionUiModel(
                    id = 1,
                    sessionName = "Android UI",
                    durationSeconds = 1800
                ),

                DailyRecordRecentsSessionUiModel(
                    id = 2,
                    sessionName = "Signals Study",
                    durationSeconds = 2700
                ),

                DailyRecordRecentsSessionUiModel(
                    id = 3,
                    sessionName = "Session 3",
                    durationSeconds = 1200
                )
            )
        )
    }
}
//@Composable
//fun ActiveBreakCardPreview() {
//
//    ActiveBreakCard(
//
//        currentBreakSeconds = 753,
//
//        todaysBreakSeconds = 2710,
//
//        todaysBreakCount = 3,
//
//        onEndBreak = {}
//    )
//}