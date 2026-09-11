package com.satyamsingh2s.productivity.omega.data_layer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.satyamsingh2s.productivity.omega.data_layer.dao.PhaseDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.PlannedProjectDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.SessionDao
import com.satyamsingh2s.productivity.omega.data_layer.entites.PhaseEntity
import com.satyamsingh2s.productivity.omega.data_layer.entites.PlannedProjectEntity
import com.satyamsingh2s.productivity.omega.data_layer.entites.SessionEntity
import com.satyamsingh2s.productivity.omega.data_layer.entites.UnplannedProjectEntity
import com.satyamsingh2s.productivity.omega.data_layer.entites.DailyRecordEntity
import com.satyamsingh2s.productivity.omega.data_layer.convertor.EnumConvertors
import com.satyamsingh2s.productivity.omega.data_layer.dao.ActiveBreakDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.ActiveSessionDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.DailyRecordDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.PomodoroDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.SessionNoteAttachmentDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.SessionNoteDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.ToDoListDao
import com.satyamsingh2s.productivity.omega.data_layer.dao.UnplannedProjectDao
import com.satyamsingh2s.productivity.omega.data_layer.entites.ActiveSessionEntity
import com.satyamsingh2s.productivity.omega.data_layer.entites.ToDoListEntity
import com.satyamsingh2s.productivity.omega.data_layer.entites.ActiveBreakEntity
import com.satyamsingh2s.productivity.omega.data_layer.entites.PomodoroEntity
import com.satyamsingh2s.productivity.omega.data_layer.entites.SessionNoteAttachmentEntity
import com.satyamsingh2s.productivity.omega.data_layer.entites.SessionNoteEntity
import com.satyamsingh2s.productivity.omega.data_layer.dao.PlannerDao
import com.satyamsingh2s.productivity.omega.data_layer.entites.PlannerNodeEntity

@Database(
    [
        PlannedProjectEntity::class,
        PhaseEntity::class,
        SessionEntity::class,

        UnplannedProjectEntity::class,
        DailyRecordEntity::class,
        ActiveSessionEntity::class,

        ToDoListEntity::class,
        SessionNoteEntity::class,
        SessionNoteAttachmentEntity::class,

        ActiveBreakEntity::class,

        PomodoroEntity::class,

        PlannerNodeEntity::class,
    ],
    version = 11
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
    abstract fun UnplannedProjectDao(): UnplannedProjectDao
    abstract fun pomodoroDao(): PomodoroDao
    abstract fun sessionNoteDao(): SessionNoteDao
    abstract fun sessionNoteAttachmentDao(): SessionNoteAttachmentDao
    abstract fun plannerDao(): PlannerDao
}

/**
 * here we used abstract class because Room will generate the implementation of this class
 * same goes for abstract functions
 */