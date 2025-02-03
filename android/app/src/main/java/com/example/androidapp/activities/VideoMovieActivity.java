package com.example.androidapp.activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.ui.PlayerView;

import com.example.androidapp.databinding.ActivityVideoMovieBinding;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.common.MediaItem;

public class VideoMovieActivity extends AppCompatActivity {
    private PlayerView videoMovie;
    private ExoPlayer exoPlayer;
    private ActivityVideoMovieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoMovie = binding.videoMovie;

        exoPlayer = new ExoPlayer.Builder(this).build();
        videoMovie.setPlayer(exoPlayer);

        String videoUrl = getIntent().getStringExtra("videoUrl");
        if (videoUrl != null) {
            Uri videoUri = Uri.parse(videoUrl);

            MediaItem mediaItem = MediaItem.fromUri(videoUri);

            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.play();
        }
    }

}