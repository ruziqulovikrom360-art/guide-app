package com.example.guidemetestapp.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.guidemetestapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.*
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), ContainerHost<ForgotPasswordState, ForgotPasswordSideEffect> {

    override val container = container<ForgotPasswordState, ForgotPasswordSideEffect>(ForgotPasswordState())

    fun onEmailChanged(email: String) = intent {
        reduce { state.copy(email = email, error = null) }
    }

    fun requestReset() = intent {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            reduce { state.copy(error = "Invalid email address") }
            return@intent
        }

        reduce { state.copy(isLoading = true, error = null) }
        val result = authRepository.forgotPassword(state.email)
        reduce { state.copy(isLoading = false) }

        result.onSuccess {
            reduce { state.copy(isSuccess = true) }
            postSideEffect(ForgotPasswordSideEffect.ShowToast("Reset link sent to your email"))
        }.onFailure {
            reduce { state.copy(error = it.message) }
            postSideEffect(ForgotPasswordSideEffect.ShowToast(it.message ?: "Request failed"))
        }
    }

    fun onBack() = intent {
        postSideEffect(ForgotPasswordSideEffect.NavigateBack)
    }
}
