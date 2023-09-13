package com.example.dependencyinjection.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.request.transition.Transition.ViewAdapter
import com.example.dependencyinjection.R
import com.example.dependencyinjection.activites.MainActivity
import com.example.dependencyinjection.adapters.MealsAdapter
import com.example.dependencyinjection.databinding.FragmentSearchBinding
import com.example.dependencyinjection.viewModel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchRecyclerViewAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        prepareSearchMealRecview()
        onClickArrow()
        observeSearchMealsLiveData()

        var searchJob: Job? = null
        binding.etSearchBox.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeals(searchQuery.toString())
            }
        }

    }

    private fun observeSearchMealsLiveData() {
        viewModel.observeSearchedMealsLivedata().observe(viewLifecycleOwner, Observer { mealsList ->
            searchRecyclerViewAdapter.differ.submitList(mealsList)
        })
    }

    private fun onClickArrow() {
        binding.imgSearchArrow.setOnClickListener {
            searchMeals()
        }
    }

    private fun searchMeals() {
        val searchQuery = binding.etSearchBox.text.toString()
        if (searchQuery.isNotEmpty()) {
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareSearchMealRecview() {
        searchRecyclerViewAdapter = MealsAdapter()
        binding.rvSearchMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = searchRecyclerViewAdapter
        }
    }
}