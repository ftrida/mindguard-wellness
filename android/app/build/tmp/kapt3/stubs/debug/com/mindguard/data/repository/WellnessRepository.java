package com.mindguard.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\f2\u0006\u0010\u000f\u001a\u00020\u0010J\u001a\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\r0\f2\u0006\u0010\u0013\u001a\u00020\u0014J\u0018\u0010\u0015\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00160\r0\fJ\u0012\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\r0\fJ\u001a\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\r0\f2\u0006\u0010\u001b\u001a\u00020\u001cJ\u001a\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001e0\r0\f2\u0006\u0010\u001f\u001a\u00020 R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/mindguard/data/repository/WellnessRepository;", "", "wellnessApi", "Lcom/mindguard/data/remote/api/WellnessApi;", "lifestyleDao", "Lcom/mindguard/data/local/dao/LifestyleDao;", "moodDao", "Lcom/mindguard/data/local/dao/MoodDao;", "journalDao", "Lcom/mindguard/data/local/dao/JournalDao;", "(Lcom/mindguard/data/remote/api/WellnessApi;Lcom/mindguard/data/local/dao/LifestyleDao;Lcom/mindguard/data/local/dao/MoodDao;Lcom/mindguard/data/local/dao/JournalDao;)V", "createEmergencyContact", "Lkotlinx/coroutines/flow/Flow;", "Lcom/mindguard/core/network/NetworkResult;", "Lcom/mindguard/data/remote/dto/EmergencyContactResponse;", "contact", "Lcom/mindguard/data/remote/dto/EmergencyContactCreate;", "createJournal", "Lcom/mindguard/data/remote/dto/JournalResponse;", "journal", "Lcom/mindguard/data/remote/dto/JournalCreate;", "getEmergencyContacts", "", "getProfile", "Lcom/mindguard/data/remote/dto/ProfileResponse;", "logLifestyle", "Lcom/mindguard/data/remote/dto/DailyLifestyleResponse;", "log", "Lcom/mindguard/data/remote/dto/DailyLifestyleCreate;", "logMood", "Lcom/mindguard/data/remote/dto/MoodResponse;", "mood", "Lcom/mindguard/data/remote/dto/MoodCreate;", "app_debug"})
public final class WellnessRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.remote.api.WellnessApi wellnessApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.local.dao.LifestyleDao lifestyleDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.local.dao.MoodDao moodDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.local.dao.JournalDao journalDao = null;
    
    @javax.inject.Inject()
    public WellnessRepository(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.api.WellnessApi wellnessApi, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.local.dao.LifestyleDao lifestyleDao, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.local.dao.MoodDao moodDao, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.local.dao.JournalDao journalDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.ProfileResponse>> getProfile() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.DailyLifestyleResponse>> logLifestyle(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.dto.DailyLifestyleCreate log) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.MoodResponse>> logMood(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.dto.MoodCreate mood) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.JournalResponse>> createJournal(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.dto.JournalCreate journal) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.EmergencyContactResponse>>> getEmergencyContacts() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.EmergencyContactResponse>> createEmergencyContact(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.dto.EmergencyContactCreate contact) {
        return null;
    }
}