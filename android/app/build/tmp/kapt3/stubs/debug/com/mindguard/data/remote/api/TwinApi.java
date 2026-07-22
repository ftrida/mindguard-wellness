package com.mindguard.data.remote.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J \u0010\u0006\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u00010\u00070\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005\u00a8\u0006\t"}, d2 = {"Lcom/mindguard/data/remote/api/TwinApi;", "", "getTwin", "Lretrofit2/Response;", "Lcom/mindguard/data/remote/dto/DigitalTwinResponse;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTwinCompare", "", "", "app_debug"})
public abstract interface TwinApi {
    
    @retrofit2.http.GET(value = "api/v1/twin")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTwin(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.mindguard.data.remote.dto.DigitalTwinResponse>> $completion);
    
    @retrofit2.http.GET(value = "api/v1/twin/compare")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTwinCompare(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.Map<java.lang.String, java.lang.Object>>> $completion);
}