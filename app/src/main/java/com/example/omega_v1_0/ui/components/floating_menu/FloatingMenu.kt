package com.example.omega_v1_0.ui.components.floating_menu

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun FloatingMenu(

    expanded: Boolean,

    onDismiss: () -> Unit,
    alignment: Alignment = FloatingMenuDefaults.PopupAlignment,
    offset: IntOffset = FloatingMenuDefaults.PopupOffset,

    content: @Composable ColumnScope.() -> Unit

) {

    if (!expanded) return

    Popup(

        alignment = alignment,

        offset = offset,

        onDismissRequest = onDismiss,

        properties = PopupProperties(

            focusable = true,

            dismissOnBackPress = true,

            dismissOnClickOutside = true

        )

    ) {

        FloatingMenuCard(

            content = content

        )
    }
}
@Preview(showBackground = true)
@Composable
private fun FloatingMenuPreview() {

    FloatingMenu(

        expanded = true,

        onDismiss = {}

    ) {

        Text("Daily")

        Text("Unplanned")

        Text("Planned")

    }

}