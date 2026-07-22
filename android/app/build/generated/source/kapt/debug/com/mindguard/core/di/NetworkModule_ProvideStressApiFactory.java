package com.mindguard.core.di;

import com.mindguard.data.remote.api.StressApi;
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
public final class NetworkModule_ProvideStressApiFactory implements Factory<StressApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideStressApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public StressApi get() {
    return provideStressApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideStressApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideStressApiFactory(retrofitProvider);
  }

  public static StressApi provideStressApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideStressApi(retrofit));
  }
}
