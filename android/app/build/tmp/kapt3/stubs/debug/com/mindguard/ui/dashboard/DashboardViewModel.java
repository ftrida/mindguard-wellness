package com.mindguard.ui.dashboard;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0011\u001a\u00020\u0012R\u001c\u0010\u0005\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\t\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001f\u0010\u000b\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001f\u0010\u000f\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\n\u0018\u00010\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000e\u00a8\u0006\u0013"}, d2 = {"Lcom/mindguard/ui/dashboard/DashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "aiRepository", "Lcom/mindguard/data/repository/AIRepository;", "(Lcom/mindguard/data/repository/AIRepository;)V", "_stressState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/mindguard/core/network/NetworkResult;", "Lcom/mindguard/data/remote/dto/StressLikelihoodResponse;", "_twinState", "Lcom/mindguard/data/remote/dto/DigitalTwinResponse;", "stressState", "Lkotlinx/coroutines/flow/StateFlow;", "getStressState", "()Lkotlinx/coroutines/flow/StateFlow;", "twinState", "getTwinState", "loadDashboardData", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.repository.AIRepository aiRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.DigitalTwinResponse>> _twinState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.DigitalTwinResponse>> twinState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StressLikelihoodResponse>> _stressState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StressLikelihoodResponse>> stressState = null;
    
    @javax.inject.Inject()
    public DashboardViewModel(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.repository.AIRepository aiRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.DigitalTwinResponse>> getTwinState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StressLikelihoodResponse>> getStressState() {
        return null;
    }
    
    public final void loadDashboardData() {
    }
}