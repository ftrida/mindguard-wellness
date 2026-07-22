package com.mindguard.core.di;

import com.mindguard.data.remote.api.TwinApi;
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
public final class NetworkModule_ProvideTwinApiFactory implements Factory<TwinApi> {
  private final Provider<Retrofit> retrofitProvider;

  public NetworkModule_ProvideTwinApiFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public TwinApi get() {
    return provideTwinApi(retrofitProvider.get());
  }

  public static NetworkModule_ProvideTwinApiFactory create(Provider<Retrofit> retrofitProvider) {
    return new NetworkModule_ProvideTwinApiFactory(retrofitProvider);
  }

  public static TwinApi provideTwinApi(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideTwinApi(retrofit));
  }
}
