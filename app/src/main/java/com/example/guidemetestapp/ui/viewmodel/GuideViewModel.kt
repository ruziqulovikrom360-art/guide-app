package com.example.guidemetestapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemetestapp.data.api.UrlUtils
import com.example.guidemetestapp.data.model.*
import com.example.guidemetestapp.data.repository.GuideRepository
import com.example.guidemetestapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(
    private val repository: GuideRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _regions = MutableStateFlow<List<Region>>(emptyList())
    val regions: StateFlow<List<Region>> = _regions.asStateFlow()

    private val _properties = MutableStateFlow<List<Property>>(emptyList())
    val properties: StateFlow<List<Property>> = _properties.asStateFlow()

    private val _tours = MutableStateFlow<List<Tour>>(emptyList())
    val tours: StateFlow<List<Tour>> = _tours.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearError() {
        _error.value = null
    }

    val filteredProperties: StateFlow<List<Property>> = combine(
        _properties, _searchQuery
    ) { props, query ->
        if (query.isBlank()) props
        else props.filter { it.nameEn.contains(query, ignoreCase = true) || it.descriptionEn?.contains(query, ignoreCase = true) == true }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredTours: StateFlow<List<Tour>> = combine(
        _tours, _searchQuery
    ) { ts, query ->
        if (query.isBlank()) ts
        else ts.filter { it.nameEn.contains(query, ignoreCase = true) || it.descriptionEn?.contains(query, ignoreCase = true) == true }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedItems: StateFlow<List<SavedItem>> = repository.savedItems

    val favoritePropertyIds: StateFlow<Set<Int>> = savedItems.map { items -> 
        items.filter { it.itemType == "property" }.map { it.itemId }.toSet() 
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val favoriteTourIds: StateFlow<Set<Int>> = savedItems.map { items -> 
        items.filter { it.itemType == "tour" }.map { it.itemId }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val favoriteGuides: StateFlow<List<GuideItem>> = combine(
        savedItems, _properties, _tours
    ) { saved, props, ts ->
        saved.mapNotNull { item ->
            when (item.itemType) {
                "property" -> {
                    val p = props.find { it.id == item.itemId } ?: item.property
                    p?.let {
                        GuideItem(
                            id = it.id,
                            title = it.nameEn,
                            description = it.descriptionEn ?: "",
                            imageUrl = UrlUtils.getFullUrl(it.coverUrl),
                            rating = it.ratingGuest ?: 0.0,
                            location = it.address ?: "",
                            category = it.propertyType,
                            savedId = item.id
                        )
                    }
                }
                "tour" -> {
                    val t = ts.find { it.id == item.itemId } ?: item.tour
                    t?.let {
                        GuideItem(
                            id = it.id,
                            title = it.nameEn,
                            description = it.descriptionEn ?: "",
                            imageUrl = UrlUtils.getFullUrl(it.coverUrl),
                            rating = it.avgTotalCost ?: 0.0,
                            location = it.transportType ?: "",
                            category = "Tour",
                            savedId = item.id
                        )
                    }
                }
                else -> null
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        fetchAllData()
        observeCurrentUser()
        viewModelScope.launch {
            repository.refreshSavedItems()
        }
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            authRepository.currentUser.collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun fetchAllData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _regions.value = repository.getRegions()
                _properties.value = repository.getProperties()
                _tours.value = repository.getTours()
            } catch (e: Exception) {
                _error.value = "Failed to load data: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(itemId: Int, type: String = "property") {
        viewModelScope.launch {
            val existing = savedItems.value.find { it.itemId == itemId && it.itemType == type }
            if (existing != null) {
                repository.removeSaved(existing.id).onFailure {
                    _error.value = "Failed to remove favorite: ${it.localizedMessage}"
                }
            } else {
                repository.saveItem(type, itemId).onFailure {
                    _error.value = "Failed to save favorite: ${it.localizedMessage}"
                }
            }
        }
    }

    suspend fun getTourNavigation(tourId: Int): List<NavigationStep> {
        return repository.getTourNavigation(tourId)
    }
}
