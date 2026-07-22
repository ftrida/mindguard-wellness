package com.mindguard.data.repository;

import com.mindguard.data.local.dao.JournalDao;
import com.mindguard.data.local.dao.LifestyleDao;
import com.mindguard.data.local.dao.MoodDao;
import com.mindguard.data.remote.api.WellnessApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class WellnessRepository_Factory implements Factory<WellnessRepository> {
  private final Provider<WellnessApi> wellnessApiProvider;

  private final Provider<LifestyleDao> lifestyleDaoProvider;

  private final Provider<MoodDao> moodDaoProvider;

  private final Provider<JournalDao> journalDaoProvider;

  public WellnessRepository_Factory(Provider<WellnessApi> wellnessApiProvider,
      Provider<LifestyleDao> lifestyleDaoProvider, Provider<MoodDao> moodDaoProvider,
      Provider<JournalDao> journalDaoProvider) {
    this.wellnessApiProvider = wellnessApiProvider;
    this.lifestyleDaoProvider = lifestyleDaoProvider;
    this.moodDaoProvider = moodDaoProvider;
    this.journalDaoProvider = journalDaoProvider;
  }

  @Override
  public WellnessRepository get() {
    return newInstance(wellnessApiProvider.get(), lifestyleDaoProvider.get(), moodDaoProvider.get(), journalDaoProvider.get());
  }

  public static WellnessRepository_Factory create(Provider<WellnessApi> wellnessApiProvider,
      Provider<LifestyleDao> lifestyleDaoProvider, Provider<MoodDao> moodDaoProvider,
      Provider<JournalDao> journalDaoProvider) {
    return new WellnessRepository_Factory(wellnessApiProvider, lifestyleDaoProvider, moodDaoProvider, journalDaoProvider);
  }

  public static WellnessRepository newInstance(WellnessApi wellnessApi, LifestyleDao lifestyleDao,
      MoodDao moodDao, JournalDao journalDao) {
    return new WellnessRepository(wellnessApi, lifestyleDao, moodDao, journalDao);
  }
}
