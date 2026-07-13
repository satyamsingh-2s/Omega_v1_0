package com.example.omega_v1_0.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega_v1_0.ui.model.UnplannedProjectUiModel
import com.example.omega_v1_0.ui.theme.AccentPalette
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
    onOpenSession: () -> Unit,
    onEndSession: () -> Unit,
    onDismissSessionDialog: () -> Unit,
    onNodeClick: (Long) -> Unit,
    onAddExpectedDuration: (Long) -> Unit,
    onExpectedDurationChanged: (String) -> Unit,
    onDismissExpectedDuration: () -> Unit,
    onConfirmExpectedDuration: () -> Unit,
    onRename: (Long, String) -> Unit,
    onRenameChanged: (String) -> Unit,
    onDismissRename: () -> Unit,
    onConfirmRename: () -> Unit,
    onDelete: (Long) -> Unit,
    onDismissDelete: () -> Unit,
    onConfirmDelete: () -> Unit,
    onShowStats: (UnplannedProjectUiModel) -> Unit,
    onDismissStats: () -> Unit,
    expandedNodeIds: Set<Long> = emptySet(),
    onToggelExpand: (Long) -> Unit = {},
    onNavigateToSession: (Long) -> Unit,
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
                            text = "Organize tasks, ideas and learning paths",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = onAddRoot,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "New", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New", fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(uiState.tree) { node ->
                UnplannedProjectNodeItem(
                    node = node,
                    depth = 0,
                    onAddChild = onAddChild,
                    onToggleCompleted = onToggleCompleted,
                    onNodeClick = onNodeClick,
                    onAddExpectedDuration = onAddExpectedDuration,
                    onRename = onRename,
                    onDelete = onDelete,
                    onShowStats = onShowStats,
                    expandedNodeIds = expandedNodeIds,
                    onToggleExpand = onToggelExpand,
                    onNavigateToSession = onNavigateToSession
                )
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
            val parentNode = uiState.selectedNodeId?.let { findNodeById(uiState.tree, it) }
            NodeInputDialog(
                title = "Add Child Node".uppercase(Locale.getDefault()),
                nodeName = parentNode?.title,
                value = uiState.dialogInput,
                onValueChange = onDialogInputChanged,
                onDismiss = onDismissChildDialog,
                onConfirm = onConfirmChild
            )
        }

        if (uiState.showExpectedDurationDialog) {
            val targetNode = uiState.selectedExpectedDurationNodeId?.let { findNodeById(uiState.tree, it) }
            NodeInputDialog(
                title = "Expected Duration (min)",
                nodeName = targetNode?.title,
                value = uiState.expectedDurationInput,
                onValueChange = onExpectedDurationChanged,
                onDismiss = onDismissExpectedDuration,
                onConfirm = onConfirmExpectedDuration
            )
        }

        if (uiState.showRenameDialog) {
            val targetNode = uiState.selectedRenameNodeId?.let { findNodeById(uiState.tree, it) }
            NodeInputDialog(
                title = "Rename Node",
                nodeName = targetNode?.title,
                value = uiState.renameInput,
                onValueChange = onRenameChanged,
                onDismiss = onDismissRename,
                onConfirm = onConfirmRename
            )
        }

        if (uiState.showDeleteDialog) {
            val targetNode = uiState.selectedDeleteNodeId?.let { findNodeById(uiState.tree, it) }
            AlertDialog(
                onDismissRequest = onDismissDelete,
                shape = RoundedCornerShape(16.dp),
                title = {
                    Column {
                        Text("Delete Node", style = MaterialTheme.typography.titleSmall)
                        if (targetNode != null) {
                            Text(
                                text = targetNode.title,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                },
                text = { Text("Delete this node and all its children?", style = MaterialTheme.typography.bodyMedium) },
                confirmButton = {
                    TextButton(onClick = onConfirmDelete) {
                        Text("Delete", style = MaterialTheme.typography.labelLarge)
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissDelete) {
                        Text("Cancel", style = MaterialTheme.typography.labelLarge)
                    }
                }
            )
        }

        if (uiState.showSessionAlreadyRunningDialog) {
            AlertDialog(
                onDismissRequest = onDismissSessionDialog,
                shape = RoundedCornerShape(16.dp),
                title = { Text("Session Running", style = MaterialTheme.typography.titleSmall) },
                text = { Text("A session is already running.", style = MaterialTheme.typography.bodyMedium) },
                confirmButton = {
                    TextButton(onClick = onOpenSession) {
                        Text("Open Session", style = MaterialTheme.typography.labelLarge)
                    }
                },
                dismissButton = {
                    Row {
                        TextButton(onClick = onEndSession) {
                            Text("End Session", style = MaterialTheme.typography.labelLarge)
                        }
                        TextButton(onClick = onDismissSessionDialog) {
                            Text("Cancel", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            )
        }

        if (uiState.showStatsDialog) {
            val node = uiState.selectedStatsNode
            if (node != null) {
                AlertDialog(
                    onDismissRequest = onDismissStats,
                    shape = RoundedCornerShape(16.dp),
                    title = {
                        Column {
                            Text("Node Stats", style = MaterialTheme.typography.titleSmall)
                            Text(
                                text = node.title,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Current: ${formatShortDuration(node.currentDurationSeconds)}", style = MaterialTheme.typography.bodyMedium)
                            Text("Expected: ${formatShortDuration(node.expectedDurationSeconds)}", style = MaterialTheme.typography.bodyMedium)
                            val progress = if (node.expectedDurationSeconds == 0) {
                                0
                            } else {
                                (node.currentDurationSeconds * 100 / node.expectedDurationSeconds)
                            }
                            Text("Progress: $progress%", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                if (node.isCompleted) "Status: Completed" else "Status: In Progress",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = onDismissStats) {
                            Text("Close", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                )
            }
        }
    }
}

// Helper to calculate indentation per depth
private fun getIndentation(depth: Int): Int {
    return when (depth) {
        0 -> 0
        1 -> 16
        2 -> 28
        3 -> 36
        else -> 40
    }
}

@Composable
fun UnplannedProjectNodeItem(
    node: UnplannedProjectUiModel,
    depth: Int,
    onAddChild: (Long) -> Unit,
    onToggleCompleted: (Long, Boolean) -> Unit,
    onNodeClick: (Long) -> Unit,
    onAddExpectedDuration: (Long) -> Unit,
    onRename: (Long, String) -> Unit,
    onDelete: (Long) -> Unit,
    onShowStats: (UnplannedProjectUiModel) -> Unit,
    expandedNodeIds: Set<Long>,
    onToggleExpand: (Long) -> Unit = {},
    onNavigateToSession: (Long) -> Unit
) {
    val isExpanded = expandedNodeIds.contains(node.nodeId)
    Log.d("EXPAND_UI", "node=${node.nodeId} expanded=$isExpanded set=$expandedNodeIds")

    var showDropdownMenu by remember { mutableStateOf(false) }

    val indent = getIndentation(depth).dp
    val progress = if (node.expectedDurationSeconds == 0) {
        0f
    } else {
        (node.currentDurationSeconds.toFloat() / node.expectedDurationSeconds).coerceAtMost(1f)
    }
    val accentColor =
        AccentPalette.getAccent(node.accentIndex)

    if (depth == 0) {
        // Root project as a card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 28.dp)
                .combinedClickable(
                    onClick = {
                        if (node.children.isNotEmpty()) {
                            onToggleExpand(node.nodeId)
                        } else {
                            onNodeClick(node.nodeId)
                        }
                    },
                    onLongClick = { showDropdownMenu = true }
                ),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {

                // -------- accent color
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.4.dp)
                        .background(accentColor)
                )


                // Root content
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = node.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (node.expectedDurationSeconds > 0) {
                                Text(
                                    text = "Estimated: ${formatShortDuration(node.expectedDurationSeconds)}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    LinearProgressIndicator(
                                        progress = progress,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(3.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                            alpha = 0.5f
                                        )
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${(progress * 100).toInt()}%",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            if (node.children.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                IconButton(
                                    onClick = { onToggleExpand(node.nodeId) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.rotate(if (isExpanded) 0f else -90f)
                                    )
                                }
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (node.children.isEmpty()) {
                                NodeCompletionToggle(
                                    isCompleted = node.isCompleted,
                                    onToggle = { onToggleCompleted(node.nodeId, node.isCompleted) }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { onNavigateToSession(node.nodeId) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }

                // Root expanded content
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(animationSpec = tween(200)) + expandVertically(
                        animationSpec = tween(
                            200
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(200)) + shrinkVertically(
                        animationSpec = tween(
                            200
                        )
                    )
                ) {
                    Column {
                        if (node.children.isNotEmpty()) {
                            // Thicker divider parent → child
                            Divider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                thickness = 1.2.dp
                            )

                            // Section header
                            Text(
                                text = "${node.children.size} Children",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                            )
                        }

                        node.children.forEachIndexed { index, child ->
                            UnplannedProjectNodeItem(
                                node = child,
                                depth = depth + 1,
                                onAddChild = onAddChild,
                                onToggleCompleted = onToggleCompleted,
                                onNodeClick = onNodeClick,
                                onAddExpectedDuration = onAddExpectedDuration,
                                onRename = onRename,
                                onDelete = onDelete,
                                onShowStats = onShowStats,
                                expandedNodeIds = expandedNodeIds,
                                onToggleExpand = onToggleExpand,
                                onNavigateToSession = onNavigateToSession
                            )
                            if (index != node.children.lastIndex) {
                                Divider(
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                    thickness = 0.5.dp,
                                    modifier = Modifier.padding(start = 36.dp)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 36.dp, top = 8.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            TextButton(
                                onClick = { onAddChild(node.nodeId) },
                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Add", fontSize = 12.sp)
                            }
                        }

                        // Thick divider after add button for root
                        Divider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            thickness = 1.2.dp,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }
                }
                // -------- accent color
                if (node.expectedDurationSeconds == 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.9.dp)
                            .background(accentColor)
                    )

                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.5.dp)
                            .background(accentColor)
                    )
                }
            }
        }
    } else {
        // Non-root as row
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        if (node.children.isNotEmpty()) {
                            onToggleExpand(node.nodeId)
                        } else {
                            onNodeClick(node.nodeId)
                        }
                    },
                    onLongClick = { showDropdownMenu = true }
                )
        ) {
            // Tree connectors
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .padding(start = indent)
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                )
                Box(
                    modifier = Modifier
                        .padding(start = indent, top = 16.dp)
                        .width(12.dp)
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                )

                // Row content
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = node.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (node.expectedDurationSeconds > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Estimated: ${formatShortDuration(node.expectedDurationSeconds)}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                LinearProgressIndicator(
                                    progress = progress,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(2.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (node.children.isNotEmpty()) {
                            IconButton(
                                onClick = { onToggleExpand(node.nodeId) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.rotate(if (isExpanded) 0f else -90f)
                                )
                            }
                        }

                        if (node.children.isEmpty()) {
                            NodeCompletionToggle(
                                isCompleted = node.isCompleted,
                                onToggle = { onToggleCompleted(node.nodeId, node.isCompleted) }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(
                                onClick = { onNavigateToSession(node.nodeId) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Expanded content for non-root
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(animationSpec = tween(200)) + expandVertically(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200)) + shrinkVertically(animationSpec = tween(200))
            ) {
                Column {
                    if (node.children.isNotEmpty()) {
                        // Thicker divider parent → child
                        Divider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            thickness = 1.2.dp,
                            modifier = Modifier.padding(start = indent + 12.dp)
                        )

                        // Section header
                        Text(
                            text = "${node.children.size} Children",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = indent + 12.dp + 12.dp, top = 8.dp, bottom = 8.dp)
                        )
                    }

                    node.children.forEachIndexed { index, child ->
                        UnplannedProjectNodeItem(
                            node = child,
                            depth = depth + 1,
                            onAddChild = onAddChild,
                            onToggleCompleted = onToggleCompleted,
                            onNodeClick = onNodeClick,
                            onAddExpectedDuration = onAddExpectedDuration,
                            onRename = onRename,
                            onDelete = onDelete,
                            onShowStats = onShowStats,
                            expandedNodeIds = expandedNodeIds,
                            onToggleExpand = onToggleExpand,
                            onNavigateToSession = onNavigateToSession
                        )
                        if (index != node.children.lastIndex) {
                            Divider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                thickness = 0.5.dp,
                                modifier = Modifier.padding(start = (getIndentation(depth + 1) + 12).dp)
                            )
                        }
                    }

                    // Add button row with vertical connector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(start = (getIndentation(depth)).dp)
                                .width(1.dp)
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                        )
                        Box(
                            modifier = Modifier
                                .padding(start = (getIndentation(depth)).dp, top = 0.dp)
                                .width(12.dp)
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                        )
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 12.dp, top = 8.dp, bottom = 12.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            TextButton(
                                onClick = { onAddChild(node.nodeId) },
                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Add", fontSize = 12.sp)
                            }
                        }
                    }

                    // Thick divider after add button
                    if (depth > 0) {
                        Divider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            thickness = 1.2.dp,
                            modifier = Modifier.padding(start = (getIndentation(depth) + 12).dp)
                        )
                    }
                }
            }
        }
    }

    // Dropdown menu
    DropdownMenu(
        expanded = showDropdownMenu,
        onDismissRequest = { showDropdownMenu = false },
        shape = RoundedCornerShape(16.dp)
    ) {
        DropdownMenuItem(
            text = { Text("Add Child", style = MaterialTheme.typography.bodyMedium) },
            leadingIcon = { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp)) },
            onClick = {
                onAddChild(node.nodeId)
                showDropdownMenu = false
            }
        )
        DropdownMenuItem(
            text = { Text("Rename", style = MaterialTheme.typography.bodyMedium) },
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp)) },
            onClick = {
                onRename(node.nodeId, node.title)
                showDropdownMenu = false
            }
        )
        DropdownMenuItem(
            text = { Text("Estimated Minutes", style = MaterialTheme.typography.bodyMedium) },
            leadingIcon = { Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(20.dp)) },
            onClick = {
                onAddExpectedDuration(node.nodeId)
                showDropdownMenu = false
            }
        )
        DropdownMenuItem(
            text = { Text("Show Statistics", style = MaterialTheme.typography.bodyMedium) },
            leadingIcon = { Icon(Icons.Outlined.BarChart, contentDescription = null, modifier = Modifier.size(20.dp)) },
            onClick = {
                onShowStats(node)
                showDropdownMenu = false
            }
        )
        DropdownMenuItem(
            text = { Text("Delete", style = MaterialTheme.typography.bodyMedium) },
            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(20.dp)) },
            onClick = {
                onDelete(node.nodeId)
                showDropdownMenu = false
            }
        )
    }
}

