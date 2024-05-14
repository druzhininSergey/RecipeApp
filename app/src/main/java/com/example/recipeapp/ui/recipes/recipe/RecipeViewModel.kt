package com.example.recipeapp.ui.recipes.recipe

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.FAVORITES_PREFS_NAME
import com.example.recipeapp.data.FAVORITE_PREFS_KEY
import com.example.recipeapp.data.IMAGE_BASE_URL
import com.example.recipeapp.data.MIN_RECIPE_SERVINGS
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.di.SharedPreferencesManager
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(
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
            val isFavorite = getFavorites().contains(recipeId.toString())

            _recipeState.value = recipeState.value?.copy(
                recipe = recipe,
                isFavorite = isFavorite,
                recipeImageUrl = IMAGE_BASE_URL + recipe.imageUrl,
                isError = false,
            )
        }
    }

    fun onFavoritesClicked() {
        val favorites = getFavorites()
        val recipeId = _recipeState.value?.recipe?.id.toString()
        val isFavorite = favorites.contains(recipeId)

        if (!isFavorite) favorites.add(recipeId)
        else favorites.remove(recipeId)
        saveFavorites(favorites)
        viewModelScope.launch {
            recipesRepository.updateRecipeInCache(
                recipesRepository.getRecipeByRecipeIdFromCache(
                    recipeId.toInt()
                )
            )
        }
        _recipeState.value = recipeState.value?.copy(isFavorite = !isFavorite)
    }

    private fun getFavorites(): MutableSet<String> {
        return SharedPreferencesManager.getFavorites()
    }

    private fun saveFavorites(idList: Set<String>) {
        viewModelScope.launch { SharedPreferencesManager.saveFavorites(idList) }
    }

    fun onChangeServings(servings: Int) {
        _recipeState.value = recipeState.value?.copy(servings = servings)
    }
}