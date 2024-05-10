package com.example.recipeapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipeapp.model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}