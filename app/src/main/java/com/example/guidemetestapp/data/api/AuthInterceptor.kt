package com.example.guidemetestapp.data.api

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath
        
        // Skip adding token for auth endpoints
        if (path.contains("/auth/login") || 
            path.contains("/auth/register") || 
            path.contains("/auth/refresh") ||
            path.contains("/auth/forgot-password") || 
            path.contains("/auth/reset-password") ||
            path.contains("/auth/google") ||
            path.contains("/auth/apple")
        ) {
            return chain.proceed(request)
        }

        val token = runBlocking {
            tokenManager.accessToken.first()
        }
        
        val authenticatedRequest = request.newBuilder().apply {
            if (token != null && !token.startsWith("dev_bypass")) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()
        
        return chain.proceed(authenticatedRequest)
    }
}
