package com.example.recipeapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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

        binding.rvIngredients.addItemDecoration(
            ItemDecoration(binding.rvIngredients.context, R.drawable.divider_line)
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

        binding.sbPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                adapterIngredient?.updateIngredients(progress)
                adapterIngredient?.notifyDataSetChanged()
                binding.tvNumberOfPortions.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
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