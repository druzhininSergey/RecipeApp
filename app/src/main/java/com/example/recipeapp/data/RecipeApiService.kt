package com.example.recipeapp.data

import com.example.recipeapp.model.Category
import retrofit2.Call
import retrofit2.http.GET

interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>
}