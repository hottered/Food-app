package com.example.dependencyinjection.viewModel

import android.icu.text.StringSearch
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dependencyinjection.db.MealDatabase
import com.example.dependencyinjection.pojo.Category
import com.example.dependencyinjection.pojo.CategoryList
import com.example.dependencyinjection.pojo.Meal
import com.example.dependencyinjection.pojo.MealList
import com.example.dependencyinjection.pojo.MealsByCategoryList
import com.example.dependencyinjection.pojo.MealsByCategory
import com.example.dependencyinjection.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popoularMealsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()

    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetLiveData = MutableLiveData<Meal>()
    
    private var searchedMealsLiveData = MutableLiveData<List<Meal>>()

    private var saveStateRandomMeal : Meal?=null
    fun getRandomMeal() {
        saveStateRandomMeal?.let { randomMeal->
            randomMealLiveData.postValue(randomMeal)
            return
        }
        RetrofitInstance
            .api
            .getRandomMeal()
            .enqueue(object : retrofit2.Callback<MealList> {
                override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                    if (response.body() != null) {
                        val randomMeal: Meal = response.body()!!.meals[0]
                        randomMealLiveData.value = randomMeal
                        saveStateRandomMeal = randomMeal
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<MealList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }

            })
    }

    fun getPopularItems() {
        
        RetrofitInstance.api
            .getPopularItems("Seafood")
            .enqueue(object : retrofit2.Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        popoularMealsLiveData.value = response.body()!!.meals
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }

            })
    }

    fun getCategories() {
        RetrofitInstance.api
            .getCategories()
            .enqueue(object : retrofit2.Callback<CategoryList> {
                override fun onResponse(
                    call: Call<CategoryList>,
                    response: Response<CategoryList>
                ) {
                    if (response.body() != null) {
                        categoriesLiveData.value = response.body()!!.categories
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }

            })
    }
    
    fun searchMeals(searchQuery: String) = 
        RetrofitInstance
            .api
            .searchMeals(searchQuery)
            .enqueue(object : Callback<MealList>{
        override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
             val mealsList = response.body()?.meals
            mealsList?.let { 
                searchedMealsLiveData.postValue(it)
            }
        }

        override fun onFailure(call: Call<MealList>, t: Throwable) {
            Log.d("HomeFragment", t.message.toString())
        }

    })

    fun observeSearchedMealsLivedata() : LiveData<List<Meal>>{
        return searchedMealsLiveData
    }
    fun deleteMealFromDatabase(meal: Meal) {
        viewModelScope.launch {
//            Log.d("TAG!@#$","Hello from ${Thread.currentThread().name}")
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun insertMealIntoDatabase(meal: Meal) {
        viewModelScope.launch {
//            Log.d("TAG!@#$","Hello from ${Thread.currentThread().name}")
            mealDatabase.mealDao().insertOrUpdateMeal(meal)
        }
    }

    fun getMealById(id: String) {
        RetrofitInstance
            .api
            .getMealDetails(id)
            .enqueue(object : Callback<MealList> {
                override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                    val meal = response.body()?.meals?.first()
                    meal?.let { mealss ->
                        bottomSheetLiveData.postValue(mealss)
                    }

                }

                override fun onFailure(call: Call<MealList>, t: Throwable) {
                    Log.d("HomeVIewModel",t.message.toString())
                }

            })
    }

    fun observeBottomSheetMealLiveData() : LiveData<Meal>{
        return  bottomSheetLiveData
    }
    fun observeRandomMealLivedata(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observePopularItemsLivedata(): LiveData<List<MealsByCategory>> {
        return popoularMealsLiveData
    }

    fun observeCategoriesLivedata(): LiveData<List<Category>> {
        return categoriesLiveData
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }
}