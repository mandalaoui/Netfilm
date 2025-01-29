package com.example.androidapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.Category;
import com.example.androidapp.R;
import com.example.androidapp.adapter.CategoryAdapter;
import com.example.androidapp.adapter.MovieAdapter;
import com.example.androidapp.api.RequestApi;
import com.example.androidapp.databinding.ActivityManagmentBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.androidapp.entities.Movie;
import com.google.android.material.switchmaterial.SwitchMaterial;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagmentActivity extends AppCompatActivity {
    private Button createMovieViewButton;
    private Button deleteMovieViewButton;
    private Button createCategoryViewButton;
    private EditText create_movieNameInput;
    private TextView deleteConfirmationText;
    private ActivityManagmentBinding binding;

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
        binding = ActivityManagmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createMovieViewButton = binding.showCreateMovieViewButton;
        deleteMovieViewButton = binding.showDeleteMovieViewButton;
        createCategoryViewButton = binding.showCreateCategoryViewButton;
//        viewFlipper = binding.ViewFlipper;






//        viewFlipper.setDisplayedChild(0);

        createMovieViewButton.setOnClickListener(v -> {
            Intent i = new Intent(ManagmentActivity.this, CreateMovieActivity.class);
            startActivity(i);
        });

//        deleteMovieViewButton.setOnClickListener(v -> {
//            viewFlipper.setDisplayedChild(2);
//        });
//
        createCategoryViewButton.setOnClickListener(v -> {
            Intent i = new Intent(ManagmentActivity.this, CreateCategoryActivity.class);
            startActivity(i);
        });


    }



}

