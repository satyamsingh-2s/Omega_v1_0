package com.example.omega_v1_0.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// ------- MY DARK THEMES ======================
private val OmegaDarkColorScheme = darkColorScheme(

    // Accent
    primary = Color(0xFF8B7FBF),
    // Backgrounds
    background = Color(0xFF141414),
    surface = Color(0xFF1B1B1B),
    surfaceVariant = Color(0xFF232323),
    // Text
    onPrimary = Color(0xFFE4E4E4),
    onBackground = Color(0xFFE4E4E4),
    onSurface = Color(0xFFE4E4E4),
    onSurfaceVariant = Color(0xFF9D9D9D),
    // Extras
    outline = Color(0xFF2A2A2A)
)

@Composable
fun OmegaDarkTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = OmegaDarkColorScheme,
        content = content,
        typography = Typography

    )
}
// xxxxxxxxxxxxxxx end xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

@Composable
fun Omega_v1_0Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// *********************************************************************8
// ------- MY RED OMEGA THEME ======================

private val OmegaRedColorScheme = darkColorScheme(

    // Accent
    primary = Color(0xFF8A2E2E),

    // Backgrounds
    background = Color(0xFF2A1111),
    surface = Color(0xFF381818),
    surfaceVariant = Color(0xFF4A2323),

    // Text
    onPrimary = Color(0xFF101010),
    onBackground = Color(0xFF101010),
    onSurface = Color(0xFF101010),
    onSurfaceVariant = Color(0xFF2D2D2D),

    // Extras
    outline = Color(0xFF5B3535),

    // Optional Material colors
    primaryContainer = Color(0xFFB65A5A),
    secondary = Color(0xFF6E3030),
    secondaryContainer = Color(0xFF5A2626)

)

@Composable
fun OmegaRedTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = OmegaMinimalBWDarkColorScheme,
        typography = TypographyPersonal,
        content = content
    )
}
// *************************************************************************************8888
// =========================
// Omega Coral Theme
// =========================

private val OmegaCoralColorScheme = darkColorScheme(

    // Primary Accent
    primary = Color(0xFFDF6C4F),

    // Secondary Accent
    secondary = Color(0xFFD14836),

    // Containers
    primaryContainer = Color(0xFFF08B6D),
    secondaryContainer = Color(0xFFB9392D),

    // Backgrounds
    background = Color(0xFFE29683),
    surface = Color(0xFFDF6C4F),
    surfaceVariant = Color(0xFFC54434),

    // Text
    onPrimary = Color(0xFFFFF5E8),
    onSecondary = Color(0xFFFFF5E8),

    onBackground = Color(0xFFFFF5E8),
    onSurface = Color(0xFFFFF5E8),

    onSurfaceVariant = Color(0xFFF2D7C7),

    // Borders
    outline = Color(0xFFB9392D),

    // Error
    error = Color(0xFF8E1F1F),

    // Optional
    tertiary = Color(0xFFF5E7D2),
    tertiaryContainer = Color(0xFFECC9B0)

)

private val OmegaMinimalBWColorScheme = darkColorScheme(
    // ================= Accent =================

    primary = Color(0xFF4F4A45),

// ================= Backgrounds =================

            background = Color(0xFFF4F1EA),

            surface = Color(0xFFECE7DE),

            surfaceVariant = Color(0xFFE1DBD0),

// ================= Text =================

            onPrimary = Color(0xFFF8F5EF),

            onBackground = Color(0xFF2D2A27),

            onSurface = Color(0xFF2D2A27),

            onSurfaceVariant = Color(0xFF6A645E),

// ================= Extras =================

            outline = Color(0xFFC8C1B7),

// Optional

            primaryContainer = Color(0xFFD9D2C8),

            secondary = Color(0xFF8C857C),

            secondaryContainer = Color(0xFFD5CEC3),
)

private val OmegaMinimalBWDarkColorScheme = darkColorScheme(

    // ================= Accent =================

    primary = Color(0xFFB5ADA3),

    // ================= Backgrounds =================

    background = Color(0xFF181715),

    surface = Color(0xFF22211F),

    surfaceVariant = Color(0xFF2B2926),

    // ================= Text =================

    onPrimary = Color(0xFF181715),

    onBackground = Color(0xFFE8E2D9),

    onSurface = Color(0xFFE8E2D9),

    onSurfaceVariant = Color(0xFFB5ADA3),

    // ================= Extras =================

    outline = Color(0xFF3B3936),

    // Optional

    primaryContainer = Color(0xFF3C3934),

    secondary = Color(0xFF8F887E),

    secondaryContainer = Color(0xFF33312D)

)