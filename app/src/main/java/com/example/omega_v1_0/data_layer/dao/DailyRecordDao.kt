package com.example.omega_v1_0.data_layer.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.omega_v1_0.data_layer.entites.DailyRecordEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailyRecordDao{

    // Create a new daily record
    @Insert
    suspend fun insert(record: DailyRecordEntity): Long

    // Get a daily record for a specific date
    @Query("""
        SELECT * FROM daily_records
        WHERE recordDate = :recordDate
        LIMIT 1
    """)
    suspend fun getRecordByDate(recordDate: LocalDate): DailyRecordEntity?

    // Get a daily record by id
    @Query("""
        SELECT * FROM daily_records
        WHERE id = :recordId
    """)
    suspend fun getRecordById(recordId: Long): DailyRecordEntity

    // History screen
    @Query("""
        SELECT * FROM daily_records
        ORDER BY recordDate DESC
    """)
    fun getAllRecords(): Flow<List<DailyRecordEntity>>

    @Query("""
    UPDATE daily_records
    SET totalDurationSeconds =
        totalDurationSeconds + :durationSeconds,
        totalSessionCount =
        totalSessionCount + 1
    WHERE id = :recordId
""")
    suspend fun updateDailySummary(
        recordId: Long,
        durationSeconds: Int
    )

    // ------------------- break fucntion qurey -------------------
    @Query("""
    UPDATE daily_records
    SET totalBreakSeconds =totalBreakSeconds + :durationSeconds,
    totalBreakCount =totalBreakCount + 1
    WHERE id = :recordId
""")
    suspend fun updateBreakSummary(
        recordId: Long,
        durationSeconds: Int
    )


}

