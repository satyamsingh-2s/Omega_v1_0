package com.example.omega_v1_0.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.ui.model.DailyRecordSessionDetailsUiModel

@Composable
fun DailyRecordDetailsScreen(
    recordDate: String,
    sessions: List<DailyRecordSessionDetailsUiModel>

) {
    val totalSeconds =
        sessions.sumOf {
            it.durationSeconds
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(
            modifier = Modifier.height(26.dp)
        )

        Text(
            text = "HISTORY",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = recordDate,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Text(
            text = "Total Time: ${
                formatDuration(totalSeconds)
            }",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Total Sessions: ${sessions.size}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn(
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            items(sessions) { session ->

                SessionDetailsCard(
                    session = session
                )
            }
        }
    }
}

@Composable
private fun SessionDetailsCard(

    session: DailyRecordSessionDetailsUiModel

) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = session.sessionName,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = formatDuration(
                    session.durationSeconds
                )
            )

            session.expectedDurationMinutes?.let {

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Expected: $it min"
                )
            }
        }
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun DailyRecordDetailsScreenPreview() {

    DailyRecordDetailsScreen(

        recordDate = "13 Jun 2026",

        sessions = listOf(

            DailyRecordSessionDetailsUiModel(
                sessionId = 1,
                sessionName = "Android Architecture",
                durationSeconds = 2700, // 45 min
                expectedDurationMinutes = 60
            ),

            DailyRecordSessionDetailsUiModel(
                sessionId = 2,
                sessionName = "Signals & Systems",
                durationSeconds = 1800, // 30 min
                expectedDurationMinutes = 45
            ),

            DailyRecordSessionDetailsUiModel(
                sessionId = 3,
                sessionName = "Reading",
                durationSeconds = 900, // 15 min
                expectedDurationMinutes = null
            ),

            DailyRecordSessionDetailsUiModel(
                sessionId = 4,
                sessionName = "Kotlin Practice",
                durationSeconds = 1500, // 25 min
                expectedDurationMinutes = 30
            ),

            DailyRecordSessionDetailsUiModel(
                sessionId = 5,
                sessionName = "Planning Tomorrow",
                durationSeconds = 1200, // 20 min
                expectedDurationMinutes = null
            )
        )
    )
}