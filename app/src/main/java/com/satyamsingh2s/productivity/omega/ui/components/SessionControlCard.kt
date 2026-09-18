package com.satyamsingh2s.productivity.omega.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.unit.sp
import com.satyamsingh2s.productivity.omega.models_enums.SessionStatus
import com.satyamsingh2s.productivity.omega.ui.components.common.CircularIconButton
import com.satyamsingh2s.productivity.omega.ui.theme.StopwatchTextStyle
import com.satyamsingh2s.productivity.omega.ui.utils.formatDuration

/**
 * Sizes to its own content (`wrapContentHeight`, set by the caller)
 * rather than stretching to fill an allotted box - this is what lets
 * it stay correct whether the parent gives it 220dp or 320dp.
 */
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
    style: SessionControlCardStyle = SessionControlCardStyle(),
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 2)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = style.horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(style.verticalSpacing)
    ) {

        // Zone 1: Session Input & Estimates
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = sessionName,
                onValueChange = onSessionNameChanged,
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = style.sessionNameFontSize),
                placeholder = {
                    Text(
                        text = if (sessionStatus != null && sessionName.isBlank()) {
                            activeSessionName ?: "Session Name"
                        } else {
                            "Session Name"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
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

            Spacer(modifier = Modifier.height(style.smallSpacing))

            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(style.chipSpacing),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(SessionDefaults.durationOptions) { option ->
                    FilterChip(
                        selected = selectedDurationMinutes == option,
                        onClick = { onDurationSelected(option) },
                        label = {
                            Text(
                                text = option?.let { "${it}m" } ?: "\u25CB",
                                fontSize = style.chipLabelFontSize
                            )
                        },
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

        // Zone 2: Stopwatch Display
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatDuration(stopwatchSeconds),
                style = style.stopwatchTextStyle,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Text(
                text = "HH : MM : SS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = style.expectedLabelFontSize
            )
        }

        // Zone 3: Session Control Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(style.buttonSpacing, Alignment.CenterHorizontally),
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
    val durationOptions = listOf(5, 15, 30, 45, null, 60, 90, 105, 120)
}

/**
 * `startButtonSizeOverride` / `controlButtonSizeOverride` let the
 * caller pass screen-relative sizes computed from BoxWithConstraints
 * (see UnplannedProjectSessionScreen). When null, falls back to the
 * old fixed values scaled by `controlsScale`, so this card still
 * works standalone / in previews without a screen-width context.
 */
data class SessionControlCardStyle(
    val sessionScale: Float = 1f,
    val stopwatchScale: Float = 1f,
    val controlsScale: Float = 1f,
    val scale: Float = 1f,
    val startButtonSizeOverride: Dp? = null,
    val controlButtonSizeOverride: Dp? = null,
) {
    val horizontalPadding = 24.dp * scale
    val verticalSpacing = 14.dp * scale
    val smallSpacing = 8.dp * scale

    val sessionNameFontSize = 16.sp * sessionScale
    val sessionIconSize = 24.dp * sessionScale

    val chipLabelFontSize = 14.sp * scale
    val expectedLabelFontSize = 10.sp * scale
    val chipSpacing = 8.dp * scale

    // Reduced from a fixed 86.dp gap (which could push buttons off a
    // narrow / one-handed width) to a smaller, centered gap.
    val buttonSpacing = 48.dp * scale

    // ~30% smaller than the old fixed 64dp / 56dp defaults, and this
    // is only the fallback used when the caller doesn't supply a
    // screen-relative override.
    val startButtonSize = startButtonSizeOverride ?: (55.dp * controlsScale)
    val controlButtonSize = controlButtonSizeOverride ?: (50.dp * controlsScale)

    val stopwatchTextStyle = StopwatchTextStyle.copy(
        fontSize = StopwatchTextStyle.fontSize * stopwatchScale
    )
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    backgroundColor = 0xFFFFFFFF
)
@Composable
private fun SessionControlCardPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.background)
        ) {
            SessionControlCard(
                sessionName = "Refactoring UI",
                activeSessionName = "Current Task",
                selectedDurationMinutes = 60,
                onDurationSelected = {},
                stopwatchSeconds = 3645,
                sessionStatus = SessionStatus.RUNNING,
                onSessionNameChanged = {},
                onStart = {},
                onPause = {},
                onResume = {},
                onStop = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}