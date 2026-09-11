package com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.planner.ui.PlannerNodeUiModel
import com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component.TodayQueueContent
import com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component.TodayQueueFooter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayQueueBottomSheet(

    todayQueue: List<PlannerNodeUiModel>,

    onDismiss: () -> Unit,

    onTaskClick: (PlannerNodeUiModel) -> Unit,

    onTaskLongClick: (PlannerNodeUiModel) -> Unit,

    onOpenPlanner: () -> Unit

) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {

        TodayQueueSheetContent(

            todayQueue = todayQueue,

            onDismiss = onDismiss,

            onTaskClick = onTaskClick,

            onTaskLongClick = onTaskLongClick,

            onOpenPlanner = onOpenPlanner

        )

    }

}

@Composable
private fun TodayQueueSheetContent(

    todayQueue: List<PlannerNodeUiModel>,

    onDismiss: () -> Unit,

    onTaskClick: (PlannerNodeUiModel) -> Unit,

    onTaskLongClick: (PlannerNodeUiModel) -> Unit,

    onOpenPlanner: () -> Unit

) {

    Column {

        TodayQueueHeader(
            onDismiss = onDismiss
        )

        HorizontalDivider()

        Box(
            modifier = Modifier.weight(1f)
        ) {
            TodayQueueContent(
                todayQueue = todayQueue,
                onTaskClick = onTaskClick,
                onTaskLongClick = onTaskLongClick,
                onOpenPlanner = onOpenPlanner
            )
        }

        TodayQueueFooter(
            onOpenPlanner = onOpenPlanner
        )
    }

}

@Composable
private fun TodayQueueHeader(
    onDismiss: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Today's Queue",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = onDismiss
        ) {

            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Close Today's Queue"
            )

        }

    }

}