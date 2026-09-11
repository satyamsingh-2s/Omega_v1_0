package com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TodayQueueFooter(

    onOpenPlanner: () -> Unit

) {

    HorizontalDivider()

    Button(

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),

        onClick = onOpenPlanner

    ) {

        Text(
            text = "Open Planner"
        )

    }

}