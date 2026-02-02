package com.example.omega_v1_0

import com.example.omega_v1_0.estimation.estimateProject
import com.example.omega_v1_0.models.Complexity
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.models.PhaseType
import com.example.omega_v1_0.models.Scope
import kotlin.test.assertEquals

class EstimationEngineTest {

    @Test
    fun estimateProject_basicCase() {
        val result = estimateProject(
            experience = Experience.INTERMEDIATE,
            phaseInputs = mapOf(
                PhaseType.IDEA to (Complexity.LOW to Scope.SMALL)
            ),
            baseTimes = mapOf(
                PhaseType.IDEA to 30
            )
        )

        assertEquals(21, result[PhaseType.IDEA])
        println("TELSNFLSNLASNLKSFLKS--------------------SDNGLSNGLS")
        println(result.toString())
    }
}

