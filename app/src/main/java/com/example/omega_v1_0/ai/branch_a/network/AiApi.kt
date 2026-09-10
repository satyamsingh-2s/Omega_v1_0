package com.example.omega_v1_0.ai.branch_a.network

import com.example.omega_v1_0.ai.branch_a.model.GenerateContentRequest
import com.example.omega_v1_0.ai.branch_a.model.GenerateContentResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AiApi {

    @POST("v1beta/models/gemini-1.5-flash:generateContent")
    suspend fun generateContent(

        @Query("key")
        apiKey: String,

        @Body
        request: GenerateContentRequest

    ): GenerateContentResponse

}