package com.mindguard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_lifestyle_logs")
data class DailyLogEntity(
    @PrimaryKey val id: Long,
    val sleepHours: Float,
    val screenTimeHours: Float,
    val activeMinutes: Int,
    val caffeineIntakeMg: Int,
    val waterIntakeLiters: Float,
    val logDate: String
)

@Entity(tableName = "mood_entries")
data class MoodEntity(
    @PrimaryKey val id: Long,
    val moodScore: Int,
    val notes: String?,
    val entryTime: String
)

@Entity(tableName = "journal_entries")
data class JournalEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val content: String,
    val sentimentScore: Float?,
    val createdAt: String
)
