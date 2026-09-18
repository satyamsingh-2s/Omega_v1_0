package com.satyamsingh2s.productivity.omega.navigation.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.satyamsingh2s.productivity.omega.navigation.NavigationItem

@Composable
fun NavigationItemView(
    item: NavigationItem,
    selected: Boolean,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {

    val iconColor =
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Box(
        modifier = modifier
            .combinedClickable(
                enabled = item.enabled,
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                },
                onClick = onClick,
                onLongClick = {
                    onLongClick?.invoke()
                }
            ),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            modifier = Modifier
                .size(
                    if (selected) 26.dp else 24.dp
                )
                .scale(
                    if (selected) 1.02f else 1f
                ),
            tint = iconColor
        )
    }
}