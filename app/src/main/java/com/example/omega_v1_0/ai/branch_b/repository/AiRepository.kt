package com.example.omega_v1_0.ai.branch_b.repository

import com.example.omega_v1_0.ai.AiConstants
import com.example.omega_v1_0.BuildConfig
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