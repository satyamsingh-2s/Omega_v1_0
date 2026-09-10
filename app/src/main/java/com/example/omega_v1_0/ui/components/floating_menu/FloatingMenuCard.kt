package com.example.omega_v1_0.ui.components.floating_menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun FloatingMenuCard(

    content: @Composable ColumnScope.() -> Unit

) {

    Card(

        shape = FloatingMenuDefaults.Shape,

        elevation = CardDefaults.cardElevation(
            defaultElevation = FloatingMenuDefaults.Elevation
        )

    ) {

        Column(

            modifier = Modifier.padding(
                FloatingMenuDefaults.ContentPadding
            ),

            content = content

        )

    }

}