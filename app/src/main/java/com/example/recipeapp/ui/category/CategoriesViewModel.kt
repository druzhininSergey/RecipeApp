package com.example.recipeapp.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.Category

class CategoriesViewModel(application: Application) : AndroidViewModel(application) {

    private var _categoriesState = MutableLiveData<CategoriesState>()
    val categoriesState: LiveData<CategoriesState> = _categoriesState

    data class CategoriesState(
        val categories: List<Category>? = null,
    )

    fun loadCategories() {
        val categories = STUB.getCategories()
        _categoriesState.value = CategoriesState(categories)
    }
}