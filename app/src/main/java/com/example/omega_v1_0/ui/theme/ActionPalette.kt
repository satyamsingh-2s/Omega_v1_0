package com.example.omega_v1_0.ui.theme

import androidx.compose.ui.graphics.Color

object AccentPalette {

    const val PALETTE_SIZE = 10

    private val accents = listOf(


        Color(0xFF3F51B5), // 0 - Indigo
        Color(0xFF2E7D32), // 1 - Green
        Color(0xFF00897B), // 2 - Teal
        Color(0xFFEF6C00), // 3 - Orange
        Color(0xFF039BE5), // 4 - Sky Blue
        Color(0xFF6D4C41), // 5 - Brown
        Color(0xFFD81B60), // 6 - Pink
        Color(0xFF8E24AA), // 7 - Purple
        Color(0xFFF9A825), // 8 - Amber
        Color(0xFF546E7A)  // 9 - Blue Grey
    )

    fun getAccent(index: Int): Color {
        require(index in accents.indices) {
            "Invalid accent index: $index"
        }
        return accents[index]
    }
}