package com.example.recipeapp.model

import android.adservices.adid.AdId
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity
@Serializable
@Parcelize
data class Category(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
) : Parcelable

@Entity
@Serializable
@Parcelize
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "category_id") val categoryId: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "ingredients") val ingredients: List<Ingredient>,
    @ColumnInfo(name = "method") val method: List<String>,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
) : Parcelable

@Entity
@Serializable
@Parcelize
data class Ingredient(
    @ColumnInfo(name = "quantity") val quantity: String,
    @ColumnInfo(name = "unit_of_measure") val unitOfMeasure: String,
    @ColumnInfo(name = "description") val description: String,
) : Parcelable