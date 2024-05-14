package com.example.recipeapp

import android.app.Application
import com.example.recipeapp.di.AppContainer
import com.example.recipeapp.di.SharedPreferencesManager

class RecipesApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        appContainer = AppContainer(this)
    }
}