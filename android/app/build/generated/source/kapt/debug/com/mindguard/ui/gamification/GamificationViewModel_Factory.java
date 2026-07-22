package com.mindguard.ui.gamification;

import com.mindguard.data.repository.AIRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class GamificationViewModel_Factory implements Factory<GamificationViewModel> {
  private final Provider<AIRepository> aiRepositoryProvider;

  public GamificationViewModel_Factory(Provider<AIRepository> aiRepositoryProvider) {
    this.aiRepositoryProvider = aiRepositoryProvider;
  }

  @Override
  public GamificationViewModel get() {
    return newInstance(aiRepositoryProvider.get());
  }

  public static GamificationViewModel_Factory create(Provider<AIRepository> aiRepositoryProvider) {
    return new GamificationViewModel_Factory(aiRepositoryProvider);
  }

  public static GamificationViewModel newInstance(AIRepository aiRepository) {
    return new GamificationViewModel(aiRepository);
  }
}
