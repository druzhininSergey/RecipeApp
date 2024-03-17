package com.example.recipeapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemRecipesBinding
import java.io.InputStream
import java.lang.Exception

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

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
        viewHolder.ivRecipe.contentDescription = R.string.recipe_image.toString() + "" + recipe.title
        try {
            val inputStream: InputStream? =
                viewHolder.itemView.context?.assets?.open(recipe.imageUrl)
            val drawable = Drawable.createFromStream(inputStream, null)
            viewHolder.ivRecipe.setImageDrawable(drawable)
        } catch (e: Exception) {
            Log.e("assets", e.stackTraceToString())
        }
        viewHolder.itemView.setOnClickListener { itemClickListener?.onItemClick(recipe.id) }
    }

    override fun getItemCount() = dataSet.size
}