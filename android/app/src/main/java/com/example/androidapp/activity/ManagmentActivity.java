package com.example.androidapp.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.Movie;
import com.example.androidapp.R;
import com.example.androidapp.adapter.MovieListAdapter;
import com.example.androidapp.api.RequestApi;
import com.example.androidapp.databinding.ActivityManagmentBinding;
import com.example.androidapp.databinding.ActivityMovieBinding;

import java.io.File;
import java.util.List;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class ManagmentActivity extends AppCompatActivity {
    private Button createMovieViewButton;
    private Button deleteMovieViewButton;
    private Button createCategoryViewButton;
    private ViewFlipper viewFlipper;
    private EditText create_movieNameInput;
    private TextView deleteConfirmationText;
    private ActivityManagmentBinding binding;
    private EditText create_movieYearInput;
    private EditText create_movieTimeInput;
    private EditText create_movieDescriptionInput;
    private EditText movieIdInput;
    private Button btnCreateMovie;
    private Button btnCreateCategory;
    private String selectedImageUri;
    private String selectedVideoUri;
    private Movie movie;
    private EditText create_nameCategory;
    private SwitchMaterial  create_isPromotedSwitch;


    private RecyclerView recyclerViewMovies;
    private MovieListAdapter moviesAdapter;
    private List<Movie> allMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createMovieViewButton = binding.showCreateMovieViewButton;
        deleteMovieViewButton = binding.showDeleteMovieViewButton;
        createCategoryViewButton = binding.showCreateCategoryViewButton;
        viewFlipper = binding.ViewFlipper;

        create_movieNameInput = binding.movieNameInput;
        create_movieYearInput = binding.movieYearInput;
        create_movieTimeInput = binding.movieTimeInput;
        create_movieDescriptionInput = binding.movieDescriptionInput;
        movieIdInput = binding.movieIdInput;
        btnCreateMovie = binding.createMovieButton;

        btnCreateCategory = binding.createCategoryButton;
        create_nameCategory = binding.categoryNameInput;
        create_isPromotedSwitch = binding.isPromotedSwitch;


        viewFlipper.setDisplayedChild(0);

        createMovieViewButton.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(1);
        });

        deleteMovieViewButton.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(2);
        });

        createCategoryViewButton.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(3);
            createCategory();
        });

        btnCreateMovie.setOnClickListener(v -> {
            createMovie();
        });


        binding.btnChooseImage.setOnClickListener(v -> {
            requestPermissions();
        });
        binding.btnChooseVideo.setOnClickListener(v -> {
            requestPermissionsForVideo();
        });


    }
    private void createCategory() {

        String nameCategoty = create_nameCategory.getText().toString();
        boolean isPromoted = create_isPromotedSwitch.isChecked();

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // נניח ש- allMovies הוא אובייקט שמכיל את כל הסרטים
//        moviesAdapter = new MovieListAdapter(ManagmentActivity.this);
//        recyclerViewMovies.setAdapter(moviesAdapter);

        if (nameCategoty.isEmpty()) {
            Toast.makeText(this, "Category name is required", Toast.LENGTH_SHORT).show();
        } else {
            btnCreateCategory.setOnClickListener(v -> {
            });
        }
    }


    private void createMovie() {
        File imageFile = null;
        File videoFile = null;

        String movieName = create_movieNameInput.getText().toString();
        String movieYearText = create_movieYearInput.getText().toString();
        String movieTime = create_movieTimeInput.getText().toString();
        String movieDescription = create_movieDescriptionInput.getText().toString();

        if (movieName.isEmpty() || movieYearText.isEmpty()|| movieTime.isEmpty() || movieDescription.isEmpty()) {
            Toast.makeText(ManagmentActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
        } else if (selectedImageUri == null || selectedImageUri.isEmpty()) {  // אם לא נבחרה תמונה
            Toast.makeText(ManagmentActivity.this, "Please select an image for the movie", Toast.LENGTH_LONG).show();
        }
        else {


            try {
                int movieYear = Integer.parseInt(movieYearText);
                movie = new Movie(movieName, movieYear, movieTime, movieDescription);
            } catch (NumberFormatException e) {
                Toast.makeText(ManagmentActivity.this, "Please enter a valid year", Toast.LENGTH_SHORT).show();
            }

            if (selectedImageUri == null || selectedImageUri.isEmpty()) {
                Toast.makeText(ManagmentActivity.this, "You must select an image for the movie!", Toast.LENGTH_LONG).show();
                return;
            }
            imageFile = getFileFromUri(Uri.parse(selectedImageUri));
            if (selectedVideoUri == null || selectedVideoUri.isEmpty()) {
                Toast.makeText(ManagmentActivity.this, "You must select an image for the movie!", Toast.LENGTH_LONG).show();
                return;
            }
            videoFile = getFileFromUri(Uri.parse(selectedVideoUri));
            RequestApi requestApi = new RequestApi(this);
            requestApi.createMovie(movie, imageFile, videoFile);


        }

        viewFlipper.setDisplayedChild(0);
    }


    private void openVideoChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/mp4", "video/avi", "video/mkv"});
        pickVideoLauncher.launch(intent);
    }
    private void requestPermissionsForVideo() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        } else {
            openVideoChooser();
        }
    }
    private ActivityResultLauncher<Intent> pickVideoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri videoUri = result.getData().getData();
                    selectedVideoUri = videoUri.toString();
                    VideoView videoView = findViewById(R.id.videoView);
                    videoView.setVideoURI(videoUri);  // הצגת הסרטון ב- VideoView
                    videoView.setVisibility(View.VISIBLE); // Save video URI
                }
            });

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");  // Filter only image files
        pickImageLauncher.launch(intent); // Waiting for a response from the action
    }

    private ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData(); // Get the URI of the selected image
                    binding.imageViewProfilePic.setImageURI(imageUri);  // Display the image in the ImageView
                    selectedImageUri = imageUri.toString();// Update the selectedImageUri variable
                }
            });

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        } else {
            openImageChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted!", Toast.LENGTH_SHORT).show();
                openImageChooser();
            } else {
                Toast.makeText(this, "Permissions denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getFileFromUri(Uri uri) {
        try {
            String path = null;

            if (uri.getScheme().equals("content")) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    path = cursor.getString(columnIndex);
                    cursor.close();
                }
            }
            if (path == null) {
                path = uri.getPath();
            }

            if (path != null) {
                return new File(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

