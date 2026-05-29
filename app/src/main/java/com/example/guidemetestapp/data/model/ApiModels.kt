package com.example.guidemetestapp.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataResponse<T>(
    @Json(name = "success") val success: Boolean = true,
    @Json(name = "message") val message: String? = null,
    @Json(name = "data") val data: T? = null
)

@JsonClass(generateAdapter = true)
data class ListResponse<T>(
    @Json(name = "success") val success: Boolean = true,
    @Json(name = "data") val data: List<T>? = emptyList(),
    @Json(name = "total") val total: Int? = 0
)

@JsonClass(generateAdapter = true)
data class Region(
    @Json(name = "id") val id: Int,
    @Json(name = "name_en") val nameEn: String,
    @Json(name = "name_uz") val nameUz: String? = null,
    @Json(name = "name_ru") val nameRu: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "icon_url") val iconUrl: String? = null,
    @Json(name = "cover_url") val coverUrl: String? = null,
    @Json(name = "best_season") val bestSeason: String? = null,
    @Json(name = "lat") val lat: Double? = null,
    @Json(name = "lon") val lon: Double? = null,
    @Json(name = "is_active") val isActive: Boolean = true,
    @Json(name = "created_at") val createdAt: String? = null
)

@JsonClass(generateAdapter = true)
data class Property(
    @Json(name = "id") val id: Int,
    @Json(name = "region_id") val regionId: Int,
    @Json(name = "owner_id") val ownerId: Int? = null,
    @Json(name = "property_type") val propertyType: String = "hotel",
    @Json(name = "name_en") val nameEn: String,
    @Json(name = "name_uz") val nameUz: String? = null,
    @Json(name = "name_ru") val nameRu: String? = null,
    @Json(name = "description_en") val descriptionEn: String? = null,
    @Json(name = "description_uz") val descriptionUz: String? = null,
    @Json(name = "description_ru") val descriptionRu: String? = null,
    @Json(name = "lat") val lat: Double? = null,
    @Json(name = "lon") val lon: Double? = null,
    @Json(name = "website") val website: String? = null,
    @Json(name = "phone") val phone: String? = null,
    @Json(name = "address") val address: String? = null,
    @Json(name = "working_hours") val workingHours: String? = null,
    @Json(name = "weekend") val weekend: String? = null,
    @Json(name = "cuisine_type") val cuisineType: String? = null,
    @Json(name = "price_level") val priceLevel: String? = null,
    @Json(name = "stars") val stars: Int? = null,
    @Json(name = "has_wifi") val hasWifi: Boolean = false,
    @Json(name = "has_parking") val hasParking: Boolean = false,
    @Json(name = "has_breakfast") val hasBreakfast: Boolean = false,
    @Json(name = "has_pool") val hasPool: Boolean = false,
    @Json(name = "has_gym") val hasGym: Boolean = false,
    @Json(name = "has_spa") val hasSpa: Boolean = false,
    @Json(name = "has_restaurant") val hasRestaurant: Boolean = false,
    @Json(name = "has_24h_front_desk") val has24hFrontDesk: Boolean = false,
    @Json(name = "pet_friendly") val petFriendly: Boolean = false,
    @Json(name = "chat_enabled") val chatEnabled: Boolean = false,
    @Json(name = "icon_url") val iconUrl: String? = null,
    @Json(name = "cover_url") val coverUrl: String? = null,
    @Json(name = "rating_uz") val ratingUz: Double? = 0.0,
    @Json(name = "rating_guest") val ratingGuest: Double? = 0.0,
    @Json(name = "total_reviews") val totalReviews: Int? = 0,
    @Json(name = "is_active") val isActive: Boolean = true,
    @Json(name = "moderation_status") val moderationStatus: String? = null,
    @Json(name = "created_at") val createdAt: String? = null,
    @Json(name = "units") val units: List<PropertyUnit> = emptyList()
)

@JsonClass(generateAdapter = true)
data class PropertyUnit(
    @Json(name = "id") val id: Int,
    @Json(name = "unit_type") val unitType: String,
    @Json(name = "name_en") val nameEn: String,
    @Json(name = "base_price") val basePrice: Double,
    @Json(name = "max_guests") val maxGuests: Int?
)

