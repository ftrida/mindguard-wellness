package com.mindguard.data.remote.dto

import com.google.gson.annotations.SerializedName

// --- Authentication DTOs ---
data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
    @SerializedName("full_name") val fullName: String? = null
)

data class LoginRequest(
    @SerializedName("email_or_username") val emailOrUsername: String,
    val password: String
)

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("token_type") val tokenType: String = "bearer"
)

data class UserResponse(
    val id: Long,
    val email: String,
    val username: String,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("is_verified") val isVerified: Boolean
)

// --- Wellness DTOs ---
data class ProfileRequest(
    @SerializedName("profile_photo_url") val profilePhotoUrl: String? = null,
    val dob: String? = null,
    val gender: String? = null,
    val height: Float? = null,
    val weight: Float? = null,
    val timezone: String? = "UTC",
    val country: String? = null,
    val language: String? = "en",
    val occupation: String? = null,
    @SerializedName("emergency_preferences") val emergencyPreferences: String? = null
)

data class ProfileResponse(
    val id: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("profile_photo_url") val profilePhotoUrl: String?,
    val dob: String?,
    val gender: String?,
    val height: Float?,
    val weight: Float?,
    val timezone: String,
    val country: String?,
    val language: String,
    val occupation: String?,
    @SerializedName("emergency_preferences") val emergencyPreferences: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class DailyLifestyleCreate(
    @SerializedName("log_date") val logDate: String,
    @SerializedName("sleep_hours") val sleepHours: Float,
    @SerializedName("screen_time") val screenTime: Float,
    @SerializedName("exercise_minutes") val exerciseMinutes: Int,
    @SerializedName("water_intake") val waterIntake: Float = 0.0f
)

data class DailyLifestyleResponse(
    val id: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("log_date") val logDate: String,
    @SerializedName("sleep_hours") val sleepHours: Float,
    @SerializedName("screen_time") val screenTime: Float,
    @SerializedName("exercise_minutes") val exerciseMinutes: Int,
    @SerializedName("water_intake") val waterIntake: Float
)

data class MoodCreate(
    @SerializedName("mood_score") val moodScore: Int,
    val notes: String? = null,
    val category: String = "Calm"
)

data class MoodResponse(
    val id: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("mood_score") val moodScore: Int,
    val notes: String?,
    val category: String,
    @SerializedName("entry_time") val entryTime: String
)

data class JournalCreate(
    val title: String,
    val content: String,
    val category: String = "General",
    val tags: List<String> = emptyList()
)

data class JournalResponse(
    val id: Long,
    @SerializedName("user_id") val userId: Long,
    val title: String,
    val content: String,
    val category: String,
    @SerializedName("sentiment_score") val sentimentScore: Float?,
    @SerializedName("created_at") val createdAt: String
)

data class MeditationCreate(
    @SerializedName("duration_seconds") val durationSeconds: Int,
    val category: String = "Mindfulness"
)

data class MeditationResponse(
    val id: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("duration_seconds") val durationSeconds: Int,
    val category: String,
    @SerializedName("completed_at") val completedAt: String
)

data class FocusSessionCreate(
    @SerializedName("custom_duration_seconds") val customDurationSeconds: Int,
    @SerializedName("break_duration_seconds") val breakDurationSeconds: Int = 0,
    @SerializedName("completed_sessions_count") val completedSessionsCount: Int = 1
)

data class FocusSessionResponse(
    val id: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("custom_duration_seconds") val customDurationSeconds: Int,
    @SerializedName("completed_at") val completedAt: String
)

// --- AI Intelligence DTOs ---
data class DigitalTwinResponse(
    val id: Long,
    @SerializedName("wellness_score") val wellnessScore: Float,
    @SerializedName("sleep_baseline") val sleepBaseline: Float,
    @SerializedName("screen_time_baseline") val screenTimeBaseline: Float,
    @SerializedName("mood_baseline") val moodBaseline: Float,
    @SerializedName("active_minutes_baseline") val activeMinutesBaseline: Float
)

data class BehaviorDriftResponse(
    val id: Long,
    @SerializedName("drift_score") val driftScore: Float,
    @SerializedName("consistency_score") val consistencyScore: Float,
    val explanation: String
)

data class StressLikelihoodResponse(
    val id: Long,
    @SerializedName("stress_score") val stressScore: Float,
    @SerializedName("confidence_score") val confidenceScore: Float,
    @SerializedName("contributing_factors") val contributingFactors: Map<String, Any>?
)

data class CoachChatRequest(
    val content: String
)

data class CoachMemoryItem(
    val role: String,
    val content: String
)

data class CoachChatResponse(
    val reply: String,
    val memory: List<CoachMemoryItem>
)

data class CoachAdviceResponse(
    @SerializedName("daily_advice") val dailyAdvice: String,
    @SerializedName("weekly_advice") val weeklyAdvice: String?,
    @SerializedName("lifestyle_suggestions") val lifestyleSuggestions: List<String> = emptyList()
)

data class RecommendationResponse(
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val priority: Int,
    @SerializedName("is_read") val isRead: Boolean,
    @SerializedName("is_completed") val isCompleted: Boolean
)

data class GoalCreate(
    val title: String,
    val category: String,
    @SerializedName("target_value") val targetValue: Float,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("target_date") val targetDate: String
)

data class GoalResponse(
    val id: Long,
    val title: String,
    val category: String,
    @SerializedName("target_value") val targetValue: Float,
    @SerializedName("current_value") val currentValue: Float,
    val status: String
)

data class AchievementResponse(
    val id: Long,
    val title: String,
    val description: String,
    @SerializedName("badge_code") val badgeCode: String,
    @SerializedName("unlocked_at") val unlockedAt: String
)

data class StreakResponse(
    @SerializedName("current_streak") val currentStreak: Int,
    @SerializedName("longest_streak") val longestStreak: Int
)

data class EmergencyContactCreate(
    val name: String,
    val relationship: String,
    @SerializedName("phone_number") val phoneNumber: String
)

data class EmergencyContactResponse(
    val id: Long,
    val name: String,
    val relationship: String,
    @SerializedName("phone_number") val phoneNumber: String
)
