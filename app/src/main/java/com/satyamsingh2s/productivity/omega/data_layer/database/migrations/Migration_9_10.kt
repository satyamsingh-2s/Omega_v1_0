package com.satyamsingh2s.productivity.omega.data_layer.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_9_10 = object : Migration(9, 10) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS session_note_attachments (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                sessionId INTEGER NOT NULL,
                imageUri TEXT NOT NULL,
                createdAt INTEGER NOT NULL,
                FOREIGN KEY(sessionId)
                    REFERENCES sessions(id)
                    ON DELETE CASCADE
            )
            """.trimIndent()
        )

        database.execSQL(
            """
            CREATE INDEX IF NOT EXISTS
            index_session_note_attachments_sessionId_createdAt
            ON session_note_attachments(sessionId, createdAt)
            """.trimIndent()
        )
    }
}