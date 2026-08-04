package com.example.omega_v1_0.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.core.storage.LocalFileStorageManager
import com.example.omega_v1_0.data_layer.entites.SessionNoteAttachmentEntity
import com.example.omega_v1_0.data_layer.omega_repository.SessionNoteAttachmentRepository
import com.example.omega_v1_0.data_layer.omega_repository.SessionNoteRepository
import com.example.omega_v1_0.models.RevisionNoteItem
import com.example.omega_v1_0.ui.mapper.toAttachmentUiModel
import com.example.omega_v1_0.ui.model.AttachmentUiModel
import com.example.omega_v1_0.ui.uistate.RevisionNoteUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RevisionNoteViewModel(
    private val repository: SessionNoteRepository,
    private val attachmentRepository: SessionNoteAttachmentRepository,
    private val localFileStorageManager: LocalFileStorageManager,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow(
        RevisionNoteUiState()
    )
    val uiState = _uiState.asStateFlow()

    private val _revisionNotes =
        MutableStateFlow<List<RevisionNoteItem>>(emptyList())

    val revisionNotes =
        _revisionNotes.asStateFlow()

    // for notes
    private var observeNotesJob: Job? = null

    // for attachemtnt like images/ and other attachmetns
    private var observeAttachmentsJob: Job? = null
    /**
     * Loads the revision note for a session.
     * If no note exists, an empty summary is prepared.
     */
    fun loadRevisionNote(
        sessionId: Long
    ) {

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }

            val note = repository.getRevisionNote(sessionId)

            _uiState.update {
                it.copy(
                    sessionId = sessionId,
                    summary = note?.summary.orEmpty(),
                    isLoading = false
                )
            }
            observeAttachments(sessionId)
        }
    }


    fun observeRevisionNotes(nodeId: Long) {

        observeNotesJob?.cancel()

        observeNotesJob = viewModelScope.launch {

            repository.observeRevisionNotes(nodeId)
                .collect { notes ->
                    _revisionNotes.value = notes
                }
        }
    }

    private fun observeAttachments(
        sessionId: Long
    ) {

        observeAttachmentsJob?.cancel()

        observeAttachmentsJob = viewModelScope.launch {

            attachmentRepository
                .observeAttachments(sessionId)
                .collect { attachments ->

                    _uiState.update {
                        it.copy(
                            attachments = attachments.toAttachmentUiModels(
                                localFileStorageManager
                            )
                        )
                    }
                }
        }
    }

    fun List<SessionNoteAttachmentEntity>.toAttachmentUiModels(
        localFileStorageManager: LocalFileStorageManager
    ): List<AttachmentUiModel> {

        return map {

            it.toAttachmentUiModel(
                localFileStorageManager
            )
        }
    }

    /**
     * Updates the editor text.
     */
    fun onSummaryChanged(
        summary: String
    ) {

        _uiState.update {
            it.copy(
                summary = summary
            )
        }
    }

    /**
     * Saves (or deletes if empty) the revision note.
     */
    fun saveRevisionNote(
        onSuccess: () -> Unit = {}
    ) {

        val state = _uiState.value
        val sessionId = state.sessionId ?: return

        // --- saves form repeatidly tapping save button
        if (_uiState.value.isSaving) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(isSaving = true)
            }

            try {

                repository.saveRevisionNote(
                    sessionId = sessionId,
                    summary = state.summary
                )

                onSuccess()

            } finally {

                _uiState.update {
                    it.copy(isSaving = false)
                }
            }
        }
    }

    fun addAttachment(
        imageUri: String
    ) {
        val sessionId =
            _uiState.value.sessionId ?: return
        viewModelScope.launch {
            attachmentRepository.addAttachment(
                sessionId = sessionId,
                imageUri = imageUri
            )
        }
    }

    fun deleteAttachment(
        attachmentId: Long
    ) {
        viewModelScope.launch {
            attachmentRepository.deleteAttachment(
                attachmentId
            )
        }
    }
    /**
     * Clears the editor state.
     */
    fun clear() {

        // because of this it closes as note disappears otherwise it will remain till viewmodel
        observeAttachmentsJob?.cancel()
        _uiState.value = RevisionNoteUiState()
    }
}