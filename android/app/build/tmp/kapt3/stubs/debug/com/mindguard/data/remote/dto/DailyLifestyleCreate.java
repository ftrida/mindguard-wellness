package com.mindguard.data.remote.dto;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0011\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B1\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J;\u0010\u0016\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u0006H\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001R\u0016\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0016\u0010\b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000e\u00a8\u0006\u001d"}, d2 = {"Lcom/mindguard/data/remote/dto/DailyLifestyleCreate;", "", "sleepHours", "", "screenTimeHours", "activeMinutes", "", "caffeineIntakeMg", "waterIntakeLiters", "(FFIIF)V", "getActiveMinutes", "()I", "getCaffeineIntakeMg", "getScreenTimeHours", "()F", "getSleepHours", "getWaterIntakeLiters", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"})
public final class DailyLifestyleCreate {
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
    
    public DailyLifestyleCreate(float sleepHours, float screenTimeHours, int activeMinutes, int caffeineIntakeMg, float waterIntakeLiters) {
        super();
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
    
    public final float component1() {
        return 0.0F;
    }
    
    public final float component2() {
        return 0.0F;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final float component5() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mindguard.data.remote.dto.DailyLifestyleCreate copy(float sleepHours, float screenTimeHours, int activeMinutes, int caffeineIntakeMg, float waterIntakeLiters) {
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