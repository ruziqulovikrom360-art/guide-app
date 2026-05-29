package com.example.guidemetestapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemetestapp.data.repository.GuideRepository
import com.example.guidemetestapp.ui.screens.AIChatUiMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AIChatViewModel @Inject constructor(
    private val repository: GuideRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<AIChatUiMessage>>(listOf(
        AIChatUiMessage("Hello! I'm your GuideMe-AI assistant. How can I help you plan your trip to Uzbekistan?", false)
    ))
    val messages: StateFlow<List<AIChatUiMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(text: String) {
        val userMessage = AIChatUiMessage(text, true)
        _messages.value = _messages.value + userMessage
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.chatWithAI(text)
                if (response != null) {
                    val aiMessage = AIChatUiMessage(
                        text = response.reply,
                        isUser = false,
                        suggestions = response.suggestions,
                        weather = response.weather
                    )
                    _messages.value = _messages.value + aiMessage
                } else {
                    _messages.value = _messages.value + AIChatUiMessage("Sorry, I couldn't process that. Please try again.", false)
                }
            } catch (e: Exception) {
                _messages.value = _messages.value + AIChatUiMessage("Error: ${e.message}", false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
