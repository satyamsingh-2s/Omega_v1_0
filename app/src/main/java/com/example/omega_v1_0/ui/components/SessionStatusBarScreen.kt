package com.example.omega_v1_0.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.models_enums.SessionStatusBarModel
import com.example.omega_v1_0.models_enums.SessionType
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ---------------- Premium Dark Green Palette ----------------

private val StatusBarBackground = Color(0xE61A241F)     // 90% alpha
private val StatusBarBorder = Color(0xFF335C45)

private val GreenPrimary = Color(0xFF57D68D)
private val GreenAccent = Color(0xFF35C96E)

private val DeskButtonBackground = Color(0xFF24352D)

private val PrimaryText = Color(0xFFF2F5F3)
private val SecondaryText = Color(0xFFB9C7BE)


@Composable
fun SessionStatusBar(
    model: SessionStatusBarModel?,
    onDeskOmegaClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 82.dp
        )
) {

    var deskOmegaEnabled by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    if (model == null) return

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.dp,
            color = StatusBarBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = StatusBarBackground
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ---------------- Left Click Area ----------------

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClick() }
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(GreenAccent)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = model.sessionType.displayName(),
                        style = MaterialTheme.typography.labelLarge,
                        color = GreenPrimary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = buildSessionTitle(model),
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // ---------------- Desk Omega Button ----------------

            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = DeskButtonBackground
            ) {

                IconButton(
                    onClick = {

                        if (!deskOmegaEnabled) return@IconButton
                        deskOmegaEnabled = false
                        onDeskOmegaClick()
                        scope.launch {
                            delay(500)
                            deskOmegaEnabled = true
                        }
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = "Desk Omega",
                        tint = GreenPrimary
                    )
                }
            }
        }
    }
}

private fun buildSessionTitle(
    model: SessionStatusBarModel
): String {

    return if (model.sessionName.isNullOrBlank()) {
        model.parentTitle
    } else {
        "${model.parentTitle} / ${model.sessionName}"
    }
}

private fun SessionType.displayName(): String =
    when (this) {
        SessionType.UNPLANNED -> "Unplanned"
        SessionType.PLANNED -> "Planned"
        SessionType.DAILY_RECORD -> "Daily"
    }

@Preview(showBackground = true, backgroundColor = 0xFF101010)
@Composable
private fun SessionStatusBarPreview() {

    SessionStatusBar(
        model = SessionStatusBarModel(
            sessionType = SessionType.UNPLANNED,
            parentTitle = "Android App",
            sessionName = "Login Screen Design"
        ),
        onDeskOmegaClick = {},
        onClick = {}
    )
}