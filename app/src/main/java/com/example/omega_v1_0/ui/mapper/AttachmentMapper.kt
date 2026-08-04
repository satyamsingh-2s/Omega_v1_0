package com.example.omega_v1_0.ui.mapper

import com.example.omega_v1_0.core.storage.LocalFileStorageManager
import com.example.omega_v1_0.data_layer.entites.SessionNoteAttachmentEntity
import com.example.omega_v1_0.ui.model.AttachmentUiModel

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