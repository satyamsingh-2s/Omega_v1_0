package com.example.omega_v1_0.ai.branch_a.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateContentRequest(
    val contents: List<Content>
)