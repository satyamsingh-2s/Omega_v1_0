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
    Font(R.font.satosh_regular)
)

// App Typography

val Typography2 = Typography().run {

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

// ---------------- Font Family ----------------

val SatoshiFont = FontFamily(
    Font(R.font.satosh_regular)
)

// ---------------- App Typography ----------------
val Typography = Typography().run {

    copy(

        // Display
        displayLarge = displayLarge.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.sp
        ),

        displayMedium = displayMedium.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.sp
        ),

        displaySmall = displaySmall.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.sp
        ),

        // Headlines
        headlineLarge = headlineLarge.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.3.sp
        ),

        headlineMedium = headlineMedium.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.3.sp
        ),

        headlineSmall = headlineSmall.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.3.sp
        ),

        // Titles
        titleLarge = titleLarge.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.4.sp
        ),

        titleMedium = titleMedium.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.4.sp
        ),

        titleSmall = titleSmall.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.4.sp
        ),

        // Body
        bodyLarge = bodyLarge.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.25.sp
        ),

        bodyMedium = bodyMedium.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.25.sp
        ),

        bodySmall = bodySmall.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.25.sp
        ),

        // Labels
        labelLarge = labelLarge.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.8.sp
        ),

        labelMedium = labelMedium.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.7.sp
        ),

        labelSmall = labelSmall.copy(
            fontFamily = SatoshiFont,
            letterSpacing = 0.6.sp
        )

    )
}

// ------------ personal font -----------
// ---------------- Font Families ----------------

val Font1 = FontFamily(
    Font(R.font.circular_std)
)

val Font2 = FontFamily(
    Font(R.font.satosh_regular)
)

// ---------------- App Typography -----------------------------------

val TypographyPersonal = Typography().run {

    copy(

        // =======================
        // Display (Font 1)
        // =======================

        displayLarge = displayLarge.copy(
            fontFamily = Font1,
            letterSpacing = 0.sp
        ),

        displayMedium = displayMedium.copy(
            fontFamily = Font1,
            letterSpacing = 0.sp
        ),

        displaySmall = displaySmall.copy(
            fontFamily = Font1,
            letterSpacing = 0.sp
        ),

        // =======================
        // Headlines (Font 1)
        // =======================

        headlineLarge = headlineLarge.copy(
            fontFamily = Font1,
            letterSpacing = 0.3.sp
        ),

        headlineMedium = headlineMedium.copy(
            fontFamily = Font1,
            letterSpacing = 0.3.sp
        ),

        headlineSmall = headlineSmall.copy(
            fontFamily = Font1,
            letterSpacing = 0.3.sp
        ),

        // =======================
        // Titles (Font 1)
        // =======================

        titleLarge = titleLarge.copy(
            fontFamily = Font1,
            letterSpacing = 0.4.sp
        ),

        titleMedium = titleMedium.copy(
            fontFamily = Font1,
            letterSpacing = 0.4.sp
        ),

        titleSmall = titleSmall.copy(
            fontFamily = Font1,
            letterSpacing = 0.4.sp
        ),

        // =======================
        // Body (Font 2)
        // =======================

        bodyLarge = bodyLarge.copy(
            fontFamily = Font2,
            letterSpacing = 0.2.sp
        ),

        bodyMedium = bodyMedium.copy(
            fontFamily = Font2,
            letterSpacing = 0.2.sp
        ),

        bodySmall = bodySmall.copy(
            fontFamily = Font2,
            letterSpacing = 0.2.sp
        ),

        // =======================
        // Labels (Font 2)
        // =======================

        labelLarge = labelLarge.copy(
            fontFamily = Font2,
            letterSpacing = 0.7.sp
        ),

        labelMedium = labelMedium.copy(
            fontFamily = Font2,
            letterSpacing = 0.6.sp
        ),

        labelSmall = labelSmall.copy(
            fontFamily = Font2,
            letterSpacing = 0.5.sp
        )

    )
}

val Font3 = FontFamily(
    Font(R.font.nothing_ndot_58)
)
val StopwatchTextStyle = TextStyle(
    fontFamily = Font3,
    fontSize = 63.sp,
    letterSpacing = 3.sp,
    lineHeight = 64.sp

)

// *****************************************************888888888888888