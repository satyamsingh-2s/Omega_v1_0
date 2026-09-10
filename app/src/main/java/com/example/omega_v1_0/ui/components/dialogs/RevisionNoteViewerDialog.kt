package com.example.omega_v1_0.ui.components.dialogs

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.models_enums.RevisionNoteItem
import com.example.omega_v1_0.ui.components.revision_notes.AttachmentThumbnail
import com.example.omega_v1_0.ui.model.AttachmentUiModel
import com.example.omega_v1_0.ui.utils.formatDate
import com.example.omega_v1_0.ui.utils.formatDuration2

@Composable
fun RevisionNoteViewerDialog(

    note: RevisionNoteItem,

    attachments: List<AttachmentUiModel>,

    onEdit: () -> Unit,

    onDismiss: () -> Unit,

    modifier: Modifier = Modifier

) {

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    AlertDialog(

        modifier = modifier,

        onDismissRequest = onDismiss,

        title = {

            Text(
                text = note.sessionName ?: "Unnamed Session",
                style = MaterialTheme.typography.titleLarge
            )

        },

        text = {

            Column {

                MetadataRow(
                    label = "Date",
                    value = formatDate(note.sessionStartTime)
                )

                Spacer(modifier = Modifier.height(4.dp))

                MetadataRow(
                    label = "Duration",
                    value = formatDuration2(note.durationSeconds)
                )

                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Revision Note",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                SelectionContainer {

                    Text(
                        text = if (note.summary.isBlank()) {
                            "No revision note available."
                        } else {
                            note.summary
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                if (attachments.isNotEmpty()) {

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Images",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(
                            items = attachments,
                            key = { it.id }
                        ) { attachment ->

                            AttachmentThumbnail(

                                attachment = attachment,

                                onClick = {
                                    selectedImageUri = attachment.imageUri
                                }

                            )
                        }
                    }
                }
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Close")
            }

        },

        confirmButton = {

            Button(
                onClick = onEdit
            ) {

                Text("Edit")
            }

        }
    )

    selectedImageUri?.let { uri ->

        ImageViewerDialog(

            imageUri = uri,

            onDismiss = {
                selectedImageUri = null
            }
        )
    }
}

@Composable
private fun MetadataRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}