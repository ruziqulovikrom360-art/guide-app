package com.example.guidemetestapp.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.guidemetestapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.*
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), ContainerHost<LoginState, LoginSideEffect> {

    override val container = container<LoginState, LoginSideEffect>(LoginState())

    fun onEmailChanged(email: String) = intent {
        reduce { state.copy(email = email) }
    }

    fun onPasswordChanged(password: String) = intent {
        reduce { state.copy(password = password) }
    }

    fun loginWithEmail(password: String) = intent {
        reduce { state.copy(isLoading = true, error = null) }
        val result = authRepository.loginWithEmail(state.email, password)
        reduce { state.copy(isLoading = false) }
        result.onSuccess {
            postSideEffect(LoginSideEffect.NavigateToHome)
        }.onFailure {
            reduce { state.copy(error = it.message) }
            postSideEffect(LoginSideEffect.ShowToast(it.message ?: "Login failed"))
        }
    }

    fun loginBypass() = intent {
        reduce { state.copy(isLoading = true, error = null) }
        val result = authRepository.loginBypass()
        reduce { state.copy(isLoading = false) }
        result.onSuccess {
            postSideEffect(LoginSideEffect.NavigateToHome)
        }.onFailure {
            reduce { state.copy(error = it.message) }
            postSideEffect(LoginSideEffect.ShowToast(it.message ?: "Bypass login failed"))
        }
    }

    fun loginWithGoogle(idToken: String) = intent {
        reduce { state.copy(isLoading = true, error = null) }
        val result = authRepository.loginWithGoogle(idToken)
        reduce { state.copy(isLoading = false) }
        result.onSuccess {
            postSideEffect(LoginSideEffect.NavigateToHome)
        }.onFailure {
            reduce { state.copy(error = it.message) }
            postSideEffect(LoginSideEffect.ShowToast(it.message ?: "Google Login failed"))
        }
    }

    fun loginWithApple(idToken: String) = intent {
        reduce { state.copy(isLoading = true, error = null) }
        val result = authRepository.loginWithApple(idToken)
        reduce { state.copy(isLoading = false) }
        result.onSuccess {
            postSideEffect(LoginSideEffect.NavigateToHome)
        }.onFailure {
            reduce { state.copy(error = it.message) }
            postSideEffect(LoginSideEffect.ShowToast(it.message ?: "Apple Login failed"))
        }
    }

    fun onNavigateToRegister() = intent {
        postSideEffect(LoginSideEffect.NavigateToRegister)
    }

    fun onNavigateToForgotPassword() = intent {
        postSideEffect(LoginSideEffect.NavigateToForgotPassword)
    }
}
