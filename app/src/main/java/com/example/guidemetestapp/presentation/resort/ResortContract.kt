package com.example.guidemetestapp.presentation.resort

data class Resort(
    val id: String,
    val name: String,
    val description: String,
    val pricePerNight: Double,
    val rating: Double,
    val views: Int,
    val distance: Double,
    val imageUrl: String,
    val rooms: Int
)

data class ResortState(
    val resorts: List<Resort> = emptyList(),
    val filteredResorts: List<Resort> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val sortByPriceAsc: Boolean = true
)

sealed class ResortSideEffect {
    data class ShowError(val message: String) : ResortSideEffect()
}
