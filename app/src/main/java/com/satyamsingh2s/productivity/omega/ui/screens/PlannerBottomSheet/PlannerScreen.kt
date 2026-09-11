package com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.satyamsingh2s.productivity.omega.models_enums.PlannerPriority
import com.satyamsingh2s.productivity.omega.planner.ui.PlannerNodeUiModel
import com.satyamsingh2s.productivity.omega.planner.ui.PlannerUiState
import com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component.PlannerBucket
import com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component.PlannerTaskRow

@Composable
fun PlannerScreen(
    uiState: PlannerUiState,
    expandedBucket: PlannerPriority,
    onExpandBucket: (PlannerPriority) -> Unit,
    onTaskClick: (PlannerNodeUiModel) -> Unit,
    onTaskLongClick: (PlannerNodeUiModel) -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {

        item {

            PlannerBucket(
                priority = PlannerPriority.CRITICAL,
                count = uiState.criticalTasks.size,
                isExpanded = expandedBucket == PlannerPriority.CRITICAL,
                onExpandClick = {
                    onExpandBucket(PlannerPriority.CRITICAL)
                }
            ) {

                uiState.criticalTasks.forEach { node ->

                    PlannerTaskRow(
                        node = node,
                        onClick = onTaskClick,
                        onLongClick = onTaskLongClick
                    )
                }
            }
        }

        item {

            PlannerBucket(
                priority = PlannerPriority.HIGH,
                count = uiState.highTasks.size,
                isExpanded = expandedBucket == PlannerPriority.HIGH,
                onExpandClick = {
                    onExpandBucket(PlannerPriority.HIGH)
                }
            ) {

                uiState.highTasks.forEach { node ->

                    PlannerTaskRow(
                        node = node,
                        onClick = onTaskClick,
                        onLongClick = onTaskLongClick
                    )
                }
            }
        }

        item {

            PlannerBucket(
                priority = PlannerPriority.MEDIUM,
                count = uiState.mediumTasks.size,
                isExpanded = expandedBucket == PlannerPriority.MEDIUM,
                onExpandClick = {
                    onExpandBucket(PlannerPriority.MEDIUM)
                }
            ) {

                uiState.mediumTasks.forEach { node ->

                    PlannerTaskRow(
                        node = node,
                        onClick = onTaskClick,
                        onLongClick = onTaskLongClick
                    )
                }
            }
        }

        item {

            PlannerBucket(
                priority = PlannerPriority.LOW,
                count = uiState.lowTasks.size,
                isExpanded = expandedBucket == PlannerPriority.LOW,
                onExpandClick = {
                    onExpandBucket(PlannerPriority.LOW)
                }
            ) {

                uiState.lowTasks.forEach { node ->

                    PlannerTaskRow(
                        node = node,
                        onClick = onTaskClick,
                        onLongClick = onTaskLongClick
                    )
                }
            }
        }

        item {

            PlannerBucket(
                priority = PlannerPriority.BACKLOG,
                count = uiState.backlogTasks.size,
                isExpanded = expandedBucket == PlannerPriority.BACKLOG,
                onExpandClick = {
                    onExpandBucket(PlannerPriority.BACKLOG)
                }
            ) {

                uiState.backlogTasks.forEach { node ->

                    PlannerTaskRow(
                        node = node,
                        onClick = onTaskClick,
                        onLongClick = onTaskLongClick
                    )
                }
            }
        }
    }
}