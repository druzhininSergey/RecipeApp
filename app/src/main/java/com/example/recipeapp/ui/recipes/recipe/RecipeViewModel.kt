package com.example.recipeapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.data.MIN_RECIPE_SERVINGS
import com.example.recipeapp.model.Recipe

class RecipeViewModel : ViewModel() {

    private var _recipeState = MutableLiveData<RecipeState>()
    val recipeState: LiveData<RecipeState> get() = _recipeState

    init {
        Log.i("!!!", "Создан объект RecipeViewModel")
        _recipeState.value = RecipeState(isFavorites = true)
    }

    data class RecipeState(
        var recipe: Recipe? = null,
        var servings: Int = MIN_RECIPE_SERVINGS,
        var isFavorites: Boolean = false,
    )

}