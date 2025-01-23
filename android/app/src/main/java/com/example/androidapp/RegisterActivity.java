package com.example.androidapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.api.ApiService;
import com.example.androidapp.api.UserApi;
import com.example.androidapp.databinding.ActivityRegisterBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityRegisterBinding binding;
    String profilePictureBase64 = null;


    private String selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Initialize AppContext with activity context
//        AppContext.initialize(this);


        // Image selection for profile picture
        ImageView imageViewProfilePic = findViewById(R.id.imageViewProfilePic);
        binding.btnChooseImage.setOnClickListener(v -> openImageChooser());

        binding.btnSignIn.setOnClickListener(v -> {
            String username = binding.UserName.getText().toString();
            String password = binding.editPassword.getText().toString();
            String Verifypassword = binding.editVerificationPassword.getText().toString();
            String nickname = binding.editDisplayname.getText().toString();

            if (username.isEmpty() || password.isEmpty() || Verifypassword.isEmpty() || nickname.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
            } else if (!password.equals(Verifypassword)) {
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            } else {

                if (selectedImageUri != null) {
                    uploadImageAndCreateUser(username, password, nickname);
                } else {
                    String profilePictureUrl = getDefaultProfilePictureUrl(); // אם אין תמונה
                    createUser(username, password, nickname, profilePictureUrl);
                }
            }
        });
    }

    private void uploadImageAndCreateUser(String username, String password, String nickname) {
        File imageFile = new File(Uri.parse(selectedImageUri).getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

        UserApi userApi = new UserApi();
        userApi.uploadImage(imagePart, new Callback<ImageResponse>() {
//        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
//        Call<ImageResponse> call = apiService.uploadImage(imagePart);
//        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String imageUrl = response.body().getImageUrl();
                    createUser(username, password, nickname, imageUrl);
                } else {
                    Toast.makeText(RegisterActivity.this, "Image upload failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error uploading image: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createUser(String username, String password, String nickname, String imageUrl) {
        User user = new User(username, password, nickname, imageUrl);
        UserApi userApi = new UserApi();
        userApi.registerUser(user, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User created successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "User creation failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error creating user: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

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
                    selectedImageUri = imageUri.toString(); // Update the selectedImageUri variable
                }
            });

    private String getDefaultProfilePictureUrl() {
        return "https://example.com/default_profile_picture.jpg"; // כאן הכנס את ה-URL של התמונה ברירת המחדל שלך
    }

}
