package com.example.androidapp.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.androidapp.R;
import com.example.androidapp.entities.User;
import com.example.androidapp.api.RequestApi;
import com.example.androidapp.databinding.ActivityRegisterBinding;
import com.example.androidapp.entities.User;

import java.io.File;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityRegisterBinding binding;

    private String selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        });
        // Initialize AppContext with activity context
//        AppContext.initialize(this);

        // Image selection for profile picture
        ImageView imageViewProfilePic = findViewById(R.id.imageViewProfilePic);
        binding.btnChooseImage.setOnClickListener(v -> {
            requestPermissions();
        });
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

                File imageFile = null;
                if (selectedImageUri != null && !selectedImageUri.isEmpty()) {
                    imageFile = getFileFromUri(Uri.parse(selectedImageUri));
                }

                if (imageFile == null) {
                    imageFile = new File(getDefaultProfilePictureUrl());
                }

                User user = new User(username, password, nickname);

                RequestApi requestApi = new RequestApi(this);
                requestApi.registerUser(user,imageFile);
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
    private String getDefaultProfilePictureUrl() {
//        try {
//            // מיקום התמונה בתיקיית res/drawable
//            InputStream inputStream = context.getResources().openRawResource(R.drawable.userdefault); // תמונה ברירת מחדל
//            File tempFile = File.createTempFile("defaultProfile", ".jpg", context.getCacheDir());
//
//            // כתיבת התמונה לקובץ
//            FileOutputStream outputStream = new FileOutputStream(tempFile);
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, length);
//            }
//
//            // סגירת הזרמים
//            outputStream.flush();
//            outputStream.close();
//            inputStream.close();
//
//            return tempFile;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
        return "app/src/main/res/drawable/userdefult.jpg";
    }


}
