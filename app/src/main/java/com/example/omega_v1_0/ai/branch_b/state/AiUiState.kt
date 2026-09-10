package com.example.omega_v1_0.ai.branch_b.state

sealed interface AiUiState {

    /**
     * Nothing is happening.
     */
    data object Idle : AiUiState

    /**
     * AI request is running.
     */
    data object Loading : AiUiState

    /**
     * Gemini returned a successful response.
     */
    data class Success(
        val generatedJson: String
    ) : AiUiState

    /**
     * Something went wrong.
     */
    data class Error(
        val message: String
    ) : AiUiState
}