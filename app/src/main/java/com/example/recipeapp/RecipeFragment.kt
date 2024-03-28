package com.example.recipeapp

import android.content.Context
import android.content.res.Resources.Theme
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
    private val theme: Theme? = view?.context?.theme
    private var isFavorite = false
    private val favorites = getFavorites()

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
            } else it.getParcelable(ARG_RECIPE)
        }
        initUi()
        initRecycler()


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
        uploadFavoriteIcon()
    }

    private fun uploadFavoriteIcon() {
        isFavorite = favorites.contains(recipe?.id.toString())
        binding.ibHeart.setOnClickListener {
            isFavorite = if (!isFavorite) {
                binding.ibHeart.setImageResource(R.drawable.ic_heart)
                recipe?.id?.let { favorites.add(it.toString()) }
                true
            } else {
                binding.ibHeart.setImageResource(R.drawable.ic_heart_empty_40)
                favorites.remove(recipe?.id.toString())
                false
            }
            saveFavorites(favorites)
        }
    }

    private fun saveFavorites(idList: Set<String>) {
        val sharedPrefs = activity?.getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPrefs?.edit()) {
            this?.putStringSet(FAVORITE_PREFS_KEY, idList.toSet())
            this?.apply()
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = activity?.getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        val savedFavorites = sharedPrefs?.getStringSet(FAVORITE_PREFS_KEY, emptySet()) ?: mutableSetOf()
        return HashSet(savedFavorites)
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