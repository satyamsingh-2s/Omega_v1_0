package com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.models_enums.PlannerPriority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerTaskContextMenu(
    state: PlannerTaskContextMenuState,
    onDismiss: () -> Unit,
    onMovePriority: (PlannerPriority) -> Unit,
    onAddToToday: () -> Unit,
    onRemove: () -> Unit
) {

    if (!state.isVisible || state.selectedNode == null) {
        return
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        Column {

            Text(
                text = state.selectedNode.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 20.dp
                )
            )

            HorizontalDivider()

            Text(
                text = "Move To",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
            )

            PlannerPriority.entries
                .filter { it != state.selectedNode.priority }
                .forEach { priority ->

                    Text(
                        text = priority.name
                            .lowercase()
                            .replaceFirstChar { it.uppercase() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onMovePriority(priority)
                            }
                            .padding(
                                horizontal = 32.dp,
                                vertical = 14.dp
                            ),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

            HorizontalDivider()

            Text(
                text = "Today",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
            )

            Text(
                text = "Add to Today",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAddToToday()
                    }
                    .padding(
                        horizontal = 32.dp,
                        vertical = 14.dp
                    ),
                style = MaterialTheme.typography.bodyLarge
            )

            HorizontalDivider()

            Text(
                text = "Planner",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
            )

            Text(
                text = "Remove from Planner",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onRemove()
                    }
                    .padding(
                        horizontal = 32.dp,
                        vertical = 14.dp
                    ),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}