package com.example.recipeapp.data

import androidx.room.TypeConverter
import com.example.recipeapp.model.Ingredient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class Converters {
    @TypeConverter
    fun fromIngredientsList(ingredients: List<Ingredient>?): String? {
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientList(ingredientsString: String?): List<Ingredient>? {
        return Json.decodeFromString<List<Ingredient>>(ingredientsString ?: "")
    }

    @TypeConverter
    fun fromMethodsList(method: List<String>?): String?{
        return Json.encodeToString(method)
    }

    @TypeConverter
    fun toMethodList(methodString: String?) :List<String>?{
        return Json.decodeFromString<List<String>>(methodString ?: "")
    }
}
