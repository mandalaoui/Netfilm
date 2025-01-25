package com.example.androidapp;

import java.util.List;
import java.util.Locale;

public class Movie {
    private String _id;
    private String name;
    private String movie_time;
    private int Publication_year;  // שנה
    private String description;

//    private List<Category> categories;
    private String videoUrl;
//    private String imageUrl;

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
//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
//
}
