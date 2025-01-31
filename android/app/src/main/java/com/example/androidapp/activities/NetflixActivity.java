package com.example.androidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.databinding.ActivityMainBinding;

public class NetflixActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        videoView = binding.videoView;
//        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.netflixstart);
//        videoView.setVideoURI(videoUri);
//        videoView.setOnPreparedListener(mp -> {
//            videoView.start();
//        });
//        videoView.setOnCompletionListener(mediaPlayer -> {
//            // אחרי שהסרטון נגמר, נסתר את ה-VideoView
//            videoView.setVisibility(View.GONE);
//            ImageView netflixScreenshot = findViewById(R.id.netflixScreenshot);
//            netflixScreenshot.setVisibility(View.GONE);
//
//            // הצגת התוכן הרגיל (כגון RecyclerView או כפתור התחברות)
//            binding.btnStart.setVisibility(View.VISIBLE);
//        });
        binding.btnStart.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);

        });
    }
}