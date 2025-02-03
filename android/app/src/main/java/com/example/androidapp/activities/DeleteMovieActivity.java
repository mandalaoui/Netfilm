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

    private RecyclerView recyclerViewMovies;
    private MovieAdapter movieAdapter;

    private MovieViewModel movieViewModel;
    private List<Movie> allMovies;
    private Movie selectedMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityDeleteMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deleteMovie = binding.deleteMovieButton;
        recyclerViewMovies = binding.recyclerViewMovies;

        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 3)); // 3 סרטים בשורה
        movieAdapter = new MovieAdapter(this, new ArrayList<>(), true);
        recyclerViewMovies.setAdapter(movieAdapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.reload();


        movieViewModel.get().observe(this,movies -> {
            movieAdapter.setMovies(movies);
            allMovies = movies;
        });

        deleteMovie.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this movie?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<String> selectedMovieIds = movieAdapter.getSelectedMovieIds();

                            Log.d("DeleteMovie","delete movie" + selectedMovieIds.toString());
                            if (selectedMovieIds.isEmpty()) {
                                Toast.makeText(DeleteMovieActivity.this, "No movies selected", Toast.LENGTH_SHORT).show();
                            } else {
                                for (String movieId : selectedMovieIds) {
                                    for (Movie movie : allMovies) {
                                        if (movie.get_id().equals(movieId)) {
                                            selectedMovie = movie;
                                            break;
                                        }
                                    }
                                    movieViewModel.deleteMovieById(selectedMovie);
                                }
                            }
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}