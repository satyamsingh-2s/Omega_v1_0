package com.satyamsingh2s.productivity.omega.navigation.components

import android.util.Log
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
        if (selected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurfaceVariant

    val textColor =
        if (selected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurface

    Box(

        modifier = modifier
            .combinedClickable(
                enabled = item.enabled,
                onClick = {
                    Log.d("NavigationItemView", "Clicked ${item.label}")
                          onClick()
                          },
                onLongClick = {
                    onLongClick?.invoke()
                }
            )
            .fillMaxWidth(),

        contentAlignment = Alignment.Center

    ) {

        Column(

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center

        ) {

            Icon(

                imageVector = item.icon,

                contentDescription = item.label,

                modifier = Modifier.size(24.dp),

                tint = iconColor

            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(

                text = item.label,

                color = textColor,

                fontWeight =
                    if (selected)
                        FontWeight.SemiBold
                    else
                        FontWeight.Normal,

                style = MaterialTheme.typography.labelSmall

            )

        }

    }

}