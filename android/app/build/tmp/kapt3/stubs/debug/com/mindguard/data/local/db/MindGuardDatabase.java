package com.mindguard.data.local.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\t"}, d2 = {"Lcom/mindguard/data/local/db/MindGuardDatabase;", "Landroidx/room/RoomDatabase;", "()V", "journalDao", "Lcom/mindguard/data/local/dao/JournalDao;", "lifestyleDao", "Lcom/mindguard/data/local/dao/LifestyleDao;", "moodDao", "Lcom/mindguard/data/local/dao/MoodDao;", "app_debug"})
@androidx.room.Database(entities = {com.mindguard.data.local.entity.DailyLogEntity.class, com.mindguard.data.local.entity.MoodEntity.class, com.mindguard.data.local.entity.JournalEntity.class}, version = 1, exportSchema = false)
public abstract class MindGuardDatabase extends androidx.room.RoomDatabase {
    
    public MindGuardDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.mindguard.data.local.dao.LifestyleDao lifestyleDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.mindguard.data.local.dao.MoodDao moodDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.mindguard.data.local.dao.JournalDao journalDao();
}