package com.example.omega_v1_0.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.ui.model.DailyRecordHistoryUiModel
import java.time.format.DateTimeFormatter
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.material3.Surface

@Composable
fun DailyRecordHistoryScreen(

    historyRecords: List<DailyRecordHistoryUiModel>,

    onRecordClick: (DailyRecordHistoryUiModel) -> Unit

) {
    Surface(
        modifier = Modifier.fillMaxSize()
             .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Daily Record History",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(historyRecords) { record ->

                    DailyRecordHistoryCard(
                        record = record,
                        onClick = {
                            Log.d(
                                "OMEGA",
                                "Card clicked⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️"
                            )
                            onRecordClick(record)
                        }
                    )
                }
            }
        }
    }
}

    @Composable
    private fun DailyRecordHistoryCard(
        record: DailyRecordHistoryUiModel,
        onClick: () -> Unit

    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = record.recordDate.format(
                        DateTimeFormatter.ofPattern(
                            "dd MMM yyyy"
                        )
                    ),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
                Spacer(Modifier.height(4.dp))

                Text(
                    text = "total working time = " + formatDuration(
                        record.totalDurationSeconds
                    )
                )
                Spacer(Modifier.height(4.dp))

                Text(
                    text = "total sessions = ${record.totalSessionCount}"
                )
                Spacer(Modifier.height(4.dp))

                Text(
                    text = "total recovery time =  " + formatDuration(
                        record.totalbreakseconds
                    )
                )
                Spacer(Modifier.height(4.dp))

                Text(
                    text = "total work ratio = ${
                        if (record.totalbreakseconds == 0) {
                            "1"
                        } else {
                            (record.totalDurationSeconds / record.totalbreakseconds)
                        }
                    } : 1"

                )
            }
        }
    }


//private fun formatDuration(
//    totalSeconds: Int
//): String {
//
//    val hours = totalSeconds / 3600
//    val minutes = (totalSeconds % 3600) / 60
//    val seconds = totalSeconds % 60
//
//    return String.format(
//        "%02d:%02d:%02d",
//        hours,
//        minutes,
//        seconds
//    )
//}