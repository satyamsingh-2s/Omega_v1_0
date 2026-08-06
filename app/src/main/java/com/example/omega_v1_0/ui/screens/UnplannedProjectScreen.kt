package com.example.omega_v1_0.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.omega_v1_0.ui.model.UnplannedProjectUiModel
import com.example.omega_v1_0.ui.theme.AccentPalette
import com.example.omega_v1_0.ui.uistate.UnplannedProjectUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnplannedProjectScreen(
    uiState: UnplannedProjectUiState,
    onAddRoot: () -> Unit,
    onAddChild: (Long) -> Unit,
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
    onToggelExpand: (Long) -> Unit = {},
    onToggleCompleted: (Long, Boolean) -> Unit,
    onNavigateToSession: (Long) -> Unit,
) {
    Scaffold(
        containerColor = DARK_BACKGROUND,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Unplanned Projects",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )
                        Text(
                            text = "Organize tasks, ideas and learning paths",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onAddRoot) {
                        Icon(Icons.Default.Add, contentDescription = "New", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New", color = Color.White, fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DARK_BACKGROUND)
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 10.dp)
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(CARD_VERTICAL_MARGIN)
        ) {
            items(uiState.tree, key = { it.nodeId }) { node ->
                UnplannedProjectRootCard(
                    node = node,
                    expandedPath = uiState.expandedPath,
                    onToggleExpand = onToggelExpand,
                    onNodeClick = onNodeClick,
                    onAddChild = onAddChild,
                    onAddExpectedDuration = onAddExpectedDuration,
                    onRename = onRename,
                    onDelete = onDelete,
                    onShowStats = onShowStats,
                    onNavigateToSession = onNavigateToSession,
                    onToggleCompleted = onToggleCompleted

                )
            }

            item {
                AddChildButton(
                    label = "Add Project",
                    onClick = onAddRoot,
                    indent = 0.dp,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { TipCard() }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }

        if (uiState.showAddRootDialog) {
            NodeInputDialog(
                title = "Add Root Node".uppercase(),
                value = uiState.dialogInput,
                onValueChange = onDialogInputChanged,
                onDismiss = onDismissRootDialog,
                onConfirm = onConfirmRoot
            )
        }

        if (uiState.showAddChildDialog) {
            val parentNode = uiState.selectedNodeId?.let { findNodeById(uiState.tree, it) }
            NodeInputDialog(
                title = "Add Child Node".uppercase(),
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
                    TextButton(onClick = onConfirmDelete) { Text("Delete", style = MaterialTheme.typography.labelLarge) }
                },
                dismissButton = {
                    TextButton(onClick = onDismissDelete) { Text("Cancel", style = MaterialTheme.typography.labelLarge) }
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
                    TextButton(onClick = onOpenSession) { Text("Open Session", style = MaterialTheme.typography.labelLarge) }
                },
                dismissButton = {
                    Row {
                        TextButton(onClick = onEndSession) { Text("End Session", style = MaterialTheme.typography.labelLarge) }
                        TextButton(onClick = onDismissSessionDialog) { Text("Cancel", style = MaterialTheme.typography.labelLarge) }
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
                            val progress = if (node.expectedDurationSeconds == 0) 0
                            else (node.currentDurationSeconds * 100 / node.expectedDurationSeconds)
                            Text("Progress: $progress%", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                if (node.isCompleted) "Status: Completed" else "Status: In Progress",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = onDismissStats) { Text("Close", style = MaterialTheme.typography.labelLarge) }
                    }
                )
            }
        }
    }
}

private val DARK_SURFACE = Color(0xFF121212)
private val DARK_BACKGROUND = Color(0xFF000000)
private val TREE_LINE_COLOR = Color(0xFF333333)

private val ROOT_CARD_INNER_PADDING = 20.dp
private val GROUP_DIVIDER_TOP = 16.dp
private val BETWEEN_CHILD_ROWS = 8.dp
private val ADD_BUTTON_TOP_PADDING = 16.dp
private val CARD_VERTICAL_MARGIN = 12.dp

private val ROOT_TITLE_SIZE = 18.sp
private val LEVEL1_TITLE_SIZE = 16.sp
private val LEVEL2_TITLE_SIZE = 15.sp

@Composable
private fun UnplannedProjectRootCard(
    node: UnplannedProjectUiModel,
    expandedPath: List<Long>,
    onToggleExpand: (Long) -> Unit,
    onNodeClick: (Long) -> Unit,
    onAddChild: (Long) -> Unit,
    onAddExpectedDuration: (Long) -> Unit,
    onRename: (Long, String) -> Unit,
    onDelete: (Long) -> Unit,
    onShowStats: (UnplannedProjectUiModel) -> Unit,
    onNavigateToSession: (Long) -> Unit,
    onToggleCompleted: (Long, Boolean) -> Unit,

) {
    val isExpanded = expandedPath.firstOrNull() == node.nodeId
    val chain = if (isExpanded) expandedPath.drop(1) else emptyList()
    var showMenu by remember { mutableStateOf(false) }
    val accentColor = AccentPalette.getAccent(node.accentIndex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { if (node.children.isNotEmpty()) onToggleExpand(node.nodeId) else onNodeClick(node.nodeId) },
                onLongClick = { showMenu = true },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true)
            ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFF222222)),
        colors = CardDefaults.cardColors(containerColor = DARK_SURFACE),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(4.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp))
                    .background(accentColor)
            )

            Column(modifier = Modifier
                .padding(ROOT_CARD_INNER_PADDING)
                .animateContentSize(animationSpec = tween(220))
            ) {
                NodeSummaryRow(
                    node = node,
                    titleFontSize = ROOT_TITLE_SIZE,
                    titleFontWeight = FontWeight.Medium,
                    isExpandable = node.children.isNotEmpty(),
                    isExpanded = isExpanded,
                    onToggleExpand = { onToggleExpand(node.nodeId) },
                    onPlay = { onNavigateToSession(node.nodeId) },
                    onToggleCompleted = onToggleCompleted
                )

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(animationSpec = tween(220)) + expandVertically(animationSpec = tween(220)),
                    exit = fadeOut(animationSpec = tween(200)) + shrinkVertically(animationSpec = tween(200))
                ) {
                    Column(modifier = Modifier.animateContentSize(animationSpec = tween(220))) {
                        if (node.children.isNotEmpty()) {
                            HorizontalDivider(
                                modifier = Modifier.padding(top = GROUP_DIVIDER_TOP, bottom = 12.dp),
                                color = accentColor.copy(alpha = 0.45f)
                            )

                            val activeChildId = chain.getOrNull(0)

                            node.children.forEachIndexed { index, child ->
                                if (child.nodeId == activeChildId) {
                                    Level1HeadingRow(
                                        node = child,
                                        accentColor=accentColor,
                                        onToggleExpand = { onToggleExpand(child.nodeId) }
                                    )

                                    val fullPathNodes = resolveFullPath(node, chain)

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(
                                            width = 1.dp,
                                            color = accentColor.copy(alpha = 0.45f)
                                        ),
                                        colors = CardDefaults.cardColors(
                                            containerColor = DARK_SURFACE
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(10.dp,10.dp,10.dp,0.1.dp)
                                                .animateContentSize()
                                        ) {

                                            val terminal = fullPathNodes.last()
                                            val isTerminalLeaf = terminal.children.isEmpty()
                                            val breadcrumbNodes =
                                                if (isTerminalLeaf) fullPathNodes.dropLast(1)
                                                else fullPathNodes
                                            val focusRows =
                                                if (isTerminalLeaf) {
                                                    breadcrumbNodes.lastOrNull()?.children
                                                        ?: child.children
                                                } else {
                                                    terminal.children
                                                }


                                            SingleBranchBreadcrumb(
                                                segments = breadcrumbNodes
                                                    .drop(1)
                                                    .map { it.nodeId to it.title },
                                                onSegmentClick = onNodeClick,
                                                modifier = Modifier.fillMaxWidth(),
                                                accentColor = accentColor
                                            )

                                            Spacer(modifier = Modifier.height(12.dp))

                                            focusRows.forEachIndexed { fIndex, fRow ->
                                                TreeRow(
                                                    node = fRow,
                                                    titleFontSize = LEVEL2_TITLE_SIZE,
                                                    onToggleExpand = onToggleExpand,
                                                    onNodeClick = onNodeClick,
                                                    onNavigateToSession = onNavigateToSession,
                                                    onAddChild = onAddChild,
                                                    onRename = onRename,
                                                    onAddExpectedDuration = onAddExpectedDuration,
                                                    onShowStats = onShowStats,
                                                    onDelete = onDelete,
                                                    showVerticalLine = true,
                                                    onToggleCompleted = onToggleCompleted
                                                )
                                                if (fIndex != focusRows.lastIndex) {
                                                    // divdier
                                                    HorizontalDivider(
                                                        modifier = Modifier.padding(vertical = 2.dp),
                                                        thickness = 0.5.dp,
                                                        color = accentColor.copy(alpha = 0.45f)
                                                    )
//                                                    Spacer(
//                                                        modifier = Modifier.height(
//                                                            BETWEEN_CHILD_ROWS
//                                                        )
//                                                    )
                                                }
                                            }

                                            AddChildButton(
                                                label = "Add Item",
                                                onClick = { onAddChild(terminal.nodeId) },
                                                indent = 0.dp,
                                            )
                                        }
                                    }
                                } else {
                                    TreeRow(
                                        node = child,
                                        titleFontSize = LEVEL1_TITLE_SIZE,
                                        onToggleExpand = onToggleExpand,
                                        onNodeClick = onNodeClick,
                                        onNavigateToSession = onNavigateToSession,
                                        onAddChild = onAddChild,
                                        onRename = onRename,
                                        onAddExpectedDuration = onAddExpectedDuration,
                                        onShowStats = onShowStats,
                                        onDelete = onDelete,
                                        onToggleCompleted = onToggleCompleted
                                    )
                                }
                                if (index != node.children.lastIndex && (child.nodeId != activeChildId)) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical=BETWEEN_CHILD_ROWS),
                                        thickness = 0.5.dp,
                                        color = accentColor.copy(alpha = 0.45f)
                                    )
                                }
                                    else{
                                     Spacer(modifier = Modifier.height(BETWEEN_CHILD_ROWS))

                                }
                            }
                        }

                        AddChildButton(
                            label = "Add Topic",
                            onClick = { onAddChild(node.nodeId) },
                            indent = 0.dp,
                            //modifier = Modifier.padding(top = ADD_BUTTON_TOP_PADDING)
                        )
                    }
                }
            }
        }
    }

    NodeOptionsBottomSheet(
        show = showMenu,
        nodeName = node.title,
        onDismiss = { showMenu = false },
        onAddChild = { onAddChild(node.nodeId) },
        onRename = { onRename(node.nodeId, node.title) },
        onSetDuration = { onAddExpectedDuration(node.nodeId) },
        onShowStats = { onShowStats(node) },
        onDelete = { onDelete(node.nodeId) }
    )
}

