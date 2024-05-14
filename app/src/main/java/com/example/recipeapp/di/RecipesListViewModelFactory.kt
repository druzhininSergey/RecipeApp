package com.example.recipeapp.di

import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.ui.recipes.recipes_list.RecipesListViewModel

class RecipesListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<RecipesListViewModel>{
    override fun create(): RecipesListViewModel {
        return RecipesListViewModel(recipesRepository)
    }
}