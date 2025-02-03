package com.example.androidapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import androidx. media3.common. MediaItem;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.MyApplication;
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

    private TextView tvName, tvYear,tvTime, tvDescription, tvAge;
    private PlayerView moviePlayer;
    private ExoPlayer exoPlayer;
    private Button btnPlay, btnTrailer;
    private List<Movie> recommendedMoviesList = new ArrayList<>();
    MovieListAdapter movieListAdapter;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        String movieId = getIntent().getStringExtra("id");
        MyApplication myApplication = MyApplication.getInstance();
        String userId = myApplication.getGlobalUserId();

        tvName = binding.movieTitle;
        tvYear = binding.year;
        tvTime = binding.movieTime;
        tvDescription = binding.movieDescription;
        moviePlayer = binding.moviePlayer;
        btnPlay = binding.btnPlay;
        recyclerView = binding.recyclerViewRecommended;
        tvAge = binding.age;
        btnTrailer = binding.btnTrailer;

        Intent intent = getIntent();
        tvName.setText(intent.getStringExtra("name"));
        tvName.setTextColor(Color.WHITE);
        tvYear.setText(intent.getStringExtra("Publication_year"));

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

        tvDescription.setText(intent.getStringExtra("description"));
        String age = intent.getStringExtra("age") + "+";
        tvAge.setText(age);
        Log.d("MovieActivity", "Movie year: " + intent.getStringExtra("Publication_year"));
        Log.d("MovieActivity", "Movie age: " + intent.getStringExtra("age"));

        exoPlayer = new ExoPlayer.Builder(this).build();
        moviePlayer.setPlayer(exoPlayer);
        String videoUrl = "http://10.0.2.2:12345/api/" + intent.getStringExtra("video");
        if (videoUrl != null) {
            MediaItem mediaItem = MediaItem.fromUri(videoUrl);

            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();
        } else {
            Log.e("MovieActivity", "Video URL is null or empty");
        }
        String trailerUrl = "http://10.0.2.2:12345/api/" + intent.getStringExtra("trailer");

        btnPlay.setOnClickListener(v -> {
            UserApi userApi = new UserApi();
            userApi.addToWatchList(movieId);
            Intent i = new Intent(this, VideoMovieActivity.class);
            i.putExtra("videoUrl", videoUrl);
            startActivity(i);
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 סרטים בשורה
        movieAdapter = new MovieAdapter(this, new ArrayList<>(), true);
        recyclerView.setAdapter(movieAdapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        btnTrailer.setOnClickListener(v -> {
            Intent i = new Intent(this, VideoMovieActivity.class);
            i.putExtra("trailerUrl", trailerUrl);
            startActivity(i);
        });

        MovieApi movieApi = new MovieApi();
        movieApi.recommend(movieId , new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieAdapter.setMovies(response.body());
                }
                else {
                    Log.e("API Response", "Response error: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t){
                Log.e("API Failure", "Failed to load searched movies: " + t.getMessage());
            }
        });
    }
}