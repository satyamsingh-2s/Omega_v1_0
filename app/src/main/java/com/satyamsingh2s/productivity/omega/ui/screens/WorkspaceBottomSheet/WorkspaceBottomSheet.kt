package com.satyamsingh2s.productivity.omega.ui.screens.WorkspaceBottomSheet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
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
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 8.dp
                )
        ) {

            // ================================================================
            // HEADER
            // ================================================================

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {

                Text(
                    text = "Workspace",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            // ================================================================
            // ADAPTIVE
            // ================================================================

            WorkspaceOptionRow(
                workspace = Workspace.PLANNED,
                selected = currentWorkspace == Workspace.PLANNED,
                onClick = {
                    onWorkspaceSelected(Workspace.PLANNED)
                }
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(
                    alpha = 0.45f
                )
            )

            // ================================================================
            // PLANNED
            // ================================================================

            WorkspaceOptionRow(
                workspace = Workspace.UNPLANNED,
                selected = currentWorkspace == Workspace.UNPLANNED,
                onClick = {
                    onWorkspaceSelected(Workspace.UNPLANNED)
                }
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(
                    alpha = 0.45f
                )
            )

            // ================================================================
            // INSTANT
            // ================================================================

            WorkspaceOptionRow(
                workspace = Workspace.DAILY_RECORD,
                selected = currentWorkspace == Workspace.DAILY_RECORD,
                onClick = {
                    onWorkspaceSelected(Workspace.DAILY_RECORD)
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
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
                horizontal = 8.dp,
                vertical = 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // ====================================================================
        // MODE NAME
        // ====================================================================

        Text(
            text = when (workspace) {
                Workspace.PLANNED -> "A D A P T I V E"
                Workspace.UNPLANNED -> "P L A N N E D"
                Workspace.DAILY_RECORD -> "I N S T A N T"
            },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (selected) {
                FontWeight.Medium
            } else {
                FontWeight.Normal
            },
            color = if (selected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // ====================================================================
        // SELECTION CIRCLE
        // ====================================================================

        SelectionCircle(
            selected = selected
        )
    }
}


@Composable
private fun SelectionCircle(
    selected: Boolean
) {
    // Resolve composable colors BEFORE entering Canvas.
    val primaryColor = MaterialTheme.colorScheme.primary
    val outlineColor = MaterialTheme.colorScheme.outline
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier.size(22.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.size(20.dp)
        ) {

            if (selected) {

                drawCircle(
                    color = primaryColor
                )

                drawCircle(
                    color = onPrimaryColor,
                    radius = 3.dp.toPx()
                )

            } else {

                drawCircle(
                    color = outlineColor,
                    style = Stroke(
                        width = 1.5.dp.toPx()
                    )
                )
            }
        }
    }
}