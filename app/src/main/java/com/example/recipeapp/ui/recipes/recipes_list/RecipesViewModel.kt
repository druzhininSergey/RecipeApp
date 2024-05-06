package com.example.recipeapp.ui.recipes.recipes_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.IMAGE_BASE_URL
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipesState = MutableLiveData(RecipesState())
    val recipesState: LiveData<RecipesState> = _recipesState
    private val recipesRepository = RecipesRepository()

    data class RecipesState(
        var recipesList: List<Recipe> = emptyList(),
        var categoryName: String? = null,
        var titleImageUrl: String? = null,
        var isError: Boolean = false,
    )

    fun loadRecipesList(categoryId: Int) {
        val category: Category? = recipesRepository.getCategoryByCategoryId(categoryId)
        if (category == null) _recipesState.value = recipesState.value?.copy(isError = true)

        val recipesList = recipesRepository.getRecipesListByCategoryId(categoryId)
        if (recipesList == null) _recipesState.value = recipesState.value?.copy(isError = true)
        else _recipesState.value = recipesState.value?.copy(
            recipesList = recipesList,
            categoryName = category?.title,
            titleImageUrl = IMAGE_BASE_URL + category?.imageUrl,
            isError = false
        )
    }
}