@Composable
private fun Level1HeadingRow(
    node: UnplannedProjectUiModel,
    accentColor: Color,
    onToggleExpand: () -> Unit
) {
    // Only container nodes can be rendered as a heading.
    if (node.children.isEmpty()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() }
            .padding( 6.dp, 8.dp,4.dp,0.2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = node.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.weight(1f)
        )
//        Icon(
//            imageVector = Icons.Default.KeyboardArrowDown,
//            contentDescription = "Collapse",
//            tint = accentColor,
//            modifier = Modifier.size(24.dp)
//        )
    }
}

private fun resolveFullPath(root: UnplannedProjectUiModel, chain: List<Long>): List<UnplannedProjectUiModel> {
    val path = mutableListOf<UnplannedProjectUiModel>()
    var current = root
    for (id in chain) {
        val next = current.children.find { it.nodeId == id }
        if (next != null) {
            path.add(next)
            current = next
        } else {
            break
        }
    }
    return path
}

@Composable
private fun TreeRow(
    node: UnplannedProjectUiModel,
    titleFontSize: TextUnit,
    titleFontWeight: FontWeight = FontWeight.Normal,
    onToggleExpand: (Long) -> Unit,
    onNodeClick: (Long) -> Unit,
    onNavigateToSession: (Long) -> Unit,
    onAddChild: (Long) -> Unit,
    onRename: (Long, String) -> Unit,
    onAddExpectedDuration: (Long) -> Unit,
    onShowStats: (UnplannedProjectUiModel) -> Unit,
    onDelete: (Long) -> Unit,
    showVerticalLine: Boolean = true,
    onToggleCompleted: (Long, Boolean) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    val accentColor = AccentPalette.getAccent(node.accentIndex)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { if (node.children.isNotEmpty()) onToggleExpand(node.nodeId) else onNodeClick(node.nodeId) },
                onLongClick = { showMenu = true },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true)
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
               // .width(10.dp)
                .height(80.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (showVerticalLine) {
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp),
                    color = TREE_LINE_COLOR
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(6.dp)
                    .background( accentColor.copy(alpha = 0.45f), CircleShape)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        NodeSummaryRow(
            node = node,
            titleFontSize = titleFontSize,
            titleFontWeight = titleFontWeight,
            isExpandable = node.children.isNotEmpty(),
            isExpanded = false,
            onToggleExpand = { onToggleExpand(node.nodeId) },
            onPlay = { onNavigateToSession(node.nodeId) },
            onToggleCompleted = onToggleCompleted
        )
    }

    NodeOptionsBottomSheet(
        show = showMenu,
        nodeName = node.title,
        onDismiss = { showMenu = false },
        onAddChild = { onAddChild(node.nodeId) },
        onRename = { onRename(node.nodeId, node.title) },
        onSetDuration = { onAddExpectedDuration(node.nodeId) },
        onShowStats = { onShowStats(node) },
        onDelete = { onDelete(node.nodeId) }
    )
}

