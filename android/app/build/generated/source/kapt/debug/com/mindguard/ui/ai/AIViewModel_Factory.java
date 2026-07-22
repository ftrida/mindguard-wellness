package com.mindguard.ui.ai;

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
public final class AIViewModel_Factory implements Factory<AIViewModel> {
  private final Provider<AIRepository> aiRepositoryProvider;

  public AIViewModel_Factory(Provider<AIRepository> aiRepositoryProvider) {
    this.aiRepositoryProvider = aiRepositoryProvider;
  }

  @Override
  public AIViewModel get() {
    return newInstance(aiRepositoryProvider.get());
  }

  public static AIViewModel_Factory create(Provider<AIRepository> aiRepositoryProvider) {
    return new AIViewModel_Factory(aiRepositoryProvider);
  }

  public static AIViewModel newInstance(AIRepository aiRepository) {
    return new AIViewModel(aiRepository);
  }
}
