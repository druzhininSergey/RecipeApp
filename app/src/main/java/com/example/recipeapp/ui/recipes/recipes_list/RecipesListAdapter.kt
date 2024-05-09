package com.example.recipeapp.ui.recipes.recipes_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.data.IMAGE_BASE_URL
import com.example.recipeapp.databinding.ItemRecipesBinding
import com.example.recipeapp.model.Recipe

class RecipesListAdapter() :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    var dataSet: List<Recipe> = emptyList()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemRecipesBinding.bind(itemView)
        val ivRecipe = binding.ivRecipe
        val tvRecipe = binding.tvRecipe
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_recipes, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe = dataSet[position]
        viewHolder.tvRecipe.text = recipe.title
        viewHolder.ivRecipe.contentDescription =
            viewHolder.itemView.context.getString(R.string.recipe_image) + " " + recipe.title
        Glide.with(viewHolder.itemView.context)
            .load(IMAGE_BASE_URL + recipe.imageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(viewHolder.ivRecipe)
        viewHolder.itemView.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
    }

    override fun getItemCount() = dataSet.size
}