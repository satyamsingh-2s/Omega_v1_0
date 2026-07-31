package com.example.omega_v1_0.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.ui.components.common.CircularIconButton
import com.example.omega_v1_0.ui.theme.StopwatchTextStyle
import com.example.omega_v1_0.ui.utils.formatDuration

@Composable
fun SessionControlCard(

    // ---------- Session ----------
    sessionName: String,
    activeSessionName: String?,

    // ---------- Duration ----------
    selectedDurationMinutes: Int?,
    onDurationSelected: (Int?) -> Unit,

    // ---------- Stopwatch ----------
    stopwatchSeconds: Int,
    sessionStatus: SessionStatus?,

    // ---------- Inputs ----------
    onSessionNameChanged: (String) -> Unit,

    // ---------- Controls ----------
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,

    // ---------- Style ----------
    style: SessionControlCardStyle =
        SessionControlCardStyle(),

    modifier: Modifier = Modifier
){

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 2
    )

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = style.horizontalPadding), // Only side padding for the main column
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {

            // Zone 2: Session Input & Estimates
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                ,verticalArrangement = Arrangement.Center, // Center content vertically
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = sessionName,
                    onValueChange = onSessionNameChanged,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = style.sessionNameFontSize),
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
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(style.sessionIconSize)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    )
                )

                Spacer(modifier = Modifier.height(style.smallSpacing)) // Small internal spacer

                LazyRow(
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(style.chipSpacing),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(SessionDefaults.durationOptions) { option ->
                        FilterChip(
                            selected = selectedDurationMinutes == option,
                            onClick = { onDurationSelected(option) },
                            label = { Text(text = option?.let { "${it}m" } ?: "○",
                                fontSize = style.chipLabelFontSize,) },
                        )
                    }
                }
                if (selectedDurationMinutes != null) {
                    Text(
                        text = "Expected Durations",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = style.expectedLabelFontSize
                    )
                }
            }

            // Zone 3: Stopwatch Display
            Column(
                modifier = Modifier
                    .fillMaxWidth()
               , verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatDuration(stopwatchSeconds),
                    // style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
                    style = style.stopwatchTextStyle,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "HH : MM : SS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = style.expectedLabelFontSize
                )
            }

            Spacer(
                modifier = Modifier.height(style.verticalSpacing)
            )

            // Zone 4: Session Control Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                ,horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (sessionStatus) {
                    null -> {
                        CircularIconButton(
                            onClick = onStart,
                            icon = Icons.Filled.PlayArrow,
                            contentDescription = "Start Session",
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            iconTint = MaterialTheme.colorScheme.onPrimary,
                            size = style.startButtonSize
                        )
                    }

                    SessionStatus.RUNNING -> {
                        CircularIconButton(
                            onClick = onPause,
                            icon = Icons.Filled.Pause,
                            contentDescription = "Pause Session",
                            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                            iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                            size = style.controlButtonSize
                        )
                        Spacer(modifier = Modifier.width(style.buttonSpacing))
                        CircularIconButton(
                            onClick = onStop,
                            icon = Icons.Filled.Stop,
                            contentDescription = "Stop Session",
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            iconTint = MaterialTheme.colorScheme.onPrimary,
                            size = style.controlButtonSize
                        )
                    }

                    SessionStatus.PAUSED -> {
                        CircularIconButton(
                            onClick = onResume,
                            icon = Icons.Filled.PlayArrow,
                            contentDescription = "Resume Session",
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            iconTint = MaterialTheme.colorScheme.onPrimary,
                            size = style.controlButtonSize
                        )
                        Spacer(modifier = Modifier.width(style.buttonSpacing))
                        CircularIconButton(
                            onClick = onStop,
                            icon = Icons.Filled.Stop,
                            contentDescription = "Stop Session",
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            iconTint = MaterialTheme.colorScheme.onPrimary,
                            size = style.controlButtonSize
                        )
                    }
                }
            }
        }
    }

object SessionDefaults {
    val durationOptions = listOf(
        5, 15, 30, 45, null, 60, 90, 105, 120
    )
}

data class SessionControlCardStyle(

    val sessionScale: Float = 1f,
    val stopwatchScale: Float = 1f,
    val controlsScale: Float = 1f,
    val scale: Float = 1f

) {

    val horizontalPadding = 24.dp * scale
    val verticalSpacing = 8.dp * scale

    val smallSpacing = 8.dp * scale

    // Session section
    val sessionNameFontSize = 16.sp * sessionScale
    val sessionIconSize = 24.dp * sessionScale

    // Chips
    val chipLabelFontSize = 14.sp * scale

    val expectedLabelFontSize = 10.sp * scale

    val chipSpacing = 8.dp * scale

    val buttonSpacing = 86.dp * scale

    val startButtonSize = 64.dp * controlsScale

    val controlButtonSize = 56.dp * controlsScale

    val stopwatchTextStyle =
        StopwatchTextStyle.copy(
            fontSize = StopwatchTextStyle.fontSize * stopwatchScale
        )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun SessionControlCardPreview(){
    SessionControlCard(
        // ---------- Session ----------
        sessionName= "apple",
        activeSessionName="dkjfsl",

    // ---------- Duration ----------
    selectedDurationMinutes=3223,
    onDurationSelected={},

    // ---------- Stopwatch ----------
    stopwatchSeconds=323,
    sessionStatus= SessionStatus.RUNNING ,

    // ---------- Inputs ----------
    onSessionNameChanged={},

    // ---------- Controls ----------
    onStart={},
    onPause={},
    onResume={},
    onStop={},

    // ---------- Style ----------
//    style: SessionControlCardStyle =
//    SessionControlCardStyle(),
//
//    modifier: Modifier = Modifier

    )
}
