package com.example.omega_v1_0.ui.navigation.navigation_bar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Workspaces
import androidx.compose.ui.tooling.preview.Preview
import com.example.omega_v1_0.navigation.NavigationAction
import com.example.omega_v1_0.navigation.NavigationItem
import com.example.omega_v1_0.navigation.components.NavigationItemView

@Composable
fun BottomNavigationBar(
    items: List<NavigationItem>,
    selectedAction: NavigationAction,
    onAction: (NavigationAction) -> Unit,
    onWorkspaceLongClick: () -> Unit,
    onTodoLongClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    NavigationBar(
        modifier = modifier.fillMaxWidth()
    ) {

        Row(modifier = Modifier.fillMaxWidth())
        {
            items.forEach { item ->
                Box(
                    modifier = Modifier.weight(1f)
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
            onTodoLongClick ={},

        )

    }
}