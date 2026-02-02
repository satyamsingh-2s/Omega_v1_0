package com.example.omega_v1_0.data_layer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.omega_v1_0.data_layer.dao.PhaseDao
import com.example.omega_v1_0.data_layer.dao.ProjectDao
import com.example.omega_v1_0.data_layer.dao.SessionDao
import com.example.omega_v1_0.data_layer.entites.PhaseEntity
import com.example.omega_v1_0.data_layer.entites.ProjectEntity
import com.example.omega_v1_0.data_layer.entites.SessionEntity
import com.example.omega_v1_0.data_layer.convertor.EnumConvertors

@Database(
    [
        ProjectEntity::class,
        PhaseEntity::class,
        SessionEntity::class
    ],
    version = 1
)

// here we are telling database that to use type convertors for enum
@TypeConverters(EnumConvertors::class)
abstract class OmegaDatabase: RoomDatabase() {
    abstract fun ProjectDao(): ProjectDao
    abstract fun PhaseDao(): PhaseDao
    abstract fun SessionDao(): SessionDao
}

/**
 * here we used abstract class because Room will generate the implementation of this class
 * same goes for abstract functions
 */