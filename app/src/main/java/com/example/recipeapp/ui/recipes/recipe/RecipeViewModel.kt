package com.example.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import com.example.recipeapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeState(
        var recipe: Recipe? = null,
        var servings: Int = 1,
        var isFavorites: Boolean = false,
    )

}