package com.mindguard.core.di;

import com.mindguard.data.local.dao.JournalDao;
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
public final class DatabaseModule_ProvideJournalDaoFactory implements Factory<JournalDao> {
  private final Provider<MindGuardDatabase> dbProvider;

  public DatabaseModule_ProvideJournalDaoFactory(Provider<MindGuardDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public JournalDao get() {
    return provideJournalDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideJournalDaoFactory create(
      Provider<MindGuardDatabase> dbProvider) {
    return new DatabaseModule_ProvideJournalDaoFactory(dbProvider);
  }

  public static JournalDao provideJournalDao(MindGuardDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideJournalDao(db));
  }
}
