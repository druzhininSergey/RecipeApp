package com.example.recipeapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.FAVORITES_PREFS_NAME
import com.example.recipeapp.data.FAVORITE_PREFS_KEY
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Recipe

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private var _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> = _favoritesState
    private val recipesRepository = RecipesRepository()

    data class FavoritesState(
        var favoritesList: List<Recipe> = emptyList(),
        var isError: Boolean = false,
    )

    fun loadFavorites() {
        val favoritesIds = getFavorites()
        if (favoritesIds.isEmpty()) {
            _favoritesState.value = favoritesState.value?.copy(favoritesList = emptyList())
            return
        }
        val favorites = recipesRepository.getRecipesByIdsList(favoritesIds.joinToString(","))
        if (favorites == null) _favoritesState.value = favoritesState.value?.copy(isError = true)
        else _favoritesState.value =
            favoritesState.value?.copy(favoritesList = favorites, isError = false)
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            FAVORITES_PREFS_NAME,
            Context.MODE_PRIVATE
        )
        return HashSet(
            sharedPrefs?.getStringSet(FAVORITE_PREFS_KEY, HashSet<String>()) ?: mutableSetOf()
        )
    }
}