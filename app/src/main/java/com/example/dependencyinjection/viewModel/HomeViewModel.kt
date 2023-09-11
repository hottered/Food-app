package com.example.dependencyinjection.viewModel

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
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popoularMealsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()

    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()

    fun getRandomMeal() {
        RetrofitInstance
            .api
            .getRandomMeal()
            .enqueue(object : retrofit2.Callback<MealList> {
                override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                    if (response.body() != null) {
                        val randomMeal: Meal = response.body()!!.meals[0]
                        Log.d(
                            "HomeFragment",
                            "meal id ${randomMeal.idMeal} name ${randomMeal.strMeal}"
                        )
                        randomMealLiveData.value = randomMeal
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
                    }else
                    {
                        return
                    }
                }

                override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }

            })
    }
    fun deleteMealFromDatabase(meal: Meal){
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
    fun observeRandomMealLivedata(): LiveData<Meal> {
        return randomMealLiveData
    }

    fun observePopularItemsLivedata(): LiveData<List<MealsByCategory>> {
        return popoularMealsLiveData
    }
    fun observeCategoriesLivedata() : LiveData<List<Category>>  {
        return categoriesLiveData
    }
    fun observeFavoritesMealsLiveData() : LiveData<List<Meal>> {
        return favoritesMealsLiveData
    }
}