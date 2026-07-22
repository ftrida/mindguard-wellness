package com.mindguard.ui.dashboard;

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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<AIRepository> aiRepositoryProvider;

  public DashboardViewModel_Factory(Provider<AIRepository> aiRepositoryProvider) {
    this.aiRepositoryProvider = aiRepositoryProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(aiRepositoryProvider.get());
  }

  public static DashboardViewModel_Factory create(Provider<AIRepository> aiRepositoryProvider) {
    return new DashboardViewModel_Factory(aiRepositoryProvider);
  }

  public static DashboardViewModel newInstance(AIRepository aiRepository) {
    return new DashboardViewModel(aiRepository);
  }
}
