package com.example.androidapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx. media3.common. MediaItem;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import com.google.android.exoplayer2.MediaItem;

import com.example.androidapp.MyApplication;
import com.example.androidapp.adapters.MovieAdapter;
import com.example.androidapp.adapters.MovieListAdapter;
import com.example.androidapp.api.MovieApi;
import com.example.androidapp.api.UserApi;
import com.example.androidapp.databinding.ActivityMovieBinding;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.viewmodels.MovieViewModel;
//import com.google.android.exoplayer2.MediaItem;
//import com.google.android.exoplayer2.SimpleExoPlayer;
//import com.example.androidapp.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {
    private ActivityMovieBinding binding;
    private TextView tvName, tvYear,tvTime, tvDescription;
//    private MovieViewModel movieViewModel;
    private PlayerView moviePlayer;
    private ExoPlayer exoPlayer;
    private Handler handler = new Handler();
    private Button btnPlay;
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

        Intent intent = getIntent();
        tvName.setText(intent.getStringExtra("name"));
        Log.d("MovieActivity", "Movie name: " + intent.getStringExtra("name"));
        tvName.setTextColor(Color.WHITE);
        tvYear.setText(intent.getStringExtra("Publication_year"));
        tvTime.setText(intent.getStringExtra("movie_time"));
        tvDescription.setText(intent.getStringExtra("description"));

        exoPlayer = new ExoPlayer.Builder(this).build();
        moviePlayer.setPlayer(exoPlayer);
//        moviePlayer.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        String videoUrl = "http://10.0.2.2:12345/api/" + intent.getStringExtra("video");
        if (videoUrl != null) {
            MediaItem mediaItem = MediaItem.fromUri(videoUrl);

            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();
        } else {
            Log.e("MovieActivity", "Video URL is null or empty");
        }

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 סרטים בשורה
        movieAdapter = new MovieAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(movieAdapter);

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

//        movieViewModel.recommend(intent.getStringExtra("id"));

//        movieViewModel.get().observe(this,movies -> {
//            movieAdapter.setMovies(movies);
//        });
        btnPlay.setOnClickListener(v -> {
            UserApi userApi = new UserApi();
            userApi.addToWatchList(movieId);
            Intent i = new Intent(this, VideoMovieActivity.class);
            i.putExtra("videoUrl", videoUrl);
            startActivity(i);
        });

        MovieApi movieApi = new MovieApi();
        movieApi.recommend(movieId , new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    movieListAdapter.setMovies(response.body());
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