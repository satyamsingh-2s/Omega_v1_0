package com.example.omega_v1_0.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.omega_engines.pomodoro_engine.PomodoroPhase
import com.example.omega_v1_0.omega_engines.pomodoro_engine.PomodoroState
import com.example.omega_v1_0.ui.components.common.CircularIconButton
import com.example.omega_v1_0.ui.utils.formatDuration

@Composable
fun PomodoroCard(
    sessionName: String,
    totalSessionSeconds: Int,
    sessionStatus: SessionStatus?,
    pomodoroState: PomodoroState,
    workCyclesBeforeLongBreak: Int,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    onSkipBreak: () -> Unit,
    onMenuClick: () -> Unit,
    style: PomodoroCardStyle = PomodoroCardStyle(),
    modifier: Modifier = Modifier
) {
    val isBreak = pomodoroState.phase == PomodoroPhase.SHORT_BREAK || pomodoroState.phase == PomodoroPhase.LONG_BREAK
    val phaseProgress by animateFloatAsState(
        targetValue = calculateProgress(pomodoroState),
        label = "phase_progress"
    )

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        PomodoroProgressBorder(
            progress = phaseProgress,
            phase = pomodoroState.phase,
            style = style
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(style.cardCornerRadius),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                       // .padding(style.contentPadding),
                    ,horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(style.cardSpacing)
                ) {
                    PomodoroHeader(
                        sessionName = sessionName,
                        onMenuClick = onMenuClick,
                        style = style
                    )
                    PomodoroPhaseChip(
                        phase = pomodoroState.phase,
                        style = style
                    )
                    Spacer(modifier = Modifier.height(style.timerSpacing))
                    PomodoroTimer(
                        remainingSeconds = pomodoroState.remainingSeconds,
                        style = style
                    )
                    PomodoroCycleInfo(
                        completedWorkCycles = pomodoroState.completedWorkCycles,
                        totalCycles = workCyclesBeforeLongBreak,
                        style = style
                    )
                    PomodoroCycleIndicators(
                        completedWorkCycles = pomodoroState.completedWorkCycles,
                        totalCycles = workCyclesBeforeLongBreak,
                        phase = pomodoroState.phase,
                        style = style
                    )
                    Spacer(modifier = Modifier.height(style.dividerPadding))
                    Divider(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f),
                        thickness = style.dividerThickness
                    )
                    PomodoroFooter(
                        totalSessionSeconds = totalSessionSeconds,
                        style = style
                    )
                    Spacer(modifier = Modifier.height(style.bottonPadding))
                }
            }
        }
        Spacer(modifier = Modifier.height(style.outerSpacing))
        PomodoroControls(
            sessionStatus = sessionStatus,
            onStart = onStart,
            onPause = onPause,
            onResume = onResume,
            onStop = onStop,
            style = style
        )
        if (isBreak) {
            Spacer(modifier = Modifier.height(style.skipButtonSpacing))
            Button(
                onClick = onSkipBreak,
                colors = ButtonDefaults.filledTonalButtonColors(),
                modifier = Modifier.height(style.skipButtonHeight)
            ) {
                Text(
                    text = "Skip Break",
                    fontSize = style.skipButtonFontSize
                )
            }
        }
    }
}

