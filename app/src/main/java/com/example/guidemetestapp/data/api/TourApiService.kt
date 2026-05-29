package com.example.guidemetestapp.data.api

import com.example.guidemetestapp.data.model.Resort
import com.example.guidemetestapp.data.model.TourPackage
import com.example.guidemetestapp.data.model.TravelItinerary
import retrofit2.http.GET
import retrofit2.http.Query

interface TourApiService {
    @GET("tours/suggestions")
    suspend fun getTourSuggestions(): List<TourPackage>

    @GET("tours/detail")
    suspend fun getTourById(@Query("id") id: String): TourPackage

    @GET("resorts")
    suspend fun getResorts(): List<Resort>

    @GET("resorts/detail")
    suspend fun getResortById(@Query("id") id: String): Resort

    @GET("itinerary/generate")
    suspend fun generateItinerary(
        @Query("destination") destination: String,
        @Query("vehicle") vehicle: String,
        @Query("duration") duration: String
    ): TravelItinerary

    // AI Chat Endpoint placeholders
    @retrofit2.http.POST("ai/chat")
    suspend fun sendAiMessage(
        @retrofit2.http.Body request: com.example.guidemetestapp.data.model.AiChatRequest
    ): com.example.guidemetestapp.data.model.AiChatResponse
}
