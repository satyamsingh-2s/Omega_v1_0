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
import com.example.omega_v1_0.data_layer.dao.ActiveBreakDao
import com.example.omega_v1_0.data_layer.dao.ActiveSessionDao
import com.example.omega_v1_0.data_layer.dao.DailyRecordDao
import com.example.omega_v1_0.data_layer.dao.ToDoListDao
import com.example.omega_v1_0.data_layer.entites.ActiveSessionEntity
import com.example.omega_v1_0.data_layer.entites.ToDoListEntity
import com.example.omega_v1_0.data_layer.entites.ActiveBreakEntity

@Database(
    [
        PlannedProjectEntity::class,
        PhaseEntity::class,
        SessionEntity::class,

        UnplannedProjectEntity::class,
        DailyRecordEntity::class,
        ActiveSessionEntity::class,

        ToDoListEntity::class,

        ActiveBreakEntity::class
    ],
    version = 6
)

// here we are telling database that to use type convertors for enum
@TypeConverters(EnumConvertors::class)
abstract class OmegaDatabase: RoomDatabase() {
    abstract fun ProjectDao(): PlannedProjectDao
    abstract fun PhaseDao(): PhaseDao
    abstract fun SessionDao(): SessionDao
    abstract fun DailyRecordDao(): DailyRecordDao
    abstract fun ActiveSessionDao(): ActiveSessionDao
    abstract fun ToDoListDao(): ToDoListDao
    abstract fun ActiveBreakDao(): ActiveBreakDao
}

/**
 * here we used abstract class because Room will generate the implementation of this class
 * same goes for abstract functions
 */