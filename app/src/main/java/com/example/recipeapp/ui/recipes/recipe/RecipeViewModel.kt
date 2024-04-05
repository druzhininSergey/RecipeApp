package com.example.recipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.FAVORITES_PREFS_NAME
import com.example.recipeapp.data.FAVORITE_PREFS_KEY
import com.example.recipeapp.data.MIN_RECIPE_SERVINGS
import com.example.recipeapp.model.Recipe

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState> = _recipeState

    init {
        Log.i("!!!", "Создан объект RecipeViewModel")
        _recipeState.value = RecipeState(isFavorites = true)
    }

    data class RecipeState(
        var recipe: Recipe? = null,
        var servings: Int = MIN_RECIPE_SERVINGS,
        var isFavorites: Boolean = false,
        var imageUrl: String? = null,
    )

    fun loadRecipe(recipeId: Int){
//        TODO("load from network")
        _recipeState.value?.recipe?.id = recipeId
        _recipeState.value?.recipe?.
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = getApplication<Application>().getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        return HashSet(
            sharedPrefs?.getStringSet(FAVORITE_PREFS_KEY, HashSet<String>()) ?: mutableSetOf()
        )
    }
}