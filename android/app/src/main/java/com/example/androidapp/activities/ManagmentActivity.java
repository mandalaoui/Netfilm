package com.example.androidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp.adapters.MovieAdapter;
import com.example.androidapp.databinding.ActivityManagmentBinding;

import java.util.List;

import com.example.androidapp.entities.Movie;

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

        createMovieViewButton.setOnClickListener(v -> {
            Intent i = new Intent(ManagmentActivity.this, CreateMovieActivity.class);
            startActivity(i);
        });

        deleteMovieViewButton.setOnClickListener(v -> {
            Intent i = new Intent(ManagmentActivity.this, DeleteMovieActivity.class);
            startActivity(i);
        });
//
        createCategoryViewButton.setOnClickListener(v -> {
            Intent i = new Intent(ManagmentActivity.this, CreateCategoryActivity.class);
            startActivity(i);
        });

    }

}

