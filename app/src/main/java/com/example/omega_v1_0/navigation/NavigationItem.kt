package com.example.omega_v1_0.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a single item displayed in the Omega Navigation Bar.
 */
data class NavigationItem(

    /**
     * Label displayed below the icon.
     */
    val label: String,

    /**
     * Icon displayed in the navigation bar.
     */
    val icon: ImageVector,

    /**
     * Event emitted when the item is clicked.
     */
    val action: NavigationAction,

    /**
     * Whether the item can currently be interacted with.
     */
    val enabled: Boolean = true,

    /**
     * Optional badge count.
     *
     * Reserved for future use.
     */
    val badgeCount: Int? = null
)