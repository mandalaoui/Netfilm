package com.example.androidapp;

import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.databinding.ActivityMainBinding;
import com.example.androidapp.databinding.ActivityVideoMovieBinding;

public class VideoMovieActivity extends AppCompatActivity {
    private VideoView videoView;
    private ActivityVideoMovieBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        videoView = binding.videoView;

//        String videoUrl = getIntent().getStringExtra("videoUrl");
//        if (videoUrl != null) {
//            Uri videoUri = Uri.parse(videoUrl);
//            videoView.setVideoURI(videoUri);
//            rotateVideo(videoView);
//
//            videoView.setOnPreparedListener(mp -> {
//                videoView.start();
////                setVideoViewSize(videoView, mp);
//            });
//        }
//
//    }
//    private void rotateSurface(Surface surface, float angle) {
//        Canvas canvas = surface.lockCanvas(null);
//        if (canvas != null) {
//            Matrix matrix = new Matrix();
//            matrix.setRotate(angle, canvas.getWidth() / 2, canvas.getHeight() / 2);
//            canvas.setMatrix(matrix);
//            surface.unlockCanvasAndPost(canvas);
//        }
//    }
////    private void setVideoViewSize(VideoView videoView, android.media.MediaPlayer mp) {
//        // מקבל את רוחב וגובה המסך
//        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
//        int screenWidth = display.getWidth();
//        int screenHeight = display.getHeight();
//
//        // מקבל את רוחב וגובה הוידאו
//        int videoWidth = mp.getVideoWidth();
//        int videoHeight = mp.getVideoHeight();
//
//        // מחשב את יחס הגודל בין רוחב המסך לרוחב הוידאו
//        float scaleX = (float) screenWidth / videoWidth;
//        float scaleY = (float) screenHeight / videoHeight;
//
//        // קובע את הסקייל כך שיתפוס את כל רוחב המסך (לא משנה את הגובה)
//        float scale = Math.max(scaleX, scaleY);
//
//        // הגדרת גודל הוידאו לפי הגודל החדש
//        videoView.setLayoutParams(new FrameLayout.LayoutParams(
//                (int) (videoWidth * scale), (int) (videoHeight * scale)));
//
//        // הגדרת המידות מחדש כך שהוידאו יתפוס את כל המסך
//        videoView.setLayoutParams(new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT));
//    }
    }

}