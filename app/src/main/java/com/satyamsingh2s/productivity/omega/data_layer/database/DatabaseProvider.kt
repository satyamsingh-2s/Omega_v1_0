package com.satyamsingh2s.productivity.omega.data_layer.database

import android.content.Context
import androidx.room.Room
import com.satyamsingh2s.productivity.omega.data_layer.database.migrations.MIGRATION_10_11
import com.satyamsingh2s.productivity.omega.data_layer.database.migrations.MIGRATION_6_7
import com.satyamsingh2s.productivity.omega.data_layer.database.migrations.MIGRATION_7_8
import com.satyamsingh2s.productivity.omega.data_layer.database.migrations.MIGRATION_8_9
import com.satyamsingh2s.productivity.omega.data_layer.database.migrations.MIGRATION_9_10

object DatabaseProvider {

    @Volatile
    private var INSTANCE: OmegaDatabase? = null

    fun getDatabase(context: Context): OmegaDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                OmegaDatabase::class.java,
                "omega_db"
            )
                .addMigrations(MIGRATION_6_7,
                    MIGRATION_7_8,
                    MIGRATION_8_9,
                    MIGRATION_9_10,
                    MIGRATION_10_11
                )
                .build()
            INSTANCE = instance
            instance
        }
    }
}