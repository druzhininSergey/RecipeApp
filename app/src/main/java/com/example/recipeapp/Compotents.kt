package com.example.recipeapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Category(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
)

@Parcelize
data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
    val method: List<String>,
    val imageUrl: String,
) : Parcelable

@Parcelize
data class Ingredient(
    val quantity: String,
    val unitOfMeasure: String,
    val description: String,
) : Parcelable
