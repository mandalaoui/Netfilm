package com.example.androidapp.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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

import com.example.androidapp.Movie;
import com.example.androidapp.R;
import com.example.androidapp.databinding.ActivityManagmentBinding;
import com.example.androidapp.databinding.ActivityMovieBinding;

import java.io.File;

public class ManagmentActivity extends AppCompatActivity {
    private Button createMovieViewButton;
    private Button deleteMovieViewButton;
    private ViewFlipper viewFlipper;
    private EditText movieNameInput;
    private TextView deleteConfirmationText;
    private ActivityManagmentBinding binding;
    private EditText movieYearInput;
    private EditText movieTimeInput;
    private EditText movieDescriptionInput;
    private EditText movieIdInput;
    private Button btnCreateMovie;
    private String selectedImageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createMovieViewButton = binding.showCreateMovieViewButton;
        deleteMovieViewButton = binding.showDeleteMovieViewButton;
        viewFlipper = binding.ViewFlipper;

        movieNameInput = binding.movieNameInput;
        movieYearInput = binding.movieYearInput;
        movieTimeInput = binding.movieTimeInput;
        movieDescriptionInput = binding.movieDescriptionInput;
        movieIdInput = binding.movieIdInput;
        btnCreateMovie = binding.createMovieButton;

        viewFlipper.setDisplayedChild(0);

        createMovieViewButton.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(1);

        });

        deleteMovieViewButton.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(2);
        });

        btnCreateMovie.setOnClickListener(v -> {
            createMovie();
        });



    }
    private void createMovie() {

        binding.btnChooseImage.setOnClickListener(v -> {
            requestPermissions();
        });
        String movieName = movieNameInput.getText().toString();
        String movieYear = movieYearInput.getText().toString();
        String movieTime = movieTimeInput.getText().toString();
        String movieDescription = movieDescriptionInput.getText().toString();

        if (movieName.isEmpty() || movieYear.isEmpty() || movieTime.isEmpty() || movieDescription.isEmpty()) {
            Toast.makeText(ManagmentActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
        } else if (selectedImageUri == null || selectedImageUri.isEmpty()) {  // אם לא נבחרה תמונה
            Toast.makeText(ManagmentActivity.this, "Please select an image for the movie", Toast.LENGTH_LONG).show();
        }
        else {
            Movie movie = new Movie(movieName, movieYear, movieTime, movieDescription, selectedImageUri)
        }


        viewFlipper.setDisplayedChild(0);
    }
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");  // Filter only image files
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/jpeg", "image/png", "image/jpg"});
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

