package com.satyamsingh2s.productivity.omega.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.satyamsingh2s.productivity.omega.ui.model.DailyRecordSessionDetailsUiModel
import com.satyamsingh2s.productivity.omega.ui.theme.OmegaDarkTheme
import com.satyamsingh2s.productivity.omega.ui.utils.formatDuration

@Composable
fun DailyRecordDetailsScreen(
    recordDate: String,
    sessions: List<DailyRecordSessionDetailsUiModel>

) {
    val totalSeconds =
        sessions.sumOf {
            it.durationSeconds
        }

    Surface(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Text(
                text = "History",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            // Total Time and Total Sessions Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Time",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = formatDuration(totalSeconds),
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 36.sp, fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(1.dp).height(60.dp).background(MaterialTheme.colorScheme.outline))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Sessions",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = sessions.size.toString(),
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 36.sp, fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sessions",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(sessions) { session ->

                    SessionDetailsCard(
                        session = session
                    )
                }
            }
        }
    }
}

@Composable
private fun SessionDetailsCard(

    session: DailyRecordSessionDetailsUiModel

) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.Top
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(

                    text = session.sessionName,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,

                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatDuration(session.durationSeconds),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            session.expectedDurationMinutes?.let {
                Card(
                    modifier = Modifier.widthIn(min = 90.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = "exp. $it m",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
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
    OmegaDarkTheme {
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
}