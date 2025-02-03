package com.example.androidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.adapters.MovieListAdapter;
import com.example.androidapp.databinding.ActivitySelectedCategoryBinding;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class SelectedCategoryActivity extends AppCompatActivity {
    private ActivitySelectedCategoryBinding binding;
    private MovieListAdapter movieListAdapter;
    private MovieViewModel movieViewModel;
    private RecyclerView categoryMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectedCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the category name from the Intent and display it
        TextView categoryName = binding.categoryName;
        categoryName.setText(getIntent().getStringExtra("name"));

        // Initialize the MovieViewModel to handle movie data
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Get the list of movie IDs from the Intent
        ArrayList<String> moviesId = getIntent().getStringArrayListExtra("movies");

        // Set up the RecyclerView to display the movies in a grid layout
        categoryMovies = binding.categoryMovies;
        movieListAdapter = new MovieListAdapter(this);
        categoryMovies.setAdapter(movieListAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        categoryMovies.setLayoutManager(gridLayoutManager);

        // If the list of movie IDs is not null, fetch movie details from the ViewModel
        if (moviesId != null) {
            List<Movie> movieList = new ArrayList<>();
            for (String movieId : moviesId) {
                movieViewModel.getMovieById(movieId).observe(this, movie -> {
                    if (movie != null) {
                        movieList.add(movie);
                        if (movieList.size() == moviesId.size()) {
                            movieListAdapter.setMovies(movieList);
                        }
                    } else {
                        Log.d("SelectedCategoryActivity", "Fail");
                    }
                });
            }
        }

        // Set up the back button to navigate back to the home activity
        binding.btnBack.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        });

    }
}