package com.satyamsingh2s.productivity.omega.ai

object AiConstants {

    /**
     * Google Gemini model used for workspace generation.
     */
    const val MODEL_NAME = "gemini-3.5-flash-lite"

    /**
     * Default temperature.
     * Lower values produce more deterministic JSON.
     */
    const val TEMPERATURE = 0.2f

    /**
     * Maximum output tokens.
     * Increase later if larger workspaces are required.
     */
    const val MAX_OUTPUT_TOKENS = 4096

    /**
     * Safety: maximum retry attempts for generation.
     */
    const val MAX_RETRY_COUNT = 2

    /**
     * Request timeout (milliseconds).
     */
    const val REQUEST_TIMEOUT = 30_000L
}