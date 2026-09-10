package com.example.omega_v1_0.navigation.workspace

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

object WorkspaceSwitcherDefaults {

    /**
     * Popup alignment relative to the screen.
     * Later this will be replaced by dynamic anchoring.
     */
    val PopupAlignment: Alignment = Alignment.BottomEnd

    /**
     * Temporary fixed offset.
     * This will be removed once the popup is anchored
     * to the Workspace button.
     */
    val PopupOffset = IntOffset(
        x = -16,
        y = -80
    )

    /**
     * Width of the switcher popup.
     */
    val Width = 220.dp

    /**
     * Space between menu items.
     */
    val ItemSpacing = 4.dp
}