package com.example.recipeapp.data

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class RecipesRepository() {
    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)
    private val threadPool = Executors.newFixedThreadPool(10)


    fun getRecipeByRecipeId(recipeId: Int): Recipe? {
        val callable = Callable {
            try {
                val recipeCall = recipeApiService.getRecipeByRecipeId(recipeId)
                val recipeResponse = recipeCall.execute()
                recipeResponse.body()
            } catch (e: Exception) {
                null
            }
        }
        val future = threadPool.submit(callable)
        return try {
            future.get()
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesByIdsList(ids: String): List<Recipe>? {
        val callable = Callable {
            try {
                val recipesCall = recipeApiService.getRecipesByIdsList(ids)
                val recipesResponse = recipesCall.execute()
                recipesResponse.body()
            } catch (e: Exception) {
                null
            }
        }
        val future = threadPool.submit(callable)
        return try {
            future.get()
        } catch (e: Exception) {
            null
        }

    }

    fun getCategoryByCategoryId(categoryId: Int): Category? {
        val callable = Callable {
            try {
                val categoryCall = recipeApiService.getCategoryByCategoryId(categoryId)
                val categoryResponse = categoryCall.execute()
                val category = categoryResponse.body()
                category
            } catch (e: Exception) {
                null
            }
        }
        val future = threadPool.submit(callable)
        return try {
            future.get()
        } catch (e: Exception) {
            null
        }
    }

    fun getRecipesListByCategoryId(categoryId: Int): List<Recipe>? {
        val callable = Callable {
            try {
                val recipesCall = recipeApiService.getRecipesListByCategoryId(categoryId)
                val recipesResponse = recipesCall.execute()
                val recipes = recipesResponse.body()
                recipes
            } catch (e: Exception) {
                null
            }
        }
        val future = threadPool.submit(callable)
        return try {
            future.get()
        } catch (e: Exception) {
            null
        }
    }

    fun getCategories(): List<Category>? {
        val callable = Callable {
            try {
                val categoriesCall: Call<List<Category>> = recipeApiService.getCategories()
                val categoriesResponse: Response<List<Category>> = categoriesCall.execute()
                val categories: List<Category>? = categoriesResponse.body()
                Log.i("!!!", "Список избранного: $categories")
                categories
            } catch (e: Exception) {
                null
            }
        }
        val future = threadPool.submit(callable)
        return try {
            future.get()
        } catch (e: Exception) {
            null
        }
    }

}

