package com.example.guidemetestapp.presentation.planner

enum class VehicleType(val displayName: String, val basePrice: Double) {
    PUBLIC("Public transport", 15.0),
    PRIVATE("Private car", 50.0)
}

data class PlannerState(
    val selectedPackage: String = "Toshkent",
    val selectedTour: com.example.guidemetestapp.data.model.Tour? = null,
    val availableTours: List<com.example.guidemetestapp.data.model.Tour> = emptyList(),
    val vehicleType: VehicleType = VehicleType.PUBLIC,
    val durationDays: Int = 7,
    val paymentMethod: String = "Visa *3811",
    val promoCode: String = "",
    val placesPrice: Double = 200.0, // Fixed base for places in this example
    val serviceFee: Double = 7.0,
    val total: Double = 222.0,
    val isLoading: Boolean = false
)

sealed class PlannerSideEffect {
    object NavigateToHome : PlannerSideEffect()
    object StartTour : PlannerSideEffect()
    data class ShowToast(val message: String) : PlannerSideEffect()
}
