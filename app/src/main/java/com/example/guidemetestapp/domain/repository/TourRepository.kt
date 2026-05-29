package com.example.guidemetestapp.domain.repository

import com.example.guidemetestapp.data.model.Resort
import com.example.guidemetestapp.data.model.TourPackage
import com.example.guidemetestapp.data.model.TravelItinerary

interface TourRepository {
    suspend fun getTourSuggestions(): Result<List<TourPackage>>
    suspend fun getResorts(): Result<List<Resort>>
    suspend fun getTourById(id: String): Result<TourPackage>
    suspend fun getResortById(id: String): Result<Resort>
    suspend fun generateItinerary(destination: String, vehicle: String, duration: String): Result<TravelItinerary>
}
