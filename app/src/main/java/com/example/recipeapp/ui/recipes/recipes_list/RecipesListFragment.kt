package com.example.recipeapp.ui.recipes.recipes_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentListRecipesBinding
import com.example.recipeapp.data.ARG_CATEGORY_ID
import com.example.recipeapp.data.ARG_RECIPE_ID
import com.example.recipeapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

    private val binding: FragmentListRecipesBinding by lazy {
        FragmentListRecipesBinding.inflate(layoutInflater)
    }
    private val recipesViewModel: RecipesViewModel by activityViewModels()
    private val recipesListAdapter = RecipesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            recipesViewModel.loadRecipesList(it.getInt(ARG_CATEGORY_ID))
        }
        recipesViewModel.recipesState.observe(viewLifecycleOwner) { state ->
            initUI(state)
            initRecycler(state)
        }
    }

    private fun initUI(state: RecipesViewModel.RecipesState) {
        binding.rvRecipes.adapter = recipesListAdapter
        binding.ivRecipesTitle.setImageDrawable(state.titleImage)
        binding.tvRecipesTitle.apply {
            text = state.categoryName
            contentDescription =
                view?.context?.getString(R.string.recipes_title_image) + " " + state.categoryName
        }
    }

    private fun initRecycler(state: RecipesViewModel.RecipesState) {
        recipesListAdapter.dataSet = state.recipesList
        recipesListAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(ARG_RECIPE_ID to recipeId)
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
        }
    }
}