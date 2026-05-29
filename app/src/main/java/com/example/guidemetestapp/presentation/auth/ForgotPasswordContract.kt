package com.example.guidemetestapp.presentation.auth

data class ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

sealed class ForgotPasswordSideEffect {
    object NavigateBack : ForgotPasswordSideEffect()
    data class ShowToast(val message: String) : ForgotPasswordSideEffect()
}
