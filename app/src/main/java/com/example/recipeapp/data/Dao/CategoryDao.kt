package com.example.recipeapp.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapp.model.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAllCategories(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategories(categories: List<Category>)
}