package com.satyamsingh2s.productivity.omega.data_layer.omega_repository

import android.net.Uri
import com.satyamsingh2s.productivity.omega.core.storage.LocalFileStorageManager
import com.satyamsingh2s.productivity.omega.data_layer.dao.SessionNoteAttachmentDao
import com.satyamsingh2s.productivity.omega.data_layer.entites.SessionNoteAttachmentEntity
import kotlinx.coroutines.flow.Flow

class SessionNoteAttachmentRepository(
    private val attachmentDao: SessionNoteAttachmentDao,
    private val localFileStorageManager: LocalFileStorageManager
) {

    suspend fun addAttachment(
        sessionId: Long,
        imageUri: String
    ) {

        val localImagePath =
            localFileStorageManager.saveImage(
                Uri.parse(imageUri)
            )
        attachmentDao.insertAttachment(
            SessionNoteAttachmentEntity(
                sessionId = sessionId,
                imageUri = localImagePath,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteAttachment(
        attachmentId:Long
    ) {

        val attachment =
            attachmentDao.getAttachmentById(
                attachmentId
            ) ?: return

        localFileStorageManager.deleteImage(
            attachment.imageUri)

        attachmentDao.deleteAttachment(
            attachment
        )
    }

    suspend fun getAttachments(
        sessionId: Long
    ): List<SessionNoteAttachmentEntity> {

        return attachmentDao.getAttachmentsBySessionId(
            sessionId
        )
    }

    fun observeAttachments(
        sessionId: Long
    ): Flow<List<SessionNoteAttachmentEntity>> {

        return attachmentDao.observeAttachmentsBySessionId(
            sessionId
        )
    }

    suspend fun deleteAttachments(
        sessionId: Long
    ) {

        val attachments =
            attachmentDao.getAttachmentsBySessionId(
                sessionId
            )

        attachments.forEach {

            localFileStorageManager.deleteImage(
                it.imageUri
            )
        }

        attachmentDao.deleteAttachmentsBySessionId(
            sessionId
        )
    }
}