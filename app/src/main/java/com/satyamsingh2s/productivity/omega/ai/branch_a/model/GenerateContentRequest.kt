package com.satyamsingh2s.productivity.omega.ai.branch_a.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateContentRequest(
    val contents: List<Content>
)