@Composable
private fun NodeSummaryRow(
    node: UnplannedProjectUiModel,
    titleFontSize: TextUnit,
    titleFontWeight: FontWeight,
    isExpandable: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onPlay: () -> Unit,
    onToggleCompleted: (Long, Boolean) -> Unit,
) {
    val progress = if (node.expectedDurationSeconds == 0) {
        0f
    } else {
        (node.currentDurationSeconds.toFloat() / node.expectedDurationSeconds).coerceIn(0f, 1f)
    }
    val displayedProgress = if (node.children.isEmpty()) progress else node.completionProgress
    val displayedPercent = if (node.children.isEmpty()) (progress * 100).toInt() else (node.completionProgress * 100).toInt()

    val accentColor = AccentPalette.getAccent(node.accentIndex)

    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth())
    {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = node.title,
                fontSize = titleFontSize,
                fontWeight = titleFontWeight,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Est. ${formatShortDuration(node.expectedDurationSeconds)}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { displayedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.5.dp)
                    .clip(CircleShape),
                color = accentColor,
                trackColor = Color(0xFF222222),
                strokeCap = StrokeCap.Round
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.height(80.dp)
        ) {
            if (isExpandable) {
                IconButton(onClick = onToggleExpand, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = Color.Gray
                    )
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
//                    Checkbox(
//                        checked = node.isCompleted,
//                        onCheckedChange = {
//                            onToggleCompleted(node.nodeId, it)
//                        }
//                    )

                    IconButton(
                        onClick = onPlay
                    ) {
                        Icon(
                            Icons.Filled.PlayArrow,
                            contentDescription = "Start Session",
                            tint = Color.Gray
                        )
                    }
                }
            }

            Text(
                text = "$displayedPercent%",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 3.dp)
            )
        }
    }
}

