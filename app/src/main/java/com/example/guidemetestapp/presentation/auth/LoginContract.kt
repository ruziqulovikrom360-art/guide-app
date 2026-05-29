package com.example.guidemetestapp.presentation.auth

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class LoginSideEffect {
    object NavigateToHome : LoginSideEffect()
    object NavigateToRegister : LoginSideEffect()
    object NavigateToForgotPassword : LoginSideEffect()
    data class ShowToast(val message: String) : LoginSideEffect()
}
