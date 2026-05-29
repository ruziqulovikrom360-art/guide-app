package com.example.guidemetestapp.data.model

data class GuideItem(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val rating: Double,
    val location: String,
    val category: String,
    val savedId: Int? = null
)
