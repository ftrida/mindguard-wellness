package com.mindguard.data.repository;

import com.mindguard.data.remote.api.AchievementsApi;
import com.mindguard.data.remote.api.BehaviorApi;
import com.mindguard.data.remote.api.CoachApi;
import com.mindguard.data.remote.api.GoalsApi;
import com.mindguard.data.remote.api.RecommendationsApi;
import com.mindguard.data.remote.api.StressApi;
import com.mindguard.data.remote.api.TwinApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AIRepository_Factory implements Factory<AIRepository> {
  private final Provider<TwinApi> twinApiProvider;

  private final Provider<BehaviorApi> behaviorApiProvider;

  private final Provider<StressApi> stressApiProvider;

  private final Provider<CoachApi> coachApiProvider;

  private final Provider<RecommendationsApi> recommendationsApiProvider;

  private final Provider<GoalsApi> goalsApiProvider;

  private final Provider<AchievementsApi> achievementsApiProvider;

  public AIRepository_Factory(Provider<TwinApi> twinApiProvider,
      Provider<BehaviorApi> behaviorApiProvider, Provider<StressApi> stressApiProvider,
      Provider<CoachApi> coachApiProvider, Provider<RecommendationsApi> recommendationsApiProvider,
      Provider<GoalsApi> goalsApiProvider, Provider<AchievementsApi> achievementsApiProvider) {
    this.twinApiProvider = twinApiProvider;
    this.behaviorApiProvider = behaviorApiProvider;
    this.stressApiProvider = stressApiProvider;
    this.coachApiProvider = coachApiProvider;
    this.recommendationsApiProvider = recommendationsApiProvider;
    this.goalsApiProvider = goalsApiProvider;
    this.achievementsApiProvider = achievementsApiProvider;
  }

  @Override
  public AIRepository get() {
    return newInstance(twinApiProvider.get(), behaviorApiProvider.get(), stressApiProvider.get(), coachApiProvider.get(), recommendationsApiProvider.get(), goalsApiProvider.get(), achievementsApiProvider.get());
  }

  public static AIRepository_Factory create(Provider<TwinApi> twinApiProvider,
      Provider<BehaviorApi> behaviorApiProvider, Provider<StressApi> stressApiProvider,
      Provider<CoachApi> coachApiProvider, Provider<RecommendationsApi> recommendationsApiProvider,
      Provider<GoalsApi> goalsApiProvider, Provider<AchievementsApi> achievementsApiProvider) {
    return new AIRepository_Factory(twinApiProvider, behaviorApiProvider, stressApiProvider, coachApiProvider, recommendationsApiProvider, goalsApiProvider, achievementsApiProvider);
  }

  public static AIRepository newInstance(TwinApi twinApi, BehaviorApi behaviorApi,
      StressApi stressApi, CoachApi coachApi, RecommendationsApi recommendationsApi,
      GoalsApi goalsApi, AchievementsApi achievementsApi) {
    return new AIRepository(twinApi, behaviorApi, stressApi, coachApi, recommendationsApi, goalsApi, achievementsApi);
  }
}
