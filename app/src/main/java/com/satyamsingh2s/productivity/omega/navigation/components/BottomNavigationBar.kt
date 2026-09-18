package com.satyamsingh2s.productivity.omega.ui.navigation.navigation_bar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Workspaces
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.navigation.NavigationAction
import com.satyamsingh2s.productivity.omega.navigation.NavigationItem
import com.satyamsingh2s.productivity.omega.navigation.components.NavigationItemView

@Composable
fun BottomNavigationBar(
    items: List<NavigationItem>,
    selectedAction: NavigationAction,
    onAction: (NavigationAction) -> Unit,
    onWorkspaceLongClick: () -> Unit,
    onTodoLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(52.dp),
        contentAlignment = Alignment.Center
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { item ->

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {

                    NavigationItemView(
                        modifier = Modifier.fillMaxWidth(),
                        item = item,
                        selected = item.action == selectedAction,

                        onClick = {
                            onAction(item.action)
                        },

                        onLongClick = when (item.action) {

                            NavigationAction.OpenTodo ->
                                onTodoLongClick

                            NavigationAction.OpenWorkspace ->
                                onWorkspaceLongClick

                            else ->
                                null
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun BottomNavigationBarPreview() {

    val items = listOf(

        NavigationItem(
            label = "Todo",
            icon = Icons.Outlined.CheckCircle,
            action = NavigationAction.OpenTodo
        ),

        NavigationItem(
            label = "History",
            icon = Icons.Outlined.History,
            action = NavigationAction.OpenHistory
        ),

        NavigationItem(
            label = "Workspace",
            icon = Icons.Outlined.Workspaces,
            action = NavigationAction.OpenWorkspace
        )
    )

    MaterialTheme {

        BottomNavigationBar(
            items = items,
            selectedAction = NavigationAction.OpenWorkspace,
            onAction = {},
            onWorkspaceLongClick = {},
            onTodoLongClick = {}
        )
    }
}