package com.example.androidapp.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.androidapp.adapters.CategoryListAdapter;
import com.example.androidapp.adapters.MovieListAdapter;
import com.example.androidapp.databinding.ActivityHomeBinding;
import com.example.androidapp.viewmodels.CategoriesViewModel;
import com.google.android.material.appbar.AppBarLayout;
public class HomeActivity extends AppCompatActivity {
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

        categoriesViewModel.reload();

        AppBarLayout appBarLayout = binding.menu;
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) lstCategories.getLayoutParams();
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        lstCategories.setLayoutParams(params);

        categoriesViewModel.get().observe(this, categories -> {
            Log.d("CategoriesViewModel", "Categories list: " + categories);
            categoryListAdapter.setCategories(categories);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        categoriesViewModel.reload();
    }
}