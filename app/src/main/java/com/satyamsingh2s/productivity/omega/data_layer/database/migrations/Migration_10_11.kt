package com.satyamsingh2s.productivity.omega.data_layer.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_10_11 = object : Migration(10, 11) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS planner_nodes (
                nodeId INTEGER NOT NULL,
                priority TEXT NOT NULL,
                priorityOrder INTEGER NOT NULL,
                todayOrder INTEGER,
                addedToPlannerAt INTEGER NOT NULL,
                PRIMARY KEY(nodeId)
            )
            """.trimIndent()
        )
    }
}