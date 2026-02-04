package com.example.omega_v1_0.ui.model

import java.util.Date

/**
 * Represents a single project item for the "Show All Projects" UI list.
 *
 * This is a UI-specific model, containing only the data needed by the composable,
 * which decouples the UI from the database `ProjectEntity`.
 */
data class AllProjectsUiModel(
    val id: Long,
    val projectName: String,
    val createdAt: Date
)