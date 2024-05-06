package com.example.recipeapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.FAVORITES_PREFS_NAME
import com.example.recipeapp.data.FAVORITE_PREFS_KEY
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private var _favoritesState = MutableLiveData(FavoritesState())
    val favoritesState: LiveData<FavoritesState> = _favoritesState
    val recipesRepository = RecipesRepository()

    data class FavoritesState(
        val favoritesList: List<Recipe> = emptyList(),
    )

    fun loadFavorites() {
        viewModelScope.launch {
            val favoritesIds = getFavorites()
            val favorites = recipesRepository.getRecipesByIdsList(favoritesIds.joinToString(","))
            if (favorites == null) Toast
                .makeText(getApplication(), "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            else _favoritesState.value = favoritesState.value?.copy(favoritesList = favorites)
        }
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