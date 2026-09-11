package com.satyamsingh2s.productivity.omega.data_layer.convertor

import androidx.room.TypeConverter
import com.satyamsingh2s.productivity.omega.omega_engines.pomodoro_engine.PomodoroPhase
import com.satyamsingh2s.productivity.omega.models_enums.Complexity
import com.satyamsingh2s.productivity.omega.models_enums.Experience
import com.satyamsingh2s.productivity.omega.models_enums.PhaseType
import com.satyamsingh2s.productivity.omega.models_enums.PlannerPriority
import com.satyamsingh2s.productivity.omega.models_enums.Scope
import com.satyamsingh2s.productivity.omega.models_enums.SessionType
import java.time.LocalDate

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

    // --------- DailyRecord --- converting local date to string and inverse
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }
    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }

    // ---------- type convertor for pomodoro phase -----
    @TypeConverter
    fun fromPomodoroPhase(value: PomodoroPhase): String {
        return value.name
    }
    @TypeConverter
    fun toPomodoroPhase(value: String): PomodoroPhase {
        return PomodoroPhase.valueOf(value)
    }

    // ----------- convertor for planner priority
    @TypeConverter
    fun fromPlannerPriority(priority: PlannerPriority): String {
        return priority.name
    }
    @TypeConverter
    fun toPlannerPriority(value: String): PlannerPriority {
        return PlannerPriority.valueOf(value)
    }


    // -> now tell database that about the EnumConvertor file, as Database.kt act




}