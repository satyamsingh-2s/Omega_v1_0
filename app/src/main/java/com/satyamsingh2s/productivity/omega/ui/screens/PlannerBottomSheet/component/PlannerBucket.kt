package com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.models_enums.PlannerPriority

@Composable
fun PlannerBucket(
    priority: PlannerPriority,
    count: Int,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    content: @Composable () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onExpandClick
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    if (isExpanded)
                        Icons.Default.KeyboardArrowDown
                    else
                        Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )

            Text(
                text = priority.name
                    .lowercase()
                    .replaceFirstChar {
                        it.uppercase()
                    },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 4.dp)
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (isExpanded) {

            content()
        }

        HorizontalDivider()
    }
}