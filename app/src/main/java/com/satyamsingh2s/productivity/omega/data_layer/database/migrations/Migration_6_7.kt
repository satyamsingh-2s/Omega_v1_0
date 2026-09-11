package com.satyamsingh2s.productivity.omega.data_layer.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_6_7 = object : Migration(6, 7) {

    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS pomodoro (
                id INTEGER NOT NULL,
                phase TEXT NOT NULL,
                remainingSeconds INTEGER NOT NULL,
                completedWorkCycles INTEGER NOT NULL,
                isRunning INTEGER NOT NULL,
                startedAt INTEGER NOT NULL,
                workDurationSeconds INTEGER NOT NULL,
                shortBreakDurationSeconds INTEGER NOT NULL,
                longBreakDurationSeconds INTEGER NOT NULL,
                workCyclesBeforeLongBreak INTEGER NOT NULL,
                PRIMARY KEY(id)
            )
            """.trimIndent()
        )
    }
}