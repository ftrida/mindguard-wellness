package com.mindguard.data.remote.dto;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0019\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BE\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\u000b\u0012\u0006\u0010\r\u001a\u00020\b\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001f\u001a\u00020\bH\u00c6\u0003J\t\u0010 \u001a\u00020\u000bH\u00c6\u0003J\t\u0010!\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\"\u001a\u00020\bH\u00c6\u0003JY\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\bH\u00c6\u0001J\u0013\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\'\u001a\u00020\u000bH\u00d6\u0001J\t\u0010(\u001a\u00020\u0006H\u00d6\u0001R\u0016\u0010\n\u001a\u00020\u000b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0016\u0010\f\u001a\u00020\u000b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0016\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0016\u0010\t\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0016\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017R\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0013R\u0016\u0010\r\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0017\u00a8\u0006)"}, d2 = {"Lcom/mindguard/data/remote/dto/DailyLifestyleResponse;", "", "id", "", "userId", "logDate", "", "sleepHours", "", "screenTimeHours", "activeMinutes", "", "caffeineIntakeMg", "waterIntakeLiters", "(JJLjava/lang/String;FFIIF)V", "getActiveMinutes", "()I", "getCaffeineIntakeMg", "getId", "()J", "getLogDate", "()Ljava/lang/String;", "getScreenTimeHours", "()F", "getSleepHours", "getUserId", "getWaterIntakeLiters", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "toString", "app_debug"})
public final class DailyLifestyleResponse {
    private final long id = 0L;
    @com.google.gson.annotations.SerializedName(value = "user_id")
    private final long userId = 0L;
    @com.google.gson.annotations.SerializedName(value = "log_date")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String logDate = null;
    @com.google.gson.annotations.SerializedName(value = "sleep_hours")
    private final float sleepHours = 0.0F;
    @com.google.gson.annotations.SerializedName(value = "screen_time_hours")
    private final float screenTimeHours = 0.0F;
    @com.google.gson.annotations.SerializedName(value = "active_minutes")
    private final int activeMinutes = 0;
    @com.google.gson.annotations.SerializedName(value = "caffeine_intake_mg")
    private final int caffeineIntakeMg = 0;
    @com.google.gson.annotations.SerializedName(value = "water_intake_liters")
    private final float waterIntakeLiters = 0.0F;
    
    public DailyLifestyleResponse(long id, long userId, @org.jetbrains.annotations.NotNull()
    java.lang.String logDate, float sleepHours, float screenTimeHours, int activeMinutes, int caffeineIntakeMg, float waterIntakeLiters) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final long getUserId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLogDate() {
        return null;
    }
    
    public final float getSleepHours() {
        return 0.0F;
    }
    
    public final float getScreenTimeHours() {
        return 0.0F;
    }
    
    public final int getActiveMinutes() {
        return 0;
    }
    
    public final int getCaffeineIntakeMg() {
        return 0;
    }
    
    public final float getWaterIntakeLiters() {
        return 0.0F;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long component2() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final float component4() {
        return 0.0F;
    }
    
    public final float component5() {
        return 0.0F;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final float component8() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mindguard.data.remote.dto.DailyLifestyleResponse copy(long id, long userId, @org.jetbrains.annotations.NotNull()
    java.lang.String logDate, float sleepHours, float screenTimeHours, int activeMinutes, int caffeineIntakeMg, float waterIntakeLiters) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}