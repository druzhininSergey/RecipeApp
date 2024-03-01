package com.example.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemCategoryBinding

class CategoriesListAdapter(private val dataSet: Array<String>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ivCategory = viewHolder.binding.ivCategory
        val tvCategory = viewHolder.binding.tvCategories
        val tvCategoryDescription = viewHolder.binding.tvCategoriesDescription
    }

    override fun getItemCount() = dataSet.size

}
