package com.example.omega_v1_0.ai.branch_a.model

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String
)