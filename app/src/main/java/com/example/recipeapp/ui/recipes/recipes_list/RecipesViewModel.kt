package com.example.recipeapp.ui.recipes.recipes_list

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.IMAGE_BASE_URL
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import java.io.InputStream

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private var _recipesState = MutableLiveData(RecipesState())
    val recipesState: LiveData<RecipesState> = _recipesState
    private val recipesRepository = RecipesRepository()

    data class RecipesState(
        val recipesList: List<Recipe> = emptyList(),
        val categoryName: String? = null,
        val titleImageUrl: String? = null,
    )

    fun loadRecipesList(categoryId: Int) {
        val category: Category? = recipesRepository.getCategoryByCategoryId(categoryId)
        _recipesState.value = recipesRepository.getRecipesListByCategoryId(categoryId)?.let {
            RecipesState(
                recipesList = it,
                categoryName = category?.title,
                titleImageUrl = IMAGE_BASE_URL + category?.imageUrl,
            )
        }
    }
}