package com.example.dependencyinjection.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dependencyinjection.databinding.MealItemBinding
import com.example.dependencyinjection.pojo.MealList
import com.example.dependencyinjection.pojo.MealsByCategory

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>() {

    private var mealsList = ArrayList<MealsByCategory>()

    fun setMealsList(meals: List<MealsByCategory>){
        this.mealsList = meals as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(
            MealItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return this.mealsList.size
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealsList[position].strMeal
    }
    inner class CategoryMealsViewModel(val binding: MealItemBinding ) : RecyclerView.ViewHolder(binding.root)


}