package com.example.omega_v1_0.data_layer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.omega_v1_0.data_layer.dao.PhaseDao
import com.example.omega_v1_0.data_layer.dao.PlannedProjectDao
import com.example.omega_v1_0.data_layer.dao.SessionDao
import com.example.omega_v1_0.data_layer.entites.PhaseEntity
import com.example.omega_v1_0.data_layer.entites.PlannedProjectEntity
import com.example.omega_v1_0.data_layer.entites.SessionEntity
import com.example.omega_v1_0.data_layer.entites.UnplannedProjectEntity
import com.example.omega_v1_0.data_layer.entites.DailyRecordEntity
import com.example.omega_v1_0.data_layer.convertor.EnumConvertors
import com.example.omega_v1_0.data_layer.dao.ActiveSessionDao
import com.example.omega_v1_0.data_layer.dao.DailyRecordDao
import com.example.omega_v1_0.data_layer.entites.ActiveSessionEntity

@Database(
    [
        PlannedProjectEntity::class,
        PhaseEntity::class,
        SessionEntity::class,

        UnplannedProjectEntity::class,
        DailyRecordEntity::class,
        ActiveSessionEntity::class
    ],
    version = 4
)

// here we are telling database that to use type convertors for enum
@TypeConverters(EnumConvertors::class)
abstract class OmegaDatabase: RoomDatabase() {
    abstract fun ProjectDao(): PlannedProjectDao
    abstract fun PhaseDao(): PhaseDao
    abstract fun SessionDao(): SessionDao
    abstract fun DailyRecordDao(): DailyRecordDao
    abstract fun ActiveSessionDao(): ActiveSessionDao
}

/**
 * here we used abstract class because Room will generate the implementation of this class
 * same goes for abstract functions
 */