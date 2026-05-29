package com.example.guidemetestapp.presentation.auth

data class ResetPasswordState(
    val token: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

sealed class ResetPasswordSideEffect {
    object NavigateToLogin : ResetPasswordSideEffect()
    data class ShowToast(val message: String) : ResetPasswordSideEffect()
}
