package com.example.recipeapp.ui.recipes.recipes_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import java.io.InputStream

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipesState = MutableLiveData<RecipesState>()
    val recipesState: LiveData<RecipesState> = _recipesState
    val recipesRepository = RecipesRepository()

    data class RecipesState(
        val recipesList: List<Recipe> = emptyList(),
        val categoryName: String? = null,
        val titleImage: Drawable? = null,
    )

    fun loadRecipesList(categoryId: Int) {
        var titleImage: Drawable? = null
        val category : Category = STUB.getCategoryByCategoryId(categoryId)
        try {
            val inputStream: InputStream? = getApplication<Application>().assets?.open(
                category.imageUrl
            )
            titleImage = Drawable.createFromStream(inputStream, null)
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
        _recipesState.value = RecipesState(
            recipesList = STUB.getRecipesByCategoryId(categoryId),
            categoryName = category.title,
            titleImage = titleImage,
        )
    }
}