package com.satyamsingh2s.productivity.omega.ai.branch_a.network

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<Content>
)

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String
)

@Serializable
data class GenerateContentRequest(
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(
    val content: Content? = null
)
