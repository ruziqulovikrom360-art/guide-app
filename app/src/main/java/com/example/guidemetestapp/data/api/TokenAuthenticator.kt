package com.example.guidemetestapp.data.api

import com.example.guidemetestapp.data.model.RefreshTokenRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val guideApiProvider: Provider<GuideApi>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = runBlocking {
            tokenManager.refreshToken.first()
        }

        if (refreshToken == null) {
            return null
        }

        synchronized(this) {
            val currentToken = runBlocking { tokenManager.accessToken.first() }
            
            // If the token in the request is already different from the one we have, 
            // it means it was already refreshed by another thread.
            val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")
            if (requestToken != currentToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            return try {
                val refreshResponse = runBlocking {
                    guideApiProvider.get().refreshToken(RefreshTokenRequest(refreshToken))
                }

                if (refreshResponse.success && refreshResponse.data != null) {
                    val newData = refreshResponse.data
                    runBlocking {
                        tokenManager.saveTokens(newData.accessToken, newData.refreshToken)
                    }
                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${newData.accessToken}")
                        .build()
                } else {
                    runBlocking { tokenManager.clearTokens() }
                    null
                }
            } catch (e: Exception) {
                runBlocking { tokenManager.clearTokens() }
                null
            }
        }
    }
}
