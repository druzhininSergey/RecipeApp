package com.example.recipeapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipeapp.data.AppDatabase
import com.example.recipeapp.data.BASE_URL
import com.example.recipeapp.data.RecipeApiService
import com.example.recipeapp.data.RecipesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(context: Context) {

    private val bd = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database-category"
    ).build()

    private val categoryDao = bd.categoryDao()
    private val recipeListDao = bd.recipeListDao()
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client: OkHttpClient =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    val repository = RecipesRepository(
        recipeListDao = recipeListDao,
        categoryDao = categoryDao,
        recipeApiService = recipeApiService,
        ioDispatcher = ioDispatcher,
    )

    val categoriesViewModelFactory = CategoriesViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
    val recipesListViewModelFactory = RecipesListViewModelFactory(repository)
}