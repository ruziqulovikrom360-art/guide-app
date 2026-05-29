package com.example.guidemetestapp.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.guidemetestapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.*
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), ContainerHost<RegisterState, RegisterSideEffect> {

    override val container = container<RegisterState, RegisterSideEffect>(RegisterState())

    fun onNameChanged(name: String) = intent {
        reduce { state.copy(name = name) }
    }

    fun onEmailChanged(email: String) = intent {
        reduce { state.copy(email = email) }
    }

    fun onPasswordChanged(password: String) = intent {
        reduce { state.copy(password = password) }
    }

    fun register() = intent {
        if (state.name.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            postSideEffect(RegisterSideEffect.ShowToast("Please fill all fields"))
            return@intent
        }

        reduce { state.copy(isLoading = true, error = null) }
        val result = authRepository.registerWithEmail(state.name, state.email, state.password)
        reduce { state.copy(isLoading = false) }
        
        result.onSuccess {
            postSideEffect(RegisterSideEffect.NavigateToHome)
        }.onFailure {
            reduce { state.copy(error = it.message) }
            postSideEffect(RegisterSideEffect.ShowToast(it.message ?: "Registration failed"))
        }
    }

    fun loginBypass() = intent {
        reduce { state.copy(isLoading = true, error = null) }
        val result = authRepository.loginBypass()
        reduce { state.copy(isLoading = false) }
        result.onSuccess {
            postSideEffect(RegisterSideEffect.NavigateToHome)
        }.onFailure {
            reduce { state.copy(error = it.message) }
            postSideEffect(RegisterSideEffect.ShowToast(it.message ?: "Bypass login failed"))
        }
    }

    fun onNavigateToLogin() = intent {
        postSideEffect(RegisterSideEffect.NavigateToLogin)
    }
}
