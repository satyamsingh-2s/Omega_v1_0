package com.example.omega_v1_0.navigation.workspace

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WorkspaceMenuItem(

    title: String,

    icon: ImageVector,

    workspace: Workspace,

    currentWorkspace: Workspace,

    onClick: (Workspace) -> Unit

) {

    val selected = workspace == currentWorkspace

    Surface(

        modifier = Modifier.fillMaxWidth(),

        onClick = {
            onClick(workspace)
        },

        shape = RoundedCornerShape(12.dp),

        color =
            if (selected)
                MaterialTheme.colorScheme.primaryContainer
            else
                Color.Transparent,

        tonalElevation =
            if (selected) 2.dp else 0.dp

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),

            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.Start

        ) {

            Icon(

                imageVector = icon,

                contentDescription = title,

                modifier = Modifier.size(22.dp),

                tint =
                    if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant

            )

            Spacer(
                modifier = Modifier.size(12.dp)
            )

            Text(

                text = title,

                style = MaterialTheme.typography.bodyLarge,

                fontWeight =
                    if (selected)
                        FontWeight.SemiBold
                    else
                        FontWeight.Normal,

                color =
                    if (selected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface

            )

        }

    }

}

@Preview(showBackground = true)
@Composable
private fun WorkspaceMenuItemPreview() {

    MaterialTheme {

        WorkspaceMenuItem(

            title = "Daily",

            icon = Icons.Outlined.Today,

            workspace = Workspace.DAILY_RECORD,

            currentWorkspace = Workspace.DAILY_RECORD,

            onClick = {}

        )

    }

}