package com.example.androidapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
@Entity
public class Category {
    @PrimaryKey
    private String name;
    private List<Movie> movies;
    private Boolean isPromoted;

    public Category(String categoryName, List<Movie> movies) {
        this.name = categoryName;
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPromoted() {
        return isPromoted;
    }

    public void setPromoted(Boolean promoted) {
        isPromoted = promoted;
    }

    public Category(String name, List<Movie> movies, Boolean isPromoted) {
        this.name = name;
        this.movies = movies;
        this.isPromoted = isPromoted;
    }
}