@Composable
private fun PomodoroProgressBorder(
    progress: Float,
    phase: PomodoroPhase,
    style: PomodoroCardStyle,
    content: @Composable () -> Unit
) {
    val activeColor = when (phase) {
        PomodoroPhase.WORK -> MaterialTheme.colorScheme.primary
        PomodoroPhase.SHORT_BREAK -> Color(0xFF4CAF50)
        PomodoroPhase.LONG_BREAK -> Color(0xFF2196F3)
    }
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
    val cornerRadius = style.completionBorderCornerRadius
    val borderThickness = style.completionBorderThickness

    Box(
        modifier = Modifier
            .drawBehind {
                val strokeWidth = borderThickness.toPx()
                val inset = strokeWidth / 2
                val size = Size(
                    width = size.width - inset * 2,
                    height = size.height - inset * 2
                )

                // Draw inactive border
                drawRoundRect(
                    color = inactiveColor,
                    topLeft = Offset(inset, inset),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius.toPx()),
                    style = Stroke(width = strokeWidth)
                )

                // Draw active border
                if (progress > 0f) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        val topLeft = Offset(inset, inset)
                        val topRight = Offset(inset + size.width, inset)
                        val bottomRight = Offset(inset + size.width, inset + size.height)
                        val bottomLeft = Offset(inset, inset + size.height)
                        val cornerRadiusPx = cornerRadius.toPx()

                        // Start at top-left corner (after the first radius)
                        moveTo(topLeft.x + cornerRadiusPx, topLeft.y)

                        // Top edge
                        lineTo(topRight.x - cornerRadiusPx, topLeft.y)
                        // Top-right corner
                        arcTo(
                            rect = androidx.compose.ui.geometry.Rect(
                                topRight.x - cornerRadiusPx * 2,
                                topRight.y,
                                topRight.x,
                                topRight.y + cornerRadiusPx * 2
                            ),
                            startAngleDegrees = -90f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )

                        // Right edge
                        lineTo(bottomRight.x, bottomRight.y - cornerRadiusPx)
                        // Bottom-right corner
                        arcTo(
                            rect = androidx.compose.ui.geometry.Rect(
                                bottomRight.x - cornerRadiusPx * 2,
                                bottomRight.y - cornerRadiusPx * 2,
                                bottomRight.x,
                                bottomRight.y
                            ),
                            startAngleDegrees = 0f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )

                        // Bottom edge
                        lineTo(bottomLeft.x + cornerRadiusPx, bottomRight.y)
                        // Bottom-left corner
                        arcTo(
                            rect = androidx.compose.ui.geometry.Rect(
                                bottomLeft.x,
                                bottomRight.y - cornerRadiusPx * 2,
                                bottomLeft.x + cornerRadiusPx * 2,
                                bottomRight.y
                            ),
                            startAngleDegrees = 90f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )

                        // Left edge
                        lineTo(topLeft.x, topLeft.y + cornerRadiusPx)
                        // Top-left corner
                        arcTo(
                            rect = androidx.compose.ui.geometry.Rect(
                                topLeft.x,
                                topLeft.y,
                                topLeft.x + cornerRadiusPx * 2,
                                topLeft.y + cornerRadiusPx * 2
                            ),
                            startAngleDegrees = 180f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                        close()
                    }

                    val pathMeasure = androidx.compose.ui.graphics.PathMeasure().apply {
                        setPath(path, false)
                    }
                    val totalLength = pathMeasure.length
                    val desiredLength = totalLength * progress
                    val drawnPath = androidx.compose.ui.graphics.Path()
                    pathMeasure.getSegment(0f, desiredLength, drawnPath, true)

                    drawPath(
                        path = drawnPath,
                        color = activeColor,
                        style = Stroke(width = strokeWidth)
                    )
                }
            }
    ) {
        content()
    }
}

@Composable
private fun PomodoroHeader(
    sessionName: String,
    onMenuClick: () -> Unit,
    style: PomodoroCardStyle
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = sessionName,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = style.sessionNameFontSize),
            color = MaterialTheme.colorScheme.onSurface
        )
        IconButton(onClick = onMenuClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(style.menuIconSize)
            )
        }
    }
}

@Composable
private fun PomodoroPhaseChip(
    phase: PomodoroPhase,
    style: PomodoroCardStyle
) {
    val phaseTitle = when (phase) {
        PomodoroPhase.WORK -> "WORK"
        PomodoroPhase.SHORT_BREAK -> "SHORT BREAK"
        PomodoroPhase.LONG_BREAK -> "LONG BREAK"
    }
    val color = when (phase) {
        PomodoroPhase.WORK -> MaterialTheme.colorScheme.primary
        PomodoroPhase.SHORT_BREAK -> Color(0xFF4CAF50)
        PomodoroPhase.LONG_BREAK -> Color(0xFF2196F3)
    }

    Box(
        modifier = Modifier
            .background(
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(style.phaseChipCornerRadius)
            )
            .height(style.phaseChipHeight)
            .padding(horizontal = style.phaseChipHorizontalPadding),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = phaseTitle,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = style.phaseChipFontSize
            ),
            color = color
        )
    }
}

