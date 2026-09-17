package com.satyamsingh2s.productivity.omega.ui.screens.WorkspaceBottomSheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.navigation.workspace.Workspace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceBottomSheet(
    currentWorkspace: Workspace,
    onWorkspaceSelected: (Workspace) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = "Workspace",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(
                    horizontal = 4.dp,
                    vertical = 8.dp
                )
            )

            WorkspaceOptionRow(
                workspace = Workspace.DAILY_RECORD,
                selected = currentWorkspace == Workspace.DAILY_RECORD,
                onClick = {
                    onWorkspaceSelected(Workspace.DAILY_RECORD)
                }
            )

            WorkspaceOptionRow(
                workspace = Workspace.PLANNED,
                selected = currentWorkspace == Workspace.PLANNED,
                onClick = {
                    onWorkspaceSelected(Workspace.PLANNED)
                }
            )

            WorkspaceOptionRow(
                workspace = Workspace.UNPLANNED,
                selected = currentWorkspace == Workspace.UNPLANNED,
                onClick = {
                    onWorkspaceSelected(Workspace.UNPLANNED)
                }
            )

            Spacer(
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Composable
private fun WorkspaceOptionRow(
    workspace: Workspace,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = 4.dp,
                vertical = 12.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (workspace) {
                Workspace.DAILY_RECORD -> "I N S T A N T"
                Workspace.PLANNED -> "A D A P T I V E"
                Workspace.UNPLANNED -> "P L A N N E D"
            },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Checkbox(
            checked = selected,
            onCheckedChange = null
        )
    }
}
