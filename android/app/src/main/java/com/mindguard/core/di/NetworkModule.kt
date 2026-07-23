package com.mindguard.core.di

import android.content.Context
import com.mindguard.core.network.AuthInterceptor
import com.mindguard.core.network.TokenAuthenticator
import com.mindguard.data.local.datastore.TokenManager
import com.mindguard.data.remote.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://10.0.2.2:10000/"

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager {
        return TokenManager(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenAuthenticator)
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideWellnessApi(retrofit: Retrofit): WellnessApi = retrofit.create(WellnessApi::class.java)

    @Provides
    @Singleton
    fun provideTwinApi(retrofit: Retrofit): TwinApi = retrofit.create(TwinApi::class.java)

    @Provides
    @Singleton
    fun provideBehaviorApi(retrofit: Retrofit): BehaviorApi = retrofit.create(BehaviorApi::class.java)

    @Provides
    @Singleton
    fun provideStressApi(retrofit: Retrofit): StressApi = retrofit.create(StressApi::class.java)

    @Provides
    @Singleton
    fun provideCoachApi(retrofit: Retrofit): CoachApi = retrofit.create(CoachApi::class.java)

    @Provides
    @Singleton
    fun provideRecommendationsApi(retrofit: Retrofit): RecommendationsApi = retrofit.create(RecommendationsApi::class.java)

    @Provides
    @Singleton
    fun provideGoalsApi(retrofit: Retrofit): GoalsApi = retrofit.create(GoalsApi::class.java)

    @Provides
    @Singleton
    fun provideAchievementsApi(retrofit: Retrofit): AchievementsApi = retrofit.create(AchievementsApi::class.java)

    @Provides
    @Singleton
    fun provideReportsApi(retrofit: Retrofit): ReportsApi = retrofit.create(ReportsApi::class.java)
}
