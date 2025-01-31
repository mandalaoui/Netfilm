package com.example.androidapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.entities.Category;
import com.example.androidapp.R;
import com.example.androidapp.adapters.MovieAdapter;
import com.example.androidapp.databinding.ActivityCreateCategoryBinding;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.viewmodels.MovieViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class CreateCategoryActivity extends AppCompatActivity {
    private ActivityCreateCategoryBinding binding;
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

        setContentView(R.layout.activity_create_category);
        binding = ActivityCreateCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btnCreateCategory = binding.createCategoryButton;
        create_nameCategory = binding.categoryNameInput;
        create_isPromotedSwitch = binding.isPromotedSwitch;

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 3)); // 3 סרטים בשורה
        movieAdapter = new MovieAdapter(this, new ArrayList<>());
        recyclerViewMovies.setAdapter(movieAdapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.reload();

        movieViewModel.get().observe(this,movies -> {
            movieAdapter.setMovies(movies);
        });

//        movieViewModel.fetchMoviesFromApi();

//        movieViewModel.getMoviesFromApi().observe(this, movies -> {
//            if (movies != null && !movies.isEmpty()) {
//                Log.e("MovieViewModel",movies.toString());
//                movieAdapter.setMovies(movies);
//            } else {
//                Toast.makeText(this, "Failed to load movies", Toast.LENGTH_SHORT).show();
//            }
//        });


        btnCreateCategory.setOnClickListener(v -> {
            String categoryName = create_nameCategory.getText().toString();
            boolean isPromoted = create_isPromotedSwitch.isChecked();
            List<String> selectedIds = movieAdapter.getSelectedMovieIds();

            if (categoryName.isEmpty()) {
                Toast.makeText(this, "Category name is required", Toast.LENGTH_SHORT).show();
                return;
            }

            Category category = new Category(categoryName, isPromoted, selectedIds);

            Toast.makeText(this, "Category created", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        movieViewModel.reload();
    }

    private void onMovieSelected(Movie movie, boolean isSelected) {
//        if (isSelected) {
//            selectedMovies.add(movie);  // הוספת סרט שנבחר
//        } else {
//            selectedMovies.remove(movie);  // הסרת סרט שנבחר
//        }
    }

}