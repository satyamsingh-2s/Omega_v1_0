package com.satyamsingh2s.productivity.omega.ui.components.dialogs

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

private val IMAGE_VIEWER_BACKGROUND = Color.Black.copy(alpha = 0.6f)
private val CLOSE_BUTTON_PADDING = 12.dp
private val CLOSE_ICON_COLOR = Color.White
private const val PAN_SPEED = 1.5f

@Composable
fun ImageViewerDialog(
    imageUri: Uri,
    onDismiss: () -> Unit

) {

    var scale by remember {
        mutableFloatStateOf(1f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->

        val newScale =
            (scale * zoomChange)
                .coerceIn(1f, 5f)

        scale = newScale

        if (scale > 1f) {

            offset += Offset(
                x = panChange.x * PAN_SPEED,
                y = panChange.y * PAN_SPEED
            )

        } else {

            offset = Offset.Zero
        }
    }

    Dialog(
        onDismissRequest = onDismiss

    ) {
        Box(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    IMAGE_VIEWER_BACKGROUND
                )
                .pointerInput(Unit) {

                detectTapGestures(

                    onDoubleTap = {

                        if (scale > 1f) {

                            scale = 1f
                            offset = Offset.Zero

                        } else {

                            scale = 2f
                        }
                    }
                )
            }

        ) {

            AsyncImage(

                model = imageUri,

                contentDescription = null,

                Modifier
                    .fillMaxSize()

                    .graphicsLayer {

                        scaleX = scale
                        scaleY = scale

                        translationX = offset.x
                        translationY = offset.y
                    }

                    .transformable(transformableState)

                    .pointerInput(Unit) {

                        detectTapGestures(

                            onDoubleTap = {

                                if (scale > 1f) {

                                    scale = 1f
                                    offset = Offset.Zero

                                } else {

                                    scale = 2f
                                }
                            }
                        )
                    },

                contentScale = ContentScale.Fit
            )

            IconButton(

                onClick = onDismiss,

                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        CLOSE_BUTTON_PADDING
                    )

            ) {

                Icon(

                    imageVector = Icons.Default.Close,

                    contentDescription = "Close",

                    tint = CLOSE_ICON_COLOR
                )
            }
        }
    }
}