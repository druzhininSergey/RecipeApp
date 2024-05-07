package com.example.recipeapp.data

import android.util.Log
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class RecipesRepository() {
    private val contentType = "application/json".toMediaType()
    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)


    suspend fun getRecipeByRecipeId(recipeId: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                val recipeCall = recipeApiService.getRecipeByRecipeId(recipeId)
                val recipeResponse = recipeCall.execute()
                recipeResponse.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByIdsList(ids: String): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val recipesCall = recipeApiService.getRecipesByIdsList(ids)
                val recipesResponse = recipesCall.execute()
                recipesResponse.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategoryByCategoryId(categoryId: Int): Category? {
        return withContext(Dispatchers.IO) {
            try {
                val categoryCall = recipeApiService.getCategoryByCategoryId(categoryId)
                val categoryResponse = categoryCall.execute()
                val category = categoryResponse.body()
                category
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesListByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val recipesCall = recipeApiService.getRecipesListByCategoryId(categoryId)
                val recipesResponse = recipesCall.execute()
                val recipes = recipesResponse.body()
                recipes
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                val categoriesCall: Call<List<Category>> = recipeApiService.getCategories()
                val categoriesResponse: Response<List<Category>> = categoriesCall.execute()
                val categories: List<Category>? = categoriesResponse.body()
                categories
            } catch (e: Exception) {
                null
            }
        }
    }
}

