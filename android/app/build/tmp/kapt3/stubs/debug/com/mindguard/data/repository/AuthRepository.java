package com.mindguard.data.repository;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u001a\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\t0\b2\u0006\u0010\u000b\u001a\u00020\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/mindguard/data/repository/AuthRepository;", "", "authApi", "Lcom/mindguard/data/remote/api/AuthApi;", "tokenManager", "Lcom/mindguard/data/local/datastore/TokenManager;", "(Lcom/mindguard/data/remote/api/AuthApi;Lcom/mindguard/data/local/datastore/TokenManager;)V", "login", "Lkotlinx/coroutines/flow/Flow;", "Lcom/mindguard/core/network/NetworkResult;", "Lcom/mindguard/data/remote/dto/TokenResponse;", "request", "Lcom/mindguard/data/remote/dto/LoginRequest;", "logout", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "register", "Lcom/mindguard/data/remote/dto/UserResponse;", "Lcom/mindguard/data/remote/dto/RegisterRequest;", "app_debug"})
public final class AuthRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.remote.api.AuthApi authApi = null;
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.local.datastore.TokenManager tokenManager = null;
    
    @javax.inject.Inject()
    public AuthRepository(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.api.AuthApi authApi, @org.jetbrains.annotations.NotNull()
    com.mindguard.data.local.datastore.TokenManager tokenManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.TokenResponse>> login(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.dto.LoginRequest request) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.UserResponse>> register(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.remote.dto.RegisterRequest request) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object logout(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}