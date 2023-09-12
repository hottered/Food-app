package com.example.dependencyinjection.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dependencyinjection.R
import com.example.dependencyinjection.activites.CategoryMealsActivity
import com.example.dependencyinjection.activites.MainActivity
import com.example.dependencyinjection.adapters.CategoriesAdapter
import com.example.dependencyinjection.databinding.FragmentCategoriesBinding
import com.example.dependencyinjection.viewModel.HomeViewModel

class CategoriesFragment : Fragment() {

    private lateinit var binding : FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        oberserveCategories()
        onCategoryItemClick()
    }
    private fun onCategoryItemClick() {
        categoriesAdapter.onItemClick = {category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME,category.strCategory)
            startActivity(intent)

        }
    }
    private fun oberserveCategories() {
        viewModel.observeCategoriesLivedata().observe(viewLifecycleOwner, Observer { categories->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter;
        }
    }

}