package com.example.androidapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapters.MovieAdapter;
import com.example.androidapp.databinding.ActivityEditCategoryBinding;
import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.viewmodels.CategoriesViewModel;
import com.example.androidapp.viewmodels.MovieViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class EditCategoryActivity extends AppCompatActivity {
    private ActivityEditCategoryBinding binding;
    private CategoriesViewModel categoriesViewModel;
    private Category selectCategory;
    private Button chooseCategory, editCategory;
    private String selectCategoryId;
    private EditText create_nameCategory;
    private SwitchMaterial create_isPromotedSwitch;
    private RecyclerView recyclerViewMovies;
    private MovieViewModel movieViewModel;
    private MovieAdapter movieAdapter;
    private List<String> categoryTitles,categoryIds;
    private List<Category> allCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        create_nameCategory = binding.categoryNameInput;
        create_isPromotedSwitch = binding.isPromotedSwitch;

        chooseCategory = binding.chooseCategoryButton;
        editCategory = binding.editCategoryButton;
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        categoriesViewModel.reload();
        categoriesViewModel.get().observe(this, categories -> {
                    categoryTitles = new ArrayList<>();
                    categoryIds = new ArrayList<>();
                    for (Category category : categories) {
                        categoryTitles.add(category.getName());
                        categoryIds.add(category.getId());
                    }
                    allCategories = categories;
                });

        chooseCategory.setOnClickListener(v-> {
            showCategorySelectionDialog();
        });


        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 3)); // 3 סרטים בשורה
        movieAdapter = new MovieAdapter(this, new ArrayList<>(), false);
        recyclerViewMovies.setAdapter(movieAdapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.reload();

        movieViewModel.get().observe(this,movies -> {
            movieAdapter.setMovies(movies);
        });
        // Set up the edit button for updating the category
        editCategory.setOnClickListener(v-> {
            selectCategory.setName(create_nameCategory.getText().toString());
            selectCategory.setIsPromoted(create_isPromotedSwitch.isChecked());
            selectCategory.setMovies(movieAdapter.getSelectedMovieIds());
            categoriesViewModel.edit(selectCategory);
        });

    }
    public void onResume() {
        super.onResume();
        movieViewModel.reload();
    }

    private void showCategorySelectionDialog() {
        selectCategory = new Category();
        // Create and show an AlertDialog for category selection
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose category")
                    .setSingleChoiceItems(categoryTitles.toArray(new String[0]), -1, (dialog, which) -> {

                        String selectedCategoryName = categoryTitles.get(which);
                        selectCategoryId = categoryIds.get(which);

                        // Find the category that matches the selected ID
                        for (Category category : allCategories) {
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