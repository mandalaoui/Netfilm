package com.example.androidapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.androidapp.api.RequestApi;
import com.example.androidapp.databinding.ActivityMovieBinding;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieActivity extends AppCompatActivity {
    private ActivityMovieBinding binding;

    private TextView tvName, tvYear,tvTime, tvDescription;
    private VideoView videoView;
    private ImageButton btnPlayPause;
    LinearLayout videoControlPanel;
    private SeekBar seekBar;
    private Handler handler = new Handler();
    private Button btnPlay;
    private int videoDuration;
    String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String movieId = "6794d57dc3b3353fd3a318ac";
        String userId = "679178e884e6da9a833f5452";

        tvName = binding.movieTitle;
        tvYear = binding.year;
        tvTime = binding.movieTime;
        tvDescription = binding.movieDescription;
        videoView = binding.videoView;
        btnPlayPause = binding.btnPlayPause;
        videoControlPanel = binding.videoControlPanel;
        seekBar = binding.seekBar;
        btnPlay = binding.btnPlay;


        RequestApi requestApi = new RequestApi();
        requestApi.getMovie(movieId,userId, new Callback<Movie>() {

            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    Movie movie = response.body();
                    if (movie != null) {
                        Log.d("API_RESPONSE", "Movie details: " + movie.getVideoUrl());

                        // הצגת המידע בפרטי הסרט
                        tvName.setText(movie.getName());
                        tvYear.setText(String.valueOf(movie.getPublication_year()));  // שינוי לשדה הנכון
                        tvDescription.setText(movie.getDescription());
                        tvTime.setText(movie.getMovie_time());
                        // הצגת תמונת הסרט
//                        Picasso.get().load(movie.getImageUrl()).into(ivPoster);

                        // הצגת וידאו (שימוש ב-VideoView)
//                        if (movie.getVideoUrl() != null && !movie.getVideoUrl().isEmpty()) {
//                            Uri videoUri = Uri.parse(movie.getVideoUrl());
//                            videoView.setVideoURI(videoUri);
//                            videoView.start();
//                        }
                        videoUrl = movie.getVideoUrl();
                        if (videoUrl != null && !videoUrl.isEmpty()) {
                            if (!videoUrl.startsWith("http")) {
                                videoUrl = "http://10.0.2.2:12345/" + videoUrl;
                            }
                            Log.e("VideoView", "playing video: ");
                            Uri videoUri = Uri.parse(videoUrl);
                            videoView.setVideoURI(videoUri);
                            videoView.setOnPreparedListener(mp -> {
                                Log.d("VideoView", "Video is prepared and will start now.");
                                videoView.start();
                                videoDuration = videoView.getDuration();
                                seekBar.setMax(videoDuration);
                                updateSeekBar();  // עדכון ה-SeekBar עם הזמן הנוכחי
                                moviePlay();
                            });

                            // הוספת Listener לטיפול בשגיאות
                            videoView.setOnErrorListener((mp, what, extra) -> {
                                Log.e("VideoView", "Error playing video: " + what + ", " + extra);
                                return true;
                            });
                        }
                    } else {
                        try {
                            if (response.errorBody() != null) {
                                String errorMessage = response.errorBody().string();
                                Log.e("Register", "Error: " + errorMessage);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("Register", "An error occurred");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("GET", "get movie failed with error: " + t.getMessage());
            }
        });

        btnPlay.setOnClickListener(view -> {
            Intent i = new Intent(this, VideoMovieActivity.class);
            i.putExtra("videoUrl",videoUrl);
            startActivity(i);
        });


    }

    private void moviePlay() {
        videoView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                videoControlPanel.setVisibility(View.VISIBLE);
                v.performClick();
                new Handler().postDelayed(() -> videoControlPanel.setVisibility(View.GONE), 3000);
            }
            return false;
        });

        btnPlayPause.setOnClickListener(view -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                btnPlayPause.setImageResource(R.drawable.ic_play);  // הצגת האייקון של play
            } else {
                videoView.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause);  // הצגת האייקון של pause
            }
        });

        // הוספת Listener ל-SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);  // העברת הסרטון לנקודת הזמן החדשה
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // עדכון ה-SeekBar בזמן אמת
    private void updateSeekBar() {
        seekBar.setProgress(videoView.getCurrentPosition());
        if (videoView.isPlaying()) {
            handler.postDelayed(this::updateSeekBar, 1000);  // עדכון כל שנייה
        }
    }

}