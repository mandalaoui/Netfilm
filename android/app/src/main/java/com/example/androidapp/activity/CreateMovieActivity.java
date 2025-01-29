package com.example.androidapp.activity;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.Category;
import com.example.androidapp.R;
import com.example.androidapp.adapter.CategoryAdapter;
import com.example.androidapp.adapter.MovieAdapter;
import com.example.androidapp.api.RequestApi;
import com.example.androidapp.databinding.ActivityCreateMovieBinding;
import com.example.androidapp.databinding.ActivityManagmentBinding;
import com.example.androidapp.entities.Movie;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateMovieActivity extends AppCompatActivity {
    private ActivityCreateMovieBinding binding;
    private EditText create_movieYearInput;
    private EditText create_movieTimeInput;
    private EditText create_movieDescriptionInput;
    private EditText movieIdInput;
    private Button btnCreateMovie;
    private EditText create_movieNameInput;

    private String selectedImageUri;
    private String selectedVideoUri;
    private Movie movie;

    private List<String> selectedCategories;
    private MovieAdapter moviesAdapter;
    private List<Movie> allMovies;
    private ListView categoryListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateMovieBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        create_movieNameInput = binding.movieNameInput;
        create_movieYearInput = binding.movieYearInput;
        create_movieTimeInput = binding.movieTimeInput;
        create_movieDescriptionInput = binding.movieDescriptionInput;
//        movieIdInput = binding.movieIdInput;
        btnCreateMovie = binding.createMovieButton;
        categoryListView = binding.categoryListView;

        btnCreateMovie.setOnClickListener(v -> {
            createMovie();
        });


        binding.btnChooseImage.setOnClickListener(v -> {
            requestPermissions();
        });
        binding.btnChooseVideo.setOnClickListener(v -> {
            requestPermissionsForVideo();
        });
        RequestApi apiRequest = new RequestApi(this);
        apiRequest.getCategories(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    onCategoriesReceived(categories);
                    Log.d("API Response", "Categories received: " + categories.toString());  // לוג לעזרה
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

    public void onCategoriesReceived(List<Category> categories) {
        if (categories == null || categories.isEmpty()) {
            Log.e("Categories", "No categories received.");
            return;
        }
        for (Category category : categories) {
            Log.d("Category", "Category: " + category.getName() + ", ID: " + category.getId());
        }

        // יצירת ה-Adapter והגדרת ה-ListView
        CategoryAdapter adapter = new CategoryAdapter(this, categories);
        ListView categoryListView = findViewById(R.id.categoryListView);
        categoryListView.setAdapter(adapter);
        categoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // יצירת רשימה עבור ה-IDs שנבחרו
        selectedCategories = new ArrayList<>();

        categoryListView.setOnItemClickListener((parent, view, position, id) -> {
            Category selectedCategory = categories.get(position);
            boolean isChecked = categoryListView.isItemChecked(position);
            Log.d("Category Clicked", "Category: " + selectedCategory.getName() + ", Checked: " + isChecked);

            // אם נבחרה קטגוריה, הוסף את ה-ID שלה לרשימה
            if (isChecked) {
                if (selectedCategory.getId() != null) {
                    selectedCategories.add(selectedCategory.getId());
                } else {
                    Log.e("Error", "Category ID is null for category: " + selectedCategory.getName());
                }            } else {
                selectedCategories.remove(selectedCategory.getId());  // אם בוטלה הבחירה, הסר את ה-ID
            }

            Log.d("Selected Categories", "Selected IDs: " + selectedCategories);
        });
    }

    private void createMovie() {
        File imageFile = null;
        File videoFile = null;

        String movieName = create_movieNameInput.getText().toString();
        String movieYearText = create_movieYearInput.getText().toString();
        String movieTime = create_movieTimeInput.getText().toString();
        String movieDescription = create_movieDescriptionInput.getText().toString();


        if (movieName.isEmpty() || movieYearText.isEmpty()|| movieTime.isEmpty() || movieDescription.isEmpty()) {
            Toast.makeText(CreateMovieActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
        } else if (selectedImageUri == null || selectedImageUri.isEmpty()) {  // אם לא נבחרה תמונה
            Toast.makeText(CreateMovieActivity.this, "Please select an image for the movie", Toast.LENGTH_LONG).show();
        }
        else {
            try {
                int movieYear = Integer.parseInt(movieYearText);
                movie = new Movie(movieName,movieYear,movieTime , movieDescription, selectedCategories);

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
            RequestApi requestApi = new RequestApi(this);
            requestApi.createMovie(movie, imageFile, videoFile);


        }

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
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(videoUri);  // הצגת הסרטון ב- VideoView
                    videoView.start();// Save video URI
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