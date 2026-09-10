//package com.example.omega_v1_0.ai.testing
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.omega_v1_0.BuildConfig
//import com.example.omega_v1_0.ai.repository.AiRepository
//import kotlinx.coroutines.launch
//
//class AiTestViewModel : ViewModel() {
//
//    private val repository = AiRepository()
//
//    fun testGemini() {
//
//        viewModelScope.launch {
//
//            val result = repository.generateContent(
//
//                prompt = "Say only the word Hello.",
//
//                apiKey = BuildConfig.GEMINI_API_KEY
//
//            )
//
//            if (result.isSuccess) {
//
//                Log.d(
//                    "AiTest",
//                    result.getOrNull().orEmpty()
//                )
//
//            } else {
//
//                Log.e(
//                    "AiTest",
//                    result.exceptionOrNull()?.stackTraceToString()
//                        ?: "Unknown Error"
//                )
//
//            }
//
//        }
//
//    }
//
//}