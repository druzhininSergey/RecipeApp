package com.example.recipeapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.model.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

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
        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val response = connection.inputStream.bufferedReader().readText()

            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            categories = json.decodeFromString(response)
        }
        thread.start()
    }
}


