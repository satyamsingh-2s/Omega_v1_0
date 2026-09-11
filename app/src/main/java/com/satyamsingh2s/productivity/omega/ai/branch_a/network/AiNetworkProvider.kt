package com.satyamsingh2s.productivity.omega.ai.branch_a.network

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

object AiNetworkProvider {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    // ---- later we will implement as it requires to turn build.config on----
//    private val loggingInterceptor =
//        HttpLoggingInterceptor().apply {
//            level =
//                if (BuildConfig.DEBUG)
//                    HttpLoggingInterceptor.Level.BODY
//                else
//                    HttpLoggingInterceptor.Level.NONE
//        }

    private val loggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    //-- configure for llm as it takes time of 15-30 seconds
    private val okHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()


    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()

    val aiApi: AiApi =
        retrofit.create(AiApi::class.java)
}