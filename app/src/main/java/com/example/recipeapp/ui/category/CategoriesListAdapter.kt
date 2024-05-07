package com.example.recipeapp.ui.category

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.model.Category
import com.example.recipeapp.R
import com.example.recipeapp.data.BASE_URL
import com.example.recipeapp.data.IMAGE_BASE_URL
import com.example.recipeapp.databinding.ItemCategoryBinding
import java.io.InputStream
import java.lang.Exception

class CategoriesListAdapter(var dataSet: List<Category> = emptyList()) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemCategoryBinding.bind(itemView)
        val ivCategory = binding.ivCategory
        val tvCategory = binding.tvCategory
        val tvCategoryDescription = binding.tvCategoriesDescription
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_category, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category = dataSet[position]
        viewHolder.tvCategory.text = category.title
        viewHolder.ivCategory.contentDescription =
            viewHolder.itemView.context.getString(R.string.category_image) + " " + category.title
        viewHolder.tvCategoryDescription.text = category.description
        Glide.with(viewHolder.itemView.context)
            .load(IMAGE_BASE_URL + category.imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(viewHolder.ivCategory)
        viewHolder.itemView.setOnClickListener { itemClickListener?.onItemClick(category.id) }
    }

    override fun getItemCount() = dataSet.size
}