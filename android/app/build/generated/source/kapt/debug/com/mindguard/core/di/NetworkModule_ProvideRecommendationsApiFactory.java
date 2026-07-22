package com.mindguard.core.di;

import com.mindguard.data.remote.api.RecommendationsApi;
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
public final class NetworkModule_ProvideRecommendationsApiFactory implements Factory<RecommendationsApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideRecommendationsApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public RecommendationsApi get() {
    return provideRecommendationsApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideRecommendationsApiFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideRecommendationsApiFactory(retrofitProvider);
  }

  public static RecommendationsApi provideRecommendationsApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideRecommendationsApi(retrofit));
  }
}
