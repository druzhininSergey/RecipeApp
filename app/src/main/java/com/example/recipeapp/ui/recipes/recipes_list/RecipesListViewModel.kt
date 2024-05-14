package com.example.recipeapp.ui.recipes.recipes_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.IMAGE_BASE_URL
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesListViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    private var _recipesState = MutableLiveData(RecipesState())
    val recipesState: LiveData<RecipesState> = _recipesState


    data class RecipesState(
        var recipesList: List<Recipe> = emptyList(),
        var categoryName: String? = null,
        var titleImageUrl: String? = null,
        var isError: Boolean = false,
    )

    fun loadRecipesList(categoryId: Int) {
        viewModelScope.launch {
            val category: Category? = recipesRepository.getCategoryByCategoryId(categoryId)
            if (category == null) _recipesState.value = recipesState.value?.copy(isError = true)

            val recipesListDB = recipesRepository.getRecipeListFromCache()
            val recipeListBackend = recipesRepository.getRecipesListByCategoryId(categoryId)
            recipeListBackend?.let { recipesRepository.addRecipeListToCache(it) }
            val recipesList = recipeListBackend ?: recipesListDB

            _recipesState.value = recipesState.value?.copy(
                recipesList = recipesList,
                categoryName = category?.title,
                titleImageUrl = IMAGE_BASE_URL + category?.imageUrl,
                isError = false
            )
        }
    }
}