package com.example.guidemetestapp.presentation.planner

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.*
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PlannerViewModel @Inject constructor(
    private val repository: com.example.guidemetestapp.data.repository.GuideRepository
) : ViewModel(), ContainerHost<PlannerState, PlannerSideEffect> {

    override val container = container<PlannerState, PlannerSideEffect>(PlannerState())

    init {
        loadTours()
    }

    private fun loadTours() = intent {
        reduce { state.copy(isLoading = true) }
        try {
            val tours = repository.getTours()
            reduce {
                state.copy(
                    isLoading = false,
                    availableTours = tours
                )
            }
        } catch (e: Exception) {
            reduce { state.copy(isLoading = false) }
            postSideEffect(PlannerSideEffect.ShowToast(e.localizedMessage ?: "Failed to load tours"))
        }
    }

    fun onTourSelected(tour: com.example.guidemetestapp.data.model.Tour) = intent {
        reduce {
            val newState = state.copy(
                selectedTour = tour,
                selectedPackage = tour.nameEn,
                placesPrice = tour.avgTotalCost ?: 0.0,
                durationDays = tour.durationDays
            )
            newState.copy(total = calculateTotal(newState))
        }
    }

    fun onVehicleTypeChanged(type: VehicleType) = intent {
        reduce {
            val newState = state.copy(vehicleType = type)
            newState.copy(total = calculateTotal(newState))
        }
    }

    fun onDurationChanged(days: Int) = intent {
        reduce {
            val newState = state.copy(durationDays = days)
            newState.copy(total = calculateTotal(newState))
        }
    }

    fun onPromoCodeChanged(code: String) = intent {
        reduce {
            val discount = if (code.equals("GUIDEME", ignoreCase = true)) 10.0 else 0.0
            val newState = state.copy(promoCode = code)
            newState.copy(total = calculateTotal(newState) - discount)
        }
    }

    private fun calculateTotal(state: PlannerState): Double {
        val durationMultiplier = when (state.durationDays) {
            3 -> 0.6
            10 -> 1.3
            else -> 1.0
        }
        val basePrice = state.placesPrice * durationMultiplier
        return basePrice + state.vehicleType.basePrice + state.serviceFee
    }

    fun startTour() = intent {
        postSideEffect(PlannerSideEffect.StartTour)
    }
}
