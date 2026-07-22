package com.mindguard.data.repository

import com.mindguard.core.network.NetworkResult
import com.mindguard.data.local.dao.*
import com.mindguard.data.local.datastore.TokenManager
import com.mindguard.data.local.entity.*
import com.mindguard.data.remote.api.*
import com.mindguard.data.remote.dto.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

import org.json.JSONObject
import org.json.JSONArray

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
    private fun parseErrorBody(jsonStr: String?, default: String): String {
        if (jsonStr.isNullOrEmpty()) return default
        return try {
            val obj = JSONObject(jsonStr)
            if (obj.has("detail")) {
                val detail = obj.get("detail")
                if (detail is String) {
                    detail
                } else if (detail is JSONArray && detail.length() > 0) {
                    val firstError = detail.getJSONObject(0)
                    if (firstError.has("msg")) {
                        firstError.getString("msg")
                    } else {
                        default
                    }
                } else {
                    default
                }
            } else {
                jsonStr
            }
        } catch (e: Exception) {
            jsonStr
        }
    }

    fun login(request: LoginRequest): Flow<NetworkResult<TokenResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = authApi.login(request)
            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!
                tokenManager.saveTokens(token.accessToken, token.refreshToken)
                emit(NetworkResult.Success(token))
            } else {
                val errorMsg = parseErrorBody(response.errorBody()?.string(), "Login failed")
                emit(NetworkResult.Error(errorMsg, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error occurred"))
        }
    }

    fun register(request: RegisterRequest): Flow<NetworkResult<UserResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = authApi.register(request)
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorMsg = parseErrorBody(response.errorBody()?.string(), "Registration failed")
                emit(NetworkResult.Error(errorMsg, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error occurred"))
        }
    }

    suspend fun logout() {
        tokenManager.clearTokens()
    }
}

