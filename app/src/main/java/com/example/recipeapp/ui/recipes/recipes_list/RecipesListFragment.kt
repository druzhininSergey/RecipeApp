package com.example.recipeapp.ui.recipes.recipes_list

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentListRecipesBinding
import com.example.recipeapp.data.STUB
import com.example.recipeapp.model.ARG_CATEGORY_ID
import com.example.recipeapp.model.ARG_CATEGORY_IMAGE_URL
import com.example.recipeapp.model.ARG_CATEGORY_NAME
import com.example.recipeapp.model.ARG_RECIPE
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment
import java.io.InputStream

class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

    private val binding: FragmentListRecipesBinding by lazy {
        FragmentListRecipesBinding.inflate(layoutInflater)
    }
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(ARG_CATEGORY_ID)
            categoryName = it.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = it.getString(ARG_CATEGORY_IMAGE_URL)
        }
        loadImageFromAssets()
        binding.tvRecipesTitle.apply {
            text = categoryName
            contentDescription =
                view.context.getString(R.string.recipes_title_image) + " " + categoryName
        }
        initRecycler()
    }

    private fun initRecycler() {
        val adapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId))
        val recyclerView = binding.rvRecipes
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(ARG_RECIPE to recipe)
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
        }
    }

    private fun loadImageFromAssets() {
        try {
            val inputStream: InputStream? = view?.context?.assets?.open(categoryImageUrl.toString())
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.ivRecipesTitle.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
    }
}