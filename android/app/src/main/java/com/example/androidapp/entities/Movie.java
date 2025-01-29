package com.example.androidapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;

@Entity (tableName = "movies")
@TypeConverters(ConverterMovie.class)
public class Movie {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;

    private List<String> categories;
    private String movie_time;
    private String image;
    private int Publication_year;
    private String description;
    private int age;
    private String videoUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getMovie_time() {
        return movie_time;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setMovie_time(String movie_time) {
        this.movie_time = movie_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPublication_year() {
        return Publication_year;
    }

    public void setPublication_year(int publication_year) {
        Publication_year = publication_year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAge() {
        return age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }
    @Ignore
    public Movie(String name) {
        this.name = name;
    }

//    public Movie(String id, String name, String movie_time, String image, int publication_year, String description, int age) {
//        this.id = id;
//        this.name = name;
////        this.categories = categories;
//        this.movie_time = movie_time;
//        this.image = image;
//        this.Publication_year = publication_year;
//        this.description = description;
//        this.age = age;
//    }

    public Movie(String name, int publication_year, String movie_time, String description, List<String> categories) {
        this.name = name;
        this.movie_time = movie_time;
        this.description = description;
        Publication_year = publication_year;
        this.categories = categories;
    }
    public Movie() {
    }
}
