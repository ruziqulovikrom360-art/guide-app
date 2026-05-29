package com.example.guidemetestapp.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemetestapp.data.repository.GuideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.*
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: GuideRepository
) : ViewModel(), ContainerHost<DetailState, DetailSideEffect> {

    override val container = container<DetailState, DetailSideEffect>(DetailState())

    init {
        // Reactively update favorite status whenever repository's saved items change
        repository.savedItems.onEach { savedItems ->
            updateFavoriteState(savedItems)
        }.launchIn(viewModelScope)
    }

    private fun updateFavoriteState(savedItems: List<com.example.guidemetestapp.data.model.SavedItem>) = intent {
        val currentPropertyId = state.property?.id
        val currentTourId = state.tour?.id
        
        val item = savedItems.find { 
            (currentPropertyId != null && it.itemId == currentPropertyId && it.itemType == "property") ||
            (currentTourId != null && it.itemId == currentTourId && it.itemType == "tour")
        }
        
        reduce { 
            state.copy(
                isFavorite = item != null,
                savedId = item?.id
            )
        }
    }

    fun loadDetail(itemId: String) = intent {
        reduce { state.copy(isLoading = true, error = null, property = null, tour = null, region = null) }
        
        val parts = itemId.split("_")
        if (parts.size < 2) {
            reduce { state.copy(isLoading = false, error = "Invalid ID format") }
            return@intent
        }

        val type = parts[0]
        val id = parts[1].toIntOrNull() ?: 0

        try {
            when (type) {
                "region" -> {
                    repository.getRegion(id).onSuccess { region ->
                        reduce { state.copy(isLoading = false, region = region) }
                    }.onFailure {
                        reduce { state.copy(isLoading = false, error = it.message) }
                    }
                }
                "property" -> {
                    repository.getProperty(id).onSuccess { property ->
                        val reviewResponse = repository.getPropertyReviews(id)
                        val savedItems = repository.savedItems.value
                        val favItem = savedItems.find { it.itemId == property.id && it.itemType == "property" }
                        
                        reduce { 
                            state.copy(
                                isLoading = false, 
                                property = property, 
                                reviews = reviewResponse?.data ?: emptyList(),
                                ratingUz = reviewResponse?.ratingUz ?: 0.0,
                                ratingGuest = reviewResponse?.ratingGuest ?: 0.0,
                                isFavorite = favItem != null,
                                savedId = favItem?.id
                            ) 
                        }
                    }.onFailure {
                        reduce { state.copy(isLoading = false, error = it.message) }
                    }
                }
                "tour" -> {
                    repository.getTour(id).onSuccess { tour ->
                        val savedItems = repository.savedItems.value
                        val favItem = savedItems.find { it.itemId == tour.id && it.itemType == "tour" }
                        
                        reduce { 
                            state.copy(
                                isLoading = false, 
                                tour = tour,
                                isFavorite = favItem != null,
                                savedId = favItem?.id
                            ) 
                        }
                    }.onFailure {
                        reduce { state.copy(isLoading = false, error = it.message) }
                    }
                }
                else -> {
                    reduce { state.copy(isLoading = false, error = "Unknown type: $type") }
                }
            }
        } catch (e: Exception) {
            reduce { state.copy(isLoading = false, error = e.localizedMessage) }
            postSideEffect(DetailSideEffect.ShowError(e.localizedMessage ?: "Failed to load details"))
        }
    }

    fun toggleFavorite() = intent {
        val propertyId = state.property?.id
        val tourId = state.tour?.id
        val itemId = propertyId ?: tourId ?: return@intent
        val type = if (propertyId != null) "property" else "tour"
        
        if (state.isFavorite && state.savedId != null) {
            repository.removeSaved(state.savedId!!).onFailure {
                postSideEffect(DetailSideEffect.ShowError("Failed to remove: ${it.message}"))
            }
        } else {
            repository.saveItem(type, itemId).onFailure {
                postSideEffect(DetailSideEffect.ShowError("Failed to add: ${it.message}"))
            }
        }
    }

    fun onBackClick() = intent {
        postSideEffect(DetailSideEffect.NavigateBack)
    }

    fun bookProperty(unitId: Int, checkIn: String, checkOut: String, guests: Int) = intent {
        val propertyId = state.property?.id ?: return@intent
        reduce { state.copy(isBookingInProgress = true) }
        
        val request = com.example.guidemetestapp.data.model.BookingRequest(
            propertyId = propertyId,
            unitId = unitId,
            checkInDate = checkIn,
            checkOutDate = checkOut,
            rooms = 1,
            guests = guests,
            message = null
        )
        
        val result = repository.createBooking(request)
        reduce { state.copy(isBookingInProgress = false) }
        
        result.onSuccess {
            postSideEffect(DetailSideEffect.BookingSuccess)
            postSideEffect(DetailSideEffect.ShowMessage("Booking successful!"))
        }.onFailure {
            postSideEffect(DetailSideEffect.ShowError(it.message ?: "Booking failed"))
        }
    }

    fun addReview(rating: Int, comment: String) = intent {
        val propertyId = state.property?.id ?: return@intent
        val result = repository.addReview(
            com.example.guidemetestapp.data.model.ReviewRequest(
                propertyId = propertyId,
                rating = rating,
                textEn = comment,
                textUz = null,
                textRu = null
            )
        )
        
        result.onSuccess { newReview ->
            reduce { state.copy(reviews = state.reviews + newReview) }
            postSideEffect(DetailSideEffect.ShowMessage("Review added!"))
        }.onFailure {
            postSideEffect(DetailSideEffect.ShowError(it.message ?: "Failed to add review"))
        }
    }
}
