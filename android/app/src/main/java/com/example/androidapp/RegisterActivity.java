package com.example.androidapp;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView imageViewProfilePic = findViewById(R.id.imageViewProfilePic);
        Button btnChooseImage = findViewById(R.id.btnChooseImage);
        btnChooseImage.setOnClickListener(v -> {
            openImageChooser();  // Open the image selection system
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
                    ImageView imageViewProfilePic = findViewById(R.id.imageViewProfilePic);
                    imageViewProfilePic.setImageURI(imageUri);  // Display the image in the ImageView
                }
            });

}

