package com.example.recipeapp.data

import android.app.Application
import androidx.room.Room
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

class RecipesRepository(application: Application) {
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
    private val bd = Room.databaseBuilder(
        application.applicationContext,
        AppDatabase::class.java,
        "database-category"
    ).build()
    private val categoryDao = bd.categoryDao()
    private val recipeListDao = bd.recipeListDao()

    suspend fun getCategoriesFromCache() = withContext(Dispatchers.IO) {
        categoryDao.getAllCategories()
    }

    suspend fun addCategoriesToCache(categories: List<Category>) =
        withContext(Dispatchers.IO) { categoryDao.addCategories(categories) }

    suspend fun getRecipeListFromCache() =
        withContext(Dispatchers.IO) { recipeListDao.getAllRecipes() }

    suspend fun addRecipeListToCache(recipeList: List<Recipe>) =
        withContext(Dispatchers.IO) { recipeListDao.replaceAllRecipes(recipeList) }

    suspend fun getRecipeByRecipeIdFromCache(recipeId: Int) =
        withContext(Dispatchers.IO) { recipeListDao.getRecipeById(recipeId) }

    suspend fun getFavoriteRecipesFromCache() =
        withContext(Dispatchers.IO) { recipeListDao.getFavoriteRecipes() }

    suspend fun updateRecipeInCache(recipe: Recipe) = withContext(Dispatchers.IO) {
        recipeListDao.updateRecipe(recipe.copy(isFavorite = !recipe.isFavorite))
    }

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