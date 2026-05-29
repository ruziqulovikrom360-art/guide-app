package com.example.guidemetestapp.presentation.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.guidemetestapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.*
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<ResetPasswordState, ResetPasswordSideEffect> {

    private val token: String = savedStateHandle.get<String>("token") ?: ""

    override val container = container<ResetPasswordState, ResetPasswordSideEffect>(ResetPasswordState(token = token))

    fun onPasswordChanged(password: String) = intent {
        reduce { state.copy(newPassword = password, error = null) }
    }

    fun onConfirmPasswordChanged(password: String) = intent {
        reduce { state.copy(confirmPassword = password, error = null) }
    }

    fun resetPassword() = intent {
        if (state.newPassword.length < 6) {
            reduce { state.copy(error = "Password must be at least 6 characters") }
            return@intent
        }
        if (state.newPassword != state.confirmPassword) {
            reduce { state.copy(error = "Passwords do not match") }
            return@intent
        }

        reduce { state.copy(isLoading = true, error = null) }
        val result = authRepository.resetPassword(state.token, state.newPassword)
        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            reduce { state.copy(isSuccess = true) }
            postSideEffect(ResetPasswordSideEffect.ShowToast("Password reset successfully!"))
            postSideEffect(ResetPasswordSideEffect.NavigateToLogin)
        }.onFailure {
            reduce { state.copy(error = it.message) }
            postSideEffect(ResetPasswordSideEffect.ShowToast(it.message ?: "Reset failed"))
        }
    }
}
