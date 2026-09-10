package com.example.omega_v1_0.omega_engines.estimation_engine

import com.example.omega_v1_0.models_enums.Complexity
import com.example.omega_v1_0.models_enums.Experience
import com.example.omega_v1_0.models_enums.PhaseType
import com.example.omega_v1_0.models_enums.Scope

/**
 * here we estimates time fro a single phase
 * pure function - dterministics
 */

fun estimateMinutes(
    baseMinutes: Int,
    complexity: Complexity,
    scope: Scope,
    experience: Experience
): Int {

    val complexityFactor = when (complexity) {
        Complexity.LOW -> 0.8
        Complexity.MEDIUM -> 1.0
        Complexity.HIGH -> 1.3
    }

    val scopeFactor = when (scope) {
        Scope.SMALL -> 0.9
        Scope.MEDIUM -> 1.0
        Scope.LARGE -> 1.25
    }

    val experienceFactor = when (experience) {
        Experience.BEGINNER -> 1.2
        Experience.INTERMEDIATE -> 1.0
        Experience.ADVANCED -> 0.85
    }

    return (baseMinutes * complexityFactor * scopeFactor * experienceFactor)
        .toInt()
}

/**
 * here we calculate the total time of the project
 * phase level wrapper
 */

fun estimateProject(
    experience: Experience,
    phaseInputs: Map<PhaseType, Pair<Complexity, Scope>>,
    baseTimes: Map<PhaseType, Int>
): Map<PhaseType, Int> {
    return phaseInputs.mapValues { (phase, input) ->
        estimateMinutes(
            baseMinutes = baseTimes.getValue(phase),
            complexity = input.first,
            scope = input.second,
            experience = experience
        )
    }
}
