package com.example.dependencyinjection.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dependencyinjection.pojo.Meal
import com.example.dependencyinjection.pojo.MealList
import com.example.dependencyinjection.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel : ViewModel() {
    private var mealDetailLivedata = MutableLiveData<Meal>()

    fun getMealDetail(id: String) {
        RetrofitInstance.api
            .getMealDetails(id)
            .enqueue(object : Callback<MealList> {
                override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                    if (response.body() != null) {
                        val mealDetail: Meal = response.body()!!.meals[0]
                        mealDetailLivedata.value = mealDetail
                    } else {
                        return
                    }

                }

                override fun onFailure(call: Call<MealList>, t: Throwable) {
                    Log.d("FragmentMeal", t.message.toString())
                }
            })
    }
    fun observeMealDetailLiveData() : LiveData<Meal>{
        return mealDetailLivedata
    }
}