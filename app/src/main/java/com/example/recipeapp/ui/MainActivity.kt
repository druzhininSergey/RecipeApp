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

        threadPool.execute {
            val urlCategory = URL("https://recipes.androidsprint.ru/api/category")
            val connection = urlCategory.openConnection() as HttpURLConnection
            connection.connect()
            val response = connection.inputStream.bufferedReader().readText()

            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            categories = json.decodeFromString(response)
            categoriesIds = categories.map { it.id }

            val urlRecipesList =
                URL("https://recipes.androidsprint.ru/api/recipes?ids=${categoriesIds.joinToString(",")}")
            val connectionRecipes = urlRecipesList.openConnection() as HttpURLConnection
            val responseRecipes = connectionRecipes.inputStream.bufferedReader().readText()
            recipesList = json.decodeFromString(responseRecipes)

            Log.i("!!!", "Список рецептов: $recipesList")
        }
    }
}