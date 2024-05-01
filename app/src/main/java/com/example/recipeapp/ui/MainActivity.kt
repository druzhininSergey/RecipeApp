package com.example.recipeapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val threadPool = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }
        binding.btnFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val json = Json { ignoreUnknownKeys = true }
        var categories: List<Category>
        var categoriesIds: List<Int>
        var recipesList: List<Recipe>

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        threadPool.execute {
            val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val requestCategory: Request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()

            client.newCall(requestCategory).execute().use { response ->
                val responseBody = response.body?.string()
                categories = if (responseBody != null) json.decodeFromString(responseBody)
                else emptyList()

                categoriesIds = categories.map { it.id }
            }

            val requestRecipesList: Request = Request.Builder()
                .url(
                    "https://recipes.androidsprint.ru/api/recipes?ids=${
                        categoriesIds.joinToString(",")
                    }"
                )
                .build()

            client.newCall(requestRecipesList).execute().use { response ->
                val responseBody = response.body?.string()
                recipesList = if(responseBody != null) json.decodeFromString(responseBody)
                    else emptyList()
                Log.i("!!!", "Список рецептов: $recipesList")
            }
        }
    }
}