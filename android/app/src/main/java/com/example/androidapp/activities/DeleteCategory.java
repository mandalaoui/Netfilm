package com.example.androidapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.R;
import com.example.androidapp.databinding.ActivityCreateCategoryBinding;
import com.example.androidapp.databinding.ActivityDeleteCategoryBinding;
import com.example.androidapp.viewmodels.CategoriesViewModel;

public class DeleteCategory extends AppCompatActivity {
    private ActivityDeleteCategoryBinding binding;
    private CategoriesViewModel categoriesViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);
        binding = ActivityDeleteCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        categoriesViewModel.reload();

        categoriesViewModel.get().observe(this, categories -> {

        });

    }
}