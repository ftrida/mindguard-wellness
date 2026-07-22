package com.mindguard.data.remote.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\t"}, d2 = {"Lcom/mindguard/data/remote/api/AchievementsApi;", "", "getAchievements", "Lretrofit2/Response;", "", "Lcom/mindguard/data/remote/dto/AchievementResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getStreak", "Lcom/mindguard/data/remote/dto/StreakResponse;", "app_debug"})
public abstract interface AchievementsApi {
    
    @retrofit2.http.GET(value = "api/v1/achievements")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAchievements(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.mindguard.data.remote.dto.AchievementResponse>>> $completion);
    
    @retrofit2.http.GET(value = "api/v1/achievements/streak")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getStreak(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.mindguard.data.remote.dto.StreakResponse>> $completion);
}