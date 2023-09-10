package com.example.dependencyinjection.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dependencyinjection.pojo.MealsByCategory
import com.example.dependencyinjection.pojo.MealsByCategoryList
import com.example.dependencyinjection.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {

    private var mealsByCategoryLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName: String) {
        RetrofitInstance
            .api
            .getMealsByCategory(categoryName)
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        mealsByCategoryLiveData.value = response.body()!!.meals
                    }
                }
                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d("ActivityMealsByCategory",t.message.toString())
                }

            })
    }
    fun observeMealsByCategoryLivedata() : LiveData<List<MealsByCategory>>{
        return mealsByCategoryLiveData
    }
}