package com.example.androidapp.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.R;
import com.example.androidapp.databinding.ActivityDeleteCategoryBinding;
import com.example.androidapp.entities.Category;
import com.example.androidapp.viewmodels.CategoriesViewModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteCategory extends AppCompatActivity {
    private ActivityDeleteCategoryBinding binding;
    private CategoriesViewModel categoriesViewModel;
    private Category selectCategory;
    private Button chooseCategory, deleteCategory;
    private String selectCategoryId;
    private List<String> categoryTitles,categoryIds;
    private List<Category> allcategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);
        binding = ActivityDeleteCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chooseCategory = binding.chooseCategoryButton;
        deleteCategory = binding.deleteCategoryButton;
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        categoriesViewModel.reload();
        categoriesViewModel.get().observe(this, categories -> {
                    categoryTitles = new ArrayList<>();
                    categoryIds = new ArrayList<>();
                    for (Category category : categories) {
                        categoryTitles.add(category.getName());
                        categoryIds.add(category.getId());
                    }
                    allcategories = categories;
                });


            chooseCategory.setOnClickListener(v-> {
            showCategorySelectionDialog();
        });

        deleteCategory.setOnClickListener(v-> {
            categoriesViewModel.delete(selectCategory);
            finish();
        });

    }

    private void showCategorySelectionDialog() {
        selectCategory = new Category();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose category")
                .setSingleChoiceItems(categoryTitles.toArray(new String[0]), -1, (dialog, which) -> {
                        String selectedCategoryName = categoryTitles.get(which);
                        selectCategoryId = categoryIds.get(which);

                        for (Category category : allcategories) {
                            if (category.getId().equals(selectCategoryId)) {
                                selectCategory = category;
                                break;
                            }
                        }
                        dialog.dismiss();

                })
                .setCancelable(true)
                .show();
    }

}