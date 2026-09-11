package com.satyamsingh2s.productivity.omega.ai.branch_a.network

import com.satyamsingh2s.productivity.omega.ai.branch_a.model.GenerateContentRequest
import com.satyamsingh2s.productivity.omega.ai.branch_a.model.GenerateContentResponse
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