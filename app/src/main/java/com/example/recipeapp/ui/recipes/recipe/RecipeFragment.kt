package com.example.recipeapp.ui.recipes.recipe

import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.example.recipeapp.ui.ItemDecoration

class RecipeFragment : Fragment() {

    private val binding: FragmentRecipeBinding by lazy {
        FragmentRecipeBinding.inflate(layoutInflater)
    }
    private val recipeViewModel: RecipeViewModel by activityViewModels()
    private val theme: Theme? = view?.context?.theme
    private val ingredientsAdapter = IngredientsAdapter()
    private val methodAdapter = MethodAdapter()
    private val recipeFragmentArgs: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recipeId = recipeFragmentArgs.recipeId
        recipeViewModel.loadRecipe(recipeId)
        initUi()
    }

    private fun initUi() {
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
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = methodAdapter

        recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
            state.recipe?.let {
                binding.tvTitleRecipe.apply {
                    text = it.title
                    contentDescription =
                        it.title + " " + view?.context?.getString(R.string.recipe_title_image)
                }
                Glide.with(this).load(state.recipeImageUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_error)
                    .into(binding.ivTitleRecipe)
            }
            updateFavoriteIcon(state.isFavorite)
            initRecycler(state)
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {

        if (isFavorite) binding.ibHeart.setImageResource(R.drawable.ic_heart)
        else binding.ibHeart.setImageResource(R.drawable.ic_heart_empty)

        binding.ibHeart.setOnClickListener {
            recipeViewModel.onFavoritesClicked()
            if (!isFavorite) {
                binding.ibHeart.apply {
                    setImageResource(R.drawable.ic_heart)
                    contentDescription =
                        view?.context?.getString(R.string.remove_from_favorites_btn)
                }
            } else {
                binding.ibHeart.apply {
                    setImageResource(R.drawable.ic_heart_empty)
                    contentDescription =
                        view?.context?.getString(R.string.add_in_favorites_btn)
                }
            }
        }
    }

    private fun initRecycler(state: RecipeViewModel.RecipeState) {
        if (state.isError) Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
        state.recipe?.let { recipe ->
            ingredientsAdapter.dataSet = recipe.ingredients
            methodAdapter.dataSet = recipe.method
            methodAdapter.notifyDataSetChanged()
        }
        ingredientsAdapter.updateIngredients(state.servings)
        ingredientsAdapter.notifyDataSetChanged()

        val portionSeekBarListener = PortionSeekBarListener { progress ->
            recipeViewModel.onChangeServings(progress)
            binding.tvNumberOfPortions.text = progress.toString()
        }
        binding.sbPortions.setOnSeekBarChangeListener(portionSeekBarListener)
    }

    class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            onChangeIngredients(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}
        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    }
}