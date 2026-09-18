package com.satyamsingh2s.productivity.omega.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.satyamsingh2s.productivity.omega.ui.model.PersonalizationConfig

@Composable
fun PersonalizationCard(
    config: PersonalizationConfig,
    selectedValue: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.5f
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = config.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = selectedValue ?: "Not selected",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (selectedValue != null) {
                    FontWeight.Medium
                } else {
                    FontWeight.Normal
                },
                color = if (selectedValue != null) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.7f
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizationBottomSheet(
    config: PersonalizationConfig,
    initialValue: String?,
    onDismiss: () -> Unit,
    onValueSelected: (String) -> Unit
) {

    var customValue by remember {
        mutableStateOf(
            if (
                initialValue != null &&
                !config.predefinedChoices.contains(initialValue)
            ) {
                initialValue
            } else {
                ""
            }
        )
    }

    var selectedPredefined by remember {
        mutableStateOf(
            if (config.predefinedChoices.contains(initialValue)) {
                initialValue
            } else {
                null
            }
        )
    }

    var customFieldFocused by remember {
        mutableStateOf(false)
    }

    val customCounterVisible =
        customValue.length >= 6

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (customFieldFocused) {
                        Modifier.fillMaxHeight()
                    } else {
                        Modifier
                    }
                )
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // =================================================================
            // HEADER
            // =================================================================

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {

                Text(
                    text = config.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = config.question,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            // =================================================================
            // PREDEFINED OPTIONS
            // =================================================================

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                config.predefinedChoices.forEach { choice ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                // Selecting a predefined option
                                // immediately finalises the selection.
                                selectedPredefined = choice
                                customValue = ""

                                onValueSelected(choice)
                                onDismiss()
                            },

                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (selectedPredefined == choice) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.35f
                                    )
                                }
                        ),

                        shape = RoundedCornerShape(10.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 14.dp,
                                    vertical = 11.dp
                                ),

                            verticalAlignment =
                                Alignment.CenterVertically,

                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = choice,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight =
                                    if (selectedPredefined == choice) {
                                        FontWeight.Medium
                                    } else {
                                        FontWeight.Normal
                                    },
                                color =
                                    if (selectedPredefined == choice) {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.onSurface
                                    }
                            )

                            if (selectedPredefined == choice) {
                                Text(
                                    text = "✓",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }


            // =================================================================
            // CUSTOM
            // =================================================================

            Text(
                text = "Custom",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = customValue,

                onValueChange = { value ->

                    if (value.length <= 12) {
                        customValue = value
                        selectedPredefined = null
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { state ->
                        customFieldFocused = state.isFocused
                    },

                singleLine = true,

                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text(
                        text = "Enter your own"
                    )
                },

                supportingText = {

                    if (customCounterVisible) {

                        Text(
                            text = "${customValue.length}/12",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                }
            )


            // =================================================================
            // DONE — ONLY FOR CUSTOM INPUT
            // =================================================================

            if (customValue.isNotBlank()) {

                Button(
                    onClick = {

                        val value = customValue.trim()

                        if (value.isNotBlank()) {
                            onValueSelected(value)
                            onDismiss()
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp),

                    shape = RoundedCornerShape(10.dp)
                ) {

                    Text(
                        text = "Done",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}