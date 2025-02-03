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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.lifecycle.ViewModelProvider;

import com.example.androidapp.R;
import com.example.androidapp.adapters.CategoryAdapter;
import com.example.androidapp.api.CategoryApi;
import com.example.androidapp.api.UserApi;
import com.example.androidapp.databinding.ActivityEditMovieBinding;
import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;
import com.example.androidapp.viewmodels.MovieViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMovieActivity extends AppCompatActivity {
    private ActivityEditMovieBinding binding;
    private MovieViewModel movieViewModel;
    private EditText movieNameInput, movieTimeInput, movieYearInput, movieDescriptionInput, create_age;
    private ListView categoryListView;
    private String selectedImageUri;
    private String selectedVideoUri, selectTrailerUri;
    private Button btnChooseImage, btnChooseVideo, editMovieButton, btnChooseMovie;
    private Uri imageUri, videoUri;
    private ImageView imageCheck, videoCheck, trailerCheck;
    private String selectedMovieId;
    private Movie selectedMovie;
    private List<String> selectedCategories;
    private List<String> movieTitles, movieIds;
    private List<Movie> allMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        movieNameInput = binding.movieNameInput;
        movieTimeInput = binding.movieTimeInput;
        movieYearInput = binding.movieYearInput;
        movieDescriptionInput = binding.movieDescriptionInput;
        categoryListView = binding.categoryListView;
        btnChooseImage = binding.btnChooseImage;
        btnChooseVideo = binding.btnChooseVideo;
        editMovieButton = binding.editMovieButton;
        btnChooseMovie = binding.chooseMovieButton;
        imageCheck = binding.checkProfilePic;
        videoCheck = binding.checkVideo;
        trailerCheck = binding.checkTrailer;
        create_age = binding.movieAgeInput;

        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.reload();
        movieViewModel.get().observe(this, movies -> {
                    movieTitles = new ArrayList<>();
                    movieIds = new ArrayList<>();
                    for (Movie movie : movies) {
                        movieTitles.add(movie.getName());
                        movieIds.add(movie.get_id());
                    }
                    allMovies = movies;
                });

        editMovieButton.setOnClickListener(v -> editMovieFunc());
        btnChooseMovie.setOnClickListener(v -> showMovieSelectionDialog());
        binding.btnChooseImage.setOnClickListener(v -> requestPermissions());
        binding.btnChooseVideo.setOnClickListener(v -> requestPermissionsForVideo());
        binding.btnChooseTrailer.setOnClickListener(v-> requestPermissionsForTrailer());

        CategoryApi apiRequest = new CategoryApi();
        apiRequest.getCategories(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    onCategoriesReceived(categories);
                } else {
                    Log.e("API Response", "Response error: " + response.message());  // לוג במקרה של בעיה בתשובה
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("API Failure", "Failed to load categories: " + t.getMessage());
            }
        });
    }
    // Show a dialog to select a movie for editing
    private void showMovieSelectionDialog() {
        selectedMovie = new Movie();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose movie")
                .setSingleChoiceItems(movieTitles.toArray(new String[0]), -1, (dialog, which) -> {
                    // כאשר נבחר סרט
                    String selectedMovieName = movieTitles.get(which);
                    selectedMovieId = movieIds.get(which);
                    for (Movie movie : allMovies) {
                        if (movie.get_id().equals(selectedMovieId)) {
                            selectedMovie = movie;
                            break;
                        }
                    }
                    dialog.dismiss();

                })
                .setCancelable(true)
                .show();
    }
    // Handle movie editing functionality
    public void editMovieFunc() {
        File imageFile = null;
        File videoFile = null;
        File trailerFile = null;

        String movieName = movieNameInput.getText().toString();
        String movieYearText = movieYearInput.getText().toString();
        String age = create_age.getText().toString();
        String movieTime = movieTimeInput.getText().toString();
        String movieDescription = movieDescriptionInput.getText().toString();
        int movieYear = Integer.parseInt(movieYearText);
        int agemovie = Integer.parseInt(age);

        Movie movie = new Movie(movieName, movieYear , movieTime, movieDescription, selectedCategories, agemovie);

        if (selectedImageUri == null || selectedImageUri.isEmpty()) {
            Toast.makeText(EditMovieActivity.this, "You must select an image for the movie!", Toast.LENGTH_LONG).show();
            return;
        }
        imageFile = getFileFromUri(Uri.parse(selectedImageUri));
        if (selectedVideoUri == null || selectedVideoUri.isEmpty()) {
            Toast.makeText(EditMovieActivity.this, "You must select an image for the movie!", Toast.LENGTH_LONG).show();
            return;
        }
        videoFile = getFileFromUri(Uri.parse(selectedVideoUri));
        if (selectTrailerUri == null || selectTrailerUri.isEmpty()) {
            Toast.makeText(EditMovieActivity.this, "You must select an image for the movie!", Toast.LENGTH_LONG).show();
            return;
        }
        trailerFile = getFileFromUri(Uri.parse(selectTrailerUri));
        movieViewModel.edit(selectedMovie.get_id(),movie, imageFile, videoFile, trailerFile);
    }
    // Handle category data received from API and populate ListView
    public void onCategoriesReceived(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            Log.e("Categories", "No categories received.");
            return;
        }

        CategoryAdapter adapter = new CategoryAdapter(this, categories, false);
        ListView categoryListView = findViewById(R.id.categoryListView);
        categoryListView.setAdapter(adapter);
        categoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        selectedCategories = new ArrayList<>();

        categoryListView.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = categories.get(position);
            boolean isChecked = categoryListView.isItemChecked(position);

            if (isChecked) {
                if (selectedCategory.getId() != null) {
                    selectedCategories.add(selectedCategory.getId());
                } else {
                    Log.e("Error", "Category ID is null for category: " + selectedCategory.getName());
                }
            } else {
                selectedCategories.remove(selectedCategory.getId());  // אם בוטלה הבחירה, הסר את ה-ID
            }

        });
    }

    private void openVideoChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/mp4", "video/avi", "video/mkv"});
        pickVideoLauncher.launch(intent);
    }
    private void openTrailerChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/mp4", "video/avi", "video/mkv"});
        pickTrailerLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> pickTrailerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri videoUri = result.getData().getData();
                    selectTrailerUri = videoUri.toString();
                    imageCheck.setVisibility(View.VISIBLE);
                }
            });

    private void requestPermissionsForTrailer() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        } else {
            openTrailerChooser();
        }
    }
    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");  // Filter only image files
        pickImageLauncher.launch(intent); // Waiting for a response from the action
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
                    trailerCheck.setVisibility(View.VISIBLE);
                }
            });



    private ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData(); // Get the URI of the selected image
                    selectedImageUri = imageUri.toString();// Update the selectedImageUri variable
                    videoCheck.setVisibility(View.VISIBLE);
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

    // Convert URI to File for uploading
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