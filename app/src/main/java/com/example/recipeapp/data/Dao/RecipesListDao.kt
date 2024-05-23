package com.example.recipeapp.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipeapp.model.Recipe

@Dao
interface RecipesListDao {

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): Recipe

    @Query("SELECT * FROM recipe WHERE is_favorite = 1")
    fun getFavoriteRecipes(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE category_id = :categoryId")
    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipes(recipeList: List<Recipe>)
}