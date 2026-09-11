package com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.models_enums.PlannerPriority
import com.satyamsingh2s.productivity.omega.planner.ui.PlannerNodeUiModel
import com.satyamsingh2s.productivity.omega.planner.ui.PlannerUiState
import com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component.PlannerTaskContextMenu
import com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component.PlannerTaskContextMenuState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerBottomSheet(
    uiState: PlannerUiState,
    expandedBucket: PlannerPriority,
    taskContextMenuState: PlannerTaskContextMenuState,

    onDismiss: () -> Unit,
    onExpandBucket: (PlannerPriority) -> Unit,

    onTaskClick: (PlannerNodeUiModel) -> Unit,
    onTaskLongClick: (PlannerNodeUiModel) -> Unit,
    onDismissTaskMenu: () -> Unit,

    onMovePriority: (PlannerPriority) -> Unit,
    onAddToToday: () -> Unit,
    onRemoveFromPlanner: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        PlannerTaskContextMenu(
            state = taskContextMenuState,
            onDismiss = onDismissTaskMenu,
            onMovePriority = onMovePriority,
            onAddToToday = onAddToToday,
            onRemove = onRemoveFromPlanner
        )

        PlannerSheetContent(
            uiState = uiState,
            expandedBucket = expandedBucket,
            onDismiss = onDismiss,
            onExpandBucket = onExpandBucket,
            onTaskClick = onTaskClick,
            onTaskLongClick = onTaskLongClick
        )
    }
}

@Composable
private fun PlannerSheetContent(
    uiState: PlannerUiState,
    expandedBucket: PlannerPriority,

    onTaskClick: (PlannerNodeUiModel) -> Unit,
    onDismiss: () -> Unit,
    onExpandBucket: (PlannerPriority) -> Unit,
    onTaskLongClick: (PlannerNodeUiModel) -> Unit
) {

    Column {

        PlannerHeader(
            onDismiss = onDismiss
        )

        HorizontalDivider()

        PlannerScreen(
            uiState = uiState,
            expandedBucket = expandedBucket,
            onExpandBucket = onExpandBucket,
            onTaskClick = onTaskClick,
            onTaskLongClick = onTaskLongClick
        )
    }
}

@Composable
private fun PlannerHeader(
    onDismiss: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Planner",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onDismiss
        ) {

            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Close Planner"
            )
        }
    }
}