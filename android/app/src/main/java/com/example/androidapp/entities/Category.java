package com.example.androidapp.entities;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
@Entity
public class Category {
    @PrimaryKey (autoGenerate = true)
    private int id;
//    @NonNull
//    @PrimaryKey
    private String categoryName;
//    private List<Movie> movies;

    public Category(String categoryName, List<Movie> movies) {
        this.categoryName = categoryName;
//        this.movies = movies;
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
//    public List<Movie> getMovies() {
//        return movies;
//    }
//
//    public void setMovies(List<Movie> movies) {
//        this.movies = movies;
//    }

//    public String getName() {
//        Log.d("Category", "Getting name: " + categoryName);
//        return categoryName;
//    }
//
//    public void setName(String name) {
//        Log.d("Category", "Setting name: " + name);
//        this.categoryName = name;
//    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    //    public Boolean getPromoted() {
//        return isPromoted;
//    }

//    public void setPromoted(Boolean promoted) {
//        isPromoted = promoted;
//    }

//    public Category(String name, List<Movie> movies, Boolean isPromoted) {
//        this.name = name;
//        this.movies = movies;
//        this.isPromoted = isPromoted;
//    }
//
//    public Category(Category category) {
//        this.name = category.getName();
//        this.movies = movies;
//        this.isPromoted = isPromoted;
//    }

    public Category() {
    }
}

