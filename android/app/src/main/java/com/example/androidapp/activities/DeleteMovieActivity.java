package com.example.androidapp.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.adapters.MovieAdapter;
import com.example.androidapp.databinding.ActivityDeleteMovieBinding;
import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class DeleteMovieActivity extends AppCompatActivity {
    private ActivityDeleteMovieBinding binding;
    private Button deleteMovie;

    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;
    private List<Movie> allMovies;
    private List<String> movieTitles, movieIds;
    private Movie selectedMovie;

    private String selectedMovieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityDeleteMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deleteMovie = binding.deleteMovieButton;

        // Setting up the ViewModel to observe the movies
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.reload();
        movieViewModel.get().observe(this, movies -> {
            movieTitles = new ArrayList<>();
            movieIds = new ArrayList<>();
            for (Movie movie : movies) {
                movieTitles.add(movie.getName());
                movieIds.add(movie.get_id());
            }
            allMovies = movies;
        });
        // Set up the click listener for the "Choose Movie" button to show the movie selection dialog
        binding.chooseMovieButton.setOnClickListener(v -> {
            showMovieSelectionDialog();
        });

        // Set up the click listener for the "Delete Movie" button to confirm and delete the movie
        deleteMovie.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this movie?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Call ViewModel to delete the selected movie by ID
                            movieViewModel.deleteMovieById(selectedMovie);

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
    // Method to display the movie selection dialog
    private void showMovieSelectionDialog() {
        selectedMovie = new Movie();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose movie")
                .setSingleChoiceItems(movieTitles.toArray(new String[0]), -1, (dialog, which) -> {
                    String selectedMovieName = movieTitles.get(which);
                    selectedMovieId = movieIds.get(which);
                    for (Movie movie : allMovies) {
                        if (movie.get_id().equals(selectedMovieId)) {
                            selectedMovie = movie;
                            break;
                        }
                    }
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }
}