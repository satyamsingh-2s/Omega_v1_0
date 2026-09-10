package com.example.omega_v1_0.ui.screens.PlannerBottomSheet.component


import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.planner.ui.PlannerNodeUiModel

@Composable
fun PlannerTaskRow(
    node: PlannerNodeUiModel,
    onClick: (PlannerNodeUiModel) -> Unit,
    onLongClick: (PlannerNodeUiModel) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onClick(node)
                },
                onLongClick = {
                    onLongClick(node)
                }
            )
            .padding(
                horizontal = 20.dp,
                vertical = 14.dp
            )
    ) {

        Text(
            text = node.title,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = node.title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp)
        )
    }

    HorizontalDivider()
}