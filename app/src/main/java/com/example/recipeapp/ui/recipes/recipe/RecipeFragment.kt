package com.example.recipeapp.ui.recipes.recipe

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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.example.recipeapp.data.ARG_RECIPE
import com.example.recipeapp.data.FAVORITES_PREFS_NAME
import com.example.recipeapp.data.FAVORITE_PREFS_KEY
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.ui.ItemDecoration
import java.io.InputStream

class RecipeFragment : Fragment() {

    private val binding: FragmentRecipeBinding by lazy {
        FragmentRecipeBinding.inflate(layoutInflater)
    }

    private val recipeViewModel: RecipeViewModel by activityViewModels()

    private var recipe: Recipe? = null
    private val theme: Theme? = view?.context?.theme
    private var favorites: MutableSet<String> = getFavorites()

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

        val observer = Observer<RecipeViewModel.RecipeState> {
            Log.println(Log.INFO, "!!!", recipeViewModel.recipeState.value?.isFavorites.toString())
        }
        recipeViewModel.recipeState.observe(viewLifecycleOwner, observer)

    }

    private fun initUi() {
        favorites = getFavorites()
        recipe?.let {
            binding.tvTitleRecipe.apply {
                text = recipe?.title
                contentDescription =
                    it.title + " " + view?.context?.getString(R.string.recipe_title_image)
            }
            loadImageFromAssets(it.imageUrl)
            updateFavoriteIcon()
        }
    }

    private fun updateFavoriteIcon() {
        favorites = getFavorites()
        val recipeId = recipe?.id.toString()
        if (favorites.contains(recipeId)) {
            binding.ibHeart.setImageResource(R.drawable.ic_heart)
        } else binding.ibHeart.setImageResource(R.drawable.ic_heart_empty)

        binding.ibHeart.setOnClickListener {
            if (!favorites.contains(recipeId)) {
                binding.ibHeart.apply {
                    setImageResource(R.drawable.ic_heart)
                    contentDescription = "Button to remove from favorites"
                    favorites.add(recipeId)
                }
            } else {
                binding.ibHeart.apply {
                    setImageResource(R.drawable.ic_heart_empty)
                    contentDescription = "Button to add in favorites"
                    favorites.remove(recipeId)
                }
            }
            saveFavorites(favorites)
        }
    }

    private fun saveFavorites(idList: Set<String>) {
        val sharedPrefs = activity?.getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPrefs?.edit()) {
            this?.putStringSet(FAVORITE_PREFS_KEY, idList)
            this?.apply()
        }
    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = activity?.getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        return HashSet(
            sharedPrefs?.getStringSet(FAVORITE_PREFS_KEY, HashSet<String>()) ?: mutableSetOf()
        )
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