package com.example.recipeapp.di

import android.content.Context
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.ui.recipes.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository,
) :
    Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}