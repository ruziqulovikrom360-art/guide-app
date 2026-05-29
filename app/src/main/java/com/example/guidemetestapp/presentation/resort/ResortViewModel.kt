package com.example.guidemetestapp.presentation.resort

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.*
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ResortViewModel @Inject constructor(
    private val repository: com.example.guidemetestapp.data.repository.GuideRepository
) : ViewModel(), ContainerHost<ResortState, ResortSideEffect> {

    override val container = container<ResortState, ResortSideEffect>(ResortState())

    init {
        loadResorts()
    }

    private fun loadResorts() = intent {
        reduce { state.copy(isLoading = true) }
        try {
            val properties = repository.getProperties()
            val resorts = properties.map { property ->
                Resort(
                    id = property.id.toString(),
                    name = property.nameEn,
                    description = property.descriptionEn ?: "",
                    pricePerNight = (property.units.firstOrNull()?.basePrice ?: 50.0),
                    rating = property.ratingGuest ?: 0.0,
                    views = property.totalReviews ?: 0,
                    distance = 0.0, // Not available in API currently
                    imageUrl = property.coverUrl ?: "",
                    rooms = property.units.size
                )
            }
            reduce { 
                state.copy(
                    isLoading = false, 
                    resorts = resorts,
                    filteredResorts = resorts
                ) 
            }
        } catch (e: Exception) {
            reduce { state.copy(isLoading = false) }
            postSideEffect(ResortSideEffect.ShowError(e.localizedMessage ?: "Failed to load resorts"))
        }
    }

    fun onSearchQueryChanged(query: String) = intent {
        reduce {
            state.copy(
                searchQuery = query,
                filteredResorts = filterAndSort(state.resorts, query, state.sortByPriceAsc)
            )
        }
    }

    fun toggleSortOrder() = intent {
        reduce {
            val newSortOrder = !state.sortByPriceAsc
            state.copy(
                sortByPriceAsc = newSortOrder,
                filteredResorts = filterAndSort(state.resorts, state.searchQuery, newSortOrder)
            )
        }
    }

    private fun filterAndSort(resorts: List<Resort>, query: String, priceAsc: Boolean): List<Resort> {
        return resorts
            .filter { it.name.contains(query, ignoreCase = true) }
            .sortedBy { if (priceAsc) it.pricePerNight else -it.pricePerNight }
    }
}