@Composable
fun NodeCompletionToggle(
    isCompleted: Boolean,
    onToggle: () -> Unit
) {
    IconButton(
        onClick = onToggle,
        modifier = Modifier.size(28.dp)
    ) {
        if (isCompleted) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Completed",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(20.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun NodeInputDialog(
    title: String,
    nodeName: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),
        title = {
            Column {
                Text(text = title, style = MaterialTheme.typography.titleSmall)
                if (nodeName != null) {
                    Text(
                        text = nodeName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Add", style = MaterialTheme.typography.labelLarge)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", style = MaterialTheme.typography.labelLarge)
            }
        }
    )
}

@Composable
fun TipCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
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

private fun formatShortDuration(seconds: Int): String {
    val minutes = seconds / 60
    if (minutes < 60) {
        return "${minutes}m"
    }
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return "${hours}h ${remainingMinutes}m"
}

private fun findNodeById(tree: List<UnplannedProjectUiModel>, nodeId: Long): UnplannedProjectUiModel? {
    for (node in tree) {
        if (node.nodeId == nodeId) return node
        val foundInChildren = findNodeById(node.children, nodeId)
        if (foundInChildren != null) return foundInChildren
    }
    return null
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UnplannedProjectScreenPreview() {
    MaterialTheme {

        val sampleTree = listOf(

            UnplannedProjectUiModel(
                nodeId = 1L,
                title = "Android Architecture",
                accentIndex = 0,
                currentDurationSeconds = 5720,
                expectedDurationSeconds = 7200,
                isCompleted = false,
                children = listOf(

                    UnplannedProjectUiModel(
                        nodeId = 2L,
                        title = "Learn MVVM",
                        accentIndex = 0,
                        currentDurationSeconds = 2710,
                        expectedDurationSeconds = 3600,
                        isCompleted = true,
                        children = emptyList()
                    ),

                    UnplannedProjectUiModel(
                        nodeId = 3L,
                        title = "Dependency Injection",
                        accentIndex = 0,
                        currentDurationSeconds = 1530,
                        expectedDurationSeconds = 1800,
                        isCompleted = false,
                        children = listOf(

                            UnplannedProjectUiModel(
                                nodeId = 4L,
                                title = "Hilt Implementation",
                                accentIndex = 0,
                                currentDurationSeconds = 610,
                                expectedDurationSeconds = 900,
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
                accentIndex = 3,
                currentDurationSeconds = 1215,
                expectedDurationSeconds = 3600,
                isCompleted = false,
                children = emptyList()
            ),

            UnplannedProjectUiModel(
                nodeId = 6L,
                title = "Reading List",
                accentIndex = 6,
                currentDurationSeconds = 915,
                expectedDurationSeconds = 1800,
                isCompleted = true,
                children = emptyList()
            ),

            UnplannedProjectUiModel(
                nodeId = 7L,
                title = "Workout Plan",
                accentIndex = 8,
                currentDurationSeconds = 0,
                expectedDurationSeconds = 2700,
                isCompleted = false,
                children = emptyList()
            )
        )

        UnplannedProjectScreen(
            uiState = UnplannedProjectUiState(
                tree = sampleTree,
                dialogInput = "",
                showAddRootDialog = false,
                showAddChildDialog = false
            ),
            onAddRoot = {},
            onAddChild = {},
            onToggleCompleted = { _, _ -> },
            onDialogInputChanged = {},
            onDismissRootDialog = {},
            onDismissChildDialog = {},
            onConfirmRoot = {},
            onConfirmChild = {},
            onOpenSession = {},
            onEndSession = {},
            onDismissSessionDialog = {},
            onNodeClick = {},
            onExpectedDurationChanged = {},
            onDismissExpectedDuration = {},
            onConfirmExpectedDuration = {},
            onAddExpectedDuration = {},
            onRename = { _, _ -> },
            onRenameChanged = {},
            onDismissRename = {},
            onConfirmRename = {},
            onDelete = {},
            onDismissDelete = {},
            onConfirmDelete = {},
            onShowStats = {},
            onDismissStats = {},
            expandedNodeIds = setOf(1L, 3L),
            onToggelExpand = {},
            onNavigateToSession = {}
        )
    }
}

// Screen Version 4
