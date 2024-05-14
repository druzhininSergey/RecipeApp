package com.example.recipeapp.di

import android.content.Context
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}