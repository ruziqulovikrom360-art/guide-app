package com.example.guidemetestapp.data.repository

import com.example.guidemetestapp.data.api.TourApiService
import com.example.guidemetestapp.data.model.Resort
import com.example.guidemetestapp.data.model.TourPackage
import com.example.guidemetestapp.data.model.TravelItinerary
import com.example.guidemetestapp.domain.repository.TourRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TourRepositoryImpl @Inject constructor(
    private val apiService: TourApiService
) : TourRepository {

    override suspend fun getTourSuggestions(): Result<List<TourPackage>> {
        return try {
            val response = apiService.getTourSuggestions()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getResorts(): Result<List<Resort>> {
        return try {
            val response = apiService.getResorts()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTourById(id: String): Result<TourPackage> {
        return try {
            val response = apiService.getTourById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getResortById(id: String): Result<Resort> {
        return try {
            val response = apiService.getResortById(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateItinerary(
        destination: String,
        vehicle: String,
        duration: String
    ): Result<TravelItinerary> {
        return try {
            val response = apiService.generateItinerary(destination, vehicle, duration)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
