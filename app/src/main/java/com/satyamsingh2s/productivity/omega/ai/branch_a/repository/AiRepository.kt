package com.satyamsingh2s.productivity.omega.ai.branch_a.repository

import com.satyamsingh2s.productivity.omega.ai.branch_a.model.Content
import com.satyamsingh2s.productivity.omega.ai.branch_a.model.GenerateContentRequest
import com.satyamsingh2s.productivity.omega.ai.branch_a.model.Part
import com.satyamsingh2s.productivity.omega.ai.branch_a.network.AiNetworkProvider

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