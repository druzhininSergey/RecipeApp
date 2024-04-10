package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.FAVORITES_PREFS_NAME
import com.example.recipeapp.data.FAVORITE_PREFS_KEY
import com.example.recipeapp.data.MIN_RECIPE_SERVINGS
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Recipe
import java.io.InputStream

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState> = _recipeState

    data class RecipeState(
        var recipe: Recipe? = null,
        var servings: Int = MIN_RECIPE_SERVINGS,
        var isFavorite: Boolean = false,
        var recipeImage: Drawable? = null
    )

    fun loadRecipe(recipeId: Int) {
//        TODO("load from network")
        val recipe = STUB.getRecipeById(recipeId)
        val isFavorite = getFavorites().contains(recipeId.toString())
        var titleImage: Drawable? = null
        try {
            val inputStream: InputStream? = getApplication<Application>().assets?.open(recipe.imageUrl)
            titleImage = Drawable.createFromStream(inputStream, null)
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
        _recipeState.value = RecipeState(
            recipe = recipe,
            isFavorite = isFavorite,
            recipeImage = titleImage
        )
    }

    fun onFavoritesClicked() {
        val favorites = getFavorites()
        val recipeId = _recipeState.value?.recipe?.id.toString()
        val isFavorite = favorites.contains(recipeId)

        if (!isFavorite) favorites.add(recipeId)
        else favorites.remove(recipeId)
        saveFavorites(favorites)

        _recipeState.value = _recipeState.value?.copy(isFavorite = !isFavorite)
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
}