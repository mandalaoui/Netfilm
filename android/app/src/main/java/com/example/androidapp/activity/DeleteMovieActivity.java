package com.example.androidapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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
import com.example.androidapp.adapter.MovieAdapter;
import com.example.androidapp.databinding.ActivityCreateCategoryBinding;
import com.example.androidapp.databinding.ActivityCreateMovieBinding;
import com.example.androidapp.databinding.ActivityDeleteMovieBinding;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityDeleteMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        deleteMovie = binding.deleteMovieButton;
        recyclerViewMovies = binding.recyclerViewMovies;

        recyclerViewMovies.setLayoutManager(new GridLayoutManager(this, 3)); // 3 סרטים בשורה
        movieAdapter = new MovieAdapter(this, new ArrayList<>());
        recyclerViewMovies.setAdapter(movieAdapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.reload();


        movieViewModel.get().observe(this,movies -> {
            movieAdapter.setMovies(movies);
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
                                    movieViewModel.deleteMovieById(movieId); // נמחק סרט לפי ה-ID
                                }
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

    }

    private void onMovieSelected(Movie movie, boolean isSelected) {
        // כאן תוכל להוסיף אם יש צורך בפעולות נוספות כשהסרט נבחר או לא נבחר
        // לדוגמה, תוכל להדפיס לוג או לעדכן את רשימת הסרטים שנבחרו
    }
}