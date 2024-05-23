package com.example.recipeapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
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
            val favoritesIds = recipesRepository.getFavoriteRecipesFromCache().map { it.id }
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
}