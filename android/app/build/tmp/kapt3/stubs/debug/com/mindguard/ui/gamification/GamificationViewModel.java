package com.mindguard.ui.gamification;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0016\u001a\u00020\u0017R\"\u0010\u0005\u001a\u0016\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\"\u0010\n\u001a\u0016\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\b\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R%\u0010\u000e\u001a\u0016\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b\u0018\u00010\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R%\u0010\u0012\u001a\u0016\u0012\u0012\u0012\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\b\u0018\u00010\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u001f\u0010\u0014\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\r\u0018\u00010\u00070\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011\u00a8\u0006\u0018"}, d2 = {"Lcom/mindguard/ui/gamification/GamificationViewModel;", "Landroidx/lifecycle/ViewModel;", "aiRepository", "Lcom/mindguard/data/repository/AIRepository;", "(Lcom/mindguard/data/repository/AIRepository;)V", "_achievements", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/mindguard/core/network/NetworkResult;", "", "Lcom/mindguard/data/remote/dto/AchievementResponse;", "_goals", "Lcom/mindguard/data/remote/dto/GoalResponse;", "_streak", "Lcom/mindguard/data/remote/dto/StreakResponse;", "achievements", "Lkotlinx/coroutines/flow/StateFlow;", "getAchievements", "()Lkotlinx/coroutines/flow/StateFlow;", "goals", "getGoals", "streak", "getStreak", "loadData", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class GamificationViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.mindguard.data.repository.AIRepository aiRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.GoalResponse>>> _goals = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.GoalResponse>>> goals = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.AchievementResponse>>> _achievements = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.AchievementResponse>>> achievements = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StreakResponse>> _streak = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StreakResponse>> streak = null;
    
    @javax.inject.Inject()
    public GamificationViewModel(@org.jetbrains.annotations.NotNull()
    com.mindguard.data.repository.AIRepository aiRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.GoalResponse>>> getGoals() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<java.util.List<com.mindguard.data.remote.dto.AchievementResponse>>> getAchievements() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.mindguard.core.network.NetworkResult<com.mindguard.data.remote.dto.StreakResponse>> getStreak() {
        return null;
    }
    
    public final void loadData() {
    }
}