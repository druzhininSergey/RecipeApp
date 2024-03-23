package com.example.recipeapp

import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipeapp.databinding.FragmentRecipeBinding
import java.io.InputStream

class RecipeFragment : Fragment() {

    private val binding: FragmentRecipeBinding by lazy {
        FragmentRecipeBinding.inflate(layoutInflater)
    }

    private var recipe: Recipe? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(ARG_RECIPE, Recipe::class.java)
            } else it.getParcelable<Recipe>(ARG_RECIPE)
        }
        initUi()
        initRecycler()

        val theme: Theme = view.context.theme
        binding.rvIngredients.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(R.dimen.divider_height),
                resources.getColor(R.color.main_background_color, theme)
            )
        )

        binding.rvMethod.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(R.dimen.divider_height),
                resources.getColor(R.color.main_background_color, theme)
            )
        )

    }

    private fun initUi() {
        recipe?.let {
            binding.tvTitleRecipe.text = it.title
            loadImageFromAssets(it.imageUrl)
        }
    }

    private fun initRecycler() {
        val adapterIngredient = recipe?.let { IngredientsAdapter(it.ingredients) }
        val recyclerViewIngredient = binding.rvIngredients
        recyclerViewIngredient.adapter = adapterIngredient

        val adapterMethod = recipe?.let { MethodAdapter(it.method) }
        val recyclerViewMethod = binding.rvMethod
        recyclerViewMethod.adapter = adapterMethod
    }

    private fun loadImageFromAssets(imageUrl: String) {
        try {
            val inputStream: InputStream? = view?.context?.assets?.open(imageUrl)
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.ivTitleRecipe.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
    }
}