@JsonClass(generateAdapter = true)
data class Tour(
    @Json(name = "id") val id: Int,
    @Json(name = "region_id") val regionId: Int,
    @Json(name = "creator_id") val creatorId: Int? = null,
    @Json(name = "name_en") val nameEn: String,
    @Json(name = "name_uz") val nameUz: String? = null,
    @Json(name = "name_ru") val nameRu: String? = null,
    @Json(name = "description_en") val descriptionEn: String? = null,
    @Json(name = "description_uz") val descriptionUz: String? = null,
    @Json(name = "description_ru") val descriptionRu: String? = null,
    @Json(name = "duration_days") val durationDays: Int = 1,
    @Json(name = "avg_total_cost") val avgTotalCost: Double? = 0.0,
    @Json(name = "avg_accommodation_cost") val avgAccommodationCost: Double? = null,
    @Json(name = "avg_food_cost") val avgFoodCost: Double? = null,
    @Json(name = "avg_transport_cost") val avgTransportCost: Double? = null,
    @Json(name = "avg_entertainment_cost") val avgEntertainmentCost: Double? = null,
    @Json(name = "transport_type") val transportType: String? = null,
    @Json(name = "cover_url") val coverUrl: String? = null,
    @Json(name = "is_active") val isActive: Boolean = true,
    @Json(name = "is_template") val isTemplate: Boolean = false,
    @Json(name = "created_at") val createdAt: String? = null,
    @Json(name = "stops") val stops: List<TourStop> = emptyList()
)

@JsonClass(generateAdapter = true)
data class TourCreateRequest(
    @Json(name = "region_id") val regionId: Int,
    @Json(name = "name_en") val nameEn: String,
    @Json(name = "name_uz") val nameUz: String? = null,
    @Json(name = "name_ru") val nameRu: String? = null,
    @Json(name = "description_en") val descriptionEn: String? = null,
    @Json(name = "description_uz") val descriptionUz: String? = null,
    @Json(name = "description_ru") val descriptionRu: String? = null,
    @Json(name = "duration_days") val durationDays: Int = 1,
    @Json(name = "transport_type") val transportType: String = "public",
    @Json(name = "property_ids") val propertyIds: List<Int> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ExpenseRequest(
    @Json(name = "tour_id") val tourId: Int,
    @Json(name = "region_id") val regionId: Int,
    @Json(name = "total_spent") val totalSpent: Double,
    @Json(name = "accommodation_spent") val accommodationSpent: Double,
    @Json(name = "food_spent") val foodSpent: Double,
    @Json(name = "transport_spent") val transportSpent: Double,
    @Json(name = "entertainment_spent") val entertainmentSpent: Double,
    @Json(name = "other_spent") val otherSpent: Double,
    @Json(name = "currency") val currency: String = "UZS",
    @Json(name = "comment") val comment: String? = null
)

@JsonClass(generateAdapter = true)
data class ExpenseResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "tour_id") val tourId: Int,
    @Json(name = "region_id") val regionId: Int,
    @Json(name = "total_spent") val totalSpent: Double,
    @Json(name = "accommodation_spent") val accommodationSpent: Double,
    @Json(name = "food_spent") val foodSpent: Double,
    @Json(name = "transport_spent") val transportSpent: Double,
    @Json(name = "entertainment_spent") val entertainmentSpent: Double,
    @Json(name = "other_spent") val otherSpent: Double,
    @Json(name = "currency") val currency: String,
    @Json(name = "comment") val comment: String?,
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class RegionAverage(
    @Json(name = "region_id") val regionId: Int,
    @Json(name = "region_name") val regionName: String,
    @Json(name = "total_reports") val totalReports: Int,
    @Json(name = "avg_total") val avgTotal: Double,
    @Json(name = "avg_accommodation") val avgAccommodation: Double,
    @Json(name = "avg_food") val avgFood: Double,
    @Json(name = "avg_transport") val avgTransport: Double,
    @Json(name = "avg_entertainment") val avgEntertainment: Double
)

@JsonClass(generateAdapter = true)
data class TourStop(
    @Json(name = "id") val id: Int,
    @Json(name = "property_id") val propertyId: Int,
    @Json(name = "stop_order") val stopOrder: Int,
    @Json(name = "note_en") val noteEn: String?
)

@JsonClass(generateAdapter = true)
data class AIQueryRequest(
    @Json(name = "message") val message: String,
    @Json(name = "budget") val budget: Double? = null,
    @Json(name = "currency") val currency: String = "UZS",
    @Json(name = "lat") val lat: Double? = null,
    @Json(name = "lon") val lon: Double? = null
)

@JsonClass(generateAdapter = true)
data class AIResponse(
    @Json(name = "reply") val reply: String,
    @Json(name = "weather") val weather: Map<String, Any>? = null,
    @Json(name = "suggestions") val suggestions: List<AISuggestion> = emptyList()
)

@JsonClass(generateAdapter = true)
data class AISuggestion(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "property_type") val propertyType: String,
    @Json(name = "description") val description: String,
    @Json(name = "rating") val rating: Double
)

@JsonClass(generateAdapter = true)
data class BookingRequest(
    @Json(name = "unit_id") val unitId: Int,
    @Json(name = "property_id") val propertyId: Int,
    @Json(name = "check_in_date") val checkInDate: String,
    @Json(name = "check_out_date") val checkOutDate: String,
    @Json(name = "rooms") val rooms: Int,
    @Json(name = "guests") val guests: Int,
    @Json(name = "message") val message: String?
)

