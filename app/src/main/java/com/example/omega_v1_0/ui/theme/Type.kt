package com.example.omega_v1_0.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.omega_v1_0.R

// Set of Material typography styles to start with
val Typography1 = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )


    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

// Nothing Font Family
val NothingFont = FontFamily(
    Font(R.font.nothing_ndot_58)
)

// App Typography

val Typography = Typography().run {

    copy(

        displayLarge = displayLarge.copy(fontFamily = NothingFont),
        displayMedium = displayMedium.copy(fontFamily = NothingFont),
        displaySmall = displaySmall.copy(fontFamily = NothingFont),

        headlineLarge = headlineLarge.copy(fontFamily = NothingFont),
        headlineMedium = headlineMedium.copy(fontFamily = NothingFont),
        headlineSmall = headlineSmall.copy(fontFamily = NothingFont),

        titleLarge = titleLarge.copy(fontFamily = NothingFont),
        titleMedium = titleMedium.copy(fontFamily = NothingFont),
        titleSmall = titleSmall.copy(fontFamily = NothingFont),

        bodyLarge = bodyLarge.copy(fontFamily = NothingFont),
        bodyMedium = bodyMedium.copy(fontFamily = NothingFont),
        bodySmall = bodySmall.copy(fontFamily = NothingFont),

        labelLarge = labelLarge.copy(fontFamily = NothingFont),
        labelMedium = labelMedium.copy(fontFamily = NothingFont),
        labelSmall = labelSmall.copy(fontFamily = NothingFont)

    )
}