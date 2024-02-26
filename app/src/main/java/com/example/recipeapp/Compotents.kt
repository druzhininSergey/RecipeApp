package com.example.recipeapp

data class Category(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
)

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredients>,
    val method: List<String>,
    val imageUrl: String,
)

data class Ingredients(
    val quantity: Double,
    val unitOfMeasure: String,
    val description: String,
)
