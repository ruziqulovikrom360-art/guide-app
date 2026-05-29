package com.example.guidemetestapp.presentation.detail

import com.example.guidemetestapp.data.model.Property
import com.example.guidemetestapp.data.model.Region
import com.example.guidemetestapp.data.model.Tour
import com.example.guidemetestapp.data.model.Review

data class DetailState(
    val isLoading: Boolean = false,
    val property: Property? = null,
    val tour: Tour? = null,
    val region: Region? = null,
    val reviews: List<Review> = emptyList(),
    val ratingUz: Double = 0.0,
    val ratingGuest: Double = 0.0,
    val isBookingInProgress: Boolean = false,
    val isFavorite: Boolean = false,
    val savedId: Int? = null,
    val error: String? = null
)

sealed class DetailSideEffect {
    data class ShowError(val message: String) : DetailSideEffect()
    data class ShowMessage(val message: String) : DetailSideEffect()
    object NavigateBack : DetailSideEffect()
    object BookingSuccess : DetailSideEffect()
}
