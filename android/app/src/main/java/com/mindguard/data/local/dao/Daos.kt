package com.mindguard.data.local.dao

import androidx.room.*
import com.mindguard.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LifestyleDao {
    @Query("SELECT * FROM daily_lifestyle_logs ORDER BY logDate DESC")
    fun getAllLogs(): Flow<List<DailyLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DailyLogEntity)
}

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY entryTime DESC")
    fun getAllMoods(): Flow<List<MoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntity)
}

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY createdAt DESC")
    fun getAllJournals(): Flow<List<JournalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: JournalEntity)
}
