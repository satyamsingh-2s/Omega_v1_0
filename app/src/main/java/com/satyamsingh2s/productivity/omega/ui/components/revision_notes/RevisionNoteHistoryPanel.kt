package com.satyamsingh2s.productivity.omega.ui.components.revision_notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.models_enums.RevisionNoteItem
import com.satyamsingh2s.productivity.omega.ui.utils.formatDate
import com.satyamsingh2s.productivity.omega.ui.utils.formatDuration2

@Composable
fun RevisionHistoryPanel(
    notes: List<RevisionNoteItem>,
    onNoteClick: (RevisionNoteItem) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            )
    ) {

        Text(
            text = "Revision History",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        if (notes.isEmpty()) {

            EmptyRevisionHistory()

        } else {

            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = notes,
                    key = { it.sessionId }
                ) { note ->

                    RevisionNoteCard(
                        note = note,
                        onClick = {
                            onNoteClick(note)
                        }
                    )

                }

            }

        }
    }

}

@Composable
fun RevisionNoteCard(
    note: RevisionNoteItem,
    onClick: () -> Unit
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        onClick = onClick,

        shape = MaterialTheme.shapes.large,

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {

            Text(
                text = note.sessionName ?: "Unnamed Session",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = formatDate(note.sessionStartTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = formatDuration2(note.durationSeconds),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = note.summary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

        }

    }
}

@Composable
private fun EmptyRevisionHistory() {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "No revision notes yet.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Complete a session and save a revision note to build your revision history.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    }

}
