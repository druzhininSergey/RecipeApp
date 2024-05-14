package com.example.recipeapp.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category
import kotlinx.coroutines.launch

class CategoriesViewModel(private val recipesRepository: RecipesRepository,) : ViewModel() {

    private var _categoriesState = MutableLiveData(CategoriesState())
    val categoriesState: LiveData<CategoriesState> = _categoriesState

    data class CategoriesState(
        var categories: List<Category> = emptyList(),
        var isError: Boolean = false,
    )

    fun loadCategories() {
        viewModelScope.launch {
            val categoriesDb = recipesRepository.getCategoriesFromCache()
            val categories = if (categoriesDb.isNotEmpty()) {
                categoriesDb
            } else {
                val categoriesBackend = recipesRepository.getCategories()
                categoriesBackend?.let { recipesRepository.addCategoriesToCache(it) }
                categoriesBackend
            }

            if (categories == null) _categoriesState.value =
                categoriesState.value?.copy(isError = true)
            else _categoriesState.value =
                categoriesState.value?.copy(categories = categories, isError = false)
        }
    }
}