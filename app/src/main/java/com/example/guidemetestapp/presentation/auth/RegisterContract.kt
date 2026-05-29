package com.example.guidemetestapp.presentation.auth

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class RegisterSideEffect {
    object NavigateToHome : RegisterSideEffect()
    object NavigateToLogin : RegisterSideEffect()
    data class ShowToast(val message: String) : RegisterSideEffect()
}
