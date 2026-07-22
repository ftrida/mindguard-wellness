package com.mindguard.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.mindguard.data.repository.WellnessRepository;
import dagger.internal.DaggerGenerated;
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
public final class SyncLogsWorker_Factory {
  private final Provider<WellnessRepository> wellnessRepositoryProvider;

  public SyncLogsWorker_Factory(Provider<WellnessRepository> wellnessRepositoryProvider) {
    this.wellnessRepositoryProvider = wellnessRepositoryProvider;
  }

  public SyncLogsWorker get(Context context, WorkerParameters params) {
    return newInstance(context, params, wellnessRepositoryProvider.get());
  }

  public static SyncLogsWorker_Factory create(
      Provider<WellnessRepository> wellnessRepositoryProvider) {
    return new SyncLogsWorker_Factory(wellnessRepositoryProvider);
  }

  public static SyncLogsWorker newInstance(Context context, WorkerParameters params,
      WellnessRepository wellnessRepository) {
    return new SyncLogsWorker(context, params, wellnessRepository);
  }
}
