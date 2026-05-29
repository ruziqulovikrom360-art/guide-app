package com.example.guidemetestapp.data.repository

import com.example.guidemetestapp.data.model.GuideItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object FavoriteRepository {
    private val _favorites = MutableStateFlow<Set<Int>>(emptySet())
    val favorites: StateFlow<Set<Int>> = _favorites.asStateFlow()

    fun toggleFavorite(itemId: Int) {
        val current = _favorites.value
        if (current.contains(itemId)) {
            _favorites.value = current - itemId
        } else {
            _favorites.value = current + itemId
        }
    }

    fun isFavorite(itemId: Int): Boolean {
        return _favorites.value.contains(itemId)
    }
}
