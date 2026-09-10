package com.example.omega_v1_0.ai.branch_a.repository

import com.example.omega_v1_0.ai.branch_a.model.Content
import com.example.omega_v1_0.ai.branch_a.model.GenerateContentRequest
import com.example.omega_v1_0.ai.branch_a.model.Part
import com.example.omega_v1_0.ai.branch_a.network.AiNetworkProvider

class AiRepository {

    suspend fun generateContent(
        prompt: String,
        apiKey: String
    ): Result<String> {

        return try {
            val request = GenerateContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(

                            Part(
                                text = prompt
                            )

                        )

                    )

                )

            )

            val response = AiNetworkProvider.aiApi.generateContent(

                apiKey = apiKey,

                request = request

            )

            val generatedText = response.generatedText

            if (generatedText == null) {

                Result.failure(

                    IllegalStateException(
                        "Gemini returned an empty response."
                    )

                )

            } else {

                Result.success(
                    generatedText
                )

            }

        } catch (exception: Exception) {

            Result.failure(
                exception
            )

        }

    }

}