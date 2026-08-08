package com.example.omega_v1_0.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ================================================================
// SMART FOCUS HELPERS
// ================================================================

private fun isLevel2Node(tree: List<UnplannedProjectUiModel>, nodeId: Long): Boolean {
    return tree.any { root -> root.children.any { level2node -> level2node.nodeId == nodeId } }
}

private fun getExpandedLevel2NodeId(
    tree: List<UnplannedProjectUiModel>,
    expandedPath: List<Long>
): Long? {
    if (expandedPath.size < 2) return null
    val candidateId = expandedPath[1]
    return if (isLevel2Node(tree, candidateId)) candidateId else null
}

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
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val density = LocalDensity.current.density
    var animatedLeafNodeId by remember { mutableStateOf<Long?>(null) }

    // Smart Focus Orchestrator
    val handleContainerClick: (Long) -> Unit = { clickedNodeId ->
        val currentlyExpandedLevel2Id = getExpandedLevel2NodeId(uiState.tree, uiState.expandedPath)
        val isClickedLevel2 = isLevel2Node(uiState.tree, clickedNodeId)

        if (isClickedLevel2 && currentlyExpandedLevel2Id != null && currentlyExpandedLevel2Id != clickedNodeId) {
            scope.launch {
                onToggelExpand(currentlyExpandedLevel2Id)
                delay(10)
                onToggelExpand(clickedNodeId)
                delay(50)

                val rootIndex = uiState.tree.indexOfFirst { root ->
                    root.children.any { it.nodeId == clickedNodeId }
                }.coerceAtLeast(0)

                val rootNode = uiState.tree.getOrNull(rootIndex)
                val siblingIndex = rootNode?.children?.indexOfFirst { it.nodeId == clickedNodeId } ?: 0

                val offsetWithinCardDp = 135f + (siblingIndex * 104.5f)
                val offsetWithinCardPx = (offsetWithinCardDp * density).toInt()

                val viewportHeight = listState.layoutInfo.viewportSize.height
                val targetCenterPx = (viewportHeight * 0.45f).toInt()
                val targetScrollOffset = targetCenterPx - offsetWithinCardPx

                listState.animateScrollToItem(
                    index = rootIndex,
                    scrollOffset = targetScrollOffset
                )
            }
        } else {
            onToggelExpand(clickedNodeId)
        }
    }

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
            state = listState,
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
                    onToggleExpand = handleContainerClick,
                    onNodeClick = onNodeClick,
                    onAddChild = onAddChild,
                    onAddExpectedDuration = onAddExpectedDuration,
                    onRename = onRename,
                    onDelete = onDelete,
                    onShowStats = onShowStats,
                    onNavigateToSession = onNavigateToSession,
                    onToggleCompleted = onToggleCompleted,
                    animatedLeafNodeId = animatedLeafNodeId,
                    onLeafClicked = { nodeId ->
                        animatedLeafNodeId = nodeId
                        scope.launch {
                            delay(150)
                            animatedLeafNodeId = null
                        }
                    }
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
                title = "ADD ROOT NODE",
                value = uiState.dialogInput,
                onValueChange = onDialogInputChanged,
                onDismiss = onDismissRootDialog,
                onConfirm = onConfirmRoot
            )
        }

        if (uiState.showAddChildDialog) {
            val parentNode = uiState.selectedNodeId?.let { findNodeById(uiState.tree, it) }
            NodeInputDialog(
                title = "ADD CHILD NODE",
                nodeName = parentNode?.title,
                value = uiState.dialogInput,
                onValueChange = onDialogInputChanged,
                onDismiss = onDismissChildDialog,
                onConfirm = onConfirmChild
            )
        }

        if (uiState.showExpectedDurationDialog) {
            val targetNode = uiState.selectedExpectedDurationNodeId?.let { findNodeById(uiState.tree, it) }
            ExpectedDurationDialog(
                nodeName = targetNode?.title,
                initialInput = uiState.expectedDurationInput,
                onValueChange = onExpectedDurationChanged,
                onDismiss = onDismissExpectedDuration,
                onConfirm = onConfirmExpectedDuration
            )
        }

        if (uiState.showRenameDialog) {
            val targetNode = uiState.selectedRenameNodeId?.let { findNodeById(uiState.tree, it) }
            NodeInputDialog(
                title = "RENAME NODE",
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
                containerColor = DARK_DIALOG_SURFACE,
                shape = RoundedCornerShape(20.dp),
                title = {
                    Column {
                        Text("Delete Node", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                        if (targetNode != null) {
                            Text(
                                text = targetNode.title,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFDDDDDD),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                },
                text = { Text("Delete this node and all its children? This action cannot be undone.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFAAAAAA)) },
                confirmButton = {
                    Button(
                        onClick = onConfirmDelete,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Delete", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissDelete) { Text("Cancel", style = MaterialTheme.typography.labelLarge, color = Color.Gray) }
                }
            )
        }

        if (uiState.showSessionAlreadyRunningDialog) {
            AlertDialog(
                onDismissRequest = onDismissSessionDialog,
                containerColor = DARK_DIALOG_SURFACE,
                shape = RoundedCornerShape(20.dp),
                title = { Text("Session Already Running", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White) },
                text = { Text("A timer session is currently active. End the active session before starting a new one.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFCCCCCC)) },
                confirmButton = {
                    Button(
                        onClick = onEndSession,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("End Session", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissSessionDialog) {
                        Text("Cancel", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
                    }
                }
            )
        }

        if (uiState.showStatsDialog) {
            val node = uiState.selectedStatsNode
            if (node != null) {
                AlertDialog(
                    onDismissRequest = onDismissStats,
                    containerColor = DARK_DIALOG_SURFACE,
                    shape = RoundedCornerShape(20.dp),
                    title = {
                        Column {
                            Text("Node Statistics", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
                            Text(
                                text = node.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Current Duration:", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                                Text(formatShortDuration(node.currentDurationSeconds), style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Medium)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Expected Duration:", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                                Text(formatShortDuration(node.expectedDurationSeconds), style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Medium)
                            }
                            val progress = if (node.expectedDurationSeconds == 0) 0
                            else (node.currentDurationSeconds * 100 / node.expectedDurationSeconds)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Progress:", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                                Text("$progress%", style = MaterialTheme.typography.bodyMedium, color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Status:", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                                Text(if (node.isCompleted) "Completed" else "In Progress", style = MaterialTheme.typography.bodyMedium, color = if (node.isCompleted) Color.White else Color(0xFFCCCCCC), fontWeight = FontWeight.Medium)
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = onDismissStats) { Text("Close", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                )
            }
        }
    }
}

private val DARK_SURFACE = Color(0xFF121212)
private val DARK_DIALOG_SURFACE = Color(0xFF1E1E1E)
private val DARK_INPUT_BACKGROUND = Color(0xFF282828)
private val DARK_BACKGROUND = Color(0xFF000000)
private val TREE_LINE_COLOR = Color(0xFF333333)

private val ROOT_CARD_INNER_PADDING = 20.dp
private val GROUP_DIVIDER_TOP = 16.dp
private val BETWEEN_CHILD_ROWS = 8.dp
private val CARD_VERTICAL_MARGIN = 12.dp

private val ROOT_PROJECT_TITLE_SIZE = 18.sp
private val WORKSPACE_HEADING_TITLE_SIZE = 20.sp
private val BREADCRUMB_ROW_TITLE_SIZE = 16.sp

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
    animatedLeafNodeId: Long?,
    onLeafClicked: (Long) -> Unit,
) {
    val isExpanded = expandedPath.firstOrNull() == node.nodeId
    val activeBranchPath = if (isExpanded) expandedPath.drop(1) else emptyList()
    var showMenu by remember { mutableStateOf(false) }
    val accentColor = AccentPalette.getAccent(node.accentIndex)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { if (node.children.isNotEmpty()) onToggleExpand(node.nodeId) else onLeafClicked(node.nodeId) },
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
                    titleFontSize = ROOT_PROJECT_TITLE_SIZE,
                    titleFontWeight = FontWeight.Medium,
                    isExpandable = node.children.isNotEmpty(),
                    isExpanded = isExpanded,
                    onToggleExpand = { onToggleExpand(node.nodeId) },
                    onPlay = { onNavigateToSession(node.nodeId) },
                    onToggleCompleted = onToggleCompleted,
                    animatedLeafNodeId = animatedLeafNodeId
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

                            val activeChildId = activeBranchPath.getOrNull(0)

                            node.children.forEachIndexed { index, level2node ->
                                if (level2node.nodeId == activeChildId) {
                                    WorkspaceHeadingRow(
                                        node = level2node,
                                        onToggleExpand = { onToggleExpand(level2node.nodeId) }
                                    )

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
                                                .padding(10.dp, 10.dp, 10.dp, 0.1.dp)
                                                .animateContentSize()
                                        ) {
                                            // ==========================================
                                            // NEW IMPLEMENTATION: Single canonical path
                                            // ==========================================
                                            val subPathIds = activeBranchPath.drop(1)
                                            val fullPathNodes = mutableListOf<UnplannedProjectUiModel>()

                                            var currentNode = level2node
                                            for (id in subPathIds) {
                                                val nextNode = currentNode.children.find { it.nodeId == id }
                                                if (nextNode != null) {
                                                    fullPathNodes.add(nextNode)
                                                    currentNode = nextNode
                                                } else {
                                                    break
                                                }
                                            }

                                            if (fullPathNodes.isEmpty()) {
                                                // Strictly at Level 2, no deeper expansion
                                                WorkspaceChildRowList(
                                                    rows = level2node.children,
                                                    accentColor = accentColor,
                                                    onToggleExpand = onToggleExpand,
                                                    onNodeClick = onNodeClick,
                                                    onNavigateToSession = onNavigateToSession,
                                                    onAddChild = onAddChild,
                                                    onRename = onRename,
                                                    onAddExpectedDuration = onAddExpectedDuration,
                                                    onShowStats = onShowStats,
                                                    onDelete = onDelete,
                                                    onToggleCompleted = onToggleCompleted,
                                                    animatedLeafNodeId = animatedLeafNodeId,
                                                    onLeafClicked = onLeafClicked
                                                )
                                                AddChildButton(
                                                    label = "Add Item",
                                                    onClick = { onAddChild(level2node.nodeId) },
                                                    indent = 0.dp
                                                )
                                            } else {
                                                // Level 3 or deeper
                                                val terminal = fullPathNodes.last()
                                                val isTerminalLeaf = terminal.children.isEmpty()
                                                val terminalParent = if (fullPathNodes.size >= 2) {
                                                    fullPathNodes[fullPathNodes.size - 2]
                                                } else {
                                                    level2node
                                                }

                                                val breadcrumbNodes = if (isTerminalLeaf) {
                                                    fullPathNodes.dropLast(1)
                                                } else {
                                                    fullPathNodes
                                                }

                                                val focusRows = if (isTerminalLeaf) {
                                                    terminalParent.children
                                                } else {
                                                    terminal.children
                                                }

                                                if (breadcrumbNodes.isNotEmpty()) {
                                                    BreadcrumbTrail(
                                                        segments = breadcrumbNodes.map { it.nodeId to it.title },
                                                        onSegmentClick = onNodeClick,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        accentColor = accentColor
                                                    )
                                                    Spacer(modifier = Modifier.height(12.dp))
                                                }

                                                focusRows.forEachIndexed { fIndex, fRow ->
                                                    val isFocusedLeaf = isTerminalLeaf && fRow.nodeId == terminal.nodeId
                                                    Column {
                                                        TreeRow(
                                                            node = fRow,
                                                            titleFontSize = BREADCRUMB_ROW_TITLE_SIZE,
                                                            onToggleExpand = onToggleExpand,
                                                            onNodeClick = onNodeClick,
                                                            onNavigateToSession = onNavigateToSession,
                                                            onAddChild = onAddChild,
                                                            onRename = onRename,
                                                            onAddExpectedDuration = onAddExpectedDuration,
                                                            onShowStats = onShowStats,
                                                            onDelete = onDelete,
                                                            showVerticalLine = true,
                                                            onToggleCompleted = onToggleCompleted,
                                                            animatedLeafNodeId = animatedLeafNodeId,
                                                            onLeafClicked = onLeafClicked
                                                        )
                                                        if (isFocusedLeaf) {
                                                            AddChildButton(
                                                                label = "Add Child",
                                                                onClick = { onAddChild(fRow.nodeId) },
                                                                indent = 24.dp
                                                            )
                                                        }
                                                    }
                                                    if (fIndex != focusRows.lastIndex) {
                                                        HorizontalDivider(
                                                            modifier = Modifier.padding(vertical = 2.dp),
                                                            thickness = 0.5.dp,
                                                            color = accentColor.copy(alpha = 0.45f)
                                                        )
                                                    }
                                                }

                                                if (!isTerminalLeaf) {
                                                    AddChildButton(
                                                        label = "Add Item",
                                                        onClick = { onAddChild(terminal.nodeId) },
                                                        indent = 0.dp,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    TreeRow(
                                        node = level2node,
                                        titleFontSize = BREADCRUMB_ROW_TITLE_SIZE,
                                        onToggleExpand = onToggleExpand,
                                        onNodeClick = onNodeClick,
                                        onNavigateToSession = onNavigateToSession,
                                        onAddChild = onAddChild,
                                        onRename = onRename,
                                        onAddExpectedDuration = onAddExpectedDuration,
                                        onShowStats = onShowStats,
                                        onDelete = onDelete,
                                        onToggleCompleted = onToggleCompleted,
                                        animatedLeafNodeId = animatedLeafNodeId,
                                        onLeafClicked = onLeafClicked
                                    )
                                }
                                if (index != node.children.lastIndex && (level2node.nodeId != activeChildId)) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = BETWEEN_CHILD_ROWS),
                                        thickness = 0.5.dp,
                                        color = accentColor.copy(alpha = 0.45f)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(BETWEEN_CHILD_ROWS))
                                }
                            }
                        }

                        AddChildButton(
                            label = "Add Topic",
                            onClick = { onAddChild(node.nodeId) },
                            indent = 0.dp
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
private fun WorkspaceChildRowList(
    rows: List<UnplannedProjectUiModel>,
    accentColor: Color,
    onToggleExpand: (Long) -> Unit,
    onNodeClick: (Long) -> Unit,
    onNavigateToSession: (Long) -> Unit,
    onAddChild: (Long) -> Unit,
    onRename: (Long, String) -> Unit,
    onAddExpectedDuration: (Long) -> Unit,
    onShowStats: (UnplannedProjectUiModel) -> Unit,
    onDelete: (Long) -> Unit,
    onToggleCompleted: (Long, Boolean) -> Unit,
    animatedLeafNodeId: Long?,
    onLeafClicked: (Long) -> Unit
) {
    rows.forEachIndexed { index, row ->
        TreeRow(
            node = row,
            titleFontSize = BREADCRUMB_ROW_TITLE_SIZE,
            onToggleExpand = onToggleExpand,
            onNodeClick = onNodeClick,
            onNavigateToSession = onNavigateToSession,
            onAddChild = onAddChild,
            onRename = onRename,
            onAddExpectedDuration = onAddExpectedDuration,
            onShowStats = onShowStats,
            onDelete = onDelete,
            showVerticalLine = true,
            onToggleCompleted = onToggleCompleted,
            animatedLeafNodeId = animatedLeafNodeId,
            onLeafClicked = onLeafClicked
        )
        if (index != rows.lastIndex) {
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 2.dp),
                thickness = 0.5.dp,
                color = accentColor.copy(alpha = 0.45f)
            )
        }
    }
}

@Composable
private fun WorkspaceHeadingRow(
    node: UnplannedProjectUiModel,
    onToggleExpand: () -> Unit
) {
    if (node.children.isEmpty()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() }
            .padding(6.dp, 8.dp, 4.dp, 0.2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = node.title,
            fontSize = WORKSPACE_HEADING_TITLE_SIZE,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
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
    animatedLeafNodeId: Long?,
    onLeafClicked: (Long) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }
    val accentColor = AccentPalette.getAccent(node.accentIndex)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { if (node.children.isNotEmpty()) onToggleExpand(node.nodeId) else onLeafClicked(node.nodeId) },
                onLongClick = { showMenu = true },
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true)
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.height(80.dp),
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
                    .background(accentColor.copy(alpha = 0.45f), CircleShape)
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
            onToggleCompleted = onToggleCompleted,
            animatedLeafNodeId = animatedLeafNodeId
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
private fun CompletionToggle(
    isCompleted: Boolean,
    onToggle: () -> Unit,
) {
    var bounce by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val tint by animateColorAsState(
        targetValue = if (isCompleted) Color(0xFF4CAF50) else Color.Gray,
        animationSpec = tween(durationMillis = 140),
        label = "CompletionTint"
    )

    val scale by animateFloatAsState(
        targetValue = if (bounce) 0.88f else 1f,
        animationSpec = tween(durationMillis = 140),
        label = "CompletionScale"
    )

    val icon = if (isCompleted) {
        Icons.Filled.CheckCircle
    } else {
        Icons.Outlined.RadioButtonUnchecked
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 20.dp),
                onClick = {
                    bounce = true
                    onToggle()
                    scope.launch {
                        delay(140)
                        bounce = false
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = if (isCompleted) "Mark as not done" else "Mark as done",
            tint = tint,
            modifier = Modifier
                .size(24.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
        )
    }
}

private val TOGGLE_WIDTH = 40.dp
private val PLAY_WIDTH = 40.dp
private val TOGGLE_PLAY_GAP = 6.dp

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
    animatedLeafNodeId: Long?,
) {
    val progress = if (node.expectedDurationSeconds == 0) {
        0f
    } else {
        (node.currentDurationSeconds.toFloat() / node.expectedDurationSeconds).coerceIn(0f, 1f)
    }
    val displayedProgress = if (node.children.isEmpty()) progress else node.completionProgress
    val displayedPercent = if (node.children.isEmpty()) (progress * 100).toInt() else (node.completionProgress * 100).toInt()

    val accentColor = AccentPalette.getAccent(node.accentIndex)
    val isLeafAnimated = animatedLeafNodeId == node.nodeId

    val playScale by animateFloatAsState(
        targetValue = if (isLeafAnimated) 1.15f else 1f,
        animationSpec = tween(durationMillis = 140),
        label = "PlayPulse"
    )

    val playTint by animateColorAsState(
        targetValue = if (isLeafAnimated) Color(0xFF4CAF50) else Color.Gray,
        animationSpec = tween(durationMillis = 140),
        label = "PlayTint"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = node.title,
                fontSize = titleFontSize,
                fontWeight = titleFontWeight,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            if (isExpandable) {
                Box(
                    modifier = Modifier.width(PLAY_WIDTH),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(
                        onClick = onToggleExpand,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown
                            else Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            tint = Color.Gray
                        )
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    CompletionToggle(
                        isCompleted = node.isCompleted,
                        onToggle = { onToggleCompleted(node.nodeId, !node.isCompleted) }
                    )

                    Spacer(modifier = Modifier.width(TOGGLE_PLAY_GAP))

                    Box(
                        modifier = Modifier.width(PLAY_WIDTH),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = onPlay,
                            modifier = Modifier.size(PLAY_WIDTH)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Start Session",
                                tint = playTint,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = playScale
                                    scaleY = playScale
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Est. ${formatShortDuration(node.expectedDurationSeconds)}",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = { displayedProgress },
                modifier = Modifier
                    .weight(1f)
                    .height(4.5.dp)
                    .clip(CircleShape),
                color = accentColor,
                trackColor = Color(0xFF222222),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.width(12.dp))

            if (isExpandable) {
                Box(
                    modifier = Modifier.width(PLAY_WIDTH),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "$displayedPercent%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(TOGGLE_WIDTH + TOGGLE_PLAY_GAP))
                    Box(
                        modifier = Modifier.width(PLAY_WIDTH),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$displayedPercent%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }
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

// Redesigned Bottom Sheet Options
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
        sheetState = sheetState,
        containerColor = DARK_DIALOG_SURFACE,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(36.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF444444))
            )
        }
    ) {
        Column(modifier = Modifier.padding(bottom = 20.dp, start = 8.dp, end = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = nodeName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFF2A2A2A)
            )

            BottomSheetOption(icon = Icons.Default.Add, label = "Add Child Item", onClick = { onAddChild(); onDismiss() })
            BottomSheetOption(icon = Icons.Default.Edit, label = "Rename Item", onClick = { onRename(); onDismiss() })
            BottomSheetOption(icon = Icons.Default.Timer, label = "Set Estimated Time", onClick = { onSetDuration(); onDismiss() })
            BottomSheetOption(icon = Icons.Outlined.BarChart, label = "View Statistics", onClick = { onShowStats(); onDismiss() })

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 6.dp),
                color = Color(0xFF2A2A2A)
            )

            BottomSheetOption(
                icon = Icons.Default.Delete,
                label = "Delete Item",
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
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background((tint ?: Color.White).copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint ?: Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = tint ?: Color.White
        )
    }
}

@Composable
private fun BreadcrumbTrail(
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

// Dialog Input with Theme Styling
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
        containerColor = DARK_DIALOG_SURFACE,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column {
                Text(text = title, style = MaterialTheme.typography.titleSmall, color = Color.Gray)
                if (nodeName != null) {
                    Text(
                        text = nodeName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 2,
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
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DARK_INPUT_BACKGROUND,
                    unfocusedContainerColor = DARK_INPUT_BACKGROUND,
                    focusedBorderColor = Color(0xFF555555),
                    unfocusedBorderColor = Color(0xFF3A3A3A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            }
        }
    )
}

// Infinite Vertical Snap Wheel Column showing exactly 3 items at a time
@Composable
private fun VerticalPickerColumn(
    label: String,
    range: List<Int>,
    selectedValue: Int,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val count = range.size
    if (count == 0) return

    val virtualCount = 10_000 * count
    val initialIndex = remember(selectedValue) {
        val middleOffset = (virtualCount / 2) - ((virtualCount / 2) % count)
        val pos = range.indexOf(selectedValue).coerceAtLeast(0)
        middleOffset + pos
    }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // Automatically update selected value based on center item position
    LaunchedEffect(listState.firstVisibleItemIndex) {
        val actualIndex = listState.firstVisibleItemIndex % count
        val newValue = range[actualIndex]
        if (newValue != selectedValue) {
            onValueSelected(newValue)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .height(120.dp) // Exactly 3 items visible (40.dp * 3)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DARK_INPUT_BACKGROUND)
                .border(1.dp, Color(0xFF3A3A3A), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Fixed Center Selection Highlight Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color(0xFF383838), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF555555), RoundedCornerShape(8.dp))
            )

            LazyColumn(
                state = listState,
                flingBehavior = snapFlingBehavior,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 40.dp) // 40.dp padding centers item 0
            ) {
                items(
                    count = virtualCount,
                    key = { index -> index }
                ) { index ->
                    val value = range[index % count]
                    val isSelected = value == selectedValue
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value.toString(),
                            fontSize = if (isSelected) 18.sp else 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else Color(0xFF777777)
                        )
                    }
                }
            }
        }
    }
}

// Duration Picker Dialog with Continuous Infinite Loop Vertical Wheel Selectors
@Composable
fun ExpectedDurationDialog(
    nodeName: String?,
    initialInput: String,
    onValueChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val initialMinutes = initialInput.toIntOrNull() ?: 0
    val initialDays = initialMinutes / 1440
    val initialHours = (initialMinutes % 1440) / 60
    val initialMins = initialMinutes % 60

    var selectedDays by remember { mutableIntStateOf(initialDays) }
    var selectedHours by remember { mutableIntStateOf(initialHours) }
    var selectedMins by remember { mutableIntStateOf(initialMins) }

    val calculatedTotalMinutes = (selectedDays * 1440) + (selectedHours * 60) + selectedMins

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DARK_DIALOG_SURFACE,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column {
                Text(text = "SET ESTIMATED DURATION", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
                if (nodeName != null) {
                    Text(
                        text = nodeName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VerticalPickerColumn(
                        label = "Days",
                        range = (0..30).toList(),
                        selectedValue = selectedDays,
                        onValueSelected = { selectedDays = it },
                        modifier = Modifier.weight(1f)
                    )
                    VerticalPickerColumn(
                        label = "Hours",
                        range = (0..23).toList(),
                        selectedValue = selectedHours,
                        onValueSelected = { selectedHours = it },
                        modifier = Modifier.weight(1f)
                    )
                    VerticalPickerColumn(
                        label = "Minutes",
                        range = (0..59).toList(),
                        selectedValue = selectedMins,
                        onValueSelected = { selectedMins = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DARK_INPUT_BACKGROUND)
                        .border(1.dp, Color(0xFF3A3A3A), RoundedCornerShape(10.dp))
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Selected: ${formatShortDuration(calculatedTotalMinutes * 60)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onValueChange(calculatedTotalMinutes.toString())
                    onConfirm()
                }
            ) {
                Text("Save", style = MaterialTheme.typography.labelLarge, color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            }
        }
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

// Formats estimated time: Displays Days + Hours + Minutes if >= 24h
private fun formatShortDuration(seconds: Int): String {
    if (seconds <= 0) return "0m"
    val totalMinutes = seconds / 60
    val totalHours = totalMinutes / 60
    val days = totalHours / 24
    val remainingHours = totalHours % 24
    val remainingMinutes = totalMinutes % 60

    return when {
        days > 0 -> {
            buildString {
                append("${days}d")
                if (remainingHours > 0) append(" ${remainingHours}h")
                if (remainingMinutes > 0) append(" ${remainingMinutes}m")
            }
        }
        totalHours > 0 -> {
            buildString {
                append("${totalHours}h")
                if (remainingMinutes > 0) append(" ${remainingMinutes}m")
            }
        }
        else -> "${totalMinutes}m"
    }
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
                expectedDurationSeconds = 90000,
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
                expandedPath = emptyList()
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
            onToggleCompleted = { _, _ -> }
        )
    }
}