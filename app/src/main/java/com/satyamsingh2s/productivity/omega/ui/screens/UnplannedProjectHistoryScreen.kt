//package com.satyamsingh2s.productivity.omega_v1_0.ui.screens
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowForwardIos
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.satyamsingh2s.productivity.omega_v1_0.ui.model.DailyRecordHistoryUiModel
//import com.satyamsingh2s.productivity.omega_v1_0.ui.theme.OmegaDarkTheme
//import java.time.format.DateTimeFormatter
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.ui.tooling.preview.Preview
//import com.satyamsingh2s.productivity.omega_v1_0.ui.uistate.UnplannedProjectHistoryUiState
//import com.satyamsingh2s.productivity.omega_v1_0.ui.utils.formatDuration
//import java.time.LocalDate
//
//@Composable
//fun UnplannedProjectHistoryScreen(
//
//    historyRecords: List<UnplannedProjectHistoryUiState>,
//    onRecordClick: (DailyRecordHistoryUiModel) -> Unit
//
//) {
//    Surface(
//        modifier = Modifier.fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 24.dp)
//        ) {
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Text(
//                text = "History",
//                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold),
//                color = MaterialTheme.colorScheme.onBackground
//            )
//
//            Spacer(
//                modifier = Modifier.height(24.dp)
//            )
//
//            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//
//                items(historyRecords) { record ->
//
//                    DailyRecordHistoryCard(
//                        record = record,
//                        onClick = {
//                            Log.d(
//                                "OMEGA",
//                                "Card clicked⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️"
//                            )
//                            onRecordClick(record)
//                        }
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun DailyRecordHistoryCard(
//    record: DailyRecordHistoryUiModel,
//    onClick: () -> Unit
//
//) {
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable {
//                onClick()
//            },
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant
//        )
//    ) {
//
//        Column(
//            modifier = Modifier.padding(24.dp)
//        ) {
//            // Date and Arrow
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = record.recordDate.format(
//                        DateTimeFormatter.ofPattern(
//                            "dd MMM yyyy"
//                        )
//                    ),
//                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Icon(
//                    imageVector = Icons.Default.ArrowForwardIos,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.size(18.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            // Working Time and Sessions Row
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(
//                        text = "Working Time",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = formatDuration(record.totalDurationSeconds),
//                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold),
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                }
//                Column(horizontalAlignment = Alignment.End) {
//                    Text(
//                        text = "Sessions",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = record.totalSessionCount.toString(),
//                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold),
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Recovery Time and Work Ratio Row
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Column {
//                    Text(
//                        text = "Recovery",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = formatDuration(record.totalbreakseconds),
//                        style = MaterialTheme.typography.titleLarge,
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//                Column(horizontalAlignment = Alignment.End) {
//                    Text(
//                        text = "Work Ratio",
//                        style = MaterialTheme.typography.labelMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = "${
//                            if (record.totalbreakseconds == 0) {
//                                "1"
//                            } else {
//                                (record.totalDurationSeconds / record.totalbreakseconds)
//                            }
//                        } : 1",
//                        style = MaterialTheme.typography.titleLarge,
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
//                }
//            }
//        }
//    }
//}
