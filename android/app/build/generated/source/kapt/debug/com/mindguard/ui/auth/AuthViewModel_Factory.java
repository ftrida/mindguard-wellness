package com.mindguard.ui.auth;

import com.mindguard.data.local.datastore.TokenManager;
import com.mindguard.data.repository.AuthRepository;
import com.mindguard.data.repository.WellnessRepository;
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<WellnessRepository> wellnessRepositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  public AuthViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<WellnessRepository> wellnessRepositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.wellnessRepositoryProvider = wellnessRepositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(authRepositoryProvider.get(), wellnessRepositoryProvider.get(), tokenManagerProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<WellnessRepository> wellnessRepositoryProvider,
      Provider<TokenManager> tokenManagerProvider) {
    return new AuthViewModel_Factory(authRepositoryProvider, wellnessRepositoryProvider, tokenManagerProvider);
  }

  public static AuthViewModel newInstance(AuthRepository authRepository,
      WellnessRepository wellnessRepository, TokenManager tokenManager) {
    return new AuthViewModel(authRepository, wellnessRepository, tokenManager);
  }
}
