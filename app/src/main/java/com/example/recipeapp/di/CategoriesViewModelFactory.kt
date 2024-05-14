package com.example.recipeapp.di

import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.ui.category.CategoriesViewModel

class CategoriesViewModelFactory(private val recipesRepository: RecipesRepository) :
    Factory<CategoriesViewModel> {
    override fun create(): CategoriesViewModel {
        return CategoriesViewModel(recipesRepository)
    }
}