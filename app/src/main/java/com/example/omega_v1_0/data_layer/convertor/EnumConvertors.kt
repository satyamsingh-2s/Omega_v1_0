package com.example.omega_v1_0.data_layer.convertor

import androidx.core.view.WindowInsetsCompat
import androidx.room.TypeConverter
import com.example.omega_v1_0.models.Complexity
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.models.PhaseType
import com.example.omega_v1_0.models.Scope

class EnumConvertors {

    // --- PhaseType ---
    @TypeConverter
    fun fromPhaseType(value: PhaseType): String = value.name

    @TypeConverter
    fun toPhaseType(value: String): PhaseType= PhaseType.valueOf(value)

    // ---- Experience ---
    @TypeConverter
    fun fromExperience(value: Experience): String = value.name

    @TypeConverter
    fun toExperience(value: String): Experience = Experience.valueOf(value)

    // ---- Scope ----
    @TypeConverter
    fun fromScope(value: Scope): String = value.name

    @TypeConverter
    fun toScope(value: String): Scope = Scope.valueOf(value)

    // ---- Complexity ----
    @TypeConverter
    fun fromComplexity(value: Complexity): String = value.name

    @TypeConverter
    fun toComplexity(value: String): Complexity = Complexity.valueOf(value)

    // -> now tell database that about the EnumConvertor file, as Database.kt act




}