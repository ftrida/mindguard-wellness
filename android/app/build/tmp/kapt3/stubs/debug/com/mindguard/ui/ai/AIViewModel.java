package com.mindguard.ui.ai;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\"\u001a\u00020#J\u0006\u0010$\u001a\u00020#J\u0006\u0010%\u001a\u00020#J\u0006\u0010&\u001a\u00020#J\u0006\u0010\'\u001a\u00020#J\u000e\u0010(\u001a\u00020#2\u0006\u0010)\u001a\u00020*R\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\t\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\u000e\u001a\u0016\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\f\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0010\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0012\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001f\u0010\u0014\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u001f\u0010\u0018\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u00070\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0017R\u001d\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0017R%\u0010\u001c\u001a\u0016\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\f\u0018\u00010\u00070\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0017R\u001f\u0010\u001e\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0011\u0018\u00010\u00070\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u0017R\u001f\u0010 \u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010\u00070\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0017\u00a8\u0006+"}, d2 = {"Lcom/mindguard/ui/ai/AIViewModel;", "Landroidx/lifecycle/ViewModel;", "aiRepository", "Lcom/mindguard/data/repository/AIRepository;", "(Lcom/mindguard/data/repository/AIRepository;)V", "_chatResponse", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/mindguard/core/network/NetworkResult;", "Lcom/mindguard/data/remote/dto/CoachChatResponse;", "_drift", "Lcom/mindguard/data/remote/dto/BehaviorDriftResponse;", "_memory", "", "Lcom/mindguard/data/remote/dto/CoachMemoryItem;", "_recs", "Lcom/mindguard/data/remote/dto/RecommendationResponse;", "_stress", "Lcom/mindguard/data/remote/dto/StressLikelihoodResponse;", "_twin", "Lcom/mindguard/data/remote/dto/DigitalTwinResponse;", "chatResponse", "Lkotlinx/coroutines/flow/StateFlow;", "getChatResponse", "()Lkotlinx/coroutines/flow/StateFlow;", "drift", "getDrift", "memory", "getMemory", "recs", "getRecs", "stress", "getStress", "twin", "getTwin", "loadDrift", "", "loadMemory", "loadRecs", "loadStress", "loadTwin", "sendMessage", "msg", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class AIViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.repository.AIRepository aiRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.DigitalTwinResponse>> _twin = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.DigitalTwinResponse>> twin = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.BehaviorDriftResponse>> _drift = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.BehaviorDriftResponse>> drift = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StressLikelihoodResponse>> _stress = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StressLikelihoodResponse>> stress = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.CoachChatResponse>> _chatResponse = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.CoachChatResponse>> chatResponse = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.mindguard.data.remote.dto.CoachMemoryItem>> _memory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mindguard.data.remote.dto.CoachMemoryItem>> memory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.RecommendationResponse>>> _recs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.RecommendationResponse>>> recs = null;
    
    @javax.inject.Inject()
    public AIViewModel(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.repository.AIRepository aiRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.DigitalTwinResponse>> getTwin() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.BehaviorDriftResponse>> getDrift() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StressLikelihoodResponse>> getStress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.CoachChatResponse>> getChatResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.mindguard.data.remote.dto.CoachMemoryItem>> getMemory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.RecommendationResponse>>> getRecs() {
        return null;
    }
    
    public final void loadTwin() {
    }
    
    public final void loadDrift() {
    }
    
    public final void loadStress() {
    }
    
    public final void loadRecs() {
    }
    
    public final void loadMemory() {
    }
    
    public final void sendMessage(@org.jetbrains.annotations.NotNull()
    java.lang.String msg) {
    }
}