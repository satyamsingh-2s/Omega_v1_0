package com.satyamsingh2s.productivity.omega.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satyamsingh2s.productivity.omega.models_enums.RevisionNoteItem
import com.satyamsingh2s.productivity.omega.omega_engines.pomodoro_engine.PomodoroState
import com.satyamsingh2s.productivity.omega.models_enums.SessionStatus
import com.satyamsingh2s.productivity.omega.models_enums.TodoCategory
import com.satyamsingh2s.productivity.omega.ui.components.PomodoroCard
import com.satyamsingh2s.productivity.omega.ui.components.PomodoroCardStyle
import com.satyamsingh2s.productivity.omega.ui.components.SessionDefaults
//import com.satyamsingh2s.productivity.omega.ui.components.SessionControlCard
//import com.satyamsingh2s.productivity.omega.ui.components.SessionControlCardStyle
import com.satyamsingh2s.productivity.omega.ui.components.common.CircularIconButton
import com.satyamsingh2s.productivity.omega.ui.components.dialogs.RevisionNoteEditorDialog
import com.satyamsingh2s.productivity.omega.ui.components.dialogs.RevisionNoteViewerDialog
import com.satyamsingh2s.productivity.omega.ui.components.revision_notes.RevisionHistoryPanel
import com.satyamsingh2s.productivity.omega.ui.model.RevisionNoteMenuAction
import com.satyamsingh2s.productivity.omega.ui.model.ToDoListUiModel
import com.satyamsingh2s.productivity.omega.ui.model.UnplannedProjectRecentSessionUiModel
import com.satyamsingh2s.productivity.omega.ui.theme.StopwatchTextStyle
import com.satyamsingh2s.productivity.omega.ui.utils.formatDuration
import com.satyamsingh2s.productivity.omega.ui.viewmodel.RevisionNoteViewModel

