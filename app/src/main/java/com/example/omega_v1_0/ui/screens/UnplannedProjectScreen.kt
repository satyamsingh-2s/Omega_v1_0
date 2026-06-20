package com.example.omega_v1_0.ui.screens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.models.SessionStatus
import com.example.omega_v1_0.ui.model.UnplannedProjectUiModel
import com.example.omega_v1_0.ui.uistate.UnplannedProjectUiState
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnplannedProjectScreen(
    uiState: UnplannedProjectUiState,
    onAddRoot: () -> Unit,
    onAddChild: (Long) -> Unit,
    onToggleCompleted: (Long, Boolean) -> Unit,
    onDialogInputChanged: (String) -> Unit,
    onDismissRootDialog: () -> Unit,
    onDismissChildDialog: () -> Unit,
    onConfirmRoot: () -> Unit,
    onConfirmChild: () -> Unit,
    onStartSession: (Long) -> Unit,
    onPauseSession: () -> Unit,
    onResumeSession: () -> Unit,
    onStopSession: () -> Unit,
    runningNodeId: Long?,
    onNodeClick: (Long) -> Unit
) {



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Unplanned Projects",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Organize and track unplanned tasks and ideas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = onAddRoot,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Root")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Root")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White // Pure white background for the screen
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.tree) { node ->
                UnplannedProjectNodeItem(
                    node = node,
                    depth = 0,
                    onAddChild = onAddChild,
                    onToggleCompleted = onToggleCompleted,
                    onStartSession = onStartSession,
                    onPauseSession = onPauseSession,
                    onResumeSession = onResumeSession,
                    onStopSession = onStopSession,
                    sessionStatus = uiState.sessionStatus,
                    runningNodeId = runningNodeId,
                    onNodeClick = onNodeClick
                )
                val sessioncontrol = runningNodeId == null || runningNodeId == node.nodeId
            }

            item {
                TipCard()
            }
        }

        if (uiState.showAddRootDialog) {
            NodeInputDialog(
                title = "Add Root Node".uppercase(Locale.getDefault()),
                value = uiState.dialogInput,
                onValueChange = onDialogInputChanged,
                onDismiss = onDismissRootDialog,
                onConfirm = onConfirmRoot
            )
        }

        if (uiState.showAddChildDialog) {
            NodeInputDialog(
                title = "Add Child Node".uppercase(Locale.getDefault()),
                value = uiState.dialogInput,
                onValueChange = onDialogInputChanged,
                onDismiss = onDismissChildDialog,
                onConfirm = onConfirmChild
            )
        }
    }
}

