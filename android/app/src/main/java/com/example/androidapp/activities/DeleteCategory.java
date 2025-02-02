package com.example.androidapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.R;
import com.example.androidapp.adapters.CategoryAdapter;
import com.example.androidapp.databinding.ActivityCreateCategoryBinding;
import com.example.androidapp.databinding.ActivityDeleteCategoryBinding;
import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.viewmodels.CategoriesViewModel;
import com.example.androidapp.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteCategory extends AppCompatActivity {
    private ActivityDeleteCategoryBinding binding;
    private CategoriesViewModel categoriesViewModel;
    private CategoryAdapter categoryAdapter;
    private Category selectCategory;
    private Button chooseCategory, deleteCategory;
    private String selectCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);
        binding = ActivityDeleteCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chooseCategory = binding.chooseCategoryButton;
        deleteCategory = binding.deleteCategoryButton;
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        chooseCategory.setOnClickListener(v-> {
            showCategorySelectionDialog();
        });

        deleteCategory.setOnClickListener(v-> {
            categoriesViewModel.delete(selectCategory);
        });

    }

    private void showCategorySelectionDialog() {
        selectCategory = new Category();
        categoriesViewModel.reload();
        categoriesViewModel.get().observe(this, categories -> {
            List<String> categoryTitles = new ArrayList<>();
            List<String> categoryIds = new ArrayList<>();
            for (Category category : categories) {
                categoryTitles.add(category.getName());
                categoryIds.add(category.getId());
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose category")
                    .setSingleChoiceItems(categoryTitles.toArray(new String[0]), -1, (dialog, which) -> {

                        String selectedCategoryName = categoryTitles.get(which);
                        selectCategoryId = categoryIds.get(which);

                        for (Category category : categories) {
                            if (category.getId().equals(selectCategoryId)) {
                                selectCategory = category;
                                break;
                            }
                        }
                        Log.d("EditMovieActivity", "Selected movie ID: " + selectCategoryId);
                        dialog.dismiss();

                    })
                    .setCancelable(true)
                    .show();
        });

    }
}