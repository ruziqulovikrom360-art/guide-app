package com.example.guidemetestapp.data.repository

import com.example.guidemetestapp.data.api.GuideApi
import com.example.guidemetestapp.data.api.TokenManager
import com.example.guidemetestapp.data.model.User
import com.example.guidemetestapp.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.absoluteValue

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val guideApi: GuideApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    private val devUserFlow = kotlinx.coroutines.flow.MutableStateFlow<User?>(null)

    override val currentUser: Flow<User?> = callbackFlow {
        val job = launch {
            combine(
                tokenManager.accessToken.map { it as Any? },
                tokenManager.userId.map { it as Any? },
                tokenManager.userName.map { it as Any? },
                tokenManager.userEmail.map { it as Any? },
                tokenManager.userAvatar.map { it as Any? },
                devUserFlow.map { it as Any? }
            ) { array ->
                val token = array[0] as? String
                val id = array[1] as? String
                val name = array[2] as? String
                val email = array[3] as? String
                val avatar = array[4] as? String
                val devUser = array[5] as? User

                if (devUser != null) {
                    devUser
                } else if (token != null && id != null) {
                    User(
                        id = id,
                        name = name ?: "User",
                        email = email ?: "",
                        profileImageUrl = avatar
                    )
                } else {
                    null
                }
            }.collect {
                trySend(it)
            }
        }
        awaitClose {
            job.cancel()
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<User> {
        return try {
            val localUserId = "user_" + email.hashCode().absoluteValue
            val localName = email.substringBefore("@").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            val user = User(
                id = localUserId,
                name = localName,
                email = email,
                profileImageUrl = null
            )
            tokenManager.saveTokens("dev_bypass_access_token", "dev_bypass_refresh_token")
            tokenManager.saveUser(user.id, user.name, user.email, user.profileImageUrl)
            devUserFlow.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(name: String, email: String, password: String): Result<User> {
        return try {
            val localUserId = "user_" + email.hashCode().absoluteValue
            val user = User(
                id = localUserId,
                name = name,
                email = email,
                profileImageUrl = null
            )
            tokenManager.saveTokens("dev_bypass_access_token", "dev_bypass_refresh_token")
            tokenManager.saveUser(user.id, user.name, user.email, user.profileImageUrl)
            devUserFlow.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return try {
            val user = User(
                id = "google_bypass_user_id",
                name = "Google User",
                email = "google_user@gmail.com",
                profileImageUrl = null
            )
            tokenManager.saveTokens("dev_bypass_access_token", "dev_bypass_refresh_token")
            tokenManager.saveUser(user.id, user.name, user.email, user.profileImageUrl)
            devUserFlow.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithApple(idToken: String, nonce: String?): Result<User> {
        return try {
            val user = User(
                id = "apple_bypass_user_id",
                name = "Apple User",
                email = "apple_user@apple.com",
                profileImageUrl = null
            )
            tokenManager.saveTokens("dev_bypass_access_token", "dev_bypass_refresh_token")
            tokenManager.saveUser(user.id, user.name, user.email, user.profileImageUrl)
            devUserFlow.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        tokenManager.clearTokens()
        tokenManager.clearUser()
        devUserFlow.value = null
    }

    override suspend fun loginBypass(): Result<User> {
        return try {
            val user = User(
                id = "dev_bypass_user_id",
                name = "Ikrom (Demo)",
                email = "ruziqulovikrom285@gmail.com",
                profileImageUrl = null
            )
            tokenManager.saveTokens("dev_bypass_access_token", "dev_bypass_refresh_token")
            tokenManager.saveUser(user.id, user.name, user.email, user.profileImageUrl)
            devUserFlow.value = user
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun forgotPassword(email: String): Result<String> {
        return try {
            val response = guideApi.forgotPassword(com.example.guidemetestapp.data.model.ForgotPasswordRequest(email))
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Forgot password request failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(token: String, newPassword: String): Result<String> {
        return try {
            val response = guideApi.resetPassword(com.example.guidemetestapp.data.model.ResetPasswordRequest(token, newPassword))
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Password reset failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(): Result<com.example.guidemetestapp.data.model.UserProfile> {
        return try {
            val response = guideApi.getMe()
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to get profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
