package com.example.omega_v1_0.core.storage

import android.content.Context
import android.net.Uri
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

private const val ROOT_FOLDER = "Omega"
private const val REVISION_NOTES_FOLDER = "revision_notes"

class LocalFileStorageManager(

    private val context: Context

) {

    private val rootDirectory: File by lazy {

        File(
            context.filesDir,
            ROOT_FOLDER
        ).apply {
            mkdirs()
        }
    }

    private val revisionNotesDirectory: File by lazy {

        getDirectory(
            REVISION_NOTES_FOLDER
        )
    }

    /**
     * Returns a directory inside Omega's private storage.
     * Creates it automatically if it doesn't exist.
     */
    private fun getDirectory(
        folderName: String
    ): File {

        return File(
            rootDirectory,
            folderName
        ).apply {

            if (!exists()) {
                mkdirs()
            }
        }
    }

    /**
     * Copies an image into Omega private storage.
     *
     * Returns:
     * Absolute file path of the stored image.
     */
    suspend fun saveImage(
        sourceUri: Uri
    ): String = withContext(Dispatchers.IO) {

        val destinationDirectory = getDirectory(
            REVISION_NOTES_FOLDER
        )

        val destinationFile = File(
            destinationDirectory,
            "${UUID.randomUUID()}.jpg"
        )

        context.contentResolver
            .openInputStream(sourceUri)
            ?.use { input ->

                destinationFile
                    .outputStream()
                    .use { output ->

                        input.copyTo(output)
                    }

            } ?: throw IllegalArgumentException(
            "Unable to open image: $sourceUri"
        )

        destinationFile.absolutePath
    }

    /**
     * Converts a stored absolute path into a Uri.
     */
    fun getImageUri(
        path: String
    ): Uri {

        return Uri.fromFile(
            File(path)
        )
    }

    /**
     * Deletes an image from private storage.
     *
     * Returns true if deleted successfully.
     */
    fun deleteImage(
        path: String
    ): Boolean {
        val file = File(path)
        return file.exists() && file.delete()
    }

    /**
     * Checks whether the image exists.
     */
    fun imageExists(
        path: String
    ): Boolean {

        return File(path).exists()
    }


}