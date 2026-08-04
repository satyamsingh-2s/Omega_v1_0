package com.example.omega_v1_0.ui.uistate

import com.example.omega_v1_0.ui.model.AttachmentUiModel

data class RevisionNoteUiState(

    val summary: String = "",
    val isLoading: Boolean = false,
    val sessionId: Long? = null,
    val isSaving: Boolean = false,

    // now attachment section - attachment table link with session entity not with noteentity
    val attachments: List<AttachmentUiModel> = emptyList()
)


{
//    Why store SessionNoteAttachmentEntity directly?
//
//    Normally, I'd recommend using a UI model, but in Omega's current implementation the attachment entity is already UI-friendly:
//
//    SessionNoteAttachmentEntity(
//
//    id,
//    sessionId,
//    imageUri,
//    createdAt
//    )
//
//    The UI only needs:
//
//    id → delete attachment
//    imageUri → display image
//
//    There is no presentation-specific formatting or derived state yet, so introducing another AttachmentUiModel would just add unnecessary mapping.
//
//    If, in the future, you add things like:
//
//    upload progress,
//    image dimensions,
//    thumbnails,
//    selection state,
//
//    then creating a dedicated UI model will make sense. For v1, using the entity directly keeps the implementation simple and efficient.
}