package com.example.recipeapp.ui.recipes.favorites

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.FAVORITES_PREFS_NAME
import com.example.recipeapp.data.FAVORITE_PREFS_KEY
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.di.SharedPreferencesManager
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    private var _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> = _favoritesState

    data class FavoritesState(
        var favoritesList: List<Recipe> = emptyList(),
        var isError: Boolean = false,
    )

    fun loadFavorites() {
        viewModelScope.launch {
            val favoritesIds = getFavorites()
            if (favoritesIds.isEmpty()) {
                _favoritesState.value = favoritesState.value?.copy(favoritesList = emptyList())
            } else {
                val favorites =
                    recipesRepository.getRecipesByIdsList(favoritesIds.joinToString(","))
                        ?: recipesRepository.getFavoriteRecipesFromCache()
                _favoritesState.value =
                    favoritesState.value?.copy(favoritesList = favorites, isError = false)
            }
        }
    }

    private fun getFavorites(): MutableSet<String> {
        return SharedPreferencesManager.getFavorites()
    }
}