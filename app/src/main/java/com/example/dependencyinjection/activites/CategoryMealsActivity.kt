package com.example.dependencyinjection.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dependencyinjection.R
import com.example.dependencyinjection.adapters.CategoryMealsAdapter
import com.example.dependencyinjection.adapters.MostPopularMealsAdapter
import com.example.dependencyinjection.databinding.ActivityCategoryMealsBinding
import com.example.dependencyinjection.databinding.ActivityMealBinding
import com.example.dependencyinjection.fragments.HomeFragment
import com.example.dependencyinjection.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

     lateinit var binding: ActivityCategoryMealsBinding
     lateinit var categoryMealsViewModel: CategoryMealsViewModel
     lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]
        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        Log.d("CategoryFragmet",intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        observeCategoryMeals()
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter  = categoryMealsAdapter
        }
    }

    private fun observeCategoryMeals() {
        categoryMealsViewModel.observeMealsByCategoryLivedata()
            .observe(this, Observer { mealsList ->
                categoryMealsAdapter.setMealsList(mealsList)
                binding.tvCategoryCount.text = mealsList.size.toString()

                mealsList.forEach{
                    Log.d("HHHHH",it.strMeal)
                }
            })
    }
}