package com.example.omega_v1_0.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.omega_v1_0.models.SessionStatusBarModel
import com.example.omega_v1_0.models.SessionType

@Composable
fun SessionStatusBar(
    model: SessionStatusBarModel?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .padding(
            start = 16.dp,
            end = 16.dp,
            bottom = 82.dp
        )
) {
    if (model == null) return

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween
            ) {

                Text(
                    text = model.sessionType.displayName(),
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = "🟢",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(0.dp,2.dp,8.dp,0.dp)
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = buildSessionTitle(model),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
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



@Preview(showBackground = true)
@Composable
private fun SessionStatusBarPreview() {

    SessionStatusBar(

        model = SessionStatusBarModel(

            sessionType = SessionType.UNPLANNED,
            parentTitle = "Android App",
            sessionName = "Login Screen Design"

        ),

        onClick = {}

    )

}