package com.mindguard.core.di;

import com.mindguard.data.local.dao.MoodDao;
import com.mindguard.data.local.db.MindGuardDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideMoodDaoFactory implements Factory<MoodDao> {
  private final Provider<MindGuardDatabase> dbProvider;

  public DatabaseModule_ProvideMoodDaoFactory(Provider<MindGuardDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public MoodDao get() {
    return provideMoodDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideMoodDaoFactory create(
      Provider<MindGuardDatabase> dbProvider) {
    return new DatabaseModule_ProvideMoodDaoFactory(dbProvider);
  }

  public static MoodDao provideMoodDao(MindGuardDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMoodDao(db));
  }
}
