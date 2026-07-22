package com.mindguard.core.di;

import com.mindguard.data.remote.api.AchievementsApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class NetworkModule_ProvideAchievementsApiFactory implements Factory<AchievementsApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideAchievementsApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public AchievementsApi get() {
    return provideAchievementsApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideAchievementsApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideAchievementsApiFactory(retrofitProvider);
  }

  public static AchievementsApi provideAchievementsApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideAchievementsApi(retrofit));
  }
}