@Composable
fun UnplannedProjectNodeItem(
    node: UnplannedProjectUiModel,
    depth: Int,
    onAddChild: (Long) -> Unit,
    onToggleCompleted: (Long, Boolean) -> Unit,
    sessionStatus: SessionStatus?,
    onStartSession: (Long) -> Unit,
    onPauseSession: () -> Unit,
    onResumeSession: () -> Unit,
    onStopSession: () -> Unit,
    runningNodeId: Long?,
    onNodeClick: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(true) } // Nodes are expanded by default

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min) // Allows children to match parent height
        ) {
            // Vertical connection line
            if (depth > 0) {
                Box(
                    modifier = Modifier
                        .padding(start = ((depth - 1) * 20 + 8).dp) // Adjust based on depth
                        .width(2.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow))
                    .padding(start = if (depth == 0) 0.dp else 0.dp) // Indentation for the card itself
                    .clickable { onNodeClick(node.nodeId) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) // Light lavender
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (node.children.isNotEmpty()) {
                                IconButton(
                                    onClick = { expanded = !expanded },
                                    modifier = Modifier.size(24.dp) // Smaller icon button
                                ) {
                                    Icon(
                                        imageVector = if (expanded) Icons.Filled.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = if (expanded) "Collapse" else "Expand",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.width(24.dp)) // Maintain alignment for leaf nodes
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = node.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        NodeCompletionToggle(
                            isCompleted = node.isCompleted,
                            onToggle = { onToggleCompleted(node.nodeId, node.isCompleted) }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = formatDuration(node.currentDurationSeconds) + " / " + formatDuration(node.expectedDurationSeconds),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = if (node.children.isNotEmpty()) 40.dp else 32.dp) // Adjust for icon space
                    )

                    if (node.children.isEmpty() && expanded) { // Show session controls only for leaf nodes when expanded
                        Spacer(modifier = Modifier.height(16.dp))
                        SessionControls(
                            sessionStatus = sessionStatus,
                            onStart = { onStartSession(node.nodeId) },
                            onPause = onPauseSession,
                            onResume = onResumeSession,
                            onStop = onStopSession,
                            isNodeRunning = runningNodeId == node.nodeId,
                            runningNodeId = runningNodeId,
                            node = node // Pass the node to SessionControls
                        )
                    }
             // Add Child button for nodes
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(
                                onClick = { onAddChild(node.nodeId) },
                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Add Child")
                            }
                        }
                }
            }
        }

        if (expanded) {
            node.children.forEach { child ->
                UnplannedProjectNodeItem(
                    node = child,
                    depth = depth + 1,
                    onAddChild = onAddChild,
                    onToggleCompleted = onToggleCompleted,
                    onStartSession = onStartSession,
                    onPauseSession = onPauseSession,
                    onResumeSession = onResumeSession,
                    onStopSession = onStopSession,
                    sessionStatus = sessionStatus,
                    runningNodeId = runningNodeId,
                    onNodeClick = onNodeClick
                )
            }
        }
    }
}

@Composable
fun NodeCompletionToggle(
    isCompleted: Boolean,
    onToggle: () -> Unit
) {
    IconButton(
        onClick = onToggle,
        modifier = Modifier.size(32.dp) // Adjust size as needed
    ) {
        if (isCompleted) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(4.dp) // Padding for the checkmark
            )
        } else {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun NodeInputDialog(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SessionControls(
    sessionStatus: SessionStatus?,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,

    isNodeRunning: Boolean,
    runningNodeId: Long?,
    node: UnplannedProjectUiModel // Added node as a parameter
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (sessionStatus) {

            null -> {
                Button(
                    onClick = onStart,
                    modifier = Modifier.weight(1f),
                    enabled = !isNodeRunning, // Disable if another node is running
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = "Start")
                    Text("Start")
                }
            }

            SessionStatus.RUNNING -> {

                if (runningNodeId==node.nodeId) { // Only show pause/stop for the currently running node
                    Button(
                        onClick = onPause,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.Pause, contentDescription = "Pause")
                        Text("Pause")
                    }
                    Button(
                        onClick = onStop,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.Stop, contentDescription = "Stop")
                        Text("Stop")
                    }
                }
                else
                {
//                    // Show disabled start button for other nodes if one is running
//                    Button(
//                        onClick = { /* Do nothing */ },
//                        modifier = Modifier.weight(1f),
//                        enabled = false,
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
//                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
//                        ),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Icon(Icons.Filled.PlayArrow, contentDescription = "Start")
//                        Text("Start")
//                    }
                }
            }

            SessionStatus.PAUSED -> {
                if (runningNodeId==node.nodeId) { // Only show resume/stop for the currently paused node
                    Button(
                        onClick = onResume,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Resume")
                        Text("Resume")
                    }
                    Button(
                        onClick = onStop,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.Stop, contentDescription = "Stop")
                        Text("Stop")
                    }
                } else {
                    // Show disabled start button for other nodes if one is paused
//                    Button(
//                        onClick = { /* Do nothing */ },
//                        modifier = Modifier.weight(1f),
//                        enabled = false,
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
//                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
//                        ),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Icon(Icons.Filled.PlayArrow, contentDescription = "Start")
//                        Text("Start")
//                    }
                }
            }
        }

        // Stats Button - Always visible for leaf nodes, but only if the node is running/paused or has children
