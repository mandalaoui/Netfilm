package com.example.androidapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidapp.MovieEditFragment;
import com.example.androidapp.R;
import com.example.androidapp.adapters.CategoryAdapter;
import com.example.androidapp.adapters.MovieAdapter;
import com.example.androidapp.api.UserApi;
import com.example.androidapp.databinding.ActivityDeleteMovieBinding;
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
    private Button editMovie;
    private RecyclerView recyclerViewMovies;

    private MovieAdapter movieAdapter;

    private MovieViewModel movieViewModel;
    private EditText movieNameInput, movieTimeInput, movieYearInput, movieDescriptionInput;
    private ListView categoryListView;
    private ImageView imageViewProfilePic;
    private VideoView videoView;

    private String selectedImageUri;
    private String selectedVideoUri;
    private Button btnChooseImage, btnChooseVideo, editMovieButton, btnChooseMovie;
    private Movie movieChoose;
    private Uri imageUri, videoUri;
    private CreateMovieActivity createMovieActivity;

    private String selectedMovieId;
    private Movie selectedMovie;
    private List<String> selectedCategories;

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
        imageViewProfilePic = binding.imageViewProfilePic;
        videoView = binding.videoView;
        btnChooseImage = binding.btnChooseImage;
        btnChooseVideo = binding.btnChooseVideo;
        editMovieButton = binding.editMovieButton;
        btnChooseMovie = binding.chooseMovieButton;

        editMovieButton.setOnClickListener(v -> {
            editMovieFunc();
        });

        btnChooseMovie.setOnClickListener(v -> {
            showMovieSelectionDialog();
        });

        binding.btnChooseImage.setOnClickListener(v -> {
            requestPermissions();
        });
        binding.btnChooseVideo.setOnClickListener(v -> {
            requestPermissionsForVideo();
        });

        UserApi apiRequest = new UserApi();
        apiRequest.getCategories(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Category> categories = response.body();
                    Log.d("API Response", "Categories received: " + categories.toString());  // לוג לעזרה
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

    private void showMovieSelectionDialog() {
        selectedMovie = new Movie();
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.reload();
        movieViewModel.get().observe(this, movies -> {
            List<String> movieTitles = new ArrayList<>();
            List<String> movieIds = new ArrayList<>();
            for (Movie movie : movies) {
                movieTitles.add(movie.getName());
                movieIds.add(movie.get_id());
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose movie")
                    .setSingleChoiceItems(movieTitles.toArray(new String[0]), -1, (dialog, which) -> {
                        // כאשר נבחר סרט
                        String selectedMovieName = movieTitles.get(which);
                        selectedMovieId = movieIds.get(which);

                        for (Movie movie : movies) {
                            if (movie.get_id().equals(selectedMovieId)) {
                                selectedMovie = movie;
                                break;
                            }
                        }
                        if (selectedMovie != null) {
                            movieNameInput.setText(selectedMovie.getName());
                            movieYearInput.setText(String.valueOf(selectedMovie.getPublication_year()));
                            movieTimeInput.setText(selectedMovie.getMovie_time());
                            movieDescriptionInput.setText(selectedMovie.getDescription());
//                            Uri imageUri = Uri.parse(selectedMovie.getImage()); // המרת ה-String ל-Uri
//                            imageViewProfilePic.setImageURI(imageUri);
//                            Glide.with(this)
//                                    .load(Uri.parse("http://10.0.2.2:12345/api/" + selectedMovie.getImage()))
//                                    .into(imageViewProfilePic);
//                            Uri videoUri = Uri.parse("http://10.0.2.2:12345/api/" + selectedMovie.getVideo());
//                            videoView.setVisibility(View.VISIBLE);
//                            videoView.setVideoURI(videoUri);
//                            videoView.start();

//                            if (selectedMovie.getCategories() != null) {
//                                selectedCategories = selectedMovie.getCategories();
//                            }
                        }

                        Log.d("EditMovieActivity", "Selected movie ID: " + selectedMovieId);
                        dialog.dismiss();

                    })
                    .setCancelable(true)
                    .show();
        });
    }
    public void editMovieFunc() {
        File imageFile = null;
        File videoFile = null;

//        if ( movieNameInput.getText().toString() != null) {
//            selectedMovie.setName(movieNameInput.getText().toString());
//        }
//        if ( movieYearInput.getText().toString() != null) {
//            int movieYear = Integer.parseInt(movieYearInput.getText().toString());
//            selectedMovie.setPublication_year(movieYear);
//        }
//        if ( movieTimeInput.getText().toString() != null) {
//            selectedMovie.setMovie_time(movieTimeInput.getText().toString());
//        }
//        if ( movieDescriptionInput.getText().toString() != null) {
//            selectedMovie.setDescription(movieDescriptionInput.getText().toString());
//        }

//        if (selectedCategories != null) {
//            selectedMovie.setCategories(selectedCategories);
//        }

//
        String movieName = movieNameInput.getText().toString();
        String movieYearText = movieYearInput.getText().toString();
        String movieTime = movieTimeInput.getText().toString();
        String movieDescription = movieDescriptionInput.getText().toString();
        int movieYear = Integer.parseInt(movieYearText);
        Movie movie = new Movie(movieName, movieYear , movieTime, movieDescription, selectedCategories);

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
        movieViewModel.edit(selectedMovieId,movie, imageFile, videoFile);
    }
    public void onCategoriesReceived(List<Category> categories) {
//        public void onCategoriesReceived() {
        if (categories == null || categories.isEmpty()) {
            Log.e("Categories", "No categories received.");
            return;
        }
        for (Category category : categories) {
            Log.d("Category", "Category: " + category.getName() + ", ID: " + category.getId());
        }


//        categoriesViewModel.get().observe(this,categories -> {
//            categoryList = categories;
//        });

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
                }            } else {
                selectedCategories.remove(selectedCategory.getId());  // אם בוטלה הבחירה, הסר את ה-ID
            }

            Log.d("Selected Categories", "Selected IDs: " + selectedCategories);
        });
    }

    private void openVideoChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/mp4", "video/avi", "video/mkv"});
        pickVideoLauncher.launch(intent);
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
                    VideoView videoView = findViewById(R.id.videoView);
                    videoView.setVisibility(View.VISIBLE);
                    videoView.setVideoURI(videoUri);  // הצגת הסרטון ב- VideoView
                    videoView.start();// Save video URI
                }
            });



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