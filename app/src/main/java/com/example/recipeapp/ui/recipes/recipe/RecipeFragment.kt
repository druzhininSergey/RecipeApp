package com.example.recipeapp.ui.recipes.recipe

import android.content.Context
import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.example.recipeapp.data.ARG_RECIPE_ID
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
            recipeViewModel.loadRecipe(it.getInt(ARG_RECIPE_ID))
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
        recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
            state.recipe?.let {
                binding.tvTitleRecipe.apply {
                    text = state.recipe?.title
                    contentDescription =
                        state.recipe?.title + " " + view?.context?.getString(R.string.recipe_title_image)
                }
            }
            loadImageFromAssets(state.imageUrl)
            updateFavoriteIcon()
            Log.i("!!!", state.isFavorites.toString())
        }
    }

    private fun updateFavoriteIcon() {
        recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
            state.recipe.let {
                this.recipe = it
                favorites = getFavorites()
                val recipeId = recipe?.id.toString()
                if (favorites.contains(recipeId)) {
                    binding.ibHeart.setImageResource(R.drawable.ic_heart)
                } else binding.ibHeart.setImageResource(R.drawable.ic_heart_empty)

                binding.ibHeart.setOnClickListener {
                    recipeViewModel.onFavoritesClicked(binding)
                }
            }
        }

    }

    private fun getFavorites(): MutableSet<String> {
        val sharedPrefs = activity?.getSharedPreferences(FAVORITES_PREFS_NAME, Context.MODE_PRIVATE)
        return HashSet(
            sharedPrefs?.getStringSet(FAVORITE_PREFS_KEY, HashSet<String>()) ?: mutableSetOf()
        )
    }

    private fun initRecycler() {
        recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
            val recipe = state.recipe
            val adapterIngredient = recipe?.let { IngredientsAdapter(it.ingredients) }
            binding.rvIngredients.adapter = adapterIngredient

            val adapterMethod = recipe?.let { MethodAdapter(it.method) }
            binding.rvMethod.adapter = adapterMethod

            binding.sbPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    adapterIngredient?.updateIngredients(progress)
                    adapterIngredient?.notifyDataSetChanged()
                    binding.tvNumberOfPortions.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun loadImageFromAssets(imageUrl: String?) {
        try {
            val inputStream: InputStream? = imageUrl?.let { view?.context?.assets?.open(it) }
            val drawable = Drawable.createFromStream(inputStream, null)
            binding.ivTitleRecipe.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
    }
}