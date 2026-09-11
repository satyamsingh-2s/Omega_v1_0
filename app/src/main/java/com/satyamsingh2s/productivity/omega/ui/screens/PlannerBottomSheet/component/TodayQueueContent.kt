package com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.planner.ui.PlannerNodeUiModel

@Composable
fun TodayQueueContent(

    todayQueue: List<PlannerNodeUiModel>,

    onTaskClick: (PlannerNodeUiModel) -> Unit,

    onTaskLongClick: (PlannerNodeUiModel) -> Unit,

    onOpenPlanner: () -> Unit

) {

    if (todayQueue.isEmpty()) {

        EmptyTodayQueue()

        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {

        items(
            items = todayQueue,
            key = { it.nodeId }
        ) { task ->
            PlannerTaskRow(
                node = task,
                onClick = {
                    onTaskClick(task)
                },
                onLongClick = {
                    onTaskLongClick(task)
                }

            )

        }

    }

}

@Composable
private fun EmptyTodayQueue() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "No tasks scheduled for today",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Open Planner to add tasks.",
                style = MaterialTheme.typography.bodyMedium
            )

        }

    }

}