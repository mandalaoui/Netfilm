package com.example.androidapp.activities;

import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import androidx.media3.common.MediaItem;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.MyApplication;
import com.example.androidapp.R;
import com.example.androidapp.adapters.MovieAdapter;
import com.example.androidapp.adapters.MovieListAdapter;
import com.example.androidapp.api.MovieApi;
import com.example.androidapp.api.UserApi;
import com.example.androidapp.databinding.ActivityMovieBinding;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {
    private ActivityMovieBinding binding;

    private TextView tvName, tvYear, tvTime, tvDescription, tvAge;
    private PlayerView moviePlayer;
    private ExoPlayer exoPlayer;
    private Button btnPlay, btnTrailer;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the movie ID and user ID from the intent and global application state
        String movieId = getIntent().getStringExtra("id");
        MyApplication myApplication = MyApplication.getInstance();
        String userId = myApplication.getGlobalUserId();

        // Bind UI components
        tvName = binding.movieTitle;
        tvYear = binding.year;
        tvTime = binding.movieTime;
        tvDescription = binding.movieDescription;
        moviePlayer = binding.moviePlayer;
        btnPlay = binding.btnPlay;
        recyclerView = binding.recyclerViewRecommended;
        tvAge = binding.age;
        btnTrailer = binding.btnTrailer;

        // Set up the UI elements with data from the intent
        Intent intent = getIntent();
        tvName.setText(intent.getStringExtra("name"));
        tvName.setTextColor(ContextCompat.getColor(this, R.color.colorText));

        // Handle movie year
        int year = intent.getIntExtra("Publication_year", -1);
        tvYear.setText(String.valueOf(year));

        // Handle movie time formatting
        String movieTime = intent.getStringExtra("movie_time");
        String[] timeParts = movieTime.split(":");
        int hours = 0;
        int minutes = 0;
        if (timeParts.length == 2) {
            hours = Integer.parseInt(timeParts[0]);
            minutes = Integer.parseInt(timeParts[1]);
        }
        String formattedTime = hours + "h " + minutes + "m";
        tvTime.setText(formattedTime);

        // Set the movie description
        tvDescription.setText(intent.getStringExtra("description"));

        // Handle age restriction
        if (intent.getIntExtra("age", -1) != -1) {
            String age = String.valueOf(intent.getIntExtra("age", -1)) + "+";
            tvAge.setText(age);
        } else {
            tvAge.setVisibility(View.GONE); // Hide age if not specified
        }

        // Initialize ExoPlayer
        exoPlayer = new ExoPlayer.Builder(this).build();
        moviePlayer.setPlayer(exoPlayer);

        // Handle video playback URL
        String videoUrl = "http://10.0.2.2:12345/api/" + intent.getStringExtra("video");
        if (videoUrl != null) {
            MediaItem mediaItem = MediaItem.fromUri(videoUrl);
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();
        } else {
            Log.e("MovieActivity", "Video URL is null or empty");
        }

        // Handle trailer URL
        String trailerUrl = "http://10.0.2.2:12345/api/" + intent.getStringExtra("trailer");

        // Play the movie on button click
        btnPlay.setOnClickListener(v -> {
            UserApi userApi = new UserApi();
            userApi.addToWatchList(movieId); // Add the movie to the user's watchlist
            Intent i = new Intent(this, VideoMovieActivity.class);
            i.putExtra("url", videoUrl);
            startActivity(i); // Start VideoMovieActivity
        });

        // Set up RecyclerView for recommended movies
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        movieAdapter = new MovieAdapter(this, new ArrayList<>(), true);
        recyclerView.setAdapter(movieAdapter);

        // Initialize ViewModel
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Handle trailer button click
        btnTrailer.setOnClickListener(v -> {
            Intent i = new Intent(this, VideoMovieActivity.class);
            i.putExtra("url", trailerUrl);
            startActivity(i); // Start VideoMovieActivity with the trailer URL
        });

        // Fetch recommended movies using the Movie API
        MovieApi movieApi = new MovieApi();
        movieApi.recommend(movieId, new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieAdapter.setMovies(response.body()); // Update RecyclerView with recommended movies
                } else {
                    Log.e("API Response", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("API Failure", "Failed to load recommended movies: " + t.getMessage());
            }
        });
    }

    // Make sure to release the ExoPlayer resources when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }
}
