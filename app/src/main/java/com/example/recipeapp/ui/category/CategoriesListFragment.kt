package com.example.recipeapp.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.data.RecipesRepository
import com.example.recipeapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    private val binding: FragmentListCategoriesBinding by lazy {
        FragmentListCategoriesBinding.inflate(layoutInflater)
    }
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private val categoriesListAdapter = CategoriesListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesViewModel.loadCategories()
        initRecycler()
    }

    private fun initRecycler() {
        binding.rvCategories.adapter = categoriesListAdapter
        categoriesViewModel.categoriesState.observe(viewLifecycleOwner) { state ->
            categoriesListAdapter.dataSet = state.categories
        }

        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                findNavController().navigate(
                    CategoriesListFragmentDirections
                        .actionCategoriesListFragmentToRecipesListFragment(categoryId)
                )
            }
        })
    }
}