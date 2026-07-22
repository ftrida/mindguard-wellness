package com.mindguard.data.remote.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00032\b\b\u0001\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u000e"}, d2 = {"Lcom/mindguard/data/remote/api/CoachApi;", "", "getMemory", "Lretrofit2/Response;", "", "Lcom/mindguard/data/remote/dto/CoachMemoryItem;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getProactiveAdvice", "Lcom/mindguard/data/remote/dto/CoachAdviceResponse;", "sendChatMessage", "Lcom/mindguard/data/remote/dto/CoachChatResponse;", "request", "Lcom/mindguard/data/remote/dto/CoachChatRequest;", "(Lcom/mindguard/data/remote/dto/CoachChatRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface CoachApi {
    
    @retrofit2.http.POST(value = "api/v1/coach/chat")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sendChatMessage(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.dto.CoachChatRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.mindguard.data.remote.dto.CoachChatResponse>> $completion);
    
    @retrofit2.http.GET(value = "api/v1/coach/advice")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getProactiveAdvice(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.mindguard.data.remote.dto.CoachAdviceResponse>> $completion);
    
    @retrofit2.http.GET(value = "api/v1/coach/conversation")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getMemory(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.mindguard.data.remote.dto.CoachMemoryItem>>> $completion);
}