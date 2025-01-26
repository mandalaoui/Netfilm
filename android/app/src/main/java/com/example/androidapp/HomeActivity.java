package com.example.androidapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.androidapp.adapters.CategoryListAdapter;
import com.example.androidapp.adapters.MovieListAdapter;
import com.example.androidapp.databinding.ActivityHomeBinding;
import com.example.androidapp.viewmodels.CategoriesViewModel;

public class HomeActivity extends AppCompatActivity {

//    private CategoriesViewModel viewModel;
//    private ActivityHomeBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityHomeBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        viewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
//
//        RecyclerView recyclerViewCategories = binding.recyclerViewCategories;
//        final CategoryAdapter categoryAdapter = new CategoryAdapter(this);
//        recyclerViewCategories.setAdapter(categoryAdapter);
//        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
//
//        viewModel.get().observe(this, categories -> {
//            categoryAdapter.setCategories(categories);
//        });
//    }

//    private RecyclerView lstCategories;
//    private CategoryAdapter categoryAdapter;
    private CategoriesViewModel categoriesViewModel;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        RecyclerView lstCategories = binding.lstCategories;
        final CategoryListAdapter categoryListAdapter = new CategoryListAdapter(this);
        lstCategories.setAdapter(categoryListAdapter);
        lstCategories.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView lstMovies = findViewById(R.id.lstMovies);
        final MovieListAdapter movieListAdapter = new MovieListAdapter(this);
        lstMovies.setAdapter(movieListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lstMovies.setLayoutManager(new LinearLayoutManager(this));

        categoriesViewModel.reload();

//        SwipeRefreshLayout refreshLayout = (binding.refreshLayout);
//        refreshLayout.setOnRefreshListener(() -> {
//            categoriesViewModel.reload();
//        });

        categoriesViewModel.get().observe(this, categories -> {
            Log.d("CategoriesViewModel", "Categories list: " + categories);
            categoryListAdapter.setCategories(categories);
//            refreshLayout.setRefreshing(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        categoriesViewModel.reload();
    }
}