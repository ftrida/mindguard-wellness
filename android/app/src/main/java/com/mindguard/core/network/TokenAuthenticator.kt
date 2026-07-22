package com.mindguard.core.network

import com.mindguard.data.local.datastore.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import org.json.JSONObject
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // Prevent infinite loops if refresh endpoint itself returns 401
        if (response.request.url.encodedPath.contains("/auth/refresh")) {
            return null
        }

        val refreshToken = runBlocking { tokenManager.getRefreshToken() } ?: return null

        // Execute raw OkHttp call to refresh token endpoint
        val client = OkHttpClient()
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = JSONObject().apply {
            put("refresh_token", refreshToken)
        }.toString().toRequestBody(jsonMediaType)

        val refreshRequest = Request.Builder()
            .url("https://mindguard-api-gz19.onrender.com/api/v1/auth/refresh")
            .post(requestBody)
            .build()

        try {
            val refreshResponse = client.newCall(refreshRequest).execute()
            if (refreshResponse.isSuccessful) {
                val responseStr = refreshResponse.body?.string() ?: return null
                val jsonObj = JSONObject(responseStr)
                val newAccessToken = jsonObj.getString("access_token")
                val newRefreshToken = jsonObj.optString("refresh_token", refreshToken)

                runBlocking {
                    tokenManager.saveTokens(newAccessToken, newRefreshToken)
                }

                // Retry original request with new access token
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                runBlocking { tokenManager.clearTokens() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}
