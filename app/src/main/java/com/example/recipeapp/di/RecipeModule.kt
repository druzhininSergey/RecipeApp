package com.example.recipeapp.di

import android.content.Context
import androidx.room.Room
import com.example.recipeapp.data.AppDatabase
import com.example.recipeapp.data.BASE_URL
import com.example.recipeapp.data.Dao.CategoryDao
import com.example.recipeapp.data.Dao.RecipesListDao
import com.example.recipeapp.data.RecipeApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database-category"
        ).build()

    @Provides
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao = appDatabase.categoryDao()

    @Provides
    fun provideRecipeListDao(appDatabase: AppDatabase): RecipesListDao = appDatabase.recipeListDao()

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
        return retrofit
    }

    @Provides
    fun provideRecipeIpiServise(retrofit: Retrofit): RecipeApiService =
        retrofit.create(RecipeApiService::class.java)

}