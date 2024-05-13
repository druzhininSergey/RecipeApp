package com.example.recipeapp.data.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.recipeapp.model.Recipe

@Dao
interface RecipesListDao {
    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): Recipe?

    @Transaction
    fun replaceAllRecipes(recipeList: List<Recipe>) {
        deleteAllRecipes()
        addRecipes(recipeList)
    }

    @Query("DELETE FROM recipe")
    fun deleteAllRecipes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipes(recipeList: List<Recipe>)
}