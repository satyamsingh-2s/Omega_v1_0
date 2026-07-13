package com.example.omega_v1_0.data_layer.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_7_8 = object : Migration(7, 8) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            """
            ALTER TABLE Unplanned_projects
            ADD COLUMN accentIndex INTEGER
            """.trimIndent()
        )
    }
}