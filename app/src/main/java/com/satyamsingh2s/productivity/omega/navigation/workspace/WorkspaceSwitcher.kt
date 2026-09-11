package com.satyamsingh2s.productivity.omega.navigation.workspace

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.satyamsingh2s.productivity.omega.ui.components.floating_menu.FloatingMenu

@Composable
fun WorkspaceSwitcher(

    expanded: Boolean,

    currentWorkspace: Workspace,

    onDismiss: () -> Unit,

    onWorkspaceSelected: (Workspace) -> Unit

) {

    FloatingMenu(

        expanded = expanded,

        onDismiss = onDismiss,

        alignment = WorkspaceSwitcherDefaults.PopupAlignment,

        offset = WorkspaceSwitcherDefaults.PopupOffset

    ) {

        WorkspaceMenuItem(
            title = "Daily",
            icon = Icons.Outlined.Today,
            workspace = Workspace.DAILY_RECORD,
            currentWorkspace = currentWorkspace,
            onClick = onWorkspaceSelected
        )

        HorizontalDivider()

        WorkspaceMenuItem(
            title = "Unplanned",
            icon = Icons.Outlined.AccountTree,
            workspace = Workspace.UNPLANNED,
            currentWorkspace = currentWorkspace,
            onClick = onWorkspaceSelected
        )

        HorizontalDivider()

        WorkspaceMenuItem(
            title = "Planned",
            icon = Icons.Outlined.Assignment,
            workspace = Workspace.PLANNED,
            currentWorkspace = currentWorkspace,
            onClick = onWorkspaceSelected
        )

    }
}

//@Composable
//private fun WorkspaceMenuItem(
//
//    title: String,
//
//    workspace: Workspace,
//
//    currentWorkspace: Workspace,
//
//    onWorkspaceSelected: (Workspace) -> Unit
//
//) {
//
//    val selected = workspace == currentWorkspace
//
//    DropdownMenuItem(
//
//        text = {
//
//            Text(
//
//                text = title,
//
//                color =
//                    if (selected)
//                        MaterialTheme.colorScheme.primary
//                    else
//                        MaterialTheme.colorScheme.onSurface,
//
//                fontWeight =
//                    if (selected)
//                        FontWeight.Bold
//                    else
//                        FontWeight.Normal
//
//            )
//
//        },
//
//        onClick = {
//
//            onWorkspaceSelected(workspace)
//
//        }
//
//    )
//
//}

@Preview(showBackground = true)
@Composable
private fun WorkspaceSwitcherPreview() {

    MaterialTheme {

        WorkspaceSwitcher(

            expanded = true,

            currentWorkspace = Workspace.DAILY_RECORD,

            onDismiss = {},

            onWorkspaceSelected = {}

        )

    }

}