@Composable
private fun PomodoroTimer(
    remainingSeconds: Int,
    style: PomodoroCardStyle
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Remaining",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                fontSize = style.remainingLabelFontSize
            )
        )
        Spacer(modifier = Modifier.height(style.timerLabelSpacing))
        Text(
            text = formatDuration(remainingSeconds),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp,
                fontSize = style.timerTextSize
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun PomodoroCycleInfo(
    completedWorkCycles: Int,
    totalCycles: Int,
    style: PomodoroCardStyle
) {
    Text(
        text = "Cycle $completedWorkCycles / $totalCycles",
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Medium,
            fontSize = style.cycleFontSize
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun PomodoroCycleIndicators(
    completedWorkCycles: Int,
    totalCycles: Int,
    phase: PomodoroPhase,
    style: PomodoroCardStyle
) {
    val color = when (phase) {
        PomodoroPhase.WORK -> MaterialTheme.colorScheme.primary
        PomodoroPhase.SHORT_BREAK -> Color(0xFF4CAF50)
        PomodoroPhase.LONG_BREAK -> Color(0xFF2196F3)
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(style.cycleDotSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalCycles) { index ->
            val isCompleted = index < completedWorkCycles
            Box(
                modifier = Modifier.size(style.cycleDotContainerSize),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isCompleted) style.cycleDotActiveSize else style.cycleDotInactiveSize)
                        .background(
                            color = if (isCompleted) color else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun PomodoroFooter(
    totalSessionSeconds: Int,
    style: PomodoroCardStyle
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(style.footerIconSpacing))
        Icon(
            imageVector = Icons.Outlined.AccessTime,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.size(style.footerIconSize)
        )
        Text(
            text = "Total Session Time",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = style.footerLabelFontSize
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = formatDuration(totalSessionSeconds),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = style.footerDurationTextSize
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.width(style.footerIconSpacing))
    }
}

@Composable
private fun PomodoroControls(
    sessionStatus: SessionStatus?,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    style: PomodoroCardStyle
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (sessionStatus) {
            SessionStatus.RUNNING -> {
                CircularIconButton(
                    onClick = onPause,
                    icon = Icons.Default.Pause,
                    contentDescription = "Pause",
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                    size = style.controlButtonSize
                )
                Spacer(modifier = Modifier.width(style.buttonSpacing))
                CircularIconButton(
                    onClick = onStop,
                    icon = Icons.Default.Stop,
                    contentDescription = "Stop",
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                    size = style.controlButtonSize
                )
            }
            SessionStatus.PAUSED -> {
                CircularIconButton(
                    onClick = onResume,
                    icon = Icons.Default.PlayArrow,
                    contentDescription = "Resume",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    iconTint = MaterialTheme.colorScheme.onPrimary,
                    size = style.startButtonSize
                )
                Spacer(modifier = Modifier.width(style.buttonSpacing))
                CircularIconButton(
                    onClick = onStop,
                    icon = Icons.Default.Stop,
                    contentDescription = "Stop",
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                    size = style.controlButtonSize
                )
            }
            else -> { // Stopped or null
                CircularIconButton(
                    onClick = onStart,
                    icon = Icons.Default.PlayArrow,
                    contentDescription = "Start",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    iconTint = MaterialTheme.colorScheme.onPrimary,
                    size = style.startButtonSize
                )
            }
        }
    }
}

private fun calculateProgress(pomodoroState: PomodoroState): Float {
    val totalDurationSeconds = when (pomodoroState.phase) {
        PomodoroPhase.WORK -> 25 * 60
        PomodoroPhase.SHORT_BREAK -> 5 * 60
        PomodoroPhase.LONG_BREAK -> 15 * 60
    }
    return (totalDurationSeconds - pomodoroState.remainingSeconds).toFloat() / totalDurationSeconds.toFloat()
}

data class PomodoroCardStyle(
    val headerScale: Float = 1f,
    val timerScale: Float = 1f,
    val footerScale: Float = 1f,
    val controlsScale: Float = 1f,
    val cardScale: Float = 1f
) {
    // Card scale group
    val cardCornerRadius: Dp = 72.dp * cardScale
    val contentPadding: Dp = 0.5.dp * cardScale
    val completionBorderThickness: Dp = 10.dp * cardScale
    val completionBorderCornerRadius: Dp = 72.dp * cardScale
    val outerSpacing: Dp = 14.dp * cardScale
    val dividerPadding: Dp = 2.dp * cardScale
    val bottonPadding: Dp = 15.dp * cardScale
    val dividerThickness: Dp = 2.dp * cardScale

    // Header scale group
    val cardSpacing: Dp = 2.dp * headerScale
    val sessionNameFontSize = 16.sp * headerScale
    val menuIconSize: Dp = 24.dp * headerScale
    val phaseChipHeight: Dp = 36.dp * headerScale
    val phaseChipHorizontalPadding: Dp = 24.dp * headerScale
    val phaseChipFontSize: androidx.compose.ui.unit.TextUnit = 14.sp * headerScale
    val phaseChipCornerRadius: Dp = 50.dp * headerScale

    // Timer scale group
    val timerSpacing: Dp = 8.dp * timerScale
    val timerLabelSpacing: Dp = 4.dp * timerScale
    val remainingLabelFontSize: androidx.compose.ui.unit.TextUnit = 16.sp * timerScale
    val timerTextSize: androidx.compose.ui.unit.TextUnit = 40.sp * timerScale
    val cycleFontSize: androidx.compose.ui.unit.TextUnit = 12.sp * timerScale
    val cycleDotContainerSize: Dp = 32.dp * timerScale
    val cycleDotActiveSize: Dp = 28.dp * timerScale
    val cycleDotInactiveSize: Dp = 24.dp * timerScale
    val cycleDotSpacing: Dp = 16.dp * timerScale

    // Footer scale group
    val footerIconSize: Dp = 20.dp * footerScale
    val footerIconSpacing: Dp = 20.dp * footerScale
    val footerLabelFontSize: androidx.compose.ui.unit.TextUnit = 16.sp * footerScale
    val footerDurationTextSize: androidx.compose.ui.unit.TextUnit = 20.sp * footerScale

    // Controls scale group
    val startButtonSize: Dp = 64.dp * controlsScale
    val controlButtonSize: Dp = 56.dp * controlsScale
    val buttonSpacing: Dp = 40.dp * controlsScale
    val skipButtonSpacing: Dp = 16.dp * controlsScale
    val skipButtonHeight: Dp = 48.dp * controlsScale
    val skipButtonFontSize: androidx.compose.ui.unit.TextUnit = 14.sp * controlsScale
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PomodoroCardPreview() {
        PomodoroCard(
            sessionName = "Deep Learning Notes",
            totalSessionSeconds = 3600,
            sessionStatus = SessionStatus.RUNNING,
            pomodoroState = PomodoroState(
                phase = PomodoroPhase.WORK,
                remainingSeconds = 18 * 60 + 22,
                completedWorkCycles = 2,
                isRunning = true,
                isEnabled = true
            ),
            workCyclesBeforeLongBreak = 4,
            onStart = {},
            onPause = {},
            onResume = {},
            onStop = {},
            onSkipBreak = {},
            onMenuClick = {},
            style = PomodoroCardStyle(
                headerScale = 0.9f,
                timerScale = 1.3f,
                footerScale = 0.85f,
                controlsScale = 1.1f,
                cardScale = 1.0f
            )
        )
    }
