package com.example.omega_v1_0.ui.components.floating_menu

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

object FloatingMenuDefaults {

    /**
     * Default shape used by all floating menus.
     */
    val Shape = RoundedCornerShape(20.dp)

    /**
     * Default popup alignment.
     * Individual features (Workspace Switcher, Sort Menu, etc.)
     * can override this when needed.
     */
    val PopupAlignment: Alignment = Alignment.BottomEnd

    /**
     * Neutral default offset.
     * Each feature should provide its own offset.
     */
    val PopupOffset = IntOffset.Zero

    /**
     * Default card elevation.
     */
    val Elevation = 10.dp

    /**
     * Padding inside the floating card.
     */
    val ContentPadding = 8.dp
}