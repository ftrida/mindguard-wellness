package com.mindguard;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.mindguard.core.di.DatabaseModule_ProvideDatabaseFactory;
import com.mindguard.core.di.DatabaseModule_ProvideJournalDaoFactory;
import com.mindguard.core.di.DatabaseModule_ProvideLifestyleDaoFactory;
import com.mindguard.core.di.DatabaseModule_ProvideMoodDaoFactory;
import com.mindguard.core.di.NetworkModule_ProvideAchievementsApiFactory;
import com.mindguard.core.di.NetworkModule_ProvideAuthApiFactory;
import com.mindguard.core.di.NetworkModule_ProvideBehaviorApiFactory;
import com.mindguard.core.di.NetworkModule_ProvideCoachApiFactory;
import com.mindguard.core.di.NetworkModule_ProvideGoalsApiFactory;
import com.mindguard.core.di.NetworkModule_ProvideOkHttpClientFactory;
import com.mindguard.core.di.NetworkModule_ProvideRecommendationsApiFactory;
import com.mindguard.core.di.NetworkModule_ProvideRetrofitFactory;
import com.mindguard.core.di.NetworkModule_ProvideStressApiFactory;
import com.mindguard.core.di.NetworkModule_ProvideTokenManagerFactory;
import com.mindguard.core.di.NetworkModule_ProvideTwinApiFactory;
import com.mindguard.core.di.NetworkModule_ProvideWellnessApiFactory;
import com.mindguard.core.network.AuthInterceptor;
import com.mindguard.core.network.TokenAuthenticator;
import com.mindguard.data.local.dao.JournalDao;
import com.mindguard.data.local.dao.LifestyleDao;
import com.mindguard.data.local.dao.MoodDao;
import com.mindguard.data.local.datastore.TokenManager;
import com.mindguard.data.local.db.MindGuardDatabase;
import com.mindguard.data.remote.api.AchievementsApi;
import com.mindguard.data.remote.api.AuthApi;
import com.mindguard.data.remote.api.BehaviorApi;
import com.mindguard.data.remote.api.CoachApi;
import com.mindguard.data.remote.api.GoalsApi;
import com.mindguard.data.remote.api.RecommendationsApi;
import com.mindguard.data.remote.api.StressApi;
import com.mindguard.data.remote.api.TwinApi;
import com.mindguard.data.remote.api.WellnessApi;
import com.mindguard.data.repository.AIRepository;
import com.mindguard.data.repository.AuthRepository;
import com.mindguard.data.repository.WellnessRepository;
import com.mindguard.ui.ai.AIViewModel;
import com.mindguard.ui.ai.AIViewModel_HiltModules_KeyModule_ProvideFactory;
import com.mindguard.ui.auth.AuthViewModel;
import com.mindguard.ui.auth.AuthViewModel_HiltModules_KeyModule_ProvideFactory;
import com.mindguard.ui.auth.SplashViewModel;
import com.mindguard.ui.auth.SplashViewModel_HiltModules_KeyModule_ProvideFactory;
import com.mindguard.ui.dashboard.DashboardViewModel;
import com.mindguard.ui.dashboard.DashboardViewModel_HiltModules_KeyModule_ProvideFactory;
import com.mindguard.ui.gamification.GamificationViewModel;
import com.mindguard.ui.gamification.GamificationViewModel_HiltModules_KeyModule_ProvideFactory;
import com.mindguard.ui.wellness.WellnessViewModel;
import com.mindguard.ui.wellness.WellnessViewModel_HiltModules_KeyModule_ProvideFactory;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SetBuilder;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class DaggerMindGuardApplication_HiltComponents_SingletonC {
  private DaggerMindGuardApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public MindGuardApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements MindGuardApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public MindGuardApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements MindGuardApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public MindGuardApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements MindGuardApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public MindGuardApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements MindGuardApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public MindGuardApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements MindGuardApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public MindGuardApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements MindGuardApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public MindGuardApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements MindGuardApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public MindGuardApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends MindGuardApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends MindGuardApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends MindGuardApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends MindGuardApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return SetBuilder.<String>newSetBuilder(6).add(AIViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(AuthViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(DashboardViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(GamificationViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(SplashViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(WellnessViewModel_HiltModules_KeyModule_ProvideFactory.provide()).build();
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }
  }

  private static final class ViewModelCImpl extends MindGuardApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AIViewModel> aIViewModelProvider;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<GamificationViewModel> gamificationViewModelProvider;

    private Provider<SplashViewModel> splashViewModelProvider;

    private Provider<WellnessViewModel> wellnessViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.aIViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.gamificationViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.splashViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.wellnessViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
    }

    @Override
    public Map<String, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(6).put("com.mindguard.ui.ai.AIViewModel", ((Provider) aIViewModelProvider)).put("com.mindguard.ui.auth.AuthViewModel", ((Provider) authViewModelProvider)).put("com.mindguard.ui.dashboard.DashboardViewModel", ((Provider) dashboardViewModelProvider)).put("com.mindguard.ui.gamification.GamificationViewModel", ((Provider) gamificationViewModelProvider)).put("com.mindguard.ui.auth.SplashViewModel", ((Provider) splashViewModelProvider)).put("com.mindguard.ui.wellness.WellnessViewModel", ((Provider) wellnessViewModelProvider)).build();
    }

    @Override
    public Map<String, Object> getHiltViewModelAssistedMap() {
      return Collections.<String, Object>emptyMap();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.mindguard.ui.ai.AIViewModel 
          return (T) new AIViewModel(singletonCImpl.aIRepositoryProvider.get());

          case 1: // com.mindguard.ui.auth.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.wellnessRepositoryProvider.get(), singletonCImpl.provideTokenManagerProvider.get());

          case 2: // com.mindguard.ui.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.aIRepositoryProvider.get());

          case 3: // com.mindguard.ui.gamification.GamificationViewModel 
          return (T) new GamificationViewModel(singletonCImpl.aIRepositoryProvider.get());

          case 4: // com.mindguard.ui.auth.SplashViewModel 
          return (T) new SplashViewModel(singletonCImpl.provideTokenManagerProvider.get());

          case 5: // com.mindguard.ui.wellness.WellnessViewModel 
          return (T) new WellnessViewModel(singletonCImpl.wellnessRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends MindGuardApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends MindGuardApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends MindGuardApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<TokenManager> provideTokenManagerProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<TwinApi> provideTwinApiProvider;

    private Provider<BehaviorApi> provideBehaviorApiProvider;

    private Provider<StressApi> provideStressApiProvider;

    private Provider<CoachApi> provideCoachApiProvider;

    private Provider<RecommendationsApi> provideRecommendationsApiProvider;

    private Provider<GoalsApi> provideGoalsApiProvider;

    private Provider<AchievementsApi> provideAchievementsApiProvider;

    private Provider<AIRepository> aIRepositoryProvider;

    private Provider<AuthApi> provideAuthApiProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<WellnessApi> provideWellnessApiProvider;

    private Provider<MindGuardDatabase> provideDatabaseProvider;

    private Provider<WellnessRepository> wellnessRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private AuthInterceptor authInterceptor() {
      return new AuthInterceptor(provideTokenManagerProvider.get());
    }

    private TokenAuthenticator tokenAuthenticator() {
      return new TokenAuthenticator(provideTokenManagerProvider.get());
    }

    private LifestyleDao lifestyleDao() {
      return DatabaseModule_ProvideLifestyleDaoFactory.provideLifestyleDao(provideDatabaseProvider.get());
    }

    private MoodDao moodDao() {
      return DatabaseModule_ProvideMoodDaoFactory.provideMoodDao(provideDatabaseProvider.get());
    }

    private JournalDao journalDao() {
      return DatabaseModule_ProvideJournalDaoFactory.provideJournalDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideTokenManagerProvider = DoubleCheck.provider(new SwitchingProvider<TokenManager>(singletonCImpl, 4));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 3));
      this.provideRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 2));
      this.provideTwinApiProvider = DoubleCheck.provider(new SwitchingProvider<TwinApi>(singletonCImpl, 1));
      this.provideBehaviorApiProvider = DoubleCheck.provider(new SwitchingProvider<BehaviorApi>(singletonCImpl, 5));
      this.provideStressApiProvider = DoubleCheck.provider(new SwitchingProvider<StressApi>(singletonCImpl, 6));
      this.provideCoachApiProvider = DoubleCheck.provider(new SwitchingProvider<CoachApi>(singletonCImpl, 7));
      this.provideRecommendationsApiProvider = DoubleCheck.provider(new SwitchingProvider<RecommendationsApi>(singletonCImpl, 8));
      this.provideGoalsApiProvider = DoubleCheck.provider(new SwitchingProvider<GoalsApi>(singletonCImpl, 9));
      this.provideAchievementsApiProvider = DoubleCheck.provider(new SwitchingProvider<AchievementsApi>(singletonCImpl, 10));
      this.aIRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AIRepository>(singletonCImpl, 0));
      this.provideAuthApiProvider = DoubleCheck.provider(new SwitchingProvider<AuthApi>(singletonCImpl, 12));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 11));
      this.provideWellnessApiProvider = DoubleCheck.provider(new SwitchingProvider<WellnessApi>(singletonCImpl, 14));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<MindGuardDatabase>(singletonCImpl, 15));
      this.wellnessRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<WellnessRepository>(singletonCImpl, 13));
    }

    @Override
    public void injectMindGuardApplication(MindGuardApplication mindGuardApplication) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.mindguard.data.repository.AIRepository 
          return (T) new AIRepository(singletonCImpl.provideTwinApiProvider.get(), singletonCImpl.provideBehaviorApiProvider.get(), singletonCImpl.provideStressApiProvider.get(), singletonCImpl.provideCoachApiProvider.get(), singletonCImpl.provideRecommendationsApiProvider.get(), singletonCImpl.provideGoalsApiProvider.get(), singletonCImpl.provideAchievementsApiProvider.get());

          case 1: // com.mindguard.data.remote.api.TwinApi 
          return (T) NetworkModule_ProvideTwinApiFactory.provideTwinApi(singletonCImpl.provideRetrofitProvider.get());

          case 2: // retrofit2.Retrofit 
          return (T) NetworkModule_ProvideRetrofitFactory.provideRetrofit(singletonCImpl.provideOkHttpClientProvider.get());

          case 3: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient(singletonCImpl.authInterceptor(), singletonCImpl.tokenAuthenticator());

          case 4: // com.mindguard.data.local.datastore.TokenManager 
          return (T) NetworkModule_ProvideTokenManagerFactory.provideTokenManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.mindguard.data.remote.api.BehaviorApi 
          return (T) NetworkModule_ProvideBehaviorApiFactory.provideBehaviorApi(singletonCImpl.provideRetrofitProvider.get());

          case 6: // com.mindguard.data.remote.api.StressApi 
          return (T) NetworkModule_ProvideStressApiFactory.provideStressApi(singletonCImpl.provideRetrofitProvider.get());

          case 7: // com.mindguard.data.remote.api.CoachApi 
          return (T) NetworkModule_ProvideCoachApiFactory.provideCoachApi(singletonCImpl.provideRetrofitProvider.get());

          case 8: // com.mindguard.data.remote.api.RecommendationsApi 
          return (T) NetworkModule_ProvideRecommendationsApiFactory.provideRecommendationsApi(singletonCImpl.provideRetrofitProvider.get());

          case 9: // com.mindguard.data.remote.api.GoalsApi 
          return (T) NetworkModule_ProvideGoalsApiFactory.provideGoalsApi(singletonCImpl.provideRetrofitProvider.get());

          case 10: // com.mindguard.data.remote.api.AchievementsApi 
          return (T) NetworkModule_ProvideAchievementsApiFactory.provideAchievementsApi(singletonCImpl.provideRetrofitProvider.get());

          case 11: // com.mindguard.data.repository.AuthRepository 
          return (T) new AuthRepository(singletonCImpl.provideAuthApiProvider.get(), singletonCImpl.provideTokenManagerProvider.get());

          case 12: // com.mindguard.data.remote.api.AuthApi 
          return (T) NetworkModule_ProvideAuthApiFactory.provideAuthApi(singletonCImpl.provideRetrofitProvider.get());

          case 13: // com.mindguard.data.repository.WellnessRepository 
          return (T) new WellnessRepository(singletonCImpl.provideWellnessApiProvider.get(), singletonCImpl.lifestyleDao(), singletonCImpl.moodDao(), singletonCImpl.journalDao());

          case 14: // com.mindguard.data.remote.api.WellnessApi 
          return (T) NetworkModule_ProvideWellnessApiFactory.provideWellnessApi(singletonCImpl.provideRetrofitProvider.get());

          case 15: // com.mindguard.data.local.db.MindGuardDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
