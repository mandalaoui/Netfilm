package com.example.androidapp.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import java.util.List;

@Entity
@TypeConverters(ConverterPromotedCategory.class)
public class PromotedCategory {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String categoryName;
    private List<Movie> movies;

    @Ignore
    public PromotedCategory(String categoryName, List<Movie> movies) {
        this.categoryName = categoryName;
//        this.movies = movies;
    }

    @Ignore
    public PromotedCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Ignore
    public PromotedCategory() {
    }

    public PromotedCategory(int id, String categoryName, List<Movie> movies) {
        this.id = id;
        this.categoryName = categoryName;
        this.movies = movies;
    }
}

