package com.example.androidapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.R;
import com.example.androidapp.adapters.CategoryAdapter;
import com.example.androidapp.adapters.MovieAdapter;
import com.example.androidapp.databinding.ActivityDeleteMovieBinding;
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
    private CategoryAdapter categoryAdapter;
    private Category selectCategory;
    private Button chooseCategory, editCategory;
    private String selectCategoryId;
    private EditText create_nameCategory;
    private SwitchMaterial create_isPromotedSwitch;
    private Spinner categorySpinner;
    private Button btnCreateCategory;
    private RecyclerView recyclerViewMovies;
    private MovieViewModel movieViewModel;
    private MovieAdapter movieAdapter;
    private List<Movie> selectedMovies = new ArrayList<>(); // הרשימה של הסרטים שנבחרו

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

        editCategory.setOnClickListener(v-> {

            selectCategory.setName(create_nameCategory.getText().toString());
            selectCategory.setIsPromoted(create_isPromotedSwitch.isChecked());
            selectCategory.setMovies(movieAdapter.getSelectedMovieIds());
            categoriesViewModel.edit(selectCategory);
            Toast.makeText(this, "Category created", Toast.LENGTH_SHORT).show();
        });

    }
    public void onResume() {
        super.onResume();
        movieViewModel.reload();
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