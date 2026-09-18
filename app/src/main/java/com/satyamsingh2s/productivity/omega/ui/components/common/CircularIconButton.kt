package com.satyamsingh2s.productivity.omega.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A guaranteed-circular icon button.
 *
 * Why this fixes the "pill instead of circle" issue:
 * Wrapping Material's `IconButton` is the usual culprit for
 * non-circular buttons - `IconButton` carries its own
 * `defaultMinSize` (48.dp minWidth, different minHeight behavior in
 * some Material3 versions) that can win over a `Modifier.size(...)`
 * placed on it, especially once font/density scaling is applied,
 * producing a stretched pill instead of a circle.
 *
 * This version sidesteps that entirely: it is a plain `Box` with an
 * explicit EQUAL width/height (`Modifier.size(size)`), clipped to
 * `CircleShape` BEFORE any background/click modifiers are applied,
 * and `clickable` is used directly instead of `IconButton`. Because
 * width and height are always set from the same single `size` value,
 * it is impossible for this composable to render as anything but a
 * circle, regardless of parent layout, font scale, or Material theme
 * defaults.
 */
@Composable
fun CircularIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    backgroundColor: Color = Color.Unspecified,
    iconTint: Color = LocalContentColor.current,
    iconSize: Dp = size * 0.42f, // proportional so the glyph scales with the button
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size)                 // width == height, always -> guarantees a circle
            .clip(CircleShape)          // clip BEFORE background so ripple/background can't bleed past the circle
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, radius = size / 2),
                enabled = enabled,
                onClickLabel = contentDescription,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(iconSize)
        )
    }
}