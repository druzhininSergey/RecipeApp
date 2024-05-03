package com.example.recipeapp.ui.category

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private var _categoriesState = MutableLiveData<CategoriesState>()
    val categoriesState: LiveData<CategoriesState> = _categoriesState
    val recipesRepository = RecipesRepository()

    data class CategoriesState(
        val categories: List<Category> = emptyList(),
    )

    fun loadCategories() {
        val categories = recipesRepository.getCategories()
        if (categories == null) Toast.makeText(
            getApplication(),
            "Ошибка получения данных",
            Toast.LENGTH_SHORT
        ).show()
        else _categoriesState.value = categories.let { CategoriesState(it) }
    }
}