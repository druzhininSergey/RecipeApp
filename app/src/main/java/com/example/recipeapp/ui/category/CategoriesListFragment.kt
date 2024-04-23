package com.example.recipeapp.ui.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipeapp.data.ARG_CATEGORY_ID
import com.example.recipeapp.data.ARG_CATEGORY_IMAGE_URL
import com.example.recipeapp.data.ARG_CATEGORY_NAME
import com.example.recipeapp.R
import com.example.recipeapp.ui.recipes.recipes_list.RecipesListFragment
import com.example.recipeapp.databinding.FragmentListCategoriesBinding
import com.example.recipeapp.data.STUB

class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    private val binding: FragmentListCategoriesBinding by lazy {
        FragmentListCategoriesBinding.inflate(layoutInflater)
    }
    private val categoriesViewModel: CategoriesViewModel by activityViewModels()
    private val categoriesListAdapter = CategoriesListAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments.let {
            categoriesViewModel.loadCategories()
        }
        initRecycler()
    }

    private fun initRecycler() {
        binding.rvCategories.adapter = categoriesListAdapter
        categoriesViewModel.categoriesState.observe(viewLifecycleOwner) { categories ->
            categories.categories?.let {
                categoriesListAdapter.dataSet = it
            }
        }
       categoriesListAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    fun openRecipesByCategoryId(categoryId: Int) {
        val category = STUB.getCategories()[categoryId]
        val categoryName = category.title
        val categoryImageUrl = category.imageUrl

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl
        )
        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
            setReorderingAllowed(true)
        }
    }
}