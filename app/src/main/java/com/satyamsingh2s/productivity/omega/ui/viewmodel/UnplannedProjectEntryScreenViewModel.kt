package com.satyamsingh2s.productivity.omega.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satyamsingh2s.productivity.omega.ai.branch_b.prompt.AiPromptBuilder
import com.satyamsingh2s.productivity.omega.ai.branch_b.repository.AiRepository
import com.satyamsingh2s.productivity.omega.ai.branch_b.state.AiUiState
import com.satyamsingh2s.productivity.omega.data_layer.imports.OmegaImport
import com.satyamsingh2s.productivity.omega.data_layer.omega_repository.Omega_Repository
import com.satyamsingh2s.productivity.omega.ui.utils.OmegaJsonParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UnplannedProjectEntryScreenViewModel(
    private val repository: Omega_Repository,
    private val aiRepository: AiRepository
) : ViewModel() {

    // -------------------------------------------------------------------------
    // AI UI STATE
    // -------------------------------------------------------------------------

    private val _aiUiState =
        MutableStateFlow<AiUiState>(
            AiUiState.Idle
        )

    val aiUiState: StateFlow<AiUiState> =
        _aiUiState.asStateFlow()


    // -------------------------------------------------------------------------
    // MANUAL WORKSPACE IMPORT
    // -------------------------------------------------------------------------

    fun importWorkspace(
        omegaImport: OmegaImport
    ) {
        viewModelScope.launch {
            repository.importStructure(omegaImport)
        }
    }


    // -------------------------------------------------------------------------
    // AI WORKSPACE GENERATION
    // -------------------------------------------------------------------------

    fun generateWorkspace(

        topic: String,

        goal: String? = null,

        currentLevel: String? = null,

        targetDuration: String? = null,

        learningStyle: String? = null,

        finalDeliverable: String? = null,

        additionalContext: String? = null,

        onSuccess: () -> Unit

    ) {

        viewModelScope.launch {

            // -------------------------------------------------------------
            // START LOADING
            // -------------------------------------------------------------

            _aiUiState.value =
                AiUiState.Loading


            // -------------------------------------------------------------
            // BUILD PROMPT
            // -------------------------------------------------------------

            val prompt =
                AiPromptBuilder.buildWorkspacePrompt(
                    topic = topic,
                    goal = goal,
                    currentLevel = currentLevel,
                    targetDuration = targetDuration,
                    learningStyle = learningStyle,
                    finalDeliverable = finalDeliverable,
                    additionalContext = additionalContext
                )


            // -------------------------------------------------------------
            // GENERATE WORKSPACE
            // -------------------------------------------------------------

            val result =
                aiRepository.generateWorkspace(prompt)


            // -------------------------------------------------------------
            // HANDLE RESULT
            // -------------------------------------------------------------

            result
                .onSuccess { json ->

                    val parseResult =
                        OmegaJsonParser.decode(json)


                    // -----------------------------------------------------
                    // VALID WORKSPACE JSON
                    // -----------------------------------------------------

                    if (parseResult.isSuccess) {

                        parseResult
                            .getOrNull()
                            ?.let { omegaImport ->

                                repository.importStructure(
                                    omegaImport
                                )

                                _aiUiState.value =
                                    AiUiState.Success(json)

                                onSuccess()
                            }

                    }

                    // -----------------------------------------------------
                    // INVALID WORKSPACE JSON
                    // -----------------------------------------------------

                    else {

                        _aiUiState.value =
                            AiUiState.Error(
                                "Gemini returned an invalid workspace."
                            )
                    }
                }


                // ---------------------------------------------------------
                // AI / NETWORK / OTHER FAILURE
                // ---------------------------------------------------------

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