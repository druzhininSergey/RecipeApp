package com.example.recipeapp

import android.app.Application
import com.example.recipeapp.di.SharedPreferencesManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(applicationContext)
    }
}