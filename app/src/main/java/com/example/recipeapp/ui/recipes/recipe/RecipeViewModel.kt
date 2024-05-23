package com.example.recipeapp.ui.recipes.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.IMAGE_BASE_URL
import com.example.recipeapp.data.MIN_RECIPE_SERVINGS
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    private var _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState

    data class RecipeState(
        var recipe: Recipe? = null,
        var servings: Int = MIN_RECIPE_SERVINGS,
        var isFavorite: Boolean = false,
        var recipeImageUrl: String? = null,
        var isError: Boolean = false,
    )

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipeDB = recipesRepository.getRecipeByRecipeIdFromCache(recipeId)
            val recipeBackend = recipesRepository.getRecipeByRecipeId(recipeId)

            val recipe = recipeBackend ?: recipeDB
            val isFavorite = recipeDB.isFavorite

            _recipeState.value = recipeState.value?.copy(
                recipe = recipe,
                isFavorite = isFavorite,
                recipeImageUrl = IMAGE_BASE_URL + recipe.imageUrl,
                isError = false,
            )
        }
    }

    fun onFavoritesClicked() {
        viewModelScope.launch {
            val recipeId = _recipeState.value?.recipe?.id
            val recipe = recipeId?.let { recipesRepository.getRecipeByRecipeIdFromCache(it) }
            if (recipe != null) {
                val isFavorite =
                    recipesRepository.getRecipeByRecipeIdFromCache(recipeId).isFavorite
                _recipeState.value = recipeState.value?.copy(isFavorite = !isFavorite)
                recipesRepository.updateRecipeInCache(recipe.copy(isFavorite = !isFavorite))
            }
        }
    }

    fun onChangeServings(servings: Int) {
        _recipeState.value = recipeState.value?.copy(servings = servings)
    }
}