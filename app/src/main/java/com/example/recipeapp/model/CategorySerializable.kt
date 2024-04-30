package com.example.recipeapp.model

import kotlinx.serialization.Serializable

@Serializable
data class CategorySerializable(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
)