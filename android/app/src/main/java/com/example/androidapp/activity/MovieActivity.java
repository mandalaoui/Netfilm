package com.example.androidapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.media3.common.MediaItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.Movie;
import com.example.androidapp.adapter.MovieListAdapter;
import com.example.androidapp.api.RequestApi;
import com.example.androidapp.databinding.ActivityMovieBinding;
import com.example.androidapp.viewmodels.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {
    private ActivityMovieBinding binding;

    private TextView tvName, tvYear,tvTime, tvDescription;
    private VideoView videoView;
    private MovieViewModel movieViewModel;
    private PlayerView moviePlayer;
    private ExoPlayer exoPlayer;
    private Handler handler = new Handler();
    private Button btnPlay;
    private List<Movie> recommendedMoviesList = new ArrayList<>();
    MovieListAdapter movieListAdapter;
    String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        String movieId = getIntent().getStringExtra("movieId");
//        String movieId = "679629522d6eaf038e9e1768";
        String userId = "67963d5d483e59c7cb61231b";

        tvName = binding.movieTitle;
        tvYear = binding.year;
        tvTime = binding.movieTime;
        tvDescription = binding.movieDescription;
        moviePlayer = binding.moviePlayer;
        btnPlay = binding.btnPlay;

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getMovie().observe(this, movie -> {
            if (movie != null) {
                tvName.setText(movie.getName());
                tvYear.setText(String.valueOf(movie.getPublication_year()));
                tvDescription.setText(movie.getDescription());
                tvTime.setText(movie.getMovie_time());

                videoUrl = movie.getVideoUrl();
                if (videoUrl != null && !videoUrl.isEmpty()) {
                    if (!videoUrl.startsWith("http")) {
                        videoUrl = "http://10.0.2.2:12345/" + videoUrl;
                    }

                    Uri videoUri = Uri.parse(videoUrl);
                    MediaItem mediaItem = MediaItem.fromUri(videoUri);
                    exoPlayer.setMediaItem(mediaItem);
                    exoPlayer.prepare();
                    exoPlayer.play();
//                    videoView.setVideoURI(videoUri);
//                    videoView.start();
//                    videoView.setOnPreparedListener(mp -> {
//                                Log.d("VideoView", "Video is prepared and will start now.");
//                                MediaController mc = new MediaController(MovieActivity.this);
//                                mc.setAnchorView(videoView);
//                                videoView.setMediaController(mc);
//                                videoView.start();
//                            });
                }
            }
        });

        btnPlay.setOnClickListener(view -> {
            Intent i = new Intent(this, VideoMovieActivity.class);
            i.putExtra("videoUrl",videoUrl);
            startActivity(i);
        });

        RecyclerView recyclerView = binding.recyclerViewRecommended;
//        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false); // 3 סרטים בשורה
        recyclerView.setLayoutManager(gridLayoutManager);

        RequestApi recommendMovie = new RequestApi();
        recommendMovie.getRecommendMovie(movieId,userId, new Callback<List<Movie>>() {
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recommendedMoviesList.clear();
                    recommendedMoviesList.addAll(response.body());
                    movieListAdapter.notifyDataSetChanged();
                } else {
                    Log.e("API", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e("API", "Error fetching recommended movies: " + t.getMessage());
            }

        });

        movieListAdapter = new MovieListAdapter(this, recommendedMoviesList, new MovieListAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClick(Movie movie) {
                MovieOnClick(movie);
            }
        });
        recyclerView.setAdapter(movieListAdapter);

    }
    public void MovieOnClick(Movie movie) {
        movieViewModel.setSelectedMovie(movie);
    }

}