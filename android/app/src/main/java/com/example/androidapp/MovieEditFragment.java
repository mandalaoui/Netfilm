package com.example.androidapp;

import static android.app.Activity.RESULT_OK;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.androidapp.activities.CreateMovieActivity;
import com.example.androidapp.activities.EditMovieActivity;
import com.example.androidapp.adapters.CategoryAdapter;
import com.example.androidapp.api.UserApi;
import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieEditFragment extends Fragment {
    private EditText movieNameInput, movieTimeInput, movieYearInput, movieDescriptionInput;
    private ListView categoryListView;
    private ImageView imageViewProfilePic;
    private VideoView videoView;

    private String selectedImageUri;
    private String selectedVideoUri;
    private Button btnChooseImage, btnChooseVideo, createMovieButton;
    private Movie movie;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_VIDEO_REQUEST = 2;
    private Uri imageUri, videoUri;

    private List<String> selectedCategories;

    public static MovieEditFragment newInstance(Movie movie) {
        MovieEditFragment fragment = new MovieEditFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", movie);  // העברת פרטי הסרט לעריכה
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.editmovie_layout, container, false);

        movieNameInput = rootView.findViewById(R.id.movieNameInput);
        movieTimeInput = rootView.findViewById(R.id.movieTimeInput);
        movieYearInput = rootView.findViewById(R.id.movieYearInput);
        movieDescriptionInput = rootView.findViewById(R.id.movieDescriptionInput);
        categoryListView = rootView.findViewById(R.id.categoryListView);
        imageViewProfilePic = rootView.findViewById(R.id.imageViewProfilePic);
        videoView = rootView.findViewById(R.id.videoView);
        btnChooseImage = rootView.findViewById(R.id.btnChooseImage);
        btnChooseVideo = rootView.findViewById(R.id.btnChooseVideo);
        createMovieButton = rootView.findViewById(R.id.createMovieButton);

        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("movie");

            // הצגת נתונים של הסרט
            if (movie != null) {
                movieNameInput.setText(movie.getName());
                movieTimeInput.setText(movie.getMovie_time());
                movieYearInput.setText(movie.getPublication_year());
                movieDescriptionInput.setText(movie.getDescription());

                // הצגת תמונה אם יש
                Glide.with(this).load(movie.getImage()).into(imageViewProfilePic);
            }
        }
        btnChooseImage.setOnClickListener(v -> openImageChooser());

        btnChooseVideo.setOnClickListener(v -> openVideoChooser());


        UserApi apiRequest = new UserApi();
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

        createMovieButton.setOnClickListener(v -> {
            File imageFile = null;
            File videoFile = null;
            String newName = movieNameInput.getText().toString();
            String newTime = movieTimeInput.getText().toString();
            String newYear = movieYearInput.getText().toString();
            String newDescription = movieDescriptionInput.getText().toString();
            int movieYear = Integer.parseInt(newYear);

            Movie movie = new Movie(newName, movieYear,newTime, newDescription, selectedCategories);

            if (selectedImageUri == null || selectedImageUri.isEmpty()) {
                Toast.makeText(MyApplication.getAppContext(), "You must select an image for the movie!", Toast.LENGTH_LONG).show();
                return;
            }
            imageFile = getFileFromUri(Uri.parse(selectedImageUri));
            if (selectedVideoUri == null || selectedVideoUri.isEmpty()) {
                Toast.makeText(MyApplication.getAppContext(), "You must select an image for the movie!", Toast.LENGTH_LONG).show();
                return;
            }
            videoFile = getFileFromUri(Uri.parse(selectedVideoUri));


        });
        return rootView;
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

        // יצירת ה-Adapter והגדרת ה-ListView
        CategoryAdapter adapter = new CategoryAdapter(MyApplication.getAppContext(), categories, false);
        categoryListView.setAdapter(adapter);
        categoryListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // יצירת רשימה עבור ה-IDs שנבחרו
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



    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // פתיחת מערכת הקבצים לבחירת סרט
    private void openVideoChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    // קבלת התמונה והסרט שנבחרו
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            if (requestCode == PICK_IMAGE_REQUEST) {
                imageUri = data.getData();
                // הצגת התמונה ב-ImageView
                Glide.with(this).load(imageUri).into(imageViewProfilePic);
            } else if (requestCode == PICK_VIDEO_REQUEST) {
                videoUri = data.getData();
                videoView.setVideoURI(videoUri);
                videoView.start();
            }
        }
    }

    private File getFileFromUri(Uri uri) {
        try {
            String path = null;

            if (uri.getScheme().equals("content")) {
                String[] proj = {MediaStore.Images.Media.DATA};
                ContentResolver resolver = getActivity().getContentResolver();  // עדכון כאן
                Cursor cursor = resolver.query(uri, proj, null, null, null);
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

//    private void openVideoChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("video/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/mp4", "video/avi", "video/mkv"});
//        pickVideoLauncher.launch(intent);
//    }
//    private void openImageChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setType("image/*");  // Filter only image files
//        pickImageLauncher.launch(intent); // Waiting for a response from the action
//    }
//    private void requestPermissionsForVideo() {
//        if (ContextCompat.checkSelfPermission(MyApplication.getAppContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MyApplication.getAppContext(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
//        } else {
//            openVideoChooser();
//        }
//    }
//    private ActivityResultLauncher<Intent> pickVideoLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                    Uri videoUri = result.getData().getData();
//                    selectedVideoUri = videoUri.toString();
//                    videoView.setVisibility(View.VISIBLE);
//                    videoView.setVideoURI(videoUri);  // הצגת הסרטון ב- VideoView
//                    videoView.start();// Save video URI
//                }
//            });
//
//
//
//    private ActivityResultLauncher<Intent> pickImageLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                    Uri imageUri = result.getData().getData(); // Get the URI of the selected image
//                    imageViewProfilePic.setImageURI(imageUri);  // Display the image in the ImageView
//                    selectedImageUri = imageUri.toString();// Update the selectedImageUri variable
//                }
//            });
//
//    private void requestPermissions() {
//        if (ContextCompat.checkSelfPermission(MyApplication.getAppContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                || ContextCompat.checkSelfPermission(MyApplication.getAppContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{
//                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, 100);
//        } else {
//            openImageChooser();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 100) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
//                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(MyApplication.getAppContext(), "Permissions granted!", Toast.LENGTH_SHORT).show();
//                openImageChooser();
//            } else {
//                Toast.makeText(MyApplication.getAppContext(), "Permissions denied!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }