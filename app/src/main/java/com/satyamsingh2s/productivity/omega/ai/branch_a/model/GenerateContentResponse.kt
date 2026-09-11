package com.satyamsingh2s.productivity.omega.ai.branch_a.model

import kotlinx.serialization.Serializable


@Serializable
data class GenerateContentResponse(
    val candidates: List<Candidate>
) {

    val generatedText: String?
        get() = candidates
            .firstOrNull()
            ?.content
            ?.parts
            ?.firstOrNull()
            ?.text
}

@Serializable
data class Candidate(
    val content: Content
)