@Singleton
class WellnessRepository @Inject constructor(
    private val wellnessApi: WellnessApi,
    private val lifestyleDao: LifestyleDao,
    private val moodDao: MoodDao,
    private val journalDao: JournalDao
) {
    fun getProfile(): Flow<NetworkResult<ProfileResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = wellnessApi.getProfile()
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error("Failed to fetch profile"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun logLifestyle(log: DailyLifestyleCreate): Flow<NetworkResult<DailyLifestyleResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = wellnessApi.logLifestyle(log)
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                lifestyleDao.insertLog(
                    DailyLogEntity(
                        id = data.id,
                        sleepHours = data.sleepHours,
                        screenTimeHours = data.screenTimeHours,
                        activeMinutes = data.activeMinutes,
                        caffeineIntakeMg = data.caffeineIntakeMg,
                        waterIntakeLiters = data.waterIntakeLiters,
                        logDate = data.logDate
                    )
                )
                emit(NetworkResult.Success(data))
            } else {
                emit(NetworkResult.Error("Failed to log lifestyle metrics"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun logMood(mood: MoodCreate): Flow<NetworkResult<MoodResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = wellnessApi.logMood(mood)
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                moodDao.insertMood(
                    MoodEntity(id = data.id, moodScore = data.moodScore, notes = data.notes, entryTime = data.entryTime)
                )
                emit(NetworkResult.Success(data))
            } else {
                emit(NetworkResult.Error("Failed to log mood"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun createJournal(journal: JournalCreate): Flow<NetworkResult<JournalResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = wellnessApi.createJournal(journal)
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                journalDao.insertJournal(
                    JournalEntity(id = data.id, title = data.title, content = data.content, sentimentScore = data.sentimentScore, createdAt = data.createdAt)
                )
                emit(NetworkResult.Success(data))
            } else {
                emit(NetworkResult.Error("Failed to create journal"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getEmergencyContacts(): Flow<NetworkResult<List<EmergencyContactResponse>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = wellnessApi.getEmergencyContacts()
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error("Failed to fetch emergency contacts"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun createEmergencyContact(contact: EmergencyContactCreate): Flow<NetworkResult<EmergencyContactResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = wellnessApi.createEmergencyContact(contact)
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                emit(NetworkResult.Error("Failed to save contact"))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }
}

@Singleton
class AIRepository @Inject constructor(
    private val twinApi: TwinApi,
    private val behaviorApi: BehaviorApi,
    private val stressApi: StressApi,
    private val coachApi: CoachApi,
    private val recommendationsApi: RecommendationsApi,
    private val goalsApi: GoalsApi,
    private val achievementsApi: AchievementsApi
) {
    fun getTwin(): Flow<NetworkResult<DigitalTwinResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = twinApi.getTwin()
            if (res.isSuccessful && res.body() != null) emit(NetworkResult.Success(res.body()!!))
            else emit(NetworkResult.Error("Failed to fetch Digital Twin metrics"))
        } catch (e: Exception) { emit(NetworkResult.Error(e.message ?: "Network error")) }
    }

    fun getBehaviorDrift(): Flow<NetworkResult<BehaviorDriftResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = behaviorApi.getBehaviorDrift()
            if (res.isSuccessful && res.body() != null) emit(NetworkResult.Success(res.body()!!))
            else emit(NetworkResult.Error("Failed to fetch behavior drift"))
        } catch (e: Exception) { emit(NetworkResult.Error(e.message ?: "Network error")) }
    }

    fun getStressAssessment(): Flow<NetworkResult<StressLikelihoodResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = stressApi.getStressAssessment()
            if (res.isSuccessful && res.body() != null) emit(NetworkResult.Success(res.body()!!))
            else emit(NetworkResult.Error("Failed to fetch stress assessment"))
        } catch (e: Exception) { emit(NetworkResult.Error(e.message ?: "Network error")) }
    }

    fun sendCoachMessage(message: String): Flow<NetworkResult<CoachChatResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = coachApi.sendChatMessage(CoachChatRequest(message))
            if (res.isSuccessful && res.body() != null) emit(NetworkResult.Success(res.body()!!))
            else emit(NetworkResult.Error("Failed to send chat message"))
        } catch (e: Exception) { emit(NetworkResult.Error(e.message ?: "Network error")) }
    }

    fun getCoachAdvice(): Flow<NetworkResult<CoachAdviceResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = coachApi.getProactiveAdvice()
            if (res.isSuccessful && res.body() != null) emit(NetworkResult.Success(res.body()!!))
            else emit(NetworkResult.Error("Failed to load coach advice"))
        } catch (e: Exception) { emit(NetworkResult.Error(e.message ?: "Network error")) }
    }

    fun getRecommendations(): Flow<NetworkResult<List<RecommendationResponse>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = recommendationsApi.getRecommendations()
            if (res.isSuccessful && res.body() != null) emit(NetworkResult.Success(res.body()!!))
            else emit(NetworkResult.Error("Failed to load recommendations"))
        } catch (e: Exception) { emit(NetworkResult.Error(e.message ?: "Network error")) }
    }

    fun getGoals(): Flow<NetworkResult<List<GoalResponse>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = goalsApi.getGoals()
            if (res.isSuccessful && res.body() != null) emit(NetworkResult.Success(res.body()!!))
            else emit(NetworkResult.Error("Failed to load goals"))
        } catch (e: Exception) { emit(NetworkResult.Error(e.message ?: "Network error")) }
    }

    fun getAchievements(): Flow<NetworkResult<List<AchievementResponse>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = achievementsApi.getAchievements()
            if (res.isSuccessful && res.body() != null) emit(NetworkResult.Success(res.body()!!))
            else emit(NetworkResult.Error("Failed to load achievements"))
        } catch (e: Exception) { emit(NetworkResult.Error(e.message ?: "Network error")) }
    }

    fun getStreak(): Flow<NetworkResult<StreakResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = achievementsApi.getStreak()
            if (res.isSuccessful && res.body() != null) emit(NetworkResult.Success(res.body()!!))
            else emit(NetworkResult.Error("Failed to load streak metrics"))
        } catch (e: Exception) { emit(NetworkResult.Error(e.message ?: "Network error")) }
    }
}
