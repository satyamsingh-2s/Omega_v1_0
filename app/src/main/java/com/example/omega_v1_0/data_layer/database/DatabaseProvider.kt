package com.example.omega_v1_0.data_layer.database

import android.content.Context
import androidx.room.Room
import com.example.omega_v1_0.data_layer.database.OmegaDatabase

object DatabaseProvider {

    @Volatile
    private var INSTANCE: OmegaDatabase? = null

    fun getDatabase(context: Context): OmegaDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                OmegaDatabase::class.java,
                "omega_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}