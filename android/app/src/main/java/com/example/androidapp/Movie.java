package com.example.androidapp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.Locale;
@Entity(tableName = "movies")

public class Movie {
    @PrimaryKey
    @NonNull
    private String _id;
    private String name;
    private String movie_time;
    private int Publication_year;  // שנה
    private String description;

//    private List<Category> categories;
    private String videoUrl;
    private String image;

    public Movie(String name, String movie_time, int publication_year, String description, String videoUrl, String image) {
        this.name = name;
        this.movie_time = movie_time;
        Publication_year = publication_year;
        this.description = description;
        this.videoUrl = videoUrl;
        this.image = image;
    }

    //
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    // Getters and Setters
    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public Movie(String name,int publication_year, String movie_time, String description) {
        this.name = name;
        this.movie_time = movie_time;
        this.Publication_year = publication_year;
        this.description = description;
    }

    public Movie() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMovie_time() {
        return movie_time;
    }

    public void setMovie_time(String movie_time) {
        this.movie_time = movie_time;
    }

    public int getPublication_year() {
        return Publication_year;
    }

    public void setPublication_year(int Publication_year) {
        this.Publication_year = Publication_year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
//
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
//
}
