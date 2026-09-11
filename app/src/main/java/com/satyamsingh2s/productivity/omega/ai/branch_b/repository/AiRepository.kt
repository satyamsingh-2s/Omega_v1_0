package com.satyamsingh2s.productivity.omega.ai.branch_b.repository

import com.satyamsingh2s.productivity.omega.ai.AiConstants
import com.satyamsingh2s.productivity.omega.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel


class AiRepository {

    private val generativeModel = GenerativeModel(
        modelName = AiConstants.MODEL_NAME,
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    suspend fun generateWorkspace(
        prompt: String
    ): Result<String> {

        return try {

            val response = generativeModel.generateContent(prompt)

            val generatedText = response.text

            if (generatedText.isNullOrBlank()) {

                Result.failure(
                    IllegalStateException(
                        "Gemini returned an empty response."
                    )
                )

            } else {

                Result.success(generatedText)

            }

        } catch (exception: Exception) {

            Result.failure(exception)

        }
    }
}