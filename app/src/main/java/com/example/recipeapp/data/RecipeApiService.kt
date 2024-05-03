package com.example.recipeapp.data

import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("recipe/{recipeId}")
    fun getRecipeByRecipeId(@Path("recipeId") recipeId: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIdsList(@Query("ids") ids: String): Call<List<Recipe>>

    @GET("category/{categoryId}")
    fun getCategoryByCategoryId(@Path("categoryId") categoryId: Int): Call<Category>

    @GET("category/{categoryId}/recipes")
    fun getRecipesListByCategoryId(@Path("categoryId") categoryId: Int): Call<List<Recipe>>

    @GET("category")
    fun getCategories(): Call<List<Category>>

}
