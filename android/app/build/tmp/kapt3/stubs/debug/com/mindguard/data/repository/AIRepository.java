package com.mindguard.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0092\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001BG\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\u0002\u0010\u0012J\u0018\u0010\u0013\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00160\u00150\u0014J\u0012\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\u00150\u0014J\u0012\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001b0\u00150\u0014J\u001e\u0010\u001c\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u00010\u001d0\u00150\u0014J\u0018\u0010\u001f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020 0\u00160\u00150\u0014J\u0018\u0010!\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0\u00160\u00150\u0014J\u0018\u0010#\u001a\u0014\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020$0\u00160\u00150\u0014J\u0012\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0\u00150\u0014J\u0012\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020(0\u00150\u0014J\u0012\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020*0\u00150\u0014J\u001e\u0010+\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u00010\u001d0\u00150\u0014J\u001a\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u00150\u00142\u0006\u0010.\u001a\u00020\u001eR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/mindguard/data/repository/AIRepository;", "", "twinApi", "Lcom/mindguard/data/remote/api/TwinApi;", "behaviorApi", "Lcom/mindguard/data/remote/api/BehaviorApi;", "stressApi", "Lcom/mindguard/data/remote/api/StressApi;", "coachApi", "Lcom/mindguard/data/remote/api/CoachApi;", "recommendationsApi", "Lcom/mindguard/data/remote/api/RecommendationsApi;", "goalsApi", "Lcom/mindguard/data/remote/api/GoalsApi;", "achievementsApi", "Lcom/mindguard/data/remote/api/AchievementsApi;", "reportsApi", "Lcom/mindguard/data/remote/api/ReportsApi;", "(Lcom/mindguard/data/remote/api/TwinApi;Lcom/mindguard/data/remote/api/BehaviorApi;Lcom/mindguard/data/remote/api/StressApi;Lcom/mindguard/data/remote/api/CoachApi;Lcom/mindguard/data/remote/api/RecommendationsApi;Lcom/mindguard/data/remote/api/GoalsApi;Lcom/mindguard/data/remote/api/AchievementsApi;Lcom/mindguard/data/remote/api/ReportsApi;)V", "getAchievements", "Lkotlinx/coroutines/flow/Flow;", "Lcom/mindguard/core/network/NetworkResult;", "", "Lcom/mindguard/data/remote/dto/AchievementResponse;", "getBehaviorDrift", "Lcom/mindguard/data/remote/dto/BehaviorDriftResponse;", "getCoachAdvice", "Lcom/mindguard/data/remote/dto/CoachAdviceResponse;", "getDailyReport", "", "", "getGoals", "Lcom/mindguard/data/remote/dto/GoalResponse;", "getMemory", "Lcom/mindguard/data/remote/dto/CoachMemoryItem;", "getRecommendations", "Lcom/mindguard/data/remote/dto/RecommendationResponse;", "getStreak", "Lcom/mindguard/data/remote/dto/StreakResponse;", "getStressAssessment", "Lcom/mindguard/data/remote/dto/StressLikelihoodResponse;", "getTwin", "Lcom/mindguard/data/remote/dto/DigitalTwinResponse;", "getWeeklyReport", "sendCoachMessage", "Lcom/mindguard/data/remote/dto/CoachChatResponse;", "message", "app_debug"})
public final class AIRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.remote.api.TwinApi twinApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.remote.api.BehaviorApi behaviorApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.remote.api.StressApi stressApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.remote.api.CoachApi coachApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.remote.api.RecommendationsApi recommendationsApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.remote.api.GoalsApi goalsApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.remote.api.AchievementsApi achievementsApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.remote.api.ReportsApi reportsApi = null;
    
    @javax.inject.Inject()
    public AIRepository(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.api.TwinApi twinApi, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.api.BehaviorApi behaviorApi, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.api.StressApi stressApi, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.api.CoachApi coachApi, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.api.RecommendationsApi recommendationsApi, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.api.GoalsApi goalsApi, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.api.AchievementsApi achievementsApi, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.api.ReportsApi reportsApi) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<java.util.Map<java.lang.String, java.lang.Object>>> getDailyReport() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<java.util.Map<java.lang.String, java.lang.Object>>> getWeeklyReport() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.DigitalTwinResponse>> getTwin() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.BehaviorDriftResponse>> getBehaviorDrift() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StressLikelihoodResponse>> getStressAssessment() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.CoachChatResponse>> sendCoachMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String message) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.CoachMemoryItem>>> getMemory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.CoachAdviceResponse>> getCoachAdvice() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.RecommendationResponse>>> getRecommendations() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.GoalResponse>>> getGoals() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.AchievementResponse>>> getAchievements() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StreakResponse>> getStreak() {
        return null;
    }
}