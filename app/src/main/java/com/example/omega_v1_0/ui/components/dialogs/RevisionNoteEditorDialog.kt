package com.example.omega_v1_0.ui.components.dialogs

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.ui.components.revision_notes.AttachmentThumbnail
import com.example.omega_v1_0.ui.model.AttachmentUiModel
import com.example.omega_v1_0.ui.model.RevisionNoteMenuAction

private const val MAX_SUMMARY_LENGTH = 1000
private val MIN_EDITOR_HEIGHT = 220.dp

@Composable
fun RevisionNoteEditorDialog(
    summary: String,
    attachments: List<AttachmentUiModel>,
    onSummaryChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    onMenuAction: (RevisionNoteMenuAction) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteAttachment: (Long) -> Unit,

) {

    var showMenu by remember {
        mutableStateOf(false)
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var selectedAttachment by remember {
        mutableStateOf<AttachmentUiModel?>(null)
    }

    AlertDialog(

        modifier = modifier,

        onDismissRequest = onDismiss,

        title = {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween,

                verticalAlignment =
                    Alignment.CenterVertically

            ) {

                Text(

                    text = "Revision Note",

                    style = MaterialTheme.typography.titleLarge
                )

                Box {

                    IconButton(

                        onClick = {

                            showMenu = true
                        }

                    ) {

                        Icon(

                            imageVector =
                                Icons.Default.MoreVert,

                            contentDescription = "More options"
                        )
                    }

                    DropdownMenu(

                        expanded = showMenu,

                        onDismissRequest = {

                            showMenu = false
                        }

                    ) {

                        DropdownMenuItem(

                            text = {

                                Text("Add Image")
                            },

                            onClick = {

                                showMenu = false

                                onMenuAction(
                                    RevisionNoteMenuAction.ADD_IMAGE
                                )
                            }
                        )
                    }
                }
            }
        },

        text = {

            Column {

                OutlinedTextField(

                    value = summary,

                    onValueChange = { newValue ->

                        if (newValue.length <= MAX_SUMMARY_LENGTH) {

                            onSummaryChange(newValue)
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(
                            min = MIN_EDITOR_HEIGHT
                        ),

                    placeholder = {

                        Text(
                            text = "What did you accomplish in this session?"
                        )
                    },

                    singleLine = false
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.End,

                    verticalAlignment =
                        Alignment.CenterVertically

                ) {

                    if (attachments.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(
                            horizontalArrangement =
                                Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 2.dp)

                        ) {
                            items(
                                items = attachments,
                                key = { it.id }
                            ) { attachment ->

                                AttachmentThumbnail(
                                    attachment = attachment,
                                    onClick = {
                                        selectedImageUri =
                                            attachment.imageUri
                                    },
                                    onLongClick = {
                                        selectedAttachment =
                                            attachment
                                    }

                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Text(
                        text = "${summary.length} / $MAX_SUMMARY_LENGTH",
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.End
                    )
                }
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")
            }
        },

        confirmButton = {

            Button(

                onClick = onSave

            ) {

                Text("Save")
            }
        }
    )

    selectedImageUri?.let { imageUri ->
        ImageViewerDialog(
            imageUri = imageUri,
            onDismiss = {
                selectedImageUri = null
            }
        )
    }

    selectedAttachment?.let { attachment ->

        AlertDialog(

            onDismissRequest = {

                selectedAttachment = null
            },

            title = {

                Text("Delete Image")
            },

            text = {

                Text("Are you sure you want to delete this image?")
            },

            confirmButton = {

                Button(

                    onClick = {

                        onDeleteAttachment(attachment.id)
                        selectedAttachment = null
                    }

                ) {

                    Text("Delete")
                }
            },

            dismissButton = {

                TextButton(

                    onClick = {

                        selectedAttachment = null
                    }

                ) {

                    Text("Cancel")
                }
            }
        )
    }



}