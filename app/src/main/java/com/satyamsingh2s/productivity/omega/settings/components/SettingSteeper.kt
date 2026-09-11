package com.satyamsingh2s.productivity.omega.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SettingStepper(
    title: String,
    value: Int,
    min: Int,
    max: Int,
    step: Int = 1,
    suffix: String = "",
    modifier: Modifier = Modifier,
    onValueChanged: (Int) -> Unit
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                FilledTonalIconButton(
                    enabled = value > min,
                    onClick = {
                        onValueChanged(
                            (value - step).coerceAtLeast(min)
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease $title"
                    )
                }

                Text(
                    text = "$value $suffix",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                FilledTonalIconButton(
                    enabled = value < max,
                    onClick = {
                        onValueChanged(
                            (value + step).coerceAtMost(max)
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase $title"
                    )
                }
            }
        }
    }
}