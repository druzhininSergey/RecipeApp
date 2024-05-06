package com.example.recipeapp.ui.recipes.recipes_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.IMAGE_BASE_URL
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipesState = MutableLiveData(RecipesState())
    val recipesState: LiveData<RecipesState> = _recipesState
    private val recipesRepository = RecipesRepository()

    data class RecipesState(
        val recipesList: List<Recipe> = emptyList(),
        val categoryName: String? = null,
        val titleImageUrl: String? = null,
    )

    fun loadRecipesList(categoryId: Int) {
        viewModelScope.launch {
            val category: Category? = recipesRepository.getCategoryByCategoryId(categoryId)
            val recipesList = recipesRepository.getRecipesListByCategoryId(categoryId)
            _recipesState.value = recipesList?.let {
                recipesState.value?.copy(
                    recipesList = it,
                    categoryName = category?.title,
                    titleImageUrl = IMAGE_BASE_URL + category?.imageUrl,
                )
            }
        }
    }
}