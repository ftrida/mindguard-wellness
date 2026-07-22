package com.mindguard.ui.wellness;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015J\u0006\u0010\u0017\u001a\u00020\u0013J\u001e\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001a2\u0006\u0010\u001c\u001a\u00020\u001dJ\u0016\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u001d2\u0006\u0010 \u001a\u00020\u0015R\"\u0010\u0005\u001a\u0016\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\n\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R%\u0010\f\u001a\u0016\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b\u0018\u00010\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001f\u0010\u0010\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u000b\u0018\u00010\u00070\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/mindguard/ui/wellness/WellnessViewModel;", "Landroidx/lifecycle/ViewModel;", "wellnessRepository", "Lcom/mindguard/data/repository/WellnessRepository;", "(Lcom/mindguard/data/repository/WellnessRepository;)V", "_contacts", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/mindguard/core/network/NetworkResult;", "", "Lcom/mindguard/data/remote/dto/EmergencyContactResponse;", "_logResult", "", "contacts", "Lkotlinx/coroutines/flow/StateFlow;", "getContacts", "()Lkotlinx/coroutines/flow/StateFlow;", "logResult", "getLogResult", "createJournal", "", "title", "", "content", "loadEmergencyContacts", "logLifestyle", "sleep", "", "screen", "active", "", "logMood", "score", "notes", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class WellnessViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.repository.WellnessRepository wellnessRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<java.lang.Object>> _logResult = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<java.lang.Object>> logResult = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.EmergencyContactResponse>>> _contacts = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.EmergencyContactResponse>>> contacts = null;
    
    @javax.inject.Inject()
    public WellnessViewModel(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.repository.WellnessRepository wellnessRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<java.lang.Object>> getLogResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.EmergencyContactResponse>>> getContacts() {
        return null;
    }
    
    public final void logLifestyle(float sleep, float screen, int active) {
    }
    
    public final void logMood(int score, @org.jetbrains.annotations.NotNull()
    java.lang.String notes) {
    }
    
    public final void createJournal(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String content) {
    }
    
    public final void loadEmergencyContacts() {
    }
}