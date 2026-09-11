package com.satyamsingh2s.productivity.omega.navigation.workspace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.unit.dp

@Composable
fun WorkspaceSwitcherPopup(
    expanded: Boolean,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit

) {
    val PopupAlignment = Alignment.BottomEnd

    if (!expanded) return
    Popup(
        alignment = Alignment.BottomEnd,
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )

    ) {

        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )

        ) {

            Column(

                modifier = Modifier
                    .wrapContentSize(),

                content = content

            )

        }

    }

}
@Preview(showBackground = true)
@Composable
private fun WorkspaceSwitcherPopupPreview() {

    WorkspaceSwitcherPopup(
        expanded = true,
        onDismiss = {}

    ) {

        Text("Daily")

        Text("Unplanned")

        Text("Planned")

    }

}