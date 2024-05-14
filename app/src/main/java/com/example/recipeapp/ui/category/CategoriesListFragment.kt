package com.example.recipeapp.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.recipeapp.R
import com.example.recipeapp.RecipesApplication
import com.example.recipeapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    private val binding: FragmentListCategoriesBinding by lazy {
        FragmentListCategoriesBinding.inflate(layoutInflater)
    }
    private lateinit var categoriesViewModel: CategoriesViewModel
    private val categoriesListAdapter = CategoriesListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        categoriesViewModel = appContainer.categoriesViewModelFactory.create()
    }

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
            if (state.isError) Toast.makeText(
                context,
                "Ошибка получения данных",
                Toast.LENGTH_SHORT
            ).show()
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