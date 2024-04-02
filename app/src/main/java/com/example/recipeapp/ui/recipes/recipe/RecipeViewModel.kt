package com.example.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        var title: String? = null,
        var ingredients: List<IngredientState>? = null,
        var method: List<String>? = null,
        var imageUrl: String? = null,
    )

    data class IngredientState(
        var quantity: String? = null,
        var unitOfMeasure: String? = null,
        var description: String? = null,
    )

}