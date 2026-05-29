package com.example.guidemetestapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class TourPackage(
    val id: String,
    val name: String,
    val price: String,
    val imageUrl: String,
    val rating: Double,
    val description: String
)

@JsonClass(generateAdapter = true)
data class Resort(
    val id: String,
    val name: String,
    val description: String,
    val pricePerNight: Double,
    val imageUrl: String,
    val rating: Double,
    val rooms: Int,
    val guests: Int
)

@JsonClass(generateAdapter = true)
data class TravelItinerary(
    val id: String,
    val destination: String,
    val vehicle: String,
    val duration: String,
    val paymentMethod: String,
    val promoCode: String? = null,
    val places: List<Place>,
    val totalCost: Double
)

@JsonClass(generateAdapter = true)
data class Place(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val duration: String
)

@JsonClass(generateAdapter = true)
data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long,
    val suggestedPackageId: String? = null
)

@JsonClass(generateAdapter = true)
data class AiChatRequest(
    val prompt: String,
    val userPreferences: String? = null
)

@JsonClass(generateAdapter = true)
data class AiChatResponse(
    val reply: String,
    val packages: List<TourPackage>? = null
)
