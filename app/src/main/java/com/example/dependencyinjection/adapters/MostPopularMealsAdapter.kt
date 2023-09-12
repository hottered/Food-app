package com.example.dependencyinjection.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dependencyinjection.databinding.PopularItemsBinding
import com.example.dependencyinjection.pojo.MealsByCategory

class MostPopularMealsAdapter() : RecyclerView.Adapter<MostPopularMealsAdapter.PopularMealViewHolder>() {

    lateinit var onItemClick : ((MealsByCategory) -> Unit)
    private var mealsList = ArrayList<MealsByCategory>()

    var onLongItemClick:((MealsByCategory) -> Unit)?=null

    fun setMeals(mealsList : ArrayList<MealsByCategory>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(
            PopularItemsBinding.inflate(
                LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.popularMealItem)

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongItemClick?.invoke(mealsList[position])
            true
        }
    }

    class PopularMealViewHolder(val binding : PopularItemsBinding) : RecyclerView.ViewHolder(binding.root)

}