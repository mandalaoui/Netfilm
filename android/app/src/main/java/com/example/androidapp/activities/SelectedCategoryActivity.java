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

        TextView categoryName = binding.categoryName;
        categoryName.setText(getIntent().getStringExtra("name"));

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        ArrayList<String> moviesId = getIntent().getStringArrayListExtra("movies");

        categoryMovies = binding.categoryMovies;
        movieListAdapter = new MovieListAdapter(this);
        categoryMovies.setAdapter(movieListAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        categoryMovies.setLayoutManager(gridLayoutManager);

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

        binding.btnBack.setOnClickListener(v -> {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        });

    }
}