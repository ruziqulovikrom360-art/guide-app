package com.example.guidemetestapp.domain.repository

import com.example.guidemetestapp.data.model.User
import com.example.guidemetestapp.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    suspend fun loginWithEmail(email: String, password: String): Result<User>
    suspend fun registerWithEmail(name: String, email: String, password: String): Result<User>
    suspend fun loginWithGoogle(idToken: String): Result<User>
    suspend fun loginWithApple(idToken: String, nonce: String? = null): Result<User>
    suspend fun logout()
    suspend fun loginBypass(): Result<User>
    
    // New Backend-Direct Auth methods
    suspend fun forgotPassword(email: String): Result<String>
    suspend fun resetPassword(token: String, newPassword: String): Result<String>
    suspend fun getProfile(): Result<UserProfile>
}

