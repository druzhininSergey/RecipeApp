package com.example.recipeapp.data

import com.example.recipeapp.data.Dao.CategoryDao
import com.example.recipeapp.data.Dao.RecipesListDao
import com.example.recipeapp.model.Category
import com.example.recipeapp.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RecipesRepository @Inject constructor(
    private val recipeListDao: RecipesListDao,
    private val categoryDao: CategoryDao,
    private val recipeApiService: RecipeApiService,
) {
    private val ioDispatcher: CoroutineContext = Dispatchers.IO

    suspend fun getCategoriesFromCache() =
        withContext(ioDispatcher) { categoryDao.getAllCategories() }

    suspend fun addCategoriesToCache(categories: List<Category>) =
        withContext(ioDispatcher) { categoryDao.addCategories(categories) }

    suspend fun getRecipesByCategoryIdFromCache(categoryId: Int) =
        withContext(ioDispatcher) { recipeListDao.getRecipesByCategoryId(categoryId) }

    suspend fun addRecipeListToCache(recipeList: List<Recipe>) =
        withContext(ioDispatcher) { recipeListDao.addRecipes(recipeList) }

    suspend fun getRecipeByRecipeIdFromCache(recipeId: Int) =
        withContext(ioDispatcher) { recipeListDao.getRecipeById(recipeId) }

    suspend fun getFavoriteRecipesFromCache() =
        withContext(ioDispatcher) { recipeListDao.getFavoriteRecipes() }

    suspend fun updateRecipeInCache(recipe: Recipe) =
        withContext(ioDispatcher) { recipeListDao.updateRecipe(recipe) }

    suspend fun getRecipeByRecipeId(recipeId: Int): Recipe? {
        return withContext(ioDispatcher) {
            try {
                val recipeCall = recipeApiService.getRecipeByRecipeId(recipeId)
                val recipeResponse = recipeCall.execute()
                recipeResponse.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesByIdsList(ids: String): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                val recipesCall = recipeApiService.getRecipesByIdsList(ids)
                val recipesResponse = recipesCall.execute()
                recipesResponse.body()
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategoryByCategoryId(categoryId: Int): Category? {
        return withContext(ioDispatcher) {
            try {
                val categoryCall = recipeApiService.getCategoryByCategoryId(categoryId)
                val categoryResponse = categoryCall.execute()
                val category = categoryResponse.body()
                category
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getRecipesListByCategoryId(categoryId: Int): List<Recipe>? {
        return withContext(ioDispatcher) {
            try {
                val recipesCall = recipeApiService.getRecipesListByCategoryId(categoryId)
                val recipesResponse = recipesCall.execute()
                val recipes = recipesResponse.body()
                recipes
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getCategories(): List<Category>? {
        return withContext(ioDispatcher) {
            try {
                val categoriesCall: Call<List<Category>> = recipeApiService.getCategories()
                val categoriesResponse: Response<List<Category>> = categoriesCall.execute()
                val categories: List<Category>? = categoriesResponse.body()
                categories
            } catch (e: Exception) {
                null
            }
        }
    }
}