package com.mindguard.data.remote.api

import com.mindguard.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<UserResponse>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    @GET("api/v1/auth/me")
    suspend fun getCurrentUser(): Response<UserResponse>
}

interface WellnessApi {
    @GET("api/v1/profile/me")
    suspend fun getProfile(): Response<ProfileResponse>

    @POST("api/v1/profile")
    suspend fun updateProfile(@Body request: ProfileRequest): Response<ProfileResponse>

    @POST("api/v1/lifestyle")
    suspend fun logLifestyle(@Body request: DailyLifestyleCreate): Response<DailyLifestyleResponse>

    @GET("api/v1/lifestyle/today")
    suspend fun getTodayLifestyle(): Response<DailyLifestyleResponse>

    @POST("api/v1/mood")
    suspend fun logMood(@Body request: MoodCreate): Response<MoodResponse>

    @GET("api/v1/mood/history")
    suspend fun getMoodHistory(): Response<List<MoodResponse>>

    @GET("api/v1/journal")
    suspend fun getJournals(): Response<List<JournalResponse>>

    @POST("api/v1/journal")
    suspend fun createJournal(@Body request: JournalCreate): Response<JournalResponse>

    @POST("api/v1/meditation")
    suspend fun logMeditation(@Body request: MeditationCreate): Response<MeditationResponse>

    @POST("api/v1/focus")
    suspend fun logFocus(@Body request: FocusSessionCreate): Response<FocusSessionResponse>

    @GET("api/v1/profile/emergency-contacts")
    suspend fun getEmergencyContacts(): Response<List<EmergencyContactResponse>>

    @POST("api/v1/profile/emergency-contacts")
    suspend fun createEmergencyContact(@Body request: EmergencyContactCreate): Response<EmergencyContactResponse>
}

interface TwinApi {
    @GET("api/v1/twin")
    suspend fun getTwin(): Response<DigitalTwinResponse>

    @GET("api/v1/twin/compare")
    suspend fun getTwinCompare(): Response<Map<String, Any>>
}

interface BehaviorApi {
    @GET("api/v1/behavior/drift")
    suspend fun getBehaviorDrift(): Response<BehaviorDriftResponse>
}

interface StressApi {
    @GET("api/v1/stress/assessment")
    suspend fun getStressAssessment(): Response<StressLikelihoodResponse>
}

interface CoachApi {
    @POST("api/v1/coach/chat")
    suspend fun sendChatMessage(@Body request: CoachChatRequest): Response<CoachChatResponse>

    @GET("api/v1/coach/advice")
    suspend fun getProactiveAdvice(): Response<CoachAdviceResponse>

    @GET("api/v1/coach/conversation")
    suspend fun getMemory(): Response<List<CoachMemoryItem>>
}

interface RecommendationsApi {
    @GET("api/v1/recommendations")
    suspend fun getRecommendations(): Response<List<RecommendationResponse>>

    @POST("api/v1/recommendations/{id}/complete")
    suspend fun completeRecommendation(@Path("id") id: Long): Response<RecommendationResponse>
}

interface GoalsApi {
    @GET("api/v1/goals")
    suspend fun getGoals(): Response<List<GoalResponse>>

    @POST("api/v1/goals")
    suspend fun createGoal(@Body request: GoalCreate): Response<GoalResponse>
}

interface AchievementsApi {
    @GET("api/v1/achievements")
    suspend fun getAchievements(): Response<List<AchievementResponse>>

    @GET("api/v1/achievements/streak")
    suspend fun getStreak(): Response<StreakResponse>
}

interface ReportsApi {
    @GET("api/v1/reports/daily")
    suspend fun getDailyReport(): Response<Map<String, Any>>

    @GET("api/v1/reports/weekly")
    suspend fun getWeeklyReport(): Response<Map<String, Any>>
}
