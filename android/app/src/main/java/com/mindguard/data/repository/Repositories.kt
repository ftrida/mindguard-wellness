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

fun parseNetworkError(errorBody: String?, code: Int, default: String): String {
    if (errorBody.isNullOrEmpty()) return "$default (HTTP $code)"
    return try {
        val obj = JSONObject(errorBody)
        if (obj.has("detail")) {
            val detail = obj.get("detail")
            if (detail is String) {
                "$detail (HTTP $code)"
            } else if (detail is JSONArray && detail.length() > 0) {
                val firstError = detail.getJSONObject(0)
                if (firstError.has("msg")) {
                    "${firstError.getString("msg")} (HTTP $code)"
                } else {
                    "$default (HTTP $code)"
                }
            } else {
                "$default (HTTP $code)"
            }
        } else {
            "$default (HTTP $code)"
        }
    } catch (e: Exception) {
        "$default (HTTP $code)"
    }
}

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManager
) {
    fun login(request: LoginRequest): Flow<NetworkResult<TokenResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = authApi.login(request)
            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!
                tokenManager.saveTokens(token.accessToken, token.refreshToken)
                emit(NetworkResult.Success(token))
            } else {
                val errorMsg = parseNetworkError(response.errorBody()?.string(), response.code(), "Login failed")
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
                val errorMsg = parseNetworkError(response.errorBody()?.string(), response.code(), "Registration failed")
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
                val errorMsg = parseNetworkError(response.errorBody()?.string(), response.code(), "Failed to fetch profile")
                emit(NetworkResult.Error(errorMsg, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun updateProfile(request: ProfileRequest): Flow<NetworkResult<ProfileResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = wellnessApi.updateProfile(request)
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorMsg = parseNetworkError(response.errorBody()?.string(), response.code(), "Failed to update profile")
                emit(NetworkResult.Error(errorMsg, response.code()))
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
                        screenTimeHours = data.screenTime,
                        activeMinutes = data.exerciseMinutes,
                        caffeineIntakeMg = 0,
                        waterIntakeLiters = data.waterIntake,
                        logDate = data.logDate
                    )
                )
                emit(NetworkResult.Success(data))
            } else {
                val errorMsg = parseNetworkError(response.errorBody()?.string(), response.code(), "Failed to log lifestyle metrics")
                emit(NetworkResult.Error(errorMsg, response.code()))
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
                val errorMsg = parseNetworkError(response.errorBody()?.string(), response.code(), "Failed to log mood")
                emit(NetworkResult.Error(errorMsg, response.code()))
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
                val errorMsg = parseNetworkError(response.errorBody()?.string(), response.code(), "Failed to create journal")
                emit(NetworkResult.Error(errorMsg, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getJournals(): Flow<NetworkResult<List<JournalResponse>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = wellnessApi.getJournals()
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorMsg = parseNetworkError(response.errorBody()?.string(), response.code(), "Failed to fetch journals")
                emit(NetworkResult.Error(errorMsg, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun logMeditation(duration: Int): Flow<NetworkResult<MeditationResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = wellnessApi.logMeditation(MeditationCreate(durationSeconds = duration))
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorMsg = parseNetworkError(response.errorBody()?.string(), response.code(), "Failed to log meditation session")
                emit(NetworkResult.Error(errorMsg, response.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun logFocus(duration: Int): Flow<NetworkResult<FocusSessionResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val response = wellnessApi.logFocus(FocusSessionCreate(customDurationSeconds = duration))
            if (response.isSuccessful && response.body() != null) {
                emit(NetworkResult.Success(response.body()!!))
            } else {
                val errorMsg = parseNetworkError(response.errorBody()?.string(), response.code(), "Failed to log focus session")
                emit(NetworkResult.Error(errorMsg, response.code()))
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
    private val achievementsApi: AchievementsApi,
    private val reportsApi: ReportsApi
) {
    fun getDailyReport(): Flow<NetworkResult<Map<String, Any>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = reportsApi.getDailyReport()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to fetch daily report")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getWeeklyReport(): Flow<NetworkResult<Map<String, Any>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = reportsApi.getWeeklyReport()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to fetch weekly report")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getTwin(): Flow<NetworkResult<DigitalTwinResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = twinApi.getTwin()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to fetch Digital Twin metrics")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getBehaviorDrift(): Flow<NetworkResult<BehaviorDriftResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = behaviorApi.getBehaviorDrift()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to fetch behavior drift")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getStressAssessment(): Flow<NetworkResult<StressLikelihoodResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = stressApi.getStressAssessment()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to fetch stress assessment")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun sendCoachMessage(message: String): Flow<NetworkResult<CoachChatResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = coachApi.sendChatMessage(CoachChatRequest(message))
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to send chat message")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getMemory(): Flow<NetworkResult<List<CoachMemoryItem>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = coachApi.getMemory()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to fetch conversation history")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getCoachAdvice(): Flow<NetworkResult<CoachAdviceResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = coachApi.getProactiveAdvice()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to load coach advice")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getRecommendations(): Flow<NetworkResult<List<RecommendationResponse>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = recommendationsApi.getRecommendations()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to load recommendations")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getGoals(): Flow<NetworkResult<List<GoalResponse>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = goalsApi.getGoals()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to load goals")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getAchievements(): Flow<NetworkResult<List<AchievementResponse>>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = achievementsApi.getAchievements()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to load achievements")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }

    fun getStreak(): Flow<NetworkResult<StreakResponse>> = flow {
        emit(NetworkResult.Loading)
        try {
            val res = achievementsApi.getStreak()
            if (res.isSuccessful && res.body() != null) {
                emit(NetworkResult.Success(res.body()!!))
            } else {
                val errorMsg = parseNetworkError(res.errorBody()?.string(), res.code(), "Failed to load streak metrics")
                emit(NetworkResult.Error(errorMsg, res.code()))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Network error"))
        }
    }
}
