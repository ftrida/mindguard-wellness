package com.mindguard.core.di;

import com.mindguard.data.local.dao.LifestyleDao;
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
public final class DatabaseModule_ProvideLifestyleDaoFactory implements Factory<LifestyleDao> {
  private final Provider<MindGuardDatabase> dbProvider;

  public DatabaseModule_ProvideLifestyleDaoFactory(Provider<MindGuardDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public LifestyleDao get() {
    return provideLifestyleDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideLifestyleDaoFactory create(
      Provider<MindGuardDatabase> dbProvider) {
    return new DatabaseModule_ProvideLifestyleDaoFactory(dbProvider);
  }

  public static LifestyleDao provideLifestyleDao(MindGuardDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideLifestyleDao(db));
  }
}
