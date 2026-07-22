package com.mindguard.core.network

import com.mindguard.data.local.datastore.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Attach unique Request ID
        requestBuilder.addHeader("X-Request-ID", UUID.randomUUID().toString())

        // Fetch access token synchronously for OkHttp loop
        val token = runBlocking { tokenManager.getAccessToken() }
        if (!token.isNull_or_Empty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }

    private fun String?.isNull_or_Empty(): Boolean = this == null || this.trim().isEmpty()
}
