package com.mindguard.core.di;

import com.mindguard.data.remote.api.ReportsApi;
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
public final class NetworkModule_ProvideReportsApiFactory implements Factory<ReportsApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideReportsApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public ReportsApi get() {
    return provideReportsApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideReportsApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideReportsApiFactory(retrofitProvider);
  }

  public static ReportsApi provideReportsApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideReportsApi(retrofit));
  }
}
