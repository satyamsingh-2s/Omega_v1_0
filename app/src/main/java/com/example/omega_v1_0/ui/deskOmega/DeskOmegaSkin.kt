package com.example.omega_v1_0.ui.deskOmega

import androidx.compose.ui.graphics.Color

// ================= ENUM =================

enum class DeskOmegaSkin {

    OMEGA_DARK,

    PAPER,

    NIGHT_BLUE,

    AMOLED,
    AOD

}

// ================= DATA CLASS =================

data class DeskOmegaSkinColors(

    val background: Color,

    val content: Color,

    val secondary: Color,

    val accent: Color

)

// ================= SKIN PROVIDER =================

fun getDeskOmegaSkin(

    skin: DeskOmegaSkin

): DeskOmegaSkinColors {

    return when (skin) {

        // ---------- Omega Dark ----------

        DeskOmegaSkin.OMEGA_DARK ->

            DeskOmegaSkinColors(

                background = Color(0xFF141414),

                content = Color(0xFFE4E4E4),

                secondary = Color(0xFF9D9D9D),

                accent = Color(0xFF8B7FBF)

            )

        // ---------- Paper ----------

        DeskOmegaSkin.PAPER ->

            DeskOmegaSkinColors(

                background = Color(0xFFF4F1EA),

                content = Color(0xFF262626),

                secondary = Color(0xFF666666),

                accent = Color(0xFF7A6FA6)

            )

        // ---------- Night Blue ----------

        DeskOmegaSkin.NIGHT_BLUE ->

            DeskOmegaSkinColors(

                background = Color(0xFF0F1720),

                content = Color(0xFFE4E4E4),

                secondary = Color(0xFF95A1B2),

                accent = Color(0xFF8B7FBF)

            )

        // ---------- AMOLED ----------

        DeskOmegaSkin.AMOLED ->

            DeskOmegaSkinColors(

                background = Color(0xFF000000),

                content = Color(0xFFD9D9D9),

                secondary = Color(0xFF7A7A7A),

                accent = Color(0xFF8B7FBF)

            )

        // ------------ AOD -------------
        DeskOmegaSkin.AOD ->

            DeskOmegaSkinColors(

                background = Color(0xFF000000),

                content = Color(0xFF4D4D4D),

                secondary = Color(  0xFF4D4D4D),

                accent = Color(0xFF5F5F5F)



            )
    }
}