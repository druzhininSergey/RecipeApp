package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.FAVORITES_PREFS_NAME
import com.example.recipeapp.data.FAVORITE_PREFS_KEY
import com.example.recipeapp.data.IMAGE_BASE_URL
import com.example.recipeapp.data.MIN_RECIPE_SERVINGS
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.launch
import java.io.InputStream

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipeState = MutableLiveData(RecipeState())
    val recipeState: LiveData<RecipeState> = _recipeState
    val recipesRepository = RecipesRepository()

    data class RecipeState(
        var recipe: Recipe? = null,
        var servings: Int = MIN_RECIPE_SERVINGS,
        var isFavorite: Boolean = false,
        var recipeImageUrl: String? = null
    )

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            val recipe = recipesRepository.getRecipeByRecipeId(recipeId)
            if (recipe == null) Toast
                .makeText(getApplication(), "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            val isFavorite = getFavorites().contains(recipeId.toString())
            _recipeState.value = recipeState.value?.copy(
                recipe = recipe,
                isFavorite = isFavorite,
                recipeImageUrl = IMAGE_BASE_URL + recipe?.imageUrl
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

        _recipeState.value = recipeState.value?.copy(isFavorite = !isFavorite)
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

    private fun saveFavorites(idList: Set<String>) {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(
            FAVORITES_PREFS_NAME,
            Context.MODE_PRIVATE
        )
        with(sharedPrefs?.edit()) {
            this?.putStringSet(FAVORITE_PREFS_KEY, idList)
            this?.apply()
        }
    }

    fun onChangeServings(servings: Int) {
        _recipeState.value = recipeState.value?.copy(servings = servings)
    }
}