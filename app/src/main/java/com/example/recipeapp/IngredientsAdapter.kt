package com.example.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemIngredientBinding
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemIngredientBinding.bind(itemView)
        val ingredientName = binding.tvIngredientName
        val ingredientQuantity = binding.tvIngredientQuantity
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_ingredient, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient = dataSet[position]
        viewHolder.ingredientName.text = ingredient.description
        viewHolder.ingredientQuantity.text = setIngredientQuantityText(ingredient)
    }

    override fun getItemCount() = dataSet.size

    private fun setIngredientQuantityText(ingredient: Ingredient): String {
        val ingredientQuantity = ingredient.quantity.toBigDecimal().times(quantity.toBigDecimal())
        val formattedQuantity = if (ingredientQuantity.remainder(BigDecimal.ONE).compareTo(
                BigDecimal.ZERO) == 0) {
            ingredientQuantity.intValueExact()
        } else {
            ingredientQuantity.setScale(1, RoundingMode.HALF_UP)
        }
        return formattedQuantity.toString() + " " + ingredient.unitOfMeasure
    }

    private var quantity = 1

    fun updateIngredients(progress: Int) {
        quantity = progress
    }
}