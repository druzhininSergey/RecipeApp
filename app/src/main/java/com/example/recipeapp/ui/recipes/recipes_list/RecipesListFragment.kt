package com.example.recipeapp.ui.recipes.recipes_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.RecipesApplication
import com.example.recipeapp.databinding.FragmentListRecipesBinding

class RecipesListFragment : Fragment(R.layout.fragment_list_recipes) {

    private val binding: FragmentListRecipesBinding by lazy {
        FragmentListRecipesBinding.inflate(layoutInflater)
    }
    private lateinit var recipesListViewModel: RecipesListViewModel
    private val recipesListAdapter = RecipesListAdapter()
    private val recipesListFragmentArgs: RecipesListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        recipesListViewModel = appContainer.recipesListViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipesListViewModel.loadRecipesList(recipesListFragmentArgs.categoryId)
        recipesListViewModel.recipesState.observe(viewLifecycleOwner) { state ->
            initUI(state)
            initRecycler(state)
        }
    }

    private fun initUI(state: RecipesListViewModel.RecipesState) {
        binding.rvRecipes.adapter = recipesListAdapter
        Glide.with(this).load(state.titleImageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivRecipesTitle)
        binding.tvRecipesTitle.apply {
            text = state.categoryName
            contentDescription =
                view?.context?.getString(R.string.recipes_title_image) + " " + state.categoryName
        }
    }

    private fun initRecycler(state: RecipesListViewModel.RecipesState) {
        if (state.isError) Toast.makeText(context, "Ошибка получения данных", Toast.LENGTH_SHORT)
            .show()
        recipesListAdapter.dataSet = state.recipesList
        recipesListAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId)
        )
    }
}