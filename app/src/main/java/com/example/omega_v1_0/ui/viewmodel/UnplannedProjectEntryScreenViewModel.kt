package com.example.omega_v1_0.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.ai.branch_b.repository.AiRepository
import com.example.omega_v1_0.data_layer.imports.OmegaImport
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import kotlinx.coroutines.launch


import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.ai.branch_b.prompt.AiPromptBuilder
import com.example.omega_v1_0.ai.branch_b.state.AiUiState
import com.example.omega_v1_0.ui.utils.OmegaJsonParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
//import com.example.omega_v1_0.BuildConfig
import kotlinx.coroutines.launch

class UnplannedProjectEntryScreenViewModel(

    private val repository: Omega_Repository,
    private val aiRepository: AiRepository

) : ViewModel() {

    private val _aiUiState =
        MutableStateFlow<AiUiState>(
            AiUiState.Idle
        )
    val aiUiState: StateFlow<AiUiState> =
        _aiUiState.asStateFlow()

     fun importWorkspace(
        omegaImport: OmegaImport
    ) {
        viewModelScope.launch {
            repository.importStructure(omegaImport)
        }
    }

    fun generateWorkspace(

        topic: String,

        goal: String? = null,

        currentLevel: String? = null,

        targetDuration: String? = null,

        learningStyle: String? = null,

        finalDeliverable: String? = null,

        onSuccess: () -> Unit

    ) {

        viewModelScope.launch {

            _aiUiState.value = AiUiState.Loading

            val prompt = AiPromptBuilder.buildWorkspacePrompt(

                topic = topic,

                goal = goal,

                currentLevel = currentLevel,

                targetDuration = targetDuration,

                learningStyle = learningStyle,

                finalDeliverable = finalDeliverable

            )

            val result =
                aiRepository.generateWorkspace(prompt)

            result

                .onSuccess { json ->

                    val parseResult =
                        OmegaJsonParser.decode(json)

                    if (parseResult.isSuccess) {

                        parseResult.getOrNull()?.let {

                            repository.importStructure(it)

                            _aiUiState.value =
                                AiUiState.Success(json)

                            onSuccess()
                        }

                    } else {

                        _aiUiState.value =
                            AiUiState.Error(
                                "Gemini returned an invalid workspace."
                            )
                    }

                }

                .onFailure { exception ->

                    _aiUiState.value =
                        AiUiState.Error(
                            exception.message
                                ?: "Unknown error"
                        )
                }
        }
    }

}