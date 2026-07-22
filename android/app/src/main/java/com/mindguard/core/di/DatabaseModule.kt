package com.mindguard.core.di

import android.content.Context
import androidx.room.Room
import com.mindguard.data.local.dao.*
import com.mindguard.data.local.db.MindGuardDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MindGuardDatabase {
        return Room.databaseBuilder(
            context,
            MindGuardDatabase::class.java,
            "mindguard_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideLifestyleDao(db: MindGuardDatabase): LifestyleDao = db.lifestyleDao()

    @Provides
    fun provideMoodDao(db: MindGuardDatabase): MoodDao = db.moodDao()

    @Provides
    fun provideJournalDao(db: MindGuardDatabase): JournalDao = db.journalDao()
}
