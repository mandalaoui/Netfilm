package com.example.androidapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.R;
import com.example.androidapp.api.RequestApi;
import com.example.androidapp.databinding.ActivityRegisterBinding;
import com.example.androidapp.entities.User;

import java.io.IOException;

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

//                if (profilePictureBase64 == null) {
//                    profilePictureBase64 = getDefaultProfilePictureBase64(); // תבנה פונקציה שתחזיר תמונה דיפולטיבית כ-Base64
//                }
                // Create a User object with the data
                User user = new User(username, password, nickname);

                RequestApi requestApi = new RequestApi(this);
//                userApi.registerUser(user);
                Log.d("Register", "Starting registration request for user: " + user.getUsername());
                requestApi.registerUser(user, new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        Log.d("Register", "Response raw body: " + response.raw().body());

                        if (response.isSuccessful()) {
                            if (response.code() == 201) {
                                Log.d("API_RESPONSE", "User successfully created!");
                                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "User created successfully!", Toast.LENGTH_LONG).show());
                            } else {
                                if (response.body() != null) {
                                    Log.d("API_RESPONSE", "User created: " + response.body());
                                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "User created: " + response.body().getUsername(), Toast.LENGTH_LONG).show());
                                }
                            }
                        } else {

                            try {
                                if (response.errorBody() != null) {
                                    String errorMessage = response.errorBody().string();  // לקרוא את הגוף של השגיאה
                                    Log.e("Register", "Error: " + errorMessage);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("Register", "An error occurred");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("Register", "Registration failed with error: " + t.getMessage());
                        // Logging the error
                        t.printStackTrace();
                        runOnUiThread(() ->Toast.makeText(RegisterActivity.this, "Registration failed: " + t.getMessage(), Toast.LENGTH_SHORT).show());
//                            Toast.makeText(RegisterActivity.this, "Registration failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
//    private String encodeImageToBase64(Uri imageUri) {
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);  // כוונן את סוג הקובץ (JPEG)
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            return Base64.encodeToString(byteArray, Base64.DEFAULT);  // הקוד הופך את התמונה ל-Base64
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    private String getDefaultProfilePictureBase64() {
//        return "your_default_image_base64_string";
//    }
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
}
