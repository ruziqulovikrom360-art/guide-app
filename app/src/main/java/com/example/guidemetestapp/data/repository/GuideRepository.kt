package com.example.guidemetestapp.data.repository

import com.example.guidemetestapp.data.api.GuideApi
import com.example.guidemetestapp.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuideRepository @Inject constructor(private val api: GuideApi) {

    private val _savedItems = MutableStateFlow<List<SavedItem>>(emptyList())
    val savedItems: StateFlow<List<SavedItem>> = _savedItems.asStateFlow()

    suspend fun refreshSavedItems() {
        try {
            val response = api.getSaved()

            if (response.success) {
                _savedItems.value = response.data ?: emptyList()
            }
        } catch (e: Exception) {
            // Log error
        }
    }

    suspend fun getRegions(): List<Region> {
        return try {
            val response = api.getRegions()
            if (response.success) response.data ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getRegion(id: Int): Result<Region> {
        return try {
            val response = api.getRegion(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProperties(regionId: Int? = null, search: String? = null): List<Property> {
        return try {
            val response = api.getProperties(regionId = regionId, search = search)
            if (response.success) response.data ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getProperty(id: Int): Result<Property> {
        return try {
            val response = api.getProperty(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTours(regionId: Int? = null): List<Tour> {
        return try {
            val response = api.getTours(regionId = regionId)
            if (response.success) response.data ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getTour(id: Int): Result<Tour> {
        return try {
            val response = api.getTour(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTourNavigation(tourId: Int): List<NavigationStep> {
        return try {
            val response = api.getTourNavigation(tourId)
            if (response.success) response.data ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun createTour(request: TourCreateRequest): Result<Tour> {
        return try {
            val response = api.createTour(request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Tour creation failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addTourExpense(request: ExpenseRequest): Result<ExpenseResponse> {
        return try {
            val response = api.addTourExpense(request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to add expense"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRegionAverages(): List<RegionAverage> {
        return try {
            val response = api.getRegionAverages()
            if (response.success) response.data ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun chatWithAI(message: String): AIResponse? {
        return try {
            api.chatWithAI(AIQueryRequest(message = message))
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createBooking(request: BookingRequest): Result<Booking> {
        return try {
            val response = api.createBooking(request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Booking failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMyBookings(): List<Booking> {
        return try {
            val response = api.getMyBookings()
            if (response.success) response.data ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getPropertyReviews(propertyId: Int): ReviewListResponse? {
        return try {
            api.getPropertyReviews(propertyId)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addReview(request: ReviewRequest): Result<Review> {
        return try {
            val response = api.addReview(request)
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to add review"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getChatMessages(propertyId: Int): List<PropertyChatMessage> {
        return try {
            val response = api.getChatMessages(propertyId)
            if (response.success) response.data ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun sendMessage(propertyId: Int, message: String): Result<PropertyChatMessage> {
        return try {
            val response = api.sendMessage(SendMessageRequest(propertyId, message))
            if (response.success && response.data != null) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to send message"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Saved (Favorites)
    suspend fun getSaved(itemType: String? = null): List<SavedItem> {
        return try {
            val response = api.getSaved(itemType)
            if (response.success) response.data ?: emptyList() else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveItem(itemType: String, itemId: Int): Result<String> {
        return try {
            val response = api.saveItem(itemType, itemId)
            if (response.success && response.data != null) {
                refreshSavedItems()
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to save item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeSaved(savedId: Int): Result<String> {
        return try {
            val response = api.removeSaved(savedId)
            if (response.success && response.data != null) {
                refreshSavedItems()
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.message ?: "Failed to remove item"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
