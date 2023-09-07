package com.example.dependencyinjection.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dependencyinjection.pojo.Meal
import com.example.dependencyinjection.pojo.MealList
import com.example.dependencyinjection.retrofit.CategoryList
import com.example.dependencyinjection.retrofit.CategoryMeals
import com.example.dependencyinjection.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Response

class HomeViewModel() : ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popoularMealsLiveData = MutableLiveData<List<CategoryMeals>>()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object : retrofit2.Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal: Meal = response.body()!!.meals[0]
                    Log.d("HomeFragment", "meal id ${randomMeal.idMeal} name ${randomMeal.strMeal}")
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

    fun getPopularItems(){
        RetrofitInstance.api
            .getPopularItems("Seafood")
            .enqueue(object :retrofit2.Callback<CategoryList> {
                override fun onResponse(
                    call: Call<CategoryList>,
                    response: Response<CategoryList>
                ) {
                    if(response.body() != null) {
                        popoularMealsLiveData.value = response.body()!!.meals
                    }
                }

                override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                    Log.d("HomeFragment", t.message.toString())
                }

            })
    }

    fun observeRandomMealLivedata() : LiveData<Meal>{
        return randomMealLiveData
    }
    fun observePopularItemsLivedata()  : LiveData<List<CategoryMeals>> {
        return popoularMealsLiveData
    }
}