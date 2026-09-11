package com.satyamsingh2s.productivity.omega.ui.components.revision_notes

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.satyamsingh2s.productivity.omega.ui.model.AttachmentUiModel

private val THUMBNAIL_SIZE = 72.dp
private val THUMBNAIL_CORNER_RADIUS = 12.dp

@Composable
fun AttachmentThumbnail(

    attachment: AttachmentUiModel,

    onClick: () -> Unit,

    modifier: Modifier = Modifier,

    onLongClick: (() -> Unit)? = null,

    ) {

    Card(

        modifier = modifier
            .size(THUMBNAIL_SIZE)
            .combinedClickable(

                onClick = onClick,

                onLongClick = {
                    onLongClick?.invoke()
                }
            ),

        shape = RoundedCornerShape(
            THUMBNAIL_CORNER_RADIUS
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ) {

        AsyncImage(

            model = attachment.imageUri,

            contentDescription = null,

            modifier = Modifier
                .size(THUMBNAIL_SIZE)
                .clip(
                    RoundedCornerShape(
                        THUMBNAIL_CORNER_RADIUS
                    )
                ),

            contentScale = ContentScale.Crop
        )
    }
}