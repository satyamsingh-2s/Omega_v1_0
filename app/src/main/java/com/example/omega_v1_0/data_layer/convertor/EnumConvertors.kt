package com.example.omega_v1_0.data_layer.convertor

import androidx.room.TypeConverter
import com.example.omega_v1_0.models.Complexity
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.models.PhaseType
import com.example.omega_v1_0.models.Scope
import com.example.omega_v1_0.models.SessionType

// room cannot store enum directyl so we need type convertors
// enum is a type that contains fix predefined values

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


    // -------- SessionType -------------
    @TypeConverter
    fun fromSessionType(value: SessionType): String = value.name

    @TypeConverter
    fun toSessionType(value: String): SessionType = SessionType.valueOf(value)


    // -> now tell database that about the EnumConvertor file, as Database.kt act




}