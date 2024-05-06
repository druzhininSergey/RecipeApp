package com.example.recipeapp.ui.category

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category
import java.lang.Error

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private var _categoriesState = MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> = _categoriesState
    private val recipesRepository = RecipesRepository()

    data class CategoriesState(
        var categories: List<Category> = emptyList(),
        var isError: Boolean = false,
    )

    fun loadCategories() {
        val categories = recipesRepository.getCategories()
        if (categories == null) _categoriesState.value = categoriesState.value?.copy(isError = true)
        else _categoriesState.value =
            categoriesState.value?.copy(categories = categories, isError = false)
    }
}