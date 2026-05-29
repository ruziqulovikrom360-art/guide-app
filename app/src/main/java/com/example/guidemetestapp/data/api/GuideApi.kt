package com.example.guidemetestapp.data.api

import com.example.guidemetestapp.data.model.*
import retrofit2.http.*

interface GuideApi {
    @GET("api/v1/regions/")
    suspend fun getRegions(
        @Query("active_only") activeOnly: Boolean = true
    ): ListResponse<Region>

    @GET("api/v1/regions/{region_id}")
    suspend fun getRegion(
        @Path("region_id") regionId: Int
    ): Region

    @GET("api/v1/properties/")
    suspend fun getProperties(
        @Query("region_id") regionId: Int? = null,
        @Query("property_type") propertyType: String? = null,
        @Query("stars") stars: Int? = null,
        @Query("search") search: String? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): ListResponse<Property>

    @GET("api/v1/properties/{property_id}")
    suspend fun getProperty(
        @Path("property_id") propertyId: Int
    ): Property

    @GET("api/v1/tours/")
    suspend fun getTours(
        @Query("region_id") regionId: Int? = null,
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): ListResponse<Tour>

    @GET("api/v1/tours/{tour_id}")
    suspend fun getTour(
        @Path("tour_id") tourId: Int
    ): Tour

    @GET("api/v1/tours/{tour_id}/navigation")
    suspend fun getTourNavigation(
        @Path("tour_id") tourId: Int
    ): DataResponse<List<NavigationStep>>

    @POST("api/v1/tours/")
    suspend fun createTour(
        @Body request: TourCreateRequest
    ): DataResponse<Tour>

    @POST("api/v1/tours/expenses")
    suspend fun addTourExpense(
        @Body request: ExpenseRequest
    ): DataResponse<ExpenseResponse>

    @GET("api/v1/tours/averages/regions")
    suspend fun getRegionAverages(): DataResponse<List<RegionAverage>>

    @POST("api/v1/ai/")
    suspend fun chatWithAI(
        @Body request: AIQueryRequest
    ): AIResponse

    // Booking
    @POST("api/v1/booking/")
    suspend fun createBooking(
        @Body request: BookingRequest
    ): DataResponse<Booking>

    @GET("api/v1/booking/")
    suspend fun getMyBookings(): DataResponse<List<Booking>>

    // Reviews
    @GET("api/v1/reviews/property/{property_id}")
    suspend fun getPropertyReviews(
        @Path("property_id") propertyId: Int
    ): ReviewListResponse

    @POST("api/v1/reviews/")
    suspend fun addReview(
        @Body review: ReviewRequest
    ): DataResponse<Review>

    // Chat
    @GET("api/v1/chat/{property_id}")
    suspend fun getChatMessages(
        @Path("property_id") propertyId: Int
    ): DataResponse<List<PropertyChatMessage>>

    @POST("api/v1/chat/")
    suspend fun sendMessage(
        @Body request: SendMessageRequest
    ): DataResponse<PropertyChatMessage>

    // Auth
    @POST("api/v1/auth/register")
    suspend fun register(@Body request: RegisterRequest): DataResponse<AuthData>

    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): DataResponse<AuthData>

    @POST("api/v1/auth/forgot-password")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): DataResponse<String>

    @POST("api/v1/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): DataResponse<String>

    @POST("api/v1/auth/google")
    suspend fun authGoogle(@Body request: GoogleAuthRequest): DataResponse<AuthData>

    @POST("api/v1/auth/apple")
    suspend fun authApple(@Body request: AppleAuthRequest): DataResponse<AuthData>

    
    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): DataResponse<AuthData>

    @GET("api/v1/auth/me")
    suspend fun getMe(): DataResponse<UserProfile>

    // Saved (Favorites)
    @GET("api/v1/saved/")
    suspend fun getSaved(
        @Query("item_type") itemType: String? = null
    ): DataResponse<List<SavedItem>>

    @POST("api/v1/saved/")
    suspend fun saveItem(
        @Query("item_type") itemType: String,
        @Query("item_id") itemId: Int
    ): DataResponse<String>

    @DELETE("api/v1/saved/{saved_id}")
    suspend fun removeSaved(
        @Path("saved_id") savedId: Int
    ): DataResponse<String>

    @GET("/")
    suspend fun getRoot(): String
}
