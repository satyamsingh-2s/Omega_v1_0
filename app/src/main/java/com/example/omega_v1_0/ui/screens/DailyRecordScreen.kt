package com.example.omega_v1_0.ui.screens

import androidx.collection.emptyLongSet
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.sp

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
    onHistoryClick: () -> Unit = {}

    )
{
     val estimateOptions = listOf(
        5, 15, 30, null, 45, 60, 90, 105, 120
    )


    val listState =
        rememberLazyListState(
            initialFirstVisibleItemIndex = 1
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)

    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Daily Record",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Today's Total time",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = formatDuration(todaystotalSeconds),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary // Apply purple color
        )
        Spacer(modifier = Modifier.height(34.dp))



        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = sessionName,
                onValueChange = onSessionNameChange,

                textStyle =
                    MaterialTheme.typography.bodyMedium,

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

//            OutlinedTextField(
//                value = expectedDuration,
//                onValueChange = onExpectedDurationChange,
//
//                textStyle =
//                    MaterialTheme.typography.bodyMedium,
//
//                placeholder = {
//                    Text(
//                        text = "Expected Minutes",
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Outlined.Schedule,
//                        contentDescription = "Expected Minutes",
//                        tint = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true,
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color.Transparent,
//                    unfocusedBorderColor = Color.Transparent,
//                    focusedContainerColor = MaterialTheme.colorScheme.surface,
//                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
//                )
//            )

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
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 82.sp),
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

    // ignoring this for now ❌❌❌❌❌❌❌❌❌❌❌❌❌❌
        if(sessionStatus==null)
            Spacer(modifier = Modifier.height(48.dp))
        else
            Spacer(modifier = Modifier.height(56.dp))

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
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "View History",
                    style = MaterialTheme.typography.titleMedium,
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

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "PAST SESSIONS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))


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