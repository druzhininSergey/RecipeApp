package com.example.recipeapp.data

import android.util.Log
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class RecipesRepository {
    private val recipeApiService: RecipeApiService

    val contentType = "application/json".toMediaType()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()

    init {
        recipeApiService = retrofit.create(RecipeApiService::class.java)
    }

    fun getRecipeByRecipeId(recipeId: Int): Recipe? {
        try {
            val recipeCall = recipeApiService.getRecipeByRecipeId(recipeId)
            val recipeResponse = recipeCall.execute()
            val recipe = recipeResponse.body()
            return recipe
        } catch (e: Exception){
            return null
        }
    }

    fun getRecipesByIdsList(listIds: List<Int>): List<Recipe>? {
        try {
            val recipesCall = recipeApiService.getRecipesByIdsList(listIds)
            val recipesResponse = recipesCall.execute()
            val recipes = recipesResponse.body()
            Log.i("!!!", "Список избранного: $recipes")
            return recipes
        } catch (e: Exception) {
            return null
        }
    }

    fun getCategoryByCategoryId(categoryId: Int): Category? {
        try {
            val categoryCall = recipeApiService.getCategoryByCategoryId(categoryId)
            val categoryResponse = categoryCall.execute()
            val category = categoryResponse.body()
            return category
        } catch (e: Exception) {
            return null
        }
    }

    fun getRecipesListByCategoryId(categoryId: Int): List<Recipe>? {
        try {
            val recipesCall = recipeApiService.getRecipesListByCategoryId(categoryId)
            val recipesResponse = recipesCall.execute()
            val recipes = recipesResponse.body()
            return recipes
        } catch (e: Exception) {
            return null
        }
    }

    fun getCategories(): List<Category>? {
        try {
            val categoriesCall: Call<List<Category>> = recipeApiService.getCategories()
            val categoriesResponse: Response<List<Category>> = categoriesCall.execute()
            val categories: List<Category>? = categoriesResponse.body()
            return categories
        } catch (e: Exception) {
            return null
        }

    }

}