@Composable
private fun AddChildButton(label: String, onClick: () -> Unit, indent: Dp, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 80),
        label = "add_button_scale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = indent),
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
            interactionSource = interactionSource,
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NodeOptionsBottomSheet(
    show: Boolean,
    nodeName: String,
    onDismiss: () -> Unit,
    onAddChild: () -> Unit,
    onRename: () -> Unit,
    onSetDuration: () -> Unit,
    onShowStats: () -> Unit,
    onDelete: () -> Unit
) {
    if (!show) return

    val sheetState = androidx.compose.material3.rememberModalBottomSheetState()

    androidx.compose.material3.ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
            Text(
                text = nodeName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            BottomSheetOption(icon = Icons.Default.Add, label = "Add Child", onClick = { onAddChild(); onDismiss() })
            BottomSheetOption(icon = Icons.Default.Edit, label = "Rename", onClick = { onRename(); onDismiss() })
            BottomSheetOption(icon = Icons.Default.Timer, label = "Estimated Minutes", onClick = { onSetDuration(); onDismiss() })
            BottomSheetOption(icon = Icons.Outlined.BarChart, label = "Show Statistics", onClick = { onShowStats(); onDismiss() })
            BottomSheetOption(
                icon = Icons.Default.Delete,
                label = "Delete",
                onClick = { onDelete(); onDismiss() },
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun BottomSheetOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint ?: MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = tint ?: MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SingleBranchBreadcrumb(
    segments: List<Pair<Long, String>>,
    onSegmentClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color,
) {
    if (segments.isEmpty()) return

    val scrollState = rememberScrollState()

    LaunchedEffect(segments) {
        scrollState.animateScrollTo(
            value = scrollState.maxValue,
            animationSpec = tween(
                durationMillis = 250,
                easing = FastOutSlowInEasing
            )
        )
    }


    Row(
        modifier = modifier
            .border(1.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        segments.forEachIndexed { index, (id, title) ->
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = accentColor,
                maxLines = 1,
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = false, radius = 16.dp),
                        onClick = { onSegmentClick(id) }
                    )
            )
            if (index != segments.lastIndex) {
                Text(
                    text = "  >  ",
                    style = MaterialTheme.typography.bodySmall,
                    color = accentColor.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }
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
        confirmButton = { TextButton(onClick = onConfirm) { Text("Add", style = MaterialTheme.typography.labelLarge) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", style = MaterialTheme.typography.labelLarge) } }
    )
}

@Composable
fun TipCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
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
    if (minutes < 60) return "${minutes}m"
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
                        title = "UI",
                        accentIndex = 0,
                        currentDurationSeconds = 2710,
                        expectedDurationSeconds = 3600,
                        isCompleted = true,
                        children = emptyList()
                    ),
                    UnplannedProjectUiModel(
                        nodeId = 3L,
                        title = "Engine",
                        accentIndex = 0,
                        currentDurationSeconds = 1530,
                        expectedDurationSeconds = 1800,
                        isCompleted = false,
                        children = listOf(
                            UnplannedProjectUiModel(
                                nodeId = 8L,
                                title = "Database",
                                accentIndex = 0,
                                currentDurationSeconds = 400,
                                expectedDurationSeconds = 600,
                                isCompleted = false,
                                children = emptyList()
                            ),
                            UnplannedProjectUiModel(
                                nodeId = 4L,
                                title = "Layout",
                                accentIndex = 0,
                                currentDurationSeconds = 610,
                                expectedDurationSeconds = 900,
                                isCompleted = false,
                                children = listOf(
                                    UnplannedProjectUiModel(
                                        nodeId = 9L,
                                        title = "Canvas",
                                        accentIndex = 0,
                                        currentDurationSeconds = 200,
                                        expectedDurationSeconds = 400,
                                        isCompleted = false,
                                        children = listOf(
                                            UnplannedProjectUiModel(
                                                nodeId = 20L,
                                                title = "Renderer",
                                                accentIndex = 0,
                                                currentDurationSeconds = 50,
                                                expectedDurationSeconds = 200,
                                                isCompleted = false,
                                                children = listOf(
                                                    UnplannedProjectUiModel(
                                                        nodeId = 21L,
                                                        title = "GPU Pipeline",
                                                        accentIndex = 0,
                                                        currentDurationSeconds = 20,
                                                        expectedDurationSeconds = 150,
                                                        isCompleted = false,
                                                        children = listOf(
                                                            UnplannedProjectUiModel(
                                                                nodeId = 22L,
                                                                title = "Shaders",
                                                                accentIndex = 0,
                                                                currentDurationSeconds = 10,
                                                                expectedDurationSeconds = 100,
                                                                isCompleted = false,
                                                                children = listOf(
                                                                    UnplannedProjectUiModel(
                                                                        nodeId = 23L,
                                                                        title = "Fragment Shader Bug",
                                                                        accentIndex = 0,
                                                                        currentDurationSeconds = 0,
                                                                        expectedDurationSeconds = 30,
                                                                        isCompleted = false,
                                                                        children = emptyList()
                                                                    ),
                                                                    UnplannedProjectUiModel(
                                                                        nodeId = 24L,
                                                                        title = "Vertex Shader Bug",
                                                                        accentIndex = 0,
                                                                        currentDurationSeconds = 0,
                                                                        expectedDurationSeconds = 30,
                                                                        isCompleted = false,
                                                                        children = emptyList()
                                                                    )
                                                                )
                                                            )
                                                        )
                                                    )
                                                )
                                            )
                                        )
                                    ),
                                    UnplannedProjectUiModel(
                                        nodeId = 10L,
                                        title = "Export",
                                        accentIndex = 0,
                                        currentDurationSeconds = 0,
                                        expectedDurationSeconds = 300,
                                        isCompleted = false,
                                        children = emptyList()
                                    )
                                )
                            ),
                            UnplannedProjectUiModel(
                                nodeId = 11L,
                                title = "Pomodoro",
                                accentIndex = 0,
                                currentDurationSeconds = 0,
                                expectedDurationSeconds = 500,
                                isCompleted = false,
                                children = emptyList()
                            )
                        )
                    ),
                    UnplannedProjectUiModel(
                        nodeId = 12L,
                        title = "Testing",
                        accentIndex = 0,
                        currentDurationSeconds = 0,
                        expectedDurationSeconds = 600,
                        isCompleted = false,
                        children = emptyList()
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
                showAddChildDialog = false,
                expandedPath = listOf(1L, 3L, 4L, 9L, 20L, 21L, 22L, 23L)
            ),
            onAddRoot = {},
            onAddChild = {},
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
            onToggelExpand = {},
            onNavigateToSession = {},
            onToggleCompleted = {_, _ -> }
        )
    }
}