//        if (node.children.isEmpty()) { // Only show for leaf nodes
//            Spacer(modifier = Modifier.width(8.dp))
//            Button(
//                onClick = { /* Handle stats click */ },
//                modifier = Modifier.weight(1f),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                    contentColor = MaterialTheme.colorScheme.primary
//                ),
//                shape = RoundedCornerShape(12.dp)
//            ) {
//                Icon(Icons.Outlined.BarChart, contentDescription = "Stats")
//                Text("Stats")
//            }
//        }
    }
}

@Composable
fun TipCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f) // Subtle purple tint
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Lightbulb,
                contentDescription = "How it works",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "How it works",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Add root tasks and break them down into smaller steps. Track time and mark tasks as completed.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UnplannedProjectScreenPreview() {
    MaterialTheme {
        val sampleTree = listOf(
            UnplannedProjectUiModel(
                nodeId = 1L,
                title = "Android Architecture",
                currentDurationSeconds = 5720, // 01:35:20
                expectedDurationSeconds = 7200, // 02:00:00
                isCompleted = false,
                children = listOf(
                    UnplannedProjectUiModel(
                        nodeId = 2L,
                        title = "Learn MVVM",
                        currentDurationSeconds = 2710, // 00:45:10
                        expectedDurationSeconds = 3600, // 01:00:00
                        isCompleted = true,
                        children = emptyList()
                    ),
                    UnplannedProjectUiModel(
                        nodeId = 3L,
                        title = "Dependency Injection",
                        currentDurationSeconds = 1530, // 00:25:30
                        expectedDurationSeconds = 1800, // 00:30:00
                        isCompleted = false,
                        children = listOf(
                            UnplannedProjectUiModel(
                                nodeId = 4L,
                                title = "Hilt Implementation",
                                currentDurationSeconds = 610, // 00:10:10
                                expectedDurationSeconds = 900, // 00:15:00
                                isCompleted = false,
                                children = emptyList()
                            )
                        )
                    )
                )
            ),
            UnplannedProjectUiModel(
                nodeId = 5L,
                title = "Personal Finance Tracker",
                currentDurationSeconds = 1215,
                expectedDurationSeconds = 3600,
                isCompleted = false,
                children = emptyList()
            ),
            UnplannedProjectUiModel(
                nodeId = 6L,
                title = "Reading List",
                currentDurationSeconds = 915,
                expectedDurationSeconds = 1800,
                isCompleted = true,
                children = emptyList()
            ),
            UnplannedProjectUiModel(
                nodeId = 7L,
                title = "Workout Plan",
                currentDurationSeconds = 0,
                expectedDurationSeconds = 2700,
                isCompleted = false,
                children = emptyList()
            )
        )
        UnplannedProjectScreen(
            uiState = UnplannedProjectUiState(
                tree = sampleTree,
                sessionStatus = SessionStatus.RUNNING,
                dialogInput = "",
                showAddRootDialog = false,
                showAddChildDialog = false
            ),
            onAddRoot = { /*TODO*/ },
            onAddChild = { _ -> /*TODO*/ },
            onToggleCompleted = { _, _ -> /*TODO*/ },
            onDialogInputChanged = { _ -> /*TODO*/ },
            onDismissRootDialog = { /*TODO*/ },
            onDismissChildDialog = { /*TODO*/ },
            onConfirmRoot = { /*TODO*/ },
            onConfirmChild = { /*TODO*/ },
            onStartSession = { _ -> /*TODO*/ },
            onPauseSession = { /*TODO*/ },
            onResumeSession = { /*TODO*/ },
            onStopSession = { /*TODO*/ },
            runningNodeId = 4L, // Example: Hilt Implementation is running
            onNodeClick = { _ -> /*TODO*/ }
        )
    }
}