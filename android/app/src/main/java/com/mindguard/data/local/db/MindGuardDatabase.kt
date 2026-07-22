package com.mindguard.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mindguard.data.local.dao.*
import com.mindguard.data.local.entity.*

@Database(
    entities = [DailyLogEntity::class, MoodEntity::class, JournalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MindGuardDatabase : RoomDatabase() {
    abstract fun lifestyleDao(): LifestyleDao
    abstract fun moodDao(): MoodDao
    abstract fun journalDao(): JournalDao
}
