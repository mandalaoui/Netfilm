package com.example.androidapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.api.CategoryApi;
import com.example.androidapp.entities.Category;
import com.example.androidapp.R;
import com.example.androidapp.adapters.CategoryAdapter;
import com.example.androidapp.adapters.MovieAdapter;
import com.example.androidapp.api.UserApi;
import com.example.androidapp.databinding.ActivityCreateMovieBinding;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.viewmodels.CategoriesViewModel;
import com.example.androidapp.viewmodels.MovieViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMovieActivity extends AppCompatActivity {
    private ActivityCreateMovieBinding binding;
    private EditText create_movieYearInput,create_movieDescriptionInput, create_age;
    private EditText create_movieNameInput, create_movieTimeInput;
    private Button btnCreateMovie;
    private MovieViewModel movieViewModel;
    private String selectedVideoUri, selectTrailerUri, selectedImageUri;
    private Movie movie;
    private ImageView imageCheck, videoCheck, trailerCheck;
    private List<String> selectedCategories;
    private ListView categoryListView;
    private CategoriesViewModel categoriesViewModel;
    private List<String> categoryTitles,categoryIds;
    private List<Category> allcategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NestedScrollView scrollView = findViewById(R.id.createMovie);
        scrollView.post(() -> {
            scrollView.scrollTo(0, 0);
        });

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        create_movieNameInput = binding.movieNameInput;
        create_movieYearInput = binding.movieYearInput;
        create_movieTimeInput = binding.movieTimeInput;
        create_movieDescriptionInput = binding.movieDescriptionInput;
        create_age = binding.movieAgeInput;
        btnCreateMovie = binding.createMovieButton;
        categoryListView = binding.categoryListView;
        imageCheck = binding.checkProfilePic;
        videoCheck = binding.checkVideo;
        trailerCheck = binding.checkTrailer;

        btnCreateMovie.setOnClickListener(v -> createMovie());
        binding.btnChooseImage.setOnClickListener(v -> requestPermissions());
        binding.btnChooseVideo.setOnClickListener(v -> requestPermissionsForVideo());
        binding.btnChooseTrailer.setOnClickListener(v-> requestPermissionsForTrailer());

        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        categoriesViewModel.reload();
        // API request to fetch movie categories
        categoriesViewModel.get().observe(this, categories -> {
            categoryTitles = new ArrayList<>();
            categoryIds = new ArrayList<>();
            for (Category category : categories) {
                categoryTitles.add(category.getName());
                categoryIds.add(category.getId());
            }
            allcategories = categories;
            onCategoriesReceived(allcategories);
        });
    }

    public void onCategoriesReceived(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            Log.e("Categories", "No categories received.");
            return;
        }

        // Set up the adapter and ListView for displaying categories
        CategoryAdapter adapter = new CategoryAdapter(this, categories, false);
        ListView categoryListView = findViewById(R.id.categoryListView);
        categoryListView.setAdapter(adapter);
        categoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        selectedCategories = new ArrayList<>();

        categoryListView.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = categories.get(position);
            boolean isChecked = categoryListView.isItemChecked(position);
            Log.d("Category Clicked", "Category: " + selectedCategory.getName() + ", Checked: " + isChecked);

            if (isChecked) {
                if (selectedCategory.getId() != null) {
                    selectedCategories.add(selectedCategory.getId());
                } else {
                    Log.e("Error", "Category ID is null for category: " + selectedCategory.getName());
                }
            } else {
                selectedCategories.remove(selectedCategory.getId());
            }

        });
    }

    private void createMovie() {
        File imageFile = null;
        File videoFile = null;
        File trailerFile = null;

        String movieName = create_movieNameInput.getText().toString();
        String movieYearText = create_movieYearInput.getText().toString();
        String movieTime = create_movieTimeInput.getText().toString();
        String movieDescription = create_movieDescriptionInput.getText().toString();
        String age = create_age.getText().toString();

        if (movieName.isEmpty() || movieYearText.isEmpty()|| movieTime.isEmpty() || movieDescription.isEmpty()) {
            Toast.makeText(CreateMovieActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
        } else if (selectedImageUri == null || selectedImageUri.isEmpty()) {
            Toast.makeText(CreateMovieActivity.this, "Please select an image for the movie", Toast.LENGTH_LONG).show();
        }
        else {
            try {
                int agemovie = Integer.parseInt(age);
                int movieYear = Integer.parseInt(movieYearText);
                movie = new Movie(movieName,movieYear,movieTime , movieDescription, selectedCategories,agemovie);

            } catch (NumberFormatException e) {
                Toast.makeText(CreateMovieActivity.this, "Please enter a valid year", Toast.LENGTH_SHORT).show();
            }

            if (selectedImageUri == null || selectedImageUri.isEmpty()) {
                Toast.makeText(CreateMovieActivity.this, "You must select an image for the movie!", Toast.LENGTH_LONG).show();
                return;
            }
            imageFile = getFileFromUri(Uri.parse(selectedImageUri));
            if (selectedVideoUri == null || selectedVideoUri.isEmpty()) {
                Toast.makeText(CreateMovieActivity.this, "You must select an image for the movie!", Toast.LENGTH_LONG).show();
                return;
            }
            videoFile = getFileFromUri(Uri.parse(selectedVideoUri));
            if (selectTrailerUri == null || selectTrailerUri.isEmpty()) {
                Toast.makeText(CreateMovieActivity.this, "You must select an image for the movie!", Toast.LENGTH_LONG).show();
                return;
            }
            trailerFile = getFileFromUri(Uri.parse(selectTrailerUri));
            // Add the movie through ViewModel
            movieViewModel.add(movie, imageFile, videoFile, trailerFile);
        }

    }

    private void openVideoChooser() {
        // Open the video chooser to select a video file
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/mp4", "video/avi", "video/mkv"});
        pickVideoLauncher.launch(intent);
    }
    private void openTrailerChooser() {
        // Open the trailer chooser to select a trailer file
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/mp4", "video/avi", "video/mkv"});
        pickTrailerLauncher.launch(intent);
    }
    private void openImageChooser() {
        // Open the image chooser to select an image file
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");  // Filter only image files
        pickImageLauncher.launch(intent); // Waiting for a response from the action
    }
    private void requestPermissionsForVideo() {
        // Request permission to read external storage for video selection
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        } else {
            openVideoChooser();
        }
    }
    private void requestPermissionsForTrailer() {
        // Request permission to read external storage for trailer selection
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        } else {
            openTrailerChooser();
        }
    }
    private ActivityResultLauncher<Intent> pickVideoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri videoUri = result.getData().getData();
                    selectedVideoUri = videoUri.toString();
                    videoCheck.setVisibility(View.VISIBLE);
                }
            });

    private ActivityResultLauncher<Intent> pickTrailerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri videoUri = result.getData().getData();
                    selectTrailerUri = videoUri.toString();
                    trailerCheck.setVisibility(View.VISIBLE);
                }
            });



    private ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData(); // Get the URI of the selected image
                    selectedImageUri = imageUri.toString();// Update the selectedImageUri variable
                    imageCheck.setVisibility(View.VISIBLE);
                }
            });

    private void requestPermissions() {
        // Request permission to read and write external storage for image selection
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
        // Handle permission result
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
        // Convert URI to File using the content resolver
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