@JsonClass(generateAdapter = true)
data class Booking(
    @Json(name = "id") val id: Int,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "unit_id") val unitId: Int,
    @Json(name = "property_id") val propertyId: Int,
    @Json(name = "check_in_date") val checkInDate: String,
    @Json(name = "check_out_date") val checkOutDate: String,
    @Json(name = "rooms") val rooms: Int,
    @Json(name = "guests") val guests: Int,
    @Json(name = "status") val status: String,
    @Json(name = "message") val message: String?,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "property") val property: Property? = null,
    @Json(name = "unit") val unit: PropertyUnit? = null
)

@JsonClass(generateAdapter = true)
data class ReviewRequest(
    @Json(name = "property_id") val propertyId: Int,
    @Json(name = "rating") val rating: Int,
    @Json(name = "text_en") val textEn: String?,
    @Json(name = "text_uz") val textUz: String?,
    @Json(name = "text_ru") val textRu: String?,
    @Json(name = "parent_id") val parentId: Int? = null
)

@JsonClass(generateAdapter = true)
data class Review(
    @Json(name = "id") val id: Int,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "property_id") val propertyId: Int,
    @Json(name = "parent_id") val parentId: Int?,
    @Json(name = "rating") val rating: Int,
    @Json(name = "text_en") val textEn: String?,
    @Json(name = "text_uz") val textUz: String?,
    @Json(name = "text_ru") val textRu: String?,
    @Json(name = "is_from_resident") val isFromResident: Boolean,
    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "replies") val replies: List<Review> = emptyList(),
    @Json(name = "user_name") val userName: String? = null,
    @Json(name = "user_avatar") val userAvatar: String? = null
)

@JsonClass(generateAdapter = true)
data class ReviewListResponse(
    @Json(name = "success") val success: Boolean = true,
    @Json(name = "data") val data: List<Review> = emptyList(),
    @Json(name = "total") val total: Int = 0,
    @Json(name = "rating_uz") val ratingUz: Double = 0.0,
    @Json(name = "rating_guest") val ratingGuest: Double = 0.0
)

@JsonClass(generateAdapter = true)
data class PropertyChatMessage(
    @Json(name = "id") val id: Int,
    @Json(name = "sender_id") val senderId: Int,
    @Json(name = "property_id") val propertyId: Int,
    @Json(name = "message") val message: String,
    @Json(name = "is_read") val isRead: Boolean,
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
    @Json(name = "property_id") val propertyId: Int,
    @Json(name = "message") val message: String
)

// Auth Models
@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class ForgotPasswordRequest(
    @Json(name = "email") val email: String
)

@JsonClass(generateAdapter = true)
data class ResetPasswordRequest(
    @Json(name = "token") val token: String,
    @Json(name = "new_password") val newPassword: String
)

@JsonClass(generateAdapter = true)
data class GoogleAuthRequest(
    @Json(name = "id_token") val idToken: String
)

@JsonClass(generateAdapter = true)
data class AppleAuthRequest(
    @Json(name = "identity_token") val identityToken: String,
    @Json(name = "authorization_code") val authorizationCode: String
)

@JsonClass(generateAdapter = true)
data class RefreshTokenRequest(
    @Json(name = "refresh_token") val refreshToken: String
)

@JsonClass(generateAdapter = true)
data class AuthData(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "token_type") val tokenType: String
)

@JsonClass(generateAdapter = true)
data class SavedItem(
    @Json(name = "id") val id: Int,
    @Json(name = "user_id") val userId: Int,
    @Json(name = "item_id") val itemId: Int,
    @Json(name = "item_type") val itemType: String, // "property" or "tour"
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "property") val property: Property? = null,
    @Json(name = "tour") val tour: Tour? = null
)

@JsonClass(generateAdapter = true)
data class UserProfile(
    @Json(name = "id") val id: Int,
    @Json(name = "email") val email: String,
    @Json(name = "name") val name: String?,
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "role") val role: String?,
    @Json(name = "preferred_currency") val preferredCurrency: String? = "UZS",
    @Json(name = "language") val language: String? = "uz",
    @Json(name = "is_verified") val isVerified: Boolean,
    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "login_attempts") val loginAttempts: Int? = 0,
    @Json(name = "locked_until") val lockedUntil: String? = null,
    @Json(name = "last_login") val lastLogin: String? = null,
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class NavigationStep(
    @Json(name = "stop_order") val stopOrder: Int,
    @Json(name = "property_id") val propertyId: Int,
    @Json(name = "property_name") val propertyName: String,
    @Json(name = "property_type") val propertyType: String,
    @Json(name = "lat") val lat: Double,
    @Json(name = "lon") val lon: Double,
    @Json(name = "duration_minutes") val durationMinutes: Int,
    @Json(name = "travel_time_minutes") val travelTimeMinutes: Int,
    @Json(name = "distance_km") val distanceKm: Double
)
