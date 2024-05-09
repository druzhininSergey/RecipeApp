package com.example.recipeapp.ui.recipes.recipe

import com.example.recipeapp.databinding.ItemMethodBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R

class MethodAdapter() :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    var dataSet: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemMethodBinding.bind(itemView)
        val method = binding.tvMethod
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_method, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        val methodText = (position + 1).toString() + ". " + ingredient
        viewHolder.method.text = methodText
    }

    override fun getItemCount() = dataSet.size
}