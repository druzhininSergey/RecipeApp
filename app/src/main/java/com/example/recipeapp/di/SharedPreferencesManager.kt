package com.example.recipeapp.di

import android.content.Context
import android.content.SharedPreferences
import com.example.recipeapp.data.FAVORITES_PREFS_NAME
import com.example.recipeapp.data.FAVORITE_PREFS_KEY

object SharedPreferencesManager {
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            FAVORITES_PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    fun getFavorites(): MutableSet<String> {
        return HashSet(
            sharedPreferences?.getStringSet(FAVORITE_PREFS_KEY, HashSet<String>()) ?: mutableSetOf()
        )
    }

    fun saveFavorites(idList: Set<String>) {
        with(sharedPreferences?.edit()) {
            this?.putStringSet(com.example.recipeapp.data.FAVORITE_PREFS_KEY, idList)
            this?.apply()
        }
    }
}