/**
 * ================================================================
 * LAYOUT STRATEGY
 * ================================================================
 * Region A - "Cockpit" (does not independently scroll away; capped
 * + has its own scroll as an overflow safety net only):
 *   Header (back arrow + title + adaptive breadcrumb)
 *   ProjectProgressZone
 *   SessionControlZone (timer + start/pause/stop)
 *   UtilityZone (Notes / Today buttons)
 *
 * Region B - independent scrollable history list:
 *   RecentSessionsZone (past sessions) only
 *
 * "View Statistics" has been removed entirely from the layout.
 * `onStatsClick` is kept as a parameter (unused internally) so
 * existing call sites that pass it do not need to change yet.
 * ================================================================
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnplannedProjectSessionScreen(
    projectName: String,
    breadcrumb: String,
    currentDurationSeconds: Int,
    expectedDurationSeconds: Int,
    totalSessions: Int,
    sessionName: String,
    activeSessionName: String?,
    onSessionNameChanged: (String) -> Unit,
    sessionStatus: SessionStatus?,
    stopwatchSeconds: Int,
    onStartSession: () -> Unit,
    onPauseSession: () -> Unit,
    onResumeSession: () -> Unit,
    onStopSession: () -> Unit,
    recentSessions: List<UnplannedProjectRecentSessionUiModel>,
    onBack: () -> Unit,
    onStatsClick: () -> Unit, // kept for call-site compatibility; no longer rendered
    // ----- estimated minutes section ---
    selectedDurationMinutes: Int?,
    onDurationSelected: (Int?) -> Unit,
    // -------------------------
    pomodoroState: PomodoroState?,
    workCyclesBeforeLongBreak: Int,
    onSkipBreak: () -> Unit,

    // ----------To do list variables -----
    todoItems: List<ToDoListUiModel> = emptyList(),
    newTodoText: String = "",
    onTodoTextChanged: (String) -> Unit = {},
    onAddTodo: () -> Unit = {},
    onToggleTodo: (ToDoListUiModel) -> Unit = {},
    onDeleteTodo: (Long) -> Unit = {},
    selectedTodoCategory: TodoCategory = TodoCategory.TODAY,
    onTodoCategoryChanged: (TodoCategory) -> Unit = {},

    revisionNoteViewModel: RevisionNoteViewModel,

    //----------------
    navigateToDeskOmega: () -> Unit = {},
) {
    val progress = if (expectedDurationSeconds == 0) 0f else (currentDurationSeconds.toFloat() / expectedDurationSeconds).coerceAtMost(1f)
    val groupedSessions = recentSessions.chunked(3)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val groupWidth = screenWidth * 0.85f

    var showFocusSheet by remember { mutableStateOf(false) }

    var showNotesSheet by remember { mutableStateOf(false) }
    var selectedRevisionNote by remember { mutableStateOf<RevisionNoteItem?>(null) }
    var showRevisionEditor by remember { mutableStateOf(false) }
    val revisionNoteUiState by revisionNoteViewModel.uiState.collectAsState()
    val revisionNotes by revisionNoteViewModel.revisionNotes.collectAsState()

    val photoPickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let { revisionNoteViewModel.addAttachment(it.toString()) }
        }

    val todayTasksLeft =
        if (selectedTodoCategory == TodoCategory.TODAY) todoItems.size else null
    val futureTasksLeft =
        if (selectedTodoCategory == TodoCategory.FUTURE) todoItems.size else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        /*
 * ONE WORKSPACE SCROLL
 *
 * The entire screen shares one vertical scroll container.
 *
 * - If content fits -> there is nothing to scroll.
 * - If content overflows -> the complete workspace scrolls.
 * - Past sessions remain horizontally scrollable internally.
 *
 * This avoids the "scroll inside scroll" feeling created by
 * having separate vertical scroll areas for the cockpit and history.
 */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ============================================================
            // HEADER
            // ============================================================

            HeaderZone(
                projectName = projectName,
                breadcrumb = breadcrumb,
                onBack = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // ============================================================
            // PROJECT PROGRESS
            // ============================================================

            ProjectProgressZone(
                currentDurationSeconds = currentDurationSeconds,
                expectedDurationSeconds = expectedDurationSeconds,
                progress = progress,
                totalSessions = totalSessions,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ============================================================
            // SESSION CONTROL
            // ============================================================

            SessionControlZone(
                sessionName = sessionName,
                activeSessionName = activeSessionName,
                onSessionNameChanged = onSessionNameChanged,
                sessionStatus = sessionStatus,
                stopwatchSeconds = stopwatchSeconds,
                onStartSession = onStartSession,
                onPauseSession = onPauseSession,
                onResumeSession = onResumeSession,
                onStopSession = onStopSession,
                selectedDurationMinutes = selectedDurationMinutes,
                onDurationSelected = onDurationSelected,
                pomodoroState = pomodoroState,
                workCyclesBeforeLongBreak = workCyclesBeforeLongBreak,
                onSkipBreak = onSkipBreak,
                totalSessionSeconds = expectedDurationSeconds,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ============================================================
            // UTILITY ACTIONS
            // ============================================================

            UtilityZone(
                onNotesClick = { showNotesSheet = true },
                onTodoClick = { showFocusSheet = true },
                selectedTodoCategory = selectedTodoCategory,
                todayTasksLeft = todayTasksLeft,
                futureTasksLeft = futureTasksLeft,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ============================================================
            // PAST SESSIONS
            // ============================================================

            RecentSessionsZone(
                groupedSessions = groupedSessions,
                groupWidth = groupWidth,
                onSessionLongClick = { selectedSession ->
                    revisionNoteViewModel.loadRevisionNote(selectedSession.id)
                    showRevisionEditor = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // --------------------------------------------------------------
    // Bottom sheets and dialogs (outside the workspace layout)
    // --------------------------------------------------------------
    if (showFocusSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFocusSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 320.dp, max = 560.dp)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newTodoText,
                        onValueChange = onTodoTextChanged,
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text("Add a new task...", style = MaterialTheme.typography.bodyMedium)
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

                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                androidx.compose.foundation.Canvas(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { onToggleTodo(item) },
                                    onDraw = {
                                        drawCircle(color = checkboxColor, radius = size.minDimension / 2)
                                        if (item.isCompleted) {
                                            drawCircle(color = checkmarkColor, radius = size.minDimension / 4)
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.width(14.dp))
                                Text(
                                    text = item.text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = textColor,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(14.dp))
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
    }

    if (showNotesSheet) {
        ModalBottomSheet(
            onDismissRequest = { showNotesSheet = false }
        ) {
            RevisionHistoryPanel(
                notes = revisionNotes,
                onNoteClick = {
                    revisionNoteViewModel.loadRevisionNote(it.sessionId)
                    selectedRevisionNote = it
                }
            )
        }
    }

    selectedRevisionNote?.let { note ->
        RevisionNoteViewerDialog(
            note = note,
            attachments = revisionNoteUiState.attachments,
            onEdit = {
                selectedRevisionNote = null
                showRevisionEditor = true
            },
            onDismiss = { selectedRevisionNote = null }
        )
    }

    if (showRevisionEditor) {
        RevisionNoteEditorDialog(
            summary = revisionNoteUiState.summary,
            attachments = revisionNoteUiState.attachments,
            onSummaryChange = revisionNoteViewModel::onSummaryChanged,
            onSave = {
                revisionNoteViewModel.saveRevisionNote {
                    showRevisionEditor = false
                    revisionNoteViewModel.clear()
                }
            },
            onDismiss = {
                showRevisionEditor = false
                revisionNoteViewModel.clear()
            },
            onMenuAction = { action ->
                when (action) {
                    RevisionNoteMenuAction.ADD_IMAGE -> {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                }
            },
            onDeleteAttachment = { attachmentId ->
                revisionNoteViewModel.deleteAttachment(attachmentId)
            },
        )
    }
}

// ================================================================
// WORKSPACE ZONE COMPOSABLES
// ================================================================

@Composable
private fun HeaderZone(
    projectName: String,
    breadcrumb: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        // ------------------------------------------------------------
        // Row 1 — Back button + Project name
        // ------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = projectName,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 19.sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp)
            )
        }

        // ------------------------------------------------------------
        // Row 2 — Breadcrumb
        //
        // Intentionally outside the arrow/title Row so it can use
        // the complete horizontal width of the content area.
        // ------------------------------------------------------------
        AdaptiveBreadcrumb(
            breadcrumb = breadcrumb,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
        )
    }
}

/**
 * Renders the breadcrumb across up to 2 lines. If the full string
 * still doesn't fit in 2 lines, it falls back to a single line
 * showing "… / <last segment>" - the most specific/relevant part of
 * the path, not an arbitrary end-truncation.
 *
 * Technique: render the full text first; `onTextLayout` reports
 * whether it visually overflowed the 2-line box. If it did, swap to
 * the abbreviated fallback string on the next recomposition - there's
 * no way to know "does the whole thing fit in 2 lines?" without
 * asking the text layout engine once.
 */

@Composable
private fun AdaptiveBreadcrumb(
    breadcrumb: String,
    separator: String = ">",
    modifier: Modifier = Modifier,
) {
    val textStyle = MaterialTheme.typography.bodyMedium
    val textColor = MaterialTheme.colorScheme.onSurfaceVariant

    val density = androidx.compose.ui.platform.LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    BoxWithConstraints(
        modifier = modifier.fillMaxWidth()
    ) {
        val maxWidthPx = with(density) {
            maxWidth.roundToPx()
        }

        val segments = remember(breadcrumb, separator) {
            breadcrumb
                .split(separator)
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }

        if (segments.isEmpty()) {
            Text(
                text = breadcrumb,
                style = textStyle,
                color = textColor,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
        } else {

            /*
             * We build the breadcrumb from RIGHT -> LEFT.
             *
             * The last segment is always preserved.
             *
             * Example:
             *
             * A > B > C > D > E
             *
             * Possible result:
             *
             * … > C > D
             * E
             *
             * or:
             *
             * A > B > C
             * D > E
             *
             * depending on available width.
             */

            fun fits(text: String): Boolean {
                val result = textMeasurer.measure(
                    text = text,
                    style = textStyle,
                    constraints = Constraints(
                        maxWidth = maxWidthPx
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Clip
                )

                return !result.hasVisualOverflow
            }

            /*
             * First check whether the complete breadcrumb fits.
             */
            val displayText = if (fits(segments.joinToString(" $separator "))) {

                segments.joinToString(" $separator ")

            } else {

                /*
                 * We always keep the LAST segment.
                 *
                 * Start with:
                 *
                 * … > LAST
                 *
                 * Then progressively add segments before it.
                 *
                 * The first candidate that no longer fits is rejected.
                 */

                var best = "… $separator ${segments.last()}"

                for (startIndex in segments.lastIndex - 1 downTo 0) {

                    val candidateSegments = segments.subList(
                        startIndex,
                        segments.size
                    )

                    val candidate =
                        "… $separator " +
                                candidateSegments.joinToString(" $separator ")

                    if (fits(candidate)) {
                        best = candidate
                    } else {
                        /*
                         * Adding an older segment made it too large.
                         * Stop here because we only want the longest
                         * suffix that fits.
                         */
                        break
                    }
                }

                best
            }

            Text(
                text = displayText,
                style = textStyle,
                color = textColor,
                maxLines = 2,
                overflow = TextOverflow.Clip,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Composable
private fun ProjectProgressZone(
    currentDurationSeconds: Int,
    expectedDurationSeconds: Int,
    progress: Float,
    totalSessions: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Current",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDuration(currentDurationSeconds),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Expected",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatDuration(expectedDurationSeconds),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            )
            Text(
                text = "$totalSessions Sessions",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
private fun SessionControlZone(
    sessionName: String,
    activeSessionName: String?,
    onSessionNameChanged: (String) -> Unit,
    sessionStatus: SessionStatus?,
    stopwatchSeconds: Int,
    onStartSession: () -> Unit,
    onPauseSession: () -> Unit,
    onResumeSession: () -> Unit,
    onStopSession: () -> Unit,
    selectedDurationMinutes: Int?,
    onDurationSelected: (Int?) -> Unit,
    pomodoroState: PomodoroState?,
    workCyclesBeforeLongBreak: Int,
    onSkipBreak: () -> Unit,
    totalSessionSeconds: Int,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {

        /*
         * The session controller owns its own responsive spacing.
         *
         * We deliberately do NOT multiply font sizes by fontScale.
         * Compose already applies the user's font scale to sp values.
         *
         * Our job is to make the surrounding layout flexible enough
         * to accommodate that larger text.
         */

        val horizontalPadding = when {
            maxWidth < 320.dp -> 12.dp
            maxWidth < 400.dp -> 16.dp
            else -> 24.dp
        }

        val sectionSpacing = when {
            maxWidth < 320.dp -> 10.dp
            maxWidth < 400.dp -> 12.dp
            else -> 14.dp
        }

        val innerSpacing = when {
            maxWidth < 320.dp -> 6.dp
            maxWidth < 400.dp -> 8.dp
            else -> 10.dp
        }

        /*
         * The gap between two control buttons also adapts to width.
         * We don't use a large fixed 48.dp gap on narrow screens.
         */
        val controlSpacing = when {
            maxWidth < 320.dp -> 20.dp
            maxWidth < 400.dp -> 28.dp
            else -> 40.dp
        }

        if (pomodoroState?.isEnabled == true) {

            // =========================================================
            // POMODORO MODE
            // =========================================================

            PomodoroCard(
                sessionName = sessionName,
                totalSessionSeconds = totalSessionSeconds,
                sessionStatus = sessionStatus,
                pomodoroState = pomodoroState,
                workCyclesBeforeLongBreak = workCyclesBeforeLongBreak,
                onStart = onStartSession,
                onPause = onPauseSession,
                onResume = onResumeSession,
                onStop = onStopSession,
                onSkipBreak = onSkipBreak,
                onMenuClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                style = PomodoroCardStyle(
                    0.8f,
                    0.5f,
                    0.5f,
                    0.6f,
                    1f
                )
            )

        } else {

            // =========================================================
            // NORMAL SESSION MODE
            // =========================================================

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = horizontalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {

                // -----------------------------------------------------
                // ZONE 1 — Session name + duration
                // -----------------------------------------------------

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(innerSpacing)
                ) {

                    OutlinedTextField(
                        value = sessionName,
                        onValueChange = onSessionNameChanged,

                        /*
                         * bodyMedium uses sp and therefore naturally
                         * responds to Android's font-scale setting.
                         */
                        textStyle = MaterialTheme.typography.bodyMedium,

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
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )
                        },

                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "Session Name",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(24.dp)
                            )
                        },

                        modifier = Modifier.fillMaxWidth(),

                        singleLine = true,

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background
                        )
                    )

                    /*
                     * Duration options intentionally remain horizontally
                     * scrollable. Larger fonts therefore don't force the
                     * chips into a smaller font.
                     */
                    LazyRow(
                        state = rememberLazyListState(
                            initialFirstVisibleItemIndex = 2
                        ),
                        horizontalArrangement = Arrangement.spacedBy(
                            innerSpacing
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(SessionDefaults.durationOptions) { option ->

                            FilterChip(
                                selected = selectedDurationMinutes == option,
                                onClick = {
                                    onDurationSelected(option)
                                },
                                label = {
                                    Text(
                                        text = option?.let { "${it}m" } ?: "\u25CB",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            )
                        }
                    }

                    if (selectedDurationMinutes != null) {
                        Text(
                            text = "Expected Durations",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                // -----------------------------------------------------
                // ZONE 2 — Stopwatch
                // -----------------------------------------------------

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    Text(
                        text = formatDuration(stopwatchSeconds),
                        style = StopwatchTextStyle,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        softWrap = false
                    )

                    Text(
                        text = "HH : MM : SS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // -----------------------------------------------------
                // ZONE 3 — Session controls
                // -----------------------------------------------------

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        controlSpacing,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    when (sessionStatus) {

                        // -------------------------------------------------
                        // No active session
                        // -------------------------------------------------

                        null -> {
                            CircularIconButton(
                                onClick = onStartSession,
                                icon = Icons.Filled.PlayArrow,
                                contentDescription = "Start Session",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                iconTint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        // -------------------------------------------------
                        // Running
                        // -------------------------------------------------

                        SessionStatus.RUNNING -> {

                            CircularIconButton(
                                onClick = onPauseSession,
                                icon = Icons.Filled.Pause,
                                contentDescription = "Pause Session",
                                backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                                iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            CircularIconButton(
                                onClick = onStopSession,
                                icon = Icons.Filled.Stop,
                                contentDescription = "Stop Session",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                iconTint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }

                        // -------------------------------------------------
                        // Paused
                        // -------------------------------------------------

                        SessionStatus.PAUSED -> {

                            CircularIconButton(
                                onClick = onResumeSession,
                                icon = Icons.Filled.PlayArrow,
                                contentDescription = "Resume Session",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                iconTint = MaterialTheme.colorScheme.onPrimary,
                            )

                            CircularIconButton(
                                onClick = onStopSession,
                                icon = Icons.Filled.Stop,
                                contentDescription = "Stop Session",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                iconTint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UtilityZone(
    onNotesClick: () -> Unit,
    onTodoClick: () -> Unit,
    selectedTodoCategory: TodoCategory,
    todayTasksLeft: Int?,
    futureTasksLeft: Int?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            onClick = onNotesClick
        ) {
            Text(
                text = "Notes",
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1
            )
        }

        Button(
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            onClick = onTodoClick
        ) {
            val label = when (selectedTodoCategory) {
                TodoCategory.TODAY ->
                    "TODAY [ $todayTasksLeft ]"

                TodoCategory.FUTURE ->
                    "FUTURE [ $futureTasksLeft ]"
            }

            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RecentSessionsZone(
    groupedSessions: List<List<UnplannedProjectRecentSessionUiModel>>,
    groupWidth: Dp,
    onSessionLongClick: (UnplannedProjectRecentSessionUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "PAST SESSIONS",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(groupedSessions) { sessionGroup ->

                    Column(
                        modifier = Modifier.width(groupWidth),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        sessionGroup.forEach { session ->

                            UnplannedRecentSessionItem(
                                session = session,
                                onLongClick = onSessionLongClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UnplannedRecentSessionItem(
    session: UnplannedProjectRecentSessionUiModel,
    onLongClick: (UnplannedProjectRecentSessionUiModel) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { /* Reserved for future */ },
                onLongClick = { onLongClick(session) }
            ),
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

