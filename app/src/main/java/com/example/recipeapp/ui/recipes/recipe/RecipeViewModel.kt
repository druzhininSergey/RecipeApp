package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.R
import com.example.recipeapp.data.FAVORITES_PREFS_NAME
import com.example.recipeapp.data.FAVORITE_PREFS_KEY
import com.example.recipeapp.data.MIN_RECIPE_SERVINGS
import com.example.recipeapp.data.STUB
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.example.recipeapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState> = _recipeState

    init {
        Log.i("!!!", "Создан объект RecipeViewModel")
        _recipeState.value = RecipeState(isFavorites = false)
    }

    data class RecipeState(
        var recipe: Recipe? = null,
        var servings: Int = MIN_RECIPE_SERVINGS,
        var isFavorites: Boolean = false,
        var imageUrl: String? = null,
    )

    fun loadRecipe(recipeId: Int) {
//        TODO("load from network")
        val recipe = STUB.getRecipeById(recipeId)
        val isFavorite = getFavorites().contains(recipeId.toString())
        _recipeState.value = RecipeState(
            recipe = recipe,
            isFavorites = isFavorite,
            imageUrl = recipe.imageUrl
        )
    }

    fun onFavoritesClicked(binding: FragmentRecipeBinding) {
        val favorites = getFavorites()
        val recipeId = _recipeState.value?.recipe?.id.toString()
        val isFavorite = favorites.contains(recipeId)

        if (!isFavorite) {
            binding.ibHeart.apply {
                setImageResource(R.drawable.ic_heart)
                contentDescription = "Button to remove from favorites"
            }
            favorites.add(recipeId)
        } else {
            binding.ibHeart.apply {
                setImageResource(R.drawable.ic_heart_empty)
                contentDescription = "Button to add in favorites"
            }
            favorites.remove(recipeId)
        }
        saveFavorites(favorites)
        _recipeState.value = _recipeState.value?.copy(isFavorites = !isFavorite)
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