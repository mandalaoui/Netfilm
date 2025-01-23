package com.example.androidapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidapp.adapters.CategoryAdapter;
import com.example.androidapp.entities.Category;
import com.example.androidapp.entities.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));

        List<Category> categoryList;
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Action", Arrays.asList(new Movie("Movie 1"), new Movie("Movie 2"))));
        categoryList.add(new Category("Comedy", Arrays.asList(new Movie("Movie 3"), new Movie("Movie 4"))));

        final CategoryAdapter categoryAdapter = new CategoryAdapter(this);
        categoryAdapter.setCategories(categoryList);
        recyclerViewCategories.setAdapter(categoryAdapter);
    }
}