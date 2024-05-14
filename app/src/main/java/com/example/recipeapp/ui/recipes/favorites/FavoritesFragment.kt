package com.example.recipeapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.ui.recipes.recipes_list.RecipesListAdapter
import com.example.recipeapp.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val binging: FragmentFavoritesBinding by lazy {
        FragmentFavoritesBinding.inflate(layoutInflater)
    }
    private val favoritesViewModel: FavoritesViewModel by viewModels()
    private val favoritesAdapter = RecipesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binging.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritesViewModel.loadFavorites()
        initRecycler()
    }

    private fun initRecycler() {
        binging.rvFavorites.adapter = favoritesAdapter
        favoritesViewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            if (state.isError) Toast.makeText(
                context,
                "Ошибка получения данных",
                Toast.LENGTH_SHORT
            ).show()
            favoritesAdapter.dataSet = state.favoritesList
            binging.tvNoFavorites.isVisible = state.favoritesList.isEmpty()
            binging.rvFavorites.isVisible = state.favoritesList.isNotEmpty()
        }
        favoritesAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)
        )
    }
}