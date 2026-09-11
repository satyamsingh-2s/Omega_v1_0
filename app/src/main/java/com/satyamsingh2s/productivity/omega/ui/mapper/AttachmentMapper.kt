package com.satyamsingh2s.productivity.omega.ui.mapper

import com.satyamsingh2s.productivity.omega.core.storage.LocalFileStorageManager
import com.satyamsingh2s.productivity.omega.data_layer.entites.SessionNoteAttachmentEntity
import com.satyamsingh2s.productivity.omega.ui.model.AttachmentUiModel

fun SessionNoteAttachmentEntity.toAttachmentUiModel(
    localFileStorageManager: LocalFileStorageManager
): AttachmentUiModel {

    return AttachmentUiModel(

        id = id,

        imageUri = localFileStorageManager.getImageUri(
            imageUri
        